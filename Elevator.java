import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;

public class Elevator {
    private int currentFloor;
    private int capacity;
    private Direction direction;
    private Queue<Passenger> passengers;
    private int numberOfFloors;

    public int getCapacity() {
        return capacity;
    }

    public enum Direction {
        UP, DOWN, IDLE
    }

    public Elevator(int capacity, int numberOfFloors) {
        this.currentFloor = 0; // Assuming ground floor as start
        this.capacity = capacity;
        this.numberOfFloors = this.numberOfFloors;
        this.direction = Direction.IDLE;
        this.passengers = new LinkedList<>();
    }
    public Queue<Passenger> getPassengers() {
        return passengers;
    }

    // Move the elevator one floor up or down
    public void move() {
        if (direction == Direction.UP && currentFloor < numberOfFloors - 1) {
            currentFloor++;
        } else if (direction == Direction.DOWN && currentFloor > 0) {
            currentFloor--;
        }

        // If the elevator reaches the top or bottom floor, change its direction or set it to IDLE
        if (currentFloor == 0 || currentFloor == numberOfFloors - 1) {
            // Decide how to handle the elevator at this point - example:
            // direction = Direction.IDLE;
            // Or reverse the direction, or implement a more complex logic based on your requirements
        }
    }

    // Load a passenger onto the elevator
    public boolean loadPassenger(Passenger passenger) {
        if (passengers.size() < capacity) {
            passengers.add(passenger);
            return true;
        }
        return false;
    }

    // Unload passengers for the current floor
    public Queue<Passenger> unloadPassengers() {
        Queue<Passenger> unloaded = new LinkedList<>();
        ArrayList<Passenger> toRemove = new ArrayList<>();

        // Identify passengers to unload
        for (Passenger passenger : passengers) {
            if (passenger.getDestinationFloor() == currentFloor) {
                unloaded.add(passenger);
                toRemove.add(passenger);
            }
        }

        // Remove passengers from the elevator
        for (Passenger passenger : toRemove) {
            passengers.remove(passenger);
        }

        return unloaded;
    }


    // Setters and Getters
    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "Elevator{" +
                "currentFloor=" + currentFloor +
                ", capacity=" + capacity +
                ", direction=" + direction +
                ", passengers=" + passengers.size() +
                '}';
    }
}
