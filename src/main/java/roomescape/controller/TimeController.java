package roomescape.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.dto.request.ReservationTimeCreateRequest;
import roomescape.dto.response.ReservationTimeCreateResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ReservationTimeUserResponse;
import roomescape.service.ReservationTimeService;
import roomescape.support.page.PageRequest;
import roomescape.support.page.PageResponse;

@RestController
@RequestMapping("/times")
@Tag(name = "예약 시간 관리", description = "예약 가능 시간 관리 API")
public class TimeController {

    private final ReservationTimeService reservationTimeService;

    public TimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    @Operation(
            summary = "모든 예약 시간 조회",
            description = "사용 가능한 모든 예약 시간을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "예약 시간 목록 조회 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ReservationTimeResponse.class))
                    )
            }
    )
    public ResponseEntity<List<ReservationTimeResponse>> findAll() {
        return ResponseEntity.ok(reservationTimeService.findAll());
    }

    @GetMapping("/paged")
    @Operation(
            summary = "페이징을 적용한 예약 시간 조회",
            description = "페이징을 적용하여 예약 시간을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "예약 시간 목록 조회 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PageResponse.class))
                    )
            }
    )
    public ResponseEntity<List<ReservationTimeResponse>> findAllWithPaging(final PageRequest pageRequest) {
        return ResponseEntity.ok(reservationTimeService.findAllWithPaging(pageRequest).getContent());
    }

    @GetMapping("/available")
    public ResponseEntity<List<ReservationTimeUserResponse>> findAllByDateAndTheme(
            @RequestParam(value = "theme-id") final long themeId,
            @RequestParam(value = "date") final LocalDate date) {
        return ResponseEntity.ok(reservationTimeService.findAllByDateAndTheme(themeId, date));
    }

    @PostMapping
    @Operation(
            summary = "예약 시간 생성",
            description = "새로운 예약 가능 시간을 생성합니다. 시간은 'HH:mm' 형식(예: '13:00')으로 입력합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "예약 시간 생성 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ReservationTimeCreateResponse.class))
                    )
            }
    )
    public ResponseEntity<ReservationTimeCreateResponse> create(
            @RequestBody @Valid final ReservationTimeCreateRequest reservationTimeCreateRequest) {
        final ReservationTimeCreateResponse response = reservationTimeService.create(reservationTimeCreateRequest);
        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "예약 시간 삭제",
            description = "ID로 예약 시간을 삭제합니다. 해당 시간에 예약이 있으면 삭제할 수 없습니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "예약 시간 삭제 성공"
                    )
            }
    )
    public ResponseEntity<Void> delete(
            @Parameter(description = "삭제할 예약 시간 ID") @PathVariable final Long id) {
        reservationTimeService.deleteIfNoReservation(id);
        return ResponseEntity.noContent().build();
    }
}
