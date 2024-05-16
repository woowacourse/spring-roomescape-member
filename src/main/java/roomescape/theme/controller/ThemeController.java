package roomescape.theme.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.SaveThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

  private final ThemeService themeService;

  public ThemeController(final ThemeService themeService) {
    this.themeService = themeService;
  }

  @GetMapping
  public List<ThemeResponse> getThemes() {
    return themeService.getThemes()
        .stream()
        .map(ThemeResponse::from)
        .toList();
  }

  @PostMapping
  public ResponseEntity<ThemeResponse> saveTheme(@RequestBody final SaveThemeRequest request) {
    final Theme savedTheme = themeService.saveTheme(request);

    return ResponseEntity.created(URI.create("/themes/" + savedTheme.getId()))
        .body(ThemeResponse.from(savedTheme));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTheme(@PathVariable final Long id) {
    themeService.deleteTheme(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/popular-themes")
  public List<ThemeResponse> getPopularThemes() {
    return themeService.getPopularThemes()
        .stream()
        .map(ThemeResponse::from)
        .toList();
  }
}
