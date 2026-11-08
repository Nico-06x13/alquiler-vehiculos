package com.example.alquiler.alquiler;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoService {
    private static final String ARCHIVO = "vehiculos.txt";

    public static List<Vehiculo> cargarVehiculos() {
        List<Vehiculo> lista = new ArrayList<>();
        File file = new File(ARCHIVO);
        
        if (!file.exists()) {
            crearDatosEjemplo();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                Vehiculo v = Vehiculo.desdeLineaTxt(linea);
                if (v != null) lista.add(v);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static void guardarVehiculos(List<Vehiculo> lista) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Vehiculo v : lista) {
                writer.write(v.aLineaTxt());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void crearDatosEjemplo() {
        List<Vehiculo> ejemplos = new ArrayList<>();
        ejemplos.add(new Vehiculo("AAA123", "Toyota", "Corolla", 2022, "Sedán", 120000, "Disponible", "https://via.placeholder.com/150"));
        ejemplos.add(new Vehiculo("BBB456", "Chevrolet", "Tracker", 2023, "SUV", 180000, "Alquilado", "https://via.placeholder.com/150"));
        guardarVehiculos(ejemplos);
    }
}
