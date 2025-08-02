// Copyright 2025 D4rk-h
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package model.mathModel;

public class Complex {
    public static final Complex ZERO = new Complex(0.0, 0.0);
    public static final Complex ONE = new Complex(1.0, 0.0);
    public static final Complex I = new Complex(0.0, 1.0);
    public static final Complex MINUS_I = new Complex(0.0, -1.0);

    private double real;
    private double imaginary;
    public static final double EPSILON = 1e-10;
    private transient Double cachedMagnitude;
    private transient Double cachedPhase;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public Complex real(double real) {return new Complex(real, 0.0);}

    public Complex imaginary(double imaginary) {return new Complex(0.0, imaginary);}

    public Complex polar(double magnitude, double phase) {return new Complex(magnitude * Math.cos(phase), magnitude * Math.sin(phase));}

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

    public double magnitudeSquared() {return real * real + imaginary * imaginary;}

    public Complex conjugate() {
        return new Complex(real, -imaginary);
    }

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

    public Complex divide(Complex number) {
        double denominator = number.real * number.real + number.imaginary * number.imaginary;
        double real = (this.real * number.real + this.imaginary * number.imaginary) / denominator;
        double imag = (this.imaginary * number.real - this.real * number.imaginary) / denominator;
        return new Complex(real, imag);
    }

    public Complex scale(double lambda) {return new Complex(this.real * lambda, this.imaginary * lambda);}

    public String toString() {if (real == 0) return imaginary + "i"; if (imaginary == 0) return real + ""; if (imaginary <  0) return real + " - " + (-imaginary) + "i";return real + " + " + imaginary + "i";}

    public static Complex exponential(double theta) {return new Complex(Math.cos(theta), Math.sin(theta));}

    public boolean isApproximatelyEqual(Complex other, double tolerance) {
        if (other == null) return false;
        return Math.abs(this.real - other.real) < tolerance &&
                Math.abs(this.imaginary - other.imaginary) < tolerance;
    }

    public boolean isApproximatelyEqual(Complex other) {
        return isApproximatelyEqual(other, EPSILON);
    }
}
