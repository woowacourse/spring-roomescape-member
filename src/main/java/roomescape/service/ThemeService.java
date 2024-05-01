package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme save(Theme theme) {
        // TODO: 포맷팅 검증
        // TODO: 이름 중복 검증
        return themeRepository.save(theme);
    }

    public int delete(Long id) {
        // TODO: 테마 사용하는 예약이 존재하면 삭제 불가
        // TODO: id 값이 없으면 예외 발생
        return themeRepository.deleteById(id);
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }
}
