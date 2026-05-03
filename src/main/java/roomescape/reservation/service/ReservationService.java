package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    @Transactional
    public Reservation save(ReservationCommand command) {
        ReservationTime time = reservationTimeRepository.findById(command.timeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 예약 시간이 존재하지 않습니다."));

        Reservation reservation = Reservation.create(command.name(), command.date(), time);

        if (reservationRepository.existsByDateAndTimeId(reservation.getDate(), command.timeId())) {
            throw new IllegalStateException("이미 해당 날짜와 시간에 예약이 존재합니다.");
        }

        return reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }
}
