package roomescape.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import roomescape.dto.ThemeRequest;
import roomescape.repository.ThemeDao;

@Service
public class AdminThemeService {

    private final ThemeDao themeDao;

    public AdminThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public Long save(ThemeRequest request) {
        try {
            return themeDao.save(request.name(), request.description(), request.thumbnailUrl());
        } catch (DuplicateKeyException e){
            throw  new IllegalArgumentException("존재하는 테마는 추가할 수 없습니다.");
        }
    }

    public void delete(long id) {
        try{
            themeDao.delete(id);
        } catch (DataIntegrityViolationException e){
            throw new IllegalArgumentException("사용중인 테마는 삭제할 수 없습니다.");
        }
    }
}
