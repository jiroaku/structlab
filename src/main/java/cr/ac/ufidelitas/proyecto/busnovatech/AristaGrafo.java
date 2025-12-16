package cr.ac.ufidelitas.proyecto.busnovatech;

/**
 *
 * @author gerald
 */
// Arista con peso para el grafo ponderado
public class AristaGrafo {

    private Localidad destino;
    private int peso;

    public AristaGrafo() {
    }

    public AristaGrafo(Localidad destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }

    public Localidad getDestino() {
        return destino;
    }

    public void setDestino(Localidad destino) {
        this.destino = destino;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

}

