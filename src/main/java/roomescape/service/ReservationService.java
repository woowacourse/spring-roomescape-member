package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.ViolationException;
import roomescape.repository.ReservationRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ReservationResponse create(Reservation reservation) {
        validateReservationDate(reservation);
        validateDuplicatedReservation(reservation);
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    private void validateReservationDate(Reservation reservation) {
        if (reservation.isBeforeOrOnToday()) {
            throw new ViolationException("이전 날짜 혹은 당일은 예약할 수 없습니다.");
        }
    }

    private void validateDuplicatedReservation(Reservation reservation) {
        boolean existReservationInSameTime = reservationRepository.existByDateAndTimeIdAndThemeId(
                reservation.getDate(), reservation.getReservationTimeId(), reservation.getThemeId());
        if (existReservationInSameTime) {
            throw new ViolationException("해당 시간대에 예약이 모두 찼습니다.");
        }
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }
}
