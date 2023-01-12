package Lab_2;

// Класс точки в трехмерном пространстве
public class Point3d extends Point2d{
    private double zCoord;

    // Конструктор класса
    public Point3d(double x, double y, double z){
        super(x,y);
        zCoord = z;
    }

    // Конструктор по умолчанию
    public Point3d(){
        this(0,0,0);
    }

    // Получение координаты Z
    public double getZ (){
        return zCoord;
    }

    // Замена значения координаты Z
    public void setZ(double val){
        zCoord = val;
    }

    // Сравнение двух объектов Point3d
    public boolean equal(Point3d point1){
        if (this.getX() == point1.getX()){
            if (this.getY() == point1.getY()){
                return this.getZ() == point1.getZ();
            }
        }
        return false;
    }

    // Расстояние между двумя точками
    public double distanceTo(Point3d point){
        double p1 = Math.pow((this.getX()-point.getX()),2);
        double p2 = Math.pow((this.getY()-point.getY()),2);
        double p3 = Math.pow((this.getZ()-point.getZ()),2);
        double distance = Math.sqrt(p1 + p2 + p3);
        double scale = Math.pow(10, 2);

        // Умножаем на 10 в степени необходимой точности, округляем до целого и затем делим на 10 в той же степени
        return Math.rint(distance*scale)/scale;
    }


}
