package roomescape.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import roomescape.dto.ReservationRequestDTO;
import roomescape.dto.ReservationResponseDTO;
import roomescape.dto.ReservationTimeResponseDTO;
import roomescape.dto.ThemeResponseDTO;
import roomescape.service.ReservationService;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/popular-themes")
    @ResponseBody
    public ResponseEntity<List<ThemeResponseDTO>> getPopularThemes() {
        List<ThemeResponseDTO> popularThemes = reservationService.getPopularThemes();
        return ResponseEntity.ok(popularThemes);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Void> add(
            @RequestBody ReservationRequestDTO request) {
        ReservationResponseDTO saved = reservationService.addReservation(request);
        ResponseEntity<Void> responseEntity = ResponseEntity.created(
                URI.create("/reservations/" + saved.id())).build();
        return responseEntity;
    }

    @GetMapping("/booked-times")
    @ResponseBody
    public ResponseEntity<List<ReservationTimeResponseDTO>> findReservedTimes(
            @RequestParam LocalDate selectedDate,
            @RequestParam Long themeId
    ) {
        List<ReservationTimeResponseDTO> reservedTimes = reservationService.findReservedTimes(
                selectedDate, themeId
        );
        return ResponseEntity.ok(reservedTimes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok().build();
    }
}
