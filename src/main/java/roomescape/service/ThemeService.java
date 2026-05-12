package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.ThemeAllResponse;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ThemeResponse register(ThemeRequest themeRequest) {
        Theme theme = new Theme(null, themeRequest.name(), themeRequest.description(), themeRequest.url());
        Theme saved = themeRepository.save(theme);
        return ThemeResponse.from(saved);
    }

    public void removeById(Long id) {
        int deleteCnt = themeRepository.deleteById(id);
        if (deleteCnt == 0) {
            throw new RoomescapeException(ErrorCode.THEME_NOT_FOUND);
        }
    }

    public ThemeAllResponse readAll() {
        List<Theme> themes = themeRepository.findAll();
        List<ThemeResponse> responses = themes.stream()
                .map(ThemeResponse::from)
                .toList();
        return new ThemeAllResponse(responses);
    }

    public ThemeAllResponse readRanks(Long limit) {
        LocalDate currentDay = LocalDate.now().minusDays(1);
        LocalDate lastWeekDay = LocalDate.now().minusWeeks(1);
        List<Theme> themes = themeRepository.findByCurrentDateAndLastWeekDateAndLimit(currentDay.toString(),
                lastWeekDay.toString(), limit);

        List<ThemeResponse> responses = themes.stream()
                .map(ThemeResponse::from)
                .toList();
        return new ThemeAllResponse(responses);
    }
}
