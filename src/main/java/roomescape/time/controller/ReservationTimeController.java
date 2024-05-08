package roomescape.time.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.ReservationTimeRequestDto;
import roomescape.time.dto.ReservationTimeResponseDto;
import roomescape.time.dto.ReservationTimeStatusDto;
import roomescape.time.dto.ReservationTimeStatusResponseDto;
import roomescape.time.service.ReservationTimeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponseDto>> findAll() {
        final List<ReservationTime> reservationTimes = reservationTimeService.readAll();

        final List<ReservationTimeResponseDto> reservationTimeResponseDtos = changeToReservationTimeResponseDtos(reservationTimes);

        return ResponseEntity.ok(reservationTimeResponseDtos);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponseDto> save(@RequestBody final ReservationTimeRequestDto reservationTimeRequestDto) {
        final ReservationTime reservationTime = reservationTimeService.create(reservationTimeRequestDto.toReservationTime());

        final ReservationTimeResponseDto reservationTimeResponseDto = changeToReservationTimeResponseDto(reservationTime);
        final String url = "/times/" + reservationTimeResponseDto.id();

        return ResponseEntity.created(URI.create(url)).body(reservationTimeResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final long id) {
        reservationTimeService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<ReservationTimeStatusResponseDto>> findAvailableTime(@RequestParam("date") final String date, @RequestParam("themeId") final long themeId) {
        final List<ReservationTimeStatusDto> reservationTimeStatusDtos = reservationTimeService.findAvailableTime(date, themeId);

        final List<ReservationTimeStatusResponseDto> reservationTimeStatusResponseDtos = changeToReservationTimeStatusResponseDtos(reservationTimeStatusDtos);

        return ResponseEntity.ok(reservationTimeStatusResponseDtos);
    }

    private ReservationTimeResponseDto changeToReservationTimeResponseDto(final ReservationTime reservationTime) {
        return new ReservationTimeResponseDto(reservationTime);
    }

    private List<ReservationTimeResponseDto> changeToReservationTimeResponseDtos(final List<ReservationTime> reservationTimes) {
        return reservationTimes.stream()
                .map(this::changeToReservationTimeResponseDto)
                .toList();
    }

    private ReservationTimeStatusResponseDto changeToReservationTimeStatusResponseDto(final ReservationTimeStatusDto reservationTimeStatusDto) {
        return new ReservationTimeStatusResponseDto(reservationTimeStatusDto);
    }

    private List<ReservationTimeStatusResponseDto> changeToReservationTimeStatusResponseDtos(final List<ReservationTimeStatusDto> reservationTimeStatusDtos) {
        return reservationTimeStatusDtos.stream()
                .map(this::changeToReservationTimeStatusResponseDto)
                .toList();
    }
}
