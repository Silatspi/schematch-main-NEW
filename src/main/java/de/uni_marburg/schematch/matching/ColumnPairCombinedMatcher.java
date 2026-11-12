package de.uni_marburg.schematch.matching;
import java.util.ArrayList;
import java.util.List;

import de.uni_marburg.schematch.matchtask.columnpair.ColumnPair;

public class ColumnPairCombinedMatcher {
	private final ColumnPairJaccardMatcher instanceMatcher=new ColumnPairJaccardMatcher();
	private final ColumnPairLabelMatcher labelMatcher =new ColumnPairLabelMatcher();
	private final double weightInstance=0.5;
	private final double weightLabel=0.5;
	public double match(ColumnPair pair) {
        double instanceSim = instanceMatcher.match(pair);
        double labelSim = labelMatcher.match(pair);
        double combinedSim=weightInstance*instanceSim+weightLabel*labelSim;
        return combinedSim;
	 }
	public List<Double> matchAll(List<ColumnPair> pairs){
        List<Double> results = new ArrayList<>();
        for(ColumnPair pair:pairs) {
        	double sim=match(pair);
        	results.add(sim);
        }
        return results;
	}

}
