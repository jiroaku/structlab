package cr.ac.ufidelitas.proyecto.busnovatech;

/**
 *
 * @author @jiroaku
 */
// Andrew - clase de datos para almacenar información de tiquete atendido
public class RegistroAtencion {

    private String nombrePasajero;
    private int idPasajero;
    private String servicio;
    private String tipoBus;
    private String busAsignado;
    private String terminal;
    private String horaAtencion;
    private double montoCobrado;

    // Andrew - constructor vacío para Gson
    public RegistroAtencion() {
    }

    // Andrew - constructor con todos los datos del registro de atención
    public RegistroAtencion(String nombrePasajero, int idPasajero, String servicio,
                            String tipoBus, String busAsignado, String terminal,
                            String horaAtencion, double montoCobrado) {
        this.nombrePasajero = nombrePasajero;
        this.idPasajero = idPasajero;
        this.servicio = servicio;
        this.tipoBus = tipoBus;
        this.busAsignado = busAsignado;
        this.terminal = terminal;
        this.horaAtencion = horaAtencion;
        this.montoCobrado = montoCobrado;
    }

    public String getNombrePasajero() {
        return nombrePasajero;
    }

    public int getIdPasajero() {
        return idPasajero;
    }

    public String getServicio() {
        return servicio;
    }

    public String getTipoBus() {
        return tipoBus;
    }

    public String getBusAsignado() {
        return busAsignado;
    }

    public String getTerminal() {
        return terminal;
    }

    public String getHoraAtencion() {
        return horaAtencion;
    }

    public double getMontoCobrado() {
        return montoCobrado;
    }
}

