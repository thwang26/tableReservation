package com.zerobase.springmission.store.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zerobase.springmission.global.exception.ApiException;
import com.zerobase.springmission.store.dto.Coordinate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.zerobase.springmission.global.exception.ErrorCode.RESULT_NOT_FOUND;
import static com.zerobase.springmission.global.exception.ErrorCode.WRONG_RESPONSE;

/**
 * 도로명주소 -> 위도, 경도값 변환 시 사용되는 네이버 geocoding api
 * 네이버 api key 값들은 보안을 위해 ignore 처리 하였습니다.
 */
@Service
@RequiredArgsConstructor
public class GeocodingService {

    @Value("${naver.api.id}")
    private String CLIENT_ID;
    @Value("${naver.api.secret}")
    private String CLIENT_SECRET;
    @Value("${naver.api.endpoint}")
    private String ENDPOINT;

    /**
     * 네이버 geocoding 요청하여 도로명주소를 위도, 경도값으로 return
     */
    public Coordinate getCoordinate(String address) throws IOException {
        address = URLEncoder.encode(address, StandardCharsets.UTF_8);

        URL url = new URL(ENDPOINT + address);

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
        conn.setRequestProperty("X-NCP-APIGW-API-KEY", CLIENT_SECRET);
        conn.setRequestMethod("GET");
        conn.connect();

        checkResponseCode(conn);

        BufferedReader rd;
        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        JsonObject jsonObject = (JsonObject) JsonParser.parseReader(rd);
        rd.close();
        conn.disconnect();

        checkResultCount(jsonObject);

        JsonArray addressArray = (JsonArray) jsonObject.get("addresses");
        JsonObject addressObject = addressArray.get(0).getAsJsonObject();

        return Coordinate.builder()
                .lnt(addressObject.get("x").getAsDouble())
                .lat(addressObject.get("y").getAsDouble())
                .build();
    }

    /**
     * 잘못된 요청시 error
     */
    private void checkResponseCode(HttpsURLConnection conn) throws IOException {
        if (conn.getResponseCode() != 200) {
            throw new ApiException(WRONG_RESPONSE);
        }
    }

    /**
     * 검색결과가 없다면 error
     */
    private void checkResultCount(JsonObject jsonObject) {
        int count = ((JsonObject) jsonObject.get("meta")).get("totalCount").getAsInt();
        if (count == 0) {
            throw new ApiException(RESULT_NOT_FOUND);
        }
    }
}
