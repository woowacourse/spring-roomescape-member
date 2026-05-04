package roomescape.theme;

import org.springframework.stereotype.Service;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public long save(String name, String description, String thumbnail) {
        return themeRepository.save(name, description, thumbnail);
    }

    public void delete(long id) {
        themeRepository.delete(id);
    }
}
