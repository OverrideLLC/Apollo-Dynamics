
# Arquitectura de TaskTec (Aplicación Desktop KMP/Compose)

## 1. Introducción y Visión General

**TaskTec** es una aplicación de escritorio desarrollada con Kotlin Multiplatform (KMP) y Compose Multiplatform (CMP), diseñada para docentes que centraliza la gestión integral de alumnos. Potenciada por Inteligencia Artificial, facilita el seguimiento de asistencia, trabajos, exámenes y otras evaluaciones. La plataforma está enfocada en el profesor, mientras que los alumnos consultan sus tareas y calificaciones a través de sistemas existentes como Google Classroom o Microsoft Teams. Para el control de asistencia, TaskTec se integra con la aplicación Quickness.

El objetivo principal de la arquitectura es maximizar la compartición de código entre plataformas (aunque actualmente el foco principal es Desktop), asegurar una alta mantenibilidad, facilitar la testabilidad y permitir una escalabilidad futura mediante un diseño modular claro.

**Tecnologías Clave:**

* Kotlin Multiplatform (KMP)
* Compose Multiplatform (CMP) para la UI Desktop
* Koin para Inyección de Dependencias (DI)
* Kotlin Coroutines para concurrencia
* Ktor
* Room

**Plataforma Objetivo Principal:** Desktop (Windows, macOS, Linux)

## 2. Principios Arquitectónicos

El diseño de la aplicación se guía por los siguientes principios:

* **Separación de Responsabilidades (SoC):** División clara en capas (Presentación, Dominio, Datos) y módulos funcionales.
* **Código Compartido:** La lógica de negocio, acceso a datos y parte de la lógica de presentación (ViewModels) residen en módulos compartidos (`shared`, `network`, `feature`) siempre que sea posible.
* **Modularidad (Arquitectura Multimodular por Features):** La aplicación se divide en módulos independientes por funcionalidad (`feature/home`, `feature/start`, etc.) para mejorar la cohesión, el aislamiento y la escalabilidad.
* **Inversión de Dependencias (DI):** Se utiliza Koin para gestionar la creación y provisión de dependencias, desacoplando los componentes.
* **Testabilidad:** La separación en capas y módulos, junto con DI, facilita la creación de pruebas unitarias y de integración.
* **Flujo de Datos Reactivo y Unidireccional (UDF):** Se sigue un patrón MVVM donde la UI observa flujos de estado (`StateFlow`) expuestos por los ViewModels.

## 3. Estructura de Módulos del Proyecto

El proyecto está organizado en los siguientes módulos Gradle:

* **`composeApp`:**
* Módulo principal de la aplicación Desktop.
* Contiene el punto de entrada (`main`), configuración de la ventana (`Window`), menú, etc.
* Ensambla la UI principal utilizando componentes de los módulos `feature`.
* Proporciona las implementaciones `actual` para las declaraciones `expect` del módulo `shared` necesarias en Desktop (ej. acceso a sistema de archivos).
* **`feature`:**
* Módulo contenedor para las funcionalidades específicas de la aplicación.
    * **`desktop`:**
* * Desktop:
    * **`api`:** (Dentro de `feature/desktop`)
    * **`home`:**
* Módulo de la funcionalidad "Home".
    * Contiene UI (Composables), ViewModel, Casos de Uso y lógica de datos específicos para esta feature.
    * **`start`:**
* Módulo de la funcionalidad "Start" (Inicio/Onboarding).
    * Contiene UI (Composables), ViewModel, Casos de Uso y lógica de datos específicos para esta feature.
* **`gradle`:**
* Archivos de configuración y wrapper de Gradle.
* **`iosApp`:**
* Módulo para la versión iOS de la aplicación (demuestra capacidad KMP).
* **`network`:**
* Módulo dedicado a la comunicación de red.
* Contiene configuración de Ktor Client (o similar), DTOs, y las implementaciones de Data Sources remotos.
* **`shared`:**
* Módulo KMP central con código compartido entre `composeApp`, `iosApp` y módulos `feature`.
* Contiene `commonMain`, `desktopMain`, `iosMain`, etc.
    * **`resources`:** Recursos compartidos (strings, imágenes, fuentes - usando librerías KMP como Moko Resources).
    * **`ui`:**
* *Contenido a clarificar:* ¿ViewModels compartidos? ¿Composables básicos y reutilizables sin dependencia de plataforma? ¿Estilos/Temas de Compose?
    * **`utils`:** Clases y funciones de utilidad generales (helpers, extensiones, validadores).

## 4. Patrones Arquitectónicos Principales

* **MVVM (Model-View-ViewModel):**
* **View:** Composables (`@Composable`) dentro de `composeApp` y los módulos `feature/*`. Observan el estado del ViewModel y le notifican eventos.
    * **ViewModel:** Clases (ubicadas probablemente en `shared/ui` o dentro de cada `feature/*` module) que contienen la lógica de presentación, interactúan con Casos de Uso y exponen el estado de la UI mediante `StateFlow` u observables similares.
    * **Model:** Representa las capas de Dominio y Datos (Entidades, Casos de Uso, Repositorios).
* **Clean Architecture:** Se aplica mediante la separación en capas lógicas con dependencias bien definidas (Presentación -> Dominio -> Datos). La estructura multimodular ayuda a reforzar esta separación.
* **Arquitectura Multimodular por Features:** Los módulos `feature/*`, `network`, y `shared` promueven el bajo acoplamiento, alta cohesión, mejoran los tiempos de compilación (potencialmente) y facilitan el trabajo en paralelo.
* **Patrón Repositorio:** Se utiliza para abstraer el origen de los datos. Las interfaces se definen en la capa de Dominio (`shared` o `feature/*`) y las implementaciones en la capa de Datos (`shared`, `feature/*`, `network`).

## 5. Descripción Detallada de las Capas

Las capas lógicas se mapean a los módulos de la siguiente manera:

* **Capa de Presentación:**
* Responsable de la UI y la interacción con el usuario.
* Componentes: `@Composable` functions, ViewModels.
* Ubicación: `composeApp` (estructura principal, navegación), `feature/home`, `feature/start`, `feature/desktop` (UI específica de features), `shared/ui` (posibles ViewModels o Composables base compartidos).
* **Capa de Dominio:**
* Contiene la lógica de negocio pura y las reglas del sistema. Es independiente de la UI y de los detalles de implementación de datos.
* Componentes: Entidades de negocio, Casos de Uso (Interactors), Interfaces de Repositorio.
* Ubicación: `shared` (elementos comunes del dominio), `feature/*` (lógica de negocio específica de cada feature).
* **Capa de Datos:**
* Responsable de obtener y almacenar datos, abstrayendo los detalles de la fuente (red, base de datos, archivos).
* Componentes: Implementaciones de Repositorio, Data Sources (Remotos, Locales), Mappers, DTOs.
* Ubicación: `shared` (implementaciones de Repositorio, Data Sources locales [ej. SQLDelight], `expect` para acceso a plataforma), `network` (Data Sources remotos [ej. Ktor]), `feature/*` (si una feature tiene su propio repositorio o fuente de datos), `composeApp` (implementaciones `actual` para `expect` de `shared`).

## 6. Gestión de Dependencias (Koin)

Se utiliza **Koin** para la Inyección de Dependencias en toda la aplicación.

* **Configuración:** Se definen módulos Koin en `shared`, `network` y en cada módulo `feature/*` para declarar sus propias dependencias internas y las que exponen.
* **Inicialización:** La aplicación (`composeApp`) inicializa Koin al arrancar, cargando todos los módulos necesarios de las diferentes partes del proyecto (`startKoin {...}`).
* **Uso:** Las dependencias se inyectan en constructores o mediante delegados (`by inject()`, `viewModel()`) en Clases, ViewModels y Composables. Se pueden usar scopes como `single` o `factory` según sea necesario.

## 7. Flujo de Datos (Ejemplo Típico)

Un flujo de interacción común sigue estos pasos:

1.  **Usuario interactúa** con un Composable en `feature/home`.
2. El Composable **notifica un evento** al `HomeViewModel` (ubicado en `feature/home` o `shared/ui`).
3. El `HomeViewModel` **procesa el evento** y llama a un Caso de Uso apropiado (ej. `LoadHomeDataUseCase` en `feature/home` o `shared`).
4. El Caso de Uso **interactúa con uno o más Repositorios** (ej. `HomeRepository` definido en `feature/home`/`shared` e implementado allí).
5. El Repositorio **obtiene datos** de la fuente adecuada:
    * Llama a un `RemoteDataSource` en el módulo `network` (si requiere datos de API).
    * Llama a un `LocalDataSource` en `shared` (si requiere datos de DB o caché).
6. Los datos **fluyen de regreso** a través del Repositorio y el Caso de Uso hasta el `HomeViewModel`.
7. El `HomeViewModel` **actualiza su estado** (un `StateFlow<HomeUiState>`).
8. El Composable en `feature/home`, que está **observando el `StateFlow`** (usando `collectAsState()`), detecta el cambio y **se recompone** para mostrar la nueva información.

## 8. Consideraciones Específicas de Desktop (`composeApp`)

La integración con el sistema operativo de escritorio y la gestión de la ventana se manejan principalmente en el módulo `composeApp`:

* **Ventana Principal:** Se configura usando `Window`, `rememberWindowState`, definiendo título, tamaño, estado inicial, etc.
* **Múltiples Ventanas:** Si la aplicación lo requiere, se gestiona la creación y ciclo de vida de ventanas adicionales.
* **Menú de Aplicación:** Se define usando `MenuBar`, `Menu`, `Item`.
* **Bandeja del Sistema (System Tray):** Se puede implementar usando `Tray`, `rememberTrayState`.
* **Ciclo de Vida:** Gestión del cierre de la aplicación (`onCloseRequest` en `Window`, objeto `application`).
* **Acceso al Sistema de Archivos:** Se usan APIs de Java/Kotlin (`java.io.File`, `java.nio.file`) directamente o a través de abstracciones `expect`/`actual` definidas en `shared` e implementadas en `composeApp`.
* **Threading:** Se utiliza Kotlin Coroutines. Las operaciones de UI ocurren en el dispatcher principal de Compose. Las operaciones de Red/Disco se lanzan típicamente en `Dispatchers.IO`, y las computaciones intensivas en `Dispatchers.Default`.
* **Empaquetado:** Se utilizan herramientas como Gradle con el plugin de Compose, `jpackage`, o herramientas de terceros (como Conveyor) para crear instaladores nativos (.exe, .dmg, .deb/.rpm).

## 9. Estrategia de Pruebas

* **Pruebas Unitarias:**
* Se enfocan en clases individuales (ViewModels, Casos de Uso, Repositorios, Utils).
* Ubicación: En los conjuntos de pruebas (`commonTest`, `desktopTest`) de cada módulo (`shared`, `network`, `feature/*`).
* Herramientas: `kotlin.test`, MockK (para mocks), Turbine (para probar Flows).
* **Pruebas de Integración:**
* Prueban la interacción entre varias unidades (ej. ViewModel -> Caso de Uso -> Repositorio Mockeado).
* **Pruebas de UI (Compose):**
* Se pueden escribir pruebas para verificar el comportamiento y estado de los `@Composable` functions usando el framework de testing de Compose (`createComposeRule`).

## 10. Diagramas Arquitectónicos

Para una mejor visualización, se recomienda incluir los siguientes diagramas (pueden ser generados con herramientas como PlantUML, Mermaid, Excalidraw, etc. e incrustados o enlazados):

* **Diagrama de Capas:** Mostrando las capas lógicas (Presentación, Dominio, Datos) y sus dependencias.
* **Diagrama de Dependencia de Módulos:** Ilustrando cómo los módulos Gradle (`shared`, `composeApp`, `network`, `feature/*`) dependen entre sí.
* **Diagrama de Flujo de Datos:** Para una o dos características clave, mostrando cómo viaja la información entre componentes y módulos.