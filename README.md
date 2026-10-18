
<div align="center">
  <a href="https://git.io/typing-svg">
    <img src="https://readme-typing-svg.herokuapp.com?font=Fira+Code&size=36&pause=1000&color=7EF767&center=true&vCenter=true&width=700&lines=Inventory+Management+System" alt="Typing SVG" />
  </a>
</div>


## Project Description
A Java desktop application designed to manage warehouse inventory and its physical organization.
It supports the management of items, components, warehouses, shelves, and available stock quantities.
The system stores information in MariaDB and provides a JavaFX interface for accessing and managing inventory data.

## Project Tree Structure
 ```bash
Proyecto_AccesoConectores/
├── .gitignore
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            └── com/
                └── ut3g6/
                    └── aed_ut3_proyecto/
                        ├── AED_UT3_Proyecto.java
                        ├── controller/
                        │   ├── AlmacenarDAO.java
                        │   ├── ArticuloDAO.java
                        │   ├── ComponenteDAO.java
                        │   ├── ComponerDAO.java
                        │   ├── ConsultasAdicionalesDAO.java
                        │   ├── DepositoDAO.java
                        │   ├── EliminarRegistrosDAO.java
                        │   ├── EstanteDAO.java
                        │   ├── OperacionesAdicionalesDAO.java
                        │   ├── TotalArticulosDAO.java
                        │   └── TotalDepositosDAO.java
                        ├── model/
                        │   ├── Almacenar.java
                        │   ├── Articulo.java
                        │   ├── Componente.java
                        │   ├── Componer.java
                        │   ├── Conexion.java
                        │   ├── Deposito.java
                        │   └── Estante.java
                        └── view/
                            ├── ArticuloFormView.java
                            ├── ArticulosView.java
                            ├── ComponenteFormView.java
                            ├── ComponentesView.java
                            ├── ConfirmarEliminacionView.java
                            ├── DepositoFormView.java
                            ├── DepositosView.java
                            ├── EstanteFormView.java
                            ├── EstantesView.java
                            ├── GeneralTemplateView.java
                            ├── MainMenuView.java
                            └── StartView.java
  ```

## Architecture and Justification

The project follows a **desktop client–database architecture**, with a JavaFX user interface (more up-to-date than Java Swing), Java application logic, and a MariaDB relational database accessed through **JDBC** (MariaDB Connector/J).

This approach was chosen because it provides a straightforward way to persist structured inventory data, handle relationships between items and components, and perform database operations without requiring a separate web server. **Maven** manages project dependencies and builds, simplifying development and setup.

## Technologies

![JavaFX](https://img.shields.io/badge/JavaFX-21-0078D7?style=for-the-badge&logo=openjdk&logoColor=white)
![XAMPP](https://img.shields.io/badge/XAMPP-FB7A24?style=for-the-badge&logo=xampp&logoColor=white)
![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)

## Installation

### Requirements
- Java Development Kit (JDK) 21
- Apache NetBeans with Maven support
- XAMPP (MariaDB and phpMyAdmin) or an equivalent MariaDB installation
- The `almacen.sql` database initialization file supplied with the project

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/rin-5/Proyecto_AccesoConectores.git
   ```
2. Install **JDK 21**, **Apache NetBeans**, and **XAMPP**.
3. Start the **MySQL/MariaDB** service in the XAMPP control panel.
4. Open [phpMyAdmin](http://localhost/phpmyadmin/) and create a database named `almacen`.
5. Select the new database and import the `almacen.sql` file to create its tables and initial data.
6. Check the database settings in the Java `Conexion` class. The documented JDBC URL is:
   ```text
   jdbc:mariadb://localhost:3306/almacen
   ```
   Ensure that the configured username and password match your local MariaDB instance.
7. Open the folder containing `pom.xml` as a Maven project in NetBeans. Allow Maven to download the dependencies.
8. Select **Clean and Build**, then **Run Project**. Confirm that the application starts and can read inventory data.

For the complete configuration procedure, screenshots, checks, and troubleshooting steps, see the **[Installation Manual (PDF)](docs/Installation_Manual.pdf)**. (available on Campus).

## Additional Documentation

For more information about the project's functionality, interface, and usage, refer to **[AED_Trabajo_Unidad (PDF)](docs/AEDT_Trabajo_Unidad_2.pdf)** (special access permission required).
