package roomescape.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.CreateTimeRequest;
import roomescape.controller.dto.CreateTimeResponse;
import roomescape.controller.dto.ErrorMessageResponse;
import roomescape.controller.dto.GetTimeAndAvailabilityResponse;
import roomescape.domain.ReservationTime;
import roomescape.exception.DuplicatedReservationTimeException;
import roomescape.exception.ReservationExistsException;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.FindTimeAndAvailabilityDto;
import roomescape.service.dto.SaveReservationTimeDto;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<CreateTimeResponse> create(@RequestBody CreateTimeRequest request) {
        ReservationTime newReservationTime = reservationTimeService.save(
            new SaveReservationTimeDto(request.startAt()));
        Long id = newReservationTime.getId();

        return ResponseEntity.created(URI.create("/times/" + id))
            .body(new CreateTimeResponse(
                id,
                newReservationTime.getStartAt()
            ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBy(@PathVariable Long id) {
        reservationTimeService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CreateTimeResponse>> getReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeService.findAll();
        List<CreateTimeResponse> createReservationTimeRespons = reservationTimes.stream()
            .map(reservationTime -> new CreateTimeResponse(reservationTime.getId(),
                reservationTime.getStartAt()))
            .toList();

        return ResponseEntity.ok(createReservationTimeRespons);
    }

    @GetMapping("/user")
    public ResponseEntity<List<GetTimeAndAvailabilityResponse>> getReservationTimesWithAvailability(
        @RequestParam LocalDate date, @RequestParam Long id) {

        List<FindTimeAndAvailabilityDto> appResponses = reservationTimeService
            .findAllWithBookAvailability(date, id);
        List<GetTimeAndAvailabilityResponse> webResponses = appResponses.stream()
            .map(response -> new GetTimeAndAvailabilityResponse(
                response.id(),
                response.startAt(),
                response.alreadyBooked())
            ).toList();

        return ResponseEntity.ok(webResponses);
    }

    @ExceptionHandler(ReservationExistsException.class)
    public ResponseEntity<ErrorMessageResponse> handleReservationExistsException(ReservationExistsException e) {
        ErrorMessageResponse response = new ErrorMessageResponse(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DuplicatedReservationTimeException.class)
    public ResponseEntity<ErrorMessageResponse> handleDuplicatedReservationTimeException(DuplicatedReservationTimeException e) {
        ErrorMessageResponse response = new ErrorMessageResponse(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
