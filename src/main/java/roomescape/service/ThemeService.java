package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.global.exception.DuplicateEntityException;
import roomescape.global.exception.EntityNotFoundException;
import roomescape.repository.ThemeRepository;
import roomescape.web.dto.theme.ThemeRequest;
import roomescape.web.dto.theme.ThemeResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;

    @Transactional
    public ThemeResponse register(ThemeRequest request) {
        validateDuplicateName(request.name());

        Theme theme = Theme.create(request.name(), request.description(), request.thumbnailImageUrl());
        return ThemeResponse.from(themeRepository.save(theme));
    }

    @Transactional
    public void deactivate(Long id) {
        Theme theme = themeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 테마 정보입니다."));

        Theme inactiveTheme = theme.deactivate();
        themeRepository.update(inactiveTheme);
    }

    public List<ThemeResponse> getAllActiveThemesByPaging(int page, int size) {
        return themeRepository.findAllActiveThemesByPaging(page, size)
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> getPopularThemes(LocalDate startDate, LocalDate endDate, int limit) {
        return themeRepository.findTopThemesByReservationCount(startDate, endDate, limit)
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    private void validateDuplicateName(String name) {
        if (themeRepository.isActiveByName(name)) {
            throw new DuplicateEntityException("이미 존재하는 테마입니다. 테마 명: %s", name);
        }
    }
}
