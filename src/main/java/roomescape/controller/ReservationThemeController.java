package roomescape.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import roomescape.dto.request.CreateReservationThemeRequest;
import roomescape.dto.response.ReservationThemeResponse;
import roomescape.service.ReservationThemeService;

@RestController
@RequestMapping("/themes")
@RequiredArgsConstructor
public class ReservationThemeController {

    private final ReservationThemeService reservationThemeService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ReservationThemeResponse create(@RequestBody @Valid CreateReservationThemeRequest request) {
        return reservationThemeService.create(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ReservationThemeResponse> getAll() {
        return reservationThemeService.getAll();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        reservationThemeService.delete(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/popular")
    public List<ReservationThemeResponse> getPopularThemes(@RequestParam(value = "limit", defaultValue = "10") int limit) {
        return reservationThemeService.getPopularThemes(limit);
    }
}
