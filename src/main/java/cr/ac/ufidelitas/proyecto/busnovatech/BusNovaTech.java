package cr.ac.ufidelitas.proyecto.busnovatech;

import javax.swing.JOptionPane;

/**
 *
 * @author Luna
 */
//PUSE LA VERSION 2.8.9 DE GSON COMO EL PROFE
public class BusNovaTech {

    public static void main(String[] args) {

        JOptionPane.showMessageDialog(null,
                "************ Bienvenido al sistema BusNovaTech ***************\n"
                + "\nSolución inteligente para la gestión de terminales y servicios de buses.\n",
                "BusNovaTech", JOptionPane.INFORMATION_MESSAGE);

        // Configuración inicial del sistema
        ConfiguracionSistema gestorConfig = new ConfiguracionSistema();
        ConfiguracionSistema config = null;

        if (gestorConfig.existeConfiguracion()) {
            config = gestorConfig.cargarConfiguracion();
            if (config == null) {
                config = gestorConfig.ejecutarConfiguracionInicial();
            }
        } else {
            config = gestorConfig.ejecutarConfiguracionInicial();
        }

        if (config != null) {
            gestorConfig.guardarConfiguracion(config);
        }

        GestionBuses gestorBuses = new GestionBuses(config);
        PersistenciaCola persistencia = new PersistenciaCola();
        // Andrew - crea instancia del módulo 1.2 para atención de tiquetes
        ModuloAtencionTiquetes moduloAtencion = new ModuloAtencionTiquetes(config, gestorBuses);

        // Luna - crea una instancia del módulo 1.3 de llenado de colas
        AsignacionColas colas = new AsignacionColas();

        // Llenar colas con los buses existentes (inicialmente con cantidad 0)
        NodoBus actual = gestorBuses.getPrimero();
        while (actual != null) {
            colas.agregarBus(actual.bus, 0);
            actual = actual.siguiente;
        }

        // Cargar colas desde colas.txt si existe (restaura cantidades previas)
        PersistenciaCola.cargarColas(colas, gestorBuses);

        //Cargar cola de tiquetes
        ColaPrioridad cola = persistencia.deserializarCola("tiquetes.json");

        if (cola == null) {
            JOptionPane.showMessageDialog(null,
                    "No se encontraron tiquetes previos.\n\nSe creará una nueva cola para esta sesión.",
                    "BusNovaTech - Sistema de tiquetes",
                    JOptionPane.WARNING_MESSAGE);
            cola = new ColaPrioridad();

        } else {
            JOptionPane.showMessageDialog(null,
                    "Tiquetes previos cargados exitosamente.\n\nListos para continuar.",
                    "BusNovaTech - Sistema de tiquetes",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        //Verificar la configuración
        if (!config.existeConfiguracion()) {
            JOptionPane.showMessageDialog(null,
                    "No se encontró una configuración activa.\n\nIniciando módulo de configuración.",
                    "BusNovaTech - Configuración del sistema",
                    JOptionPane.INFORMATION_MESSAGE);
            config.cargarConfiguracion();
        }

        //Login
        boolean loginExitoso = false;
        int intentos = 0;

        while (!loginExitoso && intentos < 3) {
            String usuario = JOptionPane.showInputDialog(null,
                    "Ingrese su usuario:", "BusNovaTech - Acceso al sistema", JOptionPane.QUESTION_MESSAGE);

            if (usuario == null) {
                return;
            }

            String contrasena = JOptionPane.showInputDialog(null,
                    "Ingrese su contraseña:", "BusNovaTech - Acceso al sistema", JOptionPane.QUESTION_MESSAGE);

            if (contrasena == null) {
                return;
            }

            if (config.validarLogin(usuario, contrasena)) {
                JOptionPane.showMessageDialog(null,
                        "Acceso concedido. ¡Bienvenido, " + usuario + "!",
                        "BusNovaTech - Sesión iniciada",
                        JOptionPane.INFORMATION_MESSAGE);
                loginExitoso = true;

            } else {
                intentos++;
                JOptionPane.showMessageDialog(null,
                        "Credenciales incorrectas. Intento " + intentos + " de 3.",
                        "BusNovaTech - Error de autenticación",
                        JOptionPane.ERROR_MESSAGE);
            }

        }

        if (!loginExitoso) {
            JOptionPane.showMessageDialog(null,
                    "Demasiados intentos fallidos.\n\nEl sistema se cerrará por seguridad...",
                    "BusNovaTech - Acceso denegado",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Menu principal
        int opcion;

        do {
            String[] opciones = {"Ver configuración", "Gestionar buses", "Gestionar tiquetes", "Salir"};
            opcion = JOptionPane.showOptionDialog(
                    null,
                    "Seleccione una opción para continuar:",
                    "Menú principal - BusNovaTech",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]);

            switch (opcion) {
                case 0:
                    JOptionPane.showMessageDialog(null,
                            "Información actual del sistema:\n\n" + config.getInformacionCompleta(),
                            "BusNovaTech - Configuración del sistema",
                            JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 1:
                    gestorBuses.mostrarBuses();
                    break;
                case 2:
                    persistencia.gestionarTiquetes(cola, moduloAtencion, colas);
                    break;
                case 3:
                    JOptionPane.showMessageDialog(null,
                            "Guardando información...\n\nGracias por usar BusNovaTec!!",
                            "BusNovaTech - Cierre del sistema",
                            JOptionPane.INFORMATION_MESSAGE);
                    persistencia.serializarCola(cola, "tiquetes.json");
                    // Guardar estado de colas antes de salir
                    PersistenciaCola.guardarColas(colas);
                    break;
                default:
                    break;
            }

        } while (opcion != 3);

        JOptionPane.showMessageDialog(null,
                "¡Hasta pronto, usuario BusNovaTech!\n\nEsperamos volver a verte...",
                "BusNovaTech - Sesión finalizada",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
