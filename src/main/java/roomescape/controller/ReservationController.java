package roomescape.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.ReservationRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.global.exception.InvalidReservationPagingException;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private static final int MAX_PAGE_SIZE = 100;

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        validatePagingCondition(page, size);
        List<ReservationResponse> reservations = reservationService.getReservations(page, size);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody ReservationRequest request) {
        ReservationResponse reservation = reservationService.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

    private void validatePagingCondition(int page, int size) {
        if (page < 0) {
            throw new InvalidReservationPagingException("페이지 번호는 0 이상이어야 합니다.");
        }
        if (size < 1) {
            throw new InvalidReservationPagingException("페이지 크기는 1 이상이어야 합니다.");
        }
        if (size > MAX_PAGE_SIZE) {
            throw new InvalidReservationPagingException("페이지 크기는 " + MAX_PAGE_SIZE + "을 넘을 수 없습니다.");
        }
    }
}
