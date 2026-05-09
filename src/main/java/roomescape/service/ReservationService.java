package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.TimeDao;
import roomescape.dao.row.ReservationRow;
import roomescape.dao.row.ThemeRow;
import roomescape.dao.row.TimeRow;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.Time;
import roomescape.dto.request.ReservationRequestDto;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationDao reservationDao;
    private final TimeDao timeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, TimeDao timeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
        this.themeDao = themeDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.findAll().stream()
                .map(ReservationRow::toDomain)
                .toList();
    }

    public Reservation findById(Long id) {
        return reservationDao.findById(id)
                .map(ReservationRow::toDomain)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약입니다."));
    }

    @Transactional
    public Reservation create(ReservationRequestDto reservationRequest) {
        Time timeById = timeDao.findById(reservationRequest.timeId())
                .map(TimeRow::toDomain)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 시간입니다."));

        Theme themeById = themeDao.findById(reservationRequest.themeId())
                .map(ThemeRow::toDomain)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테마입니다."));

        if (reservationDao.existsByThemeIdAndTimeIdAndDate(reservationRequest.themeId(), reservationRequest.timeId(),
                reservationRequest.date())) {
            throw new ConflictException("이미 존재하는 예약이 있습니다. ");
        }

        ReservationRow reservation = new ReservationRow(
                reservationRequest.name(),
                reservationRequest.date(),
                TimeRow.from(timeById),
                ThemeRow.from(themeById));

        return reservationDao.create(reservation).toDomain();
    }

    @Transactional
    public void delete(Long id) {
        ReservationRow reservation = reservationDao.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약입니다."));

        reservationDao.delete(reservation.id());
    }
}
