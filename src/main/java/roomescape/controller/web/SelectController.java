package roomescape.controller.web;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationResponse;
import roomescape.dto.SelectableTimeResponse;
import roomescape.service.ReservationService;

@Controller
public class SelectController {
    private final ReservationService reservationService;

    public SelectController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<SelectableTimeResponse>> reserve() {
//        List<SelectableTimeResponse> selectableTimeResponse = reservationService.findSelectableTime();
//        return null;
        List<ReservationResponse> responses = reservationService.findAll();
        List<SelectableTimeResponse> selectableTimeResponses = responses.stream()
                .map(response -> SelectableTimeResponse.of(
                        new ReservationTime(response.id(), response.time().getStartAt()),
                        true)
                ).toList();
        return ResponseEntity.ok(selectableTimeResponses);
    }

}
