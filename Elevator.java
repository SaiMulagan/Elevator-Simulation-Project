import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;

public class Elevator {
    private int currentFloor;
    private int capacity;
    private Direction direction;
    private Queue<Passenger> passengers;

    public enum Direction {
        UP, DOWN, IDLE
    }

    public Elevator(int capacity) {
        this.currentFloor = 0; // Assuming ground floor as start
        this.capacity = capacity;
        this.direction = Direction.IDLE;
        this.passengers = new LinkedList<>();
    }

    // Move the elevator one floor up or down
    public void move() {
        if (direction == Direction.UP) {
            currentFloor++;
        } else if (direction == Direction.DOWN) {
            currentFloor--;
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
