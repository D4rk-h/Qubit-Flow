package control.commands;

public interface SimulationCommand extends CommandPort {
    @Override
    void execute();
    void pause();
    void resume();
    void abort();
}
