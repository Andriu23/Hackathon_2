package modelo;

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
            System.out.println("La capacidad debe ser mayor que cero."); // Muestra un error si la capacidad no es válida
        }
        this.capacidadMaxima = capacidadMaxima; // Guarda la capacidad recibida
        this.contactos = new ArrayList<>(); // Crea una lista vacía para almacenar los contactos
    }

    public boolean añadirContacto(Contacto c) { // Devuelve true o false - Recibe un objeto de tipo Contacto
        if (c == null) { // Comprueba que no se este enviando un contacto vacío
            System.out.println("El contacto no puede ser nulo.");
        }
        if (agendaLlena()) { // Verifica si ya se alcanzó la capacidad máxima.
            System.out.println("La agenda esta llena.");
        }
        if (existeContacto(c)) { // Comprueba que el contacto no esté duplicado.
            System.out.println("El contacto ya esta registrado.");
        }
        contactos.add(c); // Agrega el contacto a ls lista
        return true; // Indica que el registro fue exitoso.
    }

    public boolean existeContacto(Contacto c) { // Devuelve true o false - Recibe el contacto que queremos buscar
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

    public String buscarContacto(String nombre, String apellido) {
        if (nombre == null || apellido == null) { // Si algun campo esta vacío o es null - devuleve null al error de busqueda
            return null;
        }

        for (Contacto contacto : contactos) { // Recorre todos los elementos guardados en la lista
            if (nombre.equals(contacto.getNombre()) && apellido.equals(contacto.getApellido())) { // Comprueba que los campos sean iguales
                return contacto.getTelefono(); // Si el nombre y apellido coinciden - devuelve de inmediato el número del contacto
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

    public boolean modificarTelefono(String nombre, String apellido, String nuevoTelefono) { // Busca el contacto por nombre y apellido para cambiarle su número de contacto
        Contacto temporal = new Contacto(nombre, apellido, nuevoTelefono); // Se crea un objeto temporal de Contacto con los datos recibidos

        for (Contacto contacto : contactos) { // Recorre la lista de contactos
            if (contacto.equals(temporal)) { // Compara el contacto temporal con la lista
                contacto.setTelefono(nuevoTelefono); // Si el contacto temporal coincide con la lista , permite cambiar el número de contacto

                return true; // Devuelve true si la operación se ha realizado con éxito
            }
        }

        return false; // Devuelve false si el contacto no existe
    }

    public boolean agendaLlena() { // Comprueba que la agenda ya ha alcanzado su limite máximo
        return contactos.size() >= capacidadMaxima; // El metodo size() - Cuenta cuátos elementos hay guardados actualmente dentro de la lista
    }

    public int espaciosLibres() {  // Calcula y devuelve el número de espacios que todavía quedan disponibles
        return capacidadMaxima - contactos.size(); // Comprueba cuantos espacios hay disponibles en la lista
    }
}