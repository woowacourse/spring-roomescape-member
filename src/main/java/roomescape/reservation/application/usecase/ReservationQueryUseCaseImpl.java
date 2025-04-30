package roomescape.reservation.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.time.domain.ReservationTimeId;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationQueryUseCaseImpl implements ReservationQueryUseCase {

    private final ReservationRepository reservationRepository;

    @Override
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Override
    public boolean existsByTimeId(final ReservationTimeId timeId) {
        return reservationRepository.existsByParams(timeId);
    }

    @Override
    public boolean existsByParams(final LocalDate date,
                                  final ReservationTimeId timeId,
                                  final ThemeId themeId) {
        return reservationRepository.existsByParams(date, timeId, themeId);
    }
}
