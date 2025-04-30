package roomescape.domain.reservation.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.dto.ThemeRequest;
import roomescape.domain.reservation.dto.ThemeResponse;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ThemeRepository;

@Service
public class ThemeService {

    private static final int START_DATE_OFFSET = 8;
    private static final int END_DATE_OFFSET = 1;

    private final Clock clock;
    private final ThemeRepository themeRepository;

    public ThemeService(Clock clock, ThemeRepository themeRepository) {
        this.clock = clock;
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponse> getAll() {
        return themeRepository.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse create(ThemeRequest request) {
        Theme theme = themeRepository.save(Theme.withoutId(request.name(), request.description(), request.thumbnail()));
        return ThemeResponse.from(theme);
    }

    public void delete(Long id) {
        // TODO 이미 예약에서 사용할 경우 AlreadyInuse 예외 발생


        themeRepository.deleteById(id);
    }

    public List<ThemeResponse> getPopularThemes(){
        LocalDate now = getNow();

        LocalDate startDate = now.minusDays(START_DATE_OFFSET);
        LocalDate endDate = now.minusDays(END_DATE_OFFSET);

        return themeRepository.findPopularThemes(startDate, endDate)
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    private LocalDate getNow(){
        return LocalDate.now(clock);
    }
}
