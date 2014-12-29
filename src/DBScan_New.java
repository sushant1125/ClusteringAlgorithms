
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class DBScan_New {

	String fileName;

	public DBScan_New(String fileName) {
		this.fileName = fileName;
	}
	public ArrayList<ArrayList<Integer>> execute() throws Exception{
		
		HashMap<Integer, ArrayList<Double>> exprValue = new HashMap<Integer, ArrayList<Double>>();
		ArrayList<Integer> noise = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>>cl = new ArrayList<ArrayList<Integer>>();
		
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the eps value");
		double eps = Double.parseDouble(in.nextLine());
		System.out.println("Enter minpoints");
		int minPts = Integer.parseInt(in.nextLine());
		String line;
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		while((line = br.readLine())!=null)
		{	
			String columns[] = line.split("\t");
			int geneId = Integer.parseInt(columns[0]);
			ArrayList<Double> expressions = new ArrayList<Double>();
			for (int i=2;i<columns.length;i++)
				expressions.add(Double.parseDouble(columns[i]));

			exprValue.put(geneId, expressions);
		}
		br.close();

		double distanceMatrix[][] = new double[exprValue.size()+1][exprValue.size()+1];
		for(int i=1;i<=exprValue.size();i++)
		{
			for(int j=1;j<=exprValue.size();j++)
			{
				ArrayList<Double>list1 = exprValue.get(i);
				ArrayList<Double>list2 = exprValue.get(j);
				if(i!=j)
				{
					double distance = findDistance(list1,list2);
					distanceMatrix[i][j] = distance;
				}
				else
					distanceMatrix[i][j] = Double.MAX_VALUE;

			}
		}

		boolean[] visited = new boolean[exprValue.size()+1];
		
		
		for(int i=1; i<=exprValue.size(); i++){
			if(visited[i]){
				continue;
			}
			ArrayList<Integer> neighbors =findNeighbors(distanceMatrix, i, eps, exprValue.size());
			if(neighbors.size()<minPts){
				noise.add(i);
				continue;
			}
			else{
				
				ArrayList<Integer>c = new ArrayList<Integer>();
				cl.add(expandCluster(i, neighbors, c, eps, minPts, visited, distanceMatrix, exprValue.size(),cl));
				//clusters.add(neighbors);
			}
		}
		//System.out.println("size nois "+noise.size());
		cl.add(noise);
		
		
	
		System.out.println(cl);
		System.out.println(cl.size());
	//	System.out.println("noise " +cl.get(2));
		PrintWriter writer = new PrintWriter("C:/Users/evenstar/Desktop/Matlab/dbscanoutput.txt");
		for (int i=0;i<cl.size();i++) {
			ArrayList<Integer>arr = cl.get(i);
			for (Integer integer : arr) {
				String dim ="";
				ArrayList<Double>d =exprValue.get(integer);
				for (Double double1 : d) {
					if(dim.equals(""))
						dim=""+double1;
					else
						dim=dim+" "+double1;
				}
				writer.println((i+1)+" "+dim);
			}
		}
		writer.close();
		
		return cl;
		
	}
	
	public static ArrayList<Integer> findNeighbors(double[][] dm, int i, double eps, int size){
		ArrayList<Integer> neighbors = new ArrayList<Integer>();

		for(int j=1; j<=size; j++){
			if(dm[i][j]<=eps){
				neighbors.add(j);
			}
		}

		return neighbors;

	}
	
	public static double findDistance(ArrayList<Double>centroid,ArrayList<Double>point)
	{
		double sumOfSquares=0;
		for(int i=0;i<centroid.size();i++)
			sumOfSquares += (centroid.get(i)-point.get(i))*(centroid.get(i)-point.get(i));

		return Math.sqrt(sumOfSquares);
	}
	
	public static ArrayList<Integer> expandCluster(int i, ArrayList<Integer> neighbors, ArrayList<Integer>c, double eps, int minPts, boolean[] visited, double[][] dm, int size,ArrayList<ArrayList<Integer>>cl){
		
		visited[i]=true;
		c.add(i);
		
		for(int j=0;j<neighbors.size();j++){
			int currn = neighbors.get(j);
			if(visited[currn]==false){
				ArrayList<Integer>tempN = findNeighbors(dm, currn, eps,size);
				if(tempN.size()>minPts){
					for (Integer integer : tempN) {
						if(!neighbors.contains(integer))
							neighbors.add(integer);
					}
				//	neighbors.addAll(tempN);
					//neighbors.addAll(tempN);
					//int t=neighbors.size();
//					for (Integer integer : tempN) {
//						neighbors.add(t,integer);
//						t++;
//					}
				}
			}
			if(visited[currn]==false)
			{
				visited[currn]=true;
				if(!InCluster(currn,cl))
					c.add(currn);
			}
		}
		return c;
			

	}
	
	public static boolean InCluster(int curr, ArrayList<ArrayList<Integer>>cl){
		for (ArrayList<Integer> arrayList : cl) {
			if(arrayList.contains(curr))
				return true;
		}
		return false;
		
	}


}
