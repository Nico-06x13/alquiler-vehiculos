package com.example.alquiler.alquiler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VehiculoService {

    private static final String RUTA_ARCHIVO = "vehiculos.txt";

    public static List<Vehiculo> cargarVehiculos() {
        List<Vehiculo> lista = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);

        if (!archivo.exists()) {
            crearArchivoInicial();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] partes = linea.split(";");
                if (partes.length >= 8) {
                    lista.add(new Vehiculo(
                        partes[0], partes[1], partes[2],
                        Integer.parseInt(partes[3]), partes[4],
                        Double.parseDouble(partes[5]), partes[6], partes[7]
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static void guardarVehiculo(Vehiculo vehiculo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            String linea = String.join(";",
                vehiculo.getPlaca(),
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                String.valueOf(vehiculo.getAnio()),
                vehiculo.getTipo(),
                String.valueOf(vehiculo.getTarifaDia()),
                vehiculo.getEstado(),
                vehiculo.getFotoUrl() != null ? vehiculo.getFotoUrl() : ""
            );
            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void crearArchivoInicial() {
        List<Vehiculo> iniciales = List.of(
            new Vehiculo("AAA123", "Toyota", "Corolla", 2022, "Sedan", 120000.0, "Disponible", "https://via.placeholder.com/150"),
            new Vehiculo("BBB456", "Chevrolet", "Tracker", 2023, "SUV", 180000.0, "Alquilado", "https://via.placeholder.com/150"),
            new Vehiculo("CCC789", "Renault", "Duster", 2021, "SUV", 150000.0, "Mantenimiento", "https://via.placeholder.com/150")
        );
        for (Vehiculo v : iniciales) {
            guardarVehiculo(v);
        }
    }
}
