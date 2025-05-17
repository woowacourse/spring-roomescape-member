package roomescape.controller.api.timeslot;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.controller.api.timeslot.dto.AddTimeSlotRequest;
import roomescape.controller.api.timeslot.dto.AvailabilityTimeSlotRequest;
import roomescape.controller.api.timeslot.dto.AvailabilityTimeSlotResponse;
import roomescape.controller.api.timeslot.dto.TimeSlotResponse;
import roomescape.service.TimeSlotService;

@Controller
@RequestMapping("/times")
public class TimeSlotApiController {

    private final TimeSlotService timeSlotService;

    @Autowired
    public TimeSlotApiController(final TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }

    @GetMapping
    public ResponseEntity<List<TimeSlotResponse>> findAllTimeSlots() {
        final List<TimeSlotResponse> response = timeSlotService.findAll();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<TimeSlotResponse> addTimeSlot(@RequestBody @Valid final AddTimeSlotRequest request) {
        final TimeSlotResponse response = timeSlotService.add(request);
        return ResponseEntity.created(URI.create("/times/" + response.id())).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeSlot(@PathVariable("id") final Long id) {
        final boolean isRemoved = timeSlotService.removeById(id);
        if (isRemoved) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/availability")
    public ResponseEntity<List<AvailabilityTimeSlotResponse>> findAvailabilityTimeSlots(
            @Valid final AvailabilityTimeSlotRequest request) {
        final List<AvailabilityTimeSlotResponse> response = timeSlotService.findAvailableTimeSlots(request.date(),
                request.themeId());
        return ResponseEntity.ok().body(response);
    }
}
