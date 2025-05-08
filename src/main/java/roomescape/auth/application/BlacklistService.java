package roomescape.auth.application;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class BlacklistService {

    // TODO: 만료 기간이 지난 토큰은 삭제해야 함. 시간이 지나면 자동 삭제되는 기능을 제공해주는 redis로 교체 필요.
    private final Set<String> blacklistedTokens = new HashSet<>();

    public void addToBlacklist(final String token) {
        blacklistedTokens.add(token);
    }

    public boolean isBlacklisted(final String token) {
        return blacklistedTokens.contains(token);
    }
}
