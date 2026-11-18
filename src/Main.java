import java.util.List;

public class Main {
    public static void main(String[] args) {
        String archivoPrueba = "inventario_carniceria.txt";
        GestorInventario gestor = new GestorInventario(archivoPrueba);

        System.out.println("=== 1. GUARDANDO PRODUCTOS EN ARCHIVO TXT ===");
        Res res1 = new Res("R101", "Lomo fino", 12.5, 48000, "Lomo", 21);
        Cerdo cerdo1 = new Cerdo("C201", "Costilla de cerdo", 8.0, 22000, "Costilla", "2026-11-25");
        Pollo pollo1 = new Pollo("P301", "Pechuga campesina", 15.0, 16000, "Pechuga", "Avícola El Dorado");

        gestor.guardarProducto(res1);
        gestor.guardarProducto(cerdo1);
        gestor.guardarProducto(pollo1);
        System.out.println("Productos guardados con éxito.");

        System.out.println("\n=== 2. LEYENDO INVENTARIO DESDE EL ARCHIVO TXT ===");
        mostrarLista(gestor.listarProductos());

        System.out.println("\n=== 3. ACTUALIZANDO PRODUCTO (ID: R101) - se vendieron 2.5 kg ===");
        Res resActualizada = new Res("R101", "Lomo fino", 10.0, 48000, "Lomo", 21);
        if (gestor.actualizarProducto("R101", resActualizada)) {
            System.out.println("Producto R101 actualizado correctamente.");
        }
        mostrarLista(gestor.listarProductos());

        System.out.println("\n=== 4. ELIMINANDO PRODUCTO (ID: P301) ===");
        if (gestor.eliminarProducto("P301")) {
            System.out.println("Producto P301 eliminado correctamente.");
        }
        mostrarLista(gestor.listarProductos());

        System.out.printf("%nValor total del inventario: $%,.0f%n", gestor.calcularValorInventario());
    }

    private static void mostrarLista(List<Producto> productos) {
        System.out.println("----- Inventario Actual -----");
        if (productos.isEmpty()) {
            System.out.println("(Inventario vacío)");
        } else {
            for (Producto p : productos) {
                System.out.println(p);
            }
        }
    }
}
