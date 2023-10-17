package Lab5;

import java.util.Objects;

public class ShippingNodeConnection {

    private int id;
    private ShippingNode originNode;
    private ShippingNode destinationNode;
    private int cost;
    private int time;
    private VehicleType type;

    public ShippingNodeConnection(int id, ShippingNode originNode, ShippingNode destinationNode, int cost, int time,
            VehicleType type) {
        this.id = id;
        this.originNode = originNode;
        this.destinationNode = destinationNode;
        this.cost = cost;
        this.time = time;
        this.type = type;
    }

    public int getId() {
        return this.id;
    }

    public ShippingNode getOriginNode() {
        return originNode;
    }

    public ShippingNode getDestinationNode() {
        return destinationNode;
    }

    public int getCost() {
        return this.cost;
    }

    public int getTime() {
        return this.time;
    }

    public String getConnectionType() {
        return this.type.getVehicleTypeName();
    }

    public boolean isSimilarConnection(ShippingNodeConnection testConnection) {
        ShippingNode testOrigin = testConnection.getOriginNode();
        ShippingNode testDestination = testConnection.getDestinationNode();
        boolean connectionIsSimilar = originNode.equals(testOrigin) && destinationNode.equals(testDestination);
        boolean reverseConnectionIsSimilar = originNode.equals(testDestination) && destinationNode.equals(testOrigin);

        return (connectionIsSimilar || reverseConnectionIsSimilar);
    }

    @Override
    public String toString() {
        return ("Connection from " + originNode.getName() + " to " + destinationNode.getName() + " via " + type);
    }

    // Override to allow a comparison of nodes based on names using List.contains()
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ShippingNodeConnection otherConnection = (ShippingNodeConnection) obj;
        return id == otherConnection.id;
    }

    // Override for consistancy to match equals override, so hash comparison output
    // matches List.contains();
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
