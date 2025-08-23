package api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.mathModel.Complex;

import java.io.Serializable;
import java.util.Objects;

public class ComplexDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("real")
    private final double real;
    @JsonProperty("imaginary")
    private final double imaginary;

    public ComplexDto() {
        this.real = 0.0;
        this.imaginary = 0.0;
    }

    @JsonCreator
    public ComplexDto(
            @JsonProperty("real") double real,
            @JsonProperty("imaginary") double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public static ComplexDto from(Complex complex) {
        if (complex == null) return new ComplexDto();
        return new ComplexDto(complex.getRealPart(), complex.getImaginaryPart());
    }

    public Complex toComplex() {return new Complex(real, imaginary);}
    public double getReal() {return real;}
    public double getImaginary() {return imaginary;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexDto that = (ComplexDto) o;
        return Double.compare(that.real, real) == 0 &&
                Double.compare(that.imaginary, imaginary) == 0;
    }

    @Override
    public int hashCode() {return Objects.hash(real, imaginary);}

    @Override
    public String toString() {
        if (imaginary == 0) return String.format("%.4f", real);
        if (real == 0) return String.format("%.4fi", imaginary);
        return String.format("%.4f%+.4fi", real, imaginary);
    }
}