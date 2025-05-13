package roomescape.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.TokenRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.TokenResponse;
import roomescape.service.MemberLoginService;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final MemberLoginService memberLoginService;

    public LoginController(MemberLoginService memberLoginService) {
        this.memberLoginService = memberLoginService;
    }

    @PostMapping()
    @Operation(
            summary = "토큰 생성",
            description = "토큰을 생성합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "토큰 생성 성공"
                    )
            }
    )
    public ResponseEntity<Void> create(
            @RequestBody @Valid final TokenRequest tokenRequest) {
        final TokenResponse tokenResponse = memberLoginService.createToken(tokenRequest);

        final HttpCookie cookie = ResponseCookie.from("token", tokenResponse.accessToken())
                .path("/")
                .httpOnly(true)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/check")
    @Operation(
            summary = "토큰에 따른 멤버 조회",
            description = "토큰에 따른 멤버 정보를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "멤버 목록 조회 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MemberResponse.class))
                    )
            }
    )
    public ResponseEntity<MemberResponse> findInfo(final HttpServletRequest request) {
        final String token = memberLoginService.extractToken(request);
        final MemberResponse memberResponse = memberLoginService.findByToken(token);
        return ResponseEntity.ok().body(memberResponse);
    }
}
