# Elevator Simulation Project
This assignment tested the ability to use the data structures we studied so far to create a simulation of one or more elevators.

## Classes and Responsibilities
### ElevatorSimulation:
Manages the simulation, including initializing the system and running the simulation loop.
### Elevator:
Represents an individual elevator, handling its movement, passenger loading, and unloading.
### Floor:
Represents a floor in the building, managing passengers waiting for elevators with separate queues for upward and downward travel.
### Passenger:
Represents a passenger, including their origin, destination, and timestamps for various events (arrival, boarding, and reaching the destination).
### PropertyManager:
Handles reading and managing configuration properties from a file.

## Data Structure Choices
### Queues (LinkedList): 
Used in the Floor class to manage waiting passengers efficiently.
### ArrayLists: 
Used for storing collections of Elevator and Floor objects in ElevatorSimulation.

I coudn't figure out how to switch between linked and array list!

## To change path of properties file go to main.
