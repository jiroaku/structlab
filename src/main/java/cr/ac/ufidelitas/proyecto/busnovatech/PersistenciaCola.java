
package cr.ac.ufidelitas.proyecto.busnovatech;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JOptionPane;
/**
 *
 * @author Geral
 */
//Viene de la ColaPrioridad
public class PersistenciaCola {

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
            String menu = "=== GESTIÓN DE TIQUETES ===\n" +
                         "1. Crear nuevo tiquete\n" +
                         "2. Ver cola de tiquetes\n" +
                         "3. Abordar pasajero\n" +
                         "4. Ver historial de atendidos\n" +
                         "5. Guardar tiquetes\n" +
                         "6. Volver al menú principal";

            try {
                opcion = Integer.parseInt(JOptionPane.showInputDialog(menu));
            } catch (NumberFormatException e) {
                opcion = 0;
            }

            switch(opcion) {
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
        } while(opcion != 6);
    }
}
