# BusNovaTech - Sistema de Gestión de Terminales y Servicios de Buses

## Información del Proyecto

**Curso:** Estructuras de Datos
**Universidad:** Universidad Fidélitas
**Profesor:** Kenneth Artavia A.
**Grupo:** 6
**Versión:** 1.0-SNAPSHOT

## Descripción General Segunda Entrega

BusNovaTech es un sistema de gestión inteligente para terminales de buses que implementa estructuras de datos dinámicas para la administración de buses, tiquetes y configuración del sistema. El proyecto desarrolla los módulos 1.0 (Configuración de Estructuras de Datos), 1.1 (Creación de Tiquetes), 1.2 (Atención de Tiquetes), 1.3 (Llenado de las Colas) y 1.4 (Servicios Complementarios - Grafos) utilizando únicamente clases básicas de Java y manejo de archivos JSON/TXT.

## Equipo de Desarrollo

| Integrante | Módulo | Responsabilidad |
|------------|--------|-----------------|
| **Jeferson Andrew Fuentes García** | 1.0 | Configuración del sistema y persistencia |
| **Jeferson Andrew Fuentes García** | 1.2 | Atención de tiquetes e historial |
| **Jeferson Andrew Fuentes García** | 1.4 | Servicios complementarios (grafos) |
| **Samuel Alonso Mena Garro** | 1.0 | Gestión de buses |
| **Samuel Alonso Mena Garro** | 1.3 | Lógica de asignación de tiquetes a buses |
| **Gerald Obed Herra Fonseca** | 1.1 | Gestión de tiquetes |
| **Gerald Obed Herra Fonseca** | 1.3 | Interfaz y validación de flujo |
| **Luna Delgado Durango** | 1.1 | Integración de módulos, login y menú principal |
| **Luna Delgado Durango** | 1.3 | Persistencia de colas (colas.txt) |


## Arquitectura del Sistema

### Estructura de Clases

| Clase | Propósito | Autor |
|-------|-----------|-------|
| `BusNovaTech` | Clase principal y controlador del flujo | Luna |
| `ConfiguracionSistema` | Gestión de configuración y persistencia JSON | Andrew |
| `GestionBuses` | Administración de buses y tipos | Samuel |
| `ColaPrioridad` | Implementación de cola de prioridad para tiquetes | Gerald |
| `NodoTiquete` | Estructura de datos para tiquetes | Gerald |
| `Nodo<T>` | Nodo genérico para estructuras enlazadas | Gerald |
| `NodoBus` | Nodo específico para lista de buses | Samuel |
| `Bus` | Entidad de bus con propiedades básicas | Samuel |
| `PersistenciaCola` | Serialización/deserialización de tiquetes y colas | Gerald/Luna |
| `ModuloAtencionTiquetes` | Automatiza y gestiona el módulo 1.2 | Andrew |
| `HistorialAtenciones` | Control y persistencia de atendidos | Andrew |
| `RegistroAtencion` | Representa un abordaje confirmado | Andrew |
| `AsignacionColas` | Lógica de asignación de tiquetes a buses (Módulo 1.3) | Samuel |
| `NodoCola` | Nodo para lista de colas de buses | Samuel |
| `GestorIdPasajero` | Generación de IDs autoincrementales | Gerald |
| `GrafoRutas` | Grafo ponderado dirigido para rutas de buses (Módulo 1.4) | jiro |
| `GestionGrafo` | Gestión del módulo 1.4 con menú y persistencia | jiro |
| `Localidad` | Representa una localidad donde el bus pararía | jiro |
| `VerticeGrafo` | Vértice del grafo con adyacentes usando arrays | jiro |
| `AristaGrafo` | Arista con peso para el grafo ponderado | jiro |

## Módulos Implementados

### Módulo 1.0 - Configuración de Estructuras de Datos

**Objetivo:** Configurar el entorno inicial del sistema y persistir la información en `config.json`.

#### Funcionalidades Implementadas

| Funcionalidad | Descripción | Estado |
|---------------|-------------|--------|
| Detección de configuración | Verifica existencia de `config.json` | ✅ |
| Configuración inicial | Registro de terminal, buses y usuarios | ✅ |
| Validación de buses | 1 preferencial, 1 directo, resto normales | ✅ |
| Gestión de usuarios | Sistema de autenticación básico | ✅ |
| Persistencia JSON | Guardado y carga de configuración | ✅ |

#### Datos Manejados
- Nombre de la terminal
- Cantidad total de buses (dinámico)
- Distribución de tipos de buses
- Usuarios y contraseñas del sistema

### Módulo 1.1 - Creación de Tiquetes

**Objetivo:** Gestionar la creación de tiquetes con colas de prioridad según el tipo de servicio y bus.

#### Funcionalidades Implementadas

| Funcionalidad | Descripción | Estado |
|---------------|-------------|--------|
| Creación de tiquetes | Interfaz para registro de pasajeros | ✅ |
| Cola de prioridad | Organización por tipo de bus | ✅ |
| Validación de datos | Campos obligatorios y formato | ✅ |
| Persistencia JSON | Guardado en `tiquetes.json` | ✅ |
| Gestión de tiquetes | Menú para administrar tiquetes | ✅ |

#### Estructura del Tiquete
- **Nombre:** Identificación del pasajero
- **ID:** Identificador único numérico (autoincremental, generado automáticamente)
- **Edad:** Edad del pasajero
- **Moneda:** Monto a pagar (double)
- **Hora de compra:** Timestamp de compra (obtenido automáticamente del sistema)
- **Hora de abordaje:** Timestamp de abordaje (inicialmente -1)
- **Servicio:** Tipo de servicio (VIP, Regular, Carga, Ejecutivo)
- **Tipo de bus:** Clasificación (P, D, N)

#### Mejora Implementada: ID Autoincremental
El sistema genera automáticamente los IDs de pasajeros basándose en el máximo ID existente en `tiquetes.json` y `atendidos.json`, evitando duplicados y mejorando la experiencia de usuario.

### Módulo 1.2 - Atención de Tiquetes

**Objetivo:** Administrar la atención por parte de los inspectores, garantizando cobro correcto, actualización de horarios y registro persistente en `atendidos.json`.

#### Funcionalidades Implementadas

| Funcionalidad | Descripción | Estado |
|---------------|-------------|--------|
| Gestión manual de cola | El tiquete creado permanece en espera hasta usar "Abordar" | ✅ |
| Acción "Abordar" | Opción de menú para que el inspector atienda manualmente al siguiente pasajero | ✅ |
| Validación de pago | Se calcula el monto según el servicio y se solicita confirmación antes de abordar | ✅ |
| Manejo de rechazos | Si el pasajero no paga, es expulsado de la cola y debe crear un nuevo tiquete | ✅ |
| Registro histórico | Cada abordaje se guarda con hora, bus y terminal en `atendidos.json` | ✅ |
| Visualización de atendidos | Consulta del historial mediante interfaz gráfica | ✅ |
| Cálculo de cobros | Implementación de tarifas: VIP ($100), Regular ($20), Carga ($20 + $10/lb), Ejecutivo ($1000) | ✅ |
| Gestión de estados de bus | Cambio de estado a "Atendiendo" durante el proceso y vuelta a "Disponible" | ✅ |

#### Nota de Diseño

Según la especificación, el módulo 1.2 contempla dos formas de atención:
1. **Atención automática**: Cuando se crea un tiquete en una cola vacía, debe atenderse inmediatamente si hay inspector disponible.
2. **Atención manual**: Funcionalidad "Abordar" para cuando las filas están llenas.

**Decisión de implementación**: Se optó por implementar únicamente la atención manual mediante la opción "Abordar" del menú, permitiendo al usuario tener control total sobre cuándo atender a cada pasajero. Esto facilita la demostración y el uso del sistema, ya que el usuario puede crear múltiples tiquetes y luego decidir cuándo atenderlos. La atención automática puede ser implementada en futuras versiones si se requiere.

### Módulo 1.3 - Llenado de las Colas

**Objetivo:** Implementar el sistema de asignación automática de tiquetes a buses según reglas específicas, considerando el tamaño actual de las colas de cada bus.

#### Funcionalidades Implementadas

| Funcionalidad | Descripción | Estado |
|---------------|-------------|--------|
| Asignación automática | Al crear tiquete, se asigna automáticamente a un bus | ✅ |
| Reglas de asignación | Preferencial/Directo al único bus, Normal al de menor cola | ✅ |
| Gestión de colas | Mantiene cantidad de personas en cola por cada bus | ✅ |
| Persistencia de colas | Guardado en `colas.txt` (formato texto simple) | ✅ |
| Carga de colas | Restaura estado de colas al iniciar el sistema | ✅ |
| Actualización de colas | Decrementa cola al atender tiquetes | ✅ |
| Visualización de estado | Opción de menú para ver estado de todas las colas | ✅ |

#### Reglas de Asignación

- **Tiquete Preferencial (P):** Se asigna siempre al único bus preferencial (P1)
- **Tiquete Directo (D):** Se asigna siempre al único bus directo (D1)
- **Tiquete Normal (N):** Se asigna al bus normal con menor cantidad de personas en cola
  - En caso de empate, se asigna al primer bus encontrado con esa cantidad

#### Integración con Módulos Existentes

- **Módulo 1.1:** Al crear un tiquete, se asigna automáticamente a un bus y se incrementa su cola
- **Módulo 1.2:** Al atender un tiquete (pago aceptado o rechazado), se decrementa la cola del bus correspondiente
- **Persistencia:** Las colas se guardan en `colas.txt` al cerrar el sistema y se cargan al iniciar

### Módulo 1.4 - Servicios Complementarios

**Objetivo:** Implementar un grafo ponderado dirigido para definir rutas entre localidades y calcular la ruta más corta entre terminales.

#### Funcionalidades Implementadas

| Funcionalidad | Descripción | Estado |
|---------------|-------------|--------|
| Grafo ponderado dirigido | Implementación de grafo con pesos en las aristas | ✅ |
| Definición de localidades | Gestión de localidades donde el bus pararía (desde ejecución) | ✅ |
| Definición de rutas | Agregar rutas entre localidades con pesos (desde ejecución) | ✅ |
| Impresión del grafo | Visualización completa del grafo con localidades y pesos | ✅ |
| Ruta más corta | Cálculo de ruta más corta entre dos localidades usando Dijkstra | ✅ |
| Persistencia JSON | Guardado y carga del grafo en `grafo.json` | ✅ |
| Menú de gestión | Interfaz para gestionar localidades y rutas | ✅ |

#### Estructura del Grafo

- **Localidades:** Representan terminales o paradas donde el bus puede detenerse
  - Cada localidad tiene un ID único y un nombre
  - Se definen desde la ejecución del programa mediante el menú
- **Rutas:** Representan conexiones dirigidas entre localidades
  - Cada ruta tiene un peso
  - Las rutas son unidireccionales (grafo dirigido)
  - Se definen desde la ejecución del programa mediante el menú

#### Algoritmo de Ruta Más Corta

El módulo implementa el algoritmo de Dijkstra para encontrar la ruta más corta entre dos localidades:
- Utiliza solo estructuras básicas (arrays) sin HashMap, PriorityQueue o ArrayList
- Calcula el peso mínimo y reconstruye el camino completo
- Muestra la ruta paso a paso y el peso total

#### Integración con el Sistema

- **Menú Principal:** Opción "Servicios complementarios (Grafos)" en el menú principal
- **Persistencia:** El grafo se guarda automáticamente en `grafo.json` al modificar
- **Carga automática:** El grafo se carga al iniciar el sistema
- **Gratuito:** Este servicio no se cobra al cliente según especificación

## Sistema de Prioridades

La cola de prioridad implementada organiza los tiquetes según el siguiente criterio:

| Prioridad | Tipo de Bus | Código | Valor |
|-----------|-------------|--------|-------|
| Alta | Preferencial | P | 3 |
| Media | Directo | D | 2 |
| Baja | Normal | N | 1 |

## Flujo del Sistema

1. **Inicialización:** Verificación de configuración existente
2. **Configuración:** Si no existe, ejecuta configuración inicial
3. **Autenticación:** Login con usuarios del sistema
4. **Menú Principal:** Acceso a funcionalidades del sistema
5. **Gestión:** Administración de buses y tiquetes
6. **Persistencia:** Guardado automático de datos

## Tecnologías Utilizadas

- **Java 24:** Lenguaje de programación principal
- **Maven:** Gestión de dependencias y construcción
- **Gson 2.8.9:** Serialización/deserialización JSON
- **Swing (JOptionPane):** Interfaz de usuario
- **Estructuras de Datos:** Listas enlazadas, colas de prioridad y grafos ponderados dirigidos

## Estructura de Archivos

```
src/main/java/cr/ac/ufidelitas/proyecto/busnovatech/
├── BusNovaTech.java          # Clase principal
├── ConfiguracionSistema.java # Módulo 1.0 - Configuración
├── GestionBuses.java         # Módulo 1.0 - Gestión de buses
├── ColaPrioridad.java        # Módulo 1.1 - Cola de prioridad
├── NodoTiquete.java          # Módulo 1.1 - Estructura de tiquete
├── Nodo.java                 # Nodo genérico para estructuras
├── NodoBus.java              # Nodo específico para buses
├── Bus.java                  # Entidad de bus
├── PersistenciaCola.java     # Módulo 1.1/1.3 - Persistencia de tiquetes y colas
├── ModuloAtencionTiquetes.java # Módulo 1.2 - Atención de tiquetes
├── HistorialAtenciones.java    # Módulo 1.2 - Historial de atendidos
├── RegistroAtencion.java       # Módulo 1.2 - Registro de atención
├── AsignacionColas.java        # Módulo 1.3 - Lógica de asignación de tiquetes
├── NodoCola.java               # Módulo 1.3 - Nodo para colas de buses
├── GestorIdPasajero.java       # Utilidad - Generación de IDs autoincrementales
├── GrafoRutas.java             # Módulo 1.4 - Grafo ponderado dirigido
├── GestionGrafo.java           # Módulo 1.4 - Gestión del grafo con menú
├── Localidad.java              # Módulo 1.4 - Localidad donde el bus pararía
├── VerticeGrafo.java           # Módulo 1.4 - Vértice del grafo
└── AristaGrafo.java            # Módulo 1.4 - Arista con peso
```

## Archivos de Datos

| Archivo | Propósito | Formato |
|---------|-----------|---------|
| `config.json` | Configuración del sistema | JSON |
| `tiquetes.json` | Cola de tiquetes persistente | JSON |
| `atendidos.json` | Historial de tiquetes abordados | JSON |
| `colas.txt` | Estado de colas de buses (cantidad de personas por bus) | TXT |
| `grafo.json` | Grafo de rutas entre localidades | JSON |


## Características Técnicas

### Estructuras de Datos Implementadas
- **Lista Enlazada:** Para gestión de buses y colas de buses
- **Cola de Prioridad:** Para organización de tiquetes
- **Grafos Ponderados Dirigidos:** Para rutas entre localidades usando arrays
- **Nodos Genéricos:** Para flexibilidad en estructuras

### Patrones de Diseño
- **Separación de Responsabilidades:** Cada clase tiene un propósito específico
- **Persistencia de Datos:** Serialización JSON para mantener estado
- **Interfaz de Usuario:** Menús interactivos con JOptionPane

### Validaciones Implementadas
- Verificación de configuración existente
- Validación de credenciales de usuario
- Control de tipos de buses según reglas de negocio
- Validación de datos de entrada en tiquetes

## Requerimientos Implementados

### Módulo 1.0 - Configuración de Estructuras de Datos ✅
- ✅ Detección y carga de configuración desde `config.json`
- ✅ Configuración inicial del sistema (terminal, buses, usuarios)
- ✅ Validación de distribución de buses (1 preferencial, 1 directo, resto normales)
- ✅ Sistema de autenticación con al menos 2 usuarios
- ✅ Persistencia de configuración en formato JSON
- ✅ Carga dinámica de buses según configuración

### Módulo 1.1 - Creación de Tiquetes ✅
- ✅ Creación de tiquetes con información completa del pasajero
- ✅ Implementación de cola de prioridad según tipo de bus (P/D/N)
- ✅ Validación de datos de entrada
- ✅ Asignación automática de hora de compra del sistema
- ✅ **ID autoincremental:** Generación automática de IDs basada en máximo existente
- ✅ Persistencia de tiquetes en `tiquetes.json`
- ✅ Carga de tiquetes pendientes al iniciar el sistema
- ✅ Visualización de cola de tiquetes

### Módulo 1.2 - Atención de Tiquetes ✅
- ✅ Funcionalidad "Abordar pasajero" desde menú de gestión
- ✅ Asignación de bus disponible según tipo de tiquete
- ✅ Cálculo de cobro según tipo de servicio:
  - VIP: $100 adicionales
  - Regular: $20
  - Carga: $20 + $10 por libra de carga
  - Ejecutivo: $1000 adicionales
- ✅ Confirmación de pago mediante diálogo de usuario
- ✅ Manejo de rechazo de pago (pasajero retirado de la cola)
- ✅ Actualización de hora de abordaje al momento de atención
- ✅ Cambio de estado del bus a "Atendiendo" durante el proceso
- ✅ Registro completo en `atendidos.json` con:
  - Información del pasajero
  - Bus asignado
  - Terminal de compra
  - Hora de atención
  - Monto cobrado
- ✅ Visualización de historial de atendidos mediante opción de menú
- ✅ Persistencia automática después de cada atención

### Módulo 1.3 - Llenado de las Colas ✅
- ✅ Asignación automática de tiquetes a buses al crear tiquete
- ✅ Reglas de asignación: Preferencial/Directo al único bus, Normal al de menor cola
- ✅ Gestión de cantidades de personas en cola por cada bus
- ✅ Persistencia de colas en `colas.txt` (formato texto simple)
- ✅ Carga de colas al iniciar el sistema (restaura cantidades)
- ✅ Actualización automática de colas al atender tiquetes
- ✅ Visualización del estado de todas las colas mediante opción de menú
- ✅ Integración completa con módulos 1.1 y 1.2

### Módulo 1.4 - Servicios Complementarios ✅
- ✅ Implementación de grafo ponderado dirigido usando arrays
- ✅ Definición de localidades donde el bus pararía (desde ejecución)
- ✅ Definición de rutas entre localidades con pesos (desde ejecución)
- ✅ Impresión del grafo construido con localidades y pesos de rutas
- ✅ Cálculo de ruta más corta entre dos localidades usando algoritmo de Dijkstra
- ✅ Persistencia del grafo en `grafo.json`
- ✅ Carga automática del grafo al iniciar el sistema
- ✅ Menú de gestión para agregar localidades y rutas
- ✅ Integración en el menú principal del sistema

## Requerimientos Pendientes

### Módulo 1.5 - Consulta BCCR ⏳
- ⏳ Integración con Web Service del Banco Central de Costa Rica
- ⏳ Consulta de tipo de cambio del dólar en línea
- ⏳ Integración del tipo de cambio en el cálculo de cobros

## Estado del Proyecto

**Módulo 1.0:** ✅ Completado
**Módulo 1.1:** ✅ Completado
**Módulo 1.2:** ✅ Completado
**Módulo 1.3:** ✅ Completado
**Módulo 1.4:** ✅ Completado
**Módulo 1.5:** ⏳ Pendiente
**Integración:** ✅ Completada
**Persistencia:** ✅ Implementada
**Interfaz de Usuario:** ✅ Funcional


### Primera Ejecución

Al ejecutar por primera vez, el sistema solicitará:
- **Nombre de la terminal**: Nombre identificador de la terminal
- **Cantidad de buses**: Mínimo 3 buses (se asignará automáticamente 1 preferencial, 1 directo, el resto normales)
- **Usuario 1**: Credencial de acceso al sistema
- **Contraseña 1**: Contraseña del primer usuario
- **Usuario 2**: Segunda credencial de acceso
- **Contraseña 2**: Contraseña del segundo usuario

Esta información se guardará en `config.json` y no será necesario ingresarla nuevamente en ejecuciones posteriores.

### Credenciales de Acceso

El sistema requiere autenticación con uno de los usuarios configurados. Las credenciales se almacenan en `config.json` y pueden ser modificadas editando este archivo o eliminándolo para reconfigurar el sistema.

**Nota de Seguridad**: Las credenciales se almacenan en texto plano en `config.json`.

### Archivos de Datos

El sistema genera y utiliza los siguientes archivos:
- `config.json`: Configuración del sistema (terminal, buses, usuarios) - Formato JSON
- `tiquetes.json`: Cola de tiquetes pendientes de atención - Formato JSON
- `atendidos.json`: Historial de tiquetes atendidos - Formato JSON
- `colas.txt`: Estado de colas de buses (cantidad de personas por bus) - Formato TXT
- `grafo.json`: Grafo de rutas entre localidades - Formato JSON

**Importante**: Si se elimina `config.json`, deberá reconfigurar el sistema. Los demás archivos se regeneran automáticamente si se eliminan.

## Documentación de Clases y Métodos

### Clases Principales

#### `BusNovaTech`
- **Propósito**: Clase principal y controlador del flujo de la aplicación
- **Métodos principales**:
  - `main(String[] args)`: Punto de entrada, gestiona inicialización, login y menú principal

#### `ConfiguracionSistema`
- **Propósito**: Gestión de configuración del sistema y persistencia
- **Métodos principales**:
  - `existeConfiguracion()`: Verifica si existe `config.json`
  - `cargarConfiguracion()`: Carga configuración desde JSON
  - `guardarConfiguracion(ConfiguracionSistema)`: Guarda configuración en JSON
  - `ejecutarConfiguracionInicial()`: Solicita y crea configuración inicial
  - `validarLogin(String, String)`: Valida credenciales de usuario

#### `GestionBuses`
- **Propósito**: Administración de la lista de buses del sistema
- **Métodos principales**:
  - `GestionBuses(ConfiguracionSistema)`: Constructor que crea buses desde configuración
  - `mostrarBuses()`: Muestra lista de todos los buses con su estado
  - `obtenerBusDisponiblePorTipo(String)`: Busca bus disponible del tipo especificado

#### `ColaPrioridad`
- **Propósito**: Implementación de cola de prioridad para tiquetes
- **Métodos principales**:
  - `encolar(NodoTiquete)`: Agrega tiquete ordenado por prioridad (P:3, D:2, N:1)
  - `desencolar()`: Remueve y retorna el tiquete del frente
  - `crearTiquete()`: Crea nuevo tiquete solicitando datos al usuario (ID se genera automáticamente)
  - `mostrarCola()`: Muestra todos los tiquetes en la cola
  - `verFrente()`: Retorna el tiquete del frente sin desencolarlo
  - `exportarTiquetes()`: Convierte cola a arreglo para serialización
  - `importarTiquetes(NodoTiquete[])`: Reconstruye cola desde arreglo

#### `NodoTiquete`
- **Propósito**: Estructura de datos que representa un tiquete
- **Atributos**:
  - `nombre`, `id`, `edad`: Datos del pasajero
  - `moneda`: Monto a pagar (calculado al abordar)
  - `horaCompra`: Timestamp de creación
  - `horaAbordaje`: Timestamp de atención (-1 si no atendido)
  - `servicio`: Tipo de servicio (VIP/Regular/Carga/Ejecutivo)
  - `tipoBus`: Tipo de bus requerido (P/D/N)

#### `PersistenciaCola`
- **Propósito**: Serialización y deserialización de tiquetes y gestión de colas de buses
- **Métodos principales**:
  - `serializarCola(ColaPrioridad, String)`: Guarda cola en archivo JSON
  - `deserializarCola(String)`: Carga cola desde archivo JSON
  - `gestionarTiquetes(ColaPrioridad, ModuloAtencionTiquetes, AsignacionColas)`: Menú de gestión de tiquetes
  - `guardarColas(AsignacionColas)`: Guarda estado de colas en `colas.txt`
  - `cargarColas(AsignacionColas, GestionBuses)`: Carga estado de colas desde `colas.txt`

#### `ModuloAtencionTiquetes`
- **Propósito**: Gestión completa del proceso de atención de tiquetes (Módulo 1.2)
- **Métodos principales**:
  - `atenderDesdeMenu(ColaPrioridad, AsignacionColas)`: Atiende tiquete desde opción de menú
  - `mostrarHistorial()`: Muestra historial de atendidos
  - `procesarAtencionDesdeCola(ColaPrioridad, boolean, AsignacionColas)`: Procesa atención completa
  - `calcularCobro(NodoTiquete)`: Calcula monto según tipo de servicio
  - `confirmarPago(NodoTiquete, double)`: Solicita confirmación de pago
  - `registrarAtendido(NodoTiquete, Bus, double)`: Registra atención en historial

#### `HistorialAtenciones`
- **Propósito**: Gestión del historial de tiquetes atendidos usando lista enlazada
- **Métodos principales**:
  - `agregarRegistro(RegistroAtencion)`: Agrega nuevo registro y guarda en JSON
  - `mostrarHistorial()`: Muestra todos los registros de atención
  - `cargarHistorial()`: Carga historial desde `atendidos.json`
  - `guardarHistorial()`: Guarda historial en `atendidos.json`

#### `RegistroAtencion`
- **Propósito**: Estructura de datos para registro de atención completada
- **Atributos**: Información completa del pasajero, bus asignado, terminal, hora y monto

### Estructuras de Datos Auxiliares

#### `Nodo<T>`
- **Propósito**: Nodo genérico para estructuras enlazadas
- **Uso**: Utilizado en `ColaPrioridad` y `HistorialAtenciones`

#### `NodoBus`
- **Propósito**: Nodo específico para lista de buses
- **Uso**: Utilizado en `GestionBuses`

#### `Bus`
- **Propósito**: Entidad que representa un bus
- **Atributos**: `idBus`, `tipo`, `estado` (Disponible/Atendiendo)

#### `AsignacionColas` (Módulo 1.3)
- **Propósito**: Gestiona la asignación de tiquetes a buses según reglas específicas
- **Métodos principales**:
  - `asignarTiquete(String)`: Asigna tiquete a bus según tipo (P/D/N) y reglas
  - `decrementarCola(String)`: Decrementa cantidad en cola cuando se atiende tiquete
  - `obtenerEstadoColas()`: Retorna estado actual de todas las colas
  - `agregarBus(Bus, int)`: Agrega bus a la lista de colas
  - `actualizarCantidadBus(String, int)`: Actualiza cantidad de un bus existente

#### `NodoCola` (Módulo 1.3)
- **Propósito**: Nodo para lista enlazada de colas de buses
- **Atributos**: `bus`, `cantidad`, `siguiente`
- **Uso**: Utilizado en `AsignacionColas` para mantener estado de colas

#### `GestorIdPasajero` (Implementado por Gerald)
- **Propósito**: Utilidad para generar IDs autoincrementales de pasajeros
- **Métodos principales**:
  - `obtenerSiguienteId()`: Retorna el siguiente ID disponible basado en máximo existente en `tiquetes.json` y `atendidos.json`
  - `obtenerMaxIdDesdeTiquetes()`: Busca el máximo ID en `tiquetes.json`
  - `obtenerMaxIdDesdeAtendidos()`: Busca el máximo ID en `atendidos.json`

#### `GrafoRutas` (Módulo 1.4 - Implementado por jiro)
- **Propósito**: Grafo ponderado dirigido para representar rutas entre localidades usando arrays
- **Métodos principales**:
  - `agregarVertice(Localidad)`: Agrega una localidad al grafo
  - `agregarArista(Localidad, Localidad, int)`: Agrega una ruta dirigida con peso
  - `imprimir()`: Retorna representación en texto del grafo completo
  - `rutaMasCorta(Localidad, Localidad)`: Calcula y retorna la ruta más corta usando Dijkstra
  - `obtenerArrayLocalidades()`: Retorna array de localidades
  - `obtenerVertices()`: Retorna array de vértices para serialización
  - `cargarVertices(VerticeGrafo[])`: Carga vértices desde array

#### `GestionGrafo` (Módulo 1.4 - Implementado por jiro)
- **Propósito**: Gestión completa del módulo 1.4 con menú y persistencia
- **Métodos principales**:
  - `gestionarGrafo()`: Menú principal del módulo de grafos
  - `guardarGrafo()`: Guarda el grafo en `grafo.json`
  - `cargarGrafo()`: Carga el grafo desde `grafo.json`
  - `agregarLocalidad()`: Permite agregar localidades desde el menú
  - `agregarRuta()`: Permite agregar rutas desde el menú
  - `consultarRutaMasCorta()`: Consulta la ruta más corta entre dos localidades

#### `Localidad` (Módulo 1.4 - Implementado por jiro)
- **Propósito**: Representa una localidad donde el bus puede parar
- **Atributos**: `id`, `nombre`

#### `VerticeGrafo` (Módulo 1.4 - Implementado por jiro)
- **Propósito**: Vértice del grafo con array de adyacentes
- **Atributos**: `valor` (Localidad), `adyacentes` (array de AristaGrafo), `cantidadAristas` (int)
- **Métodos principales**:
  - `agregarArista(AristaGrafo)`: Agrega una arista al vértice

#### `AristaGrafo` (Módulo 1.4 - Implementado por jiro)
- **Propósito**: Arista con peso para el grafo ponderado
- **Atributos**: `destino` (Localidad), `peso` (int)

## Notas de Desarrollo

- El sistema utiliza únicamente librerías básicas de Java y Gson para JSON
- No se emplean frameworks externos, cumpliendo con los requisitos académicos
- La implementación sigue principios de programación orientada a objetos
- El código está documentado con comentarios explicativos
- Se implementó manejo de errores básico para robustez del sistema
- Se utiliza lista enlazada simple para el historial de atendidos y colas de buses
- La atención al crear tiquete se hace por medio de control manual mediante opción "Abordar"
- IDs de pasajeros se generan automáticamente para evitar duplicados
- **Integración del módulo 1.3:** Gerald integró las funcionalidades del módulo 1.3 en el menú principal de gestión de tiquetes, incluyendo la asignación automática de tiquetes a buses y la opción para ver el estado de las colas.
- **GestorIdPasajero:** Implementado por Gerald para generar IDs autoincrementales de pasajeros, evitando duplicados y mejorando la experiencia de usuario.
- **Integración módulo 1.3 con 1.2:** Implementada por Gerald, modificando `ModuloAtencionTiquetes` para que decremente las colas de buses cuando se atiende un tiquete, tanto si el pago es aceptado como rechazado.

## Correcciones Realizadas en Esta Entrega

### Correcciones Módulo 1.2
- ✅ Corrección de persistencia de tiquetes en `tiquetes.json`
- ✅ Implementación correcta de cálculo de cobros según tipo de servicio
- ✅ Validación de pago antes de marcar tiquete como atendido
- ✅ Manejo correcto de rechazo de pago (pasajero retirado de la cola)
- ✅ Integración completa del módulo 1.2 con menú de gestión
- ✅ Persistencia automática después de cada operación
- ✅ Visualización mejorada de cola y historial mediante JOptionPane

### Correcciones Módulo 1.3
- ✅ Integración completa del módulo 1.3 con módulos existentes
- ✅ Corrección de duplicados en `colas.txt` (ahora actualiza cantidades en lugar de crear duplicados)
- ✅ Asignación automática de tiquetes a buses al crear tiquete
- ✅ Actualización automática de colas al atender tiquetes
- ✅ Persistencia y carga correcta de colas desde `colas.txt`
- ✅ Compatibilidad de códigos P/D/N con nombres completos en asignación
- ✅ **Integración del módulo 1.3:** Gerald integró las funcionalidades del módulo 1.3 en el menú principal de gestión de tiquetes, incluyendo la asignación automática de tiquetes a buses (opción 1) y la opción para ver el estado de las colas (opción 5).
- ✅ **GestorIdPasajero:** Implementado por Gerald para generar IDs autoincrementales, evitando duplicados.
- ✅ **Integración módulo 1.3 con 1.2:** Implementada por Gerald, conectando el decremento de colas cuando se atiende un tiquete.
- ✅ **Módulo 1.4 - Servicios Complementarios:** Implementado por jiro (Jeferson Andrew Fuentes García), incluyendo grafo ponderado dirigido usando arrays, algoritmo de Dijkstra para ruta más corta, definición de localidades y rutas desde ejecución, persistencia en `grafo.json` e integración en el menú principal.

### Mejoras Implementadas
- ✅ **ID autoincremental:** Implementación de generación automática de IDs de pasajeros (Gerald - `GestorIdPasajero`)
- ✅ **Gestión mejorada de colas:** Sistema robusto de actualización sin duplicados
- ✅ **Integración módulo 1.3 con 1.2:** Decremento automático de colas al atender tiquetes (Gerald)


---

**Desarrollado para el curso de Estructuras de Datos - Universidad Fidélitas**
**Semestre 2024 - Grupo 6**
