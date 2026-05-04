package roomescape.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.domain.ReservationTime;
import roomescape.service.ReservationTimeService;

@RestController
public class TimeController {

    private final ReservationTimeService reservationTimeService;

    public TimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    public List<ReservationTimeResponse> getReservationTime(){
        return reservationTimeService.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
   }

    @PostMapping("/times")
    public ReservationTimeResponse postReservationTime(@RequestBody ReservationTimeRequest request){
        ReservationTime time = new ReservationTime(null, request.startAt());
        ReservationTime savedTime = reservationTimeService.save(time);
        return ReservationTimeResponse.from(savedTime);
    }

    @DeleteMapping("/times/{id}")
    public void deleteReservationTime(@PathVariable Long id){
        reservationTimeService.deleteById(id);
    }
}
