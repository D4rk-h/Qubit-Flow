# Qubit Flow

## Quantum Circuit Simulator Backend

A high-performance Java-based quantum circuit simulation engine designed to serve as the backend for web-based quantum computing applications. This project provides a comprehensive REST API for building, simulating, and analyzing quantum circuits with support for various quantum gates and algorithms.

## 🚧 Project Status

**This is a partial/initial version** - The core quantum simulation engine is implemented and functional, with REST API integration currently in development. The project serves as the backend foundation for a future web service enabling quantum circuit design and simulation.

## ✨ Features

### Core Quantum Computing Capabilities
- **Multi-qubit quantum state simulation** (1-10 qubits)
- **Comprehensive gate library** including:
  - Single-qubit gates: Hadamard, Pauli-X/Y/Z, T, S, Phase
  - Multi-qubit gates: CNOT, SWAP, Toffoli
  - Controlled gate variants
- **Quantum circuit construction** with automatic layer optimization
- **State vector simulation** with efficient amplitude calculations
- **Measurement operations** with probabilistic outcomes
- **Quantum state analysis** (probabilities, fidelity, entropy)

### Advanced Features
- **Command pattern** with full undo/redo support
- **Circuit export** to multiple formats (JSON, QASM, Qiskit)
- **Multiple display providers** (Amplitude, Probability, Bloch Sphere, Density Matrix)
- **Optimized gate operations** for improved performance
- **Comprehensive state management** with validation and normalization

### Architecture Highlights
- **Clean Architecture** with separation of concerns
- **Command Pattern** for operation management
- **Strategy Pattern** for display measurements
- **Port-Adapter Pattern** for extensibility
- **Immutable quantum states** with efficient cloning

## 🏗️ Architecture Overview

```
quantum-core/
├── src/main/java/
│   ├── control/                          # Application layer
│   │   ├── Controller.java               
│   │   ├── command/                      
│   │   │   ├── history/
│   │   │   ├── ports/
│   │   │   ├── add/
│   │   │   ├── remover/
│   │   │   ├── exporter/
│   │   │   ├── gate/
│   │   │   └── simulate/
│   │   └── ...
│   ├── model/                            # Domain layer
│   │   ├── mathModel/
│   │   │   ├── Complex.java
│   │   │   └── Matrix.java
│   │   ├── quantumModel/                 # Quantum computing core
│   │   │   ├── quantumCircuit/
│   │   │   ├── quantumGate/
│   │   │   ├── quantumState/
│   │   │   ├── quantumAlgorithm/
│   │   │   ├── displayMeasurementProvider/
│   │   │   └── quantumPort/
│   │   └── commandModel/
│   └── ...
```

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.8+

### Basic Usage (examples coming soon)

## 🧮 Mathematical Foundation

### Complex Numbers
The `Complex` class provides comprehensive complex number operations:
- Basic arithmetic (add, subtract, multiply, divide)
- Polar coordinate conversion
- Magnitude and phase calculations
- Normalization and conjugation

### Matrix Operations
The `Matrix` class supports:
- Matrix multiplication and addition
- Tensor products for multi-qubit operations
- Matrix inversion and determinant calculation
- Gate extension to multi-qubit systems

### Quantum State Representation
Quantum states are represented as normalized complex amplitude vectors:
- Automatic normalization validation
- Efficient gate application
- Optimized measurement operations
- State fidelity and entropy calculations

## 🎯 Planned Algorithms

The framework is prepared for implementing major quantum algorithms:

- **Deutsch Algorithm** - Determine if a function is constant or balanced
- **Deutsch-Jozsa Algorithm** - Generalized version of Deutsch algorithm  
- **Grover's Algorithm** - Quantum search algorithm
- **Shor's Algorithm** - Integer factorization
- **Quantum Fourier Transform** - Foundation for many quantum algorithms
- **QAOA** - Quantum Approximate Optimization Algorithm
- **VQE** - Variational Quantum Eigensolver

## 🔧 Configuration

### Circuit Constraints
- **Minimum qubits**: 1
- **Maximum qubits**: 10 (configurable)
- **Automatic layer optimization** for parallel gate execution
- **State validation** with configurable tolerance (ε = 1e-10)

## 🚀 REST API (In Development)

## 📦 Dependencies

- **Java 21** - Core runtime
- **Maven 3.8+** - Build system
- **JUnit 5** - Testing framework
- **Jackson** - JSON processing (for export features)

## 🤝 Contributing

We welcome contributions! Please see our contributing guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/quantum-algorithm`)
3. Commit your changes (`git commit -am 'Add new quantum algorithm'`)
4. Push to the branch (`git push origin feature/quantum-algorithm`)
5. Create a Pull Request

### Areas for Contribution
- **Algorithm implementations** (Grover, Shor, etc.)
- **Performance optimizations**
- **Additional export formats**
- **Enhanced measurement providers**
- **REST API endpoints**
- **Documentation and examples**

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

*This project is actively developed and contributions are welcome! Join us in building the future of quantum computing education and simulation.*
