package roomescape.controller.api;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.CreateTimeRequest;
import roomescape.controller.dto.CreateTimeResponse;
import roomescape.controller.dto.FindTimeAndAvailabilityResponse;
import roomescape.controller.dto.FindTimeResponse;
import roomescape.domain.reservation.ReservationTime;
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
    public ResponseEntity<CreateTimeResponse> save(@RequestBody CreateTimeRequest request) {
        ReservationTime newReservationTime = reservationTimeService.save(
            new SaveReservationTimeDto(request.startAt()));
        Long id = newReservationTime.getId();

        return ResponseEntity.created(URI.create("/times/" + id))
            .body(new CreateTimeResponse(id, newReservationTime.getStartAt()));
    }

    // TODO: 일반 사용자는 시간을 삭제할 수 없도록 수정
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationTimeService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FindTimeResponse>> findAll() {
        List<FindTimeResponse> response = reservationTimeService.findAll()
            .stream()
            .map(time -> new FindTimeResponse(
                time.getId(),
                time.getStartAt()
            )).toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<List<FindTimeAndAvailabilityResponse>> findAllWithAvailability(
        @RequestParam LocalDate date, @RequestParam Long id) {

        List<FindTimeAndAvailabilityDto> appResponses = reservationTimeService
            .findAllWithBookAvailability(date, id);
        List<FindTimeAndAvailabilityResponse> webResponses = appResponses.stream()
            .map(response -> new FindTimeAndAvailabilityResponse(
                response.id(),
                response.startAt(),
                response.alreadyBooked()
            )).toList();

        return ResponseEntity.ok(webResponses);
    }
}
