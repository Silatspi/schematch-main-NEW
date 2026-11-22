package de.uni_marburg.schematch.matching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni_marburg.schematch.data.Column;


public class ColumnPairSortedNeighborhoodBlocking {
	private final int windowSize;
    public ColumnPairSortedNeighborhoodBlocking(int windowSize) {
    	if(windowSize<2) {
    		throw new IllegalArgumentException("Window size must be at least 2");
    	}
    	this.windowSize=windowSize;
    }
    public Map<Integer, Set<Integer>> generateCandidates(List<Column> columns) {
    	Map<Integer,Set<Integer>>candidates=new HashMap<>();
    	List<Integer>ids=new ArrayList<>();
    	for(int i=0;i<columns.size();i++) {
    		ids.add(i);
    		
    	}
    	ids.sort(Comparator.comparing(i->{
    		String label=columns.get(i).getLabel();
    		if(label==null)return "";
    		return label.trim().toLowerCase();
    	}));
    	for(int i=0;i<ids.size();i++) {
    		for(int j=i+1;j<Math.min(i+windowSize,ids.size());j++) {
    			int ids1=ids.get(i);
    			int ids2=ids.get(j);
    			int a=Math.min(ids1, ids2);
    			int b=Math.max(ids1, ids2);
    			if(!candidates.containsKey(a)) {
    				candidates.put(a,new HashSet<>());
    			}
    			candidates.get(a).add(b);
    		}
    	}
    	return candidates;
    	
    	
    }

}
