package roomescape.reservation.presentation;

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
import roomescape.reservation.application.ThemeService;
import roomescape.reservation.presentation.dto.request.ThemeSaveRequest;
import roomescape.reservation.presentation.dto.response.ThemeFindResponse;
import roomescape.reservation.presentation.dto.response.ThemeSaveResponse;

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
    public ResponseEntity<List<ThemeFindResponse>> findByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<ThemeFindResponse> responses = themeService.findByDate(date);

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeFindResponse>> findByDayAndLimit(
            @RequestParam int day,
            @RequestParam int limit
    ) {
        List<ThemeFindResponse> responses = themeService.findByDayAndLimit(day,limit);

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
