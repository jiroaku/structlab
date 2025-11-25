package cr.ac.ufidelitas.proyecto.busnovatech;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JOptionPane;

/**
 *
 * @author jiroaku
 */
public class ConfiguracionSistema {

    private String nombreTerminal;
    private int cantidadBuses;
    private int busesPreferenciales;
    private int busesDirectos;
    private int busesNormales;
    private String usuario1;
    private String contra1;
    private String usuario2;
    private String contra2;

    // Constructor vacío para Gson
    public ConfiguracionSistema() {
    }
    
    // Constructor que permite inicializar solo el nombre de la terminal
    public ConfiguracionSistema(String nombreTerminal) {
        this.nombreTerminal = nombreTerminal;
    }

    // Verificar si existe config.json
    public boolean existeConfiguracion() {
        File archivo = new File("config.json");
        return archivo.exists();
    }

    // Cargar configuración desde JSON
    public ConfiguracionSistema cargarConfiguracion() {
        Gson gson = new GsonBuilder().create();
        try (FileReader reader = new FileReader("config.json")) {
            return gson.fromJson(reader, ConfiguracionSistema.class);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar configuración: " + e.getMessage());
            return null;
        }
    }

    // Guardar configuración en JSON
    public void guardarConfiguracion(ConfiguracionSistema config) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("config.json")) {
            gson.toJson(config, writer);
            JOptionPane.showMessageDialog(null, "Configuración guardada exitosamente");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar configuración: " + e.getMessage());
        }
    }


    // Ejecutar configuración inicial
    public ConfiguracionSistema ejecutarConfiguracionInicial() {
        JOptionPane.showMessageDialog(null, "Configuración inicial del sistema");

        // Nombre terminal
        String terminal = JOptionPane.showInputDialog("Ingrese nombre de la terminal:");
        if (terminal == null || terminal.trim().isEmpty()) {
            terminal = "Terminal BusNovaTech";
        }

        // Cantidad buses
        int buses = 0;
        try {
            String input = JOptionPane.showInputDialog("Ingrese cantidad de buses (mínimo 3):");
            buses = Integer.parseInt(input);
            if (buses < 3) {
                buses = 3;
            }
        } catch (Exception e) {
            buses = 3;
        }

        // Distribución automática
        int preferenciales = 1;
        int directos = 1;
        int normales = buses - 2;

        // Usuarios
        String usuario1 = JOptionPane.showInputDialog("Usuario 1:");
        String contra1 = JOptionPane.showInputDialog("Contraseña 1:");
        String usuario2 = JOptionPane.showInputDialog("Usuario 2:");
        String contra2 = JOptionPane.showInputDialog("Contraseña 2:");

        if (usuario1 == null || usuario1.trim().isEmpty()) usuario1 = "admin";
        if (contra1 == null || contra1.trim().isEmpty()) contra1 = "123456";
        if (usuario2 == null || usuario2.trim().isEmpty()) usuario2 = "operador";
        if (contra2 == null || contra2.trim().isEmpty()) contra2 = "contra123";

        // Crear configuración
        ConfiguracionSistema config = new ConfiguracionSistema();
        config.setNombreTerminal(terminal);
        config.setCantidadBuses(buses);
        config.setBusesPreferenciales(preferenciales);
        config.setBusesDirectos(directos);
        config.setBusesNormales(normales);
        config.setUsuario1(usuario1);
        config.setContra1(contra1);
        config.setUsuario2(usuario2);
        config.setContra2(contra2);

        return config;
    }

    // Getters y Setters
    public String getNombreTerminal() { return nombreTerminal; }
    public void setNombreTerminal(String nombreTerminal) { this.nombreTerminal = nombreTerminal; }

    public int getCantidadBuses() { return cantidadBuses; }
    public void setCantidadBuses(int cantidadBuses) { this.cantidadBuses = cantidadBuses; }

    public int getBusesPreferenciales() { return busesPreferenciales; }
    public void setBusesPreferenciales(int busesPreferenciales) { this.busesPreferenciales = busesPreferenciales; }

    public int getBusesDirectos() { return busesDirectos; }
    public void setBusesDirectos(int busesDirectos) { this.busesDirectos = busesDirectos; }

    public int getBusesNormales() { return busesNormales; }
    public void setBusesNormales(int busesNormales) { this.busesNormales = busesNormales; }

    public String getUsuario1() { return usuario1; }
    public void setUsuario1(String usuario1) { this.usuario1 = usuario1; }

    public String getContra1() { return contra1; }
    public void setContra1(String contra1) { this.contra1 = contra1; }

    public String getUsuario2() { return usuario2; }
    public void setUsuario2(String usuario2) { this.usuario2 = usuario2; }

    public String getContra2() { return contra2; }
    public void setContra2(String contra2) { this.contra2 = contra2; }

    // Validar login
    public boolean validarLogin(String usuario, String contrasena) {
        return (usuario.equals(usuario1) && contrasena.equals(contra1)) ||
               (usuario.equals(usuario2) && contrasena.equals(contra2));
    }

    // Obtener información completa
    public String getInformacionCompleta() {
        return "Terminal: " + nombreTerminal + "\n" +
               "Total Buses: " + cantidadBuses + "\n" +
               "Preferenciales: " + busesPreferenciales + "\n" +
               "Directos: " + busesDirectos + "\n" +
               "Normales: " + busesNormales + "\n" +
               "Usuarios: " + usuario1 + ", " + usuario2;
    }    
}