# Sistema de Alquiler de Vehículos

Aplicación web desarrollada en **Java** con **Spring Boot** y **Vaadin Flow** para la gestión y alquiler de vehículos. Proyecto final enfocado en la construcción de una interfaz dinámica, responsiva y con manejo de persistencia local en archivos de texto.

---

## Tecnologías Utilizadas

- **Lenguaje:** Java 17+
- **Framework Backend:** Spring Boot 3
- **Framework Frontend/UI:** Vaadin Flow (UI programada en Java)
- **Persistencia:** Archivos de texto plano (`.txt`)
- **Control de Versiones:** Git & GitHub

---

## Funcionalidades Principales

1. **Catálogo de Vehículos (`VehiculosView`):**
   - Visualización de la flota de vehículos en un componente `Grid`.
   - Filtro interactivo multi-estado mediante `CheckboxGroup` (Disponible, Alquilado, Mantenimiento).
   - Ficha técnica desplegable con detalles extendidos e imágenes por vehículo mediante el componente `Details` e `Image`.

2. **Gestión de Vehículos (`GestionView`):**
   - Formulario completo para la creación y registro de nuevos vehículos.
   - Selección de tipos y estados mediante `ComboBox`.
   - Diálogo de confirmación antes de guardar (`Dialog`).
   - Notificaciones contextuales al usuario (`Notification`).

3. **Navegación:**
   - Menú lateral responsivo implementado con `AppLayout` y `SideNav`.

---

## Instrucciones de Ejecución

1. Clonar el repositorio:
   ```bash
   git clone [https://github.com/Nico-06x13/alquiler-vehiculos.git](https://github.com/Nico-06x13/alquiler-vehiculos.git)