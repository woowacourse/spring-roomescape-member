package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import roomescape.controller.member.dto.LoginMember;

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
    public String showLoginPage(final LoginMember loginMember) {
        if (loginMember != null) {
            return showPopularTheme();
        }
        return "/login";
    }

    @PostMapping("/logout")
    public String logout(final HttpServletResponse response) {
        // todo 세션도 만료시키기.
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "/index";
    }
}
