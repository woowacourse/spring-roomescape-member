package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.TimeRequest;
import roomescape.controller.dto.TimeResponse;
import roomescape.domain.TimeSlot;
import roomescape.service.TimeSlotService;
import roomescape.service.dto.AvailableTimeSlot;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final TimeSlotService reservationTimeSlotService;

    public TimeController(TimeSlotService reservationTimeSlotService) {
        this.reservationTimeSlotService = reservationTimeSlotService;
    }

    @GetMapping
    public ResponseEntity<List<TimeResponse>> times() {
        return ResponseEntity.ok(convertToTimeResponses(reservationTimeSlotService.allTimes()));
    }

    @GetMapping(params = {"themeId", "date"})
    public ResponseEntity<List<TimeResponse>> getAvailableTimes(long themeId, LocalDate date) {
        List<AvailableTimeSlot> availableSlots = reservationTimeSlotService.findAvailableTimes(themeId, date);
        return ResponseEntity.ok(convertToAvailableResponses(availableSlots));
    }

    @PostMapping
    public ResponseEntity<TimeResponse> createTime(
            @RequestBody TimeRequest timeRequest) {
        TimeSlot timeSlot = reservationTimeSlotService.saveTime(timeRequest.startAt());
        return ResponseEntity.ok(TimeResponse.from(timeSlot));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable long id) {
        reservationTimeSlotService.removeTime(id);
        return ResponseEntity.ok().build();
    }

    private List<TimeResponse> convertToTimeResponses(List<TimeSlot> reservationTimeSlots) {
        return reservationTimeSlots.stream()
                .map(TimeResponse::from)
                .toList();
    }

    private List<TimeResponse> convertToAvailableResponses(List<AvailableTimeSlot> availableSlots) {
        return availableSlots.stream()
                .map(TimeResponse::from)
                .toList();
    }
}
