package roomescape.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.dto.ResourceIdResponse;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // TODO: ResponseEntity 쓸지? ResponseStatus 그대로 유지?
    // TODO: 리스트 반환할 때 변수 선언 후 stream 도는게 디버깅 시 유리한지?
    // TODO: 리스트 반환 시 한번 더 감싸는게 어떤 이득이 있는지?
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationResponse> getReservations() {
        return reservationService.getReservations().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResourceIdResponse addReservation(@Valid @RequestBody ReservationRequest requestDto) {
        Reservation reservation = reservationService.addReservation(requestDto);
        return new ResourceIdResponse(reservation.getId());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
    }
}
