package roomescape.time.service;

import roomescape.time.domain.ReservationTime;

import java.time.LocalTime;
import java.util.List;

public interface ReservationTimeService {

    List<ReservationTime> getTimes();
    ReservationTime createTime(LocalTime localTime);
    void removeTime(Long id);
}
