package roomescape.domain.reservation.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.auth.config.AuthenticationPrincipal;
import roomescape.domain.auth.dto.LoginUserDto;
import roomescape.domain.reservation.dto.reservation.ReservationCreateDto;
import roomescape.domain.reservation.dto.reservation.ReservationResponse;
import roomescape.domain.reservation.dto.reservation.ReservationUserCreateRequest;
import roomescape.domain.reservation.dto.reservationtime.BookedReservationTimeResponse;
import roomescape.domain.reservation.service.ReservationService;

@RequestMapping("/reservations")
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/available")
    public ResponseEntity<List<BookedReservationTimeResponse>> readAvailableReservationTimes(
            @RequestParam("date") final LocalDate date, @RequestParam("themeId") final Long themeId) {
        final List<BookedReservationTimeResponse> responses = reservationService.getAvailableTimes(date, themeId);

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody final ReservationUserCreateRequest request,
                                                      @AuthenticationPrincipal final LoginUserDto userDto) {
        final ReservationCreateDto createDto = new ReservationCreateDto(userDto.id(), request.timeId(),
                request.themeId(), request.date());

        final ReservationResponse response = reservationService.create(createDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final Long id) {
        reservationService.delete(id);

        return ResponseEntity.noContent()
                .build();
    }
}
