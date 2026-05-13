package roomescape.controller.client.api;

import static org.springframework.http.HttpStatus.CREATED;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.Page;
import roomescape.common.Pageable;
import roomescape.controller.client.api.dto.ReservationRequest;
import roomescape.controller.client.api.dto.ReservationResponse;
import roomescape.query.ReservationQuery;
import roomescape.query.ReservationSearchCondition;
import roomescape.query.ReservationSearchResponse;
import roomescape.service.ReservationService;
import roomescape.service.result.ReservationResult;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationApiController {

    private final ReservationService reservationService;
    private final ReservationQuery reservationQuery;

    @PostMapping
    public ResponseEntity<ReservationResponse> reserve(@Valid @RequestBody ReservationRequest request) {
        ReservationResult result = reservationService.reserve(request.toCommand());
        return ResponseEntity.status(CREATED).body(ReservationResponse.from(result));
    }

    @GetMapping
    public ResponseEntity<Page<ReservationSearchResponse>> searchBy(
            @ModelAttribute @Valid
            ReservationSearchCondition condition,
            Pageable pageable
    ) {
        return ResponseEntity.ok(reservationQuery.search(condition, pageable));
    }
}
