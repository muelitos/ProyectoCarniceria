import java.awt.*;
import java.util.List;
import javax.swing.*;


public class FormularioCarniceria extends JFrame {

    private final GestorInventario gestor;

    private final JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Res", "Cerdo", "Pollo"});
    private final JTextField txtId = new JTextField(15);
    private final JTextField txtNombre = new JTextField(15);
    private final JTextField txtKilos = new JTextField(15);
    private final JTextField txtPrecioKilo = new JTextField(15);

    private final JLabel lblCampoEspecial1 = new JLabel("Corte:");
    private final JTextField txtCampoEspecial1 = new JTextField(15);

    private final JLabel lblCampoEspecial2 = new JLabel("Días de maduración:");
    private final JTextField txtCampoEspecial2 = new JTextField(15);

    private final JTextArea areaSalida = new JTextArea(12, 60);
    private final JLabel lblValorTotal = new JLabel("Valor total del inventario: $0");

    public FormularioCarniceria() {
        super("Inventario Carnicería - Persistencia en .TXT (POO)");

        this.gestor = new GestorInventario("inventario_carniceria.txt");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelEntradas = new JPanel(new GridLayout(7, 2, 8, 8));
        panelEntradas.setBorder(BorderFactory.createTitledBorder(" Datos del Producto "));

        panelEntradas.add(new JLabel("Tipo de Carne:"));
        panelEntradas.add(comboTipo);

        panelEntradas.add(new JLabel("Código (ID):"));
        panelEntradas.add(txtId);

        panelEntradas.add(new JLabel("Nombre del Producto:"));
        panelEntradas.add(txtNombre);

        panelEntradas.add(new JLabel("Cantidad (kg):"));
        panelEntradas.add(txtKilos);

        panelEntradas.add(new JLabel("Precio por kilo ($):"));
        panelEntradas.add(txtPrecioKilo);

        panelEntradas.add(lblCampoEspecial1);
        panelEntradas.add(txtCampoEspecial1);

        panelEntradas.add(lblCampoEspecial2);
        panelEntradas.add(txtCampoEspecial2);

        add(panelEntradas, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnGuardar = new JButton("Guardar (Create)");
        JButton btnListar = new JButton("Listar Todo (Read)");
        JButton btnActualizar = new JButton("Actualizar (Update)");
        JButton btnEliminar = new JButton("Eliminar (Delete)");
        JButton btnLimpiar = new JButton("Limpiar Campos");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnListar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        add(panelBotones, BorderLayout.CENTER);

        JPanel panelSalida = new JPanel(new BorderLayout());
        panelSalida.setBorder(BorderFactory.createTitledBorder(" Contenido del Inventario (.TXT) "));

        areaSalida.setEditable(false);
        areaSalida.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panelSalida.add(new JScrollPane(areaSalida), BorderLayout.CENTER);

        lblValorTotal.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblValorTotal.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelSalida.add(lblValorTotal, BorderLayout.SOUTH);

        add(panelSalida, BorderLayout.SOUTH);

        comboTipo.addActionListener(e -> actualizarEtiquetasFormulario());

        btnGuardar.addActionListener(e -> guardarRegistro());
        btnListar.addActionListener(e -> listarRegistros());
        btnActualizar.addActionListener(e -> actualizarRegistro());
        btnEliminar.addActionListener(e -> eliminarRegistro());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        listarRegistros();

        pack();
        setLocationRelativeTo(null);
    }

    private void actualizarEtiquetasFormulario() {
        String seleccion = (String) comboTipo.getSelectedItem();
        if ("Res".equals(seleccion)) {
            lblCampoEspecial1.setText("Corte:");
            lblCampoEspecial2.setText("Días de maduración:");
        } else if ("Cerdo".equals(seleccion)) {
            lblCampoEspecial1.setText("Corte:");
            lblCampoEspecial2.setText("Fecha de vencimiento:");
        } else if ("Pollo".equals(seleccion)) {
            lblCampoEspecial1.setText("Parte (pechuga, muslo...):");
            lblCampoEspecial2.setText("Proveedor:");
        }
    }

    private Producto construirProductoDesdeCampos() throws NumberFormatException {
        String id = txtId.getText().trim().replace(",", " ");
        String nombre = txtNombre.getText().trim().replace(",", " ");

        if (id.isEmpty() || nombre.isEmpty()) {
            throw new IllegalArgumentException("Los campos Código y Nombre no pueden estar vacíos.");
        }

        double kilos = Double.parseDouble(txtKilos.getText().trim());
        double precioKilo = Double.parseDouble(txtPrecioKilo.getText().trim());

        if (kilos < 0 || precioKilo < 0) {
            throw new IllegalArgumentException("Los kilos y el precio no pueden ser negativos.");
        }

        // Se quitan comas para no romper el formato CSV del archivo
        String especial1 = txtCampoEspecial1.getText().trim().replace(",", " ");
        String especial2 = txtCampoEspecial2.getText().trim().replace(",", " ");

        if (especial1.isEmpty() || especial2.isEmpty()) {
            throw new IllegalArgumentException("Completa los dos campos específicos del tipo de carne.");
        }

        String tipo = (String) comboTipo.getSelectedItem();

        if ("Res".equals(tipo)) {
            int diasMaduracion = Integer.parseInt(especial2);
            return new Res(id, nombre, kilos, precioKilo, especial1, diasMaduracion);
        } else if ("Cerdo".equals(tipo)) {
            return new Cerdo(id, nombre, kilos, precioKilo, especial1, especial2);
        } else {
            return new Pollo(id, nombre, kilos, precioKilo, especial1, especial2);
        }
    }

    private void guardarRegistro() {
        try {
            Producto nuevoProducto = construirProductoDesdeCampos();

            if (gestor.existeId(nuevoProducto.getId())) {
                JOptionPane.showMessageDialog(this, "Ya existe un producto con el código: " + nuevoProducto.getId()
                        + "\nUsa 'Actualizar' si quieres modificarlo.", "Código Repetido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean exito = gestor.guardarProducto(nuevoProducto);

            if (exito) {
                JOptionPane.showMessageDialog(this, "Producto guardado correctamente en el inventario.");
                limpiarCampos();
                listarRegistros();
            } else {
                JOptionPane.showMessageDialog(this, "Ocurrió un error al guardar en el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Verifica que Kilos, Precio y Días de maduración sean números válidos.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void listarRegistros() {
        List<Producto> productos = gestor.listarProductos();
        areaSalida.setText("");

        if (productos.isEmpty()) {
            areaSalida.setText("El inventario está vacío.");
        } else {
            for (Producto p : productos) {
                areaSalida.append(p.toString() + "\n");
            }
        }
        lblValorTotal.setText(String.format("Valor total del inventario: $%,.0f", gestor.calcularValorInventario()));
    }

    private void actualizarRegistro() {
        try {
            Producto productoActualizado = construirProductoDesdeCampos();
            boolean exito = gestor.actualizarProducto(productoActualizado.getId(), productoActualizado);

            if (exito) {
                JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");
                limpiarCampos();
                listarRegistros();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún producto con el código: " + productoActualizado.getId(), "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Para actualizar, completa todos los campos correctamente e ingresa el código a modificar.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarRegistro() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el código del producto que deseas eliminar en el campo 'Código'.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar el producto con código: " + id + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean exito = gestor.eliminarProducto(id);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Producto eliminado correctamente del inventario.");
                limpiarCampos();
                listarRegistros();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún producto con ese código.", "No Encontrado", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtKilos.setText("");
        txtPrecioKilo.setText("");
        txtCampoEspecial1.setText("");
        txtCampoEspecial2.setText("");
        txtId.requestFocus();
    }
}
