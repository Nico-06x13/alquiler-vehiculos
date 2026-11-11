package com.example.alquiler.alquiler;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "devoluciones", layout = MainLayout.class)
@PageTitle("Devoluciones y Multas | Alquiler")
public class DevolucionesView extends VerticalLayout {

    private ComboBox<Reserva> comboReserva = new ComboBox<>("Seleccionar Reserva Activa");
    private DatePicker fechaDevolucionReal = new DatePicker("Fecha de Devolución Real");
    private Span infoMulta = new Span("Multa por mora: $0.0");

    public DevolucionesView() {
        setSizeFull();
        setPadding(true);

        add(new H2("Registro de Devolución de Vehículo"));

        List<Reserva> pendientes = ReservaService.cargarReservas().stream()
                .filter(r -> !r.isDevuelto())
                .collect(Collectors.toList());

        comboReserva.setItems(pendientes);
        comboReserva.setItemLabelGenerator(r -> "Reserva " + r.getId() + " - Auto: " + r.getPlacaVehiculo() + " (Cliente: " + r.getCedulaCliente() + ")");

        fechaDevolucionReal.setValue(LocalDate.now());

        comboReserva.addValueChangeListener(e -> calcularMultaVista());
        fechaDevolucionReal.addValueChangeListener(e -> calcularMultaVista());

        FormLayout form = new FormLayout(comboReserva, fechaDevolucionReal);

        infoMulta.getStyle().set("font-weight", "bold").set("color", "red");

        Button btnRegistrar = new Button("Registrar Devolución", e -> procesarDevolucion());
        btnRegistrar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(form, infoMulta, btnRegistrar);
    }

    private void calcularMultaVista() {
        if (comboReserva.getValue() != null && fechaDevolucionReal.getValue() != null) {
            Reserva r = comboReserva.getValue();
            Vehiculo v = VehiculoService.cargarVehiculos().stream()
                    .filter(veh -> veh.getPlaca().equalsIgnoreCase(r.getPlacaVehiculo()))
                    .findFirst().orElse(null);

            double tarifa = (v != null) ? v.getTarifaDia() : 0.0;
            double multa = Reserva.calcularMulta(tarifa, r.getFechaFin(), fechaDevolucionReal.getValue());
            infoMulta.setText("Multa por mora: $" + String.format("%.2f", multa));
        }
    }

    private void procesarDevolucion() {
        if (comboReserva.isEmpty() || fechaDevolucionReal.isEmpty()) {
            Notification.show("Por favor selecciona una reserva y la fecha de entrega.");
            return;
        }

        Reserva r = comboReserva.getValue();
        Vehiculo v = VehiculoService.cargarVehiculos().stream()
                .filter(veh -> veh.getPlaca().equalsIgnoreCase(r.getPlacaVehiculo()))
                .findFirst().orElse(null);

        double tarifa = (v != null) ? v.getTarifaDia() : 0.0;
        double multa = Reserva.calcularMulta(tarifa, r.getFechaFin(), fechaDevolucionReal.getValue());

        List<Reserva> lista = ReservaService.cargarReservas();
        for (Reserva res : lista) {
            if (res.getId().equalsIgnoreCase(r.getId())) {
                res.setDevuelto(true);
                res.setMulta(multa);
                break;
            }
        }
        ReservaService.reagruparYGuardar(lista);

        Notification n = Notification.show("Devolución registrada con éxito. Multa cobrada: $" + multa);
        n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        // Recargar
        comboReserva.clear();
        infoMulta.setText("Multa por mora: $0.0");
    }
}