package Lab_4;

public class Complex {
    private double real;
    private double im;

    public Complex (double real, double im){
        this.real = real;
        this.im = im;
    }

    public double moduleSqr (){
        return (real*real+im*im);
    }

    public Complex sqr (){
        return new Complex(real*real-im*im, 2*real*im);
    }

    public Complex add (Complex z){
        return new Complex(this.real + z.real, this.im + z.im);
    }

}
