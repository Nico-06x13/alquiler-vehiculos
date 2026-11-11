package com.example.alquiler.alquiler;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Route(value = "reservas", layout = MainLayout.class)
@PageTitle("Reservas y Clientes | Alquiler")
public class ReservasView extends VerticalLayout {

    private TextField txtCedula = new TextField("Cédula / ID Cliente");
    private TextField txtNombre = new TextField("Nombre Completo");
    private TextField txtTelefono = new TextField("Teléfono");

    private ComboBox<Vehiculo> comboVehiculo = new ComboBox<>("Vehículo");
    private DatePicker fechaInicio = new DatePicker("Fecha Inicio");
    private DatePicker fechaFin = new DatePicker("Fecha Fin");
    private CheckboxGroup<String> extrasGroup = new CheckboxGroup<>();
    private Span costoCalculadoLabel = new Span("Costo Total Estimado: $0.0");

    private Grid<Reserva> gridReservas = new Grid<>(Reserva.class, false);

    public ReservasView() {
        setSizeFull();
        setPadding(true);

        add(new H2("Gestión de Reservas y Clientes"));

        FormLayout form = new FormLayout();

        List<Vehiculo> vehiculos = VehiculoService.cargarVehiculos();
        comboVehiculo.setItems(vehiculos);
        comboVehiculo.setItemLabelGenerator(v -> v.getMarca() + " " + v.getModelo() + " (" + v.getPlaca() + ") - $" + v.getTarifaDia() + "/día");

        extrasGroup.setLabel("Extras Seleccionados (por día):");
        extrasGroup.setItems("Seguro ($15.000/día)", "GPS ($10.000/día)", "Silla de bebé ($8.000/día)");
        extrasGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);

        fechaInicio.addValueChangeListener(e -> recalcularCosto());
        fechaFin.addValueChangeListener(e -> recalcularCosto());
        comboVehiculo.addValueChangeListener(e -> recalcularCosto());
        extrasGroup.addValueChangeListener(e -> recalcularCosto());

        form.add(txtCedula, txtNombre, txtTelefono, comboVehiculo, fechaInicio, fechaFin, extrasGroup);

        costoCalculadoLabel.getStyle().set("font-weight", "bold").set("font-size", "1.1em");

        Button btnGuardar = new Button("Confirmar Reserva", e -> procesarReserva());
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout acciones = new HorizontalLayout(btnGuardar, costoCalculadoLabel);
        acciones.setAlignItems(Alignment.CENTER);

        H3 tituloTabla = new H3("Historial de Reservas Registradas");
        gridReservas.addColumn(Reserva::getId).setHeader("ID Reserva");
        gridReservas.addColumn(Reserva::getCedulaCliente).setHeader("Cédula Cliente");
        gridReservas.addColumn(Reserva::getPlacaVehiculo).setHeader("Placa Vehículo");
        gridReservas.addColumn(Reserva::getFechaInicio).setHeader("Inicio");
        gridReservas.addColumn(Reserva::getFechaFin).setHeader("Fin");
        gridReservas.addColumn(Reserva::getExtras).setHeader("Extras");
        gridReservas.addColumn(r -> "$" + r.getCostoTotal()).setHeader("Costo Total");

        actualizarTabla();

        add(form, acciones, tituloTabla, gridReservas);
    }

    private void recalcularCosto() {
        if (comboVehiculo.getValue() != null && fechaInicio.getValue() != null && fechaFin.getValue() != null) {
            if (!fechaFin.getValue().isBefore(fechaInicio.getValue())) {
                double total = Reserva.calcularCostoTotal(
                    comboVehiculo.getValue().getTarifaDia(),
                    fechaInicio.getValue(),
                    fechaFin.getValue(),
                    extrasGroup.getValue()
                );
                costoCalculadoLabel.setText("Costo Total Estimado: $" + String.format("%.2f", total));
                return;
            }
        }
        costoCalculadoLabel.setText("Costo Total Estimado: $0.0");
    }

    private void procesarReserva() {
        if (txtCedula.isEmpty() || txtNombre.isEmpty() || comboVehiculo.isEmpty() || fechaInicio.isEmpty() || fechaFin.isEmpty()) {
            Notification.show("Por favor llena todos los campos obligatorios.", 3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        if (fechaFin.getValue().isBefore(fechaInicio.getValue())) {
            Notification.show("La fecha fin debe ser posterior a la fecha inicio.", 3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        Vehiculo v = comboVehiculo.getValue();

        if (!ReservaService.estaVehiculoDisponible(v.getPlaca(), fechaInicio.getValue(), fechaFin.getValue())) {
            Notification.show("El vehículo ya está ocupado en ese rango de fechas.", 3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        Cliente nuevoCliente = new Cliente(txtCedula.getValue().trim(), txtNombre.getValue().trim(), txtTelefono.getValue().trim());
        ClienteService.guardarCliente(nuevoCliente);

        double costo = Reserva.calcularCostoTotal(v.getTarifaDia(), fechaInicio.getValue(), fechaFin.getValue(), extrasGroup.getValue());
        String extrasStr = extrasGroup.getValue().isEmpty() ? "Ninguno" : String.join(", ", extrasGroup.getValue());
        String idReserva = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        Reserva nuevaReserva = new Reserva(idReserva, v.getPlaca(), txtCedula.getValue().trim(), fechaInicio.getValue(), fechaFin.getValue(), extrasStr, costo, false, 0.0);
        ReservaService.guardarReserva(nuevaReserva);

        Notification.show("¡Reserva completada con éxito! Código: " + idReserva, 3000, Notification.Position.BOTTOM_START).addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        limpiarFormulario();
        actualizarTabla();
    }

    private void actualizarTabla() {
        gridReservas.setItems(ReservaService.cargarReservas());
    }

    private void limpiarFormulario() {
        txtCedula.clear();
        txtNombre.clear();
        txtTelefono.clear();
        comboVehiculo.clear();
        fechaInicio.clear();
        fechaFin.clear();
        extrasGroup.clear();
        costoCalculadoLabel.setText("Costo Total Estimado: $0.0");
    }
}