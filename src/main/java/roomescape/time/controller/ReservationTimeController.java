package roomescape.time.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import roomescape.time.domain.ReservationUserTime;
import roomescape.time.dto.ReservationTimeRequestDto;
import roomescape.time.dto.ReservationTimeResponseDto;
import roomescape.time.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
@Validated
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponseDto> save(@RequestBody @Valid final ReservationTimeRequestDto request) {
        final ReservationTimeResponseDto responseDto = new ReservationTimeResponseDto(reservationTimeService.save(request));
        return ResponseEntity.created(URI.create("/times/" + responseDto.id())).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponseDto>> findAll() {
        final List<ReservationTimeResponseDto> responseDtos = reservationTimeService.findAll().stream()
                .map(ReservationTimeResponseDto::new)
                .toList();
        return ResponseEntity.ok(responseDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id")
            @Min(value = 1, message = "올바른 시간 ID를 입력해야 합니다.") final long id) {
        reservationTimeService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/available")
    public List<ReservationUserTime> findAvailableTime(
            @NotBlank(message = "날짜를 입력해야 합니다.") @RequestParam("date") final String date,
            @Min(value = 1, message = "올바른 테마 ID를 입력해야 합니다.") @RequestParam("themeId") final long themeId) {
        return reservationTimeService.findAvailableTime(date, themeId);
    }
}
