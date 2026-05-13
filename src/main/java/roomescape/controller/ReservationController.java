package roomescape.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.controller.dto.ReservationDetailResponse;
import roomescape.controller.dto.ReservationSummaryResponse;
import roomescape.controller.mapper.ReservationMapper;
import roomescape.domain.EntityId;
import roomescape.service.ReservationService;
import roomescape.service.dto.ReservationCreateCommand;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService service;
    private final ReservationMapper mapper;

    @PostMapping
    public ResponseEntity<ReservationSummaryResponse> create(
            @RequestBody ReservationCreateRequest createRequest
    ) {
        ReservationCreateCommand createCommand = mapper.mapToCommand(createRequest);
        ReservationSummaryResponse response = service.create(createCommand);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationDetailResponse>> findByName(
            @RequestParam String name
    ) {
        List<ReservationDetailResponse> responses = service.findAllIncludeDetail(name);

        return ResponseEntity.ok(responses);
    }
}
