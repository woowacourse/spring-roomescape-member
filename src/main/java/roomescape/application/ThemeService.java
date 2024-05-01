package roomescape.application;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.application.dto.ThemeCreationRequest;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.repository.ThemeRepository;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    public Theme save(ThemeCreationRequest request) {
        validateDuplicateName(request.name());
        Theme theme = new Theme(request.name(), request.description(), request.thumbnail());
        return themeRepository.save(theme);
    }

    private void validateDuplicateName(String name) {
        if (themeRepository.existsByName(name)) {
            throw new IllegalArgumentException("테마 이름이 존재합니다.");
        }
    }

    public void delete(long id) {
        validateIdIsExist(id);
        themeRepository.deleteById(id);
    }

    private void validateIdIsExist(long id) {
        themeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("삭제하고자 하는 id가 존재하지 않습니다."));
    }
}
