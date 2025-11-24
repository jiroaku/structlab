package cr.ac.ufidelitas.proyecto.busnovatech;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

/**
 *
 * @author @jiroaku
 */
// Andrew - clase principal del módulo 1.2 para atención y cobro de tiquetes
public class ModuloAtencionTiquetes {

    private final GestionBuses gestionBuses;
    private final ConfiguracionSistema configuracion;
    private final HistorialAtenciones historial;

    // Constructor que inicializa configuración, buses e historial
    public ModuloAtencionTiquetes(ConfiguracionSistema configuracion, GestionBuses gestionBuses) {
        this.configuracion = configuracion;
        this.gestionBuses = gestionBuses;
        this.historial = new HistorialAtenciones();
    }


    // Atiende tiquete desde opción 3 del menú de gestión
    public void atenderDesdeMenu(ColaPrioridad cola) {
        if (cola == null || cola.estaVacia()) {
            JOptionPane.showMessageDialog(null, "No hay tiquetes pendientes para abordar.");
            return;
        }
        procesarAtencionDesdeCola(cola, false);
    }

    // Muestra historial de tiquetes atendidos
    public void mostrarHistorial() {
        historial.mostrarHistorial();
    }

    // Procesa atención completa: bus, cobro, pago y registro
    private void procesarAtencionDesdeCola(ColaPrioridad cola, boolean esAutomatico) {
        if (cola == null || cola.estaVacia()) {
            return;
        }

        NodoTiquete tiqueteObjetivo = cola.verFrente();
        if (tiqueteObjetivo == null) {
            return;
        }

        Bus busDisponible = obtenerBusParaTiquete(tiqueteObjetivo);
        if (busDisponible == null) {
            if (!esAutomatico) {
                JOptionPane.showMessageDialog(null,
                        "No hay inspectores disponibles para el tipo de bus solicitado en este momento.");
            }
            return;
        }

        NodoTiquete tiqueteAtender = cola.desencolar();
        if (tiqueteAtender == null) {
            return;
        }

        busDisponible.setEstado("Atendiendo");

        double monto = calcularCobro(tiqueteAtender);
        boolean pagoAceptado = confirmarPago(tiqueteAtender, monto);

        if (!pagoAceptado) {
            busDisponible.setEstado("Disponible");
            tiqueteAtender.setHoraAbordaje("-1");
            tiqueteAtender.setMoneda(0);
            JOptionPane.showMessageDialog(null,
                    "El pasajero se negó a pagar y fue retirado de la fila.\n"
                    + "Debe volver a crear un tiquete desde el módulo 1.1.",
                    "Cobro rechazado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String horaSistema = obtenerHoraSistema();
        tiqueteAtender.setHoraAbordaje(horaSistema);
        tiqueteAtender.setMoneda(monto);

        registrarAtendido(tiqueteAtender, busDisponible, monto);

        busDisponible.setEstado("Disponible");

        String mensaje = "Tiquete atendido correctamente.\n"
                + "Pasajero: " + tiqueteAtender.getNombre() + "\n"
                + "Bus asignado: " + busDisponible.getIdBus() + "\n"
                + "Hora de abordaje: " + horaSistema;

        if (esAutomatico) {
            JOptionPane.showMessageDialog(null, mensaje,
                    "Atención automática", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, mensaje,
                    "Abordar pasajero", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Busca bus disponible según tipo del tiquete
    private Bus obtenerBusParaTiquete(NodoTiquete tiquete) {
        if (tiquete == null || gestionBuses == null) {
            return null;
        }
        String tipoBus = traducirTipoBus(tiquete.getTipoBus());
        return gestionBuses.obtenerBusDisponiblePorTipo(tipoBus);
    }

    // Convierte código P/D/N a nombre completo del tipo
    private String traducirTipoBus(String codigo) {
        if (codigo == null) {
            return "Normal";
        }
        String valor = codigo.trim().toUpperCase();
        if ("P".equals(valor) || "PREFERENCIAL".equals(valor)) {
            return "Preferencial";
        }
        if ("D".equals(valor) || "DIRECTO".equals(valor)) {
            return "Directo";
        }
        return "Normal";
    }

    // Calcula monto según tipo de servicio (VIP/Carga/Ejecutivo/Regular)
    private double calcularCobro(NodoTiquete tiquete) {
        String servicio = "REGULAR";
        if (tiquete != null && tiquete.getServicio() != null) {
            servicio = tiquete.getServicio().trim().toUpperCase();
        }
        if ("VIP".equals(servicio) || "V.I.P".equals(servicio)) {
            return 100.0;
        }
        if ("CARGA".equals(servicio)) {
            double peso = solicitarPesoCarga();
            return 20.0 + (10.0 * peso);
        }
        if ("EJECUTIVO".equals(servicio)) {
            return 1000.0;
        }
        return 20.0;
    }

    // Solicita peso de carga para calcular cobro de servicio Carga
    private double solicitarPesoCarga() {
        double peso = 0.0;
        boolean datoValido = false;
        while (!datoValido) {
            String respuesta = JOptionPane.showInputDialog(
                    null,
                    "Ingrese el peso de la carga en libras:",
                    "Dato requerido para servicio de carga",
                    JOptionPane.QUESTION_MESSAGE);
            if (respuesta == null) {
                return 0.0;
            }
            try {
                peso = Double.parseDouble(respuesta);
                if (peso < 0) {
                    JOptionPane.showMessageDialog(null, "El peso no puede ser negativo.");
                } else {
                    datoValido = true;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un número válido para el peso.");
            }
        }
        return peso;
    }

    // Solicita confirmación de pago mediante diálogo YES/NO
    private boolean confirmarPago(NodoTiquete tiquete, double monto) {
        String nombre = "Pasajero";
        String servicio = "Regular";
        if (tiquete != null) {
            if (tiquete.getNombre() != null) {
                nombre = tiquete.getNombre();
            }
            if (tiquete.getServicio() != null) {
                servicio = tiquete.getServicio();
            }
        }
        String mensaje = "Servicio: " + servicio
                + "\nMonto a cancelar: $" + monto
                + "\n\n¿El pasajero " + nombre + " acepta el cobro?";
        int respuesta = JOptionPane.showConfirmDialog(null, mensaje,
                "Cobro del servicio", JOptionPane.YES_NO_OPTION);
        return respuesta == JOptionPane.YES_OPTION;
    }

    // Obtiene hora actual del sistema en formato dd/MM/yyyy HH:mm:ss
    private String obtenerHoraSistema() {
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return ahora.format(formatter);
    }

    // Crea registro de atención y lo agrega al historial
    private void registrarAtendido(NodoTiquete tiquete, Bus bus, double monto) {
        String terminal = "Terminal BusNovaTech";
        if (configuracion != null && configuracion.getNombreTerminal() != null) {
            terminal = configuracion.getNombreTerminal();
        }
        RegistroAtencion registro = new RegistroAtencion(
                tiquete.getNombre(),
                tiquete.getId(),
                tiquete.getServicio(),
                traducirTipoBus(tiquete.getTipoBus()),
                bus.getIdBus(),
                terminal,
                tiquete.getHoraAbordaje(),
                monto);
        historial.agregarRegistro(registro);
    }
}

