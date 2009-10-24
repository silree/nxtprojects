package org.programus.nxj.util;

public class StringComparator implements Comparator<String> {

	@Override
	public int compare(String o1, String o2) {
		if (o1 == o2) {
			return 0; 
		}
		if (o1 == null) {
			return -1; 
		}
		if (o2 == null) {
			return 1; 
		}
		
		char[] cs1 = o1.toCharArray(); 
		char[] cs2 = o2.toCharArray(); 
		int n = Math.min(cs1.length, cs2.length); 
		for (int i = 0; i < n; i++) {
			if (cs1[i] != cs2[i]) {
				return cs1[i] > cs2[i] ? 1 : -1; 
			}
		}
		if (cs1.length == cs2.length) {
			return 0; 
		} else {
			return cs1.length > cs2.length ? 1 : -1; 
		}
	}

}
