package com.example.alquiler.alquiler;

import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.Set;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Catálogo | Alquiler de Vehículos")
public class VehiculosView extends VerticalLayout {

    private final Grid<Vehiculo> grid = new Grid<>(Vehiculo.class, false);
    private final CheckboxGroup<String> filtroEstado = new CheckboxGroup<>();

    public VehiculosView() {
        setSizeFull();
        setSpacing(true);

        // 1. Configurar Filtro por Estado
        filtroEstado.setLabel("Filtrar por Estado:");
        filtroEstado.setItems("Disponible", "Alquilado", "Mantenimiento");
        filtroEstado.setValue(Set.of("Disponible", "Alquilado", "Mantenimiento"));

        filtroEstado.addValueChangeListener(e -> actualizarTabla());

        // 2. Configurar Grid
        grid.addColumn(Vehiculo::getPlaca).setHeader("Placa").setAutoWidth(true);
        grid.addColumn(Vehiculo::getMarca).setHeader("Marca").setAutoWidth(true);
        grid.addColumn(Vehiculo::getModelo).setHeader("Modelo").setAutoWidth(true);
        grid.addColumn(Vehiculo::getAnio).setHeader("Año").setAutoWidth(true);
        grid.addColumn(Vehiculo::getTipo).setHeader("Tipo").setAutoWidth(true);
        grid.addColumn(v -> "$" + v.getTarifaDia()).setHeader("Tarifa/Día").setAutoWidth(true);
        grid.addColumn(Vehiculo::getEstado).setHeader("Estado").setAutoWidth(true);

        // 3. Ficha Técnica desplegable (Details + Image)
        grid.setItemDetailsRenderer(new ComponentRenderer<>(vehiculo -> {
            HorizontalLayout detallesLayout = new HorizontalLayout();
            detallesLayout.setPadding(true);
            detallesLayout.setSpacing(true);

            String imagenUrl = obtenerUrlImagen(vehiculo);

            Image foto = new Image(imagenUrl, "Foto de " + vehiculo.getMarca() + " " + vehiculo.getModelo());
            foto.setWidth("180px");
            foto.setHeight("120px");
            foto.getStyle().set("object-fit", "cover");
            foto.getStyle().set("border-radius", "8px");

            VerticalLayout infoLayout = new VerticalLayout();
            infoLayout.add(
                new Span("Detalles del Vehículo: " + vehiculo.getMarca() + " " + vehiculo.getModelo()),
                new Span("Placa: " + vehiculo.getPlaca()),
                new Span("Tarifa diaria: $" + vehiculo.getTarifaDia())
            );

            detallesLayout.add(foto, infoLayout);
            return new Details("Ver Ficha Técnica", detallesLayout);
        }));

        add(filtroEstado, grid);
        actualizarTabla();
    }

    private void actualizarTabla() {
        Set<String> estadosSeleccionados = filtroEstado.getValue();
        List<Vehiculo> todos = VehiculoService.cargarVehiculos();

        List<Vehiculo> filtrados = todos.stream()
                .filter(v -> estadosSeleccionados.contains(v.getEstado()))
                .toList();

        grid.setItems(filtrados);
    }

    private String obtenerUrlImagen(Vehiculo v) {
        String marcaModelo = (v.getMarca() + " " + v.getModelo()).toLowerCase();

        if (marcaModelo.contains("duster") || marcaModelo.contains("renault")) {
            return "https://acroadtrip.blob.core.windows.net/catalogo-imagenes/m/RT_V_16dd66c8cf0c41d8a79d312ba7d5e6b6.webp";
        } else if (marcaModelo.contains("tracker") || marcaModelo.contains("chevrolet")) {
            return "https://www.chevrolet.com.co/content/dam/chevrolet/south-america/colombia/espanol/index/crossovers-and-suvs/2026-tracker/1-mh/mh-chevrolet-tracker-mobile.jpg?imwidth=1600";
        } else if (marcaModelo.contains("corolla") || marcaModelo.contains("toyota")) {
            return "https://autoamerica.com.co/wp-content/uploads/2020/08/1000x535-COROLLA-SEDAN-XEI-V-9-25-.jpg.avif";
        }

        return "https://acroadtrip.blob.core.windows.net/catalogo-imagenes/m/RT_V_16dd66c8cf0c41d8a79d312ba7d5e6b6.webp";
    }
}