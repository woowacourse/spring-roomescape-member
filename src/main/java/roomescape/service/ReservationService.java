package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.reservation.CreateReservationRequest;
import roomescape.dto.reservation.ReservationResponses;
import roomescape.dto.reservation.UpdateReservationRequest;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.PastDateTimeReservationException;
import roomescape.exception.PastReservationModificationException;
import roomescape.exception.ReservationOwnerMismatchException;
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

    @Transactional
    public Reservation updateOwnReservation(Long id, UpdateReservationRequest request) {
        Reservation existing = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("예약", id));
        validateReservationOwner(request.name(), existing);
        validateExistingNotInPast(existing);

        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new ResourceNotFoundException("테마", request.themeId()));
        ReservationTime time = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new ResourceNotFoundException("예약 시간", request.timeId()));
        Reservation updated = new Reservation(id, existing.getName(), theme, request.date(), time);

        validateNotPastDateTime(updated);
        validateNotDuplicatedForUpdate(existing, updated);

        reservationRepository.update(updated);
        return updated;
    }

    private void validateNotDuplicatedForUpdate(Reservation existing, Reservation updated) {
        if (existing.hasSameSlot(updated)) {
            return;
        }
        validateNotDuplicated(updated);
    }

    public void deleteReservation(Long id) {
        int affected = reservationRepository.deleteById(id);
        if (affected == 0) {
            throw new ResourceNotFoundException("예약", id);
        }
    }

    public void cancelOwnReservation(Long id, String name) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("예약", id));
        validateReservationOwner(name, reservation);
        validateExistingNotInPast(reservation);

        reservationRepository.deleteById(id);
    }

    private static void validateReservationOwner(String name, Reservation reservation) {
        if (!reservation.getName().equals(name)) {
            throw new ReservationOwnerMismatchException();
        }
    }

    private void validateNotPastDateTime(Reservation reservation) {
        if (reservation.isInPast(timeProvider.currentDateTime())) {
            throw new PastDateTimeReservationException();
        }
    }

    private void validateExistingNotInPast(Reservation existing) {
        if (existing.isInPast(timeProvider.currentDateTime())) {
            throw new PastReservationModificationException();
        }
    }

    private void validateNotDuplicated(Reservation reservation) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(
                reservation.getDate(), reservation.getTime().getId(), reservation.getTheme().getId())) {
            throw new DuplicateReservationException();
        }
    }
}