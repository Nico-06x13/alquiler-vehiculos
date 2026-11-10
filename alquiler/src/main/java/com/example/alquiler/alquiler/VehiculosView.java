package com.example.alquiler.alquiler;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Catálogo de Vehículos")
public class VehiculosView extends VerticalLayout {

    private final Grid<Vehiculo> grid = new Grid<>(Vehiculo.class, false);

    public VehiculosView() {
        setSizeFull();
        setPadding(true);

        add(new H2("Catálogo de Vehículos Disponibles"));

        grid.addColumn(Vehiculo::getPlaca).setHeader("Placa");
        grid.addColumn(Vehiculo::getMarca).setHeader("Marca");
        grid.addColumn(Vehiculo::getModelo).setHeader("Modelo");
        grid.addColumn(Vehiculo::getAnio).setHeader("Año");
        grid.addColumn(Vehiculo::getTipo).setHeader("Tipo");
        grid.addColumn(v -> "$" + v.getTarifaDia()).setHeader("Tarifa/Día");
        grid.addColumn(Vehiculo::getEstado).setHeader("Estado");

        grid.setItems(VehiculoService.cargarVehiculos());
        grid.setSizeFull();

        add(grid);
    }
}
