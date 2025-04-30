package roomescape.controller.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import roomescape.controller.api.dto.request.CreateReservationThemeRequest;
import roomescape.controller.api.dto.response.ReservationThemeResponse;
import roomescape.service.ReservationThemeService;
import roomescape.service.dto.response.ReservationThemeServiceResponse;

@RestController
@RequestMapping("/themes")
@RequiredArgsConstructor
public class ReservationThemeController {

    private final ReservationThemeService reservationThemeService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ReservationThemeResponse create(@RequestBody CreateReservationThemeRequest request) {
        ReservationThemeServiceResponse response = reservationThemeService.create(request.toServiceRequest());
        return ReservationThemeResponse.from(response);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ReservationThemeResponse> getAll() {
        List<ReservationThemeServiceResponse> reservationThemes = reservationThemeService.getAll();
        return reservationThemes.stream()
                .map(ReservationThemeResponse::from)
                .toList();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        reservationThemeService.delete(id);
    }
}
