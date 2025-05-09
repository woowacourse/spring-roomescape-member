package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.exception.conflict.ReservationConflictException;
import roomescape.exception.notFound.ReservationNotFoundException;
import roomescape.exception.notFound.ReservationTimeNotFoundException;
import roomescape.exception.notFound.ThemeNotFoundException;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.dto.request.ReservationRequest;
import roomescape.reservation.service.dto.response.ReservationResponse;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.entity.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.util.List;

// TODO: findByXXX - DataAccessException 핸들링
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository timeRepository,
            ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> getAllReservation() {
        return reservationRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private ReservationResponse convertToResponse(Reservation reservation) {
        final Long themeId = reservation.getThemeId();
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeNotFoundException(themeId));
        return ReservationResponse.from(reservation, theme);
    }

    public ReservationResponse createReservation(ReservationRequest request) {
        ReservationTime timeEntity = timeRepository.findById(request.timeId())
                .orElseThrow(() -> new ReservationTimeNotFoundException(request.timeId()));
        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new ThemeNotFoundException(request.themeId()));

        Reservation newReservation = request.toEntity(timeEntity);
        validateDuplicated(newReservation);

        Reservation saved = reservationRepository.save(newReservation);
        return ReservationResponse.from(saved, theme);
    }

    private void validateDuplicated(Reservation newReservation) {
        if (isExistDuplicatedWith(newReservation)) {
            throw new ReservationConflictException();
        }
    }

    private boolean isExistDuplicatedWith(Reservation target) {
        return reservationRepository.findDuplicatedWith(target).isPresent();
    }

    public void deleteReservation(final Long id) {
        final boolean deleted = reservationRepository.deleteById(id);
        if (!deleted) {
            throw new ReservationNotFoundException(id);
        }
    }
}
