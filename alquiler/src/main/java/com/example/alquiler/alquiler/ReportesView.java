package com.example.alquiler.alquiler;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.*;
import java.util.stream.Collectors;

@Route(value = "reportes", layout = MainLayout.class)
@PageTitle("Reportes de Negocio | Alquiler")
public class ReportesView extends VerticalLayout {

    public ReportesView() {
        setSizeFull();
        setPadding(true);

        add(new H2("Reportes e Ingresos del Negocio"));

        List<Reserva> reservas = ReservaService.cargarReservas();

        
        double totalIngresos = reservas.stream().mapToDouble(r -> r.getCostoTotal() + r.getMulta()).sum();
        Span txtTotal = new Span("Ingresos Totales Acumulados: $" + String.format("%.2f", totalIngresos));
        txtTotal.getStyle().set("font-weight", "bold").set("font-size", "1.2em");
        add(txtTotal);

        
        add(new H3("Vehículos Más Alquilados"));
        Grid<VehiculoConteo> gridConteo = new Grid<>(VehiculoConteo.class, false);
        gridConteo.addColumn(VehiculoConteo::getPlaca).setHeader("Placa Vehículo");
        gridConteo.addColumn(VehiculoConteo::getCantidad).setHeader("Cantidad de Alquileres");

        Map<String, Long> conteoPlacas = reservas.stream()
                .collect(Collectors.groupingBy(Reserva::getPlacaVehiculo, Collectors.counting()));

        List<VehiculoConteo> listaConteo = conteoPlacas.entrySet().stream()
                .map(e -> new VehiculoConteo(e.getKey(), e.getValue()))
                .sorted((a, b) -> Long.compare(b.getCantidad(), a.getCantidad()))
                .collect(Collectors.toList());

        gridConteo.setItems(listaConteo);
        add(gridConteo);
    }

    public static class VehiculoConteo {
        private String placa;
        private long cantidad;

        public VehiculoConteo(String placa, long cantidad) {
            this.placa = placa;
            this.cantidad = cantidad;
        }

        public String getPlaca() { return placa; }
        public long getCantidad() { return cantidad; }
    }
}