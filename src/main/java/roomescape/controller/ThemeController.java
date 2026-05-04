package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
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
import roomescape.domain.Theme;
import roomescape.repository.dto.ReservedTheme;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;
    private final ThemeMapper themeMapper;

    public ThemeController(ThemeService themeService, ThemeMapper themeMapper) {
        this.themeService = themeService;
        this.themeMapper = themeMapper;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> findAll() {
        List<ThemeResponse> responses = themeService.findAll()
                .stream()
                .map(themeMapper::mapToResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> create(
            @RequestBody ThemeCreateRequest themeCreateRequest
    ) {
        Theme theme = themeService.create(themeCreateRequest);
        ThemeResponse response = themeMapper.mapToResponse(theme);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable long id
    ) {
        themeService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/most-reserved-themes")
    public ResponseEntity<List<ReservedTheme>> findMostReserved(
            @RequestParam int limit,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        List<ReservedTheme> responses = themeService.findMostReserved(limit, startDate, endDate);

        return ResponseEntity.ok(responses);
    }
}
