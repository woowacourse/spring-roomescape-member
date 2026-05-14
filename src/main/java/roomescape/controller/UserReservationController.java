package roomescape.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.net.URI;
import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationCommandService;
import roomescape.service.ReservationQueryService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class UserReservationController {

    private final ReservationCommandService reservationCommandService;
    private final ReservationQueryService reservationQueryService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody ReservationRequest request) {
        LocalDateTime requestDateTime = LocalDateTime.now();
        ReservationResponse reservationResponse = reservationCommandService.create(request.name(), request.date(),
                request.timeId(), request.themeId(), requestDateTime);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build()
                .toUri();

        return ResponseEntity.created(location).body(reservationResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findReservationsByName(
            @NotBlank(message = "예약자 이름은 필수입니다.")
            @RequestParam
            String name
    ) {
        return ResponseEntity.ok(reservationQueryService.getReservationsByName(name));
    }

    @GetMapping("{id}")
    public ResponseEntity<ReservationResponse> findReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationQueryService.getReservationById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Long id) {
        reservationCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
