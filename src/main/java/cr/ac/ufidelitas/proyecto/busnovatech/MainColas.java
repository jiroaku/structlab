package cr.ac.ufidelitas.proyecto.busnovatech;

import javax.swing.JOptionPane;

/**
 *
 * @author Geral
 */
public class MainColas {

    public static void main(String[] args) {

        //Cargar archivos iniciales
        GestionBuses gestionBuses = new GestionBuses();
        AsignacionColas colas = new AsignacionColas();
        PersistenciaCola.cargarColas(colas, gestionBuses);

        ConfiguracionSistema config = new ConfiguracionSistema("Terminal Central");
        ModuloAtencionTiquetes moduloAtencion = new ModuloAtencionTiquetes(config, gestionBuses);

        ColaPrioridad colaPrioridad = new ColaPrioridad();
        PersistenciaCola persistencia = new PersistenciaCola();
        ColaPrioridad colaCargada = persistencia.deserializarCola("tiquetes.json");
        if (colaCargada != null) {
            colaPrioridad = colaCargada;
        }

        int opcion;

        //Menu princial
        do {
            String menu = """
                --------BUSNOVATECH - SISTEMA DE COLAS--------
                1. Crear nuevo tiquete
                2. Ver colas y estado de buses
                3. Asignar tiquete a un bus
                4. Guardar todo y salir
                """;

            try {
                opcion = Integer.parseInt(JOptionPane.showInputDialog(menu));
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {

                //Crear tiquete

                case 1 -> {
                    NodoTiquete nuevo = colaPrioridad.crearTiquete();
                    if (nuevo != null) {
                        persistencia.serializarCola(colaPrioridad, "tiquetes.json");
                        JOptionPane.showMessageDialog(null,
                                "Tiquete creado correctamente y guardado.");
                    }
                }

                //Ver estado de las colas
                case 2 -> {
                JOptionPane.showMessageDialog(null, "Estado actual de colas:");
                String estado = colas.obtenerEstadoColas();
                JOptionPane.showMessageDialog(null, estado);
                }

                //Asignar tiquetes
                case 3 -> {
                    if (colaPrioridad.estaVacia()) {
                        JOptionPane.showMessageDialog(null,
                                "No hay tiquetes en espera.");
                        break;
                    }

                    moduloAtencion.atenderDesdeMenu(colaPrioridad);
                    persistencia.serializarCola(colaPrioridad, "tiquetes.json");
                }

                //Guardar y salir
                case 4 -> {
                    PersistenciaCola.guardarColas(colas);
                    persistencia.serializarCola(colaPrioridad, "tiquetes.json");
                    JOptionPane.showMessageDialog(null,
                            "Datos guardados correctamente.\nSaliendo del sistema...");
                }

                default -> JOptionPane.showMessageDialog(null, "Opción inválida.");
            }
        } while (opcion != 4);
    }
}