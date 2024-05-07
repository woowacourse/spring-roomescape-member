package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.TimeSlot;
import roomescape.domain.dto.BookResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.TimeDao;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookService {
    private final TimeDao timeDao;

    public BookService(final ReservationDao reservationDao, final TimeDao timeDao) {
        this.timeDao = timeDao;
    }

    public List<BookResponse> findAvaliableBookList(final LocalDate date, final Long themeId) {
        List<TimeSlot> reservationTimeSlots = timeDao.findByDateAndThemeId(date, themeId);
        List<TimeSlot> timeSlots = timeDao.findAll();
        return timeSlots.stream().map(timeSlot -> {
            Boolean alreadyBooked = reservationTimeSlots.stream()
                    .anyMatch(reservationTimeSlot -> reservationTimeSlot.getId().equals(timeSlot.getId()));
            return new BookResponse(timeSlot.getStartAt(), timeSlot.getId(), alreadyBooked);
        }).toList();
    }
}
