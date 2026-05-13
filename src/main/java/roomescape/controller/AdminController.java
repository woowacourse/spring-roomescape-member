package roomescape.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationResponseDTO;
import roomescape.dto.ReservationTimeRequestDTO;
import roomescape.dto.ReservationTimeResponseDTO;
import roomescape.dto.ThemeRequestDTO;
import roomescape.dto.ThemeResponseDTO;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ReservationService reservationService;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public AdminController(
            ReservationService reservationService,
            ReservationTimeService reservationTimeService,
            ThemeService themeService
    ) {
        this.reservationService = reservationService;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponseDTO> readAll() {
        return reservationService.readAllReservation();
    }

    @GetMapping("/reservations/{id}")
    public ReservationResponseDTO findReservationById(@PathVariable Long id) {
        return reservationService.findById(id);
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponseDTO> add(@RequestBody ReservationTimeRequestDTO request) {
        ReservationTimeResponseDTO saved = reservationTimeService.addReservationTime(request);
        return ResponseEntity.created(URI.create("/times/" + saved.id())).build();
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable Long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponseDTO> add(@RequestBody ThemeRequestDTO request) {
        ThemeResponseDTO saved = themeService.addTheme(request);
        return ResponseEntity.created(URI.create("/theme/" + saved.id())).build();
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
