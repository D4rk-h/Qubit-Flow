package api.dto.control;


import control.Controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/quantum/history")
@Tag(name = "Command History", description = "Undo/Redo operations")
@CrossOrigin(origins = "*")
public class HistoryController {

    private final Controller quantumController;

    public HistoryController() {
        this.quantumController = new Controller();
    }

    @PostMapping("/undo")
    @Operation(summary = "Undo last operation")
    public ResponseEntity<Map<String, Object>> undo() {
        boolean success = quantumController.undo();
        return ResponseEntity.ok(Map.of(
                "success", success,
                "canUndo", quantumController.canUndo(),
                "canRedo", quantumController.canRedo(),
                "historySize", quantumController.getHistorySize()
        ));
    }

    @PostMapping("/redo")
    @Operation(summary = "Redo last undone operation")
    public ResponseEntity<Map<String, Object>> redo() {
        boolean success = quantumController.redo();
        return ResponseEntity.ok(Map.of(
                "success", success,
                "canUndo", quantumController.canUndo(),
                "canRedo", quantumController.canRedo(),
                "historySize", quantumController.getHistorySize()
        ));
    }

    @GetMapping("/status")
    @Operation(summary = "Get history status")
    public ResponseEntity<Map<String, Object>> getHistoryStatus() {
        return ResponseEntity.ok(Map.of(
                "canUndo", quantumController.canUndo(),
                "canRedo", quantumController.canRedo(),
                "historySize", quantumController.getHistorySize(),
                "lastCommand", quantumController.getLastCommandType()
        ));
    }
}