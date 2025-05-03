package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.*;
import roomescape.persistence.query.CreateReservationQuery;
import roomescape.service.param.CreateReservationParam;
import roomescape.service.result.ReservationResult;
import roomescape.service.result.ReservationTimeResult;
import roomescape.service.result.ThemeResult;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationTimeRepository reservationTImeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationTimeRepository reservationTImeRepository,
                              ReservationRepository reservationRepository, final ThemeRepository themeRepository) {
        this.reservationTImeRepository = reservationTImeRepository;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public Long create(CreateReservationParam createReservationParam, LocalDateTime currentDateTime) {
        ReservationTime reservationTime = reservationTImeRepository.findById(createReservationParam.timeId()).orElseThrow(
                () -> new IllegalArgumentException(
                        createReservationParam.timeId() + "에 해당하는 reservation_time 튜플이 없습니다."));
        Theme theme = themeRepository.findById(createReservationParam.themeId()).orElseThrow(() -> new IllegalArgumentException(
                createReservationParam.themeId() + "에 해당하는 theme 튜플이 없습니다."));

        validateUniqueReservation(createReservationParam, reservationTime, theme);
        validateReservationDateTime(createReservationParam, currentDateTime, reservationTime);

        return reservationRepository.create(
                new CreateReservationQuery(
                        createReservationParam.name(),
                        createReservationParam.date(),
                        reservationTime,
                        theme
                ));
    }

    public void deleteById(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    public List<ReservationResult> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(this::toReservationResult)
                .toList();
    }

    public ReservationResult findById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException(reservationId + "에 해당하는 reservation 튜플이 없습니다."));
        return toReservationResult(reservation);
    }

    private ReservationResult toReservationResult(Reservation reservation) {
        return new ReservationResult(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResult.from(reservation.getTime()),
                ThemeResult.from(reservation.getTheme()));
    }

    private void validateUniqueReservation(final CreateReservationParam createReservationParam, final ReservationTime reservationTime, final Theme theme) {
        if (reservationRepository.existByDateAndTimeIdAndThemeId(createReservationParam.date(), reservationTime.id(), theme.getId())) {
            throw new IllegalArgumentException("테마에 대해 날짜와 시간이 중복된 예약이 존재합니다.");
        }
    }

    private void validateReservationDateTime(final CreateReservationParam createReservationParam, final LocalDateTime currentDateTime, final ReservationTime reservationTime) {
        LocalDateTime reservationDateTime = LocalDateTime.of(createReservationParam.date(), reservationTime.startAt());
        if (reservationDateTime.isBefore(currentDateTime)) {
            throw new IllegalArgumentException("지난 날짜와 시간에 대한 예약은 불가능합니다.");
        }
        Duration duration = Duration.between(currentDateTime, reservationDateTime);
        if (duration.toMinutes() < 10) {
            throw new IllegalArgumentException("예약 시간까지 10분도 남지 않아 예약이 불가합니다.");
        }
    }
}
