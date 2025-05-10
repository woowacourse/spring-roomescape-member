package roomescape.controller.api;

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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import roomescape.auth.RequestMember;
import roomescape.domain.Member;
import roomescape.dto.request.CreateReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ReservationResponse> getAll() {
        return reservationService.getAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void create(@RequestMember Member member, @RequestBody @Valid CreateReservationRequest request) {
        reservationService.create(member.id(), request.date(), request.timeId(), request.themeId());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        reservationService.delete(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public List<ReservationResponse> search(
            @RequestParam(value = "themeId", required = false) Long themeId,
            @RequestParam(value = "memberId", required = false) Long memberId,
            @RequestParam(value = "dateFrom", required = false) LocalDate dateFrom,
            @RequestParam(value = "dateTo", required = false) LocalDate dateTo
    ) {
        return reservationService.search(themeId, memberId, dateFrom, dateTo);
    }
}
