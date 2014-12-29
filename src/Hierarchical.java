
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map.Entry;

public class Hierarchical {
	String fileName;


	public Hierarchical(String fileName) {
		this.fileName = fileName;
	}

	public ArrayList<ArrayList<Integer>> execute() throws NumberFormatException, IOException {
		String line;
		HashMap<Integer, ArrayList<Double>> exprValue = new HashMap<Integer, ArrayList<Double>>();

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


		Scanner in = new Scanner(System.in);
		System.out.println("Enter number of clusters:");
		int noOfClusters = Integer.parseInt(in.nextLine());

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


		int count = 386;
		int cnt = 1;
		HashMap<Integer, ArrayList<Integer>> mergeList = new HashMap<Integer,ArrayList<Integer>>();

		//initialize mergelist
		for(int i=0; i<exprValue.size(); i++){
			mergeList.put(i+1, new ArrayList<Integer>());
		}

		while(mergeList.size()>noOfClusters){
			double min = Double.MAX_VALUE;
			int cluster1 = 0,cluster2 = 0;


			for(int i=1;i<=exprValue.size();i++)
			{
				for(int j=i+1;j<=exprValue.size();j++)
				{
					if(distanceMatrix[i][j]<min)
					{
						min = distanceMatrix[i][j];
						if(min==0.0)
							System.out.println();
						cluster1 = i;
						cluster2=j;
					}
				}

			}




			//		System.out.println(min);
			//		System.out.println(cluster1+" "+cluster2);
			//		
			//
			//		
			//		System.out.println("before "+cluster1+" "+distanceMatrix[cluster1][1]);
			//		System.out.println("before "+cluster2+" "+distanceMatrix[cluster2][1]);

			update(distanceMatrix,cluster1,cluster2,exprValue, mergeList);

			count--;
			//		System.out.println("after"+cluster1+" "+distanceMatrix[cluster1][1]);
			//		System.out.println("after "+cluster2+" "+distanceMatrix[cluster2][1]);


			//		for(int i=1;i<=exprValue.size();i++)
			//		{
			//			for(int j=1;j<=exprValue.size();j++)
			//			{
			//				System.out.print(distanceMatrix[i][j]+"\t");
			//			}
			//			System.out.println();
			//		}
			//		





			//System.out.println("COUNT----->"+count);
		}
		//System.out.println(mergeList);

		ArrayList<ArrayList<Integer>> output = new ArrayList<ArrayList<Integer>>();
		for(Entry<Integer, ArrayList<Integer>> e:mergeList.entrySet()){
			if(e.getValue().size()==0){
				 e.getValue().add(e.getKey());
			}
			output.add(e.getValue());
		}


		//		PrintWriter writer = new PrintWriter("C:/Users/evenstar/Desktop/Matlab/file-name.txt");
		//		for (Integer i : clusters.keySet()) {
		//			ArrayList<ArrayList<Double>> temp = clusters.get(i);
		//			for (ArrayList<Double> arrayList : temp) {
		//				String onedim = "";
		//				for (Double double1 : arrayList) {
		//					if(onedim.equals(""))
		//						onedim = ""+double1;
		//					else
		//						onedim=onedim+" "+double1;
		//				}
		//				writer.println(i+" "+onedim);
		//				//System.out.println(i+"    "+onedim);
		//			}
		//			
		//		}
		//		
		//	//	writer.println("The second line");
		//		writer.close();
		//		

		PrintWriter writer = new PrintWriter("C:/Users/evenstar/Desktop/Matlab/hierarchicaloutput.txt");
		for (int i=0;i<output.size();i++) {
			ArrayList<Integer>arr = output.get(i);
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
		
		
		return output;










	}

	public static double findDistance(ArrayList<Double>centroid,ArrayList<Double>point)
	{
		double sumOfSquares=0;
		for(int i=0;i<centroid.size();i++)
			sumOfSquares += (centroid.get(i)-point.get(i))*(centroid.get(i)-point.get(i));

		return Math.sqrt(sumOfSquares);
	}
	public static void update(double dm[][],int c1,int c2, HashMap<Integer,ArrayList<Double>> expValue, HashMap<Integer, ArrayList<Integer>> mergeList)
	{

		ArrayList<Integer> temp1;
		ArrayList<Integer> temp2;

		if(c1<c2)
		{
			for(int i=1;i<=expValue.size();i++)
			{
				if(dm[c1][i]>dm[c2][i])
				{	
					if(c1!=i){
						dm[c1][i]=dm[c2][i];
						dm[i][c1]= dm [c2][i];
					}
				}
				dm[i][c2]=  Double.MAX_VALUE;
				dm[c2][i]=  Double.MAX_VALUE;

			}

			temp2 = mergeList.get(c2);
			temp1 = mergeList.get(c1);
			if(temp1.isEmpty()){
				temp1.add(c1);
			}
			if(temp2.isEmpty()){
				temp2.add(c2);
			}
			System.out.println(mergeList.get(c1)+"<-----"+ mergeList.get(c2));
			temp1.addAll(temp2);
			mergeList.remove(c2);

			//old
			//			if(mergeList.containsKey(c2)){
			//				temp = mergeList.get(c2);
			//				if(mergeList.containsKey(c1)){
			//					ArrayList<Integer> list = mergeList.get(c1);
			//					list.addAll(temp);
			//				}
			//				else{
			//					temp.add(c1);
			//					mergeList.put(c1, temp);
			//				}
			//				mergeList.remove(c2);
			//			}
			//			else{
			//				if(mergeList.containsKey(c1)){
			//					ArrayList<Integer> list = mergeList.get(c1);
			//					list.add(c2);
			//				}
			//				else{
			//					ArrayList<Integer> list = new ArrayList<Integer>();
			//					list.add(c2);
			//					list.add(c1);
			//					mergeList.put(c1, list);
			//				}
			//			}
		}else if(c2<c1){
			for(int i=1;i<=expValue.size();i++)
			{
				if(dm[c1][i]>dm[c2][i])
				{	
					if(c1!=i){
						dm[c2][i]=dm[c1][i];
						dm[i][c2]= dm [c1][i]; 
					}
				}
				dm[i][c1]=  Double.MAX_VALUE;
				dm[c1][i]=  Double.MAX_VALUE;
			}
			temp2 = mergeList.get(c2);
			temp1 = mergeList.get(c1);
			if(temp1.isEmpty()){
				temp1.add(c1);
			}
			if(temp2.isEmpty()){
				temp2.add(c2);
			}
			temp2.addAll(temp1);
			mergeList.remove(c1);



			//old
			//			if(mergeList.containsKey(c1)){
			//				temp = mergeList.get(c1);
			//				if(mergeList.containsKey(c2)){
			//					ArrayList<Integer> list = mergeList.get(c2);
			//					list.addAll(temp);
			//				}
			//				else{
			//					mergeList.put(c2, temp);
			//				}
			//				mergeList.remove(c1);
			//			}
			//			else{
			//				if(mergeList.containsKey(c2)){
			//					ArrayList<Integer> list = mergeList.get(c2);
			//					list.add(c1);
			//				}
			//				else{
			//					ArrayList<Integer> list = new ArrayList<Integer>();
			//					list.add(c1);
			//					list.add(c2);
			//					mergeList.put(c2, list);
			//				}
			//		}
		}
	}

}

