package roomescape.reservationtime.controller;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationtime.dto.request.ReservationTimeRequest;
import roomescape.reservationtime.dto.response.AdminReservationTimePageResponse;
import roomescape.reservationtime.dto.response.CreateReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;

@RequestMapping("/admin/times")
@RestController
public class AdminReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public AdminReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<AdminReservationTimePageResponse> getReservationTimesByPage(
            @RequestParam(required = false, defaultValue = "1") int page) {
        AdminReservationTimePageResponse reservationTimesByPage = reservationTimeService.getReservationTimesByPage(
                page);
        return ResponseEntity.ok(reservationTimesByPage);
    }

    @PostMapping
    public ResponseEntity<CreateReservationTimeResponse> addTime(
            @RequestBody @Valid ReservationTimeRequest reservationTimeRequest) {
        CreateReservationTimeResponse newReservation = reservationTimeService.addTime(reservationTimeRequest);
        Long id = newReservation.id();
        return ResponseEntity.created(URI.create("/time/" + id)).body(newReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable(name = "id") Long id) {
        reservationTimeService.deleteTime(id);
        return ResponseEntity.noContent().build();
    }
}
