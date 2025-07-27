package control.command;

public interface SimulationCommand extends CommandPort {
    @Override
    void execute();
    void pause();
    void resume();
    void abort();
}
