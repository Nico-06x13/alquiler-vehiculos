package com.example.alquiler.alquiler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteService {
    private static final String RUTA_ARCHIVO = "clientes.txt";

    public static List<Cliente> cargarClientes() {
        List<Cliente> lista = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);

        if (!archivo.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] partes = linea.split(";");
                if (partes.length >= 3) {
                    lista.add(new Cliente(partes[0], partes[1], partes[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static void guardarCliente(Cliente cliente) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            String linea = String.join(";", cliente.getCedula(), cliente.getNombre(), cliente.getTelefono());
            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}