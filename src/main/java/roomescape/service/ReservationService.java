package roomescape.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.BadRequestException;
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

    public List<Reservation> findAllByName(String name) {
        return reservationDao.findAllByName(name);
    }

    public Reservation findActiveById(Long id) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약입니다."));
        if (!reservation.isActive()) {
            throw new NotFoundException("존재하지 않는 예약입니다.");
        }
        return reservation;
    }

    @Transactional
    public Reservation create(ReservationRequestDto reservationRequest) {
        Reservation reservation = buildReservation(reservationRequest);
        reservation.validateCreate(LocalDateTime.now());
        return reservationDao.insert(reservation);
    }

    @Transactional
    public Reservation updateByUser(Long id, String name, ReservationPatchDto reservationPatchDto) {
        Reservation reservation = findActiveById(id);
        if (!reservation.getName().equals(name)) {
            throw new BadRequestException("본인의 예약만 수정할 수 있습니다.");
        }
        Time time = timeDao.findById(reservationPatchDto.timeId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 시간입니다."));
        reservation.update(reservationPatchDto.date(), time);
        return reservationDao.update(reservation);
    }

    @Transactional
    public void cancel(Long id) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약입니다."));
        LocalDateTime now = LocalDateTime.now();
        reservation.validateCancel(now);
        reservation.cancel(now);
        reservationDao.update(reservation);
    }

    private Reservation buildReservation(ReservationRequestDto reservationRequest) {
        Time time = timeDao.findById(reservationRequest.timeId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 시간입니다."));
        Theme theme = themeDao.findById(reservationRequest.themeId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테마입니다."));
        if (reservationDao.existsByThemeIdAndTimeIdAndDate(reservationRequest.themeId(), reservationRequest.timeId(),
                reservationRequest.date())) {
            throw new ConflictException("이미 존재하는 예약이 있습니다.");
        }
        return new Reservation(reservationRequest.name(), reservationRequest.date(), time, theme);
    }
}
