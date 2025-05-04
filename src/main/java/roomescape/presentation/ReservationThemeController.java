package roomescape.presentation;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.presentation.dto.ReservationThemeRequestDto;
import roomescape.presentation.dto.ReservationThemeResponseDto;
import roomescape.business.service.ReservationThemeService;

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

    @PostMapping
    public ResponseEntity<ReservationThemeResponseDto> createReservationTheme(
            @Valid @RequestBody ReservationThemeRequestDto reservationThemeDto) {
        ReservationThemeResponseDto reservationTheme = reservationThemeService.createTheme(reservationThemeDto);
        String location = "/themes/" + reservationTheme.id();
        return ResponseEntity.created(URI.create(location)).body(reservationTheme);
    }

    @DeleteMapping("/{themeId}")
    public ResponseEntity<Void> deleteReservationTheme(@PathVariable("themeId") Long id) {
        reservationThemeService.deleteThemeById(id);
        return ResponseEntity.noContent().build();
    }
}
