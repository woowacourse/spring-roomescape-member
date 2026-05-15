package roomescape.controller.admin;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.domain.Reservation;
import roomescape.dto.request.ReservationPatchDto;
import roomescape.dto.request.ReservationRequestDto;
import roomescape.dto.response.AdminReservationResponseDto;
import roomescape.dto.response.PageResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {
    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<AdminReservationResponseDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<AdminReservationResponseDto> responses = reservationService.findAll(page, size)
                .map(AdminReservationResponseDto::from);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminReservationResponseDto> findById(@PathVariable Long id) {
        Reservation reservation = reservationService.findById(id);
        return ResponseEntity.ok(AdminReservationResponseDto.from(reservation));
    }

    @PostMapping
    public ResponseEntity<AdminReservationResponseDto> create(
            @Valid @RequestBody ReservationRequestDto reservationRequest) {
        Reservation reservation = reservationService.createByAdmin(reservationRequest);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(reservation.getId()).toUri();
        return ResponseEntity.created(uri).body(AdminReservationResponseDto.from(reservation));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AdminReservationResponseDto> patch(
            @PathVariable Long id,
            @Valid @RequestBody ReservationPatchDto request) {
        Reservation updated = reservationService.update(id, request);
        return ResponseEntity.ok(AdminReservationResponseDto.from(updated));
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        reservationService.cancelByAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
