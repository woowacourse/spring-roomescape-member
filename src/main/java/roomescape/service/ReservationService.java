package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.common.NotFoundEntityException;
import roomescape.common.BusinessRuleViolationException;
import roomescape.domain.*;
import roomescape.service.param.CreateReservationParam;
import roomescape.service.result.ReservationResult;
import roomescape.service.result.ReservationTimeResult;
import roomescape.service.result.ThemeResult;

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

    public Long create(CreateReservationParam createReservationParam) {
        ReservationTime reservationTime = reservationTImeRepository.findById(createReservationParam.timeId()).orElseThrow(
                () -> new NotFoundEntityException(
                        createReservationParam.timeId() + "에 해당하는 reservation_time 튜플이 없습니다."));
        Theme theme = themeRepository.findById(createReservationParam.themeId()).orElseThrow(() -> new NotFoundEntityException(
                createReservationParam.themeId() + "에 해당하는 theme 튜플이 없습니다."));
        if (reservationRepository.existByDateAndTimeId(createReservationParam.date(), reservationTime.id())) {
            throw new BusinessRuleViolationException("날짜와 시간이 중복된 예약이 존재합니다.");
        }
        return reservationRepository.create(
                new Reservation(
                        createReservationParam.name(),
                        LocalDateTime.now(),
                        createReservationParam.date(),
                        reservationTime,
                        theme));
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
                .orElseThrow(() -> new NotFoundEntityException(reservationId + "에 해당하는 reservation 튜플이 없습니다."));
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
}
