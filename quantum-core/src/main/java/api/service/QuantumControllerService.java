package api.service;

import control.Controller;
import org.springframework.stereotype.Service;

@Service
public class QuantumControllerService {
    private final Controller quantumController;

    public QuantumControllerService() {
        this.quantumController = new Controller(2);
    }

    public Controller getController() {
        return quantumController;
    }
}