package MATH;

public class Complex {
    private double real;
    private double imaginary;
    private String istringpart;

    public Complex(double real_part, double imaginary_part, String i_string_part) {
        real = real_part;
        imaginary = imaginary_part;
        istringpart = i_string_part;
    }

    public double getRealPart(){
        return real;
    }

    public double getImaginaryPart(){
        return imaginary;
    }

    public String getFullImaginaryPart() {
        return getImaginaryPart() + getIStringPart();
    }

    public String getIStringPart(){
        return istringpart;
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
     * Convert a double number to a Complex with imaginary part = 0
     * @param number a certain double number.
     * @return a new Complex object with real part = number
     * and imaginary part = 0
     */
    public Complex toComplex(double number) {
        return new Complex(number, 0, "");
    }


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
        if (imaginaryAddition == 0) return new Complex(realAddition, imaginaryAddition, "");
        if (number.getIStringPart().isEmpty()) return new Complex(realAddition, imaginaryAddition, this.getIStringPart());
        return new Complex(realAddition, imaginaryAddition, number.getIStringPart());
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
        if (imaginarySubtraction == 0) return new Complex(realSubtraction, imaginarySubtraction, "");
        if (number.getIStringPart().isEmpty()) return new Complex(realSubtraction, imaginarySubtraction, this.getIStringPart());
        return new Complex(realSubtraction, imaginarySubtraction, number.getIStringPart());
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
        double a = number.getRealPart();
        double b = number.getImaginaryPart();
        double d = this.getImaginaryPart();
        double c = this.getRealPart();
        if (this.getFullImaginaryPart().contains("i") && number.getFullImaginaryPart().contains("i")) {
            double newRealPart = (a * c - b * d);
            double newImaginaryPart = (a * d + b * c);
            return new Complex(newRealPart, newImaginaryPart, "i");
        } else if (!number.getFullImaginaryPart().contains("i")) {
            double newRealPart = a * c;
            double newImaginaryPart = b * c;
            return new Complex(newRealPart, newImaginaryPart, "i");
        } else {
            double newRealPart = a * c;
            double newImaginaryPart = a * d;
            return new Complex(newRealPart, newImaginaryPart, "i");
        }
    }


    /**
     * Formatting Complex parts to look how a real Complex numbers
     *
     * @return formatted numbers given certain conditions.
     */
    public String toString() {
        if (real == 0) return imaginary + "i";
        if (imaginary == 0) return real + "";
        if (imaginary <  0) return real + " - " + (-imaginary) + "i";
        return real + " + " + imaginary + "i";
    }
}
