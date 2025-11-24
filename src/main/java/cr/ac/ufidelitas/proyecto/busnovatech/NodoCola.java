
package cr.ac.ufidelitas.proyecto.busnovatech;

/**
 *
 * @author samim
 */
public class NodoCola {
    public Bus bus;          // El bus original
    public int cantidad;     // Cantidad en cola
    public NodoCola siguiente;

    public NodoCola(Bus bus, int cantidad) {
        this.bus = bus;
        this.cantidad = cantidad;
        this.siguiente = null;
    }
}
