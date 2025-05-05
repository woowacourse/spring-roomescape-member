package roomescape.controller.timeslot;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import roomescape.controller.timeslot.dto.AddTimeSlotRequest;
import roomescape.controller.timeslot.dto.AvailabilityTimeSlotRequest;
import roomescape.controller.timeslot.dto.TimeSlotResponse;
import roomescape.service.TimeSlotService;
import roomescape.controller.timeslot.dto.AvailabilityTimeSlotResponse;

@Controller
@RequestMapping("/times")
public class TimeSlotController {

    private final TimeSlotService service;

    @Autowired
    public TimeSlotController(final TimeSlotService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TimeSlotResponse>> findAllTimeSlots() {
        var timeSlots = service.findAll();
        return ResponseEntity.ok(timeSlots);
    }

    @PostMapping
    public ResponseEntity<TimeSlotResponse> addTimeSlot(@RequestBody @Valid final AddTimeSlotRequest request) {
        var timeSlot = service.add(request);
        return ResponseEntity.created(URI.create("/times/" + timeSlot.id())).body(timeSlot);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeSlot(@PathVariable("id") final Long id) {
        boolean isRemoved = service.removeById(id);
        if (isRemoved) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/availability")
    public ResponseEntity<List<AvailabilityTimeSlotResponse>> findAvailabilityTimeSlots(@Valid final AvailabilityTimeSlotRequest request) {
        var availableTimeSlots = service.findAvailableTimeSlots(request.date(), request.themeId());
        return ResponseEntity.ok(availableTimeSlots);
    }
}
