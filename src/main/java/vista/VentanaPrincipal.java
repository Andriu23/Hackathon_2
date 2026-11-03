package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private JPanel panelPrincipal;
    private JLabel lblCapacidad;
    private JPanel panelFormulario;
    private JPanel panelCampos;
    private JPanel panelBotones;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtTelefono;
    private JButton btnAgregar;
    private JButton btnEliminar;
    private JButton btnModificar;
    private JButton btnLimpiar;
    private JPanel panelBusqueda;
    private JLabel lblBuscar;
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JTable tablaContactos;
    private JPanel panelEstado;
    private JLabel lblEstado;

    private DefaultTableModel modeloTabla;

    public VentanaPrincipal() {
        setTitle("Agenda Telefónica");
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        modeloTabla = new DefaultTableModel(new String[]{"Nombre", "Apellido", "Teléfono"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaContactos.setModel(modeloTabla);
        tablaContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void mostrarError(String mensaje) {
        lblEstado.setForeground(Color.RED);
        lblEstado.setText(mensaje);
    }

    public void mostrarExito(String mensaje) {
        lblEstado.setForeground(new Color(0, 140, 0));
        lblEstado.setText(mensaje);
    }

    public void setCapacidad(int usados, int total) {
        lblCapacidad.setText("Capacidad: " + usados + "/" + total);
    }

    public void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        tablaContactos.clearSelection();
    }

    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtApellido() { return txtApellido; }
    public JTextField getTxtTelefono() { return txtTelefono; }
    public JTextField getTxtBuscar() { return txtBuscar; }
    public JButton getBtnAgregar() { return btnAgregar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnModificar() { return btnModificar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JTable getTablaContactos() { return tablaContactos; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
}
