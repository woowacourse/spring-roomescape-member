package roomescape.core.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.core.domain.Theme;
import roomescape.core.dto.ThemeRequestDto;
import roomescape.core.dto.ThemeResponseDto;
import roomescape.core.repository.ReservationRepository;
import roomescape.core.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(final ThemeRepository themeRepository,
        final ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ThemeResponseDto create(final ThemeRequestDto request) {
        final Theme theme = new Theme(request.getName(), request.getDescription(),
            request.getThumbnail());
        validateDuplicatedName(theme);
        final Long id = themeRepository.save(theme);

        return new ThemeResponseDto(id, theme);
    }

    private void validateDuplicatedName(final Theme theme) {
        final Integer themeCount = themeRepository.countByName(theme.getName());
        if (themeCount > 0) {
            throw new IllegalArgumentException("해당 이름의 테마가 이미 존재합니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<ThemeResponseDto> findAll() {
        return themeRepository.findAll()
            .stream()
            .map(ThemeResponseDto::new)
            .toList();
    }

    @Transactional
    public void delete(final long id) {
        final int reservationCount = reservationRepository.countByThemeId(id);
        if (reservationCount > 0) {
            throw new IllegalArgumentException("Theme that have reservations cannot be deleted.");
        }
        themeRepository.deleteById(id);
    }

    public List<ThemeResponseDto> findPopular() {
        return themeRepository.findPopular()
            .stream()
            .map(ThemeResponseDto::new)
            .toList();
    }
}
