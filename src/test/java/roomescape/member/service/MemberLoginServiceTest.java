package roomescape.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.exception.BadRequestException;
import roomescape.member.dao.MemberJdbcDao;
import roomescape.member.dto.MemberLoginRequest;
import roomescape.member.dto.MemberRegistrationInfo;

@ExtendWith(MockitoExtension.class)
class MemberLoginServiceTest {

    public static final String USER_EXAMPLE_COM = "user@example.com";
    public static final String USER_EXAMPLE_PASSWORD = "password";
    @Mock
    private MemberJdbcDao memberJdbcDao;

    @InjectMocks
    private MemberLoginService memberLoginService;

    @Test
    @DisplayName("등록된 이메일로 회원 정보를 정상적으로 조회한다")
    void findRegistrationInfo_ShouldReturnInfo_WhenEmailIsRegistered() {
        // Given
        MemberLoginRequest request = new MemberLoginRequest(USER_EXAMPLE_COM, USER_EXAMPLE_PASSWORD);
        MemberRegistrationInfo expectedInfo = new MemberRegistrationInfo("Name", USER_EXAMPLE_COM, "password");

        // When
        when(memberJdbcDao.findRegistrationInfoByEmail(USER_EXAMPLE_COM)).thenReturn(expectedInfo);

        // Then
        MemberRegistrationInfo actualInfo = memberLoginService.findRegistrationInfo(request);
        assertEquals(expectedInfo, actualInfo);
    }

    @Test
    @DisplayName("등록되지 않은 이메일로 조회 시 BadRequestException을 발생시킨다")
    void findRegistrationInfo_ShouldThrowException_WhenEmailIsNotRegistered() {
        // Given
        String email = "nonexistent@example.com";
        MemberLoginRequest request = new MemberLoginRequest(email, USER_EXAMPLE_PASSWORD);

        // When
        when(memberJdbcDao.findRegistrationInfoByEmail(email)).thenReturn(null);

        // Then
        assertThrows(BadRequestException.class, () -> {
            memberLoginService.findRegistrationInfo(request);
        });
    }

}
