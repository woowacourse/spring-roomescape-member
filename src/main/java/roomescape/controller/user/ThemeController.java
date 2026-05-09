package roomescape.controller.user;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dao.row.AvailableTimeRow;
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
            @RequestParam(defaultValue = "10") @Min(1) @Max(15) int limit,
            @RequestParam(defaultValue = "7")  @Min(1) @Max(10) int days,
            @RequestParam(required = false) LocalDate date) {
        return ResponseEntity.ok(themeService.findPopulars(limit, days, date));
    }
}
