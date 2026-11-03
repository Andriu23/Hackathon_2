package controlador;

public class AgrendaControlador {

    private final Agenda agenda;
    private final VentanaPrincipal vista;

    // Constructor
    public void AgendaControlador(Agenda agenda, VentanaPrincipal vista) {
        this.agenda = agenda;
        this.vista = vista;

        configurarEventos();
        actualizarTabla();
    }

    // Configuración de eventos
    private void configurarEventos() {

        // ActionListeners de los botones
            // Boton agregar
            // Boton modificar
            // Boton eliminar
            // Boton limpiar

        // ActionListeners de los inputs
            // Busqueda

        // Eventos de selección de la tabla


    }

    // Operaciones de la agenda
    private void agregarContacto() {
    }

    private void buscarContacto() {
    }

    private void modificarTelefono() {
    }

    private void eliminarContacto() {
    }

    private void actualizarTabla() {
    }

    // Validaciones del formulario.

        // Nombre y Apellidos

        // Telefono

}
