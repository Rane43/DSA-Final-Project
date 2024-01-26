import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;


public class BusCode {
    static Graph myGraph;
    static int[] stopIDs;
    static int[] stopIndices;

    public static void main (String[] args) throws FileNotFoundException {
        pruneData data = new pruneData();
        stopIDs = data.stopIDArray;
        stopIndices = data.stopIndicesArray;

        // Create Graph;
        myGraph = new Graph(stopIDs.length);
        data.fillInGraph(myGraph);

        String userInput;

        System.out.println("\nWelcome user! Please choose what you would like to do: ");
        printMainMenu();
        while (true) {
            userInput = uI(); 
            if (userInput.compareTo("1") == 0) {
                searchForStop();
    
            } else if (userInput.compareTo("2") == 0) {
                findShortestPath();
    
            } else if (userInput.compareTo("3") == 0) {
                arrivalTime();
    
            } else if (isExitCode(userInput)) {
                return;

            } else {
                if (canBeConvertedToInt(userInput)) {
                    System.out.println(userInput + " is not a valid program.");
                    continue;
                }
                
                System.out.println(userInput + " is not a valid exit code. Enter exit to leave the program.");
                continue;
            }

            printMainMenu();

        }

    }


    // 1.
    public static void findShortestPath() throws FileNotFoundException {
        int startBusStop = userBusStop("Please enter current bus stop: ");
        if (startBusStop == -1) {
            return;
        }
        
        int endBusStop = userBusStop("Please enter bus stop you wish to arrive at: ");
        if (endBusStop == -1) {
            return;
        }
        


        // Find stop given userInput and using time if it exists:
        // Using the TST we have- Dont want to read in files twice, should i read in once as stop objects?

        // myGraph.displayGraph();
        System.out.println("--------------------");

        Bundle<int[], int[], double[]> pair = myGraph.dijkstra(startBusStop, endBusStop, 0, stopIndices);
        
        System.out.println("Path:");
        // This might actually need stopIds for hashing back***
        myGraph.printPath(endBusStop, pair, startBusStop, stopIndices, stopIDs);
    }
    
    // 2.
    public static void searchForStop() throws FileNotFoundException {
        // Build with stop ID as key
        TST<Integer> st = new TST<Integer>();
        readInStops(st);

        Console cnsl = System.console();
        String input = cnsl.readLine("What bus would you like to search for? ").toUpperCase().trim();
        if (isExitCode(input)) {
            return;
        }
        if (st.keysWithPrefix(input).isEmpty()) System.out.println("No stops matching your input.");
        
        System.out.println();
    }

    private static void readInStops(TST<Integer> st) throws FileNotFoundException {
        // Read in stops into TST
        File file = new File("input files/stops.txt");
        Scanner scan = new Scanner(file);

        scan.nextLine();

        String[] line;
        String busName;
        while (scan.hasNextLine()) {
            line = scan.nextLine().split(",");
            busName = pushBackKeywords(line[2].trim());

            // add busName to TST
            st.put(busName, Integer.valueOf(line[0]));
        }

        scan.close();
    }

    private static String pushBackKeywords(String busName) {
        String[] line = busName.split("\\s+");
        String word;
        String busNameEdit = "";
        String edits = "";

        for (int i = 0; i < line.length; i++) {
            word = line[i];
            if (word.compareTo("FLAGSTOP") == 0 || word.compareTo("WB") == 0 || word.compareTo("SB") == 0 || word.compareTo("NB") == 0 || word.compareTo("EB") == 0) {
                edits += word + " ";
                continue;
            }

            busNameEdit += word + " ";
        }

        return busNameEdit + edits.trim();

    }

    // 3. -- Not Complete -- (Trip IDs not sorted)
    // Need to add error checking to this mehtod****
    // is there a way to use the graph to search for the bus stops because loading them in again is slow?
    public static void arrivalTime() throws FileNotFoundException {
        Console cnsl = System.console();
        String userInput = cnsl.readLine("Please enter arrival time: ").trim();

        while (!canBeConvertedToTime(userInput)) {
            if (isExitCode(userInput)) {
                return;
            }
            userInput = cnsl.readLine("Sorry, " + userInput + " is not a valid time, please try again: ").trim();
        }

        
        Stop givenStop = new Stop(0, 0, userInput);

        File file = new File("input files/stop_times.txt");
        Scanner scan = new Scanner(file);

        // Save them in a linked list- copy over to array and use merge/quick/insertion sort?
        LinkedList<Stop> trips = new LinkedList<Stop>();
        // Use stop object? ****

        String[] line1;
        scan.nextLine();

        // could create stop objects only for the ones who need to get added to linkedlist to save memory
        while (scan.hasNextLine()) {
            line1 = scan.nextLine().split(",");
            Stop thisStop = new Stop(Integer.valueOf(line1[3].trim()), Integer.valueOf(line1[0].trim()), line1[1].trim());
            
            // is time equal
            if (givenStop.isTimeEqual(thisStop)) {
                // Add to trips
                trips.add(thisStop);
            }


        }

        scan.close();


        // copy to an array
        Stop[] myStops = new Stop[trips.size()];
        int counter = 0;
        for (Stop stop : trips) {
            myStops[counter] = stop;
            counter++;
        }

        // sort array- quick sort because of small array size?
        Arrays.sort(myStops);

        for (int i = 0; i < myStops.length; i++) {
            myStops[i].displayStopInfo();
        }

        System.out.println("");


    }

    // how to integrate this method with error checking method, inputAgrees?
    public static int userBusStop(String message) {
        String userInput;
        Console cnsl = System.console();

        userInput = cnsl.readLine("\n" + message);
        while (!inputAgrees(userInput)) {
            userInput = userInput.trim();
            if (isExitCode(userInput)) {
                return -1;
            }
            // Need APPROPRIATE mesasge here
            if (canBeConvertedToInt(userInput)) {
                userInput = cnsl.readLine("Sorry, the bus stop: " + userInput + " does not exist, please try again. ");
                continue;
            }
            
            userInput = cnsl.readLine("Sorry, " + userInput + " is not a bus stop, please try again. "); 

        }

        return Integer.valueOf(userInput);
        
    }



    
    // 4. Error Checking
    private static boolean isExitCode(String input) {
        if (input.toLowerCase().compareTo("exit") == 0) {
            System.out.println();
            return true;
        }

        return false;
    }

    private static boolean canBeConvertedToInt(String input) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }

    private static boolean canBeConvertedToTime(String input) {
        try {
            Stop stop = new Stop(0, 0, input);
            if (stop.time.isValid()) {
                return true;
            }
            return false;
        }
        catch (Exception e) {
            return false;
        }
    }

    // method to check that imput is correct format-wise
    private static boolean inputAgrees(String input) {
        if (!canBeConvertedToInt(input)) {
            return false;
        }
        // Change this to look in the hash table?
        try {
            if (stopIndices[Integer.valueOf(input)] != 0) {
                return true;
            }
        } catch (IndexOutOfBoundsException i) {
            return false; // it's not a valid input
        }

        return false;
    }

    private static void printMainMenu() {
        // User Interface:
        System.out.println("Main Menu:");
        System.out.println("Search for bus stop (1)");
        System.out.println("Find Shortest Bus route between two stops. (2)");
        System.out.println("Find all stops by arrival time (3)");
        System.out.println("To exit, type \"exit\"");
    }

    public static String uI() {
        Console cnsl = System.console();
        return cnsl.readLine("").trim();
        
    }
}