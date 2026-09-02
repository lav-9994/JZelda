package Controller;
import Model.*;
import Config.GameConstants;
import java.awt.Rectangle;
import java.util.List;

/**
 * Gestisce le collisioni tra i personaggi del gioco.
 */
public class CollisionController {

    /** Riferimento a Link. */
    private Link link;

    /**
     * Costruttore di CollisionController.
     *
     * @param link il personaggio principale
     */
    public CollisionController(Link link) {
        this.link = link;
    }

    /**
     * Controlla se due personaggi si sovrappongono.
     *
     * @param a primo personaggio
     * @param b secondo personaggio
     * @return true se collidono
     */
    private boolean collidono(Personaggio a, Personaggio b) {
        Rectangle ra = new Rectangle(a.getX(), a.getY(), a.getWidth(), a.getHeight());
        Rectangle rb = new Rectangle(b.getX(), b.getY(), b.getWidth(), b.getHeight());
        return ra.intersects(rb);
    }

    /**
     * Controlla se un personaggio collide con un proiettile.
     *
     * @param a il personaggio
     * @param p il proiettile
     * @return true se collidono
     */
    private boolean collidonoProiettile(Personaggio a, Proiettile p) {
        Rectangle ra = new Rectangle(a.getX(), a.getY(), a.getWidth(), a.getHeight());
        Rectangle rb = new Rectangle(p.getX(), p.getY(), p.getWidth(), p.getHeight());
        return ra.intersects(rb);
    }

    /**
     * Restituisce il rettangolo di attacco di Link in base alla direzione corrente.
     *
     * @return rettangolo dell'hitbox di attacco
     */
    private Rectangle getHitboxAttacco() {
        int x = link.getX();
        int y = link.getY();
        int w = link.getWidth();
        int h = link.getHeight();
        return switch (link.getDirezione()) {
            case SU       -> new Rectangle(x, y - h, w, h);
            case GIU      -> new Rectangle(x, y + h, w, h);
            case SINISTRA -> new Rectangle(x - w, y, w, h);
            case DESTRA   -> new Rectangle(x + w, y, w, h);
        };
    }

    /**
     * Controlla se l'hitbox di attacco di Link colpisce un personaggio.
     *
     * @param p il personaggio da controllare
     * @return true se Link lo sta colpendo
     */
    private boolean linkStaColpendo(Personaggio p) {
        Rectangle hitbox = new Rectangle(p.getX(), p.getY(), p.getWidth(), p.getHeight());
        return getHitboxAttacco().intersects(hitbox);
    }

    /**
     * Gestisce tutte le collisioni del livello corrente.
     *
     * @param livello     il livello corrente
     * @param coniglietti lista dei coniglietti attivi
     */
    public void gestisciCollisioni(Livello livello, List<Coniglietti> coniglietti) {
        gestisciCollisioniNemici(livello.getNemiciStanzaCorrente());
        if (livello.getStanzaCorrente() == 2) {
            gestisciCollisioneBoss(livello.getBoss(), coniglietti);
        }
        gestisciCollisioniConiglietti(coniglietti);
    }

    /**
     * Gestisce le collisioni tra Link e i nemici base.
     *
     * @param nemici lista dei nemici della stanza corrente
     */
    private void gestisciCollisioniNemici(List<NemicoBase> nemici) {
        for (NemicoBase n : nemici) {
            if (!n.isVivo()) continue;
            if (collidono(link, n)) {
                n.infliggiDanno(link);
            }
            if (link.isAttaccando() && linkStaColpendo(n)) {
                n.subisciDanno(link.getDannoTotale());
            }
        }
    }
    public boolean puoMuoversi(Personaggio p, int dx, int dy,
            List<Rectangle> ostacoli, List<Personaggio> altriPersonaggi) {
        int nx = p.getX() + dx;
        int ny = p.getY() + dy;
        Rectangle dest = new Rectangle(nx, ny, p.getWidth(), p.getHeight());

        if (nx < 0 || ny < 0 ||
            nx + p.getWidth()  > GameConstants.MAPPA_W ||
            ny + p.getHeight() > GameConstants.MAPPA_H) return false;

        for (Rectangle o : ostacoli)
            if (dest.intersects(o)) return false;

        for (Personaggio altro : altriPersonaggi) {
            Rectangle ra = new Rectangle(altro.getX(), altro.getY(), altro.getWidth(), altro.getHeight());
            if (dest.intersects(ra)) return false;
        }
        return true;
    }

    /**
     * Gestisce le collisioni tra Link e il boss.
     *
     * @param boss        il boss del livello
     * @param coniglietti lista dei coniglietti attivi
     */
    private void gestisciCollisioneBoss(Nemico boss, List<Coniglietti> coniglietti) {
        if (boss == null || !boss.isVivo()) return;

        if (boss instanceof BossMischia bm) {
            if (collidono(link, boss)) bm.attacca(link);
        }

        if (boss instanceof BossDistanza bd) {
            for (Proiettile p : bd.getProiettili()) {
                if (p.isAttivo() && collidonoProiettile(link, p)) {
                    p.infliggiDanno(link);
                }
            }
        }

        if (link.isAttaccando() && linkStaColpendo(boss)) {
            boss.subisciDanno(link.getDannoTotale());
        }
    }

    /**
     * Gestisce le collisioni tra Link e i coniglietti.
     *
     * @param coniglietti lista dei coniglietti attivi
     */
    private void gestisciCollisioniConiglietti(List<Coniglietti> coniglietti) {
        for (Coniglietti c : coniglietti) {
            if (c.isAttivo() && c.isSpeciale() && collidono(link, c)) {
                c.interagisciConLink(link);
            }
        }
    }
}
