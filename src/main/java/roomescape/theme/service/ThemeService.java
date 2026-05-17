package roomescape.theme.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;
import roomescape.theme.dto.*;
import roomescape.theme.model.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.exception.ErrorCode;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemesResponse findAll() {
        List<Theme> themes = themeRepository.findAll();
        return ThemesResponse.from(themes);
    }

    @Transactional
    public Long create(ThemeRequest request) {
        Theme theme = new Theme(request.name(), request.description(), request.imageUrl(), request.requiredTime());
        return themeRepository.create(theme);
    }

    @Transactional
    public void delete(Long id) {
        try {
            int deletedRows = themeRepository.delete(id);
            if (deletedRows == 0) {
                throw new NotFoundException(ErrorCode.THEME_NOT_FOUND);
            }
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(ErrorCode.THEME_IN_USE);
        }
    }

    public PopularThemesResponse findPopularThemes(String sort, int limit, int days) {
        List<PopularThemeResponse> responses = themeRepository.findPopularThemes(sort, limit, days);
        return PopularThemesResponse.from(responses);
    }

    public Theme findById(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.THEME_NOT_FOUND));
    }
}
