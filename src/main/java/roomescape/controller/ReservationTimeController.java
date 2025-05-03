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
import roomescape.domain.ReservationSlot;
import roomescape.domain.ReservationSlots;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.AddReservationTimeRequest;
import roomescape.dto.request.AvailableTimeRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ReservationTimeSlotResponse;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationService reservationService;
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationService reservationService,
                                     ReservationTimeService reservationTimeService) {
        this.reservationService = reservationService;
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeService.allReservationTimes();
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimes.stream()
                .map((reservationTime) -> new ReservationTimeResponse(reservationTime.getId(),
                        reservationTime.getTime()))
                .toList();

        return ResponseEntity.ok(reservationTimeResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationTimeResponse> getReservationTimeById(@PathVariable Long id) {
        ReservationTime reservationTimeById = reservationTimeService.getReservationTimeById(id);
        ReservationTimeResponse reservationTimeResponse = new ReservationTimeResponse(
                reservationTimeById.getId(),
                reservationTimeById.getTime()
        );
        return ResponseEntity.ok(reservationTimeResponse);
    }

    @GetMapping("/available")
    public ResponseEntity<List<ReservationTimeSlotResponse>> getAvailableReservationTimes(
            @ModelAttribute @Valid AvailableTimeRequest availableTimeRequest) {
        ReservationSlots reservationSlotTimes = reservationService.getReservationSlots(
                availableTimeRequest);
        List<ReservationSlot> reservationSlots = reservationSlotTimes.getReservationSlots();

        List<ReservationTimeSlotResponse> reservationTimeSlotResponses = reservationSlots.stream()
                .map((time) -> new ReservationTimeSlotResponse(time.getId(), time.getTime(), time.isReserved()))
                .toList();
        return ResponseEntity.ok(reservationTimeSlotResponses);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> addReservationTime(
            @RequestBody @Valid AddReservationTimeRequest addReservationTimeRequest) {
        ReservationTime addedReservationTime = reservationTimeService.addReservationTime(addReservationTimeRequest);
        ReservationTimeResponse reservationTimeResponse = new ReservationTimeResponse(
                addedReservationTime.getId(), addedReservationTime.getTime());

        return ResponseEntity.created(URI.create("/times/" + reservationTimeResponse.id()))
                .body(reservationTimeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
