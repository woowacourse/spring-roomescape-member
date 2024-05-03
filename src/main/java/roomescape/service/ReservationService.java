package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private static final int MAX_RESERVATIONS_PER_TIME = 1;

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ReservationResponse create(Reservation reservation) {
        List<Reservation> reservationsInSameDateTime = reservationRepository.findAllByDateAndTimeAndThemeId(
                reservation.getDate(), reservation.getTime(), reservation.getThemeId());

        validateDuplicatedReservation(reservationsInSameDateTime);

        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    private void validateDuplicatedReservation(List<Reservation> reservationsInSameDateTime) {
        if (reservationsInSameDateTime.size() >= MAX_RESERVATIONS_PER_TIME) {
            throw new BadRequestException("해당 시간대에 예약이 모두 찼습니다.");
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
        boolean isExist = reservationRepository.existById(id);
        if (!isExist) {
            throw new NotFoundException("해당 ID의 예약이 없습니다.");
        }
        reservationRepository.deleteById(id);
    }
}
