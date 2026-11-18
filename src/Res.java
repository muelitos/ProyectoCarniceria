public class Res extends Producto {
    private String corte;
    private int diasMaduracion;

    public Res(String id, String nombre, double kilos, double precioKilo, String corte, int diasMaduracion) {
        super(id, nombre, kilos, precioKilo);
        this.corte = corte;
        this.diasMaduracion = diasMaduracion;
    }

    public String getCorte() {
        return corte;
    }

    public void setCorte(String corte) {
        this.corte = corte;
    }

    public int getDiasMaduracion() {
        return diasMaduracion;
    }

    public void setDiasMaduracion(int diasMaduracion) {
        this.diasMaduracion = diasMaduracion;
    }

    @Override
    public String toCSV() {
        return "RES," + id + "," + nombre + "," + kilos + "," + precioKilo + "," + corte + "," + diasMaduracion;
    }

    @Override
    public String toString() {
        return "[RES] " + super.toString() + " | Corte: " + corte + " | Días de maduración: " + diasMaduracion;
    }
}
