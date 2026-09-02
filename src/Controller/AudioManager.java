package Controller;

import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.*;

/**
 * Gestore audio del gioco. Singleton.
 * Permette di riprodurre effetti sonori e musica in loop.
 */
public class AudioManager {

    /** Istanza unica del Singleton. */
    private static AudioManager instance;

    /** Clip audio per la musica in loop. */
    private Clip clipCorrente;

    /**
     * Costruttore privato (Singleton).
     */
    private AudioManager() {}

    /**
     * Restituisce l'istanza unica di AudioManager.
     *
     * @return istanza di AudioManager
     */
    public static AudioManager getInstance() {
        if (instance == null) instance = new AudioManager();
        return instance;
    }

    /**
     * Riproduce un file audio una volta sola.
     *
     * @param filename nome del file audio (es. "morte_nemico.wav")
     */
    public void play(String filename) {
        try {
            InputStream is = getClass().getResourceAsStream("/audio/" + filename);
            if (is == null) { System.err.println("Audio non trovato: " + filename); return; }
            AudioInputStream audio = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Riproduce un file audio in loop continuo.
     * Ferma l'eventuale musica già in riproduzione.
     *
     * @param filename nome del file audio (es. "dungeon.wav")
     */
    public void loop(String filename) {
        stopMusica();
        try {
            InputStream is = getClass().getResourceAsStream("/audio/" + filename);
            if (is == null) { System.err.println("Audio non trovato: " + filename); return; }
            AudioInputStream audio = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            clipCorrente = AudioSystem.getClip();
            clipCorrente.open(audio);
            clipCorrente.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ferma e chiude la musica in loop corrente.
     */
    public void stopMusica() {
        if (clipCorrente != null && clipCorrente.isRunning()) {
            clipCorrente.stop();
            clipCorrente.close();
        }
    }
}