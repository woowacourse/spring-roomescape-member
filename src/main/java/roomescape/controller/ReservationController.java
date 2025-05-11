package roomescape.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Member;
import roomescape.dto.request.AdminReservationRequestDto;
import roomescape.dto.request.ReservationRequestDto;
import roomescape.dto.response.AdminReservationResponse;
import roomescape.dto.response.ReservationResponseDto;
import roomescape.service.ReservationService;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponseDto> readAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/members/{memberId}/themes/{themeId}/reservations")
    public List<ReservationResponseDto> readAllReservationsOf(
        @RequestParam String dateFrom,
        @RequestParam String dateTo,
        @PathVariable Long memberId,
        @PathVariable Long themeId) {
        return reservationService.getAllReservationsOf(dateFrom, dateTo, memberId, themeId);
    }

    @PostMapping("/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponseDto createReservation(
        @RequestBody ReservationRequestDto reservationRequestDto, Member member) {
        return reservationService.saveReservationOfMember(reservationRequestDto, member.getId());
    }

    @PostMapping("admin/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public AdminReservationResponse createReservationOfAdmin(
        @RequestBody AdminReservationRequestDto adminReservationRequestDto, Member member) {
        return reservationService.saveReservationOfAdmin(adminReservationRequestDto,
            member.getId());
    }

    @DeleteMapping("/reservations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable(name = "id") Long id) {
        reservationService.deleteReservation(id);
    }
}
