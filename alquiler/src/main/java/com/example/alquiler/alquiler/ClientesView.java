package com.example.alquiler.alquiler;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "clientes", layout = MainLayout.class)
@PageTitle("Clientes | Alquiler")
public class ClientesView extends VerticalLayout {

    private TextField cedula = new TextField("Cédula / ID");
    private TextField nombre = new TextField("Nombre Completo");
    private TextField telefono = new TextField("Teléfono");

    private Grid<Cliente> gridClientes = new Grid<>(Cliente.class, false);

    public ClientesView() {
        setSizeFull();
        setPadding(true);

        add(new H2("Registro y Gestión de Clientes"));

        HorizontalLayout form = new HorizontalLayout(cedula, nombre, telefono);
        form.setVerticalComponentAlignment(Alignment.END);

        Button btnGuardar = new Button("Registrar Cliente", e -> registrarCliente());
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        form.add(btnGuardar);

        gridClientes.addColumn(Cliente::getCedula).setHeader("Cédula");
        gridClientes.addColumn(Cliente::getNombre).setHeader("Nombre");
        gridClientes.addColumn(Cliente::getTelefono).setHeader("Teléfono");

        add(form, gridClientes);
        actualizarTabla();
    }

    private void registrarCliente() {
        if (cedula.isEmpty() || nombre.isEmpty()) {
            Notification.show("Por favor completa al menos Cédula y Nombre.");
            return;
        }

        Cliente nuevo = new Cliente(cedula.getValue().trim(), nombre.getValue().trim(), telefono.getValue().trim());
        ClienteService.guardarCliente(nuevo);
        
        Notification.show("Cliente registrado correctamente.");
        cedula.clear();
        nombre.clear();
        telefono.clear();
        actualizarTabla();
    }

    private void actualizarTabla() {
        List<Cliente> lista = ClienteService.cargarClientes();
        gridClientes.setItems(lista);
    }
}