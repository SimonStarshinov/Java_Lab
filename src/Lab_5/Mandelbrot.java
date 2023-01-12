package Lab_5;

import java.awt.geom.Rectangle2D;

//Высчитывает фрактал
public class Mandelbrot extends FractalGenerator {
private static final int MAX_ITERATIONS = 2000;

    //Определяет начальныый диапазон для фракталов (точка с которой начинается фрактал)
    @Override
    public void getInitialRange (Rectangle2D.Double range){
        range.x= -2;
        range.y= -1.5;
        range.width= 3;
        range.height=3;
    }

    //
    @Override
    public int numIterations(double real, double im){
        Complex z = new Complex(0,0);
        Complex c = new Complex(real, im);
        int i = 0;
        while (z.moduleSqr() <= 4 && i < MAX_ITERATIONS){
            z= z.sqr().add(c); //Ф-ия фрактала, где z= z^2+c
            i++;
        }
        if (i == MAX_ITERATIONS) return -1;
        return i;

    }
    @Override
    public String toString(){
        return "Mandelbrot";
    }
}
