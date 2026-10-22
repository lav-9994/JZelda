package Model;

/**
 * Classe astratta che rappresenta uno scudo. Ogni sottoclasse scudo indicherà poi le sue caratteristiche di difesa e viene delegata
 * a loro la scrittura di quelle funzioni, in accordo con lo strategy pattern.

 */
public abstract class Scudo implements Item {

    /** Nome dello scudo. */
    protected String nome;

    /**
     * Costruttore dello scudo.
     *
     * @param nome nome dello scudo
     */
    protected Scudo(String nome) {
        this.nome = nome;
    }

    /**
     * Quanto danno arriva a Link da un colpo diretto, con lo scudo alzato.
     *
     * @param danno il danno di partenza
     * @return il danno che passa
     */
    public abstract int danniDaColpo(int danno);

    /**
     * Quanto danno arriva a Link da un proiettile, con lo scudo alzato.
     *
     * @param danno il danno di partenza
     * @return il danno che passa
     */
    public abstract int danniDaProiettile(int danno);

    /**
     * Restituisce il nome dello scudo.
     *
     * @return il nome dello scudo
     */
    @Override
    public String getNome() { return nome; }

    /**
     * Equipaggia questo scudo su Link.
     *
     * @param link il personaggio su cui applicare l'effetto
     */
    @Override
    public void applicaEffetto(Link link) {
        link.equipaggiaScudo(this);
    }
}
