package roomescape.presentation.controller.admin;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.ReservationService;
import roomescape.presentation.dto.request.AdminReservationRequestDto;
import roomescape.presentation.dto.response.ReservationResponseDto;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final ReservationService reservationService;

    @Autowired
    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> readReservations(
            @RequestParam("themeId") Long themeId,
            @RequestParam("memberId") Long memberId,
            @RequestParam("dateFrom") LocalDate dateFrom,
            @RequestParam("dateTo") LocalDate dateTo) {
        List<ReservationResponseDto> reservations = reservationService.searchReservationsForThemeAndMemberAndDate(
                themeId,
                memberId,
                dateFrom,
                dateTo);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> create(
            @Valid @RequestBody AdminReservationRequestDto reservationDto) {
        Long id = reservationService.createReservationWithMember(reservationDto);
        ReservationResponseDto reservation = reservationService.readReservationOne(id);
        return ResponseEntity.ok().body(reservation);
    }
}
