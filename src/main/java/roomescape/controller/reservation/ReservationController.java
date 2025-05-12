package roomescape.controller.reservation;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.annotation.LoginCustomer;
import roomescape.controller.member.dto.MemberInfoDto;
import roomescape.controller.reservation.dto.AdminReservationRequestDto;
import roomescape.controller.reservation.dto.MemberReservationRequestDto;
import roomescape.controller.reservation.dto.ReservationResponseDto;
import roomescape.service.ReservationService;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationResponseDto> reservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/reservations/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationResponseDto> searchReservations(
            @RequestParam(name = "themeId", required = false) Long themeId,
            @RequestParam(name = "memberId", required = false) Long memberId,
            @RequestParam(name = "dateFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(name = "dateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo
    ) {
        return reservationService.findReservationsByConditions(themeId, memberId, dateFrom, dateTo);
    }


    @PostMapping("/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponseDto addReservation(
            @RequestBody @Valid MemberReservationRequestDto memberReservationRequestDto,
            @LoginCustomer MemberInfoDto memberInfoDto) {
        return reservationService.saveReservation(memberReservationRequestDto, memberInfoDto);
    }

    @PostMapping("/admin/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponseDto addReservation(
            @RequestBody @Valid AdminReservationRequestDto adminReservationRequestDto) {
        return reservationService.saveReservation(adminReservationRequestDto);
    }

    @DeleteMapping("/reservations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable("id") Long id) {
        reservationService.cancelReservation(id);
    }
}
