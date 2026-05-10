package roomescape.presentation;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.application.ThemeService;
import roomescape.entity.Theme;
import roomescape.entity.ThemeSortType;
import roomescape.global.auth.Admin;
import roomescape.presentation.dto.ThemeRequest;
import roomescape.presentation.dto.ThemeResponse;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService service;

    public ThemeController(ThemeService service) {
        this.service = service;
    }

    @Admin
    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(
            @RequestBody ThemeRequest request
    ) {
        Theme saved = service.save(
                request.name(),
                request.description(),
                request.thumbnail()
        );
        ThemeResponse result = ThemeResponse.from(saved);
        return ResponseEntity.created(parseCreatedResourceURI(result))
                .body(result);
    }

    private URI parseCreatedResourceURI(ThemeResponse result) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.id())
                .toUri();
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> readThemes() {
        List<Theme> result = service.findAll();
        return ResponseEntity.ok(convertToThemeResponses(result));
    }

    @GetMapping(params = {"sortType", "from", "to", "limit"})
    public ResponseEntity<List<ThemeResponse>> readTopNThemesByPeriod(
            @RequestParam ThemeSortType sortType,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            @RequestParam(required = false) Long limit
    ) {
        Long countLimit = defineLimitCount(limit);
        List<Theme> result = service.findTopNByPeriod(from, to, sortType, countLimit);
        return ResponseEntity.ok(convertToThemeResponses(result));
    }

    private List<ThemeResponse> convertToThemeResponses(List<Theme> result) {
        return result.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    private Long defineLimitCount(Long limit) {
        Long countLimit = 10L;
        if (limit != null) {
            countLimit = limit;
        }
        return countLimit;
    }

    @Admin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
