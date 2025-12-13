package cr.ac.ufidelitas.proyecto.busnovatech;

/**
 *
 * @author jiro
 */
// Vértice del grafo con adyacentes usando arrays
public class VerticeGrafo {

    private Localidad valor;
    private AristaGrafo[] adyacentes;
    private int cantidadAristas;

    public VerticeGrafo() {
        this.adyacentes = new AristaGrafo[10];
        this.cantidadAristas = 0;
    }

    public VerticeGrafo(Localidad valor) {
        this.valor = valor;
        this.adyacentes = new AristaGrafo[10];
        this.cantidadAristas = 0;
    }

    public Localidad getValor() {
        return valor;
    }

    public void setValor(Localidad valor) {
        this.valor = valor;
    }

    public AristaGrafo[] getAdyacentes() {
        return adyacentes;
    }

    public void setAdyacentes(AristaGrafo[] adyacentes) {
        this.adyacentes = adyacentes;
        if (adyacentes != null) {
            this.cantidadAristas = adyacentes.length;
        }
    }

    public int getCantidadAristas() {
        return cantidadAristas;
    }

    // Agregar arista
    public void agregarArista(AristaGrafo arista) {
        if (cantidadAristas >= adyacentes.length) {
            // Redimensionar array
            AristaGrafo[] nuevo = new AristaGrafo[adyacentes.length * 2];
            for (int i = 0; i < cantidadAristas; i++) {
                nuevo[i] = adyacentes[i];
            }
            adyacentes = nuevo;
        }
        adyacentes[cantidadAristas] = arista;
        cantidadAristas++;
    }

}
