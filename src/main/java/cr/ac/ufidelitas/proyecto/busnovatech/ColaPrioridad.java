
package cr.ac.ufidelitas.proyecto.busnovatech;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

/**
 *
 * @author Geral
 */
public class ColaPrioridad {

    private Nodo<NodoTiquete> frente;
    private Nodo<NodoTiquete> fin;

    public ColaPrioridad() {
        this.frente = null;
        this.fin = null;
    }

    //Encolar con la prioridad
    public void encolar(NodoTiquete tiquete) {
        Nodo<NodoTiquete> nuevo = new Nodo<>(tiquete);

        if (frente == null) { //Cola vacia
            frente = fin = nuevo;
            return;
        }

        //Si el nuevo tiene mayor prioridad que el de alfrente
        if (compararPrioridad(nuevo.getDato(), frente.getDato()) > 0) {
            nuevo.setSiguiente(frente);
            frente = nuevo;
            return;
        }

        Nodo<NodoTiquete> actual = frente;
        while (actual.getSiguiente() != null &&
               compararPrioridad(actual.getSiguiente().getDato(), nuevo.getDato()) >= 0) {
            actual = actual.getSiguiente();
        }

        nuevo.setSiguiente(actual.getSiguiente());
        actual.setSiguiente(nuevo);

        if (nuevo.getSiguiente() == null) {
            fin = nuevo;
        }
    }

    private int compararPrioridad(NodoTiquete a, NodoTiquete b) {
        return getPrioridad(a.getTipoBus()) - getPrioridad(b.getTipoBus());
    }

    private int getPrioridad(String tipoBus) {
        if (tipoBus == null) {
            return 0;
        }
        String codigo = tipoBus.trim().toUpperCase();
        if (codigo.startsWith("P")) { // Preferencial
            return 3;
        }
        if (codigo.startsWith("D")) { // Directo
            return 2;
        }
        if (codigo.startsWith("N")) { // Normal
            return 1;
        }
        return 0;
    }

    //Desencolar
    public NodoTiquete desencolar() {
        if (frente == null) {
            System.err.println("La cola está vacía");
            return null;
        }
        NodoTiquete tiquete = frente.getDato();
        frente = frente.getSiguiente();
        if (frente == null) {
            fin = null;
        }
        return tiquete;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    //Mostrar
    public void mostrarCola() {
        if (frente == null) {
            JOptionPane.showMessageDialog(null, "La cola está vacía.");
            return;
        }
        // Andrew - construye mensaje formateado con todos los tiquetes
        StringBuilder sb = new StringBuilder("=== Cola de tiquetes ===\n\n");
        Nodo<NodoTiquete> actual = frente;
        while (actual != null) {
            NodoTiquete tiquete = actual.getDato();
            sb.append("Pasajero: ").append(tiquete.getNombre()).append("\n")
              .append("ID: ").append(tiquete.getId()).append("\n")
              .append("Servicio: ").append(tiquete.getServicio()).append("\n")
              .append("Tipo bus: ").append(tiquete.getTipoBus()).append("\n")
              .append("Hora compra: ").append(tiquete.getHoraCompra()).append("\n")
              .append("Hora abordaje: ").append(tiquete.getHoraAbordaje()).append("\n")
              .append("----------------------------------\n");
            actual = actual.getSiguiente();
        }
        JOptionPane.showMessageDialog(null, sb.toString(),
                "BusNovaTech - Cola de tiquetes", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public String toString() {
        if (frente == null) return "Cola vacía";
        StringBuilder sb = new StringBuilder();
        Nodo<NodoTiquete> actual = frente;
        while (actual != null) {
            sb.append(actual.getDato());
            actual = actual.getSiguiente();
            if (actual != null) sb.append(" -> ");
        }
        return sb.toString();
    }

    // Andrew - crea tiquete con datos del usuario y hora automática
    public NodoTiquete crearTiquete() {
        try {
            String nombre = JOptionPane.showInputDialog("Nombre del pasajero:");
            if (nombre == null || nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nombre es requerido");
                return null;
            }

            int id = Integer.parseInt(JOptionPane.showInputDialog("ID del pasajero:"));
            int edad = Integer.parseInt(JOptionPane.showInputDialog("Edad del pasajero:"));
            String servicio = JOptionPane.showInputDialog("Tipo de servicio (VIP/Regular/Carga/Ejecutivo):");
            String tipoBus = JOptionPane.showInputDialog("Tipo de bus (P/D/N):");

            String horaCompra = obtenerHoraActual();
            String horaAbordaje = "-1";
            double montoPendiente = 0.0;

            NodoTiquete nuevoTiquete = new NodoTiquete(nombre, id, edad, montoPendiente,
                                                      horaCompra, horaAbordaje, servicio, tipoBus);
            this.encolar(nuevoTiquete);

            JOptionPane.showMessageDialog(null, "Tiquete creado exitosamente");
            return nuevoTiquete;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error en los datos ingresados");
            return null;
        }
    }

    // Retorna el tiquete del frente sin desencolarlo
    public NodoTiquete verFrente() {
        if (frente == null) {
            return null;
        }
        return frente.getDato();
    }

    // Obtiene hora del sistema en formato dd/MM/yyyy HH:mm:ss
    private String obtenerHoraActual() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    // Convierte la cola en arreglo para serialización JSON
    public NodoTiquete[] exportarTiquetes() {
        int tamano = contarNodos();
        NodoTiquete[] datos = new NodoTiquete[tamano];
        Nodo<NodoTiquete> actual = frente;
        int indice = 0;
        while (actual != null) {
            datos[indice] = actual.getDato();
            indice++;
            actual = actual.getSiguiente();
        }
        return datos;
    }

    // Reconstruye la cola desde un arreglo de tiquetes
    public void importarTiquetes(NodoTiquete[] tiquetes) {
        frente = null;
        fin = null;
        if (tiquetes == null) {
            return;
        }
        for (int i = 0; i < tiquetes.length; i++) {
            NodoTiquete tiquete = tiquetes[i];
            if (tiquete != null) {
                encolar(tiquete);
            }
        }
    }

    // Cuenta nodos de la cola para determinar tamaño
    private int contarNodos() {
        int contador = 0;
        Nodo<NodoTiquete> actual = frente;
        while (actual != null) {
            contador++;
            actual = actual.getSiguiente();
        }
        return contador;
    }
}
