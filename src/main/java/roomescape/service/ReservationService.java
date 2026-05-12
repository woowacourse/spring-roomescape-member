package roomescape.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.TimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.Time;
import roomescape.dto.request.ReservationPatchDto;
import roomescape.dto.request.ReservationRequestDto;

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
        return reservationDao.findAll();
    }

    public Reservation findById(Long id) {
        return reservationDao.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약입니다."));
    }

    @Transactional
    public Reservation create(ReservationRequestDto reservationRequest) {
        Time timeById = timeDao.findById(reservationRequest.timeId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 시간입니다."));
        Theme themeById = themeDao.findById(reservationRequest.themeId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테마입니다."));

        if (reservationDao.existsByThemeIdAndTimeIdAndDate(reservationRequest.themeId(), reservationRequest.timeId(),
                reservationRequest.date())) {
            throw new ConflictException("이미 존재하는 예약이 있습니다. ");
        }
        Reservation reservation = new Reservation(reservationRequest.name(), reservationRequest.date(), timeById,
                themeById);
        return reservationDao.insert(reservation);
    }

    @Transactional
    public Reservation update(Long id, ReservationPatchDto reservationPatchDto) {
        Reservation reservation = findById(id);
        Time time = timeDao.findById(reservationPatchDto.timeId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 시간입니다."));

        reservation.update(reservationPatchDto.date(), time);
        reservationDao.update(reservation);

        return reservation;
    }

    @Transactional
    public void cancel(Long id) {
        Reservation reservation = findById(id);
        reservation.validateCancel(LocalDateTime.now());
        reservationDao.delete(id);
    }

    @Transactional
    public void delete(Long id) {
        if (!reservationDao.existsById(id)) {
            throw new NotFoundException("존재하지 않는 예약입니다.");
        }

        reservationDao.delete(id);
    }
}
