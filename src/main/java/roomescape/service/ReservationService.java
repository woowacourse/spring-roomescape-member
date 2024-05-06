package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.TimeSlot;
import roomescape.domain.dto.ReservationRequest;
import roomescape.domain.dto.ReservationResponse;
import roomescape.exception.ErrorType;
import roomescape.exception.InvalidClientRequestException;
import roomescape.exception.ReservationFailException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ThemeDao;
import roomescape.repository.TimeDao;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    private final TimeDao timeDao;
    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;

    public ReservationService(final
                              TimeDao timeDao, final ReservationDao reservationDao, final ThemeDao themeDao) {
        this.timeDao = timeDao;
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
    }

    public List<ReservationResponse> findEntireReservationList() {
        return reservationDao.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse create(final ReservationRequest reservationRequest) {
        TimeSlot timeSlot = getTimeSlot(reservationRequest);
        Theme theme = getTheme(reservationRequest);
        validate(reservationRequest.date(), timeSlot, theme);
        Long reservationId = reservationDao.create(reservationRequest);
        Reservation reservation = reservationRequest.toEntity(reservationId, timeSlot, theme);
        return ReservationResponse.from(reservation);
    }

    private TimeSlot getTimeSlot(final ReservationRequest reservationRequest) {
        return timeDao.findById(reservationRequest.timeId())
                .orElseThrow(() -> new InvalidClientRequestException(ErrorType.NOT_EXIST_TIME, "timeId", reservationRequest.timeId().toString()));
    }

    private Theme getTheme(final ReservationRequest reservationRequest) {
        return themeDao.findById(reservationRequest.themeId())
                .orElseThrow(() -> new InvalidClientRequestException(ErrorType.NOT_EXIST_TIME, "themeId", reservationRequest.themeId().toString()));
    }

    private void validate(final LocalDate date, final TimeSlot timeSlot, final Theme theme) {
        validateReservation(date, timeSlot);
        validateDuplicatedReservation(date, timeSlot.getId(), theme.getId());
    }

    private void validateReservation(final LocalDate date, final TimeSlot time) {
        if (time == null || (time.isTimeBeforeNow() && !date.isAfter(LocalDate.now()))) {
            throw new ReservationFailException("지나간 날짜와 시간으로 예약할 수 없습니다.");
        }
    }

    private void validateDuplicatedReservation(final LocalDate date, final Long timeId, final Long themeId) {
        if (reservationDao.isExists(date, timeId, themeId)) {
            throw new ReservationFailException("이미 예약이 등록되어 있습니다.");
        }
    }

    public void delete(final Long id) {
        reservationDao.delete(id);
    }
}
