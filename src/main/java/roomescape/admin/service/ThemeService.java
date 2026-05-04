package roomescape.admin.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;

@Service
public class ThemeService {
    public List<Theme> findAll() {
        return List.of(new Theme("은하수", "은하수방", "ajs"), new Theme("지구", "지구회의실", "ajs"));
    }

    public Theme findById(Long id) {
        return new Theme(1L, "은하수", "은하수방", "ajs");
    }
}
