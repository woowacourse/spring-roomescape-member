package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.annotation.AuthenticationPrincipal;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.dto.AdminReservationRequestDto;
import roomescape.dto.ReservationRequestDto;
import roomescape.service.ReservationService;
import roomescape.service.ReservationThemeService;

import java.net.URI;
import java.util.List;

@Controller
public class UserController {

    private final ReservationService reservationService;
    private final ReservationThemeService reservationThemeService;

    public UserController(ReservationService reservationService, ReservationThemeService reservationThemeService) {
        this.reservationService = reservationService;
        this.reservationThemeService = reservationThemeService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> createReservation(@AuthenticationPrincipal Member member, @RequestBody ReservationRequestDto reservationRequestDto) {
        AdminReservationRequestDto adminReservationRequestDto = new AdminReservationRequestDto(reservationRequestDto.date(), reservationRequestDto.timeId(), reservationRequestDto.themeId(), member.getId());
        Reservation reservation = reservationService.insertReservation(adminReservationRequestDto);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @GetMapping("/best-themes")
    public ResponseEntity<List<ReservationTheme>> getBestThemes() {
        List<ReservationTheme> reservationThemes = reservationThemeService.getBestThemes();
        return ResponseEntity.ok().body(reservationThemes);
    }
}
