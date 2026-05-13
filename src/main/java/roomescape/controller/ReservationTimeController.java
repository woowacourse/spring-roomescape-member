package roomescape.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.dto.ResourceIdResponse;
import roomescape.dto.reservationTime.AvailableReservationTimesResponse;
import roomescape.dto.reservationTime.ReservationTimeRequest;
import roomescape.dto.reservationTime.ReservationTimeResponse;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationService reservationService;

    public ReservationTimeController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationTimeResponse> getReservationTimes() {
        return reservationService.getReservationTimes().stream()
            .map(ReservationTimeResponse::from)
            .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResourceIdResponse addReservationTime(
        @Valid @RequestBody ReservationTimeRequest requestDto,
        @RequestParam(value = "role", required = false) String role
    ) {
        if (!"admin".equals(role)) {
            throw new RoomEscapeException(ErrorCode.FORBIDDEN);
        }

        ReservationTime time = reservationService.addReservationTime(requestDto);
        return new ResourceIdResponse(time.getId());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservationTime(
        @PathVariable Long id,
        @RequestParam(value = "role", required = false) String role
    ) {
        if (!"admin".equals(role)) {
            throw new RoomEscapeException(ErrorCode.FORBIDDEN);
        }

        reservationService.deleteReservationTime(id);
    }

    @GetMapping("available")
    @ResponseStatus(HttpStatus.OK)
    public AvailableReservationTimesResponse getAvailableTimes(
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam("themeId") Long themeId
    ) {
        List<ReservationTime> allTimes = reservationService.getReservationTimes();
        List<ReservationTime> availableTimes = reservationService.getAvailableTimes(date, themeId);

        return AvailableReservationTimesResponse.builder()
            .allTimes(allTimes)
            .availableTimes(availableTimes)
            .build();
    }
}
