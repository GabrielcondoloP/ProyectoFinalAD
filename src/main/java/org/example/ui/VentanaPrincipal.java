package org.example.ui;

import org.example.db.GestorBaseDatos;
import org.example.model.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

// 1. La clase implementa ActionListener directamente
public class VentanaPrincipal extends JFrame implements ActionListener {

    private GestorBaseDatos db;
    private Long idSeleccionado = null;

    private JTextField txtNombre, txtDescripcion, txtPrecio, txtStock, txtBuscar;
    private JButton btnGuardar, btnEliminar, btnLimpiar, btnBuscar;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    public VentanaPrincipal() {
        db = new GestorBaseDatos();

        setTitle("Gestión de Productos");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        inicializarComponentes();
        cargarDatosEnTabla("");
    }

    private void inicializarComponentes() {
        // Panel izquierdo
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Producto"));
        panelFormulario.setPreferredSize(new Dimension(300, 0));

        panelFormulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextField();
        panelFormulario.add(txtDescripcion);

        panelFormulario.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        panelFormulario.add(txtPrecio);

        panelFormulario.add(new JLabel("Stock:"));
        txtStock = new JTextField();
        panelFormulario.add(txtStock);

        btnGuardar = new JButton("Guardar");
        btnLimpiar = new JButton("Limpiar");
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setEnabled(false);

        panelFormulario.add(btnGuardar);
        panelFormulario.add(btnLimpiar);
        panelFormulario.add(new JLabel(""));
        panelFormulario.add(btnEliminar);

        add(panelFormulario, BorderLayout.WEST);

        // Panel derecho
        JPanel panelTabla = new JPanel(new BorderLayout());

        JPanel panelBuscador = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBuscador.add(new JLabel("Buscar por nombre:"));
        txtBuscar = new JTextField(15);
        btnBuscar = new JButton("Buscar");
        panelBuscador.add(txtBuscar);
        panelBuscador.add(btnBuscar);
        panelTabla.add(panelBuscador, BorderLayout.NORTH);

        // 2. Tabla básica sin clases anónimas complejas
        String[] columnas = {"ID", "Nombre", "Descripción", "Precio", "Stock"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaProductos = new JTable(modeloTabla);
        panelTabla.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        add(panelTabla, BorderLayout.CENTER);

        // 3. Asignamos el "escuchador" a los botones
        btnGuardar.addActionListener(this);
        btnLimpiar.addActionListener(this);
        btnEliminar.addActionListener(this);
        btnBuscar.addActionListener(this);

        // Evento clásico para detectar clics en la tabla
        tablaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaProductos.getSelectedRow();
                if (fila != -1) {
                    idSeleccionado = (Long) modeloTabla.getValueAt(fila, 0);
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtDescripcion.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtPrecio.setText(modeloTabla.getValueAt(fila, 3).toString());
                    txtStock.setText(modeloTabla.getValueAt(fila, 4).toString());
                    btnEliminar.setEnabled(true);
                }
            }
        });
    }

    // 4. El clásico metodo gigante con if-else para los botones
    @Override
    public void actionPerformed(ActionEvent e) {

        // Si pulsamos LIMPIAR
        if (e.getSource() == btnLimpiar) {
            limpiarFormulario();
        }

        // Si pulsamos BUSCAR
        else if (e.getSource() == btnBuscar) {
            String textoBusqueda = txtBuscar.getText();
            cargarDatosEnTabla(textoBusqueda);
        }

        // Si pulsamos GUARDAR
        else if (e.getSource() == btnGuardar) {
            try {
                String nombre = txtNombre.getText();
                String desc = txtDescripcion.getText();
                double precio = Double.parseDouble(txtPrecio.getText());
                int stock = Integer.parseInt(txtStock.getText());

                if (idSeleccionado == null) {
                    // Es alta nueva
                    Producto p = new Producto(nombre, desc, precio, stock);
                    db.guardar(p);
                    JOptionPane.showMessageDialog(this, "Producto guardado.");
                } else {
                    // Es modificación
                    db.actualizar(idSeleccionado, nombre, desc, precio, stock);
                    JOptionPane.showMessageDialog(this, "Producto actualizado.");
                }
                limpiarFormulario();
                cargarDatosEnTabla("");
            } catch (Exception ex) {
                // Un catch
                JOptionPane.showMessageDialog(this, "Error en los datos. Revisa el precio y stock.");
                System.out.println("Error al guardar: " + ex.getMessage());
            }
        }

        // Si pulsamos ELIMINAR
        else if (e.getSource() == btnEliminar) {
            if (idSeleccionado != null) {
                db.eliminar(idSeleccionado);
                limpiarFormulario();
                cargarDatosEnTabla("");
                JOptionPane.showMessageDialog(this, "Producto borrado.");
            }
        }
    }

    private void cargarDatosEnTabla(String busqueda) {
        modeloTabla.setRowCount(0);

        List<Producto> lista;
        if (busqueda.equals("")) {
            lista = db.obtenerTodos();
        } else {
            lista = db.buscarPorNombre(busqueda);
        }

        for (int i = 0; i < lista.size(); i++) {
            Producto p = lista.get(i);
            Object[] fila = new Object[5];
            fila[0] = p.getId();
            fila[1] = p.getNombre();
            fila[2] = p.getDescripcion();
            fila[3] = p.getPrecio();
            fila[4] = p.getStock();
            modeloTabla.addRow(fila);
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        btnEliminar.setEnabled(false);
    }
}