package roomescape.service;

import common.exception.ErrorCode;
import common.exception.RoomEscapeException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.ThemeCreateRequest;
import roomescape.controller.dto.ThemeFamousFindRequest;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeName;
import roomescape.repository.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ThemeService {
    private static final long DEFAULT_DAYS = 7;
    private static final long DEFAULT_LIMIT = 10;

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme find(long themeId) {
        return themeRepository.findById(themeId).orElseThrow(
                () -> new RoomEscapeException(ErrorCode.THEME_NOT_FOUND));
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    @Transactional
    public Theme create(ThemeCreateRequest request) {
        Theme theme = Theme.of(new ThemeName(request.getName()), request.getDescription(), request.getThumbnailUrl());
        return themeRepository.save(theme);
    }

    @Transactional
    public void delete(long id) {
        if (!themeRepository.existsById(id)) {
            throw new RoomEscapeException(ErrorCode.THEME_NOT_FOUND);
        }
        themeRepository.deleteById(id);
    }

    public List<Theme> findFamous(ThemeFamousFindRequest request) {
        Long days = request.getDays();
        LocalDate date = request.getDate();
        Long limit = request.getLimit();

        if (days == null) {
            days = DEFAULT_DAYS;
        }
        if (limit == null) {
            limit = DEFAULT_LIMIT;
        }
        if (date == null) {
            date = LocalDate.now();
        }
        return themeRepository.findFamous(days, date, limit);
    }
}
