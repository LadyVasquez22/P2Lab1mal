package Modelo;

public abstract class Figura {
    protected double numA, numB, lado1, lado2, lado3;
    protected String jlErroA, jlErroB, jlErroL1, jlErroL2, jlErroL3;

    public Figura(double numA, double numB, double lado1, double lado2, double lado3) {
        this.numA = numA;
        this.numB = numB;
        this.lado1 = lado1;
        this.lado2 = lado2;
        this.lado3 = lado3;
    }

    public double getNumA() {
        return numA;
    }

    public void setNumA(double numA) {
        this.numA = numA;
    }

    public double getNumB() {
        return numB;
    }

    public void setNumB(double numB) {
        this.numB = numB;
    }

    public double getLado1() {
        return lado1;
    }

    public void setLado1(double lado1) {
        this.lado1 = lado1;
    }

    public double getLado2() {
        return lado2;
    }

    public void setLado2(double lado2) {
        this.lado2 = lado2;
    }

    public double getLado3() {
        return lado3;
    }

    public void setLado3(double lado3) {
        this.lado3 = lado3;
    }
    
    public abstract void getDataFigura();
    
}
