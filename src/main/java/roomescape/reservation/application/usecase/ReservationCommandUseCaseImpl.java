package roomescape.reservation.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.application.converter.ReservationConverter;
import roomescape.reservation.application.dto.CreateReservationServiceRequest;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationId;
import roomescape.reservation.domain.ReservationRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReservationCommandUseCaseImpl implements ReservationCommandUseCase {

    private final ReservationRepository reservationRepository;

    @Override
    public Reservation create(final CreateReservationServiceRequest createReservationServiceRequest) {
        return reservationRepository.save(
                ReservationConverter.toDomain(createReservationServiceRequest));
    }

    @Override
    public void delete(final ReservationId id) {
        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
            return;
        }

        throw new NoSuchElementException();
    }
}
