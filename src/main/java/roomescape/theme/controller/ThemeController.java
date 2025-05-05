package roomescape.theme.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.domain.dto.PopularThemeRequestDto;
import roomescape.theme.domain.dto.ThemeReqDto;
import roomescape.theme.domain.dto.ThemeResDto;
import roomescape.theme.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResDto>> findAll() {
        return ResponseEntity.ok(themeService.findAll());
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<ThemeResDto>> findThemesOrderByReservationCount2(
            @ModelAttribute PopularThemeRequestDto popularThemeRequestDto) {
        LocalDate now = LocalDate.now();
        LocalDate from = now.minusDays(7);
        LocalDate to = now.minusDays(1);
        List<ThemeResDto> topRankThemes = themeService.findThemesOrderByReservationCount(from, to,
                popularThemeRequestDto);
        return ResponseEntity.ok(topRankThemes);
    }

    @PostMapping
    public ResponseEntity<ThemeResDto> add(
            @RequestBody ThemeReqDto reqDto
    ) {
        ThemeResDto resDto = themeService.add(reqDto);
        return ResponseEntity.created(URI.create("/themes/" + resDto.id())).body(resDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable("id") Long id
    ) {
        themeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
