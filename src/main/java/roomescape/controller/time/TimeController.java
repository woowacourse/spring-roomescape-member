package roomescape.controller.time;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import roomescape.controller.time.dto.AvailabilityTimeResponse;
import roomescape.controller.time.dto.ReadTimeResponse;
import roomescape.controller.time.dto.TimeRequest;
import roomescape.service.TimeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final TimeService timeService;

    public TimeController(final TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping
    public List<ReadTimeResponse> getTimes() { //TODO 시간순으로 정렬해서 보여주기
        return timeService.getTimes();
    }

    @GetMapping("/availability") //TODO api 명세 수정
    public List<AvailabilityTimeResponse> getAvailableTimes(@RequestParam(value = "date") final String date,
                                                            @RequestParam(value = "themeId") final String themeId) {
        //TODO long으로 변경
        return timeService.getTimeAvailable(date, themeId);
    }

    @PostMapping
    public ResponseEntity<AvailabilityTimeResponse> addTime(@RequestBody final TimeRequest timeRequest) {
        final AvailabilityTimeResponse time = timeService.addTime(timeRequest);
        final URI uri = UriComponentsBuilder.fromPath("/reservations/{id}")
                .buildAndExpand(time.id())
                .toUri();

        return ResponseEntity.created(uri)
                .body(time);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable("id") final Long id) {
        final int deleteCount = timeService.deleteTime(id);
        if (deleteCount == 0) {
            return ResponseEntity.notFound()
                    .build();
        }
        return ResponseEntity.noContent()
                .build();
    }
}
