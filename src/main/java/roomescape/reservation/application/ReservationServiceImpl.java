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

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationQueryUseCase reservationQueryUseCase;
    private final ReservationCommandUseCase reservationCommandUseCase;

    @Override
    public List<ReservationResponse> getAll() {
        return ReservationConverter.toDto(
                reservationQueryUseCase.getAll());
    }

    @Override
    public ReservationResponse create(final CreateReservationWebRequest createReservationWebRequest) {
        return ReservationConverter.toDto(
                reservationCommandUseCase.create(
                        new CreateReservationServiceRequest(
                                createReservationWebRequest.name(),
                                createReservationWebRequest.date(),
                                createReservationWebRequest.timeId(),
                                createReservationWebRequest.themeId())));
    }

    @Override
    public void delete(final ReservationId id) {
        reservationCommandUseCase.delete(id);
    }
}
