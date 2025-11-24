
package cr.ac.ufidelitas.proyecto.busnovatech;

/**
 *
 * @author samim
 */
import javax.swing.*;

public class GestionBuses {

    private NodoBus primero; // cabeza de la lista

    public GestionBuses() {
        primero = null;
    }

    public GestionBuses(ConfiguracionSistema config) {
        primero = null;
        // Crear buses basándose en la configuración del sistema
        if (config != null) {
            crearBusesDesdeConfiguracion(config);
        }
    }

    public void configurarBuses(ConfiguracionSistema config) {
        if (config == null) {
            JOptionPane.showMessageDialog(null, "No hay configuración disponible.");
            return;
        }

        // Limpiar buses existentes
        primero = null;

        // Crear buses basándose en la configuración del sistema
        crearBusesDesdeConfiguracion(config);

        JOptionPane.showMessageDialog(null, "Buses configurados correctamente según la configuración del sistema.");
    }

    private void crearBusesDesdeConfiguracion(ConfiguracionSistema config) {
        int busesPreferenciales = config.getBusesPreferenciales();
        int busesDirectos = config.getBusesDirectos();
        int busesNormales = config.getBusesNormales();

        // Crear buses basados en la configuración del sistema
        // Buses preferenciales
        for (int i = 1; i <= busesPreferenciales; i++) {
            insertarBus(new Bus("P" + i, "Preferencial"));
        }

        // Buses directos
        for (int i = 1; i <= busesDirectos; i++) {
            insertarBus(new Bus("D" + i, "Directo"));
        }

        // Buses normales
        for (int i = 1; i <= busesNormales; i++) {
            insertarBus(new Bus("N" + i, "Normal"));
        }
    }





    private void insertarBus(Bus nuevoBus) {
        NodoBus nuevo = new NodoBus(nuevoBus);
        if (primero == null) {
            primero = nuevo;
        } else {
            NodoBus actual = primero;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
    }


    public void cargarBusesDesdeConfig() {
        // Cargar configuración usando ConfiguracionSistema
        ConfiguracionSistema gestorConfig = new ConfiguracionSistema();
        if (gestorConfig.existeConfiguracion()) {
            ConfiguracionSistema config = gestorConfig.cargarConfiguracion();
            if (config != null) {
                crearBusesDesdeConfiguracion(config);
            }
        }
    }

    public void mostrarBuses() {
        if (primero == null) {
            JOptionPane.showMessageDialog(null, "No hay buses registrados.");
            return;
        }

        String texto = "Lista de buses registrados:\n\n";
        NodoBus actual = primero;
        while (actual != null) {
            texto += "ID: " + actual.bus.getIdBus() +
                     " | Tipo: " + actual.bus.getTipo() +
                     " | Estado: " + actual.bus.getEstado() + "\n";
            actual = actual.siguiente;
        }
        JOptionPane.showMessageDialog(null, texto);
    }

    // Andrew - busca y retorna un bus disponible del tipo solicitado
    public Bus obtenerBusDisponiblePorTipo(String tipoBus) {
        if (tipoBus == null) {
            return null;
        }
        NodoBus actual = primero;
        while (actual != null) {
            Bus candidato = actual.bus;
            if (candidato != null
                    && candidato.getTipo().equalsIgnoreCase(tipoBus)
                    && "Disponible".equalsIgnoreCase(candidato.getEstado())) {
                return candidato;
            }
            actual = actual.siguiente;
        }
        return null;
    }
}