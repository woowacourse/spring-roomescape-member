package roomescape.date.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.date.dto.response.AvailableDateDetailDto;
import roomescape.date.service.AvailableDateService;

@RestController
@RequestMapping("/avilable-dates")
public class AvailableDateController {
    private final AvailableDateService availableDateService;

    public AvailableDateController(AvailableDateService availableDateService) {
        this.availableDateService = availableDateService;
    }

    @GetMapping
    @Operation(summary = "Get all available dates", description = "예약 가능한 날짜 전체 목록을 조회하는 api")
    public ResponseEntity<List<AvailableDateDetailDto>> getAvailableDates() {
        List<AvailableDateDetailDto> responseData = availableDateService.findAvailableDates()
                .stream()
                .map(AvailableDateDetailDto::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }
}
