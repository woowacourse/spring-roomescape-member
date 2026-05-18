package roomescape.controller.api;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import roomescape.common.dto.ApiResponse;
import roomescape.domain.reservation.dto.ReservationCreateRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.dto.ReservationUpdateRequest;
import roomescape.service.ReservationService;

import java.util.List;

@RequestMapping("/reservations")
@RestController
public class ReservationRestController {

    private final ReservationService reservationService;

    public ReservationRestController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ApiResponse<ReservationResponse> create(@Valid @RequestBody ReservationCreateRequest reservationReq) {
        return new ApiResponse<>(reservationService.create(reservationReq));
    }

    @GetMapping("/{id}")
    public ApiResponse<ReservationResponse> read(@PathVariable Long id) {
        return new ApiResponse<>(reservationService.read(id));
    }

    @GetMapping
    public ApiResponse<List<ReservationResponse>> readAll() {
        return new ApiResponse<>(reservationService.readAll());
    }

    @GetMapping("/mine")
    public ApiResponse<List<ReservationResponse>> readMyReservations(
            @RequestParam String name
    ) {
        return new ApiResponse<>(reservationService.readMyReservations(name));
    }

    @PutMapping("/{id}")
    public ApiResponse<ReservationResponse> update(@PathVariable Long id, @Valid @RequestBody ReservationUpdateRequest newReservationReq) {
        return new ApiResponse<>(reservationService.update(id, newReservationReq));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return new ApiResponse<>(null);
    }
}
