import com.fasterxml.jackson.databind.JsonNode;

public class InfoGraph {
    private JsonNode adjacencyList;

    public InfoGraph() {
    }

    public InfoGraph(JsonNode adjacencyList) {
        this.adjacencyList = adjacencyList;
    }
    public JsonNode getAdjacencyList() {
        return adjacencyList;
    }
    public void setAdjacencyList(JsonNode adjacencyList) {
        this.adjacencyList = adjacencyList;
    }
}
