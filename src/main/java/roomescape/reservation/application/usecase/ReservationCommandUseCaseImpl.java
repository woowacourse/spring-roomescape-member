package roomescape.reservation.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.application.dto.CreateReservationServiceRequest;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationId;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.exception.DuplicateReservationException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.theme.application.usecase.ThemeQueryUseCase;
import roomescape.theme.domain.Theme;
import roomescape.time.application.usecase.ReservationTimeQueryUseCase;
import roomescape.time.domain.ReservationTime;

@Service
@RequiredArgsConstructor
public class ReservationCommandUseCaseImpl implements ReservationCommandUseCase {

    private final ReservationRepository reservationRepository;
    private final ReservationQueryUseCase reservationQueryUseCase;
    private final ReservationTimeQueryUseCase reservationTimeQueryUseCase;
    private final ThemeQueryUseCase themeQueryUseCase;

    @Override
    public Reservation create(final CreateReservationServiceRequest request) {
        if (reservationQueryUseCase.existsByParams(
                request.date(),
                request.timeId(),
                request.themeId())
        ) {
            throw new DuplicateReservationException(
                    request.date(),
                    request.timeId(),
                    request.themeId());
        }

        final ReservationTime reservationTime = reservationTimeQueryUseCase.get(
                request.timeId());

        final Theme theme = themeQueryUseCase.get(
                request.themeId());

        return reservationRepository.save(
                request.toDomain(reservationTime, theme));
    }

    @Override
    public void delete(final ReservationId id) {
        if (reservationRepository.existsByParams(id)) {
            reservationRepository.deleteById(id);
            return;
        }

        throw new ReservationNotFoundException(id);
    }
}
