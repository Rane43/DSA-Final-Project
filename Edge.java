public class Edge implements Comparable<Edge> {
	int source, destination;
    int tripID;
	double weight;
	
	public Edge (int source, int destination, int tripID, double weight) {
        this.tripID = tripID;
		this.source = source;
		this.destination = destination;
		this.weight = weight;
		
	}

    @Override
    // returns smallest distancen if edges come from same node.
    public int compareTo(Edge edge) {
        if (this.source == edge.source) {
            // min(source distance + weights) 
        }
        return -1;
    }
}