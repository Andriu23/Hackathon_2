package com.hackathon.agenda.modelo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Agenda {

    private List<Contacto> contactos; // Declara una lista que almacena los objetos tipo Contacto
    private int capacidadMaxima; // Guarda el número máximo de contactos que pueda almacenar la agenda

    public Agenda(){ // Constructor con capacidad predeterminada
        this(10); // Llama a otro constructor y establece una capacidad de 10 contactos
    }

    public Agenda(int capacidadMaxima) {

        if (capacidadMaxima <= 0) { // Comprueba si la capacidad se válida
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero."); // Muestra un error si la capacidad no es válida
        }
        this.capacidadMaxima = capacidadMaxima; // Guarda la capacidad recibida
        this.contactos = new ArrayList<>(); // Crea una lista vacía para almacenar los contactos
    }

    public boolean añadirContacto(Contacto c) { // Devuelve true o false - Recibe un objeto de tipo Contacto
        if (c == null) { // Comprueba que no se este enviando un contacto vacío
            System.out.println("El contacto no puede ser nulo.");
            return false;
        }
        if (agendaLlena()) { // Verifica si ya se alcanzó la capacidad máxima.
            System.out.println("La agenda esta llena.");
            return false;
        }
        if (existeContacto(c)) { // Comprueba que el contacto no esté duplicado.
            System.out.println("El contacto ya esta registrado.");
            return false;
        }
        contactos.add(c); // Agrega el contacto a ls lista
        return true; // Indica que el registro fue exitoso.
    }

    public boolean existeContacto(Contacto c) { // Devuelve true o false - Recibe el contacto que queremos buscar
        if (c == null) {
            return false;
        }
        return contactos.contains(c); // Comprueba si ese contacto se encuentra en la lista.
    }

    public List<Contacto> listarContactos() { // Devuelve una lista de contactos y crea una copia de la lista original
        List<Contacto> listaOrdenada = new ArrayList<>(contactos);

        listaOrdenada.sort( // Ordena los elementos de la lista
                Comparator.comparing( // Establece el primer criterio
                        Contacto::getNombre, String.CASE_INSENSITIVE_ORDER) // Utiliza el nombre como primer criterio y evita diferenciar mayúsculas y minúsculas
                        .thenComparing( // Establece el segundo criterio
                                Contacto::getApellido, String.CASE_INSENSITIVE_ORDER) // Utiliza el apellido como segundo criterio y evita diferenciar mayúsculas y minúsculas
        );
        return listaOrdenada; // Devuelve una lista ordenada
    }

    // Busca un contacto por nombre y apellido
    public String buscarContacto(String nombre, String apellido) {
        if (nombre == null || apellido == null) { // Si algun campo esta vacío o es null
            return null;
        }

        for (Contacto contacto : contactos) { // Recorre todos los contactos de la lista
            if (nombre.strip().equalsIgnoreCase(contacto.getNombre())
                    && apellido.strip().equalsIgnoreCase(contacto.getApellido())) { // strip - Elimina todos los espacios al principio y final del texto y sin diferenciar mayúsculas y minúsculas

                return contacto.getTelefono(); // Devuelve su teléfono si lo encuentra
            }
        }

        return null;
    }

    public boolean eliminarContacto(Contacto c) { // Recibe un objeto de tipo contacto
        if (c == null) { // Comprueba si el contacto existe para eliminar
            return false; // Si el contacto en null - devuelve flase si no se encontro
        }

        return contactos.remove(c); // Elimina el contacto si existe en la lista
    }

    public boolean modificarTelefono(String nombre, String apellido, String nuevoTelefono) {
        if (nombre == null || apellido == null || nuevoTelefono == null) { // Comprueba que los campos no sean nulo.
            return false;
        }

        if (!Contacto.telefonoValido(nuevoTelefono)) { // Comprueba si el número de contacto cumple el formato establecido.
            return false;
        }

        for (Contacto contacto : contactos) { // Recorre todos los contactos almacenados en la lista.
            if (contacto.getNombre().equalsIgnoreCase(nombre.strip()) // equalsIgnoreCase - Compara sin diferenciar mayúsculas y minúsculas.
                    && contacto.getApellido().equalsIgnoreCase(apellido.strip())) {

                contacto.setTelefono(nuevoTelefono); // Modifica el teléfono del contacto.
                return true;
            }
        }

        return false; // Si termina de recorrer la lista y no encuentra el contacto, devuelve false.
    }

    public boolean agendaLlena() { // Comprueba que la agenda ya ha alcanzado su limite máximo
        return contactos.size() >= capacidadMaxima; // El metodo size() - Cuenta cuátos elementos hay guardados actualmente dentro de la lista
    }

    public int espaciosLibres() {  // Calcula y devuelve el número de espacios que todavía quedan disponibles
        return capacidadMaxima - contactos.size(); // Comprueba cuantos espacios hay disponibles en la lista
    }
}