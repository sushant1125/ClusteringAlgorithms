import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Kmeans {

	static String fileName; 

	public Kmeans(String string) {
		this.fileName = string;
	}


	public static ArrayList<ArrayList<Integer>> execute() throws IOException {

		ArrayList<ArrayList<Integer>> output = new ArrayList<ArrayList<Integer>>();
		String line;
		HashMap<Integer, ArrayList<Double>> exprValue = new HashMap<Integer, ArrayList<Double>>();
		ArrayList<Integer>clusterNo = new ArrayList<Integer>();

		BufferedReader br = new BufferedReader(new FileReader(fileName));
		while((line = br.readLine())!=null)
		{	
			String columns[] = line.split("\t");
			int geneId = Integer.parseInt(columns[0]);
			ArrayList<Double> expressions = new ArrayList<Double>();
			for (int i=2;i<columns.length;i++)
				expressions.add(Double.parseDouble(columns[i]));
			clusterNo.add(0);
			exprValue.put(geneId, expressions);
		}
		br.close();
		clusterNo.add(0);

		Scanner in = new Scanner(System.in);
		System.out.println("Enter number of clusters:");
		int noOfClusters = Integer.parseInt(in.nextLine());
		int geneIdArray[] = new int[noOfClusters];

		for(int i=0;i<noOfClusters;i++){
			System.out.println("Enter gene id "+(i+1));
			geneIdArray[i] = Integer.parseInt(in.nextLine());

		}

//		System.out.println("Enter iterations:");
//		int iteratons = Integer.parseInt(in.nextLine());

		in.close();
		ArrayList<ArrayList<Double>> allOldCentroids = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> allNewCentroids = new ArrayList<ArrayList<Double>>();

		/*	ArrayList<Double> centroid1 = exprValue.get(1);
		ArrayList<Double> centroid2 = exprValue.get(68);
		ArrayList<Double> centroid3 = exprValue.get(202);
		ArrayList<Double> centroid4 = exprValue.get(277);
		ArrayList<Double> centroid5 = exprValue.get(331);

		allOldCentroids.add(null);
		allOldCentroids.add(centroid1);
		allOldCentroids.add(centroid2);
		allOldCentroids.add(centroid3);
		allOldCentroids.add(centroid4);
		allOldCentroids.add(centroid5);*/

		allOldCentroids.add(null);
		for(int i=0;i<noOfClusters;i++){
			ArrayList<Double> centroid1 = exprValue.get(geneIdArray[i]);
			allOldCentroids.add(centroid1);
		}


		int currentIterations=0;
		//while(currentIterations<iteratons)
		while(true)
		{
			HashMap<Integer, ArrayList<ArrayList<Double>>> clusters = new HashMap<Integer, ArrayList<ArrayList<Double>>>();

			HashMap<Integer, ArrayList<Integer>>clusterAndGeneid  = new HashMap<Integer, ArrayList<Integer>>();

			for(int currGene = 1;currGene<=exprValue.size();currGene++){
				double minDistance = Double.MAX_VALUE;
				int nearestCentroid =0;

				for(int currCentroid=1;currCentroid<allOldCentroids.size();currCentroid++)
				{		
					double distance = findDistance(allOldCentroids.get(currCentroid),exprValue.get(currGene));
					if(distance<minDistance){
						nearestCentroid = currCentroid;
						minDistance=distance;
					}
				}
				//clusterNo.remove(currGene);
				//clusterNo.add(currGene, nearestCentroid);

				if(clusterAndGeneid.containsKey(nearestCentroid)){
					ArrayList<Integer>temp = clusterAndGeneid.get(nearestCentroid);
					temp.add(currGene);
				}
				else{
					ArrayList<Integer>temp = new ArrayList<Integer>();
					temp.add(currGene);
					clusterAndGeneid.put(nearestCentroid, temp);
				}

				if(!clusters.containsKey(nearestCentroid)){
					ArrayList<ArrayList<Double>>temp = new ArrayList<ArrayList<Double>>();
					temp.add(exprValue.get(currGene));
					clusters.put(nearestCentroid,temp);
				}
				else{
					ArrayList<ArrayList<Double>>temp = clusters.get(nearestCentroid);
					temp.add(exprValue.get(currGene));
				}
			}
			///allNewCentroids = findNewCentroids(clusters);
			allNewCentroids = findNewCentroids(clusters);

			boolean flag=true;
			for (int i=1;i<allNewCentroids.size();i++) {
				ArrayList<Double>list1 = allOldCentroids.get(i);
				ArrayList<Double>list2 = allNewCentroids.get(i);
				flag = compareLists(list1,list2);
				if(flag==false)
					break;   //old and new are differentoutput
			}
			if(flag==false)
				allOldCentroids=allNewCentroids;
			else{

				//System.out.println("\n\n\n");

				for (Integer i : clusterAndGeneid.keySet()) {
					System.out.println(i+" "+clusterAndGeneid.get(i).size());

				}

				PrintWriter writer = new PrintWriter("C:/Users/evenstar/Desktop/Matlab/kmeansoutput.txt");
				for (Integer i : clusters.keySet()) {
					ArrayList<ArrayList<Double>> temp = clusters.get(i);
					for (ArrayList<Double> arrayList : temp) {
						String onedim = "";
						for (Double double1 : arrayList) {
							if(onedim.equals(""))
								onedim = ""+double1;
							else
								onedim=onedim+" "+double1;
						}
						writer.println(i+" "+onedim);
						//System.out.println(i+"    "+onedim);
					}

				}





				//	writer.println("The second line");
				writer.close();

				PrintWriter writer1 = new PrintWriter("C:/Users/evenstar/Desktop/Matlab/pie.csv");
				writer1.println("clusterNo"+","+"geneid");
				for (Integer i : clusterAndGeneid.keySet()) {

					ArrayList<Integer>temp = clusterAndGeneid.get(i);
					for (Integer integer : temp) {
						String l="";
						writer1.println(i+","+integer);
					}

					output.add(clusterAndGeneid.get(i));
				}
				writer1.close();
				break;
			}


//			if(currentIterations<iteratons){
//
//
//				//System.out.println("\n\n\n");
//
//				for (Integer i : clusterAndGeneid.keySet()) {
//					System.out.println(i+" "+clusterAndGeneid.get(i).size());
//
//				}
//
//				PrintWriter writer = new PrintWriter("C:/Users/evenstar/Desktop/Matlab/kmeansoutput.txt");
//				for (Integer i : clusters.keySet()) {
//					ArrayList<ArrayList<Double>> temp = clusters.get(i);
//					for (ArrayList<Double> arrayList : temp) {
//						String onedim = "";
//						for (Double double1 : arrayList) {
//							if(onedim.equals(""))
//								onedim = ""+double1;
//							else
//								onedim=onedim+" "+double1;
//						}
//						writer.println(i+" "+onedim);
//						//System.out.println(i+"    "+onedim);
//					}
//
//				}
//
//
//				//	writer.println("The second line");
//				writer.close();
//
//				PrintWriter writer1 = new PrintWriter("C:/Users/evenstar/Desktop/Matlab/pie.csv");
//				writer1.println("clusterNo"+","+"geneid");
//				for (Integer i : clusterAndGeneid.keySet()) {
//
//					ArrayList<Integer>temp = clusterAndGeneid.get(i);
//					for (Integer integer : temp) {
//						String l="";
//						writer1.println(i+","+integer);
//					}
//
//					output.add(clusterAndGeneid.get(i));
//				}
//				writer1.close();
//				break;
//
//
//			}



			//System.out.println();
			currentIterations++;
		}
	//	System.out.println(currentIterations);
		System.out.println("Clusters are ");
		for (ArrayList<Integer> arrayList : output) {
			System.out.println(arrayList);
		}
		
		return output;

		//end main
	}


	public static boolean compareLists(ArrayList<Double>list1,ArrayList<Double>list2)
	{
		for(int i=0;i<list1.size();i++)
		{
			double d1 = list1.get(i);
			double d2 = list2.get(i);
			if(d1!=d2)
			{
				return false;
			}
		}
		return true;
	}


	public static ArrayList<ArrayList<Double>> findNewCentroids(HashMap<Integer, ArrayList<ArrayList<Double>>> clusters)
	{
		ArrayList<ArrayList<Double>>newCentroids = new ArrayList<ArrayList<Double>>();
		newCentroids.add(null);

		int size = clusters.get(1).get(1).size();

		Double temp[] = new Double[size];
		for(int i=0;i<size;i++)
			temp[i]=(double) 0;
		for (int i : clusters.keySet()) {

			for(int t=0;t<size;t++)
				temp[t]=(double) 0;

			ArrayList<ArrayList<Double>>pointsInCluster = clusters.get(i);
			for (ArrayList<Double> arrayList : pointsInCluster) {
				for (int j =0;j<arrayList.size();j++) {
					temp[j] = temp[j]+arrayList.get(j);
				}
			}
			ArrayList<Double>tempList = new ArrayList<Double>();
			for(int t = 0;t<size;t++)
			{
				temp[t]=(double)temp[t]/pointsInCluster.size();
				tempList.add(temp[t]);
			}
			newCentroids.add(tempList);
		}
		//System.out.println(newCentroids);
		return newCentroids;
	}

	public static double findDistance(ArrayList<Double>centroid,ArrayList<Double>point)
	{
		double sumOfSquares=0;
		for(int i=0;i<centroid.size();i++)
			sumOfSquares += (centroid.get(i)-point.get(i))*(centroid.get(i)-point.get(i));

		return Math.sqrt(sumOfSquares);
	}

}