package roomescape.reservation.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.application.converter.ReservationConverter;
import roomescape.reservation.application.dto.CreateReservationServiceRequest;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationId;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.theme.application.usecase.ThemeQueryUseCase;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeId;
import roomescape.time.application.usecase.ReservationTimeQueryUseCase;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeId;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReservationCommandUseCaseImpl implements ReservationCommandUseCase {

    private final ReservationRepository reservationRepository;
    private final ReservationQueryUseCase reservationQueryUseCase;
    private final ReservationTimeQueryUseCase reservationTimeQueryUseCase;
    private final ThemeQueryUseCase themeQueryUseCase;

    @Override
    public Reservation create(final CreateReservationServiceRequest createReservationServiceRequest) {
        if (reservationQueryUseCase.existsByParams(
                ReservationDate.from(createReservationServiceRequest.date()),
                ReservationTimeId.from(createReservationServiceRequest.timeId()),
                ThemeId.from(createReservationServiceRequest.themeId()))) {

            throw new IllegalStateException("추가하려는 예약이 이미 존재합니다.");
        }

        final ReservationTime reservationTime = reservationTimeQueryUseCase.get(
                ReservationTimeId.from(createReservationServiceRequest.timeId()));

        final Theme theme = themeQueryUseCase.get(
                ThemeId.from(createReservationServiceRequest.themeId()));

        return reservationRepository.save(
                ReservationConverter.toDomain(createReservationServiceRequest, reservationTime, theme));
    }

    @Override
    public void delete(final ReservationId id) {
        if (reservationRepository.existsByParams(id)) {
            reservationRepository.deleteById(id);
            return;
        }

        throw new NoSuchElementException();
    }
}
