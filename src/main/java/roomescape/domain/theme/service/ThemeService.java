package roomescape.domain.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.global.exception.ConflictException;
import roomescape.domain.global.exception.ErrorCode;
import roomescape.domain.global.exception.NotFoundException;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.dto.request.ThemeCreateRequestDto;
import roomescape.domain.theme.dto.response.ThemeResponseDto;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.theme.validator.ThemeValidator;

@Service
public class ThemeService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ThemeService(ReservationRepository reservationRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponseDto> getThemes() {
        return convertThemesToDto(themeRepository.findAllThemes());
    }

    public List<ThemeResponseDto> getPopularThemes(LocalDate startDate, LocalDate endDate, Integer limit) {
        return convertThemesToDto(themeRepository.findPopularThemesDateBetween(startDate, endDate, limit));
    }

    private List<ThemeResponseDto> convertThemesToDto(List<Theme> themes) {
        return themes.stream()
            .map(ThemeResponseDto::from)
            .toList();
    }

    public ThemeResponseDto saveTheme(ThemeCreateRequestDto requestDto) {
        ThemeValidator.validate(requestDto.name(), requestDto.description(), requestDto.imageUrl());
        Theme theme = Theme.create(requestDto.name(), requestDto.description(), requestDto.imageUrl());

        return ThemeResponseDto.from(themeRepository.save(theme));
    }

    public void deleteThemeById(Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new ConflictException(ErrorCode.THEME_REFERENCED_BY_RESERVATION);
        }
        if (themeRepository.deleteThemeById(id) == 0) {
            throw new NotFoundException(ErrorCode.THEME_NOT_FOUND);
        }
    }
}
