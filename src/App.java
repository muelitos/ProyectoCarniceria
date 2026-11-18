import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        // Ejecución segura en el Event Dispatch Thread (EDT) de Swing
        SwingUtilities.invokeLater(() -> new FormularioCarniceria().setVisible(true));
    }
}
