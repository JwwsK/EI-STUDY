import java.util.Scanner;

// State Interface for the State Pattern
interface LaunchState {
    void startChecks(Rocket rocket);
    void launch(Rocket rocket);
    void fastForward(Rocket rocket, int seconds);
}

// Concrete State: PreLaunchState
class PreLaunchState implements LaunchState {
    @Override
    public void startChecks(Rocket rocket) {
        System.out.println("All systems are 'Go' for launch.");
        rocket.setState(new LaunchingState());
    }

    @Override
    public void launch(Rocket rocket) {
        System.out.println("You must start the pre-launch checks first.");
    }

    @Override
    public void fastForward(Rocket rocket, int seconds) {
        System.out.println("Cannot fast forward during pre-launch checks.");
    }
}

// Concrete State: LaunchingState
class LaunchingState implements LaunchState {
    private int stage = 1;

    @Override
    public void startChecks(Rocket rocket) {
        System.out.println("Pre-launch checks already completed.");
    }

    @Override
    public void launch(Rocket rocket) {
        System.out.println("Launching...");
        rocket.launch();
    }

    @Override
    public void fastForward(Rocket rocket, int seconds) {
        for (int i = 0; i < seconds; i++) {
            if (!rocket.update()) {
                System.out.println("Mission Failed due to insufficient fuel.");
                return;
            }
            System.out.println(rocket);
        }
    }
}

// Concrete State: CompletedState
class CompletedState implements LaunchState {
    @Override
    public void startChecks(Rocket rocket) {
        System.out.println("Mission is already completed.");
    }

    @Override
    public void launch(Rocket rocket) {
        System.out.println("Mission already completed.");
    }

    @Override
    public void fastForward(Rocket rocket, int seconds) {
        System.out.println("Mission already completed.");
    }
}

// Rocket Class representing the Rocket's state and behavior
class Rocket {
    private LaunchState state;
    private int fuel = 100; // Fuel percentage
    private int altitude = 0; // Altitude in km
    private int speed = 0; // Speed in km/h
    private int time = 0; // Time in seconds

    public Rocket() {
        this.state = new PreLaunchState();
    }

    public void setState(LaunchState state) {
        this.state = state;
    }

    public void startChecks() {
        state.startChecks(this);
    }

    public void launch() {
        while (fuel > 0) {
            time++;
            fuel -= 10; // Fuel decreases by 10% each second
            altitude += 10; // Altitude increases by 10 km each second
            speed += 1000; // Speed increases by 1000 km/h each second

            System.out.println(this);
            if (fuel <= 0) {
                setState(new CompletedState());
                break;
            } else if (altitude >= 100) { // Assuming 100 km for orbit
                System.out.println("Orbit achieved! Mission Successful.");
                setState(new CompletedState());
                break;
            }
        }
    }

    public boolean update() {
        if (fuel <= 0) {
            return false;
        }
        time++;
        fuel -= 10; // Fuel decreases by 10% each second
        altitude += 10; // Altitude increases by 10 km each second
        speed += 1000; // Speed increases by 1000 km/h each second

        if (fuel <= 0) {
            setState(new CompletedState());
            return false;
        } else if (altitude >= 100) { // Assuming 100 km for orbit
            System.out.println("Orbit achieved! Mission Successful.");
            setState(new CompletedState());
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("Stage: %d, Fuel: %d%%, Altitude: %d km, Speed: %d km/h", time, fuel, altitude, speed);
    }
}

// Main class to run the simulator
public class RocketLaunchSimulator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Rocket rocket = new Rocket();
        String command;

        System.out.println("Welcome to the Rocket Launch Simulator!");

        while (true) {
            System.out.print("Enter command (start_checks, launch, fast_forward X, exit): ");
            command = scanner.nextLine();

            if (command.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the simulator.");
                break;
            } else if (command.equalsIgnoreCase("start_checks")) {
                rocket.startChecks();
            } else if (command.equalsIgnoreCase("launch")) {
                rocket.launch();
            } else if (command.startsWith("fast_forward")) {
                String[] parts = command.split(" ");
                if (parts.length == 2) {
                    try {
                        int seconds = Integer.parseInt(parts[1]);
                        rocket.launch(); // Start the launch process
                        rocket.fastForward(seconds);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number of seconds.");
                    }
                } else {
                    System.out.println("Usage: fast_forward X");
                }
            } else {
                System.out.println("Unknown command. Please try again.");
            }
        }

        scanner.close();
    }
}
