package roomescape.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.dto.ResourceIdResponseDto;
import roomescape.dto.reservationTime.AvailableReservationTimesResponseDto;
import roomescape.dto.reservationTime.ReservationTimeRequestDto;
import roomescape.dto.reservationTime.ReservationTimeResponseDto;
import roomescape.exception.ForbiddenAccessException;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationService reservationService;

    public ReservationTimeController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationTimeResponseDto> getReservationTimes() {
        return reservationService.getReservationTimes().stream()
            .map(ReservationTimeResponseDto::from)
            .toList();
    }

    // TODO: validate 까진 아니더라도 role 을 enum 으로 뺄지
    // TODO: 컨트롤러 인자값에 final?
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

    // TODO: pathVariable 의 입력 검증은?
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

    // TODO: RequestParam 에도 LocalDate로 받을 수 있는지?
    @GetMapping("available")
    @ResponseStatus(HttpStatus.OK)
    public AvailableReservationTimesResponseDto getAvailableTimes(
        @RequestParam("date") String date,
        @RequestParam("themeId") Long themeId
    ) {
        List<ReservationTime> availableTimes = reservationService.getAvailableTimes(LocalDate.parse(date), themeId);
        List<ReservationTime> allTimes = reservationService.getReservationTimes();

        // TODO: 같은 타입 인자를 두개 받는 경우 잘못 사용될 수 있음 (builder 패턴을 쓰는건가? 사이즈로 검증?)
        return AvailableReservationTimesResponseDto.of(availableTimes, allTimes);
    }
}
