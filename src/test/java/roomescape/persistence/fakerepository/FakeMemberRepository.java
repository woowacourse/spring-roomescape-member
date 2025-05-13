package roomescape.persistence.fakerepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.business.domain.member.Member;
import roomescape.business.domain.member.MemberCredential;
import roomescape.business.domain.member.SignUpMember;
import roomescape.persistence.MemberRepository;
import roomescape.persistence.entity.MemberEntity;

public final class FakeMemberRepository implements MemberRepository, FakeRepository {

    private final List<MemberEntity> members = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Long save(SignUpMember signUpMember) {
        MemberEntity newMemberEntity = MemberEntity.fromDomain(signUpMember)
                        .copyWithId(idGenerator.getAndIncrement());
        members.add(newMemberEntity);
        return newMemberEntity.getId();
    }

    @Override
    public Optional<Member> findById(Long id) {
        return members.stream()
                .filter(member -> member.getId().equals(id))
                .findFirst()
                .map(MemberEntity::toDomain);
    }

    @Override
    public Optional<MemberCredential> findCredentialByEmail(String email) {
        return members.stream()
                .filter(member -> member.getEmail().equals(email))
                .findFirst()
                .map(member -> new MemberCredential(member.getId(), member.getEmail(), member.getPassword()));
    }

    @Override
    public List<Member> findAll() {
        return members.stream()
                .map(MemberEntity::toDomain)
                .toList();
    }

    @Override
    public boolean existsByEmail(String email) {
        return members.stream()
                .anyMatch(member -> member.getEmail().equals(email));
    }

    @Override
    public void clear() {
        members.clear();
        idGenerator.set(1);
    }
}
