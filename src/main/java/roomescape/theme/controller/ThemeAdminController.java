package roomescape.theme.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.controller.dto.ThemeCreateRequest;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;

@RestController
@RequestMapping("/admin/themes")
public class ThemeAdminController {

    private final ThemeService themeService;

    public ThemeAdminController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<Theme>> read() {
        List<Theme> themes = themeService.getAll();
        return ResponseEntity.ok()
                .body(themes);
    }

    @PostMapping
    public ResponseEntity<Theme> create(@RequestBody final ThemeCreateRequest themeCreateRequest) {
        Theme theme = themeService.save(
                themeCreateRequest.name(),
                themeCreateRequest.description(),
                themeCreateRequest.thumbnailUrl());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(theme);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        themeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
