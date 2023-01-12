package Lab_5;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.Objects;

public class FractalExplorer {

    public static void main(String[] args) {
        FractalExplorer fractalExplorer = new FractalExplorer(700);
        fractalExplorer.createAndShowGUI();
        fractalExplorer.drawFractal();
    }

    // Вспомогательный класс для обработки нажатия на кнопку
    private class ListenerButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            /*Оператор, выбирает один из 2-х значенний case и выполняет набор команд следующих за оператором */
            switch (event.getActionCommand()) {
                case ("Reset") -> {  // 1 значение case (для кнопки сброса)
                    nowFractal.getInitialRange(rectangle);  // Обновляем область фрактала
                    drawFractal();  // Рисуем фрактал
                }
                case ("Save") -> { // 2 значение case (для кнопки сохранения)
                    /* контейнер, в нём несколько компонентов, которые управляют выбором файлов*/
                    JFileChooser jFileChooser = new JFileChooser();
                    /*Фильтр для того чтобы изображение могло быть сохранено только в формате png*/
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", ".png");
                    jFileChooser.setFileFilter(filter); // Устанавливаем фильтр - только на png
                    jFileChooser.setAcceptAllFileFilterUsed(false);  // Запрещаем выбирать другой тип файла
                    int result = jFileChooser.showSaveDialog(display); // Возвращает значение окна сохранения
                    if (result == JFileChooser.APPROVE_OPTION) {  // Если окно не закрыто
                        File file = new File((jFileChooser.getSelectedFile()).toString() + ".png");  // Создаем файл
                        try {
                            ImageIO.write(display.imj, "png", file);  // Записываем его
                        } catch (Exception e) {  // Обрабатываем возможное исключение
                            JOptionPane.showMessageDialog(display, e.getMessage(), "Нельзя сохранить",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        }
    }

    // Выбор фрактала для отображения
    private class ListenerBox implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            switch ((String) Objects.requireNonNull(jComboBox.getSelectedItem())) {
                case ("Tricorn") -> nowFractal = new Tricorn();
                case ("Mandelbrot") -> nowFractal = new Mandelbrot();
                case ("BurningShip") -> nowFractal = new BurningShip();
            }
            nowFractal.getInitialRange(rectangle);
            drawFractal();
        }
    }


    // Подключение мыши. При нажатии- происходит увелечение
    private class ListenerMouse extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {
            double x = FractalGenerator.getCoord(rectangle.x, rectangle.x + rectangle.width, size, event.getX());
            double y = FractalGenerator.getCoord(rectangle.y, rectangle.y + rectangle.height, size, event.getY());
            nowFractal.recenterAndZoomRange(rectangle, x, y, 0.5);
            drawFractal();
        }
    }

    private int size;  // Размер окна
    private JImageDisplay display;  // Переменная для отображения изображения
    private FractalGenerator nowFractal;  // Отображаемый фрактал
    private Rectangle2D.Double rectangle; // Область, в которой будет рисоваться фрактал
    private JComboBox<String> jComboBox;  // Меню (блок) выбора фрактала

    public FractalExplorer(int size) {
        this.size = size;
        display = new JImageDisplay(size, size);  // Создаем новый объект отображения
        rectangle = new Rectangle2D.Double();  // Создаёт прямоугольник по заданным размерам-начальную область фрактала
        nowFractal = new Mandelbrot();  // Начальный фрактал, по умолчанию,-Мандельброт
        nowFractal.getInitialRange(rectangle);  // Задаем область фрактала
        /* Выпадающий список,можно выбрать значение из выпадающего списка*/
        jComboBox = new JComboBox<>();
    }

    // Создает и выводит графический интерфейс
    public void createAndShowGUI() {
        JFrame frame = new JFrame();  // Графический интерфейс

        /*Три пункта выподающего меню для выбора*/
        jComboBox.addItem(new Mandelbrot().toString());
        jComboBox.addItem(new Tricorn().toString());
        jComboBox.addItem(new BurningShip().toString());

        /* Создаём слушатель и связываем его с выбором фрактала. Листнер в вечном ожидании
         * он ждёт когда появится событие в источнике, к которому он прикреплён, когда такое появляется,
         * листнер получает управление*/
        ListenerBox listenerBox = new ListenerBox(); //Листнер выподающего списка
        jComboBox.addActionListener(listenerBox);  // Вешаем слушатель на блок выбора фракатал

        JButton buttonReset = new JButton("Сброс");  // Создаём кнопку сброса
        JButton buttonSave = new JButton("Сохранить");  // создаём кнопку сохранения
        buttonReset.setActionCommand("Reset");  // Устанавливаем команды, для различия для каждой кнопки
        buttonSave.setActionCommand("Save");
        ListenerButton listenerButton = new ListenerButton();  // Слушатель кнопок
        buttonReset.addActionListener(listenerButton); // Добавляем слушатели для кнопок
        buttonSave.addActionListener(listenerButton);

        ListenerMouse listenerMouse = new ListenerMouse();
        frame.addMouseListener(listenerMouse);  // Добавляем слушатель для нажатия мыши

        JLabel jLabel = new JLabel("Фракталы"); // Текст на экране
        JPanel jPanelNorth = new JPanel();  // Верхняя панель с элементами
        JPanel jPanelSouth = new JPanel();  // Нижняя панель с элементами

        jPanelNorth.add(jLabel);  // Добавляем текст
        jPanelNorth.add(jComboBox);  // Добавляем блок выбора фрактала
        jPanelSouth.add(buttonReset);  // Добавляем кнопку сброса
        jPanelSouth.add(buttonSave);  // Добавляем кнопку сохранения
        frame.add(jPanelNorth, BorderLayout.NORTH);  // Добавляем панели на экран
        frame.add(jPanelSouth, BorderLayout.SOUTH);
        frame.add(display, BorderLayout.CENTER);  // Добавляем к графическому интерфейсу изображение по центру

        // Стандартные операции
        frame.pack(); //Создаёт минимал. размер окна- чтобы программа отображалась нормально
        frame.setVisible(true); //Отображает окно программы
        frame.setResizable(false); //Запрещает менять размер окна программы
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // При закрытии окна останавливаем программу
        frame.setTitle("Java"); //Устанавливает название окна
    }

    /* Рисует фрактал. Перебираем все пиксели изображения, переводим каждый в комплексную форму,
     * подаем в фрактал. Если точка не выходит за границы фрактала - красим пиксель в черный,
     * если выходит - красим по формуле*/
    private void drawFractal() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                //Превращает коррдинаты в координаты в пространстве фрактала
                double xCoord = FractalGenerator.getCoord(rectangle.x, rectangle.x + rectangle.width, size, x);
                double yCoord = FractalGenerator.getCoord(rectangle.y, rectangle.y + rectangle.height, size, y);

                //Если чис. итер. -1, красим в чёрный, или же подбираем нужный цвет
                if (nowFractal.numIterations(xCoord, yCoord) == -1) {
                    display.drawPixel(x, y, 0);
                }
                //Красит в указанный цвет
                else {
                    float hue = 0.7f + (float) nowFractal.numIterations(xCoord, yCoord) / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    display.drawPixel(x, y, rgbColor);
                }
            }
        }
        display.repaint();  // Обновляем изображение
    }
}


