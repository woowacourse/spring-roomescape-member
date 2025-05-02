package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
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
import roomescape.domain.ReservationSlot;
import roomescape.domain.ReservationSlotTimes;
import roomescape.domain.Theme;
import roomescape.dto.request.AddReservationRequest;
import roomescape.dto.request.AvailableTimeRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ReservationTimeSlotResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> reservations() {
        List<Reservation> reservations = reservationService.allReservations();
        List<ReservationResponse> reservationDtos = reservations.stream()
                .map((reservation) -> new ReservationResponse(reservation.getId(), reservation.getName(),
                        reservation.getStartAt(), reservation.getDate(), reservation.getThemeName()))
                .toList();
        return ResponseEntity.ok(reservationDtos);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> addReservations(
            @RequestBody @Valid AddReservationRequest newReservationDto) {
        long addedReservationId = reservationService.addReservation(newReservationDto);
        Reservation reservation = reservationService.getReservationById(addedReservationId);

        ReservationResponse reservationResponse = new ReservationResponse(reservation.getId(),
                reservation.getName(), reservation.getStartAt(), reservation.getDate(), reservation.getThemeName());
        return ResponseEntity.created(URI.create("/reservations/" + addedReservationId)).body(reservationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservations(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<ReservationTimeSlotResponse>> availableReservationTimes(
            @Valid @ModelAttribute AvailableTimeRequest availableTimeRequest) {
        ReservationSlotTimes reservationSlotTimes = reservationService.availableReservationTimes(
                availableTimeRequest);
        List<ReservationSlot> availableBookTimes = reservationSlotTimes.getAvailableBookTimes();

        List<ReservationTimeSlotResponse> reservationTimeSlotResponses = availableBookTimes.stream()
                .map((time) -> new ReservationTimeSlotResponse(time.getId(), time.getTime(), time.isReserved()))
                .toList();
        return ResponseEntity.ok(reservationTimeSlotResponses);
    }

    @GetMapping("/popular-themes")
    public ResponseEntity<List<ThemeResponse>> popularThemes() {
        List<Theme> rankingThemes = reservationService.getRankingThemes(LocalDate.now());

        List<ThemeResponse> themeResponses = rankingThemes.stream()
                .map((theme) -> new ThemeResponse(theme.getId(), theme.getDescription(),
                        theme.getName(), theme.getThumbnail()))
                .toList();
        return ResponseEntity.ok(themeResponses);
    }
}
