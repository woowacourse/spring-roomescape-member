package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequestDto;
import roomescape.dto.ThemeResponseDto;
import roomescape.repository.ThemeRepository;

@Transactional(readOnly = true)
@Service
public class ThemeService {
    private static final int RANKING_LIMIT = 10;
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional
    public ThemeResponseDto create(ThemeRequestDto requestDto) {
        Theme theme = requestDto.toEntity();
        return ThemeResponseDto.from(themeRepository.create(theme));
    }

    public List<ThemeResponseDto> findAll() {
        return themeRepository.findAll().stream()
                .map(ThemeResponseDto::from)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        themeRepository.delete(id);
    }

    public List<ThemeResponseDto> findRanking(LocalDate startDate, LocalDate endDate) {
        return themeRepository.findAllByOrderByReservationCountDesc(startDate, endDate, RANKING_LIMIT).stream()
                .map(ThemeResponseDto::from)
                .toList();
    }
}
