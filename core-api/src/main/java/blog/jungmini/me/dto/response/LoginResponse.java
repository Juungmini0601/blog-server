package blog.jungmini.me.dto.response;

import lombok.Getter;

@Getter
public class LoginResponse {
    private String sessionId;

    public LoginResponse(String sessionId) {
        this.sessionId = sessionId;
    }
}
