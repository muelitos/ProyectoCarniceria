public class Producto {
    protected String id;
    protected String nombre;
    protected double kilos;
    protected double precioKilo;

    public Producto(String id, String nombre, double kilos, double precioKilo) {
        this.id = id;
        this.nombre = nombre;
        this.kilos = kilos;
        this.precioKilo = precioKilo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getKilos() {
        return kilos;
    }

    public void setKilos(double kilos) {
        this.kilos = kilos;
    }

    public double getPrecioKilo() {
        return precioKilo;
    }

    public void setPrecioKilo(double precioKilo) {
        this.precioKilo = precioKilo;
    }

    // Valor en dinero de lo que hay en inventario de este producto
    public double getValorTotal() {
        return kilos * precioKilo;
    }

    public String convertirCSV() {
        return "PRODUCTO," + id + "," + nombre + "," + kilos + "," + precioKilo;
    }

    
    public String convertirString() {
        return "ID: " + id + " | Nombre: " + nombre + " | Kilos: " + kilos
                + " | Precio/kg: $" + precioKilo + " | Valor total: $" + getValorTotal();
    }
}
