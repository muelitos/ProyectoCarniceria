public class Pollo extends Producto {
    private String parte;
    private String proveedor;

    public Pollo(String id, String nombre, double kilos, double precioKilo, String parte, String proveedor) {
        super(id, nombre, kilos, precioKilo);
        this.parte = parte;
        this.proveedor = proveedor;
    }

    public String getParte() {
        return parte;
    }

    public void setParte(String parte) {
        this.parte = parte;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    @Override
    public String convertirCSV() {
        return "POLLO," + id + "," + nombre + "," + kilos + "," + precioKilo + "," + parte + "," + proveedor;
    }

    @Override
    public String convertirString() {
        return "[POLLO] " + super.convertirString() + " | Parte: " + parte + " | Proveedor: " + proveedor;
    }
}
