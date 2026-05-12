package roomescape.reservation.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.global.auth.UserInfo;
import roomescape.global.auth.annotation.CurrentUser;
import roomescape.reservation.controller.dto.ReservationCreateRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.controller.dto.ReservationUpdateRequest;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.ReservationResult;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationResponse> readByName(@CurrentUser UserInfo userInfo) {
        return reservationService.getAllByName(userInfo.name()).stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse create(@RequestBody ReservationCreateRequest request) {
        ReservationResult reservationResult = reservationService.save(
                request.name(),
                request.date(),
                request.timeId()
        );

        return ReservationResponse.from(reservationResult);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @CurrentUser UserInfo userInfo) {
        reservationService.deleteById(id, userInfo.name());
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(
            @PathVariable Long id,
            @CurrentUser UserInfo userInfo,
            @RequestBody ReservationUpdateRequest request
    ) {
        reservationService.update(
                id,
                userInfo.name(),
                request.date(),
                request.timeId()
        );
    }

}
