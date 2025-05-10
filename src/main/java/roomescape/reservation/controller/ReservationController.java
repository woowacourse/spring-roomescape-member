package roomescape.reservation.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.entity.LoginMember;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.request.CreateReservationRequest;
import roomescape.reservation.service.dto.request.ReservationRequest;
import roomescape.reservation.service.dto.response.ReservationResponse;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReservationResponse> getAllReservation() {
        return service.getAllReservation();
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody @Valid ReservationRequest requestDto,
            LoginMember loginMember
    ) {
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                requestDto.date(),
                loginMember.getId(),
                requestDto.timeId(),
                requestDto.themeId()
        );
        ReservationResponse responseDto = service.createReservation(createReservationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") @NotNull Long id) {
        service.deleteReservation(id);
        return ResponseEntity.ok().build();
    }
}
