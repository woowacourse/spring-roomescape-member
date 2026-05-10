package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.reservation.CreateReservationRequest;
import roomescape.dto.reservation.ReservationResponses;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository, ThemeRepository themeRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationResponses getReservations(int page, int size) {
        List<Reservation> reservations = reservationRepository.findAll(size + 1, page * size);
        boolean hasNext = reservations.size() > size;
        if (hasNext) {
            reservations = reservations.subList(0, size);
        }
        return ReservationResponses.of(reservations, hasNext);
    }

    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id);
    }

    @Transactional
    public Reservation createReservation(CreateReservationRequest request) {
        Theme theme = themeRepository.findById(request.themeId());
        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId());
        Reservation newReservation = new Reservation(null, request.name(), theme, request.date(), reservationTime);

        validateNotDuplicated(newReservation);

        Long newReservationId = reservationRepository.save(newReservation);
        return newReservation.withId(newReservationId);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    private void validateNotDuplicated(Reservation reservation) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(
                reservation.getDate(), reservation.getTime().getId(), reservation.getTheme().getId())) {
            throw new IllegalStateException("해당 날짜·시간·테마에 이미 예약이 존재합니다.");
        }
    }
}
