package com.hackathon.agenda.modelo;

import java.util.Objects;

public class Contacto {

    private String nombre;
    private String apellido;
    private String telefono;

    public Contacto(String nombre, String apellido, String telefono) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        if (!telefonoValido(telefono)) {
            throw new IllegalArgumentException("Teléfono inválido: " + telefono);
        }
        this.nombre = nombre.strip();
        this.apellido = apellido.strip();
        this.telefono = telefono.strip();
    }


    public static boolean telefonoValido(String telefono) {
        return telefono != null
                && telefono.strip().matches("(\\+\\d{2} ?)?\\d{3} ?\\d{3} ?\\d{4}");
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        if (!telefonoValido(telefono)) {
            throw new IllegalArgumentException("Teléfono inválido: " + telefono);
        }
        this.telefono = telefono.strip();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Contacto)) return false;
        Contacto otro = (Contacto) o;
        return nombre.equalsIgnoreCase(otro.nombre)
                && apellido.equalsIgnoreCase(otro.apellido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre.toLowerCase(), apellido.toLowerCase());
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " - " + telefono;
    }
}
