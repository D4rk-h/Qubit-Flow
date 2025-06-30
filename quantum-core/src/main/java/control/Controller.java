package control;

import model.quantumModel.BlochSphere.BlochSphere;
import model.quantumModel.QuantumCircuit.QuantumCircuit;
import model.quantumModel.Qubit;

import java.security.AlgorithmParameters;
import java.util.List;

public class Controller {
    private QuantumCircuit currentCircuit;
    private List<Qubit> qubits;
    private SimulationState simulationState;

    public SimulationResult executeSimulation(SimulationRequest request){return null;}
    public void pauseSimulation(){}
    public void resumeSimulation(){}
    public void resetSimulation(){}

    public void buildCircuit(CircuitConfiguration config){}
    public void addGateToCircuit(String gateType, int qubit, int position){}
    public void addControlledGate(String gateType, int controlQubit, int targetQubit, int position){}

    public AlgorithmResult runAlgorithm(String algorithmName, AlgorithmParameters params){return null;}

    public StateSnapshot getCurrentState(){return null;}
    public MeasurementResult measureQubit(int qubitIndex){return null;}
    public MeasurementResult measureAllQubits(){return null;}

    public BlochSphere getBlochRepresentation(int qubitIndex){return null;}
    public CircuitVisualization getCircuitVisualization(){return null;}
}
