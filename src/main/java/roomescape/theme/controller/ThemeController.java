package roomescape.theme.controller;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.dto.PopularThemeResponse;
import roomescape.theme.dto.ThemeCreateRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;

@RequiredArgsConstructor
@RequestMapping("/themes")
@RestController
public class ThemeController {

    private final ThemeService themeService;

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> findAll() {
        return ResponseEntity.ok(themeService.findAll());
    }

    @GetMapping("/popular-top-10")
    public ResponseEntity<List<PopularThemeResponse>> findPopularThemes() {
        return ResponseEntity.ok(themeService.findPopularThemes(LocalDate.now()));
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> create(
            @RequestBody ThemeCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(themeService.save(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        int deletedCount = themeService.delete(id);

        if (deletedCount == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
