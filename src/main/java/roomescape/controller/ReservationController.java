package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationSlotTimes;
import roomescape.dto.AddReservationDto;
import roomescape.dto.AvailableTimeRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.dto.ReservationTimeSlotResponseDto;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> reservations() {
        List<Reservation> reservations = reservationService.allReservations();
        List<ReservationResponseDto> reservationDtos = reservations.stream()
                .map((reservation) -> new ReservationResponseDto(reservation.getId(), reservation.getName(),
                        reservation.getStartAt(), reservation.getDate(), reservation.getThemeName()))
                .toList();
        return ResponseEntity.ok(reservationDtos);
    }

    @PostMapping
    public ResponseEntity<Void> addReservations(@RequestBody @Valid AddReservationDto newReservationDto) {
        long addedReservationId = reservationService.addReservation(newReservationDto);
        return ResponseEntity.created(URI.create("/reservations/" + addedReservationId)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservations(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<ReservationTimeSlotResponseDto>> availableReservationTimes(
            @Valid @ModelAttribute AvailableTimeRequestDto availableTimeRequestDto) {
        ReservationSlotTimes reservationSlotTimes = reservationService.availableReservationTimes(
                availableTimeRequestDto);
        List<ReservationTimeSlotResponseDto> reservationTimeSlotResponseDtos = reservationSlotTimes.getAvailableBookTimes()
                .stream()
                .map((time) -> new ReservationTimeSlotResponseDto(time.getId(), time.getTime(), time.isReserved()))
                .toList();
        return ResponseEntity.ok(reservationTimeSlotResponseDtos);
    }
}
