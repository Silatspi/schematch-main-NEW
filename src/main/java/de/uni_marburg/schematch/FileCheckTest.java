package de.uni_marburg.schematch;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

public class FileCheckTest {
    public static void main(String[] args) {
        // 이클립스 프로젝트 루트 기준 상대경로
    	File dir = new File("examples/data/product_top_100/product_top_100");

        // 1. 폴더 존재 여부 확인
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("❌ 폴더를 찾을 수 없습니다: " + dir.getAbsolutePath());
            return;
        }

        System.out.println("✅ 폴더 확인 완료: " + dir.getPath());

        // 2. 폴더 내 파일 목록 가져오기 (.gz 또는 일반 파일 모두 탐색)
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            System.err.println("❌ 폴더 안에 파일이 없습니다!");
            return;
        }

        System.out.println(">> 발견된 총 파일 수: " + files.length);

        // 3. 첫 번째 파일의 내용물 직접 읽어보기
        File firstFile = files[0];
        System.out.println("\n-------------------------------------------");
        System.out.println("🔍 테스트로 읽어볼 파일: " + firstFile.getName());
        System.out.println("-------------------------------------------");

        try {
            InputStream inStream = new FileInputStream(firstFile);

            // 파일 이름이 .gz로 끝나면 GZIPInputStream으로 압축 해제하며 읽기
            if (firstFile.getName().endsWith(".gz")) {
                inStream = new GZIPInputStream(inStream);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, StandardCharsets.UTF_8));

            String line;
            int lineCount = 0;

            // 파일 내용 앞 15줄만 콘솔에 찍어보기
            while ((line = reader.readLine()) != null && lineCount < 15) {
                System.out.println(line);
                lineCount++;
            }

            reader.close();
            System.out.println("-------------------------------------------");
            System.out.println("🎉 축하합니다! 파일 내용 " + lineCount + "줄을 정상적으로 읽어왔습니다.");

        } catch (Exception e) {
            System.err.println("❌ 파일 읽는 중 에러 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}