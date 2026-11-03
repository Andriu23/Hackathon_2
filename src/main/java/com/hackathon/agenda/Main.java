package com.hackathon.agenda;

import com.hackathon.agenda.controlador.AgendaControlador;
import com.hackathon.agenda.modelo.Agenda;
import com.hackathon.agenda.vista.VentanaPrincipal;

import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal vista = new VentanaPrincipal();
            new AgendaControlador(new Agenda(), vista);
            vista.setVisible(true);
        });
    }
}