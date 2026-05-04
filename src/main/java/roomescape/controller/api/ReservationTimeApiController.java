package roomescape.controller.api;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.service.ReservationTimeService;
import roomescape.service.command.ReservationTimeCommand;
import roomescape.service.result.ReservationTimeResult;

@RestController
@RequestMapping("/times")
@Validated
@RequiredArgsConstructor
public class ReservationTimeApiController {

    private final ReservationTimeService reservationTimeService;

    @PostMapping
    public ResponseEntity<ReservationTimeResult> register(@Valid @RequestBody ReservationTimeCommand request) {
        ReservationTimeResult result = reservationTimeService.register(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.id())
                .toUri();
        return ResponseEntity.created(location).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(
            @PathVariable
            @Positive(message = "예약 시간 제거 식별자는 양수여야 합니다.")
            Long id
    ) {
        reservationTimeService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResult>> getAllTimes() {
        return ResponseEntity.ok(reservationTimeService.getAllReservationTimes());
    }
}
