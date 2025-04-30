package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.ThemeCreateRequestDto;
import roomescape.dto.ThemeResponseDto;
import roomescape.exception.DuplicateContentException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponseDto createTheme(final ThemeCreateRequestDto requestDto) {
        Theme requestTheme = requestDto.createWithoutId();
        try {
            Theme savedTheme = themeRepository.save(requestTheme)
                    .orElseThrow(() -> new IllegalStateException("[ERROR] 알 수 없는 오류로 인해 테마를 생성 실패하였습니다."));

            return ThemeResponseDto.from(savedTheme);
        } catch (IllegalArgumentException e) {
            throw new DuplicateContentException(e.getMessage());
        }
    }

    public List<ThemeResponseDto> findAllThemes() {
        List<Theme> allTheme = themeRepository.findAll();
        return allTheme.stream()
                .map(reservationTheme -> ThemeResponseDto.from(reservationTheme))
                .toList();
    }

    public void deleteThemeById(final Long id) {
        int deletedThemeCount = themeRepository.deleteById(id);

        if (deletedThemeCount == 0) {
            throw new NotFoundException("[ERROR] 등록된 테마만 삭제할 수 있습니다. 입력된 번호는 " + id + "입니다.");
        }
    }

    public List<ThemeResponseDto> findPopularThemes() {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(7);
        List<Theme> popularThemes = themeRepository.findPopular(start, end);
        return popularThemes.stream()
                .map(ThemeResponseDto::from)
                .toList();
    }
}
