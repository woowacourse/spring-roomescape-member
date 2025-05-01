package roomescape.reservationTime.presentation;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.exceptionHandler.dto.ExceptionResponse;
import roomescape.reservationTime.dto.ReservationTimeRequest;
import roomescape.reservationTime.dto.ReservationTimeResponse;
import roomescape.reservationTime.dto.TimeConditionRequest;
import roomescape.reservationTime.dto.TimeConditionResponse;
import roomescape.reservationTime.service.ReservationTimeService;

@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponse> createReservationTime(@RequestBody ReservationTimeRequest request) {
        ReservationTimeResponse response = reservationTimeService.createReservationTime(request);
        return ResponseEntity.created(URI.create("/admin/time")).body(response);
    }

    @GetMapping(value = "/times")
    public ResponseEntity<List<ReservationTimeResponse>> getReservationTimes() {
        List<ReservationTimeResponse> response = reservationTimeService.getReservationTimes();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/times", consumes = {"application/json"})
    public ResponseEntity<List<TimeConditionResponse>> getReservationTimes(final TimeConditionRequest request) {
        List<TimeConditionResponse> responses = reservationTimeService.getTimesWithCondition(request);
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteReservationTimeById(@PathVariable("id") Long id) {
        reservationTimeService.deleteReservationTimeById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(value = DateTimeParseException.class)
    public ResponseEntity<ExceptionResponse> noMatchTimeType(final HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                400, "[ERROR] 요청 시간 형식이 맞지 않습니다.", request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(exceptionResponse);
    }
}
