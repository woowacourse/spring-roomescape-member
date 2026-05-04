package roomescape.reservation;

import java.time.LocalDate;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.ReservationTimeRepository;
import roomescape.reservationtime.ReservationTimeNotFoundException;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    @Transactional
    public Reservation createReservation(String name, LocalDate date, long timeId) {
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new ReservationTimeNotFoundException("예약 시간을 찾을 수 없습니다"));
        try {
            return reservationRepository.save(name, date, reservationTime);
        } catch (DuplicateKeyException e) {
            throw new DuplicateReservationException("해당 날짜의 해당 시간은 이미 예약되었습니다");
        } catch (DataIntegrityViolationException e) {
            throw new ReservationException("요청이 데이터 무결성 조건을 위반했습니다");
        }
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public void deleteReservation(long id) {
        reservationRepository.delete(id);
    }
}
