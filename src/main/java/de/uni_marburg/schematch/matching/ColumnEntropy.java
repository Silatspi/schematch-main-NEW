package de.uni_marburg.schematch.matching;
import java.util.HashMap;
import java.util.Map;

import de.uni_marburg.schematch.data.Column;

public class ColumnEntropy {
	  public double calculate(Column column) {
	        Map<String, Integer> counts = new HashMap<>();
	        int total=0;
	        for(String val:column.getValues()) {
	        	if(val==null||val.isEmpty()) {
	        		continue;
	        	}
	        	String token=val.trim().toLowerCase();
	        	counts.put(token,counts.getOrDefault(token,0)+1);
	        	total++;
	        }
	        if(total==0.0)return 0.0;
	        double entropy=0.0;
	        for(int count:counts.values()) {
	        	double p=(double)count/total;
	        	entropy-=p*(Math.log(p)/Math.log(2));
	        }
	        return entropy;
	  }

}
