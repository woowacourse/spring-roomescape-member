package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequestDto;
import roomescape.exception.BadRequestException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(final ThemeRepository themeRepository, final ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Theme create(final ThemeRequestDto request) {
        final Theme theme = new Theme(request.getName(), request.getDescription(), request.getThumbnail());
        validateDuplicatedName(theme);
        final Long id = themeRepository.save(theme);
        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Transactional(readOnly = true)
    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    @Transactional
    public void delete(final long id) {
        if (reservationRepository.hasReservationWithTheme(id)) {
            throw new BadRequestException("해당 테마를 예약한 내역이 존재하여 삭제할 수 없습니다.");
        }
        themeRepository.deleteById(id);
    }

    public List<Theme> findPopularThemesByPeriod(final long periodDay) {
        return themeRepository.findPopularThemesByPeriod(periodDay);
    }

    private void validateDuplicatedName(final Theme theme) {
        final String name = theme.getName();
        if (themeRepository.hasDuplicateTheme(name)) {
            throw new BadRequestException("해당 이름의 테마가 이미 존재합니다.");
        }
    }
}
