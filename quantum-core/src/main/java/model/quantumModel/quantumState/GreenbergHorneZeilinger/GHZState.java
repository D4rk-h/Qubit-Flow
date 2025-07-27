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

package model.quantumModel.quantumState.GreenbergHorneZeilinger;

import model.mathModel.Complex;
import model.quantumModel.QuantumState;

public class GHZState extends QuantumState {

    public GHZState(int nQubits) {
        super(createGHZAmplitudes(nQubits), nQubits);
        if (nQubits < 2) throw new IllegalArgumentException("GHZ State requires at least 2 qubits");
    }

    public static Complex[] createGHZAmplitudes(int nQubits) {
        int n = (int) Math.pow(2, nQubits);
        Complex[] amplitudes = new Complex[n];
        double value = 1.0/Math.sqrt(2);
        for (int i=0;i<n;i++) {
            amplitudes[i] = new Complex(0, 0);
        }
        amplitudes[0] = new Complex(value, 0);
        amplitudes[n - 1] = new Complex(value, 0);
        return amplitudes;
    }
}
