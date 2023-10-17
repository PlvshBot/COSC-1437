package Lab5;

import java.util.ArrayList;

public class Vehicle {

    private VehicleType type;
    private String name;
    private boolean isVehicleCommitted;
    private Path pathToDestination;
    private ShippingNode startNode;
    private Shipment shipment;

    public void setPathTopickup(Path pathToPickup) {
        this.pathToDestination = pathToPickup;
    }

    public boolean isVehicleCommitted() {
        return isVehicleCommitted;
    }

    public Vehicle(VehicleType vehicleType, String name, ShippingNode startingNode) {
        this.type = vehicleType;
        this.name = name;
        this.isVehicleCommitted = false;
        this.startNode = startingNode;
    }

    public String getName() {
        return this.name;
    }
    
    public int getWeight() {
    	return type.getWeight();
    }

    public String getVehicleTypeName() {
        return type.getVehicleTypeName();
    }

    public void commitVehicleToShipment(Shipment shipment) {
        isVehicleCommitted = true;
        this.shipment = shipment;
    }

    public boolean canTraverse(ShippingNodeConnection connection) {
        String connectionType = connection.getConnectionType();
        return (connectionType == type.getVehicleTypeName());
    }

    public Path getPathToNode(ShippingNode target) {
        ShippingNode currentNode;
        if (pathToDestination == null) {
            currentNode = startNode;
        } else {
            currentNode = pathToDestination.getCurrentNode();
        }
        return PathFinder.findPath(currentNode, target, this);
    }

    public void releaseVehicleFromShipment() {
        shipment = null;
        isVehicleCommitted = false;
        pathToDestination = null;
    }

    public static Vehicle reserveVehicle(Shipment shipment, ArrayList<Vehicle> vehicles) {
        ShippingNode pickupLocation = shipment.getShippingPath().getCurrentNode();
        String nextConnectionType = shipment.getNextConnection().getConnectionType();
        int bestTime = Integer.MAX_VALUE;
        Vehicle bestVehicle = null;
        Path tentativeVehiclePath = new Path();

        // Find all the best vehicles capable of reaching the starting node and making the first connection.
        for (Vehicle vehicle : vehicles) {
            if (!vehicle.isVehicleCommitted() && nextConnectionType.equals(vehicle.getVehicleTypeName()) && 
                    vehicle.getWeight() >= shipment.getWeight()) {
                tentativeVehiclePath = vehicle.getPathToNode(pickupLocation);
                if (tentativeVehiclePath.getTime() < bestTime) {
                    bestTime = tentativeVehiclePath.getTime();
                    bestVehicle = vehicle;
                }
            }
        }

        if (bestVehicle == null) {
            return null; // No suitable vehicle found.
        } else {
            // Add the shipment path to the vehicle path
            Path shipmentPath = shipment.getShippingPath().deepCopy();
            for (ShippingNodeConnection connection : shipmentPath.getConnections()) {
                tentativeVehiclePath.addConnectionToPath(connection);
            }
            bestVehicle.commitVehicleToShipment(shipment);
            bestVehicle.setPathTopickup(tentativeVehiclePath);
            return bestVehicle;
        }
    }


    public void advanceVehicle() {
        // Check if the shipment is on the vehicle

        ShippingNode vehicleNode = this.getCurrentNode();
        ShippingNode shipmentNode = shipment.getCurrentNode();
        if (vehicleNode == shipmentNode) {
            // Check if the shipment is at it's destination, or if the vehicle cannnot make
            // the next connection.
            if (shipment.getCurrentNode() == shipment.getDestinationNode()
                    || shipment.getNextConnection().getConnectionType() != this.getVehicleTypeName()) {
                shipment.releaseShipmentFromVehicle();
                releaseVehicleFromShipment();
                return;
            } else {
                pathToDestination.advancePath();
                shipment.advanceShipment();
            }
        } else {

            shipment.addTransitTime(pathToDestination.getNextConnection().getTime());
            pathToDestination.advancePath();
        }

    }

    private ShippingNode getCurrentNode() {
        return pathToDestination.getCurrentNode();
    }

}