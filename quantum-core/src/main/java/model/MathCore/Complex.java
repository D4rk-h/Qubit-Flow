package model.MathCore;

public class Complex {
    private double real;
    private double imaginary;

    public Complex(double real_part, double imaginary_part) {
        real = real_part;
        imaginary = imaginary_part;
    }

    public double getRealPart(){
        return real;
    }

    public double getImaginaryPart(){
        return imaginary;
    }

    public void setRealPart(double newRealPart){
        real = newRealPart;
    }

    public void setImaginaryPart(double newImaginaryPart){
        imaginary = newImaginaryPart;
    }

    public double magnitude() {return Math.hypot(real, imaginary);}

    public Complex normalize() {return new Complex(this.real/this.magnitude(), this.imaginary/this.magnitude());}

    public double phase() {
        return Math.atan2(imaginary, real);
    }

    public Complex add(Complex number){
        if (number == null) {
            throw new IllegalArgumentException("Number given is null");
        }
        double realAddition = this.getRealPart() + number.getRealPart();
        double imaginaryAddition = this.getImaginaryPart() + number.getImaginaryPart();
        return new Complex(realAddition, imaginaryAddition);
    }

    public Complex subtract(Complex number){
        double realSubtraction = this.getRealPart() - number.getRealPart();
        double imaginarySubtraction = this.getImaginaryPart() - number.getImaginaryPart();
        return new Complex(realSubtraction, imaginarySubtraction);
    }

    public Complex multiply(Complex number){
        double real = this.real * number.real - this.imaginary * number.imaginary;
        double imag = this.real * number.imaginary + this.imaginary * number.real;
        return new Complex(real, imag);
    }

    public Complex scalar(double lambda) {return new Complex(this.real * lambda, this.imaginary * lambda);}

    public String toString() {if (real == 0) return imaginary + "i"; if (imaginary == 0) return real + ""; if (imaginary <  0) return real + " - " + (-imaginary) + "i";return real + " + " + imaginary + "i";
    }

    public static Complex exponential(double theta) {
        return new Complex(Math.cos(theta), Math.sin(theta));
    }
}
