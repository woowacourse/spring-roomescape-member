package roomescape.reservation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.application.converter.ReservationConverter;
import roomescape.reservation.application.dto.CreateReservationServiceRequest;
import roomescape.reservation.application.usecase.ReservationCommandUseCase;
import roomescape.reservation.application.usecase.ReservationQueryUseCase;
import roomescape.reservation.domain.ReservationId;
import roomescape.reservation.ui.dto.CreateReservationWebRequest;
import roomescape.reservation.ui.dto.ReservationResponse;
import roomescape.reservation_time.application.usecase.ReservationTimeQueryUseCase;
import roomescape.reservation_time.domain.ReservationTime;
import roomescape.reservation_time.domain.ReservationTimeId;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationQueryUseCase reservationQueryUseCase;
    private final ReservationCommandUseCase reservationCommandUseCase;
    private final ReservationTimeQueryUseCase reservationTimeQueryUseCase;

    @Override
    public List<ReservationResponse> getAll() {
        return ReservationConverter.toDto(
                reservationQueryUseCase.getAll());
    }

    @Override
    public ReservationResponse create(final CreateReservationWebRequest createReservationWebRequest) {
        final ReservationTime reservationTime = reservationTimeQueryUseCase.get(
                ReservationTimeId.from(createReservationWebRequest.timeId()));

        return ReservationConverter.toDto(
                reservationCommandUseCase.create(
                        new CreateReservationServiceRequest(
                                createReservationWebRequest.name(),
                                createReservationWebRequest.date(),
                                reservationTime)));
    }

    @Override
    public void delete(final ReservationId id) {
        reservationCommandUseCase.delete(id);
    }
}
