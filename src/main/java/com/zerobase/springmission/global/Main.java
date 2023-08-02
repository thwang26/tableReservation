package com.zerobase.springmission.global;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        final String CLIENT_ID = "7w17ix9smt";
        final String CLIENT_SECRET = "A3PdpdiBrFc8boQDKR3CDR3XSfoumaghOy2VB5Hn";
        final String ENDPOINT = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=";

        Scanner scan = new Scanner(System.in);
        System.out.print("주소를 입력하세요 : ");
        String address = URLEncoder.encode(scan.nextLine(), StandardCharsets.UTF_8);

        URL url = new URL(ENDPOINT + address);

        HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
        http.setRequestProperty("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
        http.setRequestProperty("X-NCP-APIGW-API-KEY", CLIENT_SECRET);
        http.setRequestMethod("GET");
        http.connect();

        InputStreamReader in = new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(in);
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }

        System.out.println(sb);
    }
}
