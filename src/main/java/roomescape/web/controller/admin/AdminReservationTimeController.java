package roomescape.web.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ReservationTimeService;
import roomescape.web.dto.ReservationTimeRequest;
import roomescape.web.dto.ReservationTimeResponse;
import roomescape.web.dto.ReservationTimeResponses;

@RestController
@RequestMapping("/api/admin/times")
@Validated
@RequiredArgsConstructor
public class AdminReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> register(
            @Valid @RequestBody ReservationTimeRequest request
    ) {
        ReservationTimeResponse response = reservationTimeService.register(request);

        URI location = URI.create("/api/admin/times/" + response.id());

        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(
            @PathVariable
            @Positive(message = "예약 시간 제거 식별자는 양수여야 합니다.") Long id
    ) {
        reservationTimeService.remove(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ReservationTimeResponses> getAllTimes() {
        ReservationTimeResponses response = new ReservationTimeResponses(
                reservationTimeService.getAllReservationTimes());

        return ResponseEntity.ok(response);
    }
}
