package com.hackathon.agenda.controlador;

import com.hackathon.agenda.modelo.Agenda;
import com.hackathon.agenda.modelo.Contacto;
import com.hackathon.agenda.vista.VentanaPrincipal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

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
        vista.getBtnBuscar().addActionListener(e -> buscarContacto());


        // Eventos de selección de la tabla
        // Al seleccionar una fila se cargan sus datos en el formulario
        vista.getTablaContactos().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarContactoSeleccionado();
            }
        });
    }

    // Operaciones de la agenda
    private void agregarContacto() {
        String nombre = vista.getTxtNombre().getText();
        String apellido = vista.getTxtApellido().getText();
        String telefono = vista.getTxtTelefono().getText();

        String error = validarNombreYApellido(nombre, apellido);
        if (error != null) {
            vista.mostrarError(error);
            return;
        }
        if (!validarTelefono(telefono)) {
            return;
        }
        if (agenda.agendaLlena()) {
            vista.mostrarError("La agenda está llena. No se pueden agregar más contactos.");
            return;
        }

        Contacto contacto = new Contacto(nombre, apellido, telefono);

        if (agenda.existeContacto(contacto)) {
            vista.mostrarError("El contacto " + contacto.getNombre() + " " + contacto.getApellido() + " ya existe.");
            return;
        }

        agenda.añadirContacto(contacto);
        actualizarTabla();
        vista.limpiarCampos();
        vista.mostrarExito("Contacto agregado correctamente.");
    }

    private void modificarContacto() {
        String nombre = vista.getTxtNombre().getText();
        String apellido = vista.getTxtApellido().getText();
        String telefono = vista.getTxtTelefono().getText();

        String error = validarNombreYApellido(nombre, apellido);
        if (error != null) {
            vista.mostrarError(error);
            return;
        }
        if (!validarTelefono(telefono)) {
            return;
        }

        if (agenda.modificarTelefono(nombre, apellido, telefono.strip())) {
            actualizarTabla();
            vista.limpiarCampos();
            vista.mostrarExito("Teléfono modificado correctamente.");
        } else {
            vista.mostrarError("No existe un contacto con ese nombre y apellido.");
        }
    }

    private void eliminarContacto() {
        String nombre = vista.getTxtNombre().getText();
        String apellido = vista.getTxtApellido().getText();

        String error = validarNombreYApellido(nombre, apellido);
        if (error != null) {
            vista.mostrarError("Seleccione un contacto o escriba su nombre y apellido.");
            return;
        }

        Contacto contacto = obtenerContacto(nombre, apellido);
        if (contacto == null) {
            vista.mostrarError("No existe un contacto con ese nombre y apellido.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                vista,
                "¿Desea eliminar a " + contacto.getNombre() + " " + contacto.getApellido() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        agenda.eliminarContacto(contacto);
        actualizarTabla();
        vista.limpiarCampos();
        vista.mostrarExito("Contacto eliminado correctamente.");
    }

    private void buscarContacto() {
        String texto = vista.getTxtBuscar().getText().strip().toLowerCase();

        // Si la búsqueda está vacía se muestran todos los contactos
        if (texto.isEmpty()) {
            actualizarTabla();
            vista.mostrarExito("Mostrando todos los contactos.");
            return;
        }

        DefaultTableModel modelo = vista.getModeloTabla();
        modelo.setRowCount(0);

        int encontrados = 0;
        for (Contacto c : agenda.listarContactos()) {
            String completo = (c.getNombre() + " " + c.getApellido()).toLowerCase();
            if (completo.contains(texto) || c.getTelefono().contains(texto)) {
                modelo.addRow(new Object[]{c.getNombre(), c.getApellido(), c.getTelefono()});
                encontrados++;
            }
        }

        if (encontrados == 0) {
            vista.mostrarError("No se encontraron contactos para \"" + texto + "\".");
        } else {
            vista.mostrarExito(encontrados + " contacto(s) encontrado(s).");
        }
    }

    private void limpiarFormulario() {
        vista.limpiarCampos();
        vista.getTxtBuscar().setText("");
        actualizarTabla();
        vista.mostrarExito("");
    }

    private void actualizarTabla() {
        DefaultTableModel modelo = vista.getModeloTabla();
        modelo.setRowCount(0);

        List<Contacto> contactos = agenda.listarContactos();
        for (Contacto c : contactos) {
            modelo.addRow(new Object[]{c.getNombre(), c.getApellido(), c.getTelefono()});
        }

        int usados = contactos.size();
        vista.setCapacidad(usados, usados + agenda.espaciosLibres());
    }

    // Carga en el formulario los datos de la fila seleccionada en la tabla
    private void cargarContactoSeleccionado() {
        JTable tabla = vista.getTablaContactos();
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }

        DefaultTableModel modelo = vista.getModeloTabla();
        vista.getTxtNombre().setText(String.valueOf(modelo.getValueAt(fila, 0)));
        vista.getTxtApellido().setText(String.valueOf(modelo.getValueAt(fila, 1)));
        vista.getTxtTelefono().setText(String.valueOf(modelo.getValueAt(fila, 2)));
    }

    // Busca en la agenda el contacto con ese nombre y apellido, o null si no existe
    private Contacto obtenerContacto(String nombre, String apellido) {

        return agenda.buscarContacto(nombre, apellido);
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
    // Usa la misma regla que el com.hackathon.agenda.modelo (Contacto.telefonoValido) para evitar inconsistencias
    private boolean validarTelefono(String telefono) {
        if (telefono == null || telefono.isBlank()) {
            vista.mostrarError("El teléfono es obligatorio.");
            return false;
        }

        if (!Contacto.telefonoValido(telefono)) {
            vista.mostrarError("Teléfono inválido. Formatos permitidos: +57 300 000 0000 ó 3000000000.");
            return false;
        }

        return true;
    }

}
