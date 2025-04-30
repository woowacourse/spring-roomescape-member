package roomescape.presentation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import roomescape.business.dto.ReservationResponseDto;
import roomescape.business.dto.ReservationThemeResponseDto;
import roomescape.business.dto.ReservationTimeResponseDto;
import roomescape.business.service.ReservationService;

@Controller
public class ReservationQueryController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationQueryController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("reservations")
    @ResponseBody
    public ResponseEntity<List<ReservationResponseDto>> readReservations() {
        List<ReservationResponseDto> reservations = reservationService.readReservationAll();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("reservations/{reservationId}")
    @ResponseBody
    public ResponseEntity<ReservationResponseDto> readReservation(@PathVariable("reservationId") Long id) {
        ReservationResponseDto reservation = reservationService.readReservationOne(id);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("times")
    @ResponseBody
    public ResponseEntity<List<ReservationTimeResponseDto>> readReservationTimes() {
        List<ReservationTimeResponseDto> reservationTimes = reservationService.readTimeAll();
        return ResponseEntity.ok(reservationTimes);
    }

    @GetMapping("times/{timeId}")
    @ResponseBody
    public ResponseEntity<ReservationTimeResponseDto> readReservationTime(@PathVariable("timeId") Long id) {
        ReservationTimeResponseDto reservationTime = reservationService.readTimeOne(id);
        return ResponseEntity.ok(reservationTime);
    }

    @GetMapping("themes")
    public ResponseEntity<List<ReservationThemeResponseDto>> readReservationThemes() {
        List<ReservationThemeResponseDto> reservationThemes = reservationService.readThemeAll();
        return ResponseEntity.ok(reservationThemes);
    }
}
