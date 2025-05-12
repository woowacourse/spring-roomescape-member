package roomescape.controller;

import jakarta.validation.Valid;
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
import roomescape.domain.LoginMember;
import roomescape.dto.time.AvailableReservationTimeResponse;
import roomescape.dto.time.ReservationTimeCreateRequest;
import roomescape.dto.time.ReservationTimeResponse;
import roomescape.exception.UnauthorizedAccessException;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getAllReservationTimes() {
        List<ReservationTimeResponse> allReservationTimeResponses = reservationTimeService.findAllReservationTimes();
        return ResponseEntity.ok(allReservationTimeResponses);
    }

    @GetMapping("/available")
    public ResponseEntity<List<AvailableReservationTimeResponse>> getAvailableReservationTimes(@RequestParam("date") LocalDate date, @RequestParam("themeId") Long themeId) {
        List<AvailableReservationTimeResponse> allReservationTimeResponses = reservationTimeService.findAvailableReservationTimes(date, themeId);
        return ResponseEntity.ok(allReservationTimeResponses);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> addReservationTime(@Valid @RequestBody final ReservationTimeCreateRequest requestDto, LoginMember member) {
        if (member.getRole().equalsIgnoreCase("USER")) {
            throw new UnauthorizedAccessException("[ERROR] 접근 권한이 없습니다.");
        }

        ReservationTimeResponse responseDto = reservationTimeService.createReservationTime(requestDto);
        return ResponseEntity.created(URI.create("times/" + responseDto.id())).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable Long id, LoginMember member) {
        if (member.getRole().equalsIgnoreCase("USER")) {
            throw new UnauthorizedAccessException("[ERROR] 접근 권한이 없습니다.");
        }

        reservationTimeService.deleteReservationTimeById(id);
        return ResponseEntity.noContent().build();
    }
}
