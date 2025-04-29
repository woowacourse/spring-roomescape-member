package roomescape.reservationtime.service;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.dao.ReservationTimeDao;
import roomescape.reservationtime.dto.request.ReservationTimeRequest;
import roomescape.reservationtime.dto.response.ReservationTimeResponse;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTimeResponse> findAll() {
        List<ReservationTime> reservationTimeDaoAll = reservationTimeDao.findAll();

        return reservationTimeDaoAll.stream()
                .map(time -> {
                    return ReservationTimeResponse.toDto(time);
                })
                .toList();
    }

    public Long create(@Valid @RequestBody ReservationTimeRequest reservationTimeRequest) {
        ReservationTime reservationTime = reservationTimeRequest.toTime();
        return reservationTimeDao.create(reservationTime);
    }

    public int delete(@PathVariable Long id) {
        return reservationTimeDao.delete(id);
    }
}
