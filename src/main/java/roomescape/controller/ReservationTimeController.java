package roomescape.controller;

import java.time.LocalDate;
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
import roomescape.controller.dto.ReservationTimeCreateRequest;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.controller.mapper.ReservationTimeMapper;
import roomescape.domain.EntityId;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.ReservationTimeCreateCommand;

@RestController
@RequestMapping("/times")
@RequiredArgsConstructor
public class ReservationTimeController {

    private final ReservationTimeService service;
    private final ReservationTimeMapper mapper;

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(
            @RequestBody ReservationTimeCreateRequest createRequest
    ) {
        ReservationTimeCreateCommand createCommand = mapper.mapToCommand(createRequest);
        ReservationTimeResponse response = service.create(createCommand);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> findAll() {
        List<ReservationTimeResponse> responses = service.findAll();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<ReservationTimeResponse>> findAvailableTimes(
            @RequestParam UUID themeId,
            @RequestParam LocalDate date
    ) {
        List<ReservationTimeResponse> responses = service.findAvailableTimes(
                EntityId.fromUuid(themeId),
                date
        );

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
