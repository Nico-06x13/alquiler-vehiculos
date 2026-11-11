package com.example.alquiler.alquiler;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

public class MainLayout extends AppLayout {

    public MainLayout() {
        H1 title = new H1("Sistema de Alquiler de Vehículos");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

        DrawerToggle toggle = new DrawerToggle();

        addToNavbar(toggle, title);

        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Catálogo", VehiculosView.class, VaadinIcon.CAR.create()));
        nav.addItem(new SideNavItem("Gestión", GestionView.class, VaadinIcon.EDIT.create()));

        addToDrawer(nav);
    }
}