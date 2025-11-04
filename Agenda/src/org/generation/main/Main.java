package org.generation.main;

import org.generation.exception.ContactoNotFoundException;
import org.generation.model.Contacto;
import org.generation.service.AgendaService;

import java.util.List;
import java.util.Scanner;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);
    private static AgendaService service;

    static void main() {
        System.out.println("\nIndica el tamaño de tu agenda");
        int capacidad = scanner.nextInt();
        service = new AgendaService(capacidad);

        String menu = """
                \n********************Bienvenid@ a tu agenda *********************
                Ingresa el número de la opción que deseas realizar:
                
                1.- Agregar nuevo contacto
                2.- Lista de contactos
                3.- Buscar contacto
                4.- Eliminar contacto
                5.- Espacio disponible de mi agenda
                6.- Contacto existente en mi agenda
                
                0.- Salir
                """;

        while (true) {
            System.out.println(menu);
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> agregarContacto();
                case 2 -> listaContactos();
                case 3 -> buscarContacto();
                case 4 -> eliminarContacto();
                case 5 -> espacioDeMiAgenda();
                case 6 -> existeContacto();
                case 0 -> {
                    System.out.println("\n***************** Regresa pronto ******************");
                    return;
                }
                default -> System.out.println("Ingresa una opción válida");
            }
        }
    }

    private static void existeContacto() {
        String nombre = validateInput("Ingresa nombre completo:");

        try {
            Contacto contacto = service.buscaContacto(nombre);
            if (service.existeContacto(contacto)) {
                System.out.println("Contacto existente en tu agenda");
            }
        } catch (ContactoNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    private static void agregarContacto() {
        if (service.espaciosLibres() == 0) {
            System.out.println("No hay espacio para agregar nuevo contacto");
            return;
        }

        String nombre = validateInput("Ingresa nombre completo");
        String numero = validateInput("Ingresa tu número de teléfono");

        Contacto contacto = new Contacto(nombre, numero);
        service.anadirContacto(contacto);
    }

    private static String validateInput(String entryMsg) {
        String entry = "";
        while (entry.trim().isEmpty()) {
            System.out.println("\n" + entryMsg);
            entry = scanner.nextLine();
            if (entry.trim().isEmpty()) {
                System.out.println("El campo no puede estar vacío. Inténtalo de nuevo.");
            }
        }
        return entry;
    }

    private static void listaContactos() {
        System.out.println();
        if (service.listarContactos().isEmpty()) {
            System.out.println("Tu lista de contactos esta vacia.");
            return;
        }
        List<Contacto> contactos = service.listarContactos();
        for (int i = 0; i < contactos.size(); i++) {
            System.out.println((i + 1) + ".- " + contactos.get(i));
        }
    }

    private static void buscarContacto() {
        try {
            String buscarPorNombre = validateInput("Ingresa el nombre del contacto a buscar:");
            System.out.println("Contacto: " + service.buscaContacto(buscarPorNombre).getNuevoTelefono());
        } catch (ContactoNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void eliminarContacto() {
        try {
            String nombre = validateInput("Ingresa nombre completo del contacto a eliminar");
            Contacto contacto = service.buscaContacto(nombre);
            service.eliminarContacto(contacto);
            System.out.println("\nContacto eliminado exitosamente");
        } catch (ContactoNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void espacioDeMiAgenda() {
        System.out.println("\nEspacio de mi agenda: " + service.espaciosLibres());
    }
}
