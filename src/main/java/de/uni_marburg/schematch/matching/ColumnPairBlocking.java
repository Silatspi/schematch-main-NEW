package de.uni_marburg.schematch.matching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni_marburg.schematch.data.Column;

// Wir verwenden Inverted Index Blocking

public class ColumnPairBlocking {

    public Map<Integer, Set<Integer>> generateCandidates(
            List<Column> columns) {

        // 1. Inverted Index erstellen
        Map<String, Set<Integer>> invertedIndex = new HashMap<>();

        for (int i = 0; i < columns.size(); i++) {

            Column col = columns.get(i);

            Set<String> tokens = new HashSet<>();

            for (String val : col.getValues()) {

                if (val == null || val.isBlank()) {
                    continue;
                }

                String token = val.trim().toLowerCase();

                tokens.add(token);
            }

            for (String token : tokens) {

                if (!invertedIndex.containsKey(token)) {

                    invertedIndex.put(
                        token,
                        new HashSet<Integer>()
                    );
                }

                invertedIndex.get(token).add(i);
            }
        }

        // 2. ColumnPair Kandidaten erzeugen
        Map<Integer, Set<Integer>> candidateSets = new HashMap<>();

        for (Set<Integer> id : invertedIndex.values()) {

            List<Integer> ids = new ArrayList<>(id);

            for (int i = 0; i < ids.size(); i++) {

                for (int j = i + 1; j < ids.size(); j++) {

                    // Alle Kombinationen herstellen
                    int a = Math.min(ids.get(i), ids.get(j));
                    int b = Math.max(ids.get(i), ids.get(j));

                    // Damit (1,3) und (3,1) gleich behandelt werden
                    if (!candidateSets.containsKey(a)) {

                        candidateSets.put(
                            a,
                            new HashSet<Integer>()
                        );
                    }

                    // Kandidatenpaar speichern
                    candidateSets.get(a).add(b);
                }
            }
        }

        // 3. Ergebnis zurückgeben
        return candidateSets;
    }
}