package roomescape;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import roomescape.domain.entity.Member;
import roomescape.domain.entity.Role;
import roomescape.domain.repository.MemberRepository;

@Component
@Profile("local")
@RequiredArgsConstructor
public class InitData {

    private final MemberRepository memberRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        initializeAdmin();
    }

    private void initializeAdmin() {
        memberRepository.save(new Member(null, "관리자", "admin@naver.com", "1234", Role.ADMIN));
    }
}
