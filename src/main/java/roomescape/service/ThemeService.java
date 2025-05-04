package roomescape.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeCreateRequest;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {
    // todo: Service naming 다시 생각해보기
    private final ThemeRepository themeRepository;

    @Autowired
    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme createTheme(ThemeCreateRequest request) {
        return themeRepository.save(request.toTheme());
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public void deleteThemeById(Long id) {
        themeRepository.deleteById(id);
    }

    public List<Theme> findLimitedThemesByPopularDesc(String orderType, Long listNum) {
        if (orderType.equals("popular_desc")) {
            return themeRepository.findTopByReservationCountDesc(listNum);
        }

        return themeRepository.findTopByReservationCountAsc(listNum);
    }
}
