package model.mathModel;

public class EigenDecomposition {
    private Complex[] eigenvalues;
    private Matrix eigenvectors;

    public EigenDecomposition(Complex[] eigenvalues, Matrix eigenvectors) {
        this.eigenvalues = eigenvalues;
        this.eigenvectors = eigenvectors;
    }

    public Complex[] getEigenvalues(){return eigenvalues;}
    public void setEigenvalues(Complex[] eigenvalues) {this.eigenvalues = eigenvalues;}
    public Matrix getEigenvectors() {return eigenvectors;}
    public void setEigenvectors(Matrix eigenvectors) {this.eigenvectors = eigenvectors;}
}
