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

    // Andrew - menú principal de gestión de tiquetes con opciones 1.2 y 1.3
    public void gestionarTiquetes(ColaPrioridad cola, ModuloAtencionTiquetes moduloAtencion, AsignacionColas colas) {
        int opcion;
        do {
            String menu = "=== GESTIÓN DE TIQUETES ===\n"
                    + "1. Crear nuevo tiquete\n"
                    + "2. Ver cola de tiquetes\n"
                    + "3. Abordar pasajero\n"
                    + "4. Ver historial de atendidos\n"
                    + "5. Ver estado de colas de buses\n"
                    + "6. Guardar tiquetes\n"
                    + "7. Volver al menú principal";

            try {
                opcion = Integer.parseInt(JOptionPane.showInputDialog(menu));
            } catch (NumberFormatException e) {
                opcion = 0;
            }

            switch (opcion) {
                case 1: {
                    NodoTiquete nuevo = cola.crearTiquete();
                    if (nuevo != null && colas != null) {
                        // Integración módulo 1.3 - asignar tiquete a bus automáticamente
                        String busAsignado = colas.asignarTiquete(nuevo.getTipoBus());
                        if (busAsignado != null && !busAsignado.startsWith("ERROR")) {
                            serializarCola(cola, "tiquetes.json");
                            JOptionPane.showMessageDialog(null,
                                    "Tiquete agregado y guardado.\n"
                                    + "Asignado al bus: " + busAsignado + "\n"
                                    + "Use las opciones 2, 3 o 5 para gestionarlo.",
                                    "BusNovaTech - Cola de tiquetes",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            serializarCola(cola, "tiquetes.json");
                            JOptionPane.showMessageDialog(null,
                                    "Tiquete agregado pero hubo un problema con la asignación.\n"
                                    + busAsignado,
                                    "BusNovaTech - Advertencia",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    } else if (nuevo != null) {
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
                        moduloAtencion.atenderDesdeMenu(cola, colas);
                        serializarCola(cola, "tiquetes.json");
                    }
                    break;
                case 4:
                    if (moduloAtencion != null) {
                        moduloAtencion.mostrarHistorial();
                    }
                    break;
                case 5:
                    if (colas != null) {
                        String estado = colas.obtenerEstadoColas();
                        JOptionPane.showMessageDialog(null, estado,
                                "BusNovaTech - Estado de colas",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Sistema de colas no disponible.");
                    }
                    break;
                case 6:
                    this.serializarCola(cola, "tiquetes.json");
                    JOptionPane.showMessageDialog(null, "Tiquetes guardados exitosamente");
                    break;
                case 7:
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida");
                    break;
            }
        } while (opcion != 7);
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

    // Carga colas desde colas.txt y actualiza las cantidades en AsignacionColas
    // Evita duplicados actualizando buses existentes en lugar de agregar nuevos
    public static void cargarColas(AsignacionColas colas, GestionBuses gestionBuses) {
        if (colas == null || gestionBuses == null) {
            return;
        }

        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            // Archivo no existe, se creará al salir con las cantidades iniciales (0)
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) {
                    continue;
                }

                String[] partes = linea.split(",");
                if (partes.length != 2) {
                    continue;
                }

                String busId = partes[0].trim();
                int cantidad;
                try {
                    cantidad = Integer.parseInt(partes[1].trim());
                } catch (NumberFormatException e) {
                    continue; // Saltar líneas con formato inválido
                }

                if (cantidad < 0) {
                    cantidad = 0;
                }

                // Actualizar cantidad del bus existente en lugar de agregar uno nuevo
                colas.actualizarCantidadBus(busId, cantidad);
            }
        } catch (IOException | NumberFormatException e) {
            // Si hay error, las colas quedan con cantidades en 0 (ya inicializadas)
            // No es necesario mostrar error si el archivo está corrupto, simplemente usar valores por defecto
        }
    }

}
