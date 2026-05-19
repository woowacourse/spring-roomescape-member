package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.ThemeCreateRequest;
import roomescape.controller.dto.ThemeResponse;
import roomescape.controller.mapper.ThemeMapper;
import roomescape.domain.Duration;
import roomescape.domain.EntityId;
import roomescape.repository.dto.ReservedTheme;
import roomescape.service.ThemeService;
import roomescape.service.dto.ThemeCreateCommand;

@RestController
@RequestMapping("/themes")
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService service;
    private final ThemeMapper mapper;

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> findAll() {
        List<ThemeResponse> responses = service.findAll();

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> create(
            @RequestBody ThemeCreateRequest createRequest
    ) {
        ThemeCreateCommand createCommand = mapper.mapToCommand(createRequest);
        ThemeResponse response = service.create(createCommand);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {
        service.delete(EntityId.fromUuid(id));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/most-reserved")
    public ResponseEntity<List<ReservedTheme>> findMostReserved(
            @RequestParam int limit,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        Duration duration = new Duration(startDate, endDate);
        List<ReservedTheme> responses = service.findMostReserved(limit, duration);

        return ResponseEntity.ok(responses);
    }
}
