package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.service.param.CreateReservationParam;
import roomescape.service.result.ReservationResult;
import roomescape.service.result.ReservationTimeResult;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationTimeRepository reservationTImeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationTimeRepository reservationTImeRepository,
                              ReservationRepository reservationRepository) {
        this.reservationTImeRepository = reservationTImeRepository;
        this.reservationRepository = reservationRepository;
    }

    public Long create(CreateReservationParam createReservationParam) {
        ReservationTime reservationTime = reservationTImeRepository.findById(createReservationParam.timeId()).orElseThrow(
                () -> new IllegalArgumentException(
                        createReservationParam.timeId() + "에 해당하는 reservation_time 튜플이 없습니다."));
        if (reservationRepository.existByDateAndTimeId(createReservationParam.date(), reservationTime.id())) {
            throw new IllegalArgumentException("날짜와 시간이 중복된 예약이 존재합니다.");
        }
        return reservationRepository.create(
                new Reservation(
                        createReservationParam.name(),
                        LocalDateTime.now(),
                        createReservationParam.date(),
                        reservationTime));
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
                new ReservationTimeResult(
                        reservation.getTime().id(),
                        reservation.getTime().startAt()));
    }
}
