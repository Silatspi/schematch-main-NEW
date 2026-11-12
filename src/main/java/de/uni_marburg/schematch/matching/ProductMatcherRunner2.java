
package de.uni_marburg.schematch.matching;

import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import de.uni_marburg.schematch.data.Column;
import de.uni_marburg.schematch.matchtask.columnpair.ColumnPair;

public class ProductMatcherRunner2 {

    public static void main(String[] args) throws Exception {

        // 1. 실제 Product 데이터 폴더
        String folderPath =
            "C:/Users/user/OneDrive/바탕 화면/product_top_100";

        // 2. 실험 설정
        int maxFiles = 5;
        int maxRows = 200;

        // 3. Product 데이터 읽기
        ProductDataReader reader = new ProductDataReader();

        List<Column> allColumns = new ArrayList<>();

        List<Path> productFiles;

        try (Stream<Path> files =
                Files.list(Path.of(folderPath))) {

            productFiles = files
                .filter(Files::isRegularFile)
                .filter(p -> p.getFileName()
                    .toString()
                    .endsWith(".json.gz"))
                .sorted()
                .limit(maxFiles)
                .toList();
        }

        System.out.println(
            "Number of files selected: " + productFiles.size()
        );

        // 4. 각 파일의 컬럼 추출
        for (Path file : productFiles) {

            System.out.println(
                "Reading file: " + file.getFileName()
            );

            List<Column> columns =
                reader.readColumns(file.toString(), maxRows);

            System.out.println(
                "Columns in file: " + columns.size()
            );

            allColumns.addAll(columns);
        }

        // 5. 모든 컬럼 쌍 생성
        List<ColumnPair> pairs = new ArrayList<>();

        int n = allColumns.size();

        for (int i = 0; i < n; i++) {

            for (int j = i + 1; j < n; j++) {

                Column c1 = allColumns.get(i);
                Column c2 = allColumns.get(j);

                pairs.add(new ColumnPair(c1, c2));
            }
        }

        System.out.println();
        System.out.println("Total columns: " + n);

        System.out.println(
            "Total column pairs: " + pairs.size()
        );

        // 6. 세 Matcher 생성
        ColumnPairJaccardMatcher instanceMatcher =
            new ColumnPairJaccardMatcher();

        ColumnPairLabelMatcher labelMatcher =
            new ColumnPairLabelMatcher();

        ColumnPairCombinedMatcher combinedMatcher =
            new ColumnPairCombinedMatcher();

        // 7. 각각의 유사도 계산
        List<Double> instanceResults =
            instanceMatcher.matchAll(pairs);

        List<Double> labelResults =
            labelMatcher.matchAll(pairs);

        List<Double> combinedResults =
            combinedMatcher.matchAll(pairs);

        // 8. 계산 결과 출력
        System.out.println();
        System.out.println(
            "===== Matching Results ====="
        );

        for (int i = 0; i < pairs.size(); i++) {

            ColumnPair pair = pairs.get(i);

            double instanceSim = instanceResults.get(i);
            double labelSim = labelResults.get(i);
            double combinedSim = combinedResults.get(i);

            // 적어도 하나의 유사도가 0보다 큰 경우 출력
            if (instanceSim > 0 || labelSim > 0) {

                System.out.printf(
                    "%s <-> %s | Instance: %.4f | Label: %.4f | Combined: %.4f%n",
                    pair.getSourceColumn().getLabel(),
                    pair.getTargetColumn().getLabel(),
                    instanceSim,
                    labelSim,
                    combinedSim
                );
            }
        }

        System.out.println();
        System.out.println("===== Finished =====");
    }
}
