package roomescape.controller.admin;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.ReservationTimePostRequest;
import roomescape.dto.response.ReservationTimePostResponse;
import roomescape.service.ReservationTimeCommandService;
import roomescape.service.ReservationTimeQueryService;

@RestController
@RequestMapping("/admin/times")
public class AdminReservationTimeController {

    private final ReservationTimeCommandService reservationTimeCommandService;
    private final ReservationTimeQueryService reservationTimeQueryService;

    public AdminReservationTimeController(ReservationTimeCommandService reservationTimeCommandService,
                                          ReservationTimeQueryService reservationTimeQueryService) {
        this.reservationTimeCommandService = reservationTimeCommandService;
        this.reservationTimeQueryService = reservationTimeQueryService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationTimePostResponse> readTimes(
    ) {
        return reservationTimeQueryService.findAllTimes();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationTimePostResponse createTime(
            @RequestBody ReservationTimePostRequest request
    ) {
        return reservationTimeCommandService.createTime(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTime(
            @PathVariable Long id
    ) {
        reservationTimeCommandService.deleteTime(id);
    }
}
