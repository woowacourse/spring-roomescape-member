package roomescape.theme.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.exception.NotFoundException;
import roomescape.theme.domain.Theme;
import roomescape.theme.application.dto.ThemeCreateCommand;
import roomescape.theme.application.dto.ThemeResult;
import roomescape.theme.application.exception.ThemeException;
import roomescape.theme.domain.repository.ThemeRepository;

@RequiredArgsConstructor
@Transactional
@Service
public class ThemeCommandService {

    private final ThemeRepository themeRepository;

    public ThemeResult save(ThemeCreateCommand request) {
        Theme theme = request.toEntity();
        validateDuplicateTheme(theme);

        return ThemeResult.from(themeRepository.save(theme));
    }

    public void delete(long id) {
        if (themeRepository.delete(id) == 0) {
            throw new NotFoundException("존재하지 않는 테마입니다.");
        }
    }

    private void validateDuplicateTheme(Theme theme) {
        if (themeRepository.existsByNameAndDescription(theme)) {
            throw new ThemeException("이름과 설명이 같은 테마가 이미 존재합니다.");
        }
    }
}
