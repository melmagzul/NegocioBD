package NegocioDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class NegocioDB {
    private static final String URL = "jdbc:mysql://localhost:3306/bdnegocio";
    private static final String USER = "root";
    private static final String PASSWORD = "abc123**";

    public static Connection conectar() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos");
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
        return conexion;
    }

    public static void insertarProducto(String codigo, String nombre, double precio, int cantidad, String fecha) {
        String query = "INSERT INTO producto (codigoProducto, nombreProducto, precioUnitario, cantidadProducto, fechaVencimiento) VALUES (?,?,?,?,?)";
        try (Connection con = NegocioDB.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, codigo);
            pst.setString(2, nombre);
            pst.setDouble(3, precio);
            pst.setInt(4, cantidad);
            pst.setDate(5, java.sql.Date.valueOf(fecha));
            pst.executeUpdate();
            System.out.println("Producto insertado correctamente");
        } catch (SQLException e) {
            System.out.println("Error al insertar el producto: " + e.getMessage());
        }
    }

    public static void listarProductos() {
        String query = "SELECT * FROM producto;";
        try (Connection con = NegocioDB.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(query)) {
            boolean hayResultados = false;
            while (rs.next()) {
                hayResultados = true;
                System.out.println("Código: " + rs.getString("codigoProducto"));
                System.out.println("Nombre: " + rs.getString("nombreProducto"));
                System.out.println("Precio: " + rs.getDouble("precioUnitario"));
                System.out.println("Cantidad: " + rs.getInt("cantidadProducto"));
                System.out.println("Fecha de Vencimiento: " + rs.getDate("fechaVencimiento"));
                System.out.println("");
            }
            if (!hayResultados) {
                System.out.println("No hay productos disponibles.");
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los productos: " + e.getMessage());
        }
    }

    public static void actualizarProducto(String codigoProducto, String nombre, double precio) {
        String query = "UPDATE producto SET nombreProducto = ?, precioUnitario = ? WHERE codigoProducto = ?";
        try (Connection con = NegocioDB.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, nombre);
            pst.setDouble(2, precio);
            pst.setString(3, codigoProducto);
            pst.executeUpdate();
            System.out.println("Producto actualizado correctamente");
        } catch (SQLException e) {
            System.out.println("Error al actualizar el producto: " + e.getMessage());
        }
    }

    public static void eliminarProducto(String codigoProducto) {
        String query = "DELETE FROM producto WHERE codigoProducto = ?";
        try (Connection con = NegocioDB.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, codigoProducto);
            pst.executeUpdate();
            System.out.println("Producto eliminado correctamente");
        } catch (SQLException e) {
            System.out.println("Error al eliminar el producto: " + e.getMessage());
        }
    }

    public static void buscarProducto(String codigoProducto) {
        String query = "SELECT * FROM producto WHERE codigoProducto = ?";
        try (Connection con = NegocioDB.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, codigoProducto);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                System.out.println("Código: " + rs.getString("codigoProducto"));
                System.out.println("Nombre: " + rs.getString("nombreProducto"));
                System.out.println("Precio: " + rs.getDouble("precioUnitario"));
                System.out.println("Cantidad: " + rs.getInt("cantidadProducto"));
                System.out.println("Fecha de Vencimiento: " + rs.getDate("fechaVencimiento"));
            } else {
                System.out.println("No se encontró ningún producto con ese código.");
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar el producto: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("-------------------------------");
            System.out.println("        Menú principal         ");
            System.out.println("-------------------------------");
            System.out.println("1 Ingresar producto");
            System.out.println("2 Mostrar productos");
            System.out.println("3 Buscar producto");
            System.out.println("4 Modificar producto");
            System.out.println("5 Eliminar producto");
            System.out.println("6 Salir ");
            System.out.println("-------------------------------");
            System.out.print("Seleccione una opción del menú: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); 
            
            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el código del producto: ");
                    String codigo = scanner.nextLine();
                    System.out.print("Ingrese el nombre del producto: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Ingrese el precio del producto: ");
                    double precio = scanner.nextDouble();
                    System.out.print("Ingrese la cantidad del producto: ");
                    int cantidad = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer
                    System.out.print("Ingrese la fecha de vencimiento (YYYY-MM-DD): ");
                    String fecha = scanner.nextLine();
                    insertarProducto(codigo, nombre, precio, cantidad, fecha);
                    break;
                case 2:
                    listarProductos();
                    break;
                case 3:
                    System.out.print("Ingrese el código del producto a buscar: ");
                    String codigoBuscar = scanner.nextLine();
                    buscarProducto(codigoBuscar);
                    break;
                case 4:
                    System.out.print("Ingrese el código del producto a modificar: ");
                    String codigoModificar = scanner.nextLine();
                    System.out.print("Ingrese el nuevo nombre del producto: ");
                    String nuevoNombre = scanner.nextLine();
                    System.out.print("Ingrese el nuevo precio del producto: ");
                    double nuevoPrecio = scanner.nextDouble();
                    actualizarProducto(codigoModificar, nuevoNombre, nuevoPrecio);
                    break;
                case 5:
                    System.out.print("Ingrese el código del producto a eliminar: ");
                    String codigoEliminar = scanner.nextLine();
                    eliminarProducto(codigoEliminar);
                    break;
                case 6:
                    System.out.println("Saliendo del menú...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 6);

        scanner.close();
    }
}
