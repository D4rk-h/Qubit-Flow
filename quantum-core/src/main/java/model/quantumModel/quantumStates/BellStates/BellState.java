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

package model.quantumModel.quantumStates.BellStates;

import model.quantumModel.QuantumState;
import model.mathModel.Complex;

public class BellState extends QuantumState {
    public BellState(Complex[] amplitudes) {
        super(amplitudes, 2);
    }

    public Complex[] createAmplitude(boolean beta00, boolean beta01, boolean beta10, boolean beta11){
        Complex[] amplitudes = new Complex[4];
        double value = 1/Math.sqrt(2);
        amplitudes[0] = beta00 ? new Complex(value, 0): new Complex(0,0);
        amplitudes[1] = beta01 ? new Complex(value, 0): new Complex(0,0);
        amplitudes[2] = beta10 ? new Complex(value, 0): new Complex(0,0);
        amplitudes[3] = beta11 ? new Complex(value, 0): new Complex(0,0);
        return amplitudes;
    }
}
