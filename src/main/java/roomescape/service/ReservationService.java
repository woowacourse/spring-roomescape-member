package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.ReservationErrorCode;
import roomescape.common.ThemeErrorCode;
import roomescape.common.TimeErrorCode;
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
import roomescape.dto.request.ReservationUpdateDto;
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
                .orElseThrow(() -> new NotFoundException(ReservationErrorCode.NOT_FOUND));
    }

    public List<ReservationResponseDto> findByName(String name) {
        return reservationDao.findByName(name).stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    @Transactional
    public ReservationResponseDto create(ReservationRequestDto reservationRequest) {
        Time time = timeDao.findById(reservationRequest.timeId())
                .map(TimeRow::toDomain)
                .orElseThrow(() -> new NotFoundException(TimeErrorCode.NOT_FOUND));

        Theme theme = themeDao.findById(reservationRequest.themeId())
                .map(ThemeRow::toDomain)
                .orElseThrow(() -> new NotFoundException(ThemeErrorCode.NOT_FOUND));

        if (reservationDao.existsByThemeIdAndTimeIdAndDate(reservationRequest.themeId(), reservationRequest.timeId(), reservationRequest.date())) {
            throw new ConflictException(ReservationErrorCode.DUPLICATE);
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
        Reservation reservation = reservationDao.findById(id)
                .map(ReservationRow::toDomain)
                .orElseThrow(() -> new NotFoundException(ReservationErrorCode.NOT_FOUND));

        if (!reservation.isDeletable(LocalDateTime.now(clock))) {
            throw new ConflictException(ReservationErrorCode.DUPLICATE);
        }

        reservationDao.delete(id);
    }

    @Transactional
    public ReservationResponseDto update(Long id, ReservationUpdateDto reservationUpdate) {
        Time time = timeDao.findById(reservationUpdate.timeId())
                .map(TimeRow::toDomain)
                .orElseThrow(() -> new NotFoundException(TimeErrorCode.NOT_FOUND));

        Theme theme = themeDao.findById(reservationUpdate.themeId())
                .map(ThemeRow::toDomain)
                .orElseThrow(() -> new NotFoundException(ThemeErrorCode.NOT_FOUND));

        ReservationRow existing = reservationDao.findById(id)
                .orElseThrow(() -> new NotFoundException(ReservationErrorCode.NOT_FOUND));

        boolean conflicts = reservationDao.existsByThemeIdAndTimeIdAndDateAndIdNot(
                reservationUpdate.themeId(),
                reservationUpdate.timeId(),
                reservationUpdate.date(),
                id
        );

        if (conflicts) {
            throw new ConflictException(ReservationErrorCode.DUPLICATE);
        }

        Reservation updated = existing.toDomain()
                .update(new Name(reservationUpdate.name()), reservationUpdate.date(), time, theme, LocalDateTime.now(clock));

        ReservationRow saved = reservationDao.update(ReservationRow.from(updated));

        return ReservationResponseDto.from(saved);
    }
}
