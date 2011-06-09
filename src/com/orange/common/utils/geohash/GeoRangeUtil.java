package com.orange.common.utils.geohash;

import java.util.ArrayList;
import java.util.List;

public class GeoRangeUtil {

	/**
	 * 
	 * @param geohash
	 * @return
	 */
	List<GeoRange> getGeoRange(List<String> geohashList) {
		List<GeoRange> result = new ArrayList<GeoRange>();
		int size = geohashList.size();
		for (int i = 0; i < size; i++) {
			String start = geohashList.get(i);
			while (i + 1 < size
					&& canMerged(geohashList.get(i), geohashList.get(i + 1))) {
				i++;
			}
			String end = geohashList.get(i);

			GeoRange range = new GeoRange();
			range.setMax(end);
			range.setMin(start);
			result.add(range);
		}
		return result;
	}

	boolean canMerged(String smaller, String bigger) {
		String smallerPrefix = smaller.substring(0, smaller.length() - 1);
		String biggerPrefix = bigger.substring(0, bigger.length() - 1);
		if (!smallerPrefix.equals(biggerPrefix)) {
			// can't merge, they're different from prefix.
			return false;
		}
		int diff = getLastChar(bigger) - getLastChar(smaller);
		return diff <= 1;
	}

	int getLastChar(String geohash) {
		char c = geohash.charAt(geohash.length() - 1);
		return GeoHashUtil._decodemap.get(c);
	}
}
