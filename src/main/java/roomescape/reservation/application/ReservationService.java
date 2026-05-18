package roomescape.reservation.application;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.code.ReservationErrorCode;
import roomescape.exception.custom.BusinessException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.infra.ReservationRepository;
import roomescape.reservation.infra.ReservationTimeRepository;
import roomescape.reservation.infra.ThemeRepository;
import roomescape.reservation.presentation.dto.request.ReservationSaveRequest;
import roomescape.reservation.presentation.dto.request.ReservationUpdateRequest;
import roomescape.reservation.presentation.dto.response.ReservationFindResponse;
import roomescape.reservation.presentation.dto.response.ReservationSaveResponse;
import org.springframework.stereotype.Service;
import roomescape.reservation.presentation.dto.response.dto.TimeInformation;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    private final Clock clock;

    @Transactional
    public ReservationSaveResponse save(ReservationSaveRequest body) {
        ReservationTime time = reservationTimeRepository.findById(body.timeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 갖는 시간대는 존재하지 않습니다."));

        Theme theme = themeRepository.findById(body.themeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 갖는 테마는 존재하지 않습니다."));

        validateReservationDateTime(body.date(), time.getStartAt());

        try {
            Reservation reservation = reservationRepository.save(body.toDomain(time, theme));
            return ReservationSaveResponse.of(time, theme, reservation);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(ReservationErrorCode.DUPLICATE_RESERVATION);
        }
    }

    public List<ReservationFindResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ReservationFindResponse> findByName(String name) {
        return reservationRepository.findByName(name).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void update(Long id, ReservationUpdateRequest body) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 갖는 예약은 존재하지 않습니다."));
        ReservationTime time = reservationTimeRepository.findById(body.timeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 갖는 시간대는 존재하지 않습니다."));

        validateReservationDateTime(body.date(), time.getStartAt());

        Reservation updatedReservation = new Reservation(
                reservation.getId(),
                reservation.getName(),
                body.date(),
                time,
                reservation.getTheme()
        );

        try {
            reservationRepository.update(updatedReservation);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(ReservationErrorCode.DUPLICATE_RESERVATION);
        }
    }

    @Transactional
    public void delete(Long id) {
        int deletedCount = reservationRepository.deleteById(id);

        if (deletedCount == 0) {
            throw new BusinessException(ReservationErrorCode.RESERVATION_NOT_FOUND);
        }
    }

    @Transactional
    public void delete(String name) {
        int deletedCount = reservationRepository.deleteByName(name);

        if (deletedCount == 0) {
            throw new BusinessException(ReservationErrorCode.RESERVATION_NOT_FOUND);
        }
    }

    private void validateReservationDateTime(LocalDate date, LocalTime time) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time);
        LocalDateTime now = LocalDateTime.now(clock);

        if (reservationDateTime.isBefore(now)) {
            throw new BusinessException(ReservationErrorCode.RESERVATION_DATE_TIME_EXPIRED);
        }
    }

    private ReservationFindResponse toResponse(Reservation reservation) {
        return new ReservationFindResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                new TimeInformation(
                        reservation.getTimeId(),
                        reservation.getStartAt()
                ),
                reservation.getThemeId()
        );
    }
}
