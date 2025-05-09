package roomescape.presentation.member.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.member.ReservationThemeService;
import roomescape.presentation.admin.dto.ReservationThemeResponseDto;

@RestController
@RequestMapping("/themes")
public final class ReservationThemeController {

    private final ReservationThemeService reservationThemeService;

    @Autowired
    public ReservationThemeController(ReservationThemeService reservationThemeService) {
        this.reservationThemeService = reservationThemeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationThemeResponseDto>> getReservationThemes() {
        List<ReservationThemeResponseDto> reservationThemes = reservationThemeService.getAllThemes();
        return ResponseEntity.ok(reservationThemes);
    }

    @GetMapping("/ranks")
    public ResponseEntity<List<ReservationThemeResponseDto>> getBestReservedReservationThemes() {
        List<ReservationThemeResponseDto> bestReservedThemes = reservationThemeService.findBestReservedThemes();
        return ResponseEntity.ok(bestReservedThemes);
    }
}
