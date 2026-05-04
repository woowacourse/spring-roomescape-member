package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.DuplicateEntityException;
import roomescape.global.auth.Accessor;
import roomescape.global.auth.ForbiddenException;
import roomescape.repository.ThemeRepository;
import roomescape.service.command.ThemeRegisterCommand;
import roomescape.service.fake.FakeThemeRepository;
import roomescape.service.result.ThemeRegisterResult;

class ThemeServiceTest {

    private ThemeRepository themeRepository;
    private ThemeService themeService;

    @BeforeEach
    void setUp() {
        this.themeRepository = new FakeThemeRepository();
        this.themeService = new ThemeService(themeRepository);
    }

    @Test
    void 관리자가_새로운_테마를_정상적으로_등록한다() {
        // given: 관리자 권한과 등록 정보가 주어짐
        Accessor admin = new Accessor("ADMIN");
        ThemeRegisterCommand command = new ThemeRegisterCommand("공포테마", "무서운 테마입니다.", "http://image.png");

        // when: 테마 등록 진행
        ThemeRegisterResult result = themeService.register(admin, command);

        // then: 등록된 정보가 입력값과 일치하며 ID가 발급됨
        assertThat(result)
                .extracting(
                        ThemeRegisterResult::id,
                        ThemeRegisterResult::name,
                        ThemeRegisterResult::description,
                        ThemeRegisterResult::thumbnailImageUrl
                )
                .containsExactly(1L, "공포테마", "무서운 테마입니다.", "http://image.png");
    }

    @Test
    void 관리자가_아닌_사용자가_테마_등록을_시도하면_예외가_발생한다() {
        // given: 일반 사용자 권한이 주어짐
        Accessor user = new Accessor("USER");
        ThemeRegisterCommand command = new ThemeRegisterCommand("테마", "설명", "url");

        // when & then: ForbiddenException 발생 확인
        assertThatThrownBy(() -> themeService.register(user, command))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("테마 추가는 관리자만 가능합니다.");
    }

    @Test
    void 이미_존재하는_이름으로_테마_등록을_시도하면_예외가_발생한다() {
        // given: '공포테마'가 이미 등록되어 있음
        Accessor admin = new Accessor("ADMIN");
        themeService.register(admin, new ThemeRegisterCommand("공포테마", "설명", "url"));

        ThemeRegisterCommand duplicateCommand = new ThemeRegisterCommand("공포테마", "다른 설명", "다른 url");

        // when & then: DuplicateEntityException 발생 확인
        assertThatThrownBy(() -> themeService.register(admin, duplicateCommand))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("이미 존재하는 테마입니다. 테마 명: 공포테마");
    }
}
