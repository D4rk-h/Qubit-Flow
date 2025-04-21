package MATH;

public class Complex {
    private final double real;
    private final double imaginary;

    public Complex(double real_part, double imaginary_part) {
        real = real_part;
        imaginary = imaginary_part;
    }

    /**
     * Finds the magnitude of a complex number using hypotenuse from Math library
     *
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
    * Sets a double number (element of a matrix) to Complex with im part as 0
    *
    * @return : A new Complex Object with the double number as real part and 0 as imaginary part
    */
    public Complex toComplex(double number) {
        return new Complex(number, 0);
    }
}
