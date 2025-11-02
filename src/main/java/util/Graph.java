package main.java.util;

import com.google.gson.Gson; 
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;


public class Graph {
    private Map<Integer, Node> nodes; 
    private int sourceId;
    private Metrics metrics;

    public Graph() {
        this.nodes = new HashMap<>();
        this.metrics = new Metrics();
    }
    
    private static class GraphJson {
        int n;
        String weight_model;
        int source;
        NodeJson[] nodes;
        EdgeJson[] edges;
    }
    
    private static class NodeJson {
        int id;
        int duration;
    }
    

    private static class EdgeJson {
        int u;
        int v;
    }

    
      @param filePath 
      @return 
      @throws IOException 
     
    public static Graph loadFromJson(String filePath) throws IOException {
        Gson gson = new Gson();
        Graph graph = new Graph();
        
        try (FileReader reader = new FileReader(filePath)) {
            GraphJson data = gson.fromJson(reader, GraphJson.class);
            graph.sourceId = data.source;

            for (NodeJson nodeData : data.nodes) {
                graph.nodes.put(nodeData.id, new Node(nodeData.id, nodeData.duration));
            }

            
            for (EdgeJson edge : data.edges) {
                Node u = graph.nodes.get(edge.u);
                Node v = graph.nodes.get(edge.v);
                if (u != null && v != null) {
                    u.neighbors.add(v);
                }
            }
        } catch (Exception e) {
             System.err.println("Error loading graph from JSON: " + e.getMessage());
             throw new IOException("Failed to load graph data.", e);
        }
        return graph;
    }

    
    public Node getNode(int id) {
        return nodes.get(id);
    }
    
    public Collection<Node> getAllNodes() {
        return nodes.values();
    }

    public int getSourceId() {
        return sourceId;
    }

    public Metrics getMetrics() {
        return metrics;
    }
    

    public void resetNodesForNextAlgo() {
        for (Node node : nodes.values()) {
            node.resetAlgoFields();
        }
    }
}