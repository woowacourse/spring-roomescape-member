package roomescape.theme.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.DuplicateException;
import roomescape.exception.EntityInUseException;
import roomescape.exception.ErrorCode;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
public class AdminThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public AdminThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Theme save(String name, String description, String thumbnail) {
        try {
            return themeRepository.save(name, description, thumbnail);
        } catch (DuplicateKeyException e) {
            throw new DuplicateException(ErrorCode.DUPLICATE_THEME, "같은 이름의 테마가 존재합니다.");
        }
    }

    @Transactional
    public void delete(long id) {
        if (reservationRepository.countByThemeId(id) > 0) {
            throw new EntityInUseException(ErrorCode.THEME_IN_USE, "예약이 있어 삭제할 수 없습니다.");
        }
        try {
            themeRepository.delete(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(ErrorCode.THEME_IN_USE, "예약이 있어 삭제할 수 없습니다.");
        }
    }
}
