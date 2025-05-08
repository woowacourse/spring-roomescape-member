package roomescape.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import roomescape.auth.UserId;
import roomescape.controller.api.dto.request.LoginRequest;
import roomescape.controller.api.dto.response.LoginCheckResponse;
import roomescape.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * POST /login HTTP/1.1
     * content-type: application/json
     * host: localhost:8080
     * <p>
     * {
     * "password": "password",
     * "email": "admin@email.com"
     * }
     * <p>
     * HTTP/1.1 200 OK
     * Content-Type: application/json
     * Keep-Alive: timeout=60
     * Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public void login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String token = userService.login(request.toServiceRequest());
        Cookie cookie = new Cookie("token", token);
        // cookie.setPath("/");
        // cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * GET /login/check HTTP/1.1
     * cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
     * host: localhost:8080
     * <p>
     * HTTP/1.1 200 OK
     * Connection: keep-alive
     * Content-Type: application/json
     * Date: Sun, 03 Mar 2024 19:16:56 GMT
     * Keep-Alive: timeout=60
     * Transfer-Encoding: chunked
     * <p>
     * {
     * "name": "어드민"
     * }
     */

    /**
     * TODO LIST [4단계]
     *
     * 1. DB에 user 테이블 추가 및 Repository 계층 작성
     * 2. DB에 초기 데이터 삽입
     * 3. 로그인 기능 구현
     * 4. 로그인 확인 기능(인증 정보 조회) 구현
     * 5. 클라이언트 [4단계] 주석 해결
     */

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/login/check")
    public LoginCheckResponse loginCheck(@UserId Long id) {
        // return LoginCheckResponse.from();
        System.out.println("id = " + id);
        return null;
    }
}
