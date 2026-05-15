package roomescape.theme.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.DuplicateException;
import roomescape.exception.InvalidInputException;
import roomescape.exception.EntityInUseException;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
public class AdminThemeService {

    private final ThemeRepository themeRepository;

    public AdminThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Theme save(String name, String description, String thumbnail) {
        try {
            return themeRepository.save(name, description, thumbnail);
        } catch (DuplicateKeyException e) {
            throw new DuplicateException("같은 이름의 테마가 존재합니다.");
        } catch (DataIntegrityViolationException e) {
            throw new InvalidInputException("요청이 데이터 무결성 조건을 위반했습니다.");
        }

    }

    @Transactional
    public void delete(long id) {
        try {
            themeRepository.delete(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException("예약이 있어 삭제할 수 없습니다.");
        }
    }
}
