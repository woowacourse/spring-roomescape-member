package roomescape.controller.api;

import java.time.LocalDate;
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

import lombok.RequiredArgsConstructor;
import roomescape.controller.api.dto.request.CreateReservationTimeRequest;
import roomescape.controller.api.dto.response.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.response.ReservationTimeServiceResponse;

@RestController
@RequestMapping("/times")
@RequiredArgsConstructor
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ReservationTimeResponse create(
            @RequestBody CreateReservationTimeRequest request) {
        ReservationTimeServiceResponse response = reservationTimeService.create(request.toServiceRequest());
        return ReservationTimeResponse.from(response);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ReservationTimeResponse> getAllByThemeIdAndDate(
            @RequestParam(value = "themeId", required = false) Long themeId,
            @RequestParam(value = "date", required = false) LocalDate date
    ) {
        List<ReservationTimeServiceResponse> reservationTimes = getAllReservationTimeResponse(themeId, date);
        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    private List<ReservationTimeServiceResponse> getAllReservationTimeResponse(Long themeId, LocalDate date) {
        if (themeId == null && date == null) {
            return reservationTimeService.getAll();
        }
        return reservationTimeService.getAllByThemeIdAndDate(themeId, date);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        reservationTimeService.delete(id);
    }
}
