package Controlador;

import Modelo.modelInsert;
import Modelo.mongoDB;
import Vista.Triangulo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;

public final class ControllerInsert implements ActionListener {
    protected Triangulo vista;
    protected modelInsert modelo;
    private mongoDB mongo = new mongoDB();
    private boolean esNuevoTriangulo;

    public ControllerInsert(Triangulo vista, modelInsert modelo) {
        this.vista = vista;
        this.modelo = modelo;
        mongo.createConnection();
        this.vista.btnLimpiar.addActionListener(this);
        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnModificar.addActionListener(this);
        this.vista.btnGuardar.addActionListener(this);
        cargarDatosTabla();
        limpiarCampos();
        limpiarValidadores();

        // Agregar un ListSelectionListener a la tabla
        vista.jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                vista.txtTipo.setEnabled(false);
                if (!e.getValueIsAdjusting()) { // Comprobar que la selección no está ajustando
                    int row = vista.jTable1.getSelectedRow(); // Obtener la fila seleccionada
                    if (row != -1) { // Verificar que se ha seleccionado una fila
                        // Obtener los valores de la fila seleccionada
                        double numA = Double.parseDouble(vista.jTable1.getValueAt(row, 0).toString());
                        double numB = Double.parseDouble(vista.jTable1.getValueAt(row, 1).toString());
                        double area = Double.parseDouble(vista.jTable1.getValueAt(row, 2).toString());
                        double lado1 = Double.parseDouble(vista.jTable1.getValueAt(row, 3).toString());
                        double lado2 = Double.parseDouble(vista.jTable1.getValueAt(row, 4).toString());
                        double lado3 = Double.parseDouble(vista.jTable1.getValueAt(row, 5).toString());
                        double resp = Double.parseDouble(vista.jTable1.getValueAt(row, 6).toString());

                        // Colocar los valores en los campos de texto
                        vista.txtNumA.setText(String.valueOf(numA));
                        vista.txtNumB.setText(String.valueOf(numB));
                        vista.txtLado1.setText(String.valueOf(lado1));
                        vista.txtLado2.setText(String.valueOf(lado2));
                        vista.txtLado3.setText(String.valueOf(lado3));
                        vista.jlÁrea.setText(String.valueOf(area));
                        vista.jlResp.setText(""); // Limpiar respuesta
                    }
                }
            }
        });
    }

    public void iniciarView() {
        vista.setVisible(true);
    }

    public void limpiarValidadores() {
        vista.jlErrorA.setText("");
        vista.jlErrorB.setText("");
        vista.jlErrorL1.setText("");
        vista.jlErrorL2.setText("");
        vista.jlErrorL3.setText("");
    }

    public void limpiarCampos() {
        vista.txtCedula.setEnabled(true);
        vista.jlÁrea.setText("");
        vista.txtNumA.setText("");
        vista.txtNumB.setText("");
        vista.txtLado1.setText("");
        vista.txtLado2.setText("");
        vista.txtLado3.setText("");
    }

    // Método para cargar los datos de MongoDB en la Tabla
    public void cargarDatosTabla() {
        DefaultTableModel tdm = mongo.cargarDataTable();
        vista.tbtDatos.setModel(tdm);
    }

    public void guardarDatos() {
        // Aquí debes implementar la lógica para guardar los datos del triángulo
        // Asegúrate de que el modelo y la vista estén correctamente configurados
        // Ejemplo de cómo guardar los datos:
        modelo.setNumA(Double.parseDouble(vista.txtNumA.getText()));
        modelo.setNumB(Double.parseDouble(vista.txtNumB.getText()));
        modelo.setLado1(Double.parseDouble(vista.txtLado1.getText()));
        modelo.setLado2(Double.parseDouble(vista.txtLado2.getText()));
        modelo.setLado3(Double.parseDouble(vista.txtLado3.getText()));

        // Crear un documento para MongoDB
        Document doc = new Document("Número A", modelo.getNumA())
                .append("Número B", modelo.getNumB())
                .append("Área", modelo.calcularArea())
                .append("Lado 1", modelo.getLado1())
                .append("Lado 2", modelo.getLado2())
                .append("Lado 3", modelo.getLado3())
                .append("Tipo", modelo.calcularTipo());

        // Guardar el documento en MongoDB
        mongo.insertDocument(doc);

        // Actualizar la tabla
        cargarDatosTabla();

        // Limpiar campos
        limpiarCampos();
        limpiarValidadores();
    }

    public void buscar() {
        // Aquí debes implementar la lógica para buscar un triángulo por su tipo
        // Asegúrate de que el modelo y la vista estén correctamente configurados
        // Ejemplo de cómo buscar un triángulo por su tipo:
        String tipoTriangulo = JOptionPane.showInputDialog(vista, "Ingrese el tipo de triángulo a buscar:");

        // Verificar que el tipo de triángulo no esté vacío
        if (tipoTriangulo == null || tipoTriangulo.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El tipo de triángulo no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear un filtro para buscar en la base de datos
        Document filtro = new Document("Tipo", tipoTriangulo);
        ArrayList<Document> resultados = mongo.searchDocument(filtro); // Asegúrate de que este método esté implementado en tu clase mongoDB

        DefaultTableModel modelo = (DefaultTableModel) vista.tbtDatos.getModel();

        // Variable para saber si se encontró el triángulo
        boolean trianguloEncontrado = false;

        // Recorrer la tabla y buscar el triángulo
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String tipoTabla = modelo.getValueAt(i, 6).toString(); // Asumiendo que el tipo está en la séptima columna (índice 6)
            if (tipoTabla.equalsIgnoreCase(tipoTriangulo)) {
                // Resaltar la fila del triángulo encontrado
                vista.tbtDatos.setRowSelectionInterval(i, i); // Resaltar la fila
                trianguloEncontrado = true;

                // Mostrar los datos del triángulo
                double numA = Double.parseDouble(modelo.getValueAt(i, 0).toString());
                double numB = Double.parseDouble(modelo.getValueAt(i, 1).toString());
                double area = Double.parseDouble(modelo.getValueAt(i, 2).toString());
                double lado1 = Double.parseDouble(modelo.getValueAt(i, 3).toString());
                double lado2 = Double.parseDouble(modelo.getValueAt(i, 4).toString());
                double lado3 = Double.parseDouble(modelo.getValueAt(i, 5).toString());

                JOptionPane.showMessageDialog(vista, "Triángulo encontrado:\n" +
                        "Número A: " + numA + "\n" +
                        "Número B: " + numB + "\n" +
                        "Área: " + area + "\n" +
                        "Lado 1: " + lado1 + "\n" +
                        "Lado 2: " + lado2 + "\n" +
                        "Lado 3: " + lado3 + "\n" +
                        "Tipo: " + tipoTriangulo, "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
                break; // Salir del bucle una vez que se encuentra el triángulo
            }
        }

        // Si no se encontró el triángulo, mostrar un mensaje
        if (!trianguloEncontrado) {
            JOptionPane.showMessageDialog(vista, "Triángulo no encontrado.", "Búsqueda", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnGuardar) {
            guardarDatos();
        } else if (e.getSource() == vista.btnBuscar) {
            buscar();
        } else if (e.getSource() == vista.btnEliminar) {
            // Implementar lógica para eliminar un triángulo
        } else if (e.getSource() == vista.btnModificar) {
            // Implementar lógica para modificar un triángulo
        } else if (e.getSource() == vista.btnLimpiar) {
            limpiarCampos();
            limpiarValidadores();
        }
    }
}