package com.renan.booksalesonline.testhelpers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class RestClientTesting {

    private String baseUrl;
    private TestRestTemplate testRestTemplate;

    public RestClientTesting(@Value("${server.port}") Integer port) {

        baseUrl = "http://localhost:" + port + "/v1/api";
        testRestTemplate = new TestRestTemplate();
    }

    public <T> ResponseEntity<T> get(Class<T> clazz, String path) {

        return testRestTemplate.getForEntity(getUrl(path), clazz);
    }

    public <T> ResponseEntity<T> post(Class<T> clazz, String path, Object payload) {

        return testRestTemplate.postForEntity(getUrl(path), payload, clazz);
    }

    public <T> ResponseEntity<T> put(Class<T> clazz, String path, T payload) {

        var request = new HttpEntity<>(payload);
        return testRestTemplate.exchange(getUrl(path), HttpMethod.PUT, request, clazz);
    }

    public <T> T patch(Class<T> clazz, String path, Object payload) {

        return testRestTemplate.patchForObject(getUrl(path), payload, clazz);
    }

    public ResponseEntity<String> delete(String path) {

        return testRestTemplate
                .exchange(getUrl(path), HttpMethod.DELETE, null, String.class);
    }

    private String getUrl(String path) {
        return String.format("%s/%s", baseUrl, path);
    }
}
