package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @GetMapping
    public String showPopularTheme() {
        return "/index";
    }

    @GetMapping("/reservation")
    public String showUserPage() {
        return "/reservation";
    }

    @GetMapping("/login")
    public String showLoginPage(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        final String cookie = extractTokenFromCookie(cookies);
        if (cookie == null || cookie.isEmpty()) {
            return "/login";
        }
        return "redirect:/";
    }

    //TODO 여기에 있는게 맞을 지 고민해보자.
    @PostMapping("/logout")
    public String logout(final HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "/login";
    }

    private String extractTokenFromCookie(final Cookie[] cookies) {
        for (final Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
