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
     * Volume della musica in decibel: 0 lascia il file com'è, negativo abbassa.
     */
    private static final float VOLUME_MUSICA = 0.0f;

    /**
     * Volume degli effetti sonori in decibel.
     * I file in /audio sono già normalizzati tutti allo stesso volume medio,
     * cinque decibel sopra la musica: qui basta un unico valore per alzarli
     * o abbassarli in blocco mantenendo l'equilibrio fra loro.
     */
    private static final float VOLUME_EFFETTI = 0.0f;

    /**
     * Regola il volume di una clip già aperta.
     * I controlli esistono solo dopo open(), e non tutte le linee audio
     * li supportano: in quel caso il volume resta quello del file.
     *
     * @param clip     la clip da regolare
     * @param decibel  0 = volume originale, valori negativi abbassano
     */
    private void impostaVolume(Clip clip, float decibel) {
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) return;
        FloatControl guadagno = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float valore = Math.max(guadagno.getMinimum(),
                       Math.min(guadagno.getMaximum(), decibel));
        guadagno.setValue(valore);
    }

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
            clip.addLineListener(evento -> {
                if (evento.getType() == LineEvent.Type.STOP) evento.getLine().close();
            });
            clip.open(audio);
            impostaVolume(clip, VOLUME_EFFETTI);
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
            impostaVolume(clipCorrente, VOLUME_MUSICA);
            clipCorrente.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ferma e chiude la musica in loop corrente.
     */
    public void stopMusica() {
        if (clipCorrente != null) {
            clipCorrente.stop();
            clipCorrente.close();
            clipCorrente = null;
        }
    }
}