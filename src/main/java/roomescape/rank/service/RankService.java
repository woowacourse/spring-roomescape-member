package roomescape.rank.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.rank.repository.RankDao;
import roomescape.rank.response.RankTheme;

@Service
public class RankService {
    private final RankDao rankDao;

    public RankService(RankDao rankDao) {
        this.rankDao = rankDao;
    }

    public List<RankTheme> getRank() {
        return rankDao.getRank();
    }
}
