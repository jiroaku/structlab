package cr.ac.ufidelitas.proyecto.busnovatech;

/**
 *
 * @author lunad
 */
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class ServicioBCCR {

    private static final String URL = "https://gee.bccr.fi.cr/Indicadores/Suscripciones/WS/wsindicadoreseconomicos.asmx/ObtenerIndicadoresEconomicos";

    public IndicadorEconomico obtenerIndicador(String indicador, String fechaInicio, String fechaFinal,
            String nombre, String subNiveles, String correoElectronico, String token) throws Exception {

        String postData = "Indicador=" + indicador
                + "&FechaInicio=" + fechaInicio
                + "&FechaFinal=" + fechaFinal
                + "&Nombre=" + nombre
                + "&SubNiveles=" + subNiveles
                + "&CorreoElectronico=" + correoElectronico
                + "&Token=" + token;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .POST(HttpRequest.BodyPublishers.ofString(postData, StandardCharsets.UTF_8))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response = responseFuture.join();

        // Imprime status
        System.out.println("Código de estado: " + response.statusCode());
        System.out.println("Respuesta: " + response.body());

        if (response.statusCode() == 200) {
            try (InputStream inputStream = new ByteArrayInputStream(response.body().getBytes())) {
                JAXBContext jaxbContext = JAXBContext.newInstance(IndicadorEconomico.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                return (IndicadorEconomico) unmarshaller.unmarshal(inputStream);
            }
        } else {
            throw new Exception("Error en la solicitud: " + response.statusCode());
        }
    }
}
