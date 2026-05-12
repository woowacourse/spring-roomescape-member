package roomescape.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.theme.PopularThemeCondition;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ReservationThemeCommand;
import roomescape.domain.theme.ReservationThemeWithCount;
import roomescape.dto.Response;
import roomescape.dto.theme.AddThemeRequest;
import roomescape.dto.theme.PopularConditionRequest;
import roomescape.dto.theme.PopularThemeResponse;
import roomescape.dto.theme.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<Response> getThemes() {
        List<Theme> themes = themeService.getAllTheme();
        List<ThemeResponse> themeResponses = themes.stream()
                .map(ThemeResponse::from)
                .toList();

        return ResponseEntity.ok(Response.from(HttpStatus.OK.value(), themeResponses));
    }

    @PostMapping
    public ResponseEntity<Response> addTheme(@RequestBody @Valid AddThemeRequest addThemeRequest) {
        ReservationThemeCommand reservationThemeCommand = addThemeRequest.from();
        Theme addedTheme = themeService.addTheme(reservationThemeCommand);

        return new ResponseEntity<>(Response.from(HttpStatus.CREATED.value(), ThemeResponse.from(addedTheme)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable long id) {
        themeService.deleteTheme(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/popular", params = {"startDate", "endDate", "size"})
    public ResponseEntity<Response> getPopularTheme(@ModelAttribute @Valid PopularConditionRequest popularConditionRequest) {
        PopularThemeCondition popularThemeCondition = popularConditionRequest.to();
        List<ReservationThemeWithCount> reservationThemeWithCounts = themeService.getPopularTheme(popularThemeCondition);
        List<PopularThemeResponse> reservationThemeResponses = reservationThemeWithCounts.stream()
                .map(PopularThemeResponse::from)
                .toList();

        return ResponseEntity.ok(Response.from(HttpStatus.OK.value(), reservationThemeResponses));
    }
}
