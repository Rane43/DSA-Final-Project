import java.util.LinkedList;

public class Graph {
	int numberOfNodes;
	
	public LinkedList<Edge>[] adjacencyList;
	
	public Graph(int numberOfNodes) {
		this.numberOfNodes = numberOfNodes;
		
		adjacencyList = new LinkedList[numberOfNodes];
		
		for (int i = 0; i < numberOfNodes; i++) {
			adjacencyList[i] = new LinkedList<Edge>();
		}	
	}
	
	
	public boolean addEdge(int source, int destination, int tripID, double weight) {
		Edge myEdge = new Edge(source, destination, tripID, weight);
		adjacencyList[source].add(myEdge);
		return true;
	}


    // --- COMPLETE ?? ---
    public void displayGraph() {
        int size;
        Edge edgeOfInterest; 

        for (int i = 0; i < numberOfNodes; i++) {
            size = adjacencyList[i].size();
            for (int j = 0; j < size; j++) {
                edgeOfInterest = adjacencyList[i].get(j);
                System.out.println(String.valueOf(i) + " - " + edgeOfInterest.destination + " (" + edgeOfInterest.weight + ")");
            }
        }
    
    }

    // ----------------------------------- //
    // edit to find path to single source ???.
    // can remove stopIndicesArray[] as parameter and just hash it going in.
    public Bundle<int[], int[], double[]> dijkstra(int startNode1, int endNode1, int arrivalTimeInMins, int[] stopIndicesArray) {
        // Need to hash input 
        int startNode = stopIndicesArray[startNode1];

        // why do i need tripIDs?
        int[] tripIDs = new int[adjacencyList.length];
        int[] prevStops = new int[adjacencyList.length];
        double[] minKnownDistances = new double[adjacencyList.length];
        int minKnownDistanceNode;
        boolean[] visitedNodes = new boolean[adjacencyList.length];
        LinkedList<Integer> accessibleNodes = new LinkedList<Integer>();

        // Initialize all minKnownDistances to infinity
        for (int index = 0; index < adjacencyList.length; index++) {
            minKnownDistances[index] = Double.POSITIVE_INFINITY;
            visitedNodes[index] = false;
            prevStops[index] = -1;
        }



        accessibleNodes.add(startNode);
        minKnownDistanceNode = startNode;
        minKnownDistances[startNode] = 0;


        while (!(accessibleNodes.isEmpty())) {
            // Keep track of visited nodes.
            visitedNodes[minKnownDistanceNode] = true;
            // Mark currentNode as visited and remove from priorityQueue
            Object i = minKnownDistanceNode;
            accessibleNodes.remove(i);


            // expand currentNode's edges
            // Recheck- this isnt quite right ***
            for (Edge edge : adjacencyList[minKnownDistanceNode]) {
                if (/* Node not visited */ visitedNodes[edge.destination] == false) {
                    // do distance calculation
                    if (minKnownDistances[edge.source] + edge.weight < minKnownDistances[edge.destination]) {
                        minKnownDistances[edge.destination] = minKnownDistances[edge.source] + edge.weight;
                        // Update prevStop
                        prevStops[edge.destination] = edge.source;
                        tripIDs[edge.destination] = edge.tripID;
                        
                        // add to priority queue
                        accessibleNodes.add(edge.destination);
                    }
                }
            }


            // Check through priority queue to find smallest item (minKnownDistance)
            double minKnownDistance = Double.POSITIVE_INFINITY;
            for (int accessibleNode : accessibleNodes) {
                if (minKnownDistances[accessibleNode] < minKnownDistance) {
                    minKnownDistance = minKnownDistances[accessibleNode];
                    minKnownDistanceNode = accessibleNode;
                }

            }

            
            // do i need this ???
            // Update minKnownDistanceNodeS
            if (minKnownDistance == Double.POSITIVE_INFINITY) {
                Bundle<int[], int[], double[]> bundle = new Bundle<int[], int[], double[]>(prevStops, tripIDs, minKnownDistances);
                return bundle;
            }
            

            
            // Repeat


        }

        // return path instead ???
        Bundle<int[], int[], double[]> bundle = new Bundle<int[], int[], double[]>(prevStops, tripIDs, minKnownDistances);

        return bundle;
    }
    
    // ---------------------------------- //


    // Got to be careful, if no path exists, it will continue to loop forever
    public void printPath(int node, Bundle<int[], int[], double[]> pair, int startNode, int[] stopIndicesArray, int[] stopIDs) {
        int[] prevStops = pair.a;
        double[] minKnownDistances = pair.c;

        int prevNode = stopIndicesArray[node];
        String path = "";

        while (prevNode != stopIndicesArray[startNode]) {
            if (prevNode == -1) {
                System.out.println("No route possible.");
                return;
            }
            // Cost?
            // Have bus icon or smth visual?
            // is this not just going back?
            if (minKnownDistances[prevNode] - minKnownDistances[prevStops[prevNode]] > 1) {
                path = "->" + stopIDs[prevNode] + path;
                prevNode = prevStops[prevNode];
                path = "->(T)" + path;
                continue;
            }

            path = "->" + stopIDs[prevNode] + path;
            prevNode = prevStops[prevNode];

        }
        
        System.out.println(startNode + path + " Time: " + minKnownDistances[stopIndicesArray[node]] + " mins \n");
        
    }
    

}
