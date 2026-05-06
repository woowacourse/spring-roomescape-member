package roomescape.ui.admin.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
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
import roomescape.service.result.ReservationTimeResult;
import roomescape.ui.admin.api.dto.AdminReservationTimeRequest;
import roomescape.ui.admin.api.dto.AdminReservationTimeResponse;

@RestController
@RequestMapping("/api/admin/times")
@Validated
@RequiredArgsConstructor
public class AdminReservationTimeApiController {

    private final ReservationTimeService reservationTimeService;

    @PostMapping
    public ResponseEntity<AdminReservationTimeResponse> register(
            @Valid @RequestBody AdminReservationTimeRequest request
    ) {
        ReservationTimeResult result = reservationTimeService.register(request.toCommand());

        URI location = URI.create("/api/admin/times/" + result.id());

        return ResponseEntity.created(location).body(AdminReservationTimeResponse.from(result));
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
    public ResponseEntity<List<AdminReservationTimeResponse>> getAllTimes() {
        List<AdminReservationTimeResponse> response = reservationTimeService.getAllReservationTimes()
                .stream()
                .map(AdminReservationTimeResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }
}
