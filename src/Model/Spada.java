package Model;
/**
 * Classe astratta che rappresenta una spada impugnata da link.
 * Definisce il danno base e il nome della spada.
 */

public abstract class Spada implements Item {

    /** Danno inflitto dalla spada. */
    protected int danno;

    /** Nome della spada. */
    protected String nome;
    /**
     * Costruttore della spada.
     *
     * @param nome  nome della spada
     * @param danno danno inflitto
     */
    public Spada(String nome, int danno) {
        this.nome = nome;
        this.danno = danno;
    }
    /**
     * Restituisce il danno inflitto.
     *
     * @return il danno inflitto
     */
    public int getDanno() { return danno; }

    @Override
    public String getNome() { return nome; 
    }
    @Override
    public abstract void applicaEffetto(Link link);


	

}
