public class Passenger {
    private int originFloor;
    private int destinationFloor;
    private int tickAppeared;
    private int tickBoarded;
    private int tickArrived;

    public Passenger(int originFloor, int destinationFloor, int tickAppeared) {
        this.originFloor = originFloor;
        this.destinationFloor = destinationFloor;
        this.tickAppeared = tickAppeared;
        this.tickBoarded = -1; // Initialized to -1, will be set when they board an elevator
        this.tickArrived = -1; // Initialized to -1, will be set when they arrive at destination
    }

    // Getters and Setters
    public int getOriginFloor() {
        return originFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public int getTickAppeared() {
        return tickAppeared;
    }

    public int getTickBoarded() {
        return tickBoarded;
    }

    public void setTickBoarded(int tickBoarded) {
        this.tickBoarded = tickBoarded;
    }

    public int getTickArrived() {
        return tickArrived;
    }

    public void setTickArrived(int tickArrived) {
        this.tickArrived = tickArrived;
    }
    
}
