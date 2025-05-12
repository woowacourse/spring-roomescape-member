package roomescape.reservation.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.service.dto.LoginMember;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.request.CreateReservationRequest;
import roomescape.reservation.service.dto.request.ReservationRequest;
import roomescape.reservation.repository.dto.ReservationWithFilterRequest;
import roomescape.reservation.service.dto.response.ReservationResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReservationResponse> getAllReservation(
            @RequestParam("memberId") @Nullable Long memberId,
            @RequestParam("themeId") @Nullable Long themeId,
            @RequestParam("from") @Nullable LocalDate from,
            @RequestParam("to") @Nullable LocalDate to
    ) {
        ReservationWithFilterRequest request = new ReservationWithFilterRequest(memberId, themeId, from, to);
        return service.getAllReservation(request);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody @Valid ReservationRequest requestDto,
            LoginMember loginMember
    ) {
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                requestDto.date(),
                loginMember,
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
