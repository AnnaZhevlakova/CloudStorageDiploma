package com.example.CloudStorageDiploma;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class CloudStorageDiplomaApplicationTests {
    String baseUrl = "http://localhost:" + 8082 + "/cloud";
    String fileName = "test.txt";

    @Autowired
    TestRestTemplate restTemplate;

    @Container
    private final GenericContainer<?> app = new GenericContainer<>("app")
            .withExposedPorts(8082);


    @Test
    void loginTest() {
        var result = login();
    }

    @Test
    void uploadFile() throws Exception {
        var authToken = login();
        uploadFile(authToken, fileName, new ClassPathResource("files/test.txt"));
    }

    @Test
    void listFiles() {
        var authToken = login();
        listFiles(authToken);
    }

    @Test
    void downloadFile() throws Exception {
        var authToken = login();
        downloadFile(authToken, fileName);
    }

    @Test
    void renameFile() {
        var authToken = login();
        renameFile(authToken, fileName, "test1.txt");
    }

    @Test
    void deleteFile() throws Exception {
        var authToken = login();
        uploadFile(authToken, fileName, new ClassPathResource("files/test.txt"));
        deleteFile(authToken, fileName);
    }

    @Test
    void logout() {
        var authToken = login();
        logout(authToken);
    }


    String login() {
        String url = baseUrl + "/login";
        Map<String, String> request = Map.of(
                "login", "test",
                "password", "test"
        );
        ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, request, JsonNode.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody().get("auth-token").asText();
    }

    private void uploadFile(String token, String filename, Resource fileResource) throws IOException {
        String url = baseUrl + "/file?filename=" + filename;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("auth-token", token);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource); // Spring сам подхватит как FileSystemResource

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    private void listFiles(String token) {
        String url = baseUrl + "/list?limit=10";
        HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, JsonNode.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JsonNode files = response.getBody();
        assertTrue(files.isArray() || files.has("filename")); // зависит от точной структуры
        System.out.println("Files: " + files);
    }

    private void downloadFile(String token, String filename) throws IOException {
        String url = baseUrl + "/file?filename=" + filename;

        HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, byte[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);

        // можно сохранить в временный файл для проверки
        Path tempFile = Files.createTempFile("downloaded_", filename);
        Files.write(tempFile, response.getBody());
        System.out.println("Downloaded file saved to: " + tempFile);
    }

    private void renameFile(String token, String oldName, String newName) {
        String url = baseUrl + "/file?filename=" + oldName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("auth-token", token);

        Map<String, String> body = Map.of("name", newName);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.PUT, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private void deleteFile(String token, String filename) {
        String url = baseUrl + "/file?filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.DELETE, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private void logout(String token) {
        String url = baseUrl + "/logout";

        HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }



   /* @Test
    void testDevProfileContent() {
        Integer devPort = devApp.getMappedPort(8080);
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + devPort + "/profile",
                String.class
        );
        assertEquals("This is dev profile", response.getBody());
    }

    @Test
    void testProdProfileContent() {
        Integer prodPort = prodApp.getMappedPort(8081);
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + prodPort + "/profile",
                String.class
        );
        assertEquals("This is production profile", response.getBody());
    }*/

}
