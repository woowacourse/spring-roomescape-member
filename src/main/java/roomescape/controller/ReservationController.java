package roomescape.controller;

import jakarta.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationRequestDTO;
import roomescape.dto.ReservationResponseDTO;
import roomescape.dto.ReservationUpdateDtoDateAndTimeIdOnly;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> readAllByUsername(
            @RequestParam @NotBlank(message = "이름은 1글자 이상 입력되어야 합니다.") String username
    ) {
        return ResponseEntity
                .ok(reservationService.findAllByUsername(username));
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody ReservationRequestDTO request) {
        ReservationResponseDTO saved = reservationService.addReservation(request);
        return ResponseEntity
                .created(URI.create("/reservations/" + saved.id()))
                .build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody ReservationUpdateDtoDateAndTimeIdOnly updateDto
    ) {
        reservationService.update(id, updateDto);
        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteReservationById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@ModelAttribute ReservationRequestDTO request) {
        reservationService.deleteReservationByUsernameAndDateAndTimeIdAndThemeId(request);
        return ResponseEntity
                .noContent()
                .build();
    }
}
