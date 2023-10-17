package Lab5;

public class OutputResults {
    public static void printToConsole(Shipment shipment) {
        Path path = shipment.getShippingPath();
        int cost = path.getCost();
        int eta = shipment.getEta();
        int time = path.getTime();
        int maxTime = eta * 2;
        cost = time > maxTime ? 0 : cost;
        
        System.out.println(shipment.getCustomerName());
        System.out.println("Shipment ETA was " + eta);
        System.out.println("The Path from start to end is as follows: ");
        for (ShippingNodeConnection connection : path.getConnections()) {
            System.out.println(connection.toString());
        }
        System.out.println("Transit cost from start is " + cost);
        System.out.println("Transit time from start is " + time);
        System.out.println();

    }
}
