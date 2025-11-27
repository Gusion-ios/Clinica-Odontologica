import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;
import java.text.SimpleDateFormat;

public class Producto {

    private static Scanner leer = new Scanner(System.in);
    private static Set<Producto> listaProducto = new HashSet<>();

    private int idProducto;
    private String tipo;
    private String nombre;
    private int cantidad;
    private double precio;
    private Date fechaProduccion;
    private Date fechaVencimiento;

    public Producto(int idProducto, String tipo, String nombre, int cantidad, double precio, Date fechaProduccion, Date fechaVencimiento) {
        this.idProducto = idProducto;
        this.tipo = tipo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.fechaProduccion = fechaProduccion;
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public Date getFechaProduccion() { return fechaProduccion; }
    public void setFechaProduccion(Date fechaProduccion) { this.fechaProduccion = fechaProduccion; }

    public Date getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(Date fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    private static int generarIdAutomatico() {
        return listaProducto.size() + 1;
    }

    private static boolean existeProducto(String nombre) {
        for (Producto p : listaProducto) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    public static Producto buscarPorId(int id) {
        for (Producto p : listaProducto) {
            if (p.getIdProducto() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        return "\n--- PRODUCTO ---" +
                "\nID: " + idProducto +
                "\nTipo: " + tipo +
                "\nNombre: " + nombre +
                "\nCantidad: " + cantidad +
                "\nPrecio: S/. " + precio +
                "\nFecha Producción: " + f.format(fechaProduccion) +
                "\nFecha Vencimiento: " + f.format(fechaVencimiento) + "\n";
    }

    public static void registrarProducto() {

        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

            System.out.println("\n===== REGISTRAR PRODUCTO =====");

            int nuevoId = generarIdAutomatico();

            System.out.print("Ingrese tipo de producto: ");
            String tipo = leer.nextLine();

            System.out.print("Ingrese nombre del producto: ");
            String nombre = leer.nextLine();

            if (existeProducto(nombre)) {
                System.out.println("Ya existe un producto con ese nombre.");
                return;
            }

            System.out.print("Ingrese cantidad: ");
            int cantidad = Integer.parseInt(leer.nextLine());

            System.out.print("Ingrese precio: ");
            double precio = Double.parseDouble(leer.nextLine());

            System.out.print("Ingrese fecha de producción (dd/MM/yyyy): ");
            Date fp = formato.parse(leer.nextLine());

            System.out.print("Ingrese fecha de vencimiento (dd/MM/yyyy): ");
            Date fv = formato.parse(leer.nextLine());

            Producto nuevo = new Producto(nuevoId, tipo, nombre, cantidad, precio, fp, fv);
            listaProducto.add(nuevo);

            System.out.println("Producto registrado correctamente. ID: " + nuevoId);

        } catch (Exception e) {
            System.out.println("Error: formato incorrecto.");
        }
    }


    public static void buscarProductoPorId() {
        System.out.print("Ingrese ID a buscar: ");
        int id = leer.nextInt();
        leer.nextLine();

        Producto p = buscarPorId(id);

        if (p == null) {
            System.out.println("No se encontró el producto.");
        } else {
            System.out.println(p);
        }
    }


    public static void eliminarProducto() {
        System.out.print("Ingrese ID a eliminar: ");
        int id = leer.nextInt();
        leer.nextLine();

        Producto p = buscarPorId(id);

        if (p == null) {
            System.out.println("No existe ese producto.");
            return;
        }

        System.out.println("¿Seguro que desea eliminar '" + p.getNombre() + "'? (Si/No)");
        String r = leer.nextLine();

        if (r.equalsIgnoreCase("si")) {
            listaProducto.remove(p);
            System.out.println("Producto eliminado.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }


    public static void editarProducto() {
        try {
            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

            System.out.print("Ingrese ID a editar: ");
            int id = leer.nextInt();
            leer.nextLine();

            Producto p = buscarPorId(id);

            if (p == null) {
                System.out.println("No existe ese producto.");
                return;
            }

            System.out.print("Nuevo tipo: ");
            p.setTipo(leer.nextLine());

            System.out.print("Nuevo nombre: ");
            p.setNombre(leer.nextLine());

            System.out.print("Nueva cantidad: ");
            p.setCantidad(Integer.parseInt(leer.nextLine()));

            System.out.print("Nuevo precio: ");
            p.setPrecio(Double.parseDouble(leer.nextLine()));

            System.out.print("Nueva fecha producción (dd/MM/yyyy): ");
            p.setFechaProduccion(f.parse(leer.nextLine()));

            System.out.print("Nueva fecha vencimiento (dd/MM/yyyy): ");
            p.setFechaVencimiento(f.parse(leer.nextLine()));

            System.out.println("Producto editado correctamente.");

        } catch (Exception e) {
            System.out.println("Error en el formato ingresado.");
        }
    }


    public static void listarProductos() {
        if (listaProducto.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }

        for (Producto p : listaProducto) {
            System.out.println(p);
        }
    }

    public static void actualizarProducto() {
        Scanner leer = new Scanner(System.in);

        System.out.print("Ingrese ID del producto a actualizar: ");
        int id = leer.nextInt();
        leer.nextLine();

        Producto p = buscarPorId(id);

        if (p == null) {
            System.out.println("Producto no encontrado.");
            return;
        }

        System.out.println("Nuevo nombre: ");
        p.setNombre(leer.nextLine());

        System.out.println("Nuevo tipo: ");
        p.setTipo(leer.nextLine());

        System.out.println("Nueva cantidad: ");
        p.setCantidad(leer.nextInt());

        System.out.println("Nuevo precio: ");
        p.setPrecio(leer.nextDouble());

        System.out.println("Producto actualizado correctamente.");
    }

    public boolean estaVencido() {
        Date hoy = new Date();
        return fechaVencimiento.before(hoy);
    }

    public boolean stockBajo() {
        return this.cantidad < 5;
    }

    public long diasParaVencer() {
        long diferencia = fechaVencimiento.getTime() - new Date().getTime();
        return diferencia / (1000 * 60 * 60 * 24);
    }

    public static Producto buscarPorNombre(String nombre) {
        for (Producto p : listaProducto) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }

    public static void listarProductosVencidos() {
        boolean encontrado = false;
        for (Producto p : listaProducto) {
            if (p.estaVencido()) {
                System.out.println(p);
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("No hay productos vencidos.");
        }
    }

    public static void listarProductosPorVencer() {
        for (Producto p : listaProducto) {
            if (!p.estaVencido() && p.diasParaVencer() <= 7) {
                System.out.println(p + "Faltan: " + p.diasParaVencer() + " días");
            }
        }
    }

    public static void listarStockBajo() {
        for (Producto p : listaProducto) {
            if (p.stockBajo()) {
                System.out.println(p);
            }
        }
    }

    public void incrementarStock(int cant) {
        this.cantidad += cant;
    }

    public void reducirStock(int cant) {
        if (cant <= cantidad) {
            cantidad -= cant;
        } else {
            System.out.println("Stock insuficiente.");
        }
    }

}
