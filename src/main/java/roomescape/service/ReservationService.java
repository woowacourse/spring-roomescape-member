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
import roomescape.service.nowdate.CurrentDateTime;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final CurrentDateTime currentDateTime;

    public ReservationService(ReservationDao reservationDao,
        ReservationTimeDao reservationTimeDao, ThemeDao themeDao, CurrentDateTime currentDateTime) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.currentDateTime = currentDateTime;
    }

    public List<ReservationResponseDto> getAllReservations() {
        return reservationDao.findAllReservation().stream()
            .map(ReservationResponseDto::from)
            .toList();
    }

    public ReservationResponseDto saveReservation(ReservationRequestDto reservationRequestDto) {
        LocalDateTime currentDateTimeInfo = currentDateTime.get();

        Person person = new Person(reservationRequestDto.name());
        ReservationDate date = new ReservationDate(LocalDate.parse(reservationRequestDto.date()));
        date.validateDate(currentDateTimeInfo.toLocalDate());
        ReservationTime reservationTime = reservationTimeDao.findById(
                reservationRequestDto.timeId())
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 예약시간을 찾을 수 없습니다"));

        Theme theme = themeDao.findById(reservationRequestDto.themeId())
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 테마를 찾을 수 없습니다"));

        Reservation reservation = new Reservation(person, date, reservationTime, theme);
        reservation.validateDateTime(date, reservationTime, currentDateTimeInfo);

        validateAlreadyExistDateTime(reservationRequestDto, date);
        long newId = reservationDao.saveReservation(reservation);
        reservation.setId(newId);

        return ReservationResponseDto.from(reservation);
    }

    private void validateAlreadyExistDateTime(ReservationRequestDto reservationRequestDto,
        ReservationDate date) {
        if (reservationDao.findByDateAndTime(date, reservationRequestDto.timeId()) != 0) {
            throw new InvalidReservationException("중복된 날짜와 시간을 예약할 수 없습니다.");
        }
    }

    public void deleteReservation(Long id) {
        validateIsExistReservationBy(id);
        reservationDao.deleteReservation(id);
    }

    private void validateIsExistReservationBy(Long id) {
        reservationDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 예약을 찾을 수 없습니다."));
    }

    public List<BookedReservationTimeResponseDto> getAllBookedReservationTimes(String date,
        Long themeId) {
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
}
