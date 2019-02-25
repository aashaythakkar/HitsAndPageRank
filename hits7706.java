//Aashay Thakkar CS610 7706 prp


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.text.DecimalFormat;


public class hits7706 {

	private static List<ArrayList<Integer>> adjacent;
	private static int numberNode;
	private static int numberEdge;
	private static int iterations;
	private static double[] auth_old;
	private static double[] hub_old;
	private static double[] auth;
	private static double[] hub;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length < 3) {
			System.out.println("Enter a valid command line argument.");
			return;
		}
		
		String filename = args[2];
		int initialValue = Integer.parseInt(args[1]);
		iterations =  Integer.parseInt(args[0]);
		
		String input = "";	//input
		try {
			input = readFile7706(filename);
		} catch (IOException e) {
			System.out.println("Could not read the file!");
			e.printStackTrace();
			return;
		}
		
		
		String[] rows = input.split("\n");	//Divide input into nodes
		
		if (rows.length < 2) {
			System.out.println("Invalid input");
			return;
		}

		try {
			
			String []row = rows[0].split(" ");
			
			if (row.length != 2) {
				throw new Exception("Invalid Input in row:"+row);
			}
			
			int[] result = new int[2];
			result[0] = Integer.parseInt(row[0]);
			result[1] = Integer.parseInt(row[1]);
			int[] header = result;
			
		
			numberNode = header[0]; //number of nodes and edges
			numberEdge = header[1];
			
			if (numberEdge < rows.length -1) {
				System.out.println("Inconsistent data!");
				return;
			}
			
			if (numberNode > 10) {
				iterations = 0;
				initialValue = -1;
			}
			
			
			initializeVariables7706(initialValue);	//initialize 
			createadjacent7706(rows);	//Create adjacent
			
			int count =0;
			print_Auth_Hub7706(count);
			
			
			do {	//Converge values
				calculateAuth7706();
				calculateHub7706();
				scale7706();
				++count;
				if (numberNode <= 10) {		//Print the values
					print_Auth_Hub7706(count);
				}
				
			} while (!didConverge7706(count));
			
			if (numberNode > 10) {
				print_for_higher_nodes7706(count);
			}
			
		} catch (Exception e) {
			
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		

	}
	
	/**
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static String readFile7706(String filePath) throws IOException {
		 Path path = Paths.get(filePath);
		 List<String> inputFileList =  Files.readAllLines(path, StandardCharsets.US_ASCII);
		 String inputString = "";
		 for (String string : inputFileList) {
			 inputString = inputString.concat(string) + "\n";
		}
		 		 
		 return inputString;
	}
	
	public static void initializeVariables7706(int initialValue) {
	
		adjacent = new ArrayList<ArrayList<Integer>>(numberNode);
		auth = new double[numberNode];
		hub = new double[numberNode];
		auth_old = new double[numberNode];
		hub_old = new double[numberNode];
		
		
		double init = 0;
		switch (initialValue) {
		case 0:
		case 1:
			init = initialValue;
			break;
		
		case -1:
			init = 1 / (double)numberNode;
			break;
		
		case -2:
			init = 1 / Math.sqrt(numberNode);
			break;
			
		default:
			init = initialValue;
			break;
		}
		
		for (int i = 0; i < numberNode; i++) {
			adjacent.add(new ArrayList<>());
			hub[i] = init;
			auth[i] = init;
		}
	}
	
	/**
	 * 
	 * @param rows
	 * @throws Exception 
	 */
	public static void createadjacent7706(String[] rows) throws Exception { 	//Create adjacent list from input.
		int[] row;
		int node1, node2;
		for (int index = 1; index <= numberEdge; index++) {
			
			String []row1 = rows[index].split(" ");
			
			if (row1.length != 2) {
				throw new Exception("Invalid Input found in row:"+row1);
			}
			
			int[] result = new int[2];
			result[0] = Integer.parseInt(row1[0]);
			result[1] = Integer.parseInt(row1[1]);
			row = result;
			node1 = row[0];
			node2 = row[1];
			
			if (node1 >= numberNode || node2 >= numberNode) {
				System.out.println("Invalid input in rows 2: "+node1+" "+node2+ " "+ adjacent.size());
				return;
			}
			adjacent.get(node1).add(node2);		
		}
	}
	
	
	public static void printadjacent7706() {	//Print the adjacent list.
		for (List<Integer> list : adjacent) {
			for (Integer integer : list) {
				System.out.print(integer + " ");
			}
			System.out.println();
		}
	}
	

	public static void calculateAuth7706() {	//Calculate the Authority values
		double output;
		for (int i = 0; i < auth.length; i++) {
			auth_old[i] = auth[i];
			output = 0;
			for (int j = 0; j < hub.length; j++) {
				if (adjacent.get(j).contains(i)) {
					output += hub[j];
				}
			}
			auth[i] = output;
		}
	}
	

	public static void calculateHub7706() {		//Calculate the Hub values
		
		double output;
		for (int i = 0; i < hub.length; i++) {
			hub_old[i] = hub[i];
			output = 0;
			for (int j = 0; j < auth.length; j++) {
				if (adjacent.get(i).contains(j)) {
					output += auth[j];
				}
			}
			hub[i] = output;
		}
	}
	
	

	public static void scale7706() {		//Scale the calculated Values
		
		scaleVal7706(auth);
		
		scaleVal7706(hub);
	}
	
	/**
	 * @param array
	 */
	public static void scaleVal7706(double[] array) {		//Scale array by dividing value with square-root of sum of all squares .
		double factor = 0;
		
		for (int i = 0; i < array.length; i++) {
			factor += Math.pow(array[i], 2);
		}
		factor = Math.sqrt(factor);
		for (int i = 0; i < array.length; i++) {
			array[i]/= factor;
		}
	}
	
	/**
	 * Check if the values of authority matrix converged
	 * @return True if the conversion is successful
	 */
	public static boolean didConverge7706(int current_iter) {
		double multiplyFactor = 0;
		if (iterations > 0) {
			return current_iter == iterations;
		}
		else {
			if (iterations == 0) {
				multiplyFactor = 100000;
			}
			else  {
				multiplyFactor = Math.pow(10, (iterations * -1));
			}

			for (int i = 0; i < auth.length; i++) {
				if ((int)Math.floor(auth[i]*multiplyFactor) != (int)Math.floor(auth_old[i]*multiplyFactor)) {
					return false;
				}
			}
			for (int i = 0; i < hub.length; i++) {
				if ((int)Math.floor(hub[i]*multiplyFactor) != (int)Math.floor(hub_old[i]*multiplyFactor)) {
					return false;
				}
			}
			return true;
		}
	}
	

	public static void print_Auth_Hub7706(int iter) {	//Print the Authority and Hub Values
		
		if (iter == 0) {
			System.out.print("Base : "+ iter + " : ");
		}
		else {
			System.out.print("Iterat : "+ iter + " : ");
		}
		
		DecimalFormat numberFormat = new DecimalFormat("0.0000000");
		
		for (int i = 0; i < auth.length; i++) {
			 System.out.print("A/H["+ i + "] = "+numberFormat.format(Math.floor(auth[i] * 1e7)/1e7) + " / " + numberFormat.format(Math.floor(hub[i] * 1e7)/1e7) + " ");
		}
		System.out.println();
	}
	
	public static void print_for_higher_nodes7706(int iter) {
		
		System.out.println("Iterat : "+ iter + " : ");
		DecimalFormat numberFormat = new DecimalFormat("0.0000000");
		
		for (int i = 0; i < auth.length; i++) {
			 System.out.println("A/H["+ i + "] = "+numberFormat.format(Math.floor(auth[i] * 1e7)/1e7) + " / " + numberFormat.format(Math.floor(hub[i] * 1e7)/1e7) + " ");
		}
		System.out.println();
	}
}
