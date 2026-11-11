package com.example.alquiler.alquiler;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

public class Reserva {
    private String id;
    private String placaVehiculo;
    private String cedulaCliente;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String extras; 
    private double costoTotal;
    private boolean devuelto;
    private double multa;

    public Reserva(String id, String placaVehiculo, String cedulaCliente, LocalDate fechaInicio, LocalDate fechaFin, String extras, double costoTotal, boolean devuelto, double multa) {
        this.id = id;
        this.placaVehiculo = placaVehiculo;
        this.cedulaCliente = cedulaCliente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.extras = extras;
        this.costoTotal = costoTotal;
        this.devuelto = devuelto;
        this.multa = multa;
    }

    
    public static double calcularCostoTotal(double tarifaDiaria, LocalDate inicio, LocalDate fin, Set<String> seleccionExtras) {
        long dias = ChronoUnit.DAYS.between(inicio, fin);
        if (dias <= 0) dias = 1;

        double valorExtrasPorDia = 0.0;
        if (seleccionExtras != null) {
            if (seleccionExtras.contains("Seguro ($15.000/día)")) valorExtrasPorDia += 15000;
            if (seleccionExtras.contains("GPS ($10.000/día)")) valorExtrasPorDia += 10000;
            if (seleccionExtras.contains("Silla de bebé ($8.000/día)")) valorExtrasPorDia += 8000;
        }

        return dias * (tarifaDiaria + valorExtrasPorDia);
    }

  
    public static double calcularMulta(double tarifaDiaria, LocalDate fechaFinPactada, LocalDate fechaDevolucionReal) {
        if (fechaDevolucionReal.isAfter(fechaFinPactada)) {
            long diasMora = ChronoUnit.DAYS.between(fechaFinPactada, fechaDevolucionReal);
            return diasMora * (tarifaDiaria * 1.5);
        }
        return 0.0;
    }

    public String getId() { return id; }
    public String getPlacaVehiculo() { return placaVehiculo; }
    public String getCedulaCliente() { return cedulaCliente; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public String getExtras() { return extras; }
    public double getCostoTotal() { return costoTotal; }
    public boolean isDevuelto() { return devuelto; }
    public void setDevuelto(boolean devuelto) { this.devuelto = devuelto; }
    public double getMulta() { return multa; }
    public void setMulta(double multa) { this.multa = multa; }
}