package roomescape.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.ReservationDetailResponse;
import roomescape.domain.EntityId;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/admin/reservations")
@RequiredArgsConstructor
public class AdminReservationController {

    private final ReservationService service;

    @GetMapping
    public ResponseEntity<List<ReservationDetailResponse>> findAll() {
        List<ReservationDetailResponse> responses = service.findAllIncludeDetail();

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {
        service.delete(EntityId.fromUuid(id));

        return ResponseEntity.ok().build();
    }
}
