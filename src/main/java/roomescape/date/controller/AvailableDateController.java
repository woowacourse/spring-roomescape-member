package roomescape.date.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.date.dto.response.AvailableDateDetailDto;
import roomescape.date.service.AvailableDateService;

@RestController
public class AvailableDateController {
    private final AvailableDateService availableDateService;

    public AvailableDateController(AvailableDateService availableDateService) {
        this.availableDateService = availableDateService;
    }

    @GetMapping("/dates")
    public ResponseEntity<List<AvailableDateDetailDto>> getAvailableDates() {
        List<AvailableDateDetailDto> responseData = availableDateService.readAvailableDates()
                .stream()
                .map(AvailableDateDetailDto::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }
}
