package de.uni_marburg.schematch.matching;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import de.uni_marburg.schematch.data.Column;
import de.uni_marburg.schematch.preprocessing.LabelNormalizer;
public class ColumnPairAdaptiveSortedNeighborhoodBlocking {
	public Map<Integer,Set<Integer>>generateCandidates(List<Column>columns,Map<Integer,Set<Integer>>invertedIndexCandidates){
		Map<Integer,Set<Integer>>candidates=new HashMap<>();
		int n=columns.size();
		int[]cnt=new int[n];
		for(int i:invertedIndexCandidates.keySet()) {
			for(int j:invertedIndexCandidates.get(i)) {
				cnt[i]++;
				cnt[j]++;
			}
		}
		List<Integer>ids=new ArrayList<>();
		String[]suffixKeys=new String[n];
		
		for(int i=0;i<n;i++) {	
			ids.add(i);
			String label=columns.get(i).getLabel();
			if(label==null) {
				label="";
			}
			String rawSuffix=label.substring(label.lastIndexOf(".")+1);
			suffixKeys[i]=String.join("", LabelNormalizer.tokens(rawSuffix,LabelNormalizer.STEM));
		}
		ids.sort((a,b)->{
			int result=suffixKeys[a].compareTo(suffixKeys[b]);
			if(result!=0) {
				return result;
			}
			return Integer.compare(a,b);
		
		});
		for(int i=0;i<ids.size();i++) {
			int ids1=ids.get(i);
			int windowSize;
			if(cnt[ids1]==0) {
				windowSize=20;
			}else if(cnt[ids1]<3) {
				windowSize=10;
			}else {
				windowSize=5;
			}
			for(int j=i+1;j<Math.min(i+windowSize,ids.size());j++) {
				int ids2=ids.get(j);
				int a = Math.min(ids1, ids2);
				int b = Math.max(ids1, ids2);
				if(!candidates.containsKey(a)) {
					candidates.put(a, new HashSet<>());
				}
				candidates.get(a).add(b);
				
				
			}
		}
		return candidates;
		
		
	}
	

}
