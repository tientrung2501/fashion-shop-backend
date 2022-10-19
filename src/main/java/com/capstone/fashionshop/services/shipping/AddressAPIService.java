package com.capstone.fashionshop.services.shipping;

import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.payload.ResponseObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class AddressAPIService {
    @Value("${app.ghn.token}")
    private String TOKEN;
    public ResponseEntity<?> getData(String url_str) throws IOException {
        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setRequestMethod("GET");
        request.setRequestProperty("token", TOKEN);
        request.connect();
        if (request.getResponseCode() != HttpURLConnection.HTTP_OK)
            throw new AppException(HttpStatus.EXPECTATION_FAILED.value(), "Failed when get address");
        JsonObject jsonObject = JsonParser.parseReader(
                new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
        String result = jsonObject.get("code").getAsString();
        System.out.println(result);
        request.disconnect();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(true, "Get data success", result));
    }
}
