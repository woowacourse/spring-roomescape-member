package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.ReservationTime;
import roomescape.service.dto.ServiceReservationTimeAvailabilityResponse;
import roomescape.service.dto.ServiceReservationTimeRequest;
import roomescape.service.dto.ServiceReservationTimeResponse;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    @Transactional
    public ServiceReservationTimeResponse create(ServiceReservationTimeRequest requestDto) {
        ReservationTime reservationTime = reservationTimeDao.create(requestDto.toEntity());
        return ServiceReservationTimeResponse.from(reservationTime);
    }

    public List<ServiceReservationTimeResponse> readAll() {
        List<ReservationTime> reservationTimes = reservationTimeDao.readAll();
        return reservationTimes.stream()
                .map(ServiceReservationTimeResponse::from)
                .toList();
    }

    public List<ServiceReservationTimeAvailabilityResponse> readAvailabilityByDateAndTheme(
            LocalDate date, Long themeId) {
        themeDao.read(themeId);

        List<ReservationTime> allReservationTimes = reservationTimeDao.readAll();
        List<Long> reservedTimeIdByDateAndTheme = reservationTimeDao.reservedTimeIdByDateAndTheme(date, themeId);

        return allReservationTimes.stream()
                .map(reservationTime -> {
                    if (reservedTimeIdByDateAndTheme.contains(reservationTime.getId())) {
                        return ServiceReservationTimeAvailabilityResponse.from(reservationTime, false);
                    }
                    return ServiceReservationTimeAvailabilityResponse.from(reservationTime, true);
                }).toList();
    }

    @Transactional
    public void delete(Long id) {
        reservationTimeDao.delete(id);
    }
}
