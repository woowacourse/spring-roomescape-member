package roomescape.ranking;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.reservation.service.ReservationService;
import roomescape.theme.domain.Theme;

@RestController
public class RankingController {
    private final ReservationService reservationService;

    public RankingController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Theme>> getRanking(){
        return ResponseEntity.ok(reservationService.getRanking());
    }
}
