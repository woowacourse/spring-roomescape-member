package roomescape.global.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "home-page", description = "홈 페이지")
@Controller
public class HomePageController {

    @GetMapping("/")
    @Operation(summary = "홈 페이지 조회", description = "룸 이스케이프 홈 화면을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "홈 페이지 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public String read() {
        return "index";
    }

}
