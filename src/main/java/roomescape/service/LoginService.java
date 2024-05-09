package roomescape.service;

import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private static final String EMAIL = "a@a.com";
    private static final String PASSWORD = "123!";

    public boolean isInvalidLogin(String email, String password) {
        return !email.equals(EMAIL) || !password.equals(PASSWORD);
    }
}
