package roomescape.reservation_time.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation_time.application.converter.ReservationTimeConverter;
import roomescape.reservation_time.application.dto.CreateReservationTimeServiceRequest;
import roomescape.reservation_time.domain.ReservationTime;
import roomescape.reservation_time.domain.ReservationTimeId;
import roomescape.reservation_time.domain.ReservationTimeRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReservationTimeCommandUseCaseImpl implements ReservationTimeCommandUseCase {

    private final ReservationTimeRepository reservationTimeRepository;

    @Override
    public ReservationTime create(final CreateReservationTimeServiceRequest createReservationTimeServiceRequest) {
        return reservationTimeRepository.save(
                ReservationTimeConverter.toDomain(createReservationTimeServiceRequest));
    }

    @Override
    public void delete(final ReservationTimeId id) {
        if (reservationTimeRepository.existsById(id)) {
            reservationTimeRepository.deleteById(id);
            return;
        }

        throw new NoSuchElementException();
    }
}
