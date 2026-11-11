package com.example.alquiler.alquiler;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservaService {
    private static final String RUTA_ARCHIVO = "reservas.txt";

    public static List<Reserva> cargarReservas() {
        List<Reserva> lista = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(";");
                if (p.length >= 9) {
                    lista.add(new Reserva(
                        p[0], p[1], p[2],
                        LocalDate.parse(p[3]), LocalDate.parse(p[4]),
                        p[5], Double.parseDouble(p[6]),
                        Boolean.parseBoolean(p[7]), Double.parseDouble(p[8])
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static void guardarReserva(Reserva r) {
        List<Reserva> lista = cargarReservas();
        lista.add(r);
        reagruparYGuardar(lista);
    }

    public static void reagruparYGuardar(List<Reserva> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, false))) {
            for (Reserva r : lista) {
                String linea = String.join(";",
                    r.getId(),
                    r.getPlacaVehiculo(),
                    r.getCedulaCliente(),
                    r.getFechaInicio().toString(),
                    r.getFechaFin().toString(),
                    r.getExtras().isEmpty() ? "Ninguno" : r.getExtras(),
                    String.valueOf(r.getCostoTotal()),
                    String.valueOf(r.isDevuelto()),
                    String.valueOf(r.getMulta())
                );
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean estaVehiculoDisponible(String placa, LocalDate inicio, LocalDate fin) {
        List<Reserva> reservas = cargarReservas();
        for (Reserva r : reservas) {
            if (r.getPlacaVehiculo().equalsIgnoreCase(placa) && !r.isDevuelto()) {
                // Solapamiento de fechas
                if (!(fin.isBefore(r.getFechaInicio()) || inicio.isAfter(r.getFechaFin()))) {
                    return false;
                }
            }
        }
        return true;
    }
}