package roomescape.controller.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import roomescape.controller.api.dto.response.ReservationThemeResponse;
import roomescape.service.UserReservationThemeService;
import roomescape.service.dto.response.ReservationThemeServiceResponse;

@RestController
@RequestMapping("/themes")
@RequiredArgsConstructor
public class UserReservationThemeController {

    private final UserReservationThemeService userReservationThemeService;

    // TODO : Admin과 API를 공유하고 있다. 분리를 고민해보자.
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ReservationThemeResponse> getAll() {
        List<ReservationThemeServiceResponse> responses = userReservationThemeService.getAll();
        return responses.stream()
                .map(ReservationThemeResponse::from)
                .toList();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/popular")
    public List<ReservationThemeResponse> getPopularThemes(@RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<ReservationThemeServiceResponse> responses = userReservationThemeService.getPopularThemes(limit);
        return responses.stream()
                .map(ReservationThemeResponse::from)
                .toList();
    }
}
