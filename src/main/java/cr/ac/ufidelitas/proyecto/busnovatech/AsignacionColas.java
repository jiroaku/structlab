
package cr.ac.ufidelitas.proyecto.busnovatech;

/**
 *
 * @author samim
 */
public class AsignacionColas {

    private NodoCola primero;

    public AsignacionColas() {
        primero = null;
    }

    // Recibe los buses que creó GestionBuses y construye la lista de colas
    public void agregarBus(Bus bus, int cantidadInicial) {
        NodoCola nuevo = new NodoCola(bus, cantidadInicial);

        if (primero == null) {
            primero = nuevo;
        } else {
            NodoCola actual = primero;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
    }

   
    //REGLA DE ASIGNACIÓN
    
    public String asignarTiquete(String tipoTiquete) {

        tipoTiquete = tipoTiquete.toLowerCase();

        // Preferencial
        if (tipoTiquete.equals("preferencial")) {
            NodoCola actual = primero;
            while (actual != null) {
                if (actual.bus.getTipo().equalsIgnoreCase("Preferencial")) {
                    actual.cantidad++;
                    return actual.bus.getIdBus();
                }
                actual = actual.siguiente;
            }
            return "ERROR: No existe bus preferencial.";
        }

        // Directo
        if (tipoTiquete.equals("directo")) {
            NodoCola actual = primero;
            while (actual != null) {
                if (actual.bus.getTipo().equalsIgnoreCase("Directo")) {
                    actual.cantidad++;
                    return actual.bus.getIdBus();
                }
                actual = actual.siguiente;
            }
            return "ERROR: No existe bus directo.";
        }

        // Normal, menor cantidad en cola
        NodoCola actual = primero;
        NodoCola menor = primero;

        while (actual != null) {
            if (actual.cantidad < menor.cantidad) {
                menor = actual;
            }
            actual = actual.siguiente;
        }

        menor.cantidad++;
        return menor.bus.getIdBus();
    }

    // Mostrar estado para Persona C
    public String obtenerEstadoColas() {
        String texto = "=== Estado de las colas ===\n";
        NodoCola actual = primero;

        while (actual != null) {
            texto += actual.bus.getIdBus() + " (" + actual.bus.getTipo() + ") → "
                   + actual.cantidad + " personas\n";
            actual = actual.siguiente;
        }

        return texto;
    }
}
