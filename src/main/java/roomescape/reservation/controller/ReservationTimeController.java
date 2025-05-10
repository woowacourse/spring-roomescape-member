package roomescape.reservation.controller;

import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dto.request.ReservationTimeRequest.ReservationTimeCreateRequest;
import roomescape.reservation.dto.response.ReservationTimeResponse.ReservationTimeCreateResponse;
import roomescape.reservation.dto.response.ReservationTimeResponse.ReservationTimeReadResponse;
import roomescape.reservation.service.ReservationTimeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    @PostMapping
    public ResponseEntity<ReservationTimeCreateResponse> createTime(
            @RequestBody @Valid ReservationTimeCreateRequest request
    ) {
        ReservationTimeCreateResponse response = reservationTimeService.createTime(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeReadResponse>> getAllTimes() {
        List<ReservationTimeReadResponse> responses = reservationTimeService.getAllTimes();
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(
            @PathVariable("id") long id
    ) {
        reservationTimeService.deleteTime(id);
        return ResponseEntity.ok().build();
    }
}
