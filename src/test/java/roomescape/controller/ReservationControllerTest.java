package roomescape.controller;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.LoginMember;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.LoginMemberResponse;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ThemeResponse;
import roomescape.repository.CollectionMemberRepository;
import roomescape.repository.CollectionReservationRepository;
import roomescape.repository.CollectionReservationTimeRepository;
import roomescape.repository.CollectionThemeRepository;
import roomescape.repository.MemberRepository;
import roomescape.service.ReservationService;

class ReservationControllerTest {
    private static final LoginMember DEFAULT_LOGINUSER = new LoginMember(1L, "아서");
    private ReservationTime defaultTime = new ReservationTime(1L, LocalTime.now());
    private Theme defualtTheme = new Theme("name", "description", "thumbnail");
    private CollectionReservationRepository collectionReservationRepository;
    private ReservationController reservationController;
    private MemberRepository memberRepository;

    @BeforeEach
    void initController() {
        CollectionReservationTimeRepository timeRepository = new CollectionReservationTimeRepository();
        CollectionThemeRepository themeRepository = new CollectionThemeRepository();
        collectionReservationRepository = new CollectionReservationRepository();
        memberRepository = new CollectionMemberRepository();
        ReservationService reservationService = new ReservationService(collectionReservationRepository, timeRepository,
                themeRepository, memberRepository);
        reservationController = new ReservationController(reservationService);

        defaultTime = timeRepository.save(defaultTime);
        defualtTheme = themeRepository.save(defualtTheme);
    }

    @Test
    @DisplayName("예약 정보를 잘 저장하는지 확인한다. - 사용자")
    void saveReservation() {
        //given
        LocalDate date = LocalDate.now().plusDays(1);

        //when
        ReservationResponse saveResponse = reservationController.saveReservation(
                        DEFAULT_LOGINUSER,
                        new ReservationRequest(date, 1L, defualtTheme.getId()))
                .getBody();

        long id = Objects.requireNonNull(saveResponse).id();

        //then
        ReservationResponse expected = new ReservationResponse(
                id,
                date,
                ReservationTimeResponse.from(defaultTime),
                ThemeResponse.from(defualtTheme),
                LoginMemberResponse.from(DEFAULT_LOGINUSER)
        );

        Assertions.assertThat(saveResponse).isEqualTo(expected);
    }

    @Test
    @DisplayName("예약 정보를 잘 불러오는지 확인한다.")
    void findAllReservations() {
        //when
        List<ReservationResponse> allReservations = reservationController.findAllReservations();

        //then
        Assertions.assertThat(allReservations).isEmpty();
    }

    @Test
    @DisplayName("예약 정보를 잘 지우는지 확인한다.")
    void delete() {
        //given
        Reservation saved = collectionReservationRepository.save(
                new Reservation(LocalDate.now(), defaultTime, defualtTheme, DEFAULT_LOGINUSER));

        //when
        reservationController.delete(saved.getId());

        //then
        List<ReservationResponse> reservationResponses = reservationController.findAllReservations();
        Assertions.assertThat(reservationResponses)
                .isEmpty();
    }

    @Test
    @DisplayName("내부에 Repository를 의존하고 있지 않은지 확인한다.")
    void checkRepositoryDependency() {
        boolean isRepositoryInjected = false;

        for (Field field : reservationController.getClass().getDeclaredFields()) {
            if (field.getType().getName().contains("Repository")) {
                isRepositoryInjected = true;
                break;
            }
        }

        Assertions.assertThat(isRepositoryInjected).isFalse();
    }
}
