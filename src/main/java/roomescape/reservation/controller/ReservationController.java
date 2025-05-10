package roomescape.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.utils.UriFactory;
import roomescape.member.auth.dto.MemberInfo;
import roomescape.reservation.controller.dto.AvailableReservationTimeWebResponse;
import roomescape.reservation.controller.dto.CreateReservationWebRequest;
import roomescape.reservation.controller.dto.ReservationWebResponse;
import roomescape.reservation.service.ReservationService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(ReservationController.BASE_PATH)
public class ReservationController {

    public static final String BASE_PATH = "/reservations";

    private final ReservationService reservationService;

    @GetMapping
    public List<ReservationWebResponse> getAll() {
        return reservationService.getAll();
    }

    @GetMapping("/times")
    public List<AvailableReservationTimeWebResponse> getAvailable(
            @RequestParam final LocalDate date,
            @RequestParam final Long themeId) {
        return reservationService.getAvailable(date, themeId);
    }

    @PostMapping
    public ResponseEntity<ReservationWebResponse> create(
            @RequestBody final CreateReservationWebRequest createReservationWebRequest,
            MemberInfo memberInfo) {
        final ReservationWebResponse reservationWebResponse = reservationService.create(createReservationWebRequest, memberInfo);
        final URI location = UriFactory.buildPath(BASE_PATH, String.valueOf(reservationWebResponse.id()));
        return ResponseEntity.created(location)
                .body(reservationWebResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
