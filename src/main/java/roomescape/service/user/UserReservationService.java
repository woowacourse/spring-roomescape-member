package roomescape.service.user;

import java.util.ArrayList;
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
        List<ReservationTime> bookedTimes = reservationDao.findByDateAndTheme(bookableTimesRequest.getDate(),
                bookableTimesRequest.getThemeId());
        List<ReservationTime> allTimes = reservationTimeDao.findAll();
        List<BookableTimeResponse> bookableTimeResponses = new ArrayList<>();
        for (ReservationTime allTime : allTimes) {
            if (!bookedTimes.contains(allTime)) {
                bookableTimeResponses.add(new BookableTimeResponse(allTime.getStartAt(), allTime.getId(), false));
            }
        }
        List<BookableTimeResponse> bookedTimeResponse = bookedTimes.stream()
                .map((reservationTime -> new BookableTimeResponse(reservationTime.getStartAt(), reservationTime.getId(),
                        true)))
                .toList();
        bookableTimeResponses.addAll(bookedTimeResponse);
        return bookableTimeResponses;
    }
}
