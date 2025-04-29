package roomescape.presentation;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.dto.ReservationRequestDto;
import roomescape.business.dto.ReservationResponseDto;
import roomescape.business.dto.ReservationTimeRequestDto;
import roomescape.business.dto.ReservationTimeResponseDto;
import roomescape.business.service.ReservationService;

@RestController
public class ReservationCommandController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationCommandController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("reservations")
    public ResponseEntity<ReservationResponseDto> create(@Valid @RequestBody ReservationRequestDto reservationDto) {
        Long id = reservationService.createReservation(reservationDto);
        ReservationResponseDto reservation = reservationService.readReservationOne(id);
        String location = "/reservations/" + id;
        return ResponseEntity.created(URI.create(location)).body(reservation);
    }

    @DeleteMapping("reservations/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("reservationId") Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("times")
    public ResponseEntity<ReservationTimeResponseDto> create(
            @Valid @RequestBody ReservationTimeRequestDto reservationTimeDto) {
        Long id = reservationService.createTime(reservationTimeDto);
        ReservationTimeResponseDto reservationTime = reservationService.readTimeOne(id);
        String location = "/times/" + id;
        return ResponseEntity.created(URI.create(location)).body(reservationTime);
    }

    @DeleteMapping("times/{timeId}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable("timeId") Long id) {
        reservationService.deleteTime(id);
        return ResponseEntity.noContent().build();
    }
}
