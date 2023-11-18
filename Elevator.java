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
        this.numberOfFloors = numberOfFloors;
        this.direction = Direction.IDLE;
        this.passengers = new LinkedList<>();
    }
    public Queue<Passenger> getPassengers() {
        return passengers;
    }

    // Move the elevator one floor up or down
    public void move() {
        int previousFloor = this.currentFloor;
        int floorsToMove = Math.min(5, numberOfFloors - 1);
        if (this.direction == Direction.IDLE) {
            return; // Do not move if the elevator is idle
        }
        for (int i = 0; i < floorsToMove; i++) {
            if (this.direction == Direction.UP && this.currentFloor < numberOfFloors - 1) {
                this.currentFloor++;
            } else if (this.direction == Direction.DOWN && this.currentFloor > 0) {
                this.currentFloor--;
            }
        }

        // Print the message only if the floor has changed
        if (this.currentFloor != previousFloor) {
            System.out.println("Elevator now at floor " + this.currentFloor);
        }

        if (currentFloor == 0) {
            if (!hasPassengersForLowerFloors()) {
                direction = Direction.UP;
            }
        }
        else if (currentFloor == numberOfFloors - 1) {
            if (!hasPassengersForUpperFloors()) {
                direction = Direction.DOWN;
            }
            // Handling top and bottom floors
        if (currentFloor == numberOfFloors - 1 && direction == Direction.UP) {
            if (hasPassengersForLowerFloors()) {
                direction = Direction.DOWN;
            } else {
                direction = Direction.IDLE;
            }
        } else if (currentFloor == 0 && direction == Direction.DOWN) {
            if (hasPassengersForUpperFloors()) {
                direction = Direction.UP;
            } else {
                direction = Direction.IDLE;
            }
        }
        }
    }

    private boolean hasPassengersForLowerFloors() {
        for (Passenger passenger : passengers) {
            if (passenger.getDestinationFloor() < currentFloor) {
                return true;
            }
        }
        return false;
    }

    private boolean hasPassengersForUpperFloors() {
        for (Passenger passenger : passengers) {
            if (passenger.getDestinationFloor() > currentFloor) {
                return true;
            }
        }
        return false;
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
