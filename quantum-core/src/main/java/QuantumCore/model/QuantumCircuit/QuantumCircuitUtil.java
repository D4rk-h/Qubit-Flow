package QuantumCore.model.QuantumCircuit;

import java.util.List;

public class QuantumCircuitUtil {
    public String showCircuit(List<List<Object>> circuit){
        System.out.println("[");
        for (int i=0;i<circuit.size();i++){
            System.out.print("[");
            for (int j=0;j<circuit.get(i).size();j++){
                System.out.print(circuit.get(i).get(j) +",");
            }
            System.out.print("]\n");
        }
        System.out.println("]");
        return "";
    }
}
