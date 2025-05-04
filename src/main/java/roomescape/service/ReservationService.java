package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Person;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.BookedReservationTimeResponseDto;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.exception.InvalidReservationException;
import roomescape.exception.InvalidThemeException;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao,
                              ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public List<ReservationResponseDto> getAllReservations() {
        return reservationDao.findAllReservation().stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    public ReservationResponseDto saveReservation(ReservationRequestDto request) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Person person = new Person(request.name());
        ReservationDate date = getReservationDate(request, currentDateTime);
        ReservationTime reservationTime = getReservationTime(request);
        Theme theme = getTheme(request);

        Reservation reservation = createReservation(person, date, reservationTime, theme, currentDateTime);
        validateAlreadyExistDateTime(request, date);

        reservationDao.saveReservation(reservation);
        return ReservationResponseDto.from(reservation);
    }

    private Reservation createReservation(final Person person, final ReservationDate date,
                                          final ReservationTime reservationTime, final Theme theme,
                                          final LocalDateTime currentDateTime) {
        Reservation reservation = new Reservation(person, date, reservationTime, theme);
        reservation.validateDateTime(date, reservationTime, currentDateTime);
        return reservation;
    }

    private Theme getTheme(final ReservationRequestDto request) {
        return themeDao.findById(request.themeId())
                .orElseThrow(() -> new InvalidThemeException("해당 ID의 테마를 찾을 수 없습니다"));
    }

    private ReservationTime getReservationTime(final ReservationRequestDto request) {
        return reservationTimeDao.findById(request.timeId());
    }

    private ReservationDate getReservationDate(final ReservationRequestDto request,
                                               final LocalDateTime currentDateTime) {
        ReservationDate date = new ReservationDate(LocalDate.parse(request.date()));
        date.validateDate(currentDateTime.toLocalDate());
        return date;
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteReservation(id);
    }

    public List<BookedReservationTimeResponseDto> getAllBookedReservationTimes(String date, Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeDao.findAllReservationTimes();
        return reservationTimes.stream()
                .map(reservationTime -> createBookedReservationTimeResponseDto(date, themeId,
                        reservationTime))
                .toList();
    }

    private BookedReservationTimeResponseDto createBookedReservationTimeResponseDto(
            String date, Long themeId, ReservationTime reservationTime) {
        if (isAlreadyBookedTime(date, themeId, reservationTime)) {
            return BookedReservationTimeResponseDto.from(reservationTime, true);
        }
        return BookedReservationTimeResponseDto.from(reservationTime, false);
    }

    private boolean isAlreadyBookedTime(String date, Long themeId,
                                        ReservationTime reservationTime) {
        int alreadyExistReservationCount = reservationDao.findAlreadyExistReservationBy(
                date, themeId, reservationTime.getId());
        return alreadyExistReservationCount != 0;
    }

    private void validateAlreadyExistDateTime(ReservationRequestDto reservationRequestDto,
                                              ReservationDate date) {
        if (reservationDao.findByDateAndTime(date, reservationRequestDto.timeId()) != 0) {
            throw new InvalidReservationException("중복된 날짜와 시간을 예약할 수 없습니다.");
        }
    }
}
