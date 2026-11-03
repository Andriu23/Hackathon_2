package com.hackathon.agenda.modelo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Agenda {

    private List<Contacto> contactos;
    private int capacidadMaxima;

    public Agenda(){
        this(10);
    }

    public Agenda(int capacidadMaxima) {
        // Valida que la capacidad máxima sea mayor que cero.
        if (capacidadMaxima <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero.");
        }
        this.capacidadMaxima = capacidadMaxima;
        this.contactos = new ArrayList<>();
    }
    // Registra un contacto si cumple las condiciones establecidas.
    public boolean añadirContacto(Contacto c) {
        if (c == null) {
            System.out.println("El contacto no puede ser nulo.");
            return false;
        }
        if (agendaLlena()) {
            System.out.println("La agenda esta llena.");
            return false;
        }
        if (existeContacto(c)) {
            System.out.println("El contacto ya esta registrado.");
            return false;
        }
        contactos.add(c);
        return true;
    }
    // Comprueba si un contacto ya está registrado en la agenda.
    public boolean existeContacto(Contacto c) {
        if (c == null) {
            return false;
        }
        return contactos.contains(c);
    }
    // Devuelve una copia de los contactos ordenada alfabéticamente.
    public List<Contacto> listarContactos() {
        // Crea una copia para ordenar sin modificar la lista original.
        List<Contacto> listaOrdenada = new ArrayList<>(contactos);
        // Ordena por nombre y apellido sin distinguir mayúsculas y minúsculas.
        listaOrdenada.sort(
                Comparator.comparing(
                        Contacto::getNombre, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(
                                Contacto::getApellido, String.CASE_INSENSITIVE_ORDER)
        );
        return listaOrdenada;
    }

    // Busca un contacto por nombre y apellido y devuelve su teléfono.
    public String buscarContacto(String nombre, String apellido) {
        if (nombre == null || apellido == null) {
            return null;
        }

        for (Contacto contacto : contactos) {
            // Elimina los espacios al inicio y al final, y compara sin distinguir mayúsculas.
            if (nombre.strip().equalsIgnoreCase(contacto.getNombre())
                    && apellido.strip().equalsIgnoreCase(contacto.getApellido())) {

                return contacto.getTelefono();
            }
        }

        return null;
    }
    // Elimina un contacto de la agenda.
    public boolean eliminarContacto(Contacto c) {
        if (c == null) {
            return false;
        }

        return contactos.remove(c);
    }
    // Modifica el teléfono de un contacto identificado por su nombre y apellido.
    public boolean modificarTelefono(String nombre, String apellido, String nuevoTelefono) {
        if (nombre == null || apellido == null || nuevoTelefono == null) {
            return false;
        }
        // Comprueba que el nuevo teléfono cumpla el formato establecido.
        if (!Contacto.telefonoValido(nuevoTelefono)) {
            return false;
        }

        for (Contacto contacto : contactos) {
            // Compara el nombre y apellido sin distinguir mayúsculas y minúsculas.
            if (contacto.getNombre().equalsIgnoreCase(nombre.strip())
                    && contacto.getApellido().equalsIgnoreCase(apellido.strip())) {

                contacto.setTelefono(nuevoTelefono);
                return true;
            }
        }

        return false;
    }
    // Comprueba si se alcanzó la capacidad máxima.
    public boolean agendaLlena() {
        return contactos.size() >= capacidadMaxima;
    }
    // Calcula cuántos contactos todavía se pueden registrar.
    public int espaciosLibres() {
        return capacidadMaxima - contactos.size();
    }
}