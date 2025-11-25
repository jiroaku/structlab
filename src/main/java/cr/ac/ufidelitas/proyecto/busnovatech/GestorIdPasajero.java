package cr.ac.ufidelitas.proyecto.busnovatech;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;

/**
 * Clase utilitaria para generar IDs autoincrementales de pasajeros
 * basándose en los IDs existentes en tiquetes.json y atendidos.json
 *
 * @author jiroaku
 */
public class GestorIdPasajero {

    private static final String ARCHIVO_TIQUETES = "tiquetes.json";
    private static final String ARCHIVO_ATENDIDOS = "atendidos.json";

    // Calcula el siguiente ID disponible buscando el mayor ID en tiquetes.json y atendidos.json.

    public static int obtenerSiguienteId() {
        int maxIdTiquetes = obtenerMaxIdDesdeTiquetes();
        int maxIdAtendidos = obtenerMaxIdDesdeAtendidos();

        int maxId = Math.max(maxIdTiquetes, maxIdAtendidos);
        return maxId + 1;
    }

    // Devuelve el maximo id del archivo tiquetes.json, o 0 si no hay tiquetes.

    private static int obtenerMaxIdDesdeTiquetes() {
        Gson gson = new GsonBuilder().create();
        try (FileReader reader = new FileReader(ARCHIVO_TIQUETES)) {
            NodoTiquete[] tiquetes = gson.fromJson(reader, NodoTiquete[].class);
            if (tiquetes == null || tiquetes.length == 0) {
                return 0;
            }

            int maxId = 0;
            for (NodoTiquete tiquete : tiquetes) {
                if (tiquete != null && tiquete.getId() > maxId) {
                    maxId = tiquete.getId();
                }
            }
            return maxId;
        } catch (Exception e) {
            // Si hay error, retornar 0 (archivo no existe o está vacío)
            return 0;
        }
    }

    // Devuelve el maximo id del archivo atendidos.json, o 0 si no hay atendidos.

    private static int obtenerMaxIdDesdeAtendidos() {
        Gson gson = new GsonBuilder().create();
        try (FileReader reader = new FileReader(ARCHIVO_ATENDIDOS)) {
            RegistroAtencion[] atendidos = gson.fromJson(reader, RegistroAtencion[].class);
            if (atendidos == null || atendidos.length == 0) {
                return 0;
            }

            int maxId = 0;
            for (RegistroAtencion registro : atendidos) {
                if (registro != null && registro.getIdPasajero() > maxId) {
                    maxId = registro.getIdPasajero();
                }
            }
            return maxId;
        } catch (Exception e) {
            // Si hay error, retornar 0 (archivo no existe o está vacío)
            return 0;
        }
    }
}

