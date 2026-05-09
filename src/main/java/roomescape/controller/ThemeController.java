package roomescape.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.dto.PopularThemeResponse;
import roomescape.dto.ThemeReservationTimeResponse;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeService;

@Tag(name = "사용자 - 테마 조회", description = "사용자용 테마 목록·인기 테마·예약 가능 시간 조회 API")
@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @Operation(summary = "전체 테마 목록 조회", description = "등록된 모든 테마 목록을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "테마 목록 조회 성공")
    @GetMapping
    public ResponseEntity<List<ThemeResponse>> readThemes() {
        List<Theme> themes = themeService.getThemes();
        return ResponseEntity.ok(themes.stream()
                .map(ThemeResponse::from)
                .toList());
    }

    @Operation(summary = "테마별 예약 가능 시간 조회",
            description = "특정 테마에 대해 주어진 날짜의 예약 가능 시간 목록을 반환합니다. isReserved=true이면 이미 예약된 시간입니다.")
    @ApiResponse(responseCode = "200", description = "예약 가능 시간 목록 조회 성공")
    @GetMapping("/{id}/times")
    public ResponseEntity<List<ThemeReservationTimeResponse>> readThemeTimes(
            @Parameter(description = "테마 ID", example = "1") @PathVariable Long id,
            @Parameter(description = "조회할 날짜 (yyyy-MM-dd)", example = "2025-08-05") @RequestParam LocalDate date) {
        List<ThemeReservationTimeResponse> themeTimes = themeService.getThemeTimes(id, date);
        return ResponseEntity.ok(themeTimes);
    }

    @Operation(summary = "인기 테마 조회",
            description = "최근 1주일(어제 기준) 동안 예약이 많았던 테마를 내림차순으로 반환합니다.")
    @ApiResponse(responseCode = "200", description = "인기 테마 조회 성공")
    @GetMapping("/popular")
    public ResponseEntity<List<PopularThemeResponse>> readPopularThemes(
            @Parameter(description = "조회할 최대 테마 수", example = "10") @RequestParam Integer limit) {
        List<PopularThemeResponse> popularThemes = themeService.getPopularThemes(limit);
        return ResponseEntity.ok(popularThemes);
    }
}
