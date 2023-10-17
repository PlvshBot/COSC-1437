package Lab5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ShippingNode {
    private String name;
    private Path pathToGetTo = new Path();
    private ArrayList<ShippingNodeConnection> neighbors = new ArrayList<>();

    public ShippingNode(String name) {
        this.name = name;
        pathToGetTo = new Path();
    }

    public String getName() {
        return name;
    }

    public Path getPath() {
        return this.pathToGetTo;
    }

    public void setPath(Path path) {
        this.pathToGetTo = path;
    }

    public int getCost() {
        return pathToGetTo.getCost();
    }

    public int getTime() {
        return this.pathToGetTo.getTime();
    }

    public ArrayList<ShippingNodeConnection> getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(ShippingNodeConnection connection) {
        this.neighbors.add(connection);
    }

    public static void connectNeighbors(int id, ShippingNode fromNode, ShippingNode toNode, int cost, int time,
            VehicleType type) {
        ShippingNodeConnection newConnection = new ShippingNodeConnection(id, fromNode, toNode, cost, time, type);
        fromNode.addNeighbor(newConnection);
    }

    // Recursive call to traverse graph and remove the paths at the beginning
    // of each pass
    public void resetNodeAndAllOtherNodes() {
        Map<String, ShippingNode> visitedNodes = new HashMap<>();
        this.resetRecursively(visitedNodes);
    }

    private void resetRecursively(Map<String, ShippingNode> visitedNodes) {
        if (visitedNodes != null && visitedNodes.containsKey(this.getName())) {
            return;
        }

        visitedNodes.put(this.getName(), this);

        this.setPath(new Path());

        for (ShippingNodeConnection connection : this.getNeighbors()) {
            connection.getDestinationNode().resetRecursively(visitedNodes);
        }

        visitedNodes.remove(this.getName());
    }

    // Override to allow a comparison of nodes based on names using List.contains()
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ShippingNode otherNode = (ShippingNode) obj;
        return name.equals(otherNode.name);
    }

    // Override for consistancy to match equals override, so hash comparison output
    // matches List.contains();
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
