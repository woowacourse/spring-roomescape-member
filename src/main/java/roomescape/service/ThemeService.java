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
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse register(ThemeRequest themeRequest) {
        themeRequestValidate(themeRequest);
        Theme theme = new Theme(null, themeRequest.name(), themeRequest.description(), themeRequest.url());
        Theme saved = themeRepository.save(theme);
        return ThemeResponse.from(saved);
    }

    private void themeRequestValidate(ThemeRequest themeRequest) {
        if (themeRequest.name() == null) {
            throw new RoomescapeException(ErrorCode.THEME_BLANK_NAME);
        }
        if (themeRequest.url() == null) {
            throw new RoomescapeException(ErrorCode.THEME_BLANK_URL);
        }
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
