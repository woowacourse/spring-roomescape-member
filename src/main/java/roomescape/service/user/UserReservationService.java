package roomescape.service.user;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.BookableTimeResponse;
import roomescape.dto.BookableTimesRequest;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

@Service
public class UserReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public UserReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<BookableTimeResponse> findBookableTimes(BookableTimesRequest bookableTimesRequest) {
        List<ReservationTime> bookedTimes = reservationDao.findByDateAndTheme(bookableTimesRequest.date(),
                bookableTimesRequest.themeId());
        List<ReservationTime> allTimes = reservationTimeDao.findAll();
        return allTimes.stream()
                .map(time -> {
                    return new BookableTimeResponse(time.getStartAt(), time.getId(), isBookedTime(bookedTimes, time));
                })
                .toList();
    }

    private boolean isBookedTime(List<ReservationTime> bookedTimes, ReservationTime time) {
        return bookedTimes.contains(time);
    }
}
