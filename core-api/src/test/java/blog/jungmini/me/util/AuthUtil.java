package blog.jungmini.me.util;

import org.springframework.boot.test.web.client.TestRestTemplate;

import blog.jungmini.me.dto.request.CreateUserRequest;
import blog.jungmini.me.dto.request.LoginRequest;

public class AuthUtil {

    private final TestRestTemplate restTemplate;
    private final int port;

    public AuthUtil(TestRestTemplate restTemplate, int port) {
        this.restTemplate = restTemplate;
        this.port = port;
    }

    public void register(String email, String nickname, String password) {
        CreateUserRequest request = new CreateUserRequest(email, nickname, password);
        String registerUrl = String.format("http://localhost:%d/v1/users/register", port);
        restTemplate.postForObject(registerUrl, request, String.class);
    }

    public String login(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);
        String loginUrl = String.format("http://localhost:%d/v1/auth/login", port);
        return restTemplate.postForObject(loginUrl, loginRequest, String.class);
    }
}
