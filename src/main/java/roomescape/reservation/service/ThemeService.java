package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.global.exception.error.ConflictException;
import roomescape.reservation.controller.dto.ThemeRankingResponse;
import roomescape.reservation.controller.dto.ThemeRequest;
import roomescape.reservation.controller.dto.ThemeResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ThemeRepository;

@Service
public class ThemeService {

    private static final int DAYS_BEFORE_START = 7;
    private static final int DAYS_BEFORE_END = 1;
    private static final int RANKING_LIMIT = 10;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ThemeResponse add(ThemeRequest request) {
        Theme themeWithoutId = request.toThemeWithoutId();
        Long id = themeRepository.saveAndReturnId(themeWithoutId);

        Theme theme = themeWithoutId.withId(id);
        return ThemeResponse.from(theme);
    }

    public List<ThemeResponse> getThemes() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void remove(Long id) {
        validateReservationConstraint(id);
        themeRepository.deleteById(id);
    }

    private void validateReservationConstraint(Long id) {
        List<Reservation> constraintReservations = reservationRepository.findAllByThemeId(id);
        if (!constraintReservations.isEmpty()) {
            throw new ConflictException("해당 테마와 연관된 예약이 있어 삭제할 수 없습니다.");
        }
    }

    public List<ThemeRankingResponse> getThemeRankings() {
        LocalDate startDate = LocalDate.now().minusDays(DAYS_BEFORE_START);
        LocalDate endDate = LocalDate.now().minusDays(DAYS_BEFORE_END);
        List<Long> themeRankIds = themeRepository.findTopThemeIdByDateRange(startDate, endDate, RANKING_LIMIT);
        List<Theme> unorderedThemes = themeRepository.findByIdIn(themeRankIds);

        return sortByInOrder(unorderedThemes, themeRankIds)
                .stream()
                .map(ThemeRankingResponse::from)
                .toList();
    }

    private List<Theme> sortByInOrder(List<Theme> themes, List<Long> idOrder) {
        Map<Long, Theme> idThemes = themes.stream()
                .collect(Collectors.toMap(Theme::getId, Function.identity()));

        return idOrder.stream()
                .map(idThemes::get)
                .toList();
    }

}
