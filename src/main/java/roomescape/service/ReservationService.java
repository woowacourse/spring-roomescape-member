package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.SaveReservationRequest;
import roomescape.dto.SaveReservationTimeRequest;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(
            final ReservationRepository reservationRepository,
            final ReservationTimeRepository reservationTimeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    public Reservation saveReservation(final SaveReservationRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new NoSuchElementException("해당 id의 예약 시간이 존재하지 않습니다."));

        return reservationRepository.save(request.toReservation(reservationTime));
    }

    public void deleteReservation(final Long reservationId) {
        checkReservationExist(reservationId);
        reservationRepository.deleteById(reservationId);
    }

    private void checkReservationExist(final Long reservationId) {
        reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NoSuchElementException("해당 id의 예약이 존재하지 않습니다."));
    }

    public List<ReservationTime> getReservationTimes() {
        return reservationTimeRepository.findAll();
    }

    public ReservationTime saveReservationTime(final SaveReservationTimeRequest request) {
        return reservationTimeRepository.save(request.toReservationTime());
    }

    public void deleteReservationTime(final Long reservationTimeId) {
        checkReservationTimeExist(reservationTimeId);
        reservationTimeRepository.deleteById(reservationTimeId);
    }

    private void checkReservationTimeExist(final Long reservationTimeId) {
        reservationTimeRepository.findById(reservationTimeId)
                .orElseThrow(() -> new NoSuchElementException("해당 id의 예약 시간이 존재하지 않습니다."));
    }
}
