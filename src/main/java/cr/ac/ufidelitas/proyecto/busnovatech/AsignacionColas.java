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

    //REGLA DE ASIGNACIÓN - acepta códigos P/D/N o nombres completos
    public String asignarTiquete(String tipoTiquete) {
        if (tipoTiquete == null) {
            return "ERROR: Tipo de tiquete inválido.";
        }

        String tipo = tipoTiquete.trim().toLowerCase();

        // Convertir códigos a nombres para comparación
        String tipoBusBuscado = null;
        if (tipo.equals("p") || tipo.startsWith("preferencial")) {
            tipoBusBuscado = "Preferencial";
        } else if (tipo.equals("d") || tipo.startsWith("directo")) {
            tipoBusBuscado = "Directo";
        } else if (tipo.equals("n") || tipo.startsWith("normal")) {
            tipoBusBuscado = "Normal";
        } else {
            return "ERROR: Tipo de tiquete inválido.";
        }

        // Preferencial o Directo - asignar al único bus del tipo
        if (tipoBusBuscado.equals("Preferencial") || tipoBusBuscado.equals("Directo")) {
            NodoCola actual = primero;
            while (actual != null) {
                if (actual.bus.getTipo().equalsIgnoreCase(tipoBusBuscado)) {
                    actual.cantidad++;
                    return actual.bus.getIdBus();
                }
                actual = actual.siguiente;
            }
            return "ERROR: No existe bus " + tipoBusBuscado.toLowerCase() + ".";
        }

        // Normal - asignar al bus con menor cantidad en cola
        if (primero == null) {
            return "ERROR: No hay buses disponibles.";
        }

        NodoCola actual = primero;
        NodoCola menor = primero;

        while (actual != null) {
            // Solo considerar buses normales
            if (actual.bus.getTipo().equalsIgnoreCase("Normal")) {
                if (actual.cantidad < menor.cantidad || !menor.bus.getTipo().equalsIgnoreCase("Normal")) {
                    menor = actual;
                }
            }
            actual = actual.siguiente;
        }

        // Verificar que se encontró un bus normal
        if (!menor.bus.getTipo().equalsIgnoreCase("Normal")) {
            return "ERROR: No hay buses normales disponibles.";
        }

        menor.cantidad++;
        return menor.bus.getIdBus();
    }

    // Decrementa la cantidad en la cola del bus cuando se atiende un tiquete
    public void decrementarCola(String busId) {
        if (busId == null) {
            return;
        }
        NodoCola actual = primero;
        while (actual != null) {
            if (actual.bus.getIdBus().equalsIgnoreCase(busId)) {
                if (actual.cantidad > 0) {
                    actual.cantidad--;
                }
                return;
            }
            actual = actual.siguiente;
        }
    }

    // Mostrar estado para Gerald
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

    // Actualiza la cantidad de un bus existente por su ID
    public void actualizarCantidadBus(String busId, int cantidad) {
        if (busId == null) {
            return;
        }
        NodoCola actual = primero;
        while (actual != null) {
            if (actual.bus.getIdBus().equalsIgnoreCase(busId)) {
                if (cantidad < 0) {
                    actual.cantidad = 0;
                } else {
                    actual.cantidad = cantidad;
                }
                return;
            }
            actual = actual.siguiente;
        }
    }

    //Luna
    public NodoCola getPrimero() {
        return primero;
    }
}
