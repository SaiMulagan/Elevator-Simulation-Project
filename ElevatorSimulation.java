import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class ElevatorSimulation {
    private int numberOfFloors;
    private int numberOfElevators;
    private double passengerProbability;
    private int simulationDuration;
    private List<Floor> floors;
    private List<Elevator> elevators;
    private Random random;
    private int currentTick;

    public ElevatorSimulation(int numberOfFloors, int numberOfElevators, double passengerProbability, int simulationDuration) {
        this.numberOfFloors = numberOfFloors;
        this.numberOfElevators = numberOfElevators;
        this.passengerProbability = passengerProbability;
        this.simulationDuration = simulationDuration;
        this.floors = new ArrayList<>();
        this.elevators = new ArrayList<>();
        this.random = new Random();
        this.currentTick = 0;

        // Initialize floors and elevators
        for (int i = 0; i < numberOfFloors; i++) {
            floors.add(new Floor(i));
        }
        for (int i = 0; i < numberOfElevators; i++) {
            elevators.add(new Elevator(10, numberOfFloors)); // Pass numberOfFloors here
        }
    }

    // Method to start the simulation
    public void startSimulation() {
        System.out.println("Starting simulation.");
        while (currentTick < simulationDuration) {
            System.out.println("Tick: " + currentTick);
            generatePassengers();
            determineElevatorDirections();
            moveElevators();
            processElevatorStops();
            currentTick++;
        }
        System.out.println("Simulation ended.");
    }

    private void generatePassengers() {
        for (Floor floor : floors) {
            if (random.nextDouble() < passengerProbability) {
                int destinationFloor;
                do {
                    destinationFloor = random.nextInt(numberOfFloors);
                } while (destinationFloor == floor.getFloorNumber());

                Passenger passenger = new Passenger(floor.getFloorNumber(), destinationFloor, currentTick);
                floor.addPassenger(passenger);
                System.out.println("New passenger at floor " + floor.getFloorNumber() + " going to " + destinationFloor);
            }
        }
    }

    private void determineElevatorDirections() {
        for (Elevator elevator : elevators) {
            if (elevator.getDirection() == Elevator.Direction.IDLE) {
                int nearestFloor = findNearestFloorWithPassengers(elevator.getCurrentFloor());
                if (nearestFloor == -1) {
                    // No passengers waiting, keep the elevator idle or decide on a default behavior
                    continue;
                }
                if (nearestFloor > elevator.getCurrentFloor()) {
                    elevator.setDirection(Elevator.Direction.UP);
                } else if (nearestFloor < elevator.getCurrentFloor()) {
                    elevator.setDirection(Elevator.Direction.DOWN);
                }
            }
        }
    }

    private int findNearestFloorWithPassengers(int currentFloor) {
        for (int i = 0; i < floors.size(); i++) {
            if (!floors.get(i).getUpQueue().isEmpty() || !floors.get(i).getDownQueue().isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    private void moveElevators() {
        for (Elevator elevator : elevators) {
            elevator.move();
        }
    }

    private void processElevatorStops() {
        for (Elevator elevator : elevators) {
            Floor currentFloor = floors.get(elevator.getCurrentFloor());
            System.out.println("Before unloading/loading: " + elevator.toString());

            // Unload passengers
            for (Passenger passenger : elevator.unloadPassengers()) {
                passenger.setTickArrived(currentTick);
            }

            // Load passengers
            if (elevator.getDirection() == Elevator.Direction.UP) {
                loadPassengers(elevator, currentFloor.getUpQueue());
            } else if (elevator.getDirection() == Elevator.Direction.DOWN) {
                loadPassengers(elevator, currentFloor.getDownQueue());
            }

            System.out.println("After unloading/loading: " + elevator.toString());
        }
    }

    // ... previous methods ...

    private void loadPassengers(Elevator elevator, Queue<Passenger> queue) {
        while (!queue.isEmpty() && elevator.getPassengers().size() < elevator.getCapacity()) {
            Passenger passenger = queue.poll();
            elevator.loadPassenger(passenger);
            passenger.setTickBoarded(currentTick);
        }
    }

    // Main method to run the simulation
    public static void main(String[] args) {
        // Example configuration - these values can be changed or read from a file or arguments
        int numberOfFloors = 10;  // Number of floors in the simulation
        int numberOfElevators = 3; // Number of elevators
        double passengerProbability = 0.1; // Probability of a passenger appearing
        int simulationDuration = 100; // Number of ticks the simulation will run

        // Create and start the elevator simulation
        ElevatorSimulation simulation = new ElevatorSimulation(numberOfFloors, numberOfElevators, passengerProbability, simulationDuration);
        simulation.startSimulation();
    }
}
