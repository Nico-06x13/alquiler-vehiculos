package com.example.alquiler.alquiler;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "gestion", layout = MainLayout.class)
@PageTitle("Gestión de Vehículos")
public class GestionView extends VerticalLayout {

    private final TextField placa = new TextField("Placa");
    private final TextField marca = new TextField("Marca");
    private final TextField modelo = new TextField("Modelo");
    private final NumberField anio = new NumberField("Año");
    private final ComboBox<String> tipo = new ComboBox<>("Tipo");
    private final NumberField tarifaDia = new NumberField("Tarifa por Día ($)");
    private final ComboBox<String> estado = new ComboBox<>("Estado");
    private final TextField fotoUrl = new TextField("URL de la Foto");

    public GestionView() {
        setSizeFull();
        setPadding(true);

        add(new H2("Registrar Nuevo Vehículo"));

        tipo.setItems("Sedán", "SUV", "Camioneta", "Deportivo", "Hatchback");
        estado.setItems("Disponible", "Alquilado", "Mantenimiento");

        FormLayout formLayout = new FormLayout();
        formLayout.add(placa, marca, modelo, anio, tipo, tarifaDia, estado, fotoUrl);

        Button btnGuardar = new Button("Guardar Vehículo", e -> confirmarGuardado());
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button btnLimpiar = new Button("Limpiar Campos", e -> limpiarFormulario());

        HorizontalLayout botones = new HorizontalLayout(btnGuardar, btnLimpiar);

        add(formLayout, botones);
    }

    private void confirmarGuardado() {
        if (placa.isEmpty() || marca.isEmpty() || modelo.isEmpty()) {
            Notification notification = Notification.show("Por favor completa los campos obligatorios.");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Confirmar Registro");
        dialog.add(new Span("¿Deseas guardar el vehículo con placa " + placa.getValue() + "?"));

        Button btnConfirmar = new Button("Confirmar", e -> {
            // 1. Capturar la URL/Ruta digitada en el campo del formulario
            String urlImagen = fotoUrl.getValue();
            if (urlImagen == null || urlImagen.trim().isEmpty()) {
                urlImagen = "images/duster.png"; // Fallback si no escriben nada
            }

            // 2. Extraer los valores numéricos y nulos de forma segura
            int anioVal = (anio.getValue() != null) ? anio.getValue().intValue() : 2024;
            String tipoVal = (tipo.getValue() != null) ? tipo.getValue() : "Sedán";
            double tarifaVal = (tarifaDia.getValue() != null) ? tarifaDia.getValue() : 0.0;
            String estadoVal = (estado.getValue() != null) ? estado.getValue() : "Disponible";

            // 3. Crear el objeto Vehiculo pasando la imagen dinámica digitada
            Vehiculo nuevo = new Vehiculo(
                placa.getValue().toUpperCase().trim(),
                marca.getValue().trim(),
                modelo.getValue().trim(),
                anioVal,
                tipoVal,
                tarifaVal,
                estadoVal,
                urlImagen.trim()
            );

            // 4. Guardar en el servicio estático
            VehiculoService.guardarVehiculo(nuevo);
            dialog.close();

            Notification notification = Notification.show("¡Vehículo guardado con éxito!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            limpiarFormulario();
        });
        btnConfirmar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button btnCancelar = new Button("Cancelar", e -> dialog.close());

        dialog.getFooter().add(btnCancelar, btnConfirmar);
        dialog.open();
    }

    private void limpiarFormulario() {
        placa.clear();
        marca.clear();
        modelo.clear();
        anio.clear();
        tipo.clear();
        tarifaDia.clear();
        estado.clear();
        fotoUrl.clear();
    }
}