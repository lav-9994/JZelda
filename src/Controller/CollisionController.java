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
     * Vale solo per chi non passa da chiSullaStrada() e quindi puo'
     * finire davvero addosso a Link: boss in mischia e coniglietti.
     *
     * @param a primo personaggio
     * @param b secondo personaggio
     * @return true se si sovrappongono
     */
    private boolean collidono(Personaggio a, Personaggio b) {
        Rectangle ra = new Rectangle(a.getX(), a.getY(), a.getLarghezza(), a.getAltezza());
        Rectangle rb = new Rectangle(b.getX(), b.getY(), b.getLarghezza(), b.getAltezza());
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
        Rectangle ra = new Rectangle(a.getX(), a.getY(), a.getLarghezza(), a.getAltezza());
        Rectangle rb = new Rectangle(p.getX(), p.getY(), p.getLarghezza(), p.getAltezza());
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
        int w = link.getLarghezza();
        int h = link.getAltezza();
        int portata = GameConstants.TILE_SIZE / 2;
        return switch (link.getDirezione()) {
            case SU       -> new Rectangle(x, y - portata, w, portata);
            case GIU      -> new Rectangle(x, y + h, w, portata);
            case SINISTRA -> new Rectangle(x - portata, y, portata, h);
            case DESTRA   -> new Rectangle(x + w, y, portata, h);
        };
    }

    /**
     * Controlla se l'hitbox di attacco di Link colpisce un personaggio.
     *
     * @param p il personaggio da controllare
     * @return true se Link lo sta colpendo
     */
    private boolean linkStaColpendo(Personaggio p) {
        Rectangle hitbox = new Rectangle(p.getX(), p.getY(), p.getLarghezza(), p.getAltezza());
        return getHitboxAttacco().intersects(hitbox);
    }

    /**
     * Gestisce tutte le collisioni del livello corrente.
     *
     * @param livello     il livello corrente
     * @param coniglietti lista dei coniglietti attivi
     */
    public void gestisciCollisioni(Livello livello, List<Coniglietti> coniglietti) {
        gestisciCollisioniNemici(livello.getNemici());
        gestisciCollisioneBoss(livello.getBoss(), coniglietti);
        gestisciCollisioniConiglietti(coniglietti);
        gestisciRaccoltaOggetti(livello);
    }

    /**
     * Raccoglie le rupie e i cristalli a terra su cui Link sta passando.
     * Qui basta la sovrapposizione normale: gli oggetti non bloccano il
     * passaggio, quindi Link ci finisce davvero sopra e non serve
     * nessun margine di tolleranza.
     *
     * @param livello il livello corrente
     */
    private void gestisciRaccoltaOggetti(Livello livello) {
        Rectangle areaLink = new Rectangle(link.getX(), link.getY(),
                                           link.getLarghezza(), link.getAltezza());

        for (Rupia r : livello.getRupie()) {
            if (areaLink.intersects(r.getArea())) r.raccogli(link);
        }

        for (Cristallo c : livello.getCristalli()) {
            if (areaLink.intersects(c.getArea())) c.raccogli(link);
        }
    }

    /**
     * Gestisce le collisioni tra Link e i nemici base.
     *
     * @param nemici lista dei nemici della stanza corrente
     */
    private void gestisciCollisioniNemici(List<NemicoBase> nemici) {
        for (NemicoBase n : nemici) {
            if (!n.isVivo()) continue;
            if (link.isAttaccando() && linkStaColpendo(n)) {
                n.subisciDanno(link.getDannoTotale());
            }
        }
    }

    /**
     * Controlla se uno spostamento porterebbe il personaggio fuori dalla mappa.
     *
     * @param p  il personaggio che vuole muoversi
     * @param dx spostamento orizzontale in pixel di mondo
     * @param dy spostamento verticale in pixel di mondo
     * @return true se la destinazione e' fuori dai bordi
     */
    public boolean fuoriDallaMappa(Personaggio p, int dx, int dy) {
        int nx = p.getX() + dx;
        int ny = p.getY() + dy;
        return nx < 0 || ny < 0
            || nx + p.getLarghezza()  > GameConstants.MAPPA_W
            || ny + p.getAltezza() > GameConstants.MAPPA_H;
    }

    /**
     * Controlla se lo spostamento porterebbe il personaggio su una tile
     * non calpestabile (es. acqua). Guarda tutte le tile toccate dal
     * rettangolo di destinazione, non solo quella sotto un angolo: cosi'
     * un personaggio piu' grande di una tile non riesce a intrufolarsi
     * con un angolo mentre il resto del corpo e' gia' in acqua.
     *
     * @param p     il personaggio che vuole muoversi
     * @param dx    spostamento orizzontale in pixel di mondo
     * @param dy    spostamento verticale in pixel di mondo
     * @param mappa la matrice di tile del livello corrente
     * @return true se almeno una tile toccata dalla destinazione blocca il passaggio
     */
    public boolean bloccatoDaTile(Personaggio p, int dx, int dy, Tile[][] mappa) {
        int destX = p.getX() + dx;
        int destY = p.getY() + dy;
        int colInizio  = destX / GameConstants.TILE_SIZE;
        int colFine    = (destX + p.getLarghezza() - 1) / GameConstants.TILE_SIZE;
        int rigaInizio = destY / GameConstants.TILE_SIZE;
        int rigaFine   = (destY + p.getAltezza() - 1) / GameConstants.TILE_SIZE;

        for (int riga = rigaInizio; riga <= rigaFine; riga++) {
            for (int col = colInizio; col <= colFine; col++) {
                if (riga < 0 || riga >= GameConstants.MAPPA_TILE_H
                 || col < 0 || col >= GameConstants.MAPPA_TILE_W) continue;
                if (!mappa[riga][col].isCalpestabile()) return true;
            }
        }
        return false;
    }

    /**
     * Restituisce il personaggio che si troverebbe nella posizione di
     * destinazione, cioe' quello contro cui il movimento andrebbe a sbattere.
     * Il controllo e' sulla posizione futura: chi chiama lo usa sia per
     * negare lo spostamento sia per sapere chi e' stato toccato.
     *
     * @param p                il personaggio che vuole muoversi
     * @param dx               spostamento orizzontale in pixel di mondo
     * @param dy               spostamento verticale in pixel di mondo
     * @param altriPersonaggi  personaggi che bloccano il passaggio
     * @return il personaggio sulla strada, oppure null se e' libera
     */
    public Personaggio chiSullaStrada(Personaggio p, int dx, int dy,
            List<Personaggio> altriPersonaggi) {
        Rectangle dest = new Rectangle(p.getX() + dx, p.getY() + dy,
                                       p.getLarghezza(), p.getAltezza());
        for (Personaggio altro : altriPersonaggi) {
            Rectangle ra = new Rectangle(altro.getX(), altro.getY(),
                                         altro.getLarghezza(), altro.getAltezza());
            if (dest.intersects(ra)) return altro;
        }
        return null;
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
