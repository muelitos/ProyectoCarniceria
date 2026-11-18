public class Cerdo extends Producto {
    private String corte;
    private String fechaVencimiento;

    public Cerdo(String id, String nombre, double kilos, double precioKilo, String corte, String fechaVencimiento) {
        super(id, nombre, kilos, precioKilo);
        this.corte = corte;
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getCorte() {
        return corte;
    }

    public void setCorte(String corte) {
        this.corte = corte;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    @Override
    public String convertirCSV() {
        return "CERDO," + id + "," + nombre + "," + kilos + "," + precioKilo + "," + corte + "," + fechaVencimiento;
    }

    @Override
    public String convertirString() {
        return "[CERDO] " + super.convertirString() + " | Corte: " + corte + " | Vence: " + fechaVencimiento;
    }
}
