package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.PopularThemeResponse;
import roomescape.theme.dto.ThemeCreateRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.exception.ThemeException;
import roomescape.theme.repository.ThemeRepository;

@RequiredArgsConstructor
@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeResponse findById(Long id) {
        return ThemeResponse.from(themeRepository.findById(id)
                .orElseThrow(() -> new ThemeException("[ERROR] 존재하지 않는 테마 입니다.")));
    }

    public List<ThemeResponse> findAll() {
        List<Theme> themes = themeRepository.findAll();

        return themes.stream().map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse save(ThemeCreateRequest request) {
        Theme theme = request.toEntity();
        validateDuplicateTheme(theme);

        return ThemeResponse.from(themeRepository.save(theme));
    }

    public int delete(long id) {
        // 삭제 전 예약들 확인
        // 예약에서 테마가 사용중인지 확인
        // 예약 내역에 테마가 해당 예약의 날짜가 이미 지난 날인지 확인
        return themeRepository.delete(id);
    }

    private void validateDuplicateTheme(Theme theme) {
        if (themeRepository.existsByNameAndDescription(theme)) {
            throw new ThemeException("[ERROR] 이름과 설명이 같은 테마가 이미 존재합니다.");
        }
    }

    public List<PopularThemeResponse> findPopularThemes(LocalDate today) {
        return themeRepository.findTop10PopularThemesBetween(today.minusWeeks(1), today.minusDays(1))
                .stream()
                .map(PopularThemeResponse::from)
                .toList();
    }
}
