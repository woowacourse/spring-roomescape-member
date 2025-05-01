package roomescape.theme.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.application.usecase.ReservationQueryUseCase;
import roomescape.reservation.application.usecase.ThemeToBookCountServiceResponse;
import roomescape.reservation.domain.ReservationDate;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeId;
import roomescape.theme.domain.ThemeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeQueryUseCaseImpl implements ThemeQueryUseCase {

    private final ThemeRepository themeRepository;
    private final ReservationQueryUseCase reservationQueryUseCase;

    @Override
    public List<Theme> getAll() {
        return themeRepository.findAll();
    }

    @Override
    public Theme get(final ThemeId id) {
        return themeRepository.findById(id)
                .orElseThrow();
    }

    @Override
    public List<Theme> getRanking(final ReservationDate startDate, final ReservationDate endDate, final int count) {
        return reservationQueryUseCase.getRanking(startDate, endDate, count).stream()
                .map(ThemeToBookCountServiceResponse::theme)
                .toList();
    }

}
