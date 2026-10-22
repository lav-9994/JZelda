package Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;

/**
 * Classe che gestisce i profili utente salvati su file CSV.
 * Formato CSV: nickname,avatar,giocate,vinte,perse
 */
public class Profilo extends Observable {
	
	/** Percorso del file CSV. */
    private final Path filePath;
    
    /**
     * Costruttore di Profilo.
     *
     * @param fileName percorso del file CSV
     */
    public Profilo(String fileName) {
        this.filePath = Paths.get(fileName);
        try {
            Files.createDirectories(filePath.getParent());
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Aggiunge un nuovo profilo al CSV se il nickname non esiste già.
     *
     * @param nickname nome del giocatore
     * @param avatar   percorso dell'avatar scelto
     */
    public void aggiungiProfilo(String nickname, String avatar) {
        if (getProfili().stream().anyMatch(p -> p.getNickname().equals(nickname))) return;
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)) {
            writer.write(nickname + "," + avatar + ",0,0,0");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers(Evento.PROFILO);
    }
    
    /**
     * Aggiorna le statistiche del profilo a fine partita.
     *
     * @param nickname nome del giocatore
     * @param vinta    true se la partita è stata vinta
     */
    public void aggiornaStatistiche(String nickname, boolean vinta) {
        try {
            List<String> lines = Files.readAllLines(filePath);
            List<String> updated = new ArrayList<>();
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 5 && parts[0].equals(nickname)) {
                    int giocate = Integer.parseInt(parts[2]) + 1;
                    int vinte = Integer.parseInt(parts[3]) + (vinta ? 1 : 0);
                    int perse = Integer.parseInt(parts[4]) + (vinta ? 0 : 1);
                    updated.add(parts[0] + "," + parts[1] + "," + giocate + "," + vinte + "," + perse);
                } else {
                    updated.add(line);
                }
            }
            Files.write(filePath, updated);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers(Evento.STATISTICHE);
    }
    /**
     * Legge tutti i profili dal CSV.
     *
     * @return lista di ProfiloEntry
     */
    public List<ProfiloEntry> getProfili() {
        List<ProfiloEntry> profili = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) { 
                	profili.add(new ProfiloEntry(
                		parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3]),
                        Integer.parseInt(parts[4])
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return profili;
    }
    /**
     * Restituisce i profili ordinati per partite vinte in ordine decrescente.
     *
     * @return lista ordinata di ProfiloEntry
     */
    public List<ProfiloEntry> getProfiliOrdinati() {
        return getProfili().stream()
                .sorted((a, b) -> Integer.compare(b.getVinte(), a.getVinte()))
                .collect(Collectors.toList());
    }
    /**
     * Classe interna che rappresenta una riga del CSV.
     */
    public static class ProfiloEntry {

        /** Nome del giocatore. */
        private final String nickname;

        /** Percorso dell'avatar. */
        private final String avatar;

        /** Numero di partite giocate. */
        private final int giocate;

        /** Numero di partite vinte. */
        private final int vinte;

        /** Numero di partite perse. */
        private final int perse;
        /**
         * Costruttore di ProfiloEntry.
         *
         * @param nickname nome del giocatore
         * @param avatar   percorso dell'avatar
         * @param giocate  partite giocate
         * @param vinte    partite vinte
         * @param perse    partite perse
         */
        public ProfiloEntry(String nickname, String avatar, int giocate, int vinte, int perse) {
            this.nickname = nickname;
            this.avatar = avatar;
            this.giocate = giocate;
            this.vinte = vinte;
            this.perse = perse;
        }
        /**
         * Restituisce il nickname del profilo.
         *
         * @return il nickname del profilo
         */
        public String getNickname() { return nickname; }
        /**
         * Restituisce il file dell'avatar scelto.
         *
         * @return il file dell'avatar scelto
         */
        public String getAvatar() { return avatar; }
        /**
         * Restituisce il numero di partite giocate.
         *
         * @return il numero di partite giocate
         */
        public int getGiocate() { return giocate; }
        /**
         * Restituisce il numero di partite vinte.
         *
         * @return il numero di partite vinte
         */
        public int getVinte() { return vinte; }
        /**
         * Restituisce il numero di partite perse.
         *
         * @return il numero di partite perse
         */
        public int getPerse() { return perse; }
    }

    





}