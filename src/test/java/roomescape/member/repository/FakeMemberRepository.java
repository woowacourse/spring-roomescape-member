package roomescape.member.repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.member.domain.Account;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberEmail;
import roomescape.member.domain.MemberId;
import roomescape.member.repository.MemberRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeId;
import roomescape.theme.repository.ThemeRepository;

public class FakeMemberRepository implements MemberRepository {

    private final List<Member> members = new CopyOnWriteArrayList<>();
    private final AtomicLong index = new AtomicLong(1L);

    @Override
    public boolean existsByEmail(MemberEmail email) {
        return false;
    }

    @Override
    public Member save(Account account) {
        Member saved = Member.withId(
                MemberId.from(index.getAndIncrement()),
                account.getMember().getName(),
                account.getMember().getEmail(),
                account.getMember().getRole());

        members.add(saved);

        return saved;
    }

    @Override
    public Optional<Member> findById(MemberId id) {
        return members.stream()
                .filter(member -> Objects.equals(member.getId(), id))
                .findFirst();
    }

    @Override
    public Optional<Account> findAccountByEmail(MemberEmail email) {
        return Optional.empty();
    }

    @Override
    public List<Member> findAll() {
        return new CopyOnWriteArrayList<>(members);
    }
}
