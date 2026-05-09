package roomescape.controller.user;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dao.row.AvailableTimeRow;
import roomescape.domain.Theme;
import roomescape.dto.request.PopularThemeRequestDto;
import roomescape.dto.response.ThemeResponseDto;
import roomescape.service.ThemeService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponseDto>> findAll() {
        return ResponseEntity.ok(themeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThemeResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(themeService.findById(id));
    }

    @GetMapping("/{themeId}/times")
    public ResponseEntity<List<AvailableTimeRow>> findAvailableTimesById(@PathVariable Long themeId,
                                                                         @RequestParam LocalDate localDate) {
        return ResponseEntity.ok(themeService.findAvailableTimesById(themeId, localDate));
    }

    @GetMapping("/populars")
    public ResponseEntity<List<ThemeResponseDto>> findPopulars(
            @Valid @ModelAttribute PopularThemeRequestDto popularThemeRequestDto) {
        return ResponseEntity.ok(themeService.findPopulars(popularThemeRequestDto)
                .stream()
                .map(ThemeResponseDto::from)
                .toList());
    }
}
