package roomescape.controller.timeslot;

import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.controller.timeslot.dto.CreateTimeSlotRequest;
import roomescape.controller.timeslot.dto.TimeSlotResponse;
import roomescape.service.TimeSlotService;

@Controller
@RequestMapping("/times")
public class TimeSlotController {

    private final TimeSlotService service;

    @Autowired
    public TimeSlotController(final TimeSlotService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TimeSlotResponse> add(@RequestBody CreateTimeSlotRequest request) {
        // TODO : 시간 바인딩 예외 응답 처리하기
        var timeSlot = service.add(request.startAt());
        var response = TimeSlotResponse.from(timeSlot);
        return ResponseEntity.created(URI.create("/times/" + timeSlot.id())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TimeSlotResponse>> allTimeSlots() {
        var timeSlots = service.allTimeSlots();
        var response = TimeSlotResponse.from(timeSlots);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boolean isRemoved = service.removeById(id);
        if (isRemoved) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
