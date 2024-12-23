package Modelo;

import Vista.Triangulo;
import javax.swing.JOptionPane;
import java.awt.Color;

public class Triangulos extends Figura implements Interface {
    protected String tipo;
    protected double area;
    boolean validacion = true;

    public Triangulos(String tipo, double numA, double numB, double lado1, double lado2, double lado3) {
        super(numA, numB, lado1, lado2, lado3);
        this.tipo = tipo;
        this.area = calcularArea(); // Calcular el área al crear el triángulo
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public void getDataFigura() {
        JOptionPane.showMessageDialog(null, "Triángulo agregado con éxito!!!" +
                "\nNúmero A: " + getNumA() +
                "\nNúmero B: " + getNumB() +
                "\nLado 1: " + getLado1() +
                "\nLado 2: " + getLado2() +
                "\nLado 3: " + getLado3() +
                "\nTipo: " + getTipo() +
                "\nÁrea: " + area);
    }

    public boolean validarDatos(Triangulo vista) {
        boolean datosValidos = true;

        // CONTROL DE LA BASE
        try {
            double base = Double.parseDouble(vista.txtNumA.getText());
            if (base < 1) {
                vista.jlErrorA.setForeground(Color.RED);
                vista.jlErrorA.setText("Error: Rango incorrecto.");
                datosValidos = false;
            } else {
                vista.jlErrorA.setForeground(Color.GREEN);
                vista.jlErrorA.setText("Número válido: " + base);
                setNumA(base);
            }
        } catch (NumberFormatException e) {
            vista.jlErrorA.setForeground(Color.RED);
            vista.jlErrorA.setText("Error: Debe ingresar un número válido.");
            datosValidos = false;
        }

        // CONTROL DE LA ALTURA
        try {
            double altura = Double.parseDouble(vista.txtNumB.getText());
            if (altura < 1) {
                vista.jlErrorB.setForeground(Color.RED);
                vista.jlErrorB.setText("Error: Rango incorrecto.");
                datosValidos = false;
            } else {
                vista.jlErrorB.setForeground(Color.GREEN);
                vista.jlErrorB.setText("Número válido: " + altura);
                setNumB(altura);
            }
        } catch (NumberFormatException e) {
            vista.jlErrorB.setForeground(Color.RED);
            vista.jlErrorB.setText("Error: Debe ingresar un número válido.");
            datosValidos = false;
        }

        // CONTROL DE LADOS
        double[] lados = new double[3];
        for (int i = 0; i < 3; i++) {
            try {
                lados[i] = Double.parseDouble(vista.getLadoTextField(i).getText());
                if (lados[i] < 1) {
                    vista.getErrorLabel(i).setForeground(Color.RED);
                    vista.getErrorLabel(i).setText("Error: Rango incorrecto.");
                    datosValidos = false;
                } else {
                    vista.getErrorLabel(i).setForeground(Color.GREEN);
                    vista.getErrorLabel(i).setText("Número válido: " + lados[i]);
                    setLado(i, lados[i]);
                }
            } catch (NumberFormatException e) {
                vista.getErrorLabel(i).setForeground(Color.RED);
                vista.getErrorLabel(i).setText("Error: Debe ingresar un número válido.");
                datosValidos = false;
            }
        }

        // Clasificación del triángulo
        if (datosValidos) {
            if (lados[0] <= 0 || lados[1] <= 0 || lados[2] <= 0) {
                vista.jlResp.setText("Error: Los lados deben ser valores positivos.");
            } else if (lados[0] == lados[1] && lados[1] == lados[2]) {
                vista.jlResp.setText("Equilátero.");
            } else if (lados[0] == lados[1] || lados[0] == lados[2] || lados[1] == lados[2]) {
                vista.jlResp.setText("Isósceles.");
            } else {
                if (esRectangulo(lados[0], lados[1], lados[2])) {
                    vista.jlResp.setText("Rectángulo.");
                } else {
                    vista.jlResp.setText("Escaleno.");
                }
            }
        }
        return datosValidos;
    }

    private static boolean esRectangulo(double a, double b, double c) {
        double[] lados = {a, b, c};
        java.util.Arrays.sort(lados);
        return (lados[0] * lados[0] + lados[1] * lados[1] == lados[2] * lados[2]);
    }

    private void setLado(int index, double value) {
        switch (index) {
            case 0:
                setLado1(value);
                break;
            case 1:
                setLado2(value);
                break;
            case 2:
                setLado3(value);
                break;
        }
    }
}