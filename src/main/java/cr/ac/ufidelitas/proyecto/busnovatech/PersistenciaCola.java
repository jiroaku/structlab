package cr.ac.ufidelitas.proyecto.busnovatech;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Geral
 */
//Viene de la ColaPrioridad
public class PersistenciaCola {

    private static final String ARCHIVO = "colas.txt";

    //Guardamos la cola en JSON
    public void serializarCola(ColaPrioridad cola, String archivo) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(archivo)) {
            NodoTiquete[] datos;
            if (cola != null) {
                datos = cola.exportarTiquetes();
            } else {
                datos = new NodoTiquete[0];
            }
            gson.toJson(datos, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Cargamos la cola desde JSON
    public ColaPrioridad deserializarCola(String archivo) {
        Gson gson = new GsonBuilder().create();
        try (FileReader reader = new FileReader(archivo)) {
            NodoTiquete[] datos = gson.fromJson(reader, NodoTiquete[].class);
            ColaPrioridad cola = new ColaPrioridad();
            cola.importarTiquetes(datos);
            return cola;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Andrew - menú principal de gestión de tiquetes con opciones 1.2
    public void gestionarTiquetes(ColaPrioridad cola, ModuloAtencionTiquetes moduloAtencion) {
        int opcion;
        do {
            String menu = "=== GESTIÓN DE TIQUETES ===\n"
                    + "1. Crear nuevo tiquete\n"
                    + "2. Ver cola de tiquetes\n"
                    + "3. Abordar pasajero\n"
                    + "4. Ver historial de atendidos\n"
                    + "5. Guardar tiquetes\n"
                    + "6. Volver al menú principal";

            try {
                opcion = Integer.parseInt(JOptionPane.showInputDialog(menu));
            } catch (NumberFormatException e) {
                opcion = 0;
            }

            switch (opcion) {
                case 1: {
                    NodoTiquete nuevo = cola.crearTiquete();
                    if (nuevo != null) {
                        serializarCola(cola, "tiquetes.json");
                        JOptionPane.showMessageDialog(null,
                                "Tiquete agregado y guardado.\nUse las opciones 2 o 3 para gestionarlo.",
                                "BusNovaTech - Cola de tiquetes",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
                }
                case 2:
                    cola.mostrarCola();
                    break;
                case 3:
                    if (moduloAtencion != null) {
                        moduloAtencion.atenderDesdeMenu(cola);
                        serializarCola(cola, "tiquetes.json");
                    }
                    break;
                case 4:
                    if (moduloAtencion != null) {
                        moduloAtencion.mostrarHistorial();
                    }
                    break;
                case 5:
                    this.serializarCola(cola, "tiquetes.json");
                    JOptionPane.showMessageDialog(null, "Tiquetes guardados exitosamente");
                    break;
                case 6:
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida");
                    break;
            }
        } while (opcion != 6);
    }

    // Guarda la lista de colas en colas.txt
    public static void guardarColas(AsignacionColas colas) {
        if (colas == null) {
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO))) {
            NodoCola actual = colas.getPrimero();
            while (actual != null) {
                String linea = actual.bus.getIdBus() + "," + actual.cantidad;
                writer.write(linea);
                writer.newLine();
                actual = actual.siguiente;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar colas: " + e.getMessage());
        }
    }

    // Carga colas desde colas.txt y las agrega a AsignacionColas
    public static void cargarColas(AsignacionColas colas, GestionBuses gestionBuses) {
        if (colas == null || gestionBuses == null) {
            return;
        }

        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            JOptionPane.showMessageDialog(null, "Archivo colas.txt no existe, se creará al salir.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length != 2) {
                    continue;
                }

                String busId = partes[0].trim();
                int cantidad = Integer.parseInt(partes[1].trim());

                if (cantidad < 0) {
                    cantidad = 0; 
                }
                Bus bus = gestionBuses.obtenerBusPorId(busId);
                if (bus != null) {
                    colas.agregarBus(bus, cantidad);
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar colas: " + e.getMessage()
                    + "\nSe restablecerá la lista de colas.");
            colas = new AsignacionColas();
        }
    }

}
