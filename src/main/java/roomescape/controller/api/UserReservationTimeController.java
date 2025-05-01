package roomescape.controller.api;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import roomescape.controller.api.dto.response.ReservationTimeResponse;
import roomescape.service.UserReservationTimeService;
import roomescape.service.dto.response.ReservationTimeServiceResponse;

@RestController
@RequestMapping("/times")
@RequiredArgsConstructor
public class UserReservationTimeController {

    private final UserReservationTimeService userReservationTimeService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ReservationTimeResponse> getAll(
            @RequestParam("themeId") Long themeId,
            @RequestParam("date") LocalDate date
    ) {
        List<ReservationTimeServiceResponse> responses = userReservationTimeService.getAllByThemeIdAndDate(themeId, date);
        return responses.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }
}
