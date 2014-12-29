import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Main {

	
	public static void main(String[] args) throws Exception {
		
//		Normalization nr = new Normalization("C:/Users/asles_000/Desktop/Project 2/cho.txt");
		
		
		Scanner in = new Scanner(System.in);
		System.out.println("Which algo");
		int algo = Integer.parseInt(in.nextLine());
		
		ArrayList<ArrayList<Integer>> result;
		
		String filename = "C:/Users/evenstar/Desktop/Matlab/iyer_new.txt";
		switch (algo) {
		case 1:
			Kmeans k = new Kmeans(filename);
			result = k.execute();
			for(ArrayList<Integer> a:result){
				System.out.println("size "+a.size());
			}
			Validation v1 = new Validation(filename, result);
			v1.validate();
			break;
		case 2:
			Hierarchical h = new Hierarchical(filename);
			result = h.execute();
			System.out.println("No of clusters is "+result.size());
			Validation v2 = new Validation(filename, result);
			v2.validate();
			break;
		case 3:
			//DBScan db = new DBScan("C:/Users/evenstar/Desktop/Matlab/dataset1.txt");
			DBScan_New db= new DBScan_New(filename);
			result = db.execute();
			System.out.println("number of clusters= "+result.size());
			Validation v3 = new Validation(filename, result);
			v3.validate();
			break;

		default:
			break;
		}
//		//k-means
//		Kmeans k = new Kmeans("C:/Users/evenstar/Desktop/Matlab/iyer_new.txt");
//		result = k.execute();
//		Validation v = new Validation("C:/Users/evenstar/Desktop/Matlab/iyer_new.txt", result);
//		v.validate();
		
		//hierrachical
//		Hierarchical h = new Hierarchical("C:/Users/evenstar/Desktop/Matlab/cho_new.txt");
//		result = h.execute();
//		System.out.println("size is "+result.size());
//		Validation v = new Validation("C:/Users/evenstar/Desktop/Matlab/cho_new.txt", result);
//		v.validate();
		
		//DBscan
//		DBScan db = new DBScan("C:/Users/evenstar/Desktop/Matlab/cho.txt");
//		result = db.execute();
//		System.out.println("number of clusters= "+result.size());
//		Validation v = new Validation("C:/Users/evenstar/Desktop/Matlab/cho.txt", result);
//		v.validate();
//	
	}
	
	}


