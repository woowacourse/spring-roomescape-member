package roomescape.presentation.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.presentation.api.dto.request.CreateReservationThemeRequest;
import roomescape.presentation.api.dto.response.ReservationThemeResponse;
import roomescape.application.AdminReservationThemeService;
import roomescape.application.dto.response.ReservationThemeServiceResponse;

@RestController
@RequestMapping("/admin/themes")
@RequiredArgsConstructor
public class AdminReservationThemeController {

    private final AdminReservationThemeService adminReservationThemeService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ReservationThemeResponse create(@RequestBody CreateReservationThemeRequest request) {
        ReservationThemeServiceResponse response = adminReservationThemeService.create(request.toServiceRequest());
        return ReservationThemeResponse.from(response);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        adminReservationThemeService.delete(id);
    }
}
