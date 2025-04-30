package roomescape.time.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.time.application.converter.ReservationTimeConverter;
import roomescape.time.application.dto.CreateReservationTimeServiceRequest;
import roomescape.time.application.usecase.ReservationTimeCommandUseCase;
import roomescape.time.application.usecase.ReservationTimeQueryUseCase;
import roomescape.time.domain.ReservationTimeId;
import roomescape.time.ui.dto.CreateReservationTimeWebRequest;
import roomescape.time.ui.dto.ReservationTimeResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationTimeServiceImpl implements ReservationTimeService {

    private final ReservationTimeQueryUseCase reservationTimeQueryUseCase;
    private final ReservationTimeCommandUseCase reservationTimeCommandUseCase;

    @Override
    public List<ReservationTimeResponse> getAll() {
        return ReservationTimeConverter.toDto(
                reservationTimeQueryUseCase.getAll());
    }

    @Override
    public ReservationTimeResponse create(final CreateReservationTimeWebRequest createReservationTimeWebRequest) {
        return ReservationTimeConverter.toDto(
                reservationTimeCommandUseCase.create(
                        new CreateReservationTimeServiceRequest(
                                createReservationTimeWebRequest.startAt())));
    }

    @Override
    public void delete(final ReservationTimeId id) {
        reservationTimeCommandUseCase.delete(id);
    }
}
