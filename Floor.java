import java.util.Queue;
import java.util.LinkedList;

public class Floor {
    private int floorNumber;
    private Queue<Passenger> upQueue;   // Queue for passengers going up
    private Queue<Passenger> downQueue; // Queue for passengers going down

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.upQueue = new LinkedList<>();
        this.downQueue = new LinkedList<>();
    }

    // Method to add a passenger to the appropriate queue
    public void addPassenger(Passenger passenger) {
        if (passenger.getDestinationFloor() > this.floorNumber) {
            upQueue.add(passenger);
        } else {
            downQueue.add(passenger);
        }
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
