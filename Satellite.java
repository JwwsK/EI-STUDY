import java.util.*;

// Command Interface for the Command Pattern
interface Command {
    void execute();
}

// Satellite Class which maintains the state of the satellite
class Satellite {
    private String orientation = "North"; // Default orientation
    private boolean solarPanelsActive = false; // Default panel status
    private int dataCollected = 0; // Default data collected

    // Rotate the satellite
    public void rotate(String direction) {
        this.orientation = direction;
        System.out.println("Satellite rotated to: " + orientation);
    }

    // Activate solar panels
    public void activatePanels() {
        this.solarPanelsActive = true;
        System.out.println("Solar panels activated.");
    }

    // Deactivate solar panels
    public void deactivatePanels() {
        this.solarPanelsActive = false;
        System.out.println("Solar panels deactivated.");
    }

    // Collect data if solar panels are active
    public void collectData() {
        if (solarPanelsActive) {
            this.dataCollected += 10;
            System.out.println("Data collected: " + dataCollected + " units.");
        } else {
            System.out.println("Cannot collect data. Solar panels are inactive.");
        }
    }

    // Getters for checking satellite state
    public String getOrientation() {
        return orientation;
    }

    public boolean areSolarPanelsActive() {
        return solarPanelsActive;
    }

    public int getDataCollected() {
        return dataCollected;
    }
}

// Command for rotating the satellite
class RotateCommand implements Command {
    private Satellite satellite;
    private String direction;

    public RotateCommand(Satellite satellite, String direction) {
        this.satellite = satellite;
        this.direction = direction;
    }

    @Override
    public void execute() {
        satellite.rotate(direction);
    }
}

// Command for activating solar panels
class ActivatePanelsCommand implements Command {
    private Satellite satellite;

    public ActivatePanelsCommand(Satellite satellite) {
        this.satellite = satellite;
    }

    @Override
    public void execute() {
        satellite.activatePanels();
    }
}

// Command for deactivating solar panels
class DeactivatePanelsCommand implements Command {
    private Satellite satellite;

    public DeactivatePanelsCommand(Satellite satellite) {
        this.satellite = satellite;
    }

    @Override
    public void execute() {
        satellite.deactivatePanels();
    }
}

// Command for collecting data
class CollectDataCommand implements Command {
    private Satellite satellite;

    public CollectDataCommand(Satellite satellite) {
        this.satellite = satellite;
    }

    @Override
    public void execute() {
        satellite.collectData();
    }
}

// Invoker class to execute the commands sequentially
class CommandInvoker {
    private List<Command> commandQueue = new ArrayList<>();

    // Add a command to the queue
    public void addCommand(Command command) {
        commandQueue.add(command);
    }

    // Execute all the commands in the queue
    public void executeCommands() {
        for (Command command : commandQueue) {
            command.execute();
        }
        commandQueue.clear();
    }
}

// Main class to simulate the Satellite Command System
public class SatelliteCommandSystem {
    public static void main(String[] args) {
        // Initialize the satellite
        Satellite satellite = new Satellite();
        
        // Create the Command Invoker
        CommandInvoker invoker = new CommandInvoker();

        // Add a series of commands to the invoker
        invoker.addCommand(new RotateCommand(satellite, "South"));
        invoker.addCommand(new ActivatePanelsCommand(satellite));
        invoker.addCommand(new CollectDataCommand(satellite));
        invoker.addCommand(new RotateCommand(satellite, "East"));
        invoker.addCommand(new CollectDataCommand(satellite));
        invoker.addCommand(new DeactivatePanelsCommand(satellite));

        // Execute all the commands
        invoker.executeCommands();
    }
}
