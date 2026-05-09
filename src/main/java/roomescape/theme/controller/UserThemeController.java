package roomescape.theme.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;
import roomescape.time.controller.dto.ReservationTimeResponse;
import roomescape.time.service.ReservationTimeService;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "사용자 테마 API", description = "테마 조회 및 테마의 예약 가능한 시간 조회 관련 API")
@RestController
@RequestMapping("/themes")
public class UserThemeController {

    private final ThemeService themeService;
    private final ReservationTimeService reservationTimeService;

    public UserThemeController(ThemeService themeService, ReservationTimeService reservationTimeService) {
        this.themeService = themeService;
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> readAll() {
        List<ThemeResponse> responses = themeService.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> readPopular() {
        List<ThemeResponse> responses = themeService.findPopularThemes()
                .stream()
                .map(ThemeResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{themeId}/available-times")
    public ResponseEntity<List<ReservationTimeResponse>> readAvailable(
            @PathVariable Long themeId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<ReservationTimeResponse> responses =
                reservationTimeService.findAvailableTimes(themeId, date)
                        .stream()
                        .map(ReservationTimeResponse::from)
                        .toList();
        return ResponseEntity.ok(responses);
    }
}
