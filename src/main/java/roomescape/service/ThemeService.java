package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.ThemeCreateRequest;
import roomescape.dto.ThemeResponse;
import roomescape.dto.ThemeResponses;
import roomescape.exception.NotExistingEntryException;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponses findAll() {
        List<ThemeResponse> themeResponses = themeRepository.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
        return new ThemeResponses(themeResponses);
    }

    public ThemeResponse findByThemeId(Long id) {
        Theme theme = themeRepository.findByThemeId(id);
        return ThemeResponse.from(theme);
    }

    public ThemeResponses findHotThemesByDurationAndCount(LocalDate start,
                                                          LocalDate end,
                                                          Integer limit,
                                                          Integer offset
    ) {
        List<ThemeResponse> themeResponses = themeRepository.findHotThemesByDurationAndCount(start, end, limit, offset)
                .stream()
                .map(ThemeResponse::from)
                .toList();
        return new ThemeResponses(themeResponses);
    }

    public ThemeResponse create(ThemeCreateRequest themeCreateRequest) {
        Theme theme = new Theme(
                themeCreateRequest.name(),
                themeCreateRequest.description(),
                themeCreateRequest.thumbnail()
        );
        Theme savedTheme = themeRepository.save(theme);
        return ThemeResponse.from(savedTheme);
    }

    public void delete(Long id) {
        if (themeRepository.deleteById(id) == 0) {
            throw new NotExistingEntryException("삭제할 테마가 존재하지 않습니다");
        }
    }
}
