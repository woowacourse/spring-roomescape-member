package roomescape.closeddate.controller;

import static org.springframework.http.HttpStatus.CREATED;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.closeddate.dto.request.ClosedDateSaveDto;
import roomescape.closeddate.dto.response.ClosedDateDetailDto;
import roomescape.closeddate.service.ClosedDateService;

@RestController
@RequestMapping("/admin")
public class ClosedDateAdminController {
    private final ClosedDateService closedDateService;

    public ClosedDateAdminController(ClosedDateService closedDateService) {
        this.closedDateService = closedDateService;
    }

    @GetMapping("/closed-dates")
    @Operation(summary = "Get all closed dates", description = "휴무일 전체 목록을 조회하는 api")
    public ResponseEntity<List<ClosedDateDetailDto>> getClosedDates() {
        List<ClosedDateDetailDto> responseData = closedDateService.readClosedDates().stream()
                .map(ClosedDateDetailDto::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/closed-dates")
    @Operation(summary = "Create a closed date", description = "휴무일을 등록하는 api")
    public ResponseEntity<ClosedDateDetailDto> createClosedDate(
            @Valid @RequestBody ClosedDateSaveDto dto
    ) {
        ClosedDateDetailDto responseData = ClosedDateDetailDto.from(closedDateService.register(dto.date()));
        return ResponseEntity.status(CREATED).body(responseData);
    }

    @DeleteMapping("/closed-dates/{id}")
    @Operation(summary = "Delete a closed date", description = "휴무일을 삭제하는 api")
    public ResponseEntity<ClosedDateDetailDto> deleteClosedDate(@PathVariable Long id) {
        ClosedDateDetailDto responseData = ClosedDateDetailDto.from(closedDateService.deregister(id));
        return ResponseEntity.ok(responseData);
    }
}
