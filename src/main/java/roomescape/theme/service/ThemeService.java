package roomescape.theme.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.controller.dto.ThemeRankingResponse;
import roomescape.theme.controller.dto.ThemeRequest;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ThemeService {

    private static final int DAYS_BEFORE_START = 7;
    private static final int DAYS_BEFORE_END = 1;
    private static final int RANKING_LIMIT = 10;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;
    private final Clock clock;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository, Clock clock) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
        this.clock = clock;
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
        if (reservationRepository.existReservationByThemeId(id)) {
            throw new IllegalStateException("해당 테마와 연관된 예약이 있어 삭제할 수 없습니다.");
        }
        themeRepository.deleteById(id);
    }

    public List<ThemeRankingResponse> getThemeRankings() {
        LocalDate startDate = LocalDate.now(clock).minusDays(DAYS_BEFORE_START);
        LocalDate endDate = LocalDate.now(clock).minusDays(DAYS_BEFORE_END);
        List<Long> themeRankIds = themeRepository.findTopThemeIdByDateRange(startDate, endDate, RANKING_LIMIT);
        List<Theme> unorderedThemes = themeRepository.findByIdIn(themeRankIds);

        return sortByInOrder(unorderedThemes, themeRankIds)
                .stream()
                .map(ThemeRankingResponse::from)
                .toList();
    }

    private List<Theme> sortByInOrder(List<Theme> themes, List<Long> idOrder){
        Map<Long, Theme> idThemes = themes.stream()
                .collect(Collectors.toMap(Theme::getId, Function.identity()));

        return idOrder.stream()
                .map(idThemes::get)
                .toList();
    }

}
