package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.ReservationTime;
import roomescape.dto.ResourceIdResponseDto;
import roomescape.dto.reservationTime.AvailableReservationTimesResponseDto;
import roomescape.dto.reservationTime.ReservationTimeRequestDto;
import roomescape.dto.reservationTime.ReservationTimeResponseDto;
import roomescape.dto.reservationTime.ReservationTimesResponseDto;
import roomescape.exception.ForbiddenAccessException;
import roomescape.service.ReservationService;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationService reservationService;

    public ReservationTimeController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ReservationTimesResponseDto getReservationTimes() {
        return new ReservationTimesResponseDto(reservationService.getReservationTimes().stream()
                .map(ReservationTimeResponseDto::from)
                .toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResourceIdResponseDto addReservationTime(
            @Valid @RequestBody ReservationTimeRequestDto requestDto,
            @RequestParam(value = "role", required = false) String role
    ) {
        if (!"admin".equals(role)) {
            throw new ForbiddenAccessException("시간 추가는 관리자만 가능합니다.");
        }

        ReservationTime time = reservationService.addReservationTime(requestDto);
        return new ResourceIdResponseDto(time.getId());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservationTime(
            @PathVariable Long id,
            @RequestParam(value = "role", required = false) String role
    ) {
        if (!"admin".equals(role)) {
            throw new ForbiddenAccessException("시간 삭제는 관리자만 가능합니다.");
        }

        reservationService.deleteReservationTime(id);
    }

    @GetMapping("available")
    @ResponseStatus(HttpStatus.OK)
    public AvailableReservationTimesResponseDto getAvailableTimes(
            @RequestParam("date") String date,
            @RequestParam("themeId") Long themeId
    ) {
        Map<ReservationTime, Boolean> timesWithAvailability = reservationService.getTimesWithAvailability(LocalDate.parse(date), themeId);
        return AvailableReservationTimesResponseDto.of(timesWithAvailability);
    }
}
