
package de.uni_marburg.schematch.matching;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import de.uni_marburg.schematch.data.Column;
import de.uni_marburg.schematch.matchtask.columnpair.ColumnPair;

public class ProductMatcherRunner {

    public static void main(String[] args) throws Exception {

        // 1. Product Top 100 데이터 폴더
        String folderPath =
            "C:/Users/user/OneDrive/바탕 화면/product_top_100";

        // 2. 실험 설정
        int maxFiles = 5;       // 처음에는 5개 파일
        int maxRows = 200;      // 파일당 200개 행

        // 3. ProductDataReader 객체 생성
        ProductDataReader reader = new ProductDataReader();

        // 4. 모든 컬럼을 저장할 리스트
        List<Column> allColumns = new ArrayList<>();

        // 5. 폴더에서 JSON.GZ 파일 검색
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
            "Number of files selected: "
            + productFiles.size()
        );

        // 6. 각 Product 파일에서 컬럼 추출
        for (Path file : productFiles) {

            System.out.println();
            System.out.println(
                "Reading file: " + file.getFileName()
            );

            List<Column> columns =
                reader.readColumns(
                    file.toString(),
                    maxRows
                );

            System.out.println(
                "Columns in file: " + columns.size()
            );

            allColumns.addAll(columns);
        }

        // 7. 전체 컬럼 수 확인
        int n = allColumns.size();

        System.out.println();
        System.out.println("=========================");
        System.out.println("Total columns: " + n);

        // 8. 모든 컬럼 쌍 생성
        List<ColumnPair> pairs = new ArrayList<>();

        for (int i = 0; i < n; i++) {

            for (int j = i + 1; j < n; j++) {

                Column c1 = allColumns.get(i);
                Column c2 = allColumns.get(j);

                ColumnPair pair =
                    new ColumnPair(c1, c2);
                	//c1=sourceColumn c2=targetColumn
                pairs.add(pair);
            }
        }

        System.out.println(
            "Total column pairs: " + pairs.size()
        );

        // 9. Jaccard Matcher 생성
        ColumnPairJaccardMatcher matcher =
            new ColumnPairJaccardMatcher();

        // 10. 모든 컬럼 쌍의 유사도 계산
        List<Double> results =
            matcher.matchAll(pairs);

        // 11. 유사도가 0보다 큰 결과 출력
        System.out.println();
        System.out.println(
            "===== Matching Results ====="
        );

        int positiveCount = 0;

        for (int i = 0; i < pairs.size(); i++) {

            ColumnPair pair = pairs.get(i);

            double similarity = results.get(i);

            if (similarity > 0.0) {

                positiveCount++;

                System.out.println(
                    pair.getSourceColumn().getLabel()
                    + " <-> "
                    + pair.getTargetColumn().getLabel()
                    + " : "
                    + similarity
                );
            }
        }

        // 12. 최종 결과 출력
        System.out.println();
        System.out.println("===== Summary =====");

        System.out.println(
            "Files processed: " + productFiles.size()
        );

        System.out.println(
            "Total columns: " + allColumns.size()
        );

        System.out.println(
            "Total compared pairs: " + pairs.size()
        );

        System.out.println(
            "Pairs with similarity > 0: " + positiveCount
        );

        System.out.println("===== Finished =====");
    }
}
