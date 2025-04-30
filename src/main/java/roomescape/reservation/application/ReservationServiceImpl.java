package roomescape.reservation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.application.converter.ReservationConverter;
import roomescape.reservation.application.dto.AvailableReservationTimeServiceRequest;
import roomescape.reservation.application.dto.CreateReservationServiceRequest;
import roomescape.reservation.application.usecase.ReservationCommandUseCase;
import roomescape.reservation.application.usecase.ReservationQueryUseCase;
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
        return ReservationConverter.toDto(
                reservationQueryUseCase.getAll());
    }

    // TODO: Controller to Service에서는 Long으로 (래핑된 ID로 변환하는 단 수정하기)
    @Override
    public List<AvailableReservationTimeWebResponse> getAvailable(final LocalDate date, final ThemeId themeId) {
        final AvailableReservationTimeServiceRequest serviceRequest = new AvailableReservationTimeServiceRequest(
                date,
                themeId);

        return reservationQueryUseCase.getTimesWithAvailability(serviceRequest).stream()
                .map(ReservationConverter::toWebDto)
                .toList();
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
