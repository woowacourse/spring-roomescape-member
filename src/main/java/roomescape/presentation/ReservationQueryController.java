package roomescape.presentation;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.dto.AvailableTimesResponseDto;
import roomescape.business.dto.ReservationResponseDto;
import roomescape.business.dto.ReservationThemeResponseDto;
import roomescape.business.dto.ReservationTimeResponseDto;
import roomescape.business.service.ReservationService;

@RestController
public final class ReservationQueryController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationQueryController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("reservations")
    public ResponseEntity<List<ReservationResponseDto>> readReservations() {
        List<ReservationResponseDto> reservations = reservationService.readReservationAll();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("reservations/{reservationId}")
    public ResponseEntity<ReservationResponseDto> readReservation(@PathVariable("reservationId") Long id) {
        ReservationResponseDto reservation = reservationService.readReservationOne(id);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("times")
    public ResponseEntity<List<AvailableTimesResponseDto>> readReservationAvailableTimes(
            @RequestParam("date") LocalDate date, @RequestParam("themeId") Long themeId) {
        List<AvailableTimesResponseDto> reservationTimes = reservationService.readAvailableTimes(date, themeId);
        return ResponseEntity.ok(reservationTimes);
    }

    @GetMapping("times/{timeId}")
    public ResponseEntity<ReservationTimeResponseDto> readReservationTime(@PathVariable("timeId") Long id) {
        ReservationTimeResponseDto reservationTime = reservationService.readTimeOne(id);
        return ResponseEntity.ok(reservationTime);
    }

    @GetMapping("themes")
    public ResponseEntity<List<ReservationThemeResponseDto>> readReservationThemes() {
        List<ReservationThemeResponseDto> reservationThemes = reservationService.readThemeAll();
        return ResponseEntity.ok(reservationThemes);
    }

    @GetMapping("ranks")
    public ResponseEntity<List<ReservationThemeResponseDto>> readBestReservedReservationThemes() {
        List<ReservationThemeResponseDto> bestReservedThemes = reservationService.readBestReservedThemes();
        return ResponseEntity.ok(bestReservedThemes);
    }
}
