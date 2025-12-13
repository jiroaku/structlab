package cr.ac.ufidelitas.proyecto.busnovatech;

/**
 *
 * @author jiro
 */
// Grafo ponderado dirigido para rutas de buses usando arrays
public class GrafoRutas {

    private VerticeGrafo[] vertices;
    private int cantidadVertices;

    public GrafoRutas() {
        vertices = new VerticeGrafo[20];
        cantidadVertices = 0;
    }

    // Agregar vértice (localidad)
    public void agregarVertice(Localidad localidad) {
        if (buscarVertice(localidad) != null) {
            return;
        }
        
        if (cantidadVertices >= vertices.length) {
            // Redimensionar
            VerticeGrafo[] nuevo = new VerticeGrafo[vertices.length * 2];
            for (int i = 0; i < cantidadVertices; i++) {
                nuevo[i] = vertices[i];
            }
            vertices = nuevo;
        }
        
        vertices[cantidadVertices] = new VerticeGrafo(localidad);
        cantidadVertices++;
    }

    // Agregar arista dirigida con peso
    public void agregarArista(Localidad origen, Localidad destino, int peso) {
        VerticeGrafo vOrigen = buscarVertice(origen);
        if (vOrigen != null) {
            vOrigen.agregarArista(new AristaGrafo(destino, peso));
        }
    }

    // Buscar vértice por localidad
    private VerticeGrafo buscarVertice(Localidad localidad) {
        for (int i = 0; i < cantidadVertices; i++) {
            if (vertices[i].getValor().equals(localidad)) {
                return vertices[i];
            }
        }
        return null;
    }

    // Imprimir grafo
    public String imprimir() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== GRAFO DE RUTAS ===\n\n");
        for (int i = 0; i < cantidadVertices; i++) {
            VerticeGrafo vertice = vertices[i];
            sb.append(vertice.getValor().getNombre()).append(":\n");
            
            if (vertice.getCantidadAristas() == 0) {
                sb.append("  (sin rutas)\n");
            } else {
                for (int j = 0; j < vertice.getCantidadAristas(); j++) {
                    AristaGrafo arista = vertice.getAdyacentes()[j];
                    sb.append("  -> ").append(arista.getDestino().getNombre())
                      .append(" (peso: ").append(arista.getPeso()).append(")\n");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // Obtener array de localidades
    public Localidad[] obtenerArrayLocalidades() {
        Localidad[] array = new Localidad[cantidadVertices];
        for (int i = 0; i < cantidadVertices; i++) {
            array[i] = vertices[i].getValor();
        }
        return array;
    }

    // Algoritmo de Dijkstra para ruta más corta
    public String rutaMasCorta(Localidad origen, Localidad destino) {
        if (origen == null || destino == null) {
            return "Error: Localidad de origen o destino es nula.";
        }
        
        if (origen.equals(destino)) {
            return "Ruta más corta de " + origen.getNombre() + " a " + destino.getNombre() + ":\n\n" 
                 + origen.getNombre() + "\n\nPeso total: 0";
        }
        
        if (buscarVertice(origen) == null || buscarVertice(destino) == null) {
            return "Error: Una o ambas localidades no existen en el grafo.";
        }

        // Arrays para Dijkstra
        int[] distancias = new int[cantidadVertices];
        Localidad[] predecesores = new Localidad[cantidadVertices];
        boolean[] visitados = new boolean[cantidadVertices];
        
        // Inicializar
        for (int i = 0; i < cantidadVertices; i++) {
            distancias[i] = Integer.MAX_VALUE;
            predecesores[i] = null;
            visitados[i] = false;
        }
        
        // Encontrar índice del origen
        int indiceOrigen = -1;
        for (int i = 0; i < cantidadVertices; i++) {
            if (vertices[i].getValor().equals(origen)) {
                indiceOrigen = i;
                distancias[i] = 0;
                break;
            }
        }

        // Algoritmo de Dijkstra
        for (int count = 0; count < cantidadVertices; count++) {
            // Encontrar vértice no visitado con menor distancia
            int u = -1;
            int minDist = Integer.MAX_VALUE;
            for (int i = 0; i < cantidadVertices; i++) {
                if (!visitados[i] && distancias[i] < minDist) {
                    minDist = distancias[i];
                    u = i;
                }
            }
            
            if (u == -1 || minDist == Integer.MAX_VALUE) {
                break;
            }
            
            visitados[u] = true;
            
            // Si llegamos al destino, terminar
            if (vertices[u].getValor().equals(destino)) {
                break;
            }
            
            // Actualizar distancias de adyacentes
            VerticeGrafo verticeU = vertices[u];
            for (int i = 0; i < verticeU.getCantidadAristas(); i++) {
                AristaGrafo arista = verticeU.getAdyacentes()[i];
                Localidad vecino = arista.getDestino();
                
                // Encontrar índice del vecino
                int indiceVecino = -1;
                for (int j = 0; j < cantidadVertices; j++) {
                    if (vertices[j].getValor().equals(vecino)) {
                        indiceVecino = j;
                        break;
                    }
                }
                
                if (indiceVecino != -1 && !visitados[indiceVecino]) {
                    int nuevaDist = distancias[u] + arista.getPeso();
                    if (nuevaDist < distancias[indiceVecino]) {
                        distancias[indiceVecino] = nuevaDist;
                        predecesores[indiceVecino] = vertices[u].getValor();
                    }
                }
            }
        }

        // Encontrar índice del destino
        int indiceDestino = -1;
        for (int i = 0; i < cantidadVertices; i++) {
            if (vertices[i].getValor().equals(destino)) {
                indiceDestino = i;
                break;
            }
        }

        if (indiceDestino == -1 || distancias[indiceDestino] == Integer.MAX_VALUE) {
            return "No existe ruta entre " + origen.getNombre() + " y " + destino.getNombre();
        }

        // Construir camino
        Localidad[] camino = new Localidad[cantidadVertices];
        int tamCamino = 0;
        Localidad actual = destino;
        
        while (actual != null) {
            camino[tamCamino++] = actual;
            if (actual.equals(origen)) {
                break;
            }
            
            // Buscar predecesor
            int indiceActual = -1;
            for (int i = 0; i < cantidadVertices; i++) {
                if (vertices[i].getValor().equals(actual)) {
                    indiceActual = i;
                    break;
                }
            }
            
            if (indiceActual == -1) break;
            actual = predecesores[indiceActual];
        }

        // Construir mensaje
        StringBuilder ruta = new StringBuilder();
        ruta.append("Ruta más corta de ").append(origen.getNombre())
            .append(" a ").append(destino.getNombre()).append(":\n\n");
        
        for (int i = tamCamino - 1; i >= 0; i--) {
            if (i < tamCamino - 1) {
                ruta.append(" -> ");
            }
            ruta.append(camino[i].getNombre());
        }
        
        ruta.append("\n\nPeso total: ").append(distancias[indiceDestino]);

        return ruta.toString();
    }

    // Obtener todos los vértices para serialización
    public VerticeGrafo[] obtenerVertices() {
        VerticeGrafo[] array = new VerticeGrafo[cantidadVertices];
        for (int i = 0; i < cantidadVertices; i++) {
            array[i] = vertices[i];
        }
        return array;
    }

    // Cargar vértices desde array
    public void cargarVertices(VerticeGrafo[] array) {
        if (array == null) {
            cantidadVertices = 0;
            return;
        }
        cantidadVertices = array.length;
        if (array.length > vertices.length) {
            vertices = new VerticeGrafo[array.length];
        }
        for (int i = 0; i < array.length; i++) {
            vertices[i] = array[i];
        }
    }
}
