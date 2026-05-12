package roomescape.reservationtime.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationtime.controller.dto.ReservationTimeCreateRequest;
import roomescape.reservationtime.controller.dto.ReservationTimeResponse;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.reservationtime.service.dto.ReservationTimeResult;

@RestController
@RequestMapping("/admin/themes")
@RequiredArgsConstructor
public class ReservationTimeAdminController {

    private final ReservationTimeService reservationTimeService;

    @GetMapping("/{themeId}/times")
    public List<ReservationTimeResponse> read(@PathVariable Long themeId) {
        return reservationTimeService.findAllByThemeId(themeId).stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @PostMapping("/{themeId}/times")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationTimeResponse create(
            @PathVariable Long themeId,
            @RequestBody ReservationTimeCreateRequest request
    ) {
        ReservationTimeResult reservationTimeResult =
                reservationTimeService.save(request.startAt(), themeId);

        return ReservationTimeResponse.from(reservationTimeResult);
    }

    @DeleteMapping("/times/{timeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long timeId) {
        reservationTimeService.deleteById(timeId);
    }

}
