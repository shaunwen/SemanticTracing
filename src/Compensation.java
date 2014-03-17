import java.util.LinkedHashSet;

import be.fnord.util.logic.Accumulate_4ST;
import be.fnord.util.logic.WFF;



/**
 * @author Xiong Wen (xw926@uowmail.edu.au)
 *
 */
public class Compensation {

	/**
	 * 
	 */
	public Compensation() {

		// TODO Auto-generated constructor stub
	}

	/**
	 * Method for calculating Levenshtein Distance(edit distance) Version 1
	 * 
	 * a one string that needed in distance calculation
	 * b the other string that needed in distance calculation
	 * 
	 * @return cost the Levenshtein Distance between the two strings
	 * 
	 * @param args
	 */
	public static int getDistance(String a, String b) {

		a = a.toLowerCase();
		b = b.toLowerCase();
		// i == 0
		int[] costs = new int[b.length() + 1];
		for (int j = 0; j < costs.length; j++)
			costs[j] = j;
		for (int i = 1; i <= a.length(); i++) {
			// j == 0; nw = lev(i - 1, j)
			costs[0] = i;
			int nw = i - 1;
			for (int j = 1; j <= b.length(); j++) {
				int cj =
					Math.min(
						1 + Math.min(costs[j], costs[j - 1]),
						a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
				nw = costs[j];
				costs[j] = cj;
			}
		}
		return costs[b.length()];
	}

	/**
	 * Method for calculating Levenshtein Distance(edit distance) Version 2
	 */
	public static int LevenshteinDistance(String s, String t) {

		// degenerate cases
		if (s == t)
			return 0;
		if (s.length() == 0)
			return t.length();
		if (t.length() == 0)
			return s.length();

		// create two work vectors of integer distances
		int[] v0 = new int[t.length() + 1];
		int[] v1 = new int[t.length() + 1];

		// initialize v0 (the previous row of distances)
		// this row is A[0][i]: edit distance for an empty s
		// the distance is just the number of characters to delete from t
		for (int i = 0; i < v0.length; i++)
			v0[i] = i;

		for (int i = 0; i < s.length(); i++) {
			// calculate v1 (current row distances) from the previous row v0

			// first element of v1 is A[i+1][0]
			// edit distance is delete (i+1) chars from s to match empty t
			v1[0] = i + 1;

			// use formula to fill in the rest of the row
			for (int j = 0; j < t.length(); j++) {
				int cost = (s.charAt(i) == t.charAt(j)) ? 0 : 1;
				// v1[j + 1] = Minimum(v1[j] + 1, v0[j + 1] + 1, v0[j] + cost);
				v1[j + 1] =
					Math.min(v1[j] + 1, Math.min(v0[j + 1] + 1, v0[j] + cost));
			}

			// copy v1 (current row) to v0 (previous row) for next iteration
			for (int j = 0; j < v0.length; j++)
				v0[j] = v1[j];
		}

		return v1[t.length()];
	}

	/**
	 * compute the cost function J(t, et) which is the cost
	 * from a normative trace t to the estimated trace et of a compensation plan
	 * 
	 * @param t
	 *            normative trace
	 * @param et
	 *            estimated trace
	 */
	public static int computeJ(String[] t, String[] et) {

		int cost = 0;
		
		//calculate distance between tasks
		String[] observedTrace = new String[100];  //temporary defined the number of tasks as 100 
		String[][] distanceMatrixForTrace = new String[t.length][observedTrace.length];
		for( int m = 0; m < t.length; m++ ){
			distanceMatrixForTrace[m][0] = t[m];
		}
		
		for( int n = 0; n < t.length; n++ ){
			distanceMatrixForTrace[0][n] = observedTrace[n];
		}
		
		for(int i = 1; i < t.length; i++){
			for(int j = 1; j < observedTrace.length; j++){
				distanceMatrixForTrace[i][j] = Integer.toString( getDistance(distanceMatrixForTrace[i][0], distanceMatrixForTrace[0][j]));
			}		
		}
		
		
		
		return cost;
	}

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		System.out.println("LevenshteinDistance Version 1: \n");
		String[] data =
			{
				"kitten", "sitting", "saturday", "sunday", "rosettacode",
				"raisethysword"
			};
		for (int i = 0; i < data.length; i += 2)
			System.out.println("distance(" + data[i] + ", " + data[i + 1] +
				") = " + getDistance(data[i], data[i + 1]));

		System.out.println("\nLevenshteinDistance Version 2: \n");
		// System.out.println(LevenshteinDistance("kitten", "sitting"));
		for (int i = 0; i < data.length; i += 2)
			System.out.println("distance(" + data[i] + ", " + data[i + 1] +
				") = " + LevenshteinDistance(data[i], data[i + 1]));

		Accumulate_4ST accCheck = new Accumulate_4ST();

		// LinkedHashSet<WFF> effCheckList =
		// accCheck.pairwise_acc(new WFF(
		// vList.get(i - 1).get(x).esWFF), new WFF(
		// vList.get(i).get(j).immWFF), kb, true);
		
		//for the edit distance, we just need to use the method: 

	}

}
