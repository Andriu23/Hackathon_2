# Agenda Telefónica MVC

Aplicación de escritorio en Java Swing para gestionar una agenda telefónica: agregar, buscar, modificar y eliminar contactos. El proyecto usa Gradle como gestor de automatización y está organizado con el patrón Modelo-Vista-Controlador (MVC), así que la lógica del negocio, la interfaz y la coordinación entre las dos van por separado.

Proyecto desarrollado por el Equipo 5 en la Hackathon 2 de Generation Colombia.

---

## Integrantes

- Juan M. Rodríguez Lanza
- Brandon Moreno
- Andrés
- Dayana Vallejo
- Yesid Hernández

---

## Tecnologías

- **Java 21** (JDK Temurin 21)
- **Java Swing** para la interfaz gráfica
- **Gradle 9.8.2** (con Gradle Wrapper) para la configuración y gestión del proyecto
- **IntelliJ IDEA** como entorno de desarrollo, con su GUI Designer para la ventana
- **Git y GitHub** para el control de versiones y el tablero del proyecto

---

## Cómo ejecutar el proyecto

1. Clonar el repositorio:

   ```bash
   git clone https://github.com/Andriu23/Hackathon_2.git
   ```

2. Abrir la carpeta `Hackathon_2` en **IntelliJ IDEA**. Al abrirla, IntelliJ reconoce que es un proyecto Gradle y lo sincroniza solo: descarga la versión de Gradle que usa el proyecto por medio del Gradle Wrapper, así que no hace falta instalar Gradle.
3. Revisar que el proyecto use **Java 21** (en *File > Project Structure > SDK*).
4. Ejecutar la clase `Main` (`com.hackathon.agenda.Main`) con el botón Run.

La aplicación se ejecuta desde IntelliJ porque la ventana está diseñada con el GUI Designer (archivo `.form`), que IntelliJ se encarga de compilar.

---

## Arquitectura MVC

El código está en `src/main/java/` y se divide en estos paquetes:

```
com.hackathon.agenda/
│
├── com.hackathon.agenda.modelo/
│   ├── Contacto.java           Datos de un contacto y sus validaciones
│   └── Agenda.java             Lógica de negocio: guarda y gestiona los contactos
│
├── com.hackathon.agenda.vista/
│   ├── VentanaPrincipal.java   JFrame con el formulario, la tabla y el panel de estado
│   └── VentanaPrincipal.form   Diseño visual de la ventana (GUI Designer de IntelliJ)
│
├── com.hackathon.agenda.controlador/
│   └── AgendaControlador.java  Escucha los eventos de la com.hackathon.agenda.vista y coordina con el com.hackathon.agenda.modelo
│
└── Main.java                   Crea el com.hackathon.agenda.modelo, la com.hackathon.agenda.vista y el com.hackathon.agenda.controlador y los conecta
```

La regla que seguimos para separar las capas:

- El **com.hackathon.agenda.modelo** no importa nada de `javax.swing`. No sabe que existe una ventana.
- La **com.hackathon.agenda.vista** no decide qué pasa cuando se hace clic en un botón. Solo muestra los componentes y los deja disponibles.
- El **com.hackathon.agenda.controlador** es el único que conecta las dos: escucha los eventos de la com.hackathon.agenda.vista, le pide cosas al com.hackathon.agenda.modelo y muestra el resultado.

---

## Cómo lo construimos

### 1. Configuración del proyecto con Gradle

Creamos el proyecto en IntelliJ como proyecto Gradle desde el inicio. Esto nos dio:

- **El Gradle Wrapper** (`gradlew`, `gradlew.bat` y la carpeta `gradle/wrapper`), configurado con Gradle 9.8.2. El Wrapper fija la versión de Gradle dentro del proyecto, así que cualquier integrante o el docente que abra el proyecto trabaja con la misma versión, sin tener que instalarla. Esto le da portabilidad al proyecto y evita el típico "en mi máquina sí funciona".
- **La configuración centralizada en `build.gradle` y `settings.gradle`.** Ahí está el nombre del proyecto, el plugin `java` y el repositorio de donde se descargan las dependencias (Maven Central). Si más adelante se agregan librerías, por ejemplo para guardar los contactos en un archivo o una base de datos, se declaran en `build.gradle` y Gradle las descarga solo, sin copiar archivos `.jar` a mano.
- **Sincronización con IntelliJ:** IntelliJ usa Gradle para leer la estructura del proyecto y sus dependencias. Para compilar y ejecutar, usamos el compilador de IntelliJ, porque es el que procesa el diseño de la ventana hecho con el GUI Designer.

### 2. Modelo: la clase `Contacto`

`Contacto` representa a una persona de la agenda con tres atributos privados: `nombre`, `apellido` y `telefono`. Son privados para que nadie los cambie desde afuera sin pasar por las validaciones (encapsulamiento).

- **Validación en el constructor:** si el nombre o el apellido llegan vacíos o con solo espacios, el constructor lanza una `IllegalArgumentException` y el contacto no se crea. Para revisar esto usamos `isBlank()`, que detecta tanto el texto vacío (`""`) como el que tiene puros espacios (`"   "`).
- **Limpieza de espacios:** antes de guardar, el nombre, el apellido y el teléfono pasan por `strip()`, que quita los espacios de los bordes. Así `"  Ana  "` se guarda como `"Ana"`. Esto también evita que se cuelen duplicados por un espacio de más.
- **Validación del teléfono con Regex:** el método `telefonoValido()` revisa el teléfono con una expresión regular que acepta números de 10 dígitos, con la opción de un prefijo internacional al inicio (por ejemplo `+57 300 000 0000` o `3000000000`). La misma validación se aplica al crear el contacto y al cambiarle el teléfono con `setTelefono()`.
- **Criterio de igualdad:** sobrescribimos `equals()` para que dos contactos sean el mismo si tienen el mismo nombre y apellido, sin importar mayúsculas y minúsculas (usando `equalsIgnoreCase`). Así, "ana lópez" y "ANA LÓPEZ" cuentan como el mismo contacto.
- **`hashCode()` coherente con `equals()`:** en Java, si dos objetos son iguales según `equals()`, tienen que devolver el mismo `hashCode()`. Por eso el `hashCode()` se calcula con el nombre y el apellido pasados a minúsculas, con el mismo criterio que `equals()`.
- **`toString()`:** devuelve el contacto en formato `Nombre Apellido - Teléfono`, útil para mostrarlo o depurar.

### 3. Modelo: la clase `Agenda`

`Agenda` guarda los contactos en una lista (`List<Contacto>`, implementada con `ArrayList`) y tiene toda la lógica para gestionarlos.

- **Capacidad configurable:** tiene dos constructores. El que no recibe nada crea una agenda de **10 contactos**, y el otro permite indicar la capacidad que se quiera.
- **Sin duplicados:** antes de agregar, revisa con `existeContacto()` si el contacto ya está. Ese método usa `contains()`, que a su vez usa el `equals()` de `Contacto`, por eso detecta los duplicados sin importar las mayúsculas.
- **Orden alfabético automático:** `listarContactos()` hace una copia de la lista y la ordena con un `Comparator`, primero por nombre y luego por apellido, sin distinguir mayúsculas (`String.CASE_INSENSITIVE_ORDER`). Se ordena una copia para no alterar la lista original.
- **Control de capacidad:** `agendaLlena()` compara cuántos contactos hay con la capacidad máxima, y `espaciosLibres()` devuelve cuántos lugares quedan.

Estos son los métodos que expone:

| Método | Qué hace |
|---|---|
| `añadirContacto(Contacto c)` | Agrega un contacto después de revisar que haya espacio, que no esté duplicado y que no sea nulo |
| `existeContacto(Contacto c)` | Revisa si el contacto ya está registrado (por nombre y apellido) |
| `listarContactos()` | Devuelve todos los contactos ordenados alfabéticamente |
| `buscarContacto(String nombre, String apellido)` | Recorre la lista y devuelve el teléfono del contacto encontrado |
| `eliminarContacto(Contacto c)` | Elimina el contacto si existe |
| `modificarTelefono(String nombre, String apellido, String nuevoTelefono)` | Busca el contacto y le cambia el teléfono, que se valida otra vez con el Regex |
| `agendaLlena()` | Indica si la agenda ya llegó a su capacidad máxima |
| `espaciosLibres()` | Devuelve cuántos espacios quedan disponibles |

### 4. Vista: la clase `VentanaPrincipal`

La interfaz la diseñamos con el **GUI Designer de IntelliJ** (archivo `VentanaPrincipal.form`) y la clase `VentanaPrincipal` extiende de `JFrame`. Todo funciona en **una sola ventana** de 700 x 500 píxeles, centrada en la pantalla.

[Pendiente: captura de la ventana principal]

La ventana tiene estas partes:

- **Capacidad:** una etiqueta que muestra cuántos contactos hay del total permitido (por ejemplo, `Capacidad: 3/10`), que se actualiza con `setCapacidad()`.
- **Formulario:** campos de texto para Nombre, Apellido y Teléfono.
- **Botones:** Agregar, Modificar, Eliminar y Limpiar.
- **Búsqueda:** un campo de texto con su botón Buscar.
- **Tabla de contactos:** una `JTable` con las columnas Nombre, Apellido y Teléfono. Usa un `DefaultTableModel` en el que sobrescribimos `isCellEditable()` para que las celdas no se puedan editar directamente, y la tabla solo deja seleccionar una fila a la vez.
- **Panel de estado:** muestra los mensajes de la aplicación. `mostrarError()` los pinta en rojo y `mostrarExito()` en verde.

Para respetar el MVC, la com.hackathon.agenda.vista no tiene ninguna lógica de botones. Solo expone sus componentes con getters (`getTxtNombre()`, `getBtnAgregar()`, `getTablaContactos()`, `getModeloTabla()`, etc.) para que el com.hackathon.agenda.controlador les asigne los eventos. También tiene `limpiarCampos()`, que vacía el formulario y quita la selección de la tabla.

**Sincronización:** al seleccionar una fila de la tabla, sus datos llenan automáticamente el formulario para editar o borrar el contacto más rápido.

### 5. Controlador: la clase `AgendaControlador`

El com.hackathon.agenda.controlador recibe la `Agenda` y la `VentanaPrincipal` en su constructor y es el que las pone a trabajar juntas.

- **Eventos de los botones:** en `configurarEventos()` le asignamos un `ActionListener` a cada botón (Agregar, Modificar, Eliminar y Limpiar) usando expresiones lambda, por ejemplo `vista.getBtnAgregar().addActionListener(e -> agregarContacto())`. Así, cada botón llama a la operación que le corresponde.
- **Validación de nombre y apellido:** el método `validarNombreYApellido()` recibe lo que la persona escribió en el formulario y revisa que el nombre y el apellido no estén vacíos ni tengan solo espacios (con `isBlank()`). Si algo está mal, devuelve el mensaje de error que se va a mostrar; si todo está bien, devuelve `null`. Lo hicimos así para que el método solo valide y sea el com.hackathon.agenda.controlador el que decida cómo mostrar el mensaje. De esta forma se puede reutilizar en agregar, modificar, buscar y eliminar.
- **Validación del teléfono:** el método `validarTelefono()` revisa que el campo no esté vacío y que tenga el formato correcto de 10 dígitos antes de mandarlo al com.hackathon.agenda.modelo.
- **Mensajes al usuario:** cuando una validación falla, la operación se detiene ahí mismo (con `return`) y el mensaje se muestra en rojo en el panel de estado. Cuando la operación sale bien, se muestra en verde y se actualiza la tabla.

---

## Manejo de errores y validaciones

La aplicación no debe mostrar excepciones en la consola cuando le meten datos inválidos. Cada error detiene la operación y aparece en el **panel de estado**:

- **En rojo:** campos vacíos, teléfono con formato incorrecto, agenda llena o contacto duplicado.
- **En verde:** operaciones exitosas (contacto agregado, modificado o eliminado).

Las validaciones están en **dos capas**, a propósito:

1. **En el com.hackathon.agenda.controlador:** antes de llamar al com.hackathon.agenda.modelo, se revisa el formulario (nombre, apellido y teléfono) y se muestra el mensaje correspondiente. Esta es la validación que ve el usuario.
2. **En el com.hackathon.agenda.modelo:** `Contacto` vuelve a validar sus datos al crearse y al cambiar el teléfono. Es un cinturón de seguridad: aunque alguien se salte el formulario, el com.hackathon.agenda.modelo no acepta datos inválidos.

---

## Flujo de trabajo en Git

- La rama `main` tiene la versión estable del proyecto.
- Cada parte se trabajó en su propia rama (por ejemplo `teama`, `teamb`, `interfaz`, `feature/Brandon`, `agenda/andres`) para no pisarnos el código.
- Los cambios llegan a `main` por medio de **Pull Requests**, para revisarlos antes de unirlos.
- Las tareas se organizaron en el tablero de GitHub Projects "Agenda Telefónica MVC", con un issue por tarea y columnas para ver su estado.
