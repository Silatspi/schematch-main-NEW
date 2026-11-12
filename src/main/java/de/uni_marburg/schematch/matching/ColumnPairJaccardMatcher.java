
package de.uni_marburg.schematch.matching;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uni_marburg.schematch.data.Column;
import de.uni_marburg.schematch.matchtask.columnpair.ColumnPair;

public class ColumnPairJaccardMatcher {

    //Jaccard rechnen für eine column
    public double match(ColumnPair pair) {

        Column c1 = pair.getSourceColumn();
        Column c2 = pair.getTargetColumn();

        Set<String> val1 = new HashSet<>(c1.getValues());
        Set<String> val2 = new HashSet<>(c2.getValues());

        // vereinungsmenge
        Set<String> union = new HashSet<>(val1);
        union.addAll(val2);

        // schnittmenge
        Set<String> intersection = new HashSet<>(val1);
        intersection.retainAll(val2);

        if (union.isEmpty()) {
            return 0.0;
        }

        return (double) intersection.size() / union.size();
    }

    // vergleichen mit verschiedene Column

    public List<Double> matchAll(List<ColumnPair> pairs) {

    	List<Double> results = new ArrayList<>();

    	for (ColumnPair pair : pairs) {
    		
    		double sim = match(pair);

    		results.add(sim);
    	}

    	return results;
    }

}
