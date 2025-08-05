package blog.jungmini.me.util;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import blog.jungmini.me.common.response.ApiResponse;
import blog.jungmini.me.dto.request.CreateUserRequest;
import blog.jungmini.me.dto.request.LoginRequest;
import blog.jungmini.me.dto.response.CreateUserResponse;

public class AuthUtil {

    private final TestRestTemplate restTemplate;
    private final int port;

    public AuthUtil(TestRestTemplate restTemplate, int port) {
        this.restTemplate = restTemplate;
        this.port = port;
    }

    public CreateUserResponse register(String email, String nickname, String password) {
        CreateUserRequest request = new CreateUserRequest(email, nickname, password);
        String registerUrl = String.format("http://localhost:%d/v1/users/register", port);

        HttpEntity<CreateUserRequest> httpEntity = new HttpEntity<>(request);
        ResponseEntity<ApiResponse<CreateUserResponse>> response =
                restTemplate.exchange(registerUrl, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {});
        return response.getBody().getData();
    }

    public String login(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);
        String loginUrl = String.format("http://localhost:%d/v1/auth/login", port);
        return restTemplate.postForObject(loginUrl, loginRequest, String.class);
    }
}
