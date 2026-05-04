package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> allTheme() {
        return themeRepository.findAll();
    }

    public Theme saveTime(String name, String description, String thumbnailUrl) {
        Theme reservationTime = Theme.transientOf(name, description, thumbnailUrl);
        return themeRepository.save(reservationTime);
    }

    public void removeTime(long timeId) {
        themeRepository.deleteById(timeId);
    }

    public Theme findTime(long timeId) {
        return themeRepository.findById(timeId);
    }
}
