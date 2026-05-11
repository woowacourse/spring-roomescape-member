package roomescape.theme;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.dto.request.ThemeSaveRequest;
import roomescape.theme.dto.response.ThemeFindResponse;
import roomescape.theme.dto.response.ThemeSaveResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/themes")
@RequiredArgsConstructor
public class ThemeController {
    private final ThemeService themeService;

    @PostMapping
    public ResponseEntity<ThemeSaveResponse> save(@RequestBody ThemeSaveRequest body) {
        ThemeSaveResponse response = themeService.save(body);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        themeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<ThemeFindResponse>> findAll() {
        List<ThemeFindResponse> responses = themeService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping(params = "date")
    public ResponseEntity<List<ThemeFindResponse>> findScheduledThemesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<ThemeFindResponse> responses = themeService.findScheduledThemesByDate(date);

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeFindResponse>> findByDayAndLimit() {
        List<ThemeFindResponse> responses = themeService.findPopularTheme();

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
