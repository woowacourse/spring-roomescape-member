package roomescape.controller;

import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping(value = "/admin/times")
public class AdminReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public AdminReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponseDto> create(@Valid @RequestBody ReservationTimeRequestDto request) {
        ReservationTimeResponseDto response = reservationTimeService.create(request);

        URI location = buildLocationUri(response);

        return ResponseEntity.created(location)
                .body(response);
    }

    @Nonnull
    private static URI buildLocationUri(ReservationTimeResponseDto responseDto) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDto.id())
                .toUri();
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponseDto>> readAll() {
        List<ReservationTimeResponseDto> responseDtos = reservationTimeService.readAll();
        return ResponseEntity.ok(responseDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        reservationTimeService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
