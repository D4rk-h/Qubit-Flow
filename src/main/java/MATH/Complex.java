package MATH;

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

    /**
     * Finds the magnitude of a complex number using hypotenuse from Math library
     * The magnitude can be calculated by:
     *              |z| = x^2 + y^2, where:
     * z = x + iy is a complex number, and x and y are real numbers
     * representing the real and imaginary parts respectively. Which
     * essentially is the same as hypotenuse function from Math lib.
     *
     * @return a double number that corresponds to the magnitude of that complex number.
     */
    public double magnitude() {return Math.hypot(real, imaginary);}

    /**
     * Addition of Complex Numbers:
     * Given: zj = a + bi and zjj = c + di then
     * zj + zjj = (a + c) + (b + d)i
     * @param number: Complex number we are adding to another Complex number
     * @return A new Complex number fruit of the addition.
     * @throws IllegalArgumentException when number is null
     */
    public Complex add(Complex number){
        if (number == null) {
            throw new IllegalArgumentException("Number given is null");
        }
        double realAddition = this.getRealPart() + number.getRealPart();
        double imaginaryAddition = this.getImaginaryPart() + number.getImaginaryPart();
        return new Complex(realAddition, imaginaryAddition);
    }


    /**
     * Addition of Complex Numbers:
     * Given: zj = a - bi and zjj = c - di then
     * zj - zjj = (a - c) + (b - d)i
     * @param number: Complex number we are adding to another Complex number
     * @return A new Complex number fruit of the addition.
     * @throws IllegalArgumentException when number is null
     */
    public Complex subtract(Complex number){
        double realSubtraction = this.getRealPart() - number.getRealPart();
        double imaginarySubtraction = this.getImaginaryPart() - number.getImaginaryPart();
        return new Complex(realSubtraction, imaginarySubtraction);
    }


    /**
     * Addition of Complex Numbers:
     * Given: zj = a + bi and zjj = c + di then
     * zj * zjj = (ac-bd)+i(ad+bc)
     * @param number: Complex number we are multiplying to another Complex number
     * @return A new Complex number fruit of the addition.
     * @throws IllegalArgumentException when number is null
     */
    public Complex multiply(Complex number){
        return new Complex(this.real * number.real, this.imaginary * number.imaginary);
    }

    /**
     * This function scales a Complex number with a double
     *
     * @param lambda A double number that scales a Complex numbers
     * @return a new scaled Complex number.
     */
    public Complex scalar(double lambda) {return new Complex(this.real * lambda, this.imaginary * lambda);}

    /**
     * Formatting Complex parts to look how a real Complex numbers
     *
     * @return formatted numbers given certain conditions.
     */
    public String toString() {if (real == 0) return imaginary + "i"; if (imaginary == 0) return real + ""; if (imaginary <  0) return real + " - " + (-imaginary) + "i";return real + " + " + imaginary + "i";
    }
}
