import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Validation {

	String fileName;
	ArrayList<ArrayList<Integer>> result;
	
	public Validation(String string, ArrayList<ArrayList<Integer>> result) {
	this.fileName = string;
	this.result = result;
	}

	public void validate() throws Exception{
		HashMap<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();
		String line;
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		ArrayList<Integer> temp;
		HashMap<Integer, ArrayList<Double>> exprValue = new HashMap<Integer, ArrayList<Double>>();
		while((line = br.readLine())!=null)
		{	
			String columns[] = line.split("\t");
			int geneId = Integer.parseInt(columns[0]);
			int cluster = Integer.parseInt(columns[1]);
			ArrayList<Double> expressions = new ArrayList<Double>();
			for (int i=2;i<columns.length;i++)
				expressions.add(Double.parseDouble(columns[i]));
			if(map.containsKey(cluster)){
				temp = map.get(cluster);
				temp.add(Integer.parseInt(columns[0]));
			}
			else{
				temp = new ArrayList<Integer>();
				temp.add(Integer.parseInt(columns[0]));
				map.put(cluster, temp);
			}
			exprValue.put(geneId, expressions);
		}
		br.close();
		
		
		int[][] groundTruth = new int[exprValue.size()+1][exprValue.size()+1];
		int[][] ourTruth = new int[exprValue.size()+1][exprValue.size()+1];
		
		//construct ground truth
		for(Entry<Integer, ArrayList<Integer>> e: map.entrySet()){
			temp = e.getValue();
			for(int i=0; i<temp.size(); i++){
				for(int j=0; j<temp.size(); j++){
					groundTruth[temp.get(i)][temp.get(j)] = 1;
				}
			}
		}
		
		//construct ourTruth
		for(ArrayList<Integer> ar : result){
			for(int i=0; i<ar.size(); i++){
				for(int j=0; j<ar.size(); j++){
					ourTruth[ar.get(i)][ar.get(j)] = 1;
				}
			}
		}
		
		//construct distance matrix
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
					distanceMatrix[i][j] = 0.0;

			}
		}
		
		
		double jacc = Jaccard(groundTruth, ourTruth);
		double cor = corr(distanceMatrix, ourTruth);
		
		System.out.println("Jaccard coeff is "+ jacc);
		System.out.println("correlation coeff is "+ cor);
		
	}
	
	public double Jaccard(int[][]gnd, int[][]our){
		
		//start
		double jacc =0.0;
		double same=0;
		double diff=0;
		for(int i=1;i<gnd.length;i++){
			for(int j=1;j<gnd[0].length;j++){
				if(gnd[i][j]!=our[i][j]){
					diff++;
				}else if((gnd[i][j]==1) &&(our[i][j]==1)){
					same++;
				}
			}
		}
		jacc = same/(same+diff);
		return jacc;
	}
	
	public double corr(double[][]dm, int[][]c){
		double cor=0.0;
		double num=0.0;
		double den=0.0;
		double meanD = 0.0, meanC=0.0;
		//calculate means
		for(int i=1;i<dm.length;i++){
			for(int j=1;j<dm[0].length;j++){
				meanD+=dm[i][j];
			}
		}
		meanD = meanD/(double)(dm.length*dm.length);
		
		for(int i=1;i<c.length;i++){
			for(int j=1;j<c[0].length;j++){
				meanC+=c[i][j];
			}
		}
		meanC = meanC/(double)(c.length*c.length);
		
		//cal correlation
		double den1 = 0.0;
		double den2 = 0.0;
		for(int i=1;i<c.length;i++){
			for(int j=1;j<c[0].length;j++){
				num+= (dm[i][j]-meanD)*(c[i][j]-meanC);
				den1+= (dm[i][j]-meanD)*(dm[i][j]-meanD);
				den2+= (c[i][j]-meanC)*(c[i][j]-meanC);
			}
		}
		cor = num/(Math.sqrt((den1*den2)));
//		System.out.println("meanD---   "+meanD);
//		System.out.println("meanC---   "+meanC);
//		System.out.println("num---   "+num);
//		System.out.println("den1---   "+den1);
//		System.out.println("den2---   "+den2);

		return cor;
	}
	
	public static double findDistance(ArrayList<Double>centroid,ArrayList<Double>point)
	{
		double sumOfSquares=0;
		for(int i=0;i<centroid.size();i++)
			sumOfSquares += (centroid.get(i)-point.get(i))*(centroid.get(i)-point.get(i));

		return Math.sqrt(sumOfSquares);
	}
	
}
