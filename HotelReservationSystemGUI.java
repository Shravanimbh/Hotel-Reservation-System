import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
 
// Custom exception for invalid age
class InvalidAgeException extends Exception {
    public InvalidAgeException(String message) {
        super(message);
    }
}
 
// Base class for Room
abstract class Room {
    private String type;
    private double price;
    private boolean isAC;
    private boolean hasVegFood;
    private boolean hasNonVegFood;
 
    public Room(String type, double price, boolean isAC, boolean hasVegFood, boolean hasNonVegFood) {
        this.type = type;
        this.price = price;
        this.isAC = isAC;
        this.hasVegFood = hasVegFood;
        this.hasNonVegFood = hasNonVegFood;
    }
 
    public String getType() {
        return type;
    }
 
    public double getPrice() {
        return price;
    }
 
    public boolean isAC() {
        return isAC;
    }
 
    public boolean hasVegFood() {
        return hasVegFood;
    }
 
    public boolean hasNonVegFood() {
        return hasNonVegFood;
    }
 
    public abstract String getDetails();
}
 
// Subclass for StandardRoom
class StandardRoom extends Room {
    public StandardRoom(boolean isAC, boolean hasVegFood, boolean hasNonVegFood) {
        super("Standard Room", 100.0, isAC, hasVegFood, hasNonVegFood);
    }
 
    @Override
    public String getDetails() {
        return String.format("%s - Price: $%.2f, AC: %b, Veg Food: %b, Non-Veg Food: %b",
                getType(), getPrice(), isAC(), hasVegFood(), hasNonVegFood());
    }
}
 
// Subclass for DeluxeRoom
class DeluxeRoom extends Room {
    public DeluxeRoom(boolean isAC, boolean hasVegFood, boolean hasNonVegFood) {
        super("Deluxe Room", 200.0, isAC, hasVegFood, hasNonVegFood);
    }
 
    @Override
    public String getDetails() {
        return String.format("%s - Price: $%.2f, AC: %b, Veg Food: %b, Non-Veg Food: %b",
                getType(), getPrice(), isAC(), hasVegFood(), hasNonVegFood());
    }
}
 
// Class representing a Hotel
class Hotel {
    private String name;
    private String location;
    private List<Room> rooms;
    private List<String> touristPlaces;
 
    public Hotel(String name, String location) {
        this.name = name;
        this.location = location;
        this.rooms = new ArrayList<>();
        this.touristPlaces = new ArrayList<>();
    }
 
    public void addRoom(Room room) {
        rooms.add(room);
    }
 
    public void addTouristPlace(String place) {
        touristPlaces.add(place);
    }
 
    public String getDetails() {
        StringBuilder details = new StringBuilder(name + ", located in " + location + ".\nNearby tourist places:\n");
        for (String place : touristPlaces) {
            details.append("  - ").append(place).append("\n");
        }
        details.append("Available rooms:\n");
        for (Room room : rooms) {
            details.append("  - ").append(room.getDetails()).append("\n");
        }
        return details.toString();
    }
 
    public List<Room> getRooms() {
        return rooms;
    }
 
    public String getName() {
        return name;
    }
}
 
// Class to manage hotel reservations
class Reservation {
    private Hotel hotel;
    private String guestName;
    private int age;
    private Room room;
 
    public Reservation(Hotel hotel, String guestName, int age, Room room) {
        this.hotel = hotel;
        this.guestName = guestName;
        this.age = age;
        this.room = room;
    }
 
    public void validateAge() throws InvalidAgeException {
        if (age < 18) {
            throw new InvalidAgeException("Guests under 18 must be accompanied by an adult.");
        }
    }
 
    public String book() throws InvalidAgeException {
        validateAge();
        return String.format("Booking confirmed for %s at %s Room Type: %s. Total Price: $%.2f",
                guestName, hotel.getName(), room.getType(), room.getPrice());
    }
}
 
public class HotelReservationSystemGUI extends JFrame {
    private List<Hotel> hotels;
    private JComboBox<String> hotelComboBox;
    private JComboBox<String> roomComboBox;
    private JTextField nameField;
    private JTextField ageField;
    private JTextArea outputArea;
 
    public HotelReservationSystemGUI() {
        // Sample hotels and rooms
        hotels = new ArrayList<>();
        Hotel seaside = new Hotel("Seaside Resort", "California");
        seaside.addRoom(new StandardRoom(true, true, true));
        seaside.addRoom(new DeluxeRoom(true, true, true));
        seaside.addTouristPlace("Beach");
        seaside.addTouristPlace("Amusement Park");
        Hotel mountainLodge = new Hotel("Mountain Lodge", "Colorado");
        mountainLodge.addRoom(new StandardRoom(false, true, false));
        mountainLodge.addRoom(new DeluxeRoom(true, true, true));
        mountainLodge.addTouristPlace("Hiking Trails");
        mountainLodge.addTouristPlace("Ski Resort");
        hotels.add(seaside);
        hotels.add(mountainLodge);
 
        // GUI Components
        setTitle("Hotel Reservation System");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));
 
        hotelComboBox = new JComboBox<>();
        for (Hotel hotel : hotels) {
            hotelComboBox.addItem(hotel.getName());
        }
 
        roomComboBox = new JComboBox<>();
        updateRoomOptions();
 
        nameField = new JTextField();
        ageField = new JTextField();
        outputArea = new JTextArea();
        outputArea.setEditable(false);
 
        JButton bookButton = new JButton("Book Room");
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookRoom();
            }
        });
 
        // Add components to the frame
        add(new JLabel("Select Hotel:"));
        add(hotelComboBox);
        add(new JLabel("Select Room:"));
        add(roomComboBox);
        add(new JLabel("Enter Name:"));
        add(nameField);
        add(new JLabel("Enter Age:"));
        add(ageField);
        add(bookButton);
        add(new JScrollPane(outputArea));
 
        hotelComboBox.addActionListener(e -> updateRoomOptions());
    }
 
    private void updateRoomOptions() {
        roomComboBox.removeAllItems();
        Hotel selectedHotel = hotels.get(hotelComboBox.getSelectedIndex());
        for (Room room : selectedHotel.getRooms()) {
            roomComboBox.addItem(room.getDetails());
        }
    }
 
    private void bookRoom() {
        try {
            Hotel selectedHotel = hotels.get(hotelComboBox.getSelectedIndex());
            String guestName = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            Room selectedRoom = selectedHotel.getRooms().get(roomComboBox.getSelectedIndex());
 
            Reservation reservation = new Reservation(selectedHotel, guestName, age, selectedRoom);
            String bookingDetails = reservation.book();
            outputArea.setText(bookingDetails);
        } catch (NumberFormatException e) {
            outputArea.setText("Invalid age. Please enter a number.");
        } catch (InvalidAgeException e) {
            outputArea.setText(e.getMessage());
        } catch (Exception e) {
            outputArea.setText("An unexpected error occurred: " + e.getMessage());
        }
    }
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HotelReservationSystemGUI gui = new HotelReservationSystemGUI();
            gui.setVisible(true);
        });
    }
}