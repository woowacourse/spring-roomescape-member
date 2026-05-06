package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ThemeResponseDto;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponseDto>> readAll() {
        List<ThemeResponseDto> themeResponseDtos = themeService.readAll();
        return ResponseEntity.ok(themeResponseDtos);
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<ThemeResponseDto>> readRanking(
            @RequestParam("start-date") LocalDate startDate,
            @RequestParam("end-date") LocalDate endDate
    ) {
        List<ThemeResponseDto> responseDtos = themeService.readRanking(startDate, endDate);
        return ResponseEntity.ok(responseDtos);
    }
}
