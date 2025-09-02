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

package model.mathModel.qpca;

import model.mathModel.Complex;
import model.mathModel.Matrix;

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
