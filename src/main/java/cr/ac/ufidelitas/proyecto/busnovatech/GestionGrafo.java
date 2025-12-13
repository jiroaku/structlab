package cr.ac.ufidelitas.proyecto.busnovatech;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JOptionPane;

/**
 *
 * @author jiro
 */
// Gestión del módulo 1.4 - Servicios Complementarios (Grafos)
public class GestionGrafo {

    private GrafoRutas grafo;
    private static final String ARCHIVO = "grafo.json";

    public GestionGrafo() {
        grafo = new GrafoRutas();
        cargarGrafo();
    }

    // Guardar grafo en JSON
    public void guardarGrafo() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(ARCHIVO)) {
            VerticeGrafo[] vertices = grafo.obtenerVertices();
            gson.toJson(vertices, writer);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar grafo: " + e.getMessage());
        }
    }

    // Cargar grafo desde JSON
    private void cargarGrafo() {
        Gson gson = new GsonBuilder().create();
        try (FileReader reader = new FileReader(ARCHIVO)) {
            VerticeGrafo[] vertices = gson.fromJson(reader, VerticeGrafo[].class);
            if (vertices != null && vertices.length > 0) {
                grafo.cargarVertices(vertices);
            }
        } catch (Exception e) {
            // Archivo no existe o hay error
        }
    }

    // Menú principal del módulo de grafos
    public void gestionarGrafo() {
        int opcion;
        do {
            String menu = "=== SERVICIOS COMPLEMENTARIOS (GRAFOS) ===\n"
                    + "1. Ver grafo completo\n"
                    + "2. Agregar localidad\n"
                    + "3. Agregar ruta\n"
                    + "4. Consultar ruta más corta\n"
                    + "5. Volver al menú principal";

            try {
                opcion = Integer.parseInt(JOptionPane.showInputDialog(menu));
            } catch (NumberFormatException e) {
                opcion = 0;
            }

            switch (opcion) {
                case 1:
                    mostrarGrafo();
                    break;
                case 2:
                    agregarLocalidad();
                    break;
                case 3:
                    agregarRuta();
                    break;
                case 4:
                    consultarRutaMasCorta();
                    break;
                case 5:
                    guardarGrafo();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida");
                    break;
            }
        } while (opcion != 5);
    }

    // Mostrar grafo completo
    private void mostrarGrafo() {
        String grafoStr = grafo.imprimir();
        JOptionPane.showMessageDialog(null, grafoStr,
                "BusNovaTech - Grafo de Rutas",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Agregar localidad
    private void agregarLocalidad() {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre de la localidad:");
        if (nombre == null || nombre.trim().isEmpty()) {
            return;
        }

        nombre = nombre.trim();

        // Verificar que no exista una localidad con el mismo nombre
        Localidad[] localidades = grafo.obtenerArrayLocalidades();
        if (localidades != null) {
            for (Localidad loc : localidades) {
                if (loc.getNombre().equalsIgnoreCase(nombre)) {
                    JOptionPane.showMessageDialog(null, "Ya existe una localidad con ese nombre.",
                            "BusNovaTech - Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        // Obtener siguiente ID
        int maxId = 0;
        VerticeGrafo[] vertices = grafo.obtenerVertices();
        if (vertices != null) {
            for (VerticeGrafo v : vertices) {
                if (v.getValor().getId() > maxId) {
                    maxId = v.getValor().getId();
                }
            }
        }

        Localidad nueva = new Localidad(maxId + 1, nombre);
        grafo.agregarVertice(nueva);
        guardarGrafo();
        JOptionPane.showMessageDialog(null, "Localidad agregada exitosamente",
                "BusNovaTech - Grafo", JOptionPane.INFORMATION_MESSAGE);
    }

    // Agregar ruta
    private void agregarRuta() {
        Localidad[] localidades = grafo.obtenerArrayLocalidades();
        if (localidades == null || localidades.length == 0) {
            JOptionPane.showMessageDialog(null, "No hay localidades disponibles. Agregue localidades primero.");
            return;
        }

        if (localidades.length < 2) {
            JOptionPane.showMessageDialog(null, "Se necesitan al menos 2 localidades para crear una ruta.");
            return;
        }

        // Crear array de nombres
        String[] nombres = new String[localidades.length];
        for (int i = 0; i < localidades.length; i++) {
            nombres[i] = localidades[i].getNombre();
        }

        // Seleccionar origen
        int indiceOrigen = JOptionPane.showOptionDialog(
                null,
                "Seleccione la localidad de origen:",
                "BusNovaTech - Agregar Ruta - Origen",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                nombres,
                nombres[0]);

        if (indiceOrigen == JOptionPane.CLOSED_OPTION || indiceOrigen < 0) {
            return;
        }

        Localidad origen = localidades[indiceOrigen];

        // Seleccionar destino
        int indiceDestino = JOptionPane.showOptionDialog(
                null,
                "Seleccione la localidad de destino:",
                "BusNovaTech - Agregar Ruta - Destino",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                nombres,
                nombres[0]);

        if (indiceDestino == JOptionPane.CLOSED_OPTION || indiceDestino < 0) {
            return;
        }

        Localidad destino = localidades[indiceDestino];

        // Ingresar peso
        String pesoStr = JOptionPane.showInputDialog("Ingrese el peso de la ruta:");
        if (pesoStr == null) return;
        
        try {
            int peso = Integer.parseInt(pesoStr.trim());
            if (peso < 0) {
                JOptionPane.showMessageDialog(null, "El peso debe ser positivo");
                return;
            }

            grafo.agregarArista(origen, destino, peso);
            guardarGrafo();
            JOptionPane.showMessageDialog(null,
                    "Ruta agregada: " + origen.getNombre() + " -> " + destino.getNombre() + " (peso: " + peso + ")",
                    "BusNovaTech - Grafo", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Peso inválido");
        }
    }

    // Consultar ruta más corta
    private void consultarRutaMasCorta() {
        Localidad[] localidades = grafo.obtenerArrayLocalidades();
        if (localidades == null || localidades.length == 0) {
            JOptionPane.showMessageDialog(null, "No hay localidades disponibles");
            return;
        }

        // Crear array de nombres para el diálogo
        String[] nombres = new String[localidades.length];
        for (int i = 0; i < localidades.length; i++) {
            nombres[i] = localidades[i].getNombre();
        }

        // Seleccionar origen
        int indiceOrigen = JOptionPane.showOptionDialog(
                null,
                "Seleccione la localidad de origen:",
                "BusNovaTech - Ruta Más Corta - Origen",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                nombres,
                nombres[0]);

        if (indiceOrigen == JOptionPane.CLOSED_OPTION || indiceOrigen < 0) {
            return;
        }

        Localidad origen = localidades[indiceOrigen];

        // Seleccionar destino
        int indiceDestino = JOptionPane.showOptionDialog(
                null,
                "Seleccione la localidad de destino:",
                "BusNovaTech - Ruta Más Corta - Destino",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                nombres,
                nombres[0]);

        if (indiceDestino == JOptionPane.CLOSED_OPTION || indiceDestino < 0) {
            return;
        }

        Localidad destino = localidades[indiceDestino];

        String ruta = grafo.rutaMasCorta(origen, destino);
        JOptionPane.showMessageDialog(null, ruta,
                "BusNovaTech - Ruta Más Corta",
                JOptionPane.INFORMATION_MESSAGE);
    }
}

