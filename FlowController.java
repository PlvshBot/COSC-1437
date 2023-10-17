package Lab5;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FlowController {
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Shipment> shipments;

    public FlowController(FileData data) {
        this.vehicles = data.vehicles;
        this.shipments = data.shipments;
    }

    public void optimizeNetwork() {
        // Thread-safe copy of the undelivered list to allow for concurrent modification
        CopyOnWriteArrayList<Shipment> undelivered = new CopyOnWriteArrayList<>(shipments);

        while (!undelivered.isEmpty()) {
            for (Shipment shipment : undelivered) {
                if (shipment.isShipmentDelivered()) {
                    undelivered.remove(shipment);
                    continue;
                }

                if (shipment.getReservedVehicle() == null) {
                    Vehicle reservedVehicle = Vehicle.reserveVehicle(shipment, vehicles);
                    if (reservedVehicle != null) {
                        shipment.commitShipmentToVehicle(reservedVehicle);
                    }
                }

                if (shipment.getReservedVehicle() != null) {
                    List<Shipment> splittedShipments = splitShipment(shipment);
                    for (Shipment splitShipment : splittedShipments) {
                        Vehicle reservedVehicle = Vehicle.reserveVehicle(splitShipment, vehicles);
                        if (reservedVehicle != null) {
                            splitShipment.commitShipmentToVehicle(reservedVehicle);
                        }
                    }
                }

                if (shipment.getReservedVehicle() != null) {
                    shipment.getReservedVehicle().advanceVehicle();
                }
            }
        }

        for (Shipment shipment : shipments) {
            OutputResults.printToConsole(shipment);
        }
    }

    // Additional method to split a shipment into multiple shipments if it exceeds the vehicle's weight limit
    private List<Shipment> splitShipment(Shipment shipment) {
        List<Shipment> splittedShipments = new ArrayList<>();
        int vehicleWeightLimit = shipment.getReservedVehicle().getWeight();
        if (shipment.getWeight() > vehicleWeightLimit) {
            // Split the shipment into multiple shipments
            while (shipment.getWeight() > vehicleWeightLimit) {
                int remainingWeight = shipment.getWeight() - vehicleWeightLimit;
                Shipment newShipment = new Shipment(shipment.getTime(), shipment.getCustomerName(),
                        shipment.getSourceNode(), shipment.getDestinationNode(), vehicleWeightLimit);
                splittedShipments.add(newShipment);
                System.out.println(shipment.getCustomerName() + " was split into weights of " + remainingWeight + " and " + vehicleWeightLimit);
                shipment.setWeight(remainingWeight);
            }
        }

        return splittedShipments;
    }
}