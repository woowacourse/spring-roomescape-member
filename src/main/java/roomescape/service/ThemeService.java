package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<Theme> findAll(){
        return themeDao.findAll();
    }

    public Theme findById(Long id){
        return themeDao.findById(id);
    }

    public Theme save(Theme theme){
        return themeDao.save(theme);
    }

    public void deleteById(Long id){
        themeDao.deleteById(id);
    }
}
