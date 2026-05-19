package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequestDto;
import roomescape.dto.ThemeResponseDto;
import roomescape.exception.CustomException;
import roomescape.exception.ErrorCode;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

@Transactional(readOnly = true)
@Service
public class ThemeService {
    private static final int RANKING_LIMIT = 10;

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ThemeService(ReservationRepository reservationRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
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

    public List<ThemeResponseDto> findRanking(LocalDate startDate, LocalDate endDate) {
        return themeRepository.findAllByOrderByReservationCountDesc(startDate, endDate, RANKING_LIMIT).stream()
                .map(ThemeResponseDto::from)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        validateUnreferencedTheme(id);

        themeRepository.delete(id);
    }

    private void validateUnreferencedTheme(Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new CustomException(ErrorCode.THEME_DELETE_REFERENTIAL_INTEGRITY);
        }
    }

}
