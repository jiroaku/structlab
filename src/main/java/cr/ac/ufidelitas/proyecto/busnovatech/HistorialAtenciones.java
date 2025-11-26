package cr.ac.ufidelitas.proyecto.busnovatech;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JOptionPane;

/**
 *
 * @author @jiroaku
 */
// Andrew - historial de tiquetes atendidos con lista enlazada y JSON
public class HistorialAtenciones {

    private Nodo<RegistroAtencion> primero;
    private static final String ARCHIVO = "atendidos.json";

    // Constructor que carga historial desde JSON al iniciar
    public HistorialAtenciones() {
        cargarHistorial();
    }

    // Agrega nuevo registro al historial y guarda en JSON
    public void agregarRegistro(RegistroAtencion registro) {
        if (registro == null) {
            return;
        }
        Nodo<RegistroAtencion> nuevo = new Nodo<>(registro);
        anexarNodo(nuevo);
        guardarHistorial();
    }

    // Carga historial desde atendidos.json al iniciar
    private void cargarHistorial() {
        try {
            File archivo = new File(ARCHIVO);
            if (!archivo.exists()) {
                primero = null;
                return;
            }
            Gson gson = new GsonBuilder().create();
            try (FileReader reader = new FileReader(archivo)) {
                RegistroAtencion[] registros = gson.fromJson(reader, RegistroAtencion[].class);
                primero = null;
                if (registros != null) {
                    for (int i = 0; i < registros.length; i++) {
                        Nodo<RegistroAtencion> nuevo = new Nodo<>(registros[i]);
                        anexarNodo(nuevo);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar atendidos: " + e.getMessage());
        }
    }

    // Guarda historial completo en atendidos.json
    private void guardarHistorial() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(ARCHIVO)) {
                RegistroAtencion[] registros = convertirAArreglo();
                gson.toJson(registros, writer);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo guardar atendidos: " + e.getMessage());
        }
    }

    // Agrega nodo al final de la lista enlazada
    private void anexarNodo(Nodo<RegistroAtencion> nodo) {
        if (nodo == null) {
            return;
        }
        nodo.setSiguiente(null);
        if (primero == null) {
            primero = nodo;
            return;
        }
        Nodo<RegistroAtencion> actual = primero;
        while (actual.getSiguiente() != null) {
            actual = actual.getSiguiente();
        }
        actual.setSiguiente(nodo);
    }

    // Convierte lista enlazada a arreglo para serialización JSON
    private RegistroAtencion[] convertirAArreglo() {
        int tamano = contarNodos();
        RegistroAtencion[] registros = new RegistroAtencion[tamano];
        Nodo<RegistroAtencion> actual = primero;
        int indice = 0;
        while (actual != null) {
            registros[indice] = actual.getDato();
            indice++;
            actual = actual.getSiguiente();
        }
        return registros;
    }

    // Cuenta nodos de la lista para determinar tamaño
    private int contarNodos() {
        int contador = 0;
        Nodo<RegistroAtencion> actual = primero;
        while (actual != null) {
            contador++;
            actual = actual.getSiguiente();
        }
        return contador;
    }

    // Muestra todos los registros de atención en JOptionPane
    public void mostrarHistorial() {
        if (primero == null) {
            JOptionPane.showMessageDialog(null, "No existen tiquetes atendidos aún.");
            return;
        }
        StringBuilder sb = new StringBuilder("=== Tiquetes atendidos ===\n\n");
        Nodo<RegistroAtencion> actual = primero;
        while (actual != null) {
            RegistroAtencion registro = actual.getDato();
            sb.append("Pasajero: ").append(registro.getNombrePasajero()).append("\n")
              .append("ID: ").append(registro.getIdPasajero()).append("\n")
              .append("Servicio: ").append(registro.getServicio()).append("\n")
              .append("Bus: ").append(registro.getBusAsignado()).append("\n")
              .append("Tipo Bus: ").append(registro.getTipoBus()).append("\n")
              .append("Terminal: ").append(registro.getTerminal()).append("\n")
              .append("Hora atención: ").append(registro.getHoraAtencion()).append("\n")
              .append("Monto cobrado: ").append(registro.getMontoCobrado()).append("\n")
              .append("----------------------------------\n");
            actual = actual.getSiguiente();
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }
}

