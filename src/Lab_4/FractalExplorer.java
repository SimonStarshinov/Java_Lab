package Lab_4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class FractalExplorer {
    public static void main (String[] args){
        FractalExplorer fractalExplorer= new FractalExplorer(700);
        fractalExplorer.createAndShowGUI();
        fractalExplorer.drawFractal();
    }
    private int size;
    private FractalGenerator fractal;
    private Rectangle2D.Double area;
    private JImageDisplay display;

    public FractalExplorer (int size){
        this.size=size;
        area=new Rectangle2D.Double();
        fractal= new Mandelbrot();
        fractal.getInitialRange(area);
        display= new JImageDisplay(size, size);
    }

    //Создаёт и показывает графический интерфейс
    public void createAndShowGUI (){
        JFrame inter= new JFrame();
        JButton reset= new JButton("заново");
        ListenerButton listenerButton= new ListenerButton();
        ListenerMouse listenerMouse= new ListenerMouse();

        display.setLayout(new BorderLayout());

        reset.addActionListener(listenerButton);
        display.addMouseListener(listenerMouse);

        inter.add(display, BorderLayout.CENTER); //Расположение граф. интерфейса на экране
        inter.add(reset, BorderLayout.SOUTH); //расположение кнопки на экране
        inter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Чтобы приложение закрывалось с закрытием окна

        inter.pack ();
        inter.setVisible (true);
        inter.setResizable (false);
    }

    private void drawFractal (){
        for (int x=0; x<size; x++){
            for (int y=0; y<size; y++){
                //Превращает коррдинаты в координаты в пространстве фрактала
                double xCoord = FractalGenerator.getCoord (area.x, area.x + area.width, size, x);
                double yCoord = FractalGenerator.getCoord (area.y, area.y + area.height, size, y);

                //Если чис. итер. -1, красим в чёрный, или же подбираем нужный цвет
                if (fractal.numIterations(xCoord, yCoord) == -1) display.drawPixel(x,y,0);
                else {
                    float hue = 0.7f + (float) fractal.numIterations(xCoord, yCoord) / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    display.drawPixel(x, y, rgbColor);
                }
            }
        }
        display.repaint();//Обновляем изображение
    }

    //Вспомогательный класс для обработки нажатия
    private class ListenerButton implements ActionListener {

        @Override
        public void actionPerformed (ActionEvent event){
            fractal.getInitialRange(area);
            drawFractal();
        }
    }
    //Обработка нажатия мыши
    private class ListenerMouse extends MouseAdapter {

        @Override
        public void mouseClicked (MouseEvent event){
            double x = FractalGenerator.getCoord (area.x, area.x + area.width, size, event.getX());
            double y = FractalGenerator.getCoord (area.x, area.x + area.width, size, event.getY());
            fractal.recenterAndZoomRange(area, x, y, 0.5);
            drawFractal();
        }
    }
}
