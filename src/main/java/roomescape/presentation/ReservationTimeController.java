package roomescape.presentation;

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
import roomescape.business.dto.AvailableTimesResponseDto;
import roomescape.business.dto.ReservationTimeRequestDto;
import roomescape.business.dto.ReservationTimeResponseDto;
import roomescape.business.service.ReservationService;

@RestController
@RequestMapping("/times")
public final class ReservationTimeController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationTimeController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<AvailableTimesResponseDto>> readReservationAvailableTimes(
            @RequestParam("date") LocalDate date, @RequestParam("themeId") Long themeId) {
        List<AvailableTimesResponseDto> reservationTimes = reservationService.readAvailableTimes(date, themeId);
        return ResponseEntity.ok(reservationTimes);
    }

    @GetMapping("/{timeId}")
    public ResponseEntity<ReservationTimeResponseDto> readReservationTime(@PathVariable("timeId") Long id) {
        ReservationTimeResponseDto reservationTime = reservationService.readTimeOne(id);
        return ResponseEntity.ok(reservationTime);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponseDto> create(
            @Valid @RequestBody ReservationTimeRequestDto reservationTimeDto) {
        Long id = reservationService.createTime(reservationTimeDto);
        ReservationTimeResponseDto reservationTime = reservationService.readTimeOne(id);
        String location = "/times/" + id;
        return ResponseEntity.created(URI.create(location)).body(reservationTime);
    }

    @DeleteMapping("/{timeId}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable("timeId") Long id) {
        reservationService.deleteTime(id);
        return ResponseEntity.noContent().build();
    }
}
