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
    private int elevatorCapacity;

    public ElevatorSimulation(String propertiesFilePath) {
        PropertyManager propertyManager = new PropertyManager(propertiesFilePath);
        this.numberOfFloors = Integer.parseInt(propertyManager.getProperty("floors"));
        this.numberOfElevators = Integer.parseInt(propertyManager.getProperty("elevators"));
        this.passengerProbability = Double.parseDouble(propertyManager.getProperty("passengers"));
        this.simulationDuration = Integer.parseInt(propertyManager.getProperty("duration"));
        this.elevatorCapacity = Integer.parseInt(propertyManager.getProperty("elevatorCapacity"));
        this.floors = new ArrayList<>();
        this.elevators = new ArrayList<>();
        this.random = new Random();
        this.currentTick = 0;

        for (int i = 0; i < numberOfFloors; i++) {
            floors.add(new Floor(i));
        }
        for (int i = 0; i < numberOfElevators; i++) {
            elevators.add(new Elevator(elevatorCapacity, numberOfFloors));
        }
    }

    public void startSimulation() {
        System.out.println("Starting simulation.");
        while (currentTick < simulationDuration) {
            System.out.println("Tick: " + currentTick);
            generatePassengers();
            determineElevatorDirections();
            moveElevators();
            processElevatorStops();
            updateElevatorsDirection();
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
            // If the elevator is not idle or has passengers, skip setting new direction
            if (elevator.getDirection() != Elevator.Direction.IDLE || !elevator.getPassengers().isEmpty()) {
                continue;
            }

            // Check if there are any passengers waiting on any floor
            if (noPassengersWaiting()) {
                elevator.setDirection(Elevator.Direction.IDLE);
                continue;
            }

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
    private boolean noPassengersWaiting() {
        for (Floor floor : floors) {
            if (!floor.getUpQueue().isEmpty() || !floor.getDownQueue().isEmpty()) {
                return false;
            }
        }
        return true;
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
            if (elevator.getDirection() == Elevator.Direction.IDLE) {
                continue;
            }

            int nextStop = findNextStop(elevator);
            int floorsToMove = Math.min(Math.abs(nextStop - elevator.getCurrentFloor()), 5);
            // Move the elevator towards the next stop
            for (int i = 0; i < floorsToMove; i++) {
                if (elevator.getDirection() == Elevator.Direction.UP && elevator.getCurrentFloor() < numberOfFloors - 1) {
                    elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
                } else if (elevator.getDirection() == Elevator.Direction.DOWN && elevator.getCurrentFloor() > 0) {
                    elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);
                }
            }

            System.out.println("Elevator now at floor " + elevator.getCurrentFloor());
            // Existing logic for handling direction changes
        }
    }

    private int findNextStop(Elevator elevator) {
        int nextStop = elevator.getDirection() == Elevator.Direction.UP ? numberOfFloors : -1;
        int currentFloor = elevator.getCurrentFloor();
        int directionFactor = elevator.getDirection() == Elevator.Direction.UP ? 1 : -1;

        // Check passengers' destinations inside the elevator
        for (Passenger passenger : elevator.getPassengers()) {
            int destination = passenger.getDestinationFloor();
            if ((directionFactor == 1 && destination > currentFloor && destination < nextStop) ||
                    (directionFactor == -1 && destination < currentFloor && destination > nextStop)) {
                nextStop = destination;
            }
        }

        // Check waiting passengers on floors
        for (int i = currentFloor + directionFactor; i >= 0 && i < numberOfFloors; i += directionFactor) {
            Floor floor = floors.get(i);
            if (!floor.getUpQueue().isEmpty() || !floor.getDownQueue().isEmpty()) {
                nextStop = i;
                break;
            }
        }

        return nextStop;
    }
    private void updateElevatorsDirection() {
        for (Elevator elevator : elevators) {
            updateElevatorDirection(elevator);
        }
    }
    private void updateElevatorDirection(Elevator elevator) {
        // If the elevator is at the top or bottom floor, reverse its direction
        if (elevator.getCurrentFloor() == 0) {
            elevator.setDirection(Elevator.Direction.UP);
        } else if (elevator.getCurrentFloor() == numberOfFloors - 1) {
            elevator.setDirection(Elevator.Direction.DOWN);
        } else {
            // Check if there are more passengers to serve in the current direction
            boolean hasMorePassengers = false;
            if (elevator.getDirection() == Elevator.Direction.UP) {
                hasMorePassengers = hasPassengersAbove(elevator) || elevatorHasPassengersForUpperFloors(elevator);
            } else if (elevator.getDirection() == Elevator.Direction.DOWN) {
                hasMorePassengers = hasPassengersBelow(elevator) || elevatorHasPassengersForLowerFloors(elevator);
            }

            // If there are no more passengers to serve in the current direction, change direction or set to idle
            if (!hasMorePassengers) {
                if (elevator.getDirection() == Elevator.Direction.UP) {
                    elevator.setDirection(hasPassengersBelow(elevator) ? Elevator.Direction.DOWN : Elevator.Direction.IDLE);
                } else if (elevator.getDirection() == Elevator.Direction.DOWN) {
                    elevator.setDirection(hasPassengersAbove(elevator) ? Elevator.Direction.UP : Elevator.Direction.IDLE);
                }
            }
        }
    }

    // Helper methods to check for passengers
    private boolean hasPassengersAbove(Elevator elevator) {
        for (int i = elevator.getCurrentFloor() + 1; i < numberOfFloors; i++) {
            if (!floors.get(i).getUpQueue().isEmpty() || !floors.get(i).getDownQueue().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasPassengersBelow(Elevator elevator) {
        for (int i = elevator.getCurrentFloor() - 1; i >= 0; i--) {
            if (!floors.get(i).getUpQueue().isEmpty() || !floors.get(i).getDownQueue().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean elevatorHasPassengersForUpperFloors(Elevator elevator) {
        return elevator.getPassengers().stream().anyMatch(p -> p.getDestinationFloor() > elevator.getCurrentFloor());
    }

    private boolean elevatorHasPassengersForLowerFloors(Elevator elevator) {
        return elevator.getPassengers().stream().anyMatch(p -> p.getDestinationFloor() < elevator.getCurrentFloor());
    }

    private void processElevatorStops() {
        for (Elevator elevator : elevators) {
            Floor currentFloor = floors.get(elevator.getCurrentFloor());
            System.out.println("Before unloading/loading: " + elevator.toString());

            // Unload passengers
            Queue<Passenger> unloadedPassengers = elevator.unloadPassengers();
            for (Passenger passenger : unloadedPassengers) {
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

    private void loadPassengers(Elevator elevator, Queue<Passenger> queue) {
        while (!queue.isEmpty() && elevator.getPassengers().size() < elevator.getCapacity()) {
            Passenger passenger = queue.poll();
            elevator.loadPassenger(passenger);
            passenger.setTickBoarded(currentTick);
        }
    }

    // Main method to run the simulation
    public static void main(String[] args) {
        // Path to your properties file
        String propertiesFilePath = "/users/ai/Downloads/myFile.properties";
        // Create and start the elevator simulation
        ElevatorSimulation simulation = new ElevatorSimulation(propertiesFilePath);
        simulation.startSimulation();
    }
}
