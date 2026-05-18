package roomescape.controller.api;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import roomescape.common.dto.ApiResponse;
import roomescape.domain.reservationtime.dto.ReservationTimeCreateRequest;
import roomescape.domain.reservationtime.dto.ReservationTimeResponse;
import roomescape.domain.reservationtime.dto.ReservationTimeUpdateRequest;
import roomescape.service.ReservationTimeService;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/admin/times")
@RestController
public class AdminReservationTimeRestController {

    private final ReservationTimeService reservationTimeService;

    public AdminReservationTimeRestController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ApiResponse<ReservationTimeResponse> create(@Valid @RequestBody ReservationTimeCreateRequest reservationTimeReq) {
        return new ApiResponse<>(reservationTimeService.create(reservationTimeReq));
    }

    @GetMapping
    public ApiResponse<List<ReservationTimeResponse>> read(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Long themeId
    ) {
        return new ApiResponse<>(reservationTimeService.read(date, themeId));
    }

    @PutMapping("/{id}")
    public ApiResponse<ReservationTimeResponse> update(@PathVariable Long id, @Valid @RequestBody ReservationTimeUpdateRequest newReservationTimeReq) {
        return new ApiResponse<>(reservationTimeService.update(id, newReservationTimeReq));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        reservationTimeService.delete(id);
        return new ApiResponse<>(null);
    }
}
