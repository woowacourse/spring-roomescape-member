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
import roomescape.dto.AvailableReservationTimeRequestDto;
import roomescape.dto.AvailableReservationTimeResponseDto;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.exception.InvalidReservationException;
import roomescape.exception.InvalidReservationTimeException;
import roomescape.exception.InvalidThemeException;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
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

        Reservation reservation = createReservation(person, date, reservationTime, theme);
        reservation.validateDateTime(date, reservationTime, currentDateTime);
        validateAlreadyReservation(request, date);

        reservationDao.saveReservation(reservation);
        return ReservationResponseDto.from(reservation);
    }

    private ReservationDate getReservationDate(final ReservationRequestDto request,
                                               final LocalDateTime currentDateTime) {
        ReservationDate date = new ReservationDate(LocalDate.parse(request.date()));
        date.validateDate(currentDateTime.toLocalDate());
        return date;
    }

    private ReservationTime getReservationTime(final ReservationRequestDto request) {
        return reservationTimeDao.findById(request.timeId())
                .orElseThrow(() -> new InvalidReservationTimeException("해당 ID의 예약 시간을 찾을 수 없습니다."));
    }

    private Theme getTheme(final ReservationRequestDto request) {
        return themeDao.findById(request.themeId())
                .orElseThrow(() -> new InvalidThemeException("해당 ID의 테마를 찾을 수 없습니다"));
    }

    private Reservation createReservation(final Person person, final ReservationDate date,
                                          final ReservationTime reservationTime, final Theme theme) {
        return new Reservation(person, date, reservationTime, theme);
    }

    private void validateAlreadyReservation(ReservationRequestDto request, ReservationDate date) {
        if (reservationDao.existsReservationBy(date.getDate(), request.timeId(), request.themeId())) {
            throw new InvalidReservationException("해당 날짜와 시간에 이미 같은 테마가 예약되어 있습니다.");
        }
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteReservation(id);
    }

    public List<AvailableReservationTimeResponseDto> getAvailableReservationTimes(
            AvailableReservationTimeRequestDto request) {
        List<ReservationTime> times = reservationTimeDao.findAllReservationTimes();
        return times.stream()
                .map(time -> createAvailableReservationTimeResponseDto(request, time))
                .toList();
    }

    private AvailableReservationTimeResponseDto createAvailableReservationTimeResponseDto(
            AvailableReservationTimeRequestDto request,
            ReservationTime time) {
        if (isAlreadyBookedTime(request.date(), time.getId(), request.themeId())) {
            return AvailableReservationTimeResponseDto.from(time, true);
        }
        return AvailableReservationTimeResponseDto.from(time, false);
    }

    private boolean isAlreadyBookedTime(LocalDate date, Long timeId, Long themeId) {
        return reservationDao.existsReservationBy(date, timeId, themeId);
    }
}
