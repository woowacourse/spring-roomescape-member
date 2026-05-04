package roomescape.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme.Theme;
import roomescape.domain.Theme.ThemeCommand;
import roomescape.dto.theme.AddThemeRequest;
import roomescape.dto.theme.AddThemeResponse;
import roomescape.dto.theme.AllThemeResponse;
import roomescape.service.ReservationThemeService;

@RestController
@RequestMapping("/themes")
public class ReservationThemeController {
    private final ReservationThemeService reservationThemeService;

    public ReservationThemeController(ReservationThemeService reservationThemeService) {
        this.reservationThemeService = reservationThemeService;
    }

    @GetMapping()
    public ResponseEntity<AllThemeResponse> getThemes() {
        List<Theme> themes = reservationThemeService.getAllTheme();

        return new ResponseEntity<>(new AllThemeResponse(themes), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<AddThemeResponse> addTheme(@RequestBody @Valid AddThemeRequest addThemeRequest) {
        ThemeCommand themeCommand = addThemeRequest.from();
        Theme addedTheme = reservationThemeService.addTheme(themeCommand);

        return new ResponseEntity<>(new AddThemeResponse(addedTheme), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable long id) {
        reservationThemeService.deleteTheme(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
