import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Runs a number of algorithms that try to fit files onto disks.
 */
public class Bins {
    public static final String DATA_FILE = "example.txt";

    /**
     * Reads list of integer data from the given input.
     *
     * @param input tied to an input source that contains space separated numbers
     * @return list of the numbers in the order they were read
     */
    public List<Integer> readData (Scanner input) {
        List<Integer> results = new ArrayList<Integer>();
        while (input.hasNext()) {
            results.add(input.nextInt());
        }
        return results;
    }
    
    public static void addToDisk(int size, Disk d, PriorityQueue<Disk> pq){
    	pq.poll();
    	d.add(size);
    	pq.add(d);
    }
    
    public static void addToNewDisk(int size, int diskId, PriorityQueue<Disk> pq){
    	Disk d2 = new Disk(diskId);
        diskId++;
        d2.add(size);
        pq.add(d2);
    }

    public static void output(int total, PriorityQueue<Disk> pq, String method){
    	if (method.equals("in-order")){
    		System.out.println("total size = " + total / 1000000.0 + "GB");
    	}
    	System.out.println();
        System.out.println("worst-fit " + method + " method");
        System.out.println("number of pq used: " + pq.size());
        while (!pq.isEmpty()) {
            System.out.println(pq.poll());
        }
        System.out.println();
    	
    }
    
    public static boolean putInDisk(Disk d, int size, String method){
    	if (method.equals("decreasing")){
    		return (d.freeSpace() >= size);
    	}
    	else
    		return d.freeSpace() > size;
    }
    
    public static void loop(PriorityQueue<Disk> pq, List<Integer> data, String method, int total){
    	int diskId = 1;
    	for (Integer size : data) {
            Disk d = pq.peek();
            if (putInDisk(d, size, method)) {
                addToDisk(size, d, pq);
            } else {
                addToNewDisk(size, diskId, pq);
            }
            if(method.equals("in-order")){
            	total += size;
            	}
        }
    	output(total, pq, method);
    }
    
    /**
     * The main program.
     */
    public static void main (String args[]) {
        Bins b = new Bins();
        Scanner input = new Scanner(Bins.class.getClassLoader().getResourceAsStream(DATA_FILE));
        List<Integer> data = b.readData(input);

        PriorityQueue<Disk> pq = new PriorityQueue<Disk>();
        pq.add(new Disk(0));

        
        int total = 0;
        loop(pq, data, "in-order", total);
        

        Collections.sort(data, Collections.reverseOrder());
        pq.add(new Disk(0));

        loop(pq, data, "decreasing", total);
    }
}
