package roomescape.controller.user;


import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.dto.response.AvailableTimeResponseDto;
import roomescape.dto.response.ThemeResponseDto;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponseDto>> findAll() {
        List<Theme> themes = themeService.findAll();
        return ResponseEntity.ok(themes.stream()
                .map(ThemeResponseDto::from)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThemeResponseDto> findById(@PathVariable Long id) {
        Theme themeById = themeService.findById(id);
        return ResponseEntity.ok(ThemeResponseDto.from(themeById));
    }

    @GetMapping("/{id}/reservations/times")
    public ResponseEntity<List<AvailableTimeResponseDto>> findAvailableTimesById(@PathVariable Long themeId,
                                                                                 @RequestParam LocalDate localDate) {
        return ResponseEntity.ok(themeService.findAvailableTimesById(themeId, localDate));
    }
}
