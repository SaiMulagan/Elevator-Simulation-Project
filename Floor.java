import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Floor {
    private int floorNumber;
    private Queue<Passenger> upQueue;   // Queue for passengers going up
    private Queue<Passenger> downQueue; // Queue for passengers going down
    private List<Passenger> passengerHistory;

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.upQueue = new LinkedList<>();
        this.downQueue = new LinkedList<>();
        this.passengerHistory = new ArrayList<>(); // Initialize the passenger history list
    }

    // Method to add a passenger to the appropriate queue
    public void addPassenger(Passenger passenger) {
        if (passenger.getDestinationFloor() > this.floorNumber) {
            upQueue.add(passenger);
        } else {
            downQueue.add(passenger);
        }
        passengerHistory.add(passenger); // Add the passenger to the history list
    }

    // Getters for the queues
    public Queue<Passenger> getUpQueue() {
        return upQueue;
    }

    public Queue<Passenger> getDownQueue() {
        return downQueue;
    }

    // Get floor number
    public int getFloorNumber() {
        return floorNumber;
    }

    // Method to remove passengers from the queues
    public Passenger dequeueUp() {
        return upQueue.poll();
    }

    public List<Passenger> getAllPassengers() {
        return new ArrayList<>(passengerHistory); // Return a copy of the passenger history
    }

    public Passenger dequeueDown() {
        return downQueue.poll();
    }

    @Override
    public String toString() {
        return "Floor{" +
                "floorNumber=" + floorNumber +
                ", upQueueSize=" + upQueue.size() +
                ", downQueueSize=" + downQueue.size() +
                '}';
    }
}
