import java.time.LocalTime;
import java.util.*;

// Command interface for different operations
interface Command {
    void execute();
}

// Room class to manage the state of each room
class Room {
    private int roomNumber;
    private int maxCapacity;
    private int currentOccupants;
    private boolean isBooked;
    private LocalTime bookingStartTime;
    private int bookingDuration;
    private boolean acOn;
    private boolean lightsOn;

    public Room(int roomNumber) {
        this.roomNumber = roomNumber;
        this.maxCapacity = 0;
        this.currentOccupants = 0;
        this.isBooked = false;
        this.acOn = false;
        this.lightsOn = false;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public boolean isAcOn() {
        return acOn;
    }

    public boolean areLightsOn() {
        return lightsOn;
    }

    public void setMaxCapacity(int capacity) {
        this.maxCapacity = capacity;
        System.out.println("Room " + roomNumber + " maximum capacity set to " + capacity + ".");
    }

    public void addOccupants(int occupants) {
        this.currentOccupants += occupants;
        if (currentOccupants >= 2) {
            turnOnACAndLights();
        } else {
            turnOffACAndLights();
        }
    }

    public void removeOccupants() {
        this.currentOccupants = 0;
        turnOffACAndLights();
    }

    public void bookRoom(LocalTime startTime, int duration) {
        this.isBooked = true;
        this.bookingStartTime = startTime;
        this.bookingDuration = duration;
        System.out.println("Room " + roomNumber + " booked from " + startTime + " for " + duration + " minutes.");
    }

    public void cancelBooking() {
        if (!isBooked) {
            System.out.println("Room " + roomNumber + " is not booked. Cannot cancel booking.");
        } else {
            this.isBooked = false;
            System.out.println("Booking for Room " + roomNumber + " cancelled successfully.");
        }
    }

    public void checkIfBookingShouldBeReleased() {
        if (isBooked && currentOccupants < 2) {
            System.out.println("Room " + roomNumber + " is now unoccupied. Booking released. AC and lights turned off.");
            isBooked = false;
            acOn = false;
            lightsOn = false;
        }
    }

    private void turnOnACAndLights() {
        if (!acOn || !lightsOn) {
            System.out.println("Room " + roomNumber + " is now occupied by " + currentOccupants + " persons. AC and lights turned on.");
        }
        acOn = true;
        lightsOn = true;
    }

    private void turnOffACAndLights() {
        if (acOn || lightsOn) {
            System.out.println("Room " + roomNumber + " is now unoccupied. AC and lights turned off.");
        }
        acOn = false;
        lightsOn = false;
    }
}

// Singleton Office class to manage room bookings and configuration
class Office {
    private static Office instance;
    private Map<Integer, Room> rooms;

    private Office() {
        rooms = new HashMap<>();
    }

    public static Office getInstance() {
        if (instance == null) {
            instance = new Office();
        }
        return instance;
    }

    public void configureRooms(int roomCount) {
        for (int i = 1; i <= roomCount; i++) {
            rooms.put(i, new Room(i));
        }
        System.out.println("Office configured with " + roomCount + " meeting rooms: " + rooms.keySet() + ".");
    }

    public Room getRoom(int roomNumber) {
        if (!rooms.containsKey(roomNumber)) {
            System.out.println("Room " + roomNumber + " does not exist.");
            return null;
        }
        return rooms.get(roomNumber);
    }

    public void checkBookingsForRelease() {
        for (Room room : rooms.values()) {
            room.checkIfBookingShouldBeReleased();
        }
    }
}

// Commands for different room operations
class AddOccupantCommand implements Command {
    private Room room;
    private int occupants;

    public AddOccupantCommand(Room room, int occupants) {
        this.room = room;
        this.occupants = occupants;
    }

    @Override
    public void execute() {
        if (room != null) {
            room.addOccupants(occupants);
        }
    }
}

class RemoveOccupantCommand implements Command {
    private Room room;

    public RemoveOccupantCommand(Room room) {
        this.room = room;
    }

    @Override
    public void execute() {
        if (room != null) {
            room.removeOccupants();
        }
    }
}

class BookRoomCommand implements Command {
    private Room room;
    private LocalTime startTime;
    private int duration;

    public BookRoomCommand(Room room, LocalTime startTime, int duration) {
        this.room = room;
        this.startTime = startTime;
        this.duration = duration;
    }

    @Override
    public void execute() {
        if (room != null && !room.isBooked()) {
            room.bookRoom(startTime, duration);
        } else {
            System.out.println("Room " + room.getRoomNumber() + " is already booked during this time. Cannot book.");
        }
    }
}

class CancelRoomCommand implements Command {
    private Room room;

    public CancelRoomCommand(Room room) {
        this.room = room;
    }

    @Override
    public void execute() {
        if (room != null) {
            room.cancelBooking();
        }
    }
}

// Main class to handle user input and simulate the office environment
public class SmartOfficeApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Office office = Office.getInstance();

        while (true) {
            System.out.println("\n1. Configure Rooms\n2. Set Room Capacity\n3. Add Occupants\n4. Remove Occupants");
            System.out.println("5. Book Room\n6. Cancel Room Booking\n7. Exit\nChoose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            try {
                switch (option) {
                    case 1:
                        System.out.print("Enter number of meeting rooms: ");
                        int roomCount = scanner.nextInt();
                        office.configureRooms(roomCount);
                        break;

                    case 2:
                        System.out.print("Enter room number to set capacity: ");
                        int roomNumber = scanner.nextInt();
                        Room room = office.getRoom(roomNumber);
                        if (room != null) {
                            System.out.print("Enter room capacity: ");
                            int capacity = scanner.nextInt();
                            if (capacity > 0) {
                                room.setMaxCapacity(capacity);
                            } else {
                                System.out.println("Invalid capacity. Please enter a valid positive number.");
                            }
                        }
                        break;

                    case 3:
                        System.out.print("Enter room number to add occupants: ");
                        roomNumber = scanner.nextInt();
                        System.out.print("Enter number of occupants: ");
                        int occupants = scanner.nextInt();
                        room = office.getRoom(roomNumber);
                        new AddOccupantCommand(room, occupants).execute();
                        break;

                    case 4:
                        System.out.print("Enter room number to remove occupants: ");
                        roomNumber = scanner.nextInt();
                        room = office.getRoom(roomNumber);
                        new RemoveOccupantCommand(room).execute();
                        break;

                    case 5:
                        System.out.print("Enter room number to book: ");
                        roomNumber = scanner.nextInt();
                        System.out.print("Enter booking start time (HH:mm): ");
                        String time = scanner.next();
                        System.out.print("Enter booking duration in minutes: ");
                        int duration = scanner.nextInt();
                        room = office.getRoom(roomNumber);
                        new BookRoomCommand(room, LocalTime.parse(time), duration).execute();
                        break;

                    case 6:
                        System.out.print("Enter room number to cancel booking: ");
                        roomNumber = scanner.nextInt();
                        room = office.getRoom(roomNumber);
                        new CancelRoomCommand(room).execute();
                        break;

                    case 7:
                        System.out.println("Exiting the application.");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid option. Try again.");
                }

                // Check and release unoccupied rooms after bookings
                office.checkBookingsForRelease();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
