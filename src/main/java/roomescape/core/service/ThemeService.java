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

    public ThemeService(final ThemeRepository themeRepository, final ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Theme create(final Theme theme) {
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
        final boolean exist = reservationRepository.existByThemeId(id);
        if (exist) {
            throw new IllegalArgumentException("해당 테마를 예약한 내역이 존재하여 삭제할 수 없습니다.");
        }
        themeRepository.deleteById(id);
    }

    public List<Theme> findPopular() {
        return themeRepository.findPopular();
    }

    private void validateDuplicatedName(final Theme theme) {
        final boolean exist = themeRepository.existByName(theme.getName());
        if (exist) {
            throw new IllegalArgumentException("해당 이름의 테마가 이미 존재합니다.");
        }
    }
}
