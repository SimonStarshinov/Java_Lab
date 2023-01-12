package Lab_6;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JImageDisplay extends JComponent {
    public BufferedImage imj; //Управляет изображением
    private int width , height;

    public JImageDisplay (int width, int height) {
        this.width = width;
        this.height = height;
        /*Создаем объект по заданным параметрам и типом изображения RGB - красные, зеленые и синие компоненты имеют по 8 битов*/
        imj = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Dimension dimension = new Dimension(this.width, this.height);// Передаём параметры длины и высоты для отображения картинки на экране
        super.setPreferredSize(dimension);
    }

    //Отрисовка изображения
    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.drawImage (imj, 0, 0, imj.getWidth(), imj.getHeight(), null);
    }

    //Перебирает все пиксели и красит их в чёрный
    public void clearImage (){
        for (int i=0; i<width; i++ ){
            for (int j=0; j<height; j++){
                imj.setRGB(i,j,0);  //Закращиваем
            }
        }

    }

    //Устанавливает 1 конкретный пиксель в установленный цвет
    public void  drawPixel (int x, int y, int rgbColor){
        imj.setRGB(x, y, rgbColor);

    }


}
