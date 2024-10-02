import java.util.*;

// Observer pattern: ChatRoom notifies users when a new message is posted
interface ChatObserver {
    void update(String message);
}

// User class, implementing the ChatObserver interface
class User implements ChatObserver {
    private String name;

    public User(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println(name + " received: " + message);
    }

    public String getName() {
        return name;
    }

    // Simulate sending a message
    public void sendMessage(ChatRoom chatRoom, String message) {
        chatRoom.sendMessage(this, message);
    }
}

// ChatRoom acts as the subject in the observer pattern
class ChatRoom {
    private String roomId;
    private List<User> users = new ArrayList<>();
    private List<String> messageHistory = new ArrayList<>();

    public ChatRoom(String roomId) {
        this.roomId = roomId;
    }

    // Register a user in the chat room
    public void join(User user) {
        users.add(user);
        notifyAllUsers(user.getName() + " has joined the chat room.");
        displayActiveUsers();
    }

    // Unregister a user from the chat room
    public void leave(User user) {
        users.remove(user);
        notifyAllUsers(user.getName() + " has left the chat room.");
        displayActiveUsers();
    }

    // Send a message in the chat room
    public void sendMessage(User sender, String message) {
        String formattedMessage = sender.getName() + ": " + message;
        messageHistory.add(formattedMessage);
        notifyAllUsers(formattedMessage);
    }

    // Notify all users in the chat room of a new message
    private void notifyAllUsers(String message) {
        for (User user : users) {
            user.update(message);
        }
    }

    // Display list of active users in the chat room
    public void displayActiveUsers() {
        System.out.println("Active users in " + roomId + ":");
        for (User user : users) {
            System.out.println(user.getName());
        }
    }

    // Get the room ID
    public String getRoomId() {
        return roomId;
    }

    // Retrieve message history
    public List<String> getMessageHistory() {
        return messageHistory;
    }
}

// Singleton Pattern: Manages all chat rooms
class ChatRoomManager {
    private static ChatRoomManager instance;
    private Map<String, ChatRoom> chatRooms = new HashMap<>();

    private ChatRoomManager() {
    }

    public static ChatRoomManager getInstance() {
        if (instance == null) {
            instance = new ChatRoomManager();
        }
        return instance;
    }

    public ChatRoom getChatRoom(String roomId) {
        return chatRooms.computeIfAbsent(roomId, ChatRoom::new);
    }
}

// Adapter Pattern: Example of an adapter to handle WebSocket communication
class WebSocketAdapter {
    private ChatRoomManager chatRoomManager = ChatRoomManager.getInstance();

    public void connectUserToRoom(String userName, String roomId) {
        ChatRoom chatRoom = chatRoomManager.getChatRoom(roomId);
        User user = new User(userName);
        chatRoom.join(user);
    }

    public void disconnectUserFromRoom(User user, String roomId) {
        ChatRoom chatRoom = chatRoomManager.getChatRoom(roomId);
        chatRoom.leave(user);
    }

    public void sendMessageToRoom(User user, String roomId, String message) {
        ChatRoom chatRoom = chatRoomManager.getChatRoom(roomId);
        user.sendMessage(chatRoom, message);
    }

    public void displayChatHistory(String roomId) {
        ChatRoom chatRoom = chatRoomManager.getChatRoom(roomId);
        System.out.println("Chat history for room " + roomId + ":");
        for (String message : chatRoom.getMessageHistory()) {
            System.out.println(message);
        }
    }
}

// Main class to simulate the chat application
public class ChatApplication {
    public static void main(String[] args) {
        // Initialize WebSocket adapter (can be extended to different communication protocols)
        WebSocketAdapter webSocketAdapter = new WebSocketAdapter();

        // Simulate users joining a chat room
        String roomId = "Room123";
        webSocketAdapter.connectUserToRoom("Alice", roomId);
        webSocketAdapter.connectUserToRoom("Bob", roomId);

        // Get Alice and Bob to send messages
        User alice = new User("Alice");
        User bob = new User("Bob");

        webSocketAdapter.sendMessageToRoom(alice, roomId, "Hello, everyone!");
        webSocketAdapter.sendMessageToRoom(bob, roomId, "Hi, Alice!");

        // Display chat history
        webSocketAdapter.displayChatHistory(roomId);

        // Bob leaves the chat room
        webSocketAdapter.disconnectUserFromRoom(bob, roomId);
    }
}
