package Lab_2;

import java.util.Scanner;

public class Lab2 {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);  // Класс для ввода данных
        System.out.println("Введите координаты первой точки: ");
        double x1 = scanner.nextDouble();
        double y1 = scanner.nextDouble();
        double z1 = scanner.nextDouble();

        System.out.println("Введите координаты второй точки: ");
        double x2 = scanner.nextDouble();
        double y2 = scanner.nextDouble();
        double z2 = scanner.nextDouble();

        System.out.println("Введите координаты третьей точки: ");
        double x3 = scanner.nextDouble();
        double y3 = scanner.nextDouble();
        double z3 = scanner.nextDouble();

        // Создаем объекты по заданным точкам
        Point3d point1 = new Point3d(x1, y1, z1);
        Point3d point2 = new Point3d(x2, y2, z2);
        Point3d point3 = new Point3d(x3, y3, z3);

        // Проверка на равенство координат
        if (point1.equal(point2) || point2.equal(point3) || point3.equal(point1)){
            System.out.println("Введены одинаковые точки!");
        }
        else{
            System.out.println(computeArea(point1, point2, point3));
        }
    }

    // Рассчет периметра треугольника по трем точкам с помощью формулы Герона
    public static double computeArea(Point3d point1, Point3d point2, Point3d point3){
        double a = point1.distanceTo(point2);
        double b = point2.distanceTo(point3);
        double c = point3.distanceTo(point1);
        double p = (a + b + c)/2;
        return Math.sqrt(p*(p-a)*(p-b)*(p-c));
    }
}
