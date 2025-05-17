package roomescape.presentation.admin.api;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.admin.AdminReservationThemeService;
import roomescape.presentation.admin.dto.ReservationThemeRequestDto;
import roomescape.presentation.admin.dto.ReservationThemeResponseDto;

@RestController
@RequestMapping("/admin/themes")
public final class AdminReservationThemeController {

    private final AdminReservationThemeService reservationThemeService;

    public AdminReservationThemeController(AdminReservationThemeService reservationThemeService) {
        this.reservationThemeService = reservationThemeService;
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
