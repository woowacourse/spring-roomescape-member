package roomescape.reservation.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.auth.domain.Member;
import roomescape.configuration.AuthenticatedMember;
import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.reservation.dto.ReservationResponseDto;
import roomescape.reservation.service.ReservationService;

@RestController
@Validated
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> findAll() {
        List<ReservationResponseDto> responseDtos = reservationService.findAll().stream()
                .map(ReservationResponseDto::new)
                .toList();
        return ResponseEntity.ok(responseDtos);
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> save(@RequestBody @Valid final ReservationRequestDto request, @AuthenticatedMember Member member) {
        ReservationResponseDto responseDto = new ReservationResponseDto(reservationService.save(member, request));
        return ResponseEntity.created(URI.create("/reservations/" + responseDto.id())).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id")
            @Min(value = 1, message = "올바른 예약 ID를 입력해야 합니다.") final long id) {
        reservationService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
