package Lab5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileParser {
    private String filename;

    private enum ParseState {
        NETWORK,
        VEHICLES,
        SHIPMENT,
        UNKNOWN
    }

    public FileParser(String filename) {
        this.filename = filename;
    }

    public FileData readFile() throws FileNotFoundException {
        Map<String, ShippingNode> nodes = new HashMap<>();
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        ArrayList<Shipment> shipments = new ArrayList<>();
        ParseState state = ParseState.UNKNOWN;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    if (line.trim().equals("#network")) {
                        state = ParseState.NETWORK;
                    } else if (line.trim().equals("#vehicles")) {
                        state = ParseState.VEHICLES;
                    } else if (line.trim().equals("#cargo list")) {
                        state = ParseState.SHIPMENT;
                    }
                    continue;
                }

                switch (state) {
                    case NETWORK:
                        String[] parts = line.split("\\s+");
                        int id = Integer.parseInt(parts[0]);
                        String start = parts[1];
                        String end = parts[2];
                        int cost = Integer.parseInt(parts[3]);
                        int time = Integer.parseInt(parts[4]);
                        VehicleType type = VehicleType.fromString(parts[5]);
                        // The nodes appear many times over in the input file, but we should only
                        // create them once
                        ShippingNode fromNode = nodes.computeIfAbsent(start, name -> new ShippingNode(name));
                        ShippingNode toNode = nodes.computeIfAbsent(end, name -> new ShippingNode(name));
                        ShippingNode.connectNeighbors(id, fromNode, toNode, cost, time, type);
                        break;
                    case VEHICLES:
                        parts = line.split("\\s+");
                        type = VehicleType.fromString(parts[0]);
                        String name = parts[1];
                        ShippingNode node = nodes.get(parts[2]);
                        vehicles.add(new Vehicle(type, name, node));
                        break;
                    case SHIPMENT:
                        parts = line.split(",\\s+");
                        time = Integer.parseInt(parts[0]);
                        name = parts[1];
                        start = parts[2];
                        end = parts[3];
                        int weight = Integer.parseInt(parts[4]);
                        fromNode = nodes.get(start);
                        toNode = nodes.get(end);
                        shipments.add(new Shipment(time, name, fromNode, toNode, weight));
                        break;
                    default:
                        throw new IOException("File Parser encountered a fatal error");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException("Error reading file", e);
        }

        FileData data = new FileData();
        data.nodes = new ArrayList<>(nodes.values());
        data.vehicles = vehicles;
        data.shipments = shipments;

        return data;
    }

}
