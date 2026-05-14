package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.reservation.CreateReservationRequest;
import roomescape.dto.reservation.ReservationResponses;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.InvalidReservationDateTimeException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final TimeProvider timeProvider;

    public ReservationService(ReservationRepository reservationRepository, ThemeRepository themeRepository,
                              ReservationTimeRepository reservationTimeRepository, TimeProvider timeProvider) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.timeProvider = timeProvider;
    }

    public ReservationResponses getReservations(int page, int size, String name) {
        List<Reservation> reservations = fetchReservations(page, size, name);
        boolean hasNext = reservations.size() > size;
        if (hasNext) {
            reservations = reservations.subList(0, size);
        }
        return ReservationResponses.of(reservations, hasNext);
    }

    private List<Reservation> fetchReservations(int page, int size, String name) {
        if (name == null) {
            return reservationRepository.findAll(size + 1, page * size);
        }
        return reservationRepository.findAllByName(name, size + 1, page * size);
    }

    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("예약", id));
    }

    @Transactional
    public Reservation createReservation(CreateReservationRequest request) {
        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new ResourceNotFoundException("테마", request.themeId()));
        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new ResourceNotFoundException("예약 시간", request.timeId()));
        Reservation newReservation = new Reservation(null, request.name(), theme, request.date(), reservationTime);

        validateNotPastDateTime(newReservation);
        validateNotDuplicated(newReservation);

        Long newReservationId = reservationRepository.save(newReservation);
        return newReservation.withId(newReservationId);
    }

    public void deleteReservation(Long id) {
        int affected = reservationRepository.deleteById(id);
        if (affected == 0) {
            throw new ResourceNotFoundException("예약", id);
        }
    }

    private void validateNotPastDateTime(Reservation reservation) {
        if (reservation.isInPast(timeProvider.currentDateTime())) {
            throw new InvalidReservationDateTimeException();
        }
    }

    private void validateNotDuplicated(Reservation reservation) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(
                reservation.getDate(), reservation.getTime().getId(), reservation.getTheme().getId())) {
            throw new DuplicateReservationException();
        }
    }
}
