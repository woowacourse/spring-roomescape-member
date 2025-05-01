package roomescape.reservation.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.application.dto.AvailableReservationTimeServiceRequest;
import roomescape.reservation.application.dto.AvailableReservationTimeServiceResponse;
import roomescape.reservation.application.dto.ThemeToBookCountServiceResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.theme.domain.ThemeId;
import roomescape.time.application.usecase.ReservationTimeQueryUseCase;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReservationQueryUseCaseImpl implements ReservationQueryUseCase {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeQueryUseCase reservationTimeQueryUseCase;

    @Override
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Override
    public List<AvailableReservationTimeServiceResponse> getTimesWithAvailability(
            final AvailableReservationTimeServiceRequest availableReservationTimeServiceRequest) {
        final List<ReservationTime> allTimes = reservationTimeQueryUseCase.getAll();

        final Set<ReservationTimeId> bookedTimeIds = new HashSet<>(reservationRepository.findTimeIdByParams(
                ReservationDate.from(availableReservationTimeServiceRequest.date()),
                availableReservationTimeServiceRequest.themeId())
        );

        final List<AvailableReservationTimeServiceResponse> responses = new ArrayList<>();

        for (final ReservationTime reservationTime : allTimes) {
            final boolean isBooked = bookedTimeIds.contains(reservationTime.getId());
            responses.add(new AvailableReservationTimeServiceResponse(
                    reservationTime.getValue(),
                    reservationTime.getId().getValue(),
                    isBooked));
        }

        return responses;
    }

    @Override
    public List<ThemeToBookCountServiceResponse> getRanking(final ReservationDate startDate,
                                                            final ReservationDate endDate,
                                                            final int bookCount) {

        return reservationRepository.findThemesToBookedCountByParamsOrderByBookedCount(startDate, endDate, bookCount)
                .entrySet().stream()
                .map(entry -> new ThemeToBookCountServiceResponse(entry.getKey(), entry.getValue()))
                .toList();
    }

    @Override
    public boolean existsByTimeId(final ReservationTimeId timeId) {
        return reservationRepository.existsByParams(timeId);
    }

    @Override
    public boolean existsByParams(final ReservationDate date,
                                  final ReservationTimeId timeId,
                                  final ThemeId themeId) {
        return reservationRepository.existsByParams(date, timeId, themeId);
    }
}
