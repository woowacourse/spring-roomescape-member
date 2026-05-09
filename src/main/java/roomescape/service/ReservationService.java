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
import roomescape.domain.vo.Name;
import roomescape.dto.request.ReservationRequestDto;
import roomescape.dto.response.ReservationResponseDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationDao reservationDao;
    private final TimeDao timeDao;
    private final ThemeDao themeDao;
    private final Clock clock;

    public ReservationService(
            ReservationDao reservationDao,
            TimeDao timeDao,
            ThemeDao themeDao,
            Clock clock
    ) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
        this.themeDao = themeDao;
        this.clock = clock;
    }

    public List<ReservationResponseDto> findAll() {
        return reservationDao.findAll().stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    public ReservationResponseDto findById(Long id) {
        return reservationDao.findById(id)
                .map(ReservationResponseDto::from)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약입니다."));
    }

    @Transactional
    public ReservationResponseDto create(ReservationRequestDto reservationRequest) {
        Time time = timeDao.findById(reservationRequest.timeId())
                .map(TimeRow::toDomain)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 시간입니다."));

        Theme theme = themeDao.findById(reservationRequest.themeId())
                .map(ThemeRow::toDomain)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테마입니다."));

        if (reservationDao.existsByThemeIdAndTimeIdAndDate(reservationRequest.themeId(), reservationRequest.timeId(), reservationRequest.date())) {
            throw new ConflictException("이미 존재하는 예약이 있습니다. ");
        }

        Reservation reservation = Reservation.create(
                new Name(reservationRequest.name()),
                reservationRequest.date(),
                time,
                theme,
                LocalDateTime.now(clock)
        );

        ReservationRow saved = reservationDao.create(ReservationRow.from(reservation));
        return ReservationResponseDto.from(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!reservationDao.existsById(id)) {
            throw new NotFoundException("존재하지 않는 예약입니다.");
        }

        reservationDao.delete(id);
    }
}
