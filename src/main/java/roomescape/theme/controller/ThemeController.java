package roomescape.theme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;

@Tag(name = "theme", description = "테마 API")
@RestController
@RequestMapping("/themes")
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService themeService;

    @GetMapping
    @Operation(summary = "전체 테마 조회", description = "사용자가 예약 가능한 전체 테마 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전체 테마 조회 성공")
    })
    public List<ThemeResponse> readAll() {
        return themeService.getAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @GetMapping("/popular")
    @Operation(summary = "인기 테마 조회", description = "지정한 기간과 개수에 맞춰 인기 테마 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인기 테마 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 조회 조건 파라미터")
    })
    public List<ThemeResponse> read(
            @RequestParam final int period,
            @RequestParam final int limit
    ) {
        return themeService.getPopularThemes(period, limit).stream()
                .map(ThemeResponse::from)
                .toList();
    }

}
