package roomescape.reservation.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.application.AdminReservationService;
import roomescape.reservation.application.dto.response.ReservationServiceResponse;
import roomescape.reservation.controller.dto.response.ReservationResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final AdminReservationService adminReservationService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ReservationResponse> getAll() {
        List<ReservationServiceResponse> responses = adminReservationService.getAll();
        return responses.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        adminReservationService.delete(id);
    }
}
