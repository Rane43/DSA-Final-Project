import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class pruneData {
    public int[] stopIDArray;
    public int[] stopIndicesArray; // Is it worth adding this? -> It does speed it up but at the expense of so much memory unused?
    // Will use line number-1 as hash function for ease and memory efficiency in other algorithms?

    public pruneData() throws FileNotFoundException {
        stopIndicesArray = new int[12479];
        stopIDArray = stopIDs();
    }

    // Store hash function inverse (mapping index back to value)
    private int[] stopIDs() throws FileNotFoundException {
        int maxStopID = 0;

        int stops[] = new int[8757];
        // Find Bus Stop from text file
        File file = new File("input files/stops.txt");
        Scanner scan = new Scanner(file);

        
        String line;
        int counter = 0;
        int stopID;
        line = scan.nextLine();
        // scanning further lines
        while (scan.hasNextLine()) {
            line = scan.nextLine();
            stopID = Integer.valueOf(line.substring(0, line.indexOf(",")));
            stops[counter] = stopID;
            stopIndicesArray[stopID] = counter;

            if (stops[counter] > maxStopID) {
                maxStopID = stops[counter];
            }
            counter++;
        }
        

        scan.close();

        return stops;
    }


    // Adds Edges (Stop times)
    private boolean addEdgesFromStopTimes(Graph myGraph) throws FileNotFoundException {
        File file = new File("input files/stop_times.txt");
        Scanner scan = new Scanner(file);

        String[] line;
        scan.nextLine();
        
        int prevtripID = 0, prevStopID = 0;
        int thisTripID, thisStopID;

        
        // scanning further lines
        while (scan.hasNextLine()) {
            line = scan.nextLine().split(",");
            thisTripID = Integer.valueOf(line[0]);
            thisStopID = Integer.valueOf(line[3]);

            if (prevtripID == thisTripID) {
                // Add edge
                myGraph.addEdge(stopIndicesArray[prevStopID], stopIndicesArray[thisStopID], thisTripID, 1.0);

            }
            
            prevtripID = thisTripID;
            prevStopID = thisStopID;

        }

        scan.close();

        return true;
    }


    private boolean addEdgesFromTransfers(Graph myGraph) throws FileNotFoundException  {
        File file = new File("input files/transfers.txt");
        Scanner scan = new Scanner(file);

        String[] line;
        scan.nextLine();

        String transferType;
        int source;
        int destination;
        

        // scanning further lines
        while (scan.hasNextLine()) {
            line = scan.nextLine().split(",");

            transferType = line[2].trim();
            source = stopIndicesArray[Integer.valueOf(line[0])];
            destination = stopIndicesArray[Integer.valueOf(line[1])];
            
            // Add edge
            if (transferType.compareTo("0") == 0) {
                myGraph.addEdge(source, destination, 0, 2.0);
            
            } else {
                myGraph.addEdge(source, destination, 0, Double.valueOf(line[3])/100.0);
            }
            
            
        }

        scan.close();

        return true;
    }

    public Boolean fillInGraph(Graph graph) {
        try {
            this.addEdgesFromStopTimes(graph);
            this.addEdgesFromTransfers(graph);
            return false;
        } catch (Exception e) {
            return false;
        }

    }


    // I can integrate this side by side into the above code for proficiency as the transfers go in the same order as stops
    /*
    public int findIndex(int stop) {
        int index;
        for (index = 0; index < stopIDArray.length; index++) {
            if (stopIDArray[index] == stop) {
                break;
            }

        }

        return index;

    }
    */

}
