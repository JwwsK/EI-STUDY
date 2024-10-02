import java.util.*;

// Device interface for common behavior
interface SmartDevice {
    void turnOn();
    void turnOff();
    String getStatus();
    int getId();
}

// Concrete Light class
class Light implements SmartDevice {
    private int id;
    private boolean isOn;

    public Light(int id) {
        this.id = id;
        this.isOn = false;
    }

    @Override
    public void turnOn() {
        isOn = true;
    }

    @Override
    public void turnOff() {
        isOn = false;
    }

    @Override
    public String getStatus() {
        return "Light " + id + " is " + (isOn ? "On" : "Off");
    }

    @Override
    public int getId() {
        return id;
    }
}

// Concrete Thermostat class
class Thermostat implements SmartDevice {
    private int id;
    private int temperature;

    public Thermostat(int id, int initialTemperature) {
        this.id = id;
        this.temperature = initialTemperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    @Override
    public void turnOn() {
        // Simulate thermostat behavior
    }

    @Override
    public void turnOff() {
        // Simulate thermostat behavior
    }

    @Override
    public String getStatus() {
        return "Thermostat " + id + " is set to " + temperature + " degrees";
    }

    @Override
    public int getId() {
        return id;
    }

    public int getTemperature() {
        return temperature;
    }
}

// Concrete DoorLock class
class DoorLock implements SmartDevice {
    private int id;
    private boolean isLocked;

    public DoorLock(int id) {
        this.id = id;
        this.isLocked = true;
    }

    @Override
    public void turnOn() {
        isLocked = false;
    }

    @Override
    public void turnOff() {
        isLocked = true;
    }

    @Override
    public String getStatus() {
        return "Door " + id + " is " + (isLocked ? "Locked" : "Unlocked");
    }

    @Override
    public int getId() {
        return id;
    }
}

// Factory for creating smart devices
class SmartDeviceFactory {
    public static SmartDevice createDevice(String type, int id) {
        return switch (type.toLowerCase()) {
            case "light" -> new Light(id);
            case "thermostat" -> new Thermostat(id, 70);
            case "door" -> new DoorLock(id);
            default -> throw new IllegalArgumentException("Unknown device type");
        };
    }
}

// SmartHomeHub acts as the observer subject and proxy controller
class SmartHomeHub {
    private Map<Integer, SmartDevice> devices = new HashMap<>();
    private List<ScheduledTask> scheduledTasks = new ArrayList<>();
    private List<Trigger> triggers = new ArrayList<>();

    public void addDevice(SmartDevice device) {
        devices.put(device.getId(), device);
        System.out.println(device.getClass().getSimpleName() + " " + device.getId() + " added to the system.");
    }

    public void removeDevice(int id) {
        devices.remove(id);
        System.out.println("Device " + id + " removed from the system.");
    }

    public void turnOn(int id) {
        SmartDevice device = devices.get(id);
        if (device != null) {
            device.turnOn();
            System.out.println(device.getClass().getSimpleName() + " " + id + " turned On.");
        }
    }

    public void turnOff(int id) {
        SmartDevice device = devices.get(id);
        if (device != null) {
            device.turnOff();
            System.out.println(device.getClass().getSimpleName() + " " + id + " turned Off.");
        }
    }

    public void scheduleTask(int deviceId, String time, String command) {
        scheduledTasks.add(new ScheduledTask(deviceId, time, command));
        System.out.println("Scheduled task added for Device " + deviceId + " at " + time + ": " + command);
    }

    public void addTrigger(String condition, String action) {
        triggers.add(new Trigger(condition, action));
        System.out.println("Added trigger: When " + condition + " then " + action);
    }

    public void checkTriggers() {
        for (Trigger trigger : triggers) {
            if (trigger.evaluate(devices)) {
                executeAction(trigger.getAction());
            }
        }
    }

    private void executeAction(String action) {
        // Simple action parser
        if (action.startsWith("turnOn")) {
            int id = Integer.parseInt(action.replaceAll("[^0-9]", ""));
            turnOn(id);
        } else if (action.startsWith("turnOff")) {
            int id = Integer.parseInt(action.replaceAll("[^0-9]", ""));
            turnOff(id);
        }
    }

    public void statusReport() {
        for (SmartDevice device : devices.values()) {
            System.out.println(device.getStatus());
        }
    }
}

// ScheduledTask to store scheduling commands
class ScheduledTask {
    private int deviceId;
    private String time;
    private String command;

    public ScheduledTask(int deviceId, String time, String command) {
        this.deviceId = deviceId;
        this.time = time;
        this.command = command;
    }

    @Override
    public String toString() {
        return "Device " + deviceId + " at " + time + ": " + command;
    }
}

// Trigger class to manage automation
class Trigger {
    private String condition;
    private String action;

    public Trigger(String condition, String action) {
        this.condition = condition;
        this.action = action;
    }

    public boolean evaluate(Map<Integer, SmartDevice> devices) {
        // Example: condition like "temperature > 75"
        if (condition.contains("temperature")) {
            int id = 2; // Assuming thermostat has id 2
            Thermostat thermostat = (Thermostat) devices.get(id);
            if (thermostat != null) {
                String[] parts = condition.split(" ");
                int temp = Integer.parseInt(parts[2]);
                return thermostat.getTemperature() > temp;
            }
        }
        return false;
    }

    public String getAction() {
        return action;
    }
}

// Main class for Smart Home Simulation
public class SmartHomeSimulation {
    public static void main(String[] args) {
        // Create the Smart Home Hub
        SmartHomeHub hub = new SmartHomeHub();

        // Add devices using the factory
        SmartDevice light1 = SmartDeviceFactory.createDevice("light", 1);
        SmartDevice thermostat = SmartDeviceFactory.createDevice("thermostat", 2);
        SmartDevice door = SmartDeviceFactory.createDevice("door", 3);

        // Add devices to the hub
        hub.addDevice(light1);
        hub.addDevice(thermostat);
        hub.addDevice(door);

        // Turn on a device
        hub.turnOn(1);

        // Schedule a task
        hub.scheduleTask(2, "06:00", "Turn On");

        // Add automation trigger
        hub.addTrigger("temperature > 75", "turnOff(1)");

        // Simulate trigger check
        hub.checkTriggers();

        // Display status report
        hub.statusReport();
    }
}
