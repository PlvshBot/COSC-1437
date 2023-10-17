package Lab5;

import java.util.ArrayList;

public class Path {
    private ArrayList<ShippingNodeConnection> connectionsInPath;
    private int cost;
    private int time;
    private Vehicle vehicle;
    private ShippingNode currentNode;
    private int currentNodeIndex;

    public Path() {
        connectionsInPath = new ArrayList<>();
        currentNodeIndex = 0;
    }

    public ArrayList<ShippingNodeConnection> getConnections() {
        return connectionsInPath;
    }

    public ShippingNodeConnection getNextConnection(ShippingNode startNode) {
        return connectionsInPath.get(currentNodeIndex + 1);
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public ShippingNode getCurrentNode() {
        return currentNode;
    }

    public ShippingNodeConnection getNextConnection() {
        try {
            return connectionsInPath.get(currentNodeIndex + 1);
        } catch (Exception e) {
            return connectionsInPath.get(currentNodeIndex);
        }
    }

    public void advancePath() {
        // If the shipment is delivered //
        currentNode = connectionsInPath.get(currentNodeIndex).getDestinationNode();
        int lastIndex = connectionsInPath.size() - 1;
        if (currentNodeIndex != lastIndex) {
            currentNodeIndex++;
        }
    }

    public Path deepCopy() {
        Path newPath = new Path();
        for (ShippingNodeConnection connection : this.connectionsInPath) {
            newPath.addConnectionToPath(connection);
        }
        newPath.vehicle = this.vehicle;
        return newPath;
    }

    public void addConnectionToPath(ShippingNodeConnection connection) {
        if (connectionsInPath.size() == 0) {
            currentNode = connection.getOriginNode();
        }
        this.connectionsInPath.add(connection);
        this.cost += connection.getCost();
        this.time += connection.getTime();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Path Cost is " + cost).append("\n");
        builder.append("Path Time is " + time).append("\n");
        builder.append("Current Node is " + currentNode.getName());
        builder.append("Current Node index is " + currentNodeIndex);
        builder.append("Nodes in Path are as follows").append("\n");

        for (ShippingNodeConnection connection : connectionsInPath) {
            builder.append(connection.toString())
                    .append("\n");
        }
        return builder.toString();
    }
}
