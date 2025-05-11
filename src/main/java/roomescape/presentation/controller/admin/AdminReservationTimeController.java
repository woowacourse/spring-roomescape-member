package roomescape.presentation.controller.admin;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.presentation.dto.response.AvailableTimesResponseDto;
import roomescape.presentation.dto.request.ReservationTimeRequestDto;
import roomescape.presentation.dto.response.ReservationTimeResponseDto;
import roomescape.business.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public final class AdminReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    @Autowired
    public AdminReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponseDto>> readReservationTimes() {
        List<ReservationTimeResponseDto> reservationTimes = reservationTimeService.readTimeAll();
        return ResponseEntity.ok(reservationTimes);
    }

    @GetMapping("/reservation")
    public ResponseEntity<List<AvailableTimesResponseDto>> readReservationAvailableTimes(
            @RequestParam("date") LocalDate date, @RequestParam("themeId") Long themeId) {
        List<AvailableTimesResponseDto> availableReservationTimes = reservationTimeService.readAvailableTimes(date,
                themeId);
        return ResponseEntity.ok(availableReservationTimes);
    }

    @GetMapping("/{timeId}")
    public ResponseEntity<ReservationTimeResponseDto> readReservationTime(@PathVariable("timeId") Long id) {
        ReservationTimeResponseDto reservationTime = reservationTimeService.readTimeOne(id);
        return ResponseEntity.ok(reservationTime);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponseDto> create(
            @Valid @RequestBody ReservationTimeRequestDto reservationTimeDto) {
        Long id = reservationTimeService.createTime(reservationTimeDto);
        ReservationTimeResponseDto reservationTime = reservationTimeService.readTimeOne(id);
        String location = "/times/" + id;
        return ResponseEntity.created(URI.create(location)).body(reservationTime);
    }

    @DeleteMapping("/{timeId}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable("timeId") Long id) {
        reservationTimeService.deleteTime(id);
        return ResponseEntity.noContent().build();
    }
}
