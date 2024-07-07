package com.eshop.eshopapi.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.eshop.eshopapi.models.AccessToken;
import com.eshop.eshopapi.models.CategoryDto;
import com.eshop.eshopapi.services.CategoryService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@CrossOrigin(origins = "*")
public class TokenRequestController {

    @Autowired
    private CategoryService categoryService;
    // @CrossOrigin(origins = "http://localhost:4200")
    @SuppressWarnings("deprecation")
    @GetMapping("/tokenRequest")
    public AccessToken getAccessToken(@RequestBody Map<String, String> requestObject) {
        WebClient client = WebClient.builder()
        .baseUrl("http://localhost:9000")
        .build();

        AccessToken response = client.post()
        .uri("/oauth2/token")
        .body(BodyInserters.fromValue(requestObject))
        .exchange()
        .flatMap(clientResponse -> {
            if (clientResponse.statusCode().is5xxServerError()) {
                clientResponse.body((clientHttpResponse, context) -> {
                    return clientHttpResponse.getBody();
                });
                return clientResponse.bodyToMono(AccessToken.class);
            }
            else
                return clientResponse.bodyToMono(AccessToken.class);
        })
        .block();
        return response;
    }

    
    @PostMapping("/getToken")
    public String getToken(@RequestBody Map<String, String> requestObject) throws UnirestException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("http://localhost:9000/oauth2/token")
          .header("Content-Type", "application/x-www-form-urlencoded")
          .field("code", requestObject.get("code"))
          .field("grant_type", requestObject.get("grant_type"))
          .field("client_id", requestObject.get("client_id"))
          .field("redirect_uri", requestObject.get("redirect_uri"))
          .field("code_verifier", requestObject.get("code_verifier"))
          .asString();
        return response.getBody();
    }

}
