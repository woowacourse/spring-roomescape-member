package roomescape.reservationtime.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.AvailableReservationTimeResponse;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.reservationtime.dto.SaveReservationTimeRequest;
import roomescape.reservationtime.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

  private final ReservationTimeService reservationTimeService;

  public ReservationTimeController(final ReservationTimeService reservationTimeService) {
    this.reservationTimeService = reservationTimeService;
  }

  @GetMapping
  public List<ReservationTimeResponse> getReservationTimes() {
    return reservationTimeService.getReservationTimes()
        .stream()
        .map(ReservationTimeResponse::from)
        .toList();
  }

  @PostMapping
  public ResponseEntity<ReservationTimeResponse> saveReservationTime(
      @RequestBody final SaveReservationTimeRequest request) {
    final ReservationTime savedReservationTime = reservationTimeService.saveReservationTime(
        request);

    return ResponseEntity.created(URI.create("/times/" + savedReservationTime.getId()))
        .body(ReservationTimeResponse.from(savedReservationTime));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteReservationTime(@PathVariable final Long id) {
    reservationTimeService.deleteReservationTime(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/available-reservation-times")
  public List<AvailableReservationTimeResponse> getAvailableReservationTimes(
      @RequestParam("date") final LocalDate date, @RequestParam("theme-id") final Long themeId) {
    return reservationTimeService.getAvailableReservationTimes(date, themeId)
        .getValues()
        .entrySet()
        .stream()
        .map(entry -> AvailableReservationTimeResponse.of(entry.getKey(), entry.getValue()))
        .toList();
  }
}
