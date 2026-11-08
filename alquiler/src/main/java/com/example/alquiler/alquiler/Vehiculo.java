package com.example.alquiler.alquiler;


public class Vehiculo {
    private String placa;
    private String marca;
    private String modelo;
    private int anio;
    private String tipo;
    private double tarifaDia;
    private String estado; // Disponible, Alquilado, Mantenimiento
    private String fotoUrl;

    public Vehiculo(String placa, String marca, String modelo, int anio, String tipo, double tarifaDia, String estado, String fotoUrl) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.tipo = tipo;
        this.tarifaDia = tarifaDia;
        this.estado = estado;
        this.fotoUrl = fotoUrl;
    }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getTarifaDia() { return tarifaDia; }
    public void setTarifaDia(double tarifaDia) { this.tarifaDia = tarifaDia; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public String aLineaTxt() {
        return placa + "|" + marca + "|" + modelo + "|" + anio + "|" + tipo + "|" + tarifaDia + "|" + estado + "|" + fotoUrl;
    }

    public static Vehiculo desdeLineaTxt(String linea) {
        String[] partes = linea.split("\\|");
        if (partes.length < 8) return null;
        return new Vehiculo(
            partes[0], partes[1], partes[2],
            Integer.parseInt(partes[3]), partes[4],
            Double.parseDouble(partes[5]), partes[6], partes[7]
        );
    }
}
