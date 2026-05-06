package roomescape.theme.application.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.theme.domain.Theme;
import roomescape.theme.application.dto.PopularThemeQueryResult;
import roomescape.theme.application.dto.ThemeCreateCommand;
import roomescape.theme.application.dto.ThemeQueryResult;
import roomescape.theme.application.exception.ThemeException;
import roomescape.theme.domain.repository.ThemeRepository;

@RequiredArgsConstructor
@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeQueryResult findById(Long id) {
        return ThemeQueryResult.from(themeRepository.findById(id)
                .orElseThrow(() -> new ThemeException("[ERROR] 존재하지 않는 테마 입니다.")));
    }

    public List<ThemeQueryResult> findAll() {
        List<Theme> themes = themeRepository.findAll();

        return themes.stream().map(ThemeQueryResult::from)
                .toList();
    }

    public ThemeQueryResult save(ThemeCreateCommand request) {
        Theme theme = request.toEntity();
        validateDuplicateTheme(theme);

        return ThemeQueryResult.from(themeRepository.save(theme));
    }

    public int delete(long id) {
        // 삭제 전 예약들 확인
        // 예약에서 테마가 사용중인지 확인
        // 예약 내역에 테마가 해당 예약의 날짜가 이미 지난 날인지 확인

        // 외래키 제약 조건으로 인한 삭제 예외 핸들러 추가 -> 이미 예약 시간이 지난 예약들로 실패했는지, 아직 예약 시간이 오지 않은 예약들로 실패했는지 구분 불가
        // delete 내부에서 서브 쿼리? 조인?으로 처리하기에는 id가 없어서 삭제가 안된건지, 해당 테마를 사용중인 예약이 존재해서 삭제가 안된건지 구분 불가
        // Service에 조회 요청을 하자니? 순환참조 발생
        return themeRepository.delete(id);
    }

    private void validateDuplicateTheme(Theme theme) {
        if (themeRepository.existsByNameAndDescription(theme)) {
            throw new ThemeException("[ERROR] 이름과 설명이 같은 테마가 이미 존재합니다.");
        }
    }

    public List<PopularThemeQueryResult> findPopularThemes(LocalDate today) {
        return themeRepository.findTop10PopularThemesBetween(today.minusWeeks(1), today.minusDays(1))
                .stream()
                .map(PopularThemeQueryResult::from)
                .toList();
    }
}
