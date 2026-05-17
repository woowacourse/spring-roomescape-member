package roomescape.reservationtime.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationtime.controller.dto.ReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;

@Tag(name = "reservation-time", description = "예약 시간 API")
@RestController
@RequestMapping("/themes/{themeId}")
@RequiredArgsConstructor
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    @GetMapping("/available-times")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "예약 가능 시간 조회", description = "특정 테마와 날짜에 예약 가능한 시간 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예약 가능 시간 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 날짜 파라미터 또는 필수 파라미터 누락")
    })
    public List<ReservationTimeResponse> read(
            @PathVariable final Long themeId,
            @RequestParam final LocalDate date
    ) {
        return reservationTimeService.findAvailableTimes(date, themeId).stream()
                .map(ReservationTimeResponse::from).toList();
    }

}
