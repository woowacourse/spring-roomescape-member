package roomescape.theme.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.availability.service.AvailabilityService;
import roomescape.theme.controller.dto.ThemeAvailableTimeResponseDto;
import roomescape.theme.controller.dto.ThemeResponseDto;
import roomescape.theme.controller.dto.ThemeSaveRequestDto;
import roomescape.theme.service.ThemeService;

@RestController
public class ThemeController {
    private final ThemeService themeService;
    private final AvailabilityService availabilityService;
    
    public ThemeController(ThemeService themeService, AvailabilityService availabilityService) {
        this.themeService = themeService;
        this.availabilityService = availabilityService;
    }
    
    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponseDto>> getAll() {
        List<ThemeResponseDto> body = themeService.getAll().stream()
                .map(ThemeResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(body);
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponseDto> create(
            @RequestBody ThemeSaveRequestDto themeRequest) {
        ThemeResponseDto body = ThemeResponseDto.from(
                themeService.create(themeRequest.toServiceDto()));
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("/themes/{themeId}/times")
    public ResponseEntity<List<ThemeAvailableTimeResponseDto>> getAvailableTimes(
            @PathVariable Long themeId,
            @RequestParam LocalDate date
    ) {
        List<ThemeAvailableTimeResponseDto> body = availabilityService.getAvailableTimes(themeId, date).stream()
                .map(ThemeAvailableTimeResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        themeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/themes/best")
    public ResponseEntity<List<ThemeResponseDto>> getBestThemes() {
        List<ThemeResponseDto> body = themeService.getBestThemes().stream()
                .map(ThemeResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(body);
    }
}
