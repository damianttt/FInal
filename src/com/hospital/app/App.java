package com.hospital.app;

import java.util.ArrayList;
import java.util.Scanner;

import com.hospital.modelo.*;
import com.hospital.excepciones.*;

public class App {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Hospital hospital =
            new Hospital("Hospital Los Libertadores");

        ArrayList<Paciente> pacientesTemporales =
            new ArrayList<>();

        Medico m1 = new Medico(
            "Dra. Laura Gomez",
            "M001",
            "Cardiologia"
        );

        Medico m2 = new Medico(
            "Dr. Pedro Ruiz",
            "M002",
            "Pediatria"
        );

        Enfermero e1 = new Enfermero(
            "Ana Torres",
            "E001",
            "Cuidados intensivos",
            "Mañana"
        );

        hospital.agregarPersonal(m1);
        hospital.agregarPersonal(m2);
        hospital.agregarPersonal(e1);

        int opcion;

        do {

            System.out.println("\n==============================");
            System.out.println(" SISTEMA DE GESTIÓN HOSPITALARIA");
            System.out.println("==============================");
            System.out.println("1. Registrar paciente");
            System.out.println("2. Asignar paciente a sala");
            System.out.println("3. Agendar cita con médico");
            System.out.println("4. Ver pacientes de una sala");
            System.out.println("5. Ver agenda de un médico");
            System.out.println("6. Dar de alta a paciente");
            System.out.println("7. Buscar paciente por código");
            System.out.println("8. Reporte general del hospital");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número.");
                opcion = -1;
            }

            switch (opcion) {

                case 1:

                    try {

                        System.out.println("\n1. Ambulatorio");
                        System.out.println("2. Hospitalizado");
                        System.out.print("Tipo: ");

                        int tipo =
                            Integer.parseInt(scanner.nextLine());

                         System.out.print("Nombre: ");
                         String nombre = scanner.nextLine();

                         if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                         throw new IllegalArgumentException("El nombre no puede contener números ni caracteres especiales.");
                         }

                        System.out.print("Código: ");
                        String codigo = scanner.nextLine();

                        System.out.print("Edad: ");
                        int edad =
                            Integer.parseInt(scanner.nextLine());

                        Paciente paciente;

                        if (tipo == 1) {

                            System.out.print(
                                "Próxima cita: "
                            );

                            String proximaCita =
                                scanner.nextLine();

                            paciente =
                                new PacienteAmbulatorio(
                                    nombre,
                                    codigo,
                                    edad,
                                    proximaCita
                                );

                        } else if (tipo == 2) {

                            System.out.print(
                                "Número de cama: "
                            );

                            int cama =
                                Integer.parseInt(
                                    scanner.nextLine()
                                );

                            System.out.print(
                                "Días hospitalizado: "
                            );

                            int dias =
                                Integer.parseInt(
                                    scanner.nextLine()
                                );

                            paciente =
                                new PacienteHospitalizado(
                                    nombre,
                                    codigo,
                                    edad,
                                    cama,
                                    dias
                                );

                        } else {
                            System.out.println(
                                "Tipo de paciente inválido."
                            );
                            break;
                        }

                        pacientesTemporales.add(paciente);

                        System.out.println(
                            "Paciente registrado correctamente."
                        );

                    } catch (NumberFormatException e) {

                        System.out.println(
                            "Debe ingresar un número válido."
                        );

                    } catch (IllegalArgumentException e) {

                        System.out.println(
                            "Error: " + e.getMessage()
                        );
                    }

                    break;

                case 2:

                    System.out.println("\nSalas disponibles:");

                    for (Sala sala : hospital.getSalas()) {

                        System.out.println(
                            "- " + sala.getNombre() +
                            " (" +
                            sala.getPacientes().size() +
                            "/" +
                            sala.getCapacidad() +
                            ")"
                        );
                    }

                    System.out.print(
                        "Nombre de la sala: "
                    );

                    String nombreSala =
                        scanner.nextLine();

                    Sala sala =
                        hospital.buscarSala(nombreSala);

                    if (sala == null) {

                        System.out.println(
                            "Sala no encontrada."
                        );

                        break;
                    }

                    System.out.print(
                        "Código del paciente: "
                    );

                    String codigoPaciente =
                        scanner.nextLine();

                    Paciente pacienteEncontrado = null;

                    for (Paciente p :
                            pacientesTemporales) {

                        if (p.getCodigo()
                                .equals(codigoPaciente)) {

                            pacienteEncontrado = p;
                            break;
                        }
                    }

                    if (pacienteEncontrado == null) {

                        System.out.println(
                            "Paciente no encontrado en la lista temporal."
                        );

                        break;
                    }

                    try {

                        sala.agregarPaciente(
                            pacienteEncontrado
                        );

                    } catch (CamaNoDisponibleException e) {

                        System.out.println(
                            "Error: " + e.getMessage()
                        );
                    }

                    break;

                case 3:

                    System.out.println("\nMédicos disponibles:");

                    for (Personal p :
                            hospital.getPersonal()) {

                        if (p instanceof Medico) {

                            Medico medico =
                                (Medico) p;

                            System.out.println(
                                medico.getId() +
                                " - " +
                                medico.getNombre() +
                                " - " +
                                medico.getEspecialidad()
                            );
                        }
                    }

                    System.out.print(
                        "ID del médico: "
                    );

                    String idMedico =
                        scanner.nextLine();

                    Medico medicoSeleccionado = null;

                    for (Personal p :
                            hospital.getPersonal()) {

                        if (p instanceof Medico &&
                            p.getId().equals(idMedico)) {

                            medicoSeleccionado =
                                (Medico) p;

                            break;
                        }
                    }

                    if (medicoSeleccionado == null) {

                        System.out.println(
                            "Médico no encontrado."
                        );

                        break;
                    }

                    System.out.print(
                        "¿Desea agregar motivo? (s/n): "
                    );

                    String respuesta =
                        scanner.nextLine();

                    System.out.print(
                        "Fecha: "
                    );

                    String fecha =
                        scanner.nextLine();

                    try {

                        if (respuesta.equalsIgnoreCase("s")) {

                            System.out.print(
                                "Motivo: "
                            );

                            String motivo =
                                scanner.nextLine();

                            medicoSeleccionado
                                .agendarCita(
                                    fecha,
                                    motivo
                                );

                        } else {

                            medicoSeleccionado
                                .agendarCita(fecha);
                        }

                    } catch (CitaInvalidaException e) {

                        System.out.println(
                            "Error: " + e.getMessage()
                        );
                    }

                    break;

                case 4:

                    System.out.print(
                        "Nombre de la sala: "
                    );

                    String salaConsulta =
                        scanner.nextLine();

                    Sala salaMostrar =
                        hospital.buscarSala(salaConsulta);

                    if (salaMostrar == null) {

                        System.out.println(
                            "Sala no encontrada."
                        );

                    } else {

                        salaMostrar.listarPacientes();
                    }

                    break;

                case 5:

                    System.out.print(
                        "ID del médico: "
                    );

                    String idMedicoAgenda =
                        scanner.nextLine();

                    Medico medicoAgenda = null;

                    for (Personal p :
                            hospital.getPersonal()) {

                        if (p instanceof Medico &&
                            p.getId().equals(idMedicoAgenda)) {

                            medicoAgenda =
                                (Medico) p;

                            break;
                        }
                    }

                    if (medicoAgenda == null) {

                        System.out.println(
                            "Médico no encontrado."
                        );

                    } else if (
                        medicoAgenda.getCitas().isEmpty()) {

                        System.out.println(
                            "El médico no tiene citas."
                        );

                    } else {

                        for (Cita cita :
                                medicoAgenda.getCitas()) {

                            System.out.println(
                                cita.getInfo()
                            );
                        }
                    }

                    break;

                case 6:

                    System.out.print(
                        "Nombre de la sala: "
                    );

                    String salaAlta =
                        scanner.nextLine();

                    Sala salaDarAlta =
                        hospital.buscarSala(salaAlta);

                    if (salaDarAlta == null) {

                        System.out.println(
                            "Sala no encontrada."
                        );

                        break;
                    }

                    System.out.print(
                        "Código del paciente: "
                    );

                    String codigoAlta =
                        scanner.nextLine();

                    try {

                        salaDarAlta.eliminarPaciente(
                            codigoAlta
                        );

                    } catch (PacienteNoEncontradoException e) {

                        System.out.println(
                            "Error: " + e.getMessage()
                        );
                    }

                    break;

                case 7:

                    System.out.print(
                        "Código del paciente: "
                    );

                    String codigoBusqueda =
                        scanner.nextLine();

                    try {

                        Paciente pacienteBusqueda =
                            hospital.buscarPaciente(
                                codigoBusqueda
                            );

                        System.out.println(
                            pacienteBusqueda.obtenerInfo()
                        );

                    } catch (
                        PacienteNoEncontradoException e) {

                        System.out.println(
                            "Error: " + e.getMessage()
                        );
                    }

                    break;

                case 8:

                    hospital.generarReporteGeneral();

                    break;

                case 0:

                    System.out.println(
                        "Saliendo del sistema..."
                    );

                    break;

                default:

                    if (opcion != -1) {

                        System.out.println(
                            "Opción inválida."
                        );
                    }
            }

        } while (opcion != 0);

        scanner.close();
    }
}