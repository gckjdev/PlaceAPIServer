package com.orange.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringSimilarityUtil {
	
	private static double countSimilarity(int srcLength, int destLength, int commonLength){
		double sl = (double)srcLength;
		double dl = (double)destLength;
		double cl = (double)commonLength;
		
		double result = 70*cl/Math.min(sl,dl)+30*cl/Math.max(sl,dl);
		return result;
	}
	public static double getSimilarity(String src,String dest){
		int commonLength = getStringLCSValue(src, dest);
		return countSimilarity(src.length(), dest.length(), commonLength);
	}
	

	public static int getStringLCSValue(String src, String dest) {
		if (src == null || dest == null || src.length() < 1
				|| dest.length() < 1)
			return 0;
		int[][] count = new int[src.length()][dest.length()];
		if (src.charAt(0) == dest.charAt(0))
			count[0][0] = 1;
		else
			count[0][0] = 0;
		for (int i = 1; i < src.length(); i++) {
			if (src.charAt(i) == dest.charAt(0))
				count[i][0] = 1;
			else
				count[i][0] = count[i - 1][0];
		}

		for (int i = 1; i < dest.length(); i++) {
			if (dest.charAt(i) == src.charAt(0))
				count[0][i] = 1;
			else
				count[0][i] = count[0][i - 1];
		}
		for (int i = 1; i < src.length(); i++)
			for (int j = 1; j < dest.length(); j++) {
				if (src.charAt(i) == dest.charAt(j))
					count[i][j] = count[i - 1][j - 1] + 1;
				else
					count[i][j] = Math.max(count[i - 1][j], count[i][j - 1]);
			}
		return count[src.length() - 1][dest.length() - 1];
	}

	// if maxCount < 1, the return size is srcs.length;

	public static String[] getSortedLCSArray(String[] srcs, String dest,
			double similarity, int maxCount) {
		if (srcs == null || srcs.length < 1 || dest == null
				|| dest.length() < 1)
			return null;
		List<StringWithLCS> lcsList = new ArrayList<StringWithLCS>();
		for (int i = 0; i < srcs.length; ++i) {
			double stringSimilarity = getSimilarity(srcs[i], dest);
			if (stringSimilarity < similarity)
				continue;
			StringWithLCS temp = new StringWithLCS(srcs[i], stringSimilarity);
			lcsList.add(temp);
		}
		StringWithLCS stringWithLCS[] = new StringWithLCS[lcsList.size()];
		lcsList.toArray(stringWithLCS);
		Arrays.sort(stringWithLCS);
		
		int count;
		if (maxCount < 1)
			count = stringWithLCS.length;
		else
			count = Math.min(stringWithLCS.length, maxCount);

		String sortedStringArray[] = new String[count];
		for (int i = 0; i < count; ++i) {
			sortedStringArray[i] = stringWithLCS[i].string;
		}
		return sortedStringArray;
	}
	
	static class StringWithLCS implements Comparable<Object> {
		public String string;
		public double lcsValue;

		public StringWithLCS(String string, double lcsValue) {
			this.string = string;
			this.lcsValue = lcsValue;
		}

		@Override
		public int compareTo(Object obj) {
			// TODO Auto-generated method stub
			StringWithLCS temp = (StringWithLCS) obj;
			if (this.lcsValue == temp.lcsValue) {
				return 0;
			}
			if (this.lcsValue < temp.lcsValue)
				return 1;
			return -1;
		}

		@Override
		public String toString() {
			return "StringWithLCS [lcsValue=" + lcsValue + ", string=" + string
					+ "]";
		}
	}

}
