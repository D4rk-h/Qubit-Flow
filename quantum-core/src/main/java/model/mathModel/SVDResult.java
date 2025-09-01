package model.mathModel;

public record SVDResult(
        Matrix U,
        Complex[] singularValues,
        Matrix V
)
{}
