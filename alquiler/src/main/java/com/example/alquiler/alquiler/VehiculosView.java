package com.example.alquiler.alquiler;

import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Catálogo de Vehículos")
public class VehiculosView extends VerticalLayout {

    private final Grid<Vehiculo> grid = new Grid<>(Vehiculo.class, false);
    private final CheckboxGroup<String> filtroEstado = new CheckboxGroup<>();
    private List<Vehiculo> todosLosVehiculos;

    public VehiculosView() {
        setSizeFull();
        setPadding(true);

        add(new H2("Catálogo de Vehículos Disponibles"));

        // Cargar lista base desde el servicio
        todosLosVehiculos = VehiculoService.cargarVehiculos();

        // Filtro avanzado con CheckboxGroup
        filtroEstado.setLabel("Filtrar por Estado:");
        filtroEstado.setItems("Disponible", "Alquilado", "Mantenimiento");
        filtroEstado.setValue(Set.of("Disponible", "Alquilado", "Mantenimiento"));
        filtroEstado.addValueChangeListener(e -> aplicarFiltros());

        add(filtroEstado);

        // Configuración de columnas de la tabla
        grid.addColumn(Vehiculo::getPlaca).setHeader("Placa");
        grid.addColumn(Vehiculo::getMarca).setHeader("Marca");
        grid.addColumn(Vehiculo::getModelo).setHeader("Modelo");
        grid.addColumn(Vehiculo::getAnio).setHeader("Año");
        grid.addColumn(Vehiculo::getTipo).setHeader("Tipo");
        grid.addColumn(v -> "$" + v.getTarifaDia()).setHeader("Tarifa/Día");
        grid.addColumn(Vehiculo::getEstado).setHeader("Estado");

        // Ficha técnica desplegable con Details e Image
        grid.setItemDetailsRenderer(new com.vaadin.flow.data.renderer.ComponentRenderer<>(vehiculo -> {
            VerticalLayout layoutDetalles = new VerticalLayout();
            
            Image foto = new Image(
                vehiculo.getFotoUrl() != null && !vehiculo.getFotoUrl().isEmpty() 
                    ? vehiculo.getFotoUrl() 
                    : "https://via.placeholder.com/150", 
                "Foto de " + vehiculo.getMarca()
            );
            foto.setWidth("150px");

            VerticalLayout info = new VerticalLayout(
                new Span("Detalles del Vehículo: " + vehiculo.getMarca() + " " + vehiculo.getModelo()),
                new Span("Placa: " + vehiculo.getPlaca()),
                new Span("Tarifa diaria: $" + vehiculo.getTarifaDia())
            );

            HorizontalLayout contenidoFicha = new HorizontalLayout(foto, info);
            Details fichaTecnica = new Details("Ver Ficha Técnica", contenidoFicha);
            fichaTecnica.setOpened(true);

            layoutDetalles.add(fichaTecnica);
            return layoutDetalles;
        }));

        grid.setItems(todosLosVehiculos);
        grid.setSizeFull();

        add(grid);
    }

    private void aplicarFiltros() {
        Set<String> estadosSeleccionados = filtroEstado.getValue();
        List<Vehiculo> filtrados = todosLosVehiculos.stream()
                .filter(v -> estadosSeleccionados.contains(v.getEstado()))
                .collect(Collectors.toList());
        grid.setItems(filtrados);
    }
}
