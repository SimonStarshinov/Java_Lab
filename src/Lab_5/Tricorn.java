package Lab_5;

import java.awt.geom.Rectangle2D;

public class Tricorn extends FractalGenerator {
    public static final int MAX_ITERATIONS = 2000; // Предельное количество итераций

    @Override
    public void getInitialRange(Rectangle2D.Double range){  // Определеяет размер фрактала и его координаты
        range.x = -2;
        range.y = -2;
        range.width = 4;
        range.height = 4;
    }

    @Override
    public int numIterations(double real, double im){  // реализует итеративную функцию для фрактала Мандельброта
        Complex z = new Complex(0, 0);
        Complex c = new Complex(real, im);
        int i = 0;
        while (z.moduleSqr() <= 4 && i < MAX_ITERATIONS){
            z = z.conjugation().sqr().add(c);  // Функция фрактала - z = z^2 + c, но z сопряженное
            i++;
        }
        if (i == MAX_ITERATIONS) return -1;
        return i;
    }

    public String toString(){
        return "Tricorn";
    }
}