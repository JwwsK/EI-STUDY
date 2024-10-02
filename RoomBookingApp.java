import java.util.*;

// Direction Enum for Rover's Facing Direction
enum Direction {
    NORTH, EAST, SOUTH, WEST;
    
    public Direction left() {
        return values()[(this.ordinal() + 3) % 4]; // Rotate counter-clockwise
    }
    
    public Direction right() {
        return values()[(this.ordinal() + 1) % 4]; // Rotate clockwise
    }
}

// Position Class to track Rover's Coordinates
class Position {
    private int x;
    private int y;
    
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void moveForward(Direction direction) {
        switch (direction) {
            case NORTH -> y++;
            case SOUTH -> y--;
            case EAST -> x++;
            case WEST -> x--;
        }
    }
    
    public boolean isWithinGrid(int gridWidth, int gridHeight) {
        return x >= 0 && x < gridWidth && y >= 0 && y < gridHeight;
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

// Grid Class to represent terrain and obstacles
class Grid {
    private int width;
    private int height;
    private Set<Position> obstacles;

    public Grid(int width, int height, List<Position> obstacles) {
        this.width = width;
        this.height = height;
        this.obstacles = new HashSet<>(obstacles);
    }

    public boolean isObstacle(Position position) {
        return obstacles.contains(position);
    }

    public boolean isWithinBounds(Position position) {
        return position.isWithinGrid(width, height);
    }
}

// Rover Class to manage rover's state and movements
class Rover {
    private Position position;
    private Direction direction;
    private Grid grid;

    public Rover(Position startPosition, Direction startDirection, Grid grid) {
        this.position = startPosition;
        this.direction = startDirection;
        this.grid = grid;
    }

    public void turnLeft() {
        this.direction = direction.left();
    }

    public void turnRight() {
        this.direction = direction.right();
    }

    public void moveForward() {
        Position newPosition = new Position(position.getX(), position.getY());
        newPosition.moveForward(direction);

        if (grid.isWithinBounds(newPosition) && !grid.isObstacle(newPosition)) {
            position = newPosition;
        } else {
            System.out.println("Obstacle detected or out of bounds. Can't move.");
        }
    }

    public void reportStatus() {
        System.out.println("Rover is at " + position + " facing " + direction + ".");
    }

    public Position getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }
}

// Command interface for encapsulating commands
interface Command {
    void execute();
}

// Move Command
class MoveCommand implements Command {
    private Rover rover;

    public MoveCommand(Rover rover) {
        this.rover = rover;
    }

    @Override
    public void execute() {
        rover.moveForward();
    }
}

// Turn Left Command
class TurnLeftCommand implements Command {
    private Rover rover;

    public TurnLeftCommand(Rover rover) {
        this.rover = rover;
    }

    @Override
    public void execute() {
        rover.turnLeft();
    }
}

// Turn Right Command
class TurnRightCommand implements Command {
    private Rover rover;

    public TurnRightCommand(Rover rover) {
        this.rover = rover;
    }

    @Override
    public void execute() {
        rover.turnRight();
    }
}

// MarsRoverController to process commands
class MarsRoverController {
    private Map<Character, Command> commandMap;

    public MarsRoverController(Rover rover) {
        commandMap = new HashMap<>();
        commandMap.put('M', new MoveCommand(rover));
        commandMap.put('L', new TurnLeftCommand(rover));
        commandMap.put('R', new TurnRightCommand(rover));
    }

    public void processCommands(String commands) {
        for (char command : commands.toCharArray()) {
            Command cmd = commandMap.get(command);
            if (cmd != null) {
                cmd.execute();
            }
        }
    }
}

// Main class for simulation
public class MarsRoverSimulation {

    public static void main(String[] args) {
        // Define obstacles
        List<Position> obstacles = List.of(new Position(2, 2), new Position(3, 5));

        // Create a 10x10 grid
        Grid grid = new Grid(10, 10, obstacles);

        // Initialize Rover at position (0, 0) facing North
        Rover rover = new Rover(new Position(0, 0), Direction.NORTH, grid);

        // Create Mars Rover Controller with commands
        MarsRoverController controller = new MarsRoverController(rover);

        // Simulate commands
        String commands = "MMRMMLM";
        controller.processCommands(commands);

        // Report final status
        rover.reportStatus();
    }
}
