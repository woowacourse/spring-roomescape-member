package roomescape.reservation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.application.dto.AvailableReservationTimeServiceRequest;
import roomescape.reservation.application.usecase.ReservationCommandUseCase;
import roomescape.reservation.application.usecase.ReservationQueryUseCase;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationId;
import roomescape.reservation.ui.dto.AvailableReservationTimeWebResponse;
import roomescape.reservation.ui.dto.CreateReservationWebRequest;
import roomescape.reservation.ui.dto.ReservationResponse;
import roomescape.theme.domain.ThemeId;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationQueryUseCase reservationQueryUseCase;
    private final ReservationCommandUseCase reservationCommandUseCase;

    @Override
    public List<ReservationResponse> getAll() {
        return ReservationResponse.from(
                reservationQueryUseCase.getAll());
    }

    @Override
    public List<AvailableReservationTimeWebResponse> getAvailable(final LocalDate date, final Long id) {
        final AvailableReservationTimeServiceRequest request = new AvailableReservationTimeServiceRequest(
                ReservationDate.from(date),
                ThemeId.from(id));

        return reservationQueryUseCase.getTimesWithAvailability(request).stream()
                .map(AvailableReservationTimeWebResponse::from)
                .toList();
    }

    @Override
    public ReservationResponse create(final CreateReservationWebRequest request) {
        return ReservationResponse.from(
                reservationCommandUseCase.create(
                        request.toServiceRequest()));
    }

    @Override
    public void delete(final Long id) {
        reservationCommandUseCase.delete(ReservationId.from(id));
    }
}
