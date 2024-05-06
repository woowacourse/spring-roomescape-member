package roomescape.service.user;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.BookableTimeResponse;
import roomescape.dto.BookableTimesRequest;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeDao;

@Service
public class UserReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeDao reservationTimeDao;

    public UserReservationService(ReservationRepository reservationRepository, ReservationTimeDao reservationTimeDao) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<BookableTimeResponse> findBookableTimes(BookableTimesRequest bookableTimesRequest) {
        List<ReservationTime> bookedTimes = reservationRepository.findTimesByDateAndTheme(bookableTimesRequest.date(),
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
