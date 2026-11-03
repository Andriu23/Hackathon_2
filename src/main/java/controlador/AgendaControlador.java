package controlador;

public class AgendaControlador {
    package controlador;

    public class AgendaControlador {

        private final Agenda agenda;
        private final VentanaPrincipal vista;

        // Constructor
        public AgendaControlador(Agenda agenda, VentanaPrincipal vista) {
            this.agenda = agenda;
            this.vista = vista;

            configurarEventos();
            actualizarTabla();
        }

        // Configuración de eventos
        private void configurarEventos() {

            // ActionListeners de los botones
            // Boton agregar
            vista.getBtnAgregar().addActionListener(e -> agregarContacto());
            // Boton modificar
            vista.getBtnModificar().addActionListener(e -> modificarContacto());
            // Boton eliminar
            vista.getBtnEliminar().addActionListener(e -> eliminarContacto());
            // Boton limpiar
            vista.getBtnLimpiar().addActionListener(e -> limpiarFormulario());

            // ActionListeners de los inputs

            // Busqueda


            // Eventos de selección de la tabla

        }

        // Operaciones de la agenda
        private void agregarContacto() {
        }

        private void buscarContacto() {
        }

    }

    private void eliminarContacto() {
    }

    private void actualizarTabla() {
    }

    // Validaciones del formulario.

    // Nombre y Apellidos
    // Devuelve el mensaje de error, o null si nombre y apellido están bien
    private String validarNombreYApellido(String nombre, String apellido) {
        if (nombre == null || nombre.isBlank()) {
            return "El nombre no puede estar vacío.";
        }
        if (apellido == null || apellido.isBlank()) {
            return "El apellido no puede estar vacío.";
        }
        return null;
    }

    // Telefono

    private boolean validarTelefono() {

        String telefono = txtTelefono.getText().trim();

        String regex = "^\\\\d{10}$";

        if (telefono.isEmpty()) {
            vista.mostrarEstado("El teléfono es obligatorio.", Color.RED);
            return false;
        }

        if (!telefono.matches(regex)) {
            vista.mostrarEstado(
                    "Teléfono inválido. Numero de 10 digitos, solo numericos.",
                    Color.RED
            );
            return false;
        }

        vista.mostrarEstado("Teléfono válido.", Color.GREEN);
        return true;
    }

}

}
