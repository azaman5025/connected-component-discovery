import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class DummyOptimization {
    private static int MAX_VERTEX_COUNT_RELATED_WORK = 500;
    private static Iterator<Map.Entry<String, JsonNode>> outerIter = null;
    Graph<String, DefaultEdge> stringGraph = null;
    int numberOfIteration = 0;
    DummyOptimization() throws IOException{
        try {
            stringGraph= createStringGraph();
            //System.out.println(stringGraph.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Instant start = Instant.now();
        System.out.println(start);
        dummyOptimization(stringGraph);
        Instant end = Instant.now();
        System.out.println(end);

        System.out.println(Duration.between(start, end));
    }

    public void dummyOptimization(Graph<String, DefaultEdge> graph){
        System.out.println("Iter : "+ numberOfIteration++);
        boolean isChanged = true;
        Set<String> cliqueVertex = new HashSet<String>();
        //measure time for the entire execution:
        //selects a clique by greedy method:
        //RoleMerge clique = new GreedyComponentOptimizer(component,true).singleIteration() //for now just use a dummy procedure that selects two vertices
        //select a random edge
        Random rand = new Random();
        Object[] vertexSet = graph.vertexSet().toArray();
        Object[] edgeSet = graph.edgeSet().toArray();
        DefaultEdge someRndEdge = (DefaultEdge) edgeSet [rand.nextInt(vertexSet.length)];
        cliqueVertex.add(graph.getEdgeSource(someRndEdge));
        cliqueVertex.add(graph.getEdgeTarget(someRndEdge));
        if(cliqueVertex.size()> 0){
            isChanged = graph.removeAllVertices(cliqueVertex);//removeClique(graph,cliqueVertex);
        }
        if(!isChanged || cliqueVertex == null)
            return;

        //remove the vertices and all incident edges:   // from the main graph?
        //componentList = rediscover all connected components // from the main graph?
        // call to print the connected component set
        List<Set<String>> sets;
        sets= connectedComponent(graph);
        //System.out.println("no of vertex in each set");

        //for all connected components c in componentList where c.size < MAX_VERTEX_COUNT_RELATED_WORK
        //remove connected component (just ignore it)
        //for all connected components > MAX_VERTEX_COUNT_RELATED_WORK
        //call yourself recursively
        for (int i = 0; i < sets.size(); i++) {
            if(sets.get(i).size()>MAX_VERTEX_COUNT_RELATED_WORK){
                Graph<String,DefaultEdge> subGraph = new AsSubgraph(graph, sets.get(i));
                dummyOptimization(subGraph);
            }
        }


    }

    /*
     * find the connected component subgraph from a given graph G
     * A connected component graph is a set of vertices where every vertex is reachable from any vertex of the set
     * input graph
     * @return sets of connected component subgraph
     * */
    public List<Set<String>> connectedComponent(Graph<String, DefaultEdge> graph){
        List<Set<String>> sets;
        sets = new ConnectivityInspector<String, DefaultEdge>(
                (Graph<String, DefaultEdge>) graph).connectedSets();

        // prints the connected components
        System.out.println("connected components:");
        for (int i = 0; i < sets.size(); i++) {
            System.out.println(sets.get(i));
        }
        System.out.println();
        return sets;
    }
    /**
     * Create a graph based on String objects.
     * @return a graph based on String objects.
     */
    private static Graph<String, DefaultEdge> createStringGraph () throws IOException
    {

        Graph<String, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        ObjectMapper mapper = new ObjectMapper();
        InfoGraph infoGraph = new InfoGraph();
        infoGraph = mapper.readValue(new File("politics.json"), InfoGraph.class);
        ObjectNode outerObjectNode = (ObjectNode) infoGraph.getAdjacencyList() ;
        outerIter = outerObjectNode.fields();
        while (outerIter.hasNext()) {
            Map.Entry<String, JsonNode> entry = outerIter.next();
            //System.out.println("parent: "+ entry.getKey().toString());
            JsonNode innerNodeDiscovery = entry.getValue();
            ObjectNode innerObjectNode = (ObjectNode) innerNodeDiscovery;
            Iterator<Map.Entry<String, JsonNode>> innerIter = innerObjectNode.fields();
            if(!g.containsVertex(entry.getKey())){
                g.addVertex(entry.getKey());
            }
            while (innerIter.hasNext()){
                Map.Entry<String, JsonNode> innerEntry = innerIter.next();
                if(!g.containsVertex(innerEntry.getKey())){
                    g.addVertex(innerEntry.getKey());
                }
                if(!g.containsEdge(entry.getKey(), innerEntry.getKey())){
                    g.addEdge(entry.getKey(), innerEntry.getKey());
                }

                //System.out.println("child: "+innerEntry.getKey().toString());
            }


        }


        return g;
    }

}

