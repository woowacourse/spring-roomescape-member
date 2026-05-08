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
import roomescape.domain.ReservationTheme.PopularThemeCondition;
import roomescape.domain.ReservationTheme.ReservationTheme;
import roomescape.domain.ReservationTheme.ReservationThemeCommand;
import roomescape.domain.ReservationTheme.ReservationThemeWithCount;
import roomescape.dto.theme.AddThemeRequest;
import roomescape.dto.theme.PopularConditionRequest;
import roomescape.dto.theme.PopularReservationThemeResponse;
import roomescape.dto.theme.ReservationThemeResponse;
import roomescape.service.ReservationThemeService;

@RestController
@RequestMapping("/themes")
public class ReservationThemeController {
    private final ReservationThemeService reservationThemeService;

    public ReservationThemeController(ReservationThemeService reservationThemeService) {
        this.reservationThemeService = reservationThemeService;
    }

    @GetMapping()
    public ResponseEntity<List<ReservationThemeResponse>> getThemes() {
        List<ReservationTheme> themes = reservationThemeService.getAllTheme();
        List<ReservationThemeResponse> themeResponses = themes.stream()
                .map(ReservationThemeResponse::from)
                .toList();

        return new ResponseEntity<>(themeResponses, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ReservationThemeResponse> addTheme(@RequestBody @Valid AddThemeRequest addThemeRequest) {
        ReservationThemeCommand reservationThemeCommand = addThemeRequest.from();
        ReservationTheme addedReservationTheme = reservationThemeService.addTheme(reservationThemeCommand);

        return new ResponseEntity<>(ReservationThemeResponse.from(addedReservationTheme), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable long id) {
        reservationThemeService.deleteTheme(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/popular", params = {"start_date", "end_date", "size"})
    public ResponseEntity<List<PopularReservationThemeResponse>> getPopularTheme(@ModelAttribute @Valid PopularConditionRequest popularConditionRequest) {
        PopularThemeCondition popularThemeCondition = popularConditionRequest.to();
        List<ReservationThemeWithCount> reservationThemeWithCounts = reservationThemeService.getPopularTheme(popularThemeCondition);
        List<PopularReservationThemeResponse> reservationThemeResponses = reservationThemeWithCounts.stream()
                .map(PopularReservationThemeResponse::from)
                .toList();

        return new ResponseEntity<>(reservationThemeResponses, HttpStatus.OK);
    }
}
