
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;


public class ValidationM {
	public static void main(String args[]) throws Exception{
		
		BufferedReader br = new BufferedReader(new FileReader("C:/Users/evenstar/Desktop/Matlab/cho.txt"));
		HashMap<Integer, ArrayList<Double>> exprValue = new HashMap<Integer, ArrayList<Double>>();
		String line;
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
		
		
		HashMap<Integer, ArrayList<Integer>>clusterAndGeneid  = new HashMap<Integer, ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> clusters = new ArrayList<ArrayList<Integer>>();
		PrintWriter writer = new PrintWriter("C:/Users/evenstar/Desktop/Matlab/mapreduce.txt");
		for(int i=1;i<=5;i++){
			br = new BufferedReader(new FileReader("C:/Users/evenstar/Desktop/Matlab/finalopWithCombiner"+i+".txt"));
			while((line = br.readLine())!=null){
				String columns[] = line.split("\t");
				int clusterno = Integer.parseInt(columns[1]);
				int geneid = Integer.parseInt(columns[0]);
				if(clusterAndGeneid.containsKey(clusterno)){
					ArrayList<Integer>temp = clusterAndGeneid.get(clusterno);
					temp.add(geneid);
				}
				else{
					ArrayList<Integer>temp = new ArrayList<Integer>();
					temp.add(geneid);
					clusterAndGeneid.put(clusterno, temp);
				}

				
				ArrayList<Double> temp = exprValue.get(geneid);
				String dim="";
				for(int j=2;j<temp.size();j++){
					if(dim.equals(""))
						dim=""+temp.get(j);
					else
						dim=dim+" "+temp.get(j);
				}
				
				writer.println(geneid+" "+dim);
				
			}
		}
		
		writer.close();
		
		for (Integer i : clusterAndGeneid.keySet()) {
			clusters.add(clusterAndGeneid.get(i));
		}
		for (ArrayList<Integer> arrayList : clusters) {
			System.out.println(arrayList.size());
		}
		Validation v=new Validation("C:/Users/evenstar/Desktop/Matlab/cho.txt", clusters);
		v.validate();
	}

}