package roomescape.reservation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.infra.ThemeRepository;
import roomescape.reservation.presentation.dto.request.ThemeSaveRequest;
import roomescape.reservation.presentation.dto.response.ThemeFindResponse;
import roomescape.reservation.presentation.dto.response.ThemeSaveResponse;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ThemeService {
    private final ThemeRepository themeRepository;

    @Transactional
    public ThemeSaveResponse save(ThemeSaveRequest body) {
        Theme newTheme = themeRepository.save(body.toDomain());
        return ThemeSaveResponse.from(newTheme);
    }

    @Transactional
    public void delete(long id) {
        // TODO: 사이클2 요구사항에서 검증로직 추가 예정
        /**
         * 1. theme id 존재 유무 검증
         * 2. 예약에서 참조되어 있는 경우 검증
         * 3. 스케줄에서 참조되어 있는 경우 검증
         */
        themeRepository.deleteById(id);
    }

    public List<ThemeFindResponse> findByDate(LocalDate date) {
        List<Theme> themes = themeRepository.findByDate(date);
        return ThemeFindResponse.of(themes);
    }

    public List<ThemeFindResponse> findByDayAndLimit(int day, int limit) {
        List<Theme> themes = themeRepository.findByDayAndLimit(day, limit);
        return ThemeFindResponse.of(themes);
    }

    public List<ThemeFindResponse> findAll(){
        List<Theme> themes = themeRepository.findAll();
        return ThemeFindResponse.of(themes);
    }
}
