import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorInventario {
    private final String rutaArchivo;

    public GestorInventario(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        asegurarArchivoExiste();
    }

    private void asegurarArchivoExiste() {
        File archivo = new File(rutaArchivo);
        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error al crear el archivo del inventario: " + e.getMessage());
        }
    }

    //Crea el archivo si no existe y lo deja vacío
    public boolean guardarProducto(Producto producto) {
        // try-with-resources con FileWriter en modo append (true)
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(rutaArchivo, true)))) {
            pw.println(producto.convertirCSV());
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar en el archivo: " + e.getMessage());
            return false;
        }
    }

    //lee el archivo y devuelve una lista de productos, incluyendo Res, Cerdo y Pollo
    public List<Producto> listarProductos() {
        List<Producto> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                String[] partes = linea.split(",");
                if (partes.length < 5) continue;

                try {
                    String tipo = partes[0];
                    String id = partes[1];
                    String nombre = partes[2];
                    double kilos = Double.parseDouble(partes[3]);
                    double precioKilo = Double.parseDouble(partes[4]);

                    if (tipo.equals("RES") && partes.length >= 7) {
                        String corte = partes[5];
                        int diasMaduracion = Integer.parseInt(partes[6]);
                        lista.add(new Res(id, nombre, kilos, precioKilo, corte, diasMaduracion));
                    } else if (tipo.equals("CERDO") && partes.length >= 7) {
                        String corte = partes[5];
                        String fechaVencimiento = partes[6];
                        lista.add(new Cerdo(id, nombre, kilos, precioKilo, corte, fechaVencimiento));
                    } else if (tipo.equals("POLLO") && partes.length >= 7) {
                        String parte = partes[5];
                        String proveedor = partes[6];
                        lista.add(new Pollo(id, nombre, kilos, precioKilo, parte, proveedor));
                    } else if (tipo.equals("PRODUCTO")) {
                        lista.add(new Producto(id, nombre, kilos, precioKilo));
                    }
                } catch (NumberFormatException e) {
                    // Si una línea está dañada se salta, pero se siguen leyendo las demás
                    System.err.println("Línea con formato inválido, se omite: " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return lista;
    }

    //actualiza los productos en el archivo, reemplazando el producto con el mismo ID por el nuevo producto
    public boolean actualizarProducto(String id, Producto productoActualizado) {
        List<Producto> productos = listarProductos();
        boolean encontrado = false;

        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId().equalsIgnoreCase(id)) {
                productos.set(i, productoActualizado);
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            reescribirArchivo(productos);
        }
        return encontrado;
    }

    // Elimina un producto por su ID y reescribe el archivo
    public boolean eliminarProducto(String id) {
        List<Producto> productos = listarProductos();
        boolean eliminado = productos.removeIf(p -> p.getId().equalsIgnoreCase(id));

        if (eliminado) {
            reescribirArchivo(productos);
        }
        return eliminado;
    }

    // Busca si ya existe un producto con ese ID (para no repetir IDs)
    public boolean existeId(String id) {
        for (Producto p : listarProductos()) {
            if (p.getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    // Suma el valor en dinero de todo el inventario
    public double calcularValorInventario() {
        double total = 0;
        for (Producto p : listarProductos()) {
            total += p.getValorTotal();
        }
        return total;
    }

    private void reescribirArchivo(List<Producto> productos) {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(rutaArchivo, false)))) {
            for (Producto p : productos) {
                pw.println(p.convertirCSV());
            }
        } catch (IOException e) {
            System.err.println("Error al reescribir el archivo: " + e.getMessage());
        }
    }
}
