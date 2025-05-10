package roomescape.persistence.fakerepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.business.domain.member.Member;
import roomescape.persistence.MemberRepository;
import roomescape.persistence.entity.MemberEntity;

public final class FakeMemberRepository implements MemberRepository, FakeRepository {

    private final List<MemberEntity> members = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Member save(Member member) {
        MemberEntity newMemberEntity = MemberEntity.fromDomain(member)
                        .copyWithId(idGenerator.getAndIncrement());
        members.add(newMemberEntity);
        return newMemberEntity.toDomain();
    }

    @Override
    public Optional<Member> findById(Long id) {
        return members.stream()
                .filter(member -> member.getId().equals(id))
                .findFirst()
                .map(MemberEntity::toDomain);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return members.stream()
                .filter(member -> member.getEmail().equals(email))
                .findFirst()
                .map(MemberEntity::toDomain);
    }

    @Override
    public List<Member> findAll() {
        return members.stream()
                .map(MemberEntity::toDomain)
                .toList();
    }

    @Override
    public void clear() {
        members.clear();
        idGenerator.set(1);
    }
}
