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
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Sha256Encryptor;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ThemeResponse;
import roomescape.repository.CollectionMemberRepository;
import roomescape.repository.CollectionReservationRepository;
import roomescape.repository.CollectionReservationTimeRepository;
import roomescape.repository.CollectionThemeRepository;
import roomescape.service.ReservationService;

class ReservationControllerTest {
    private static final long TIME_ID = 1L;
    private static final LocalTime TIME = LocalTime.now();
    private static final Member DEFAULT_MEMBER = new Member(1L, "name", "email@email.com",
            new Sha256Encryptor().encrypt("1234"));
    private ReservationTime defaultTime = new ReservationTime(TIME_ID, TIME);
    private Theme defualtTheme = new Theme("name", "description", "http://thumbnail");
    private CollectionReservationRepository collectionReservationRepository;
    private ReservationController reservationController;

    @BeforeEach
    void initController() {
        CollectionReservationTimeRepository timeRepository = new CollectionReservationTimeRepository();
        CollectionThemeRepository themeRepository = new CollectionThemeRepository();
        collectionReservationRepository = new CollectionReservationRepository();
        CollectionMemberRepository collectionMemberRepository = new CollectionMemberRepository(List.of(DEFAULT_MEMBER));
        ReservationService reservationService = new ReservationService(collectionReservationRepository, timeRepository,
                themeRepository, collectionMemberRepository);
        reservationController = new ReservationController(reservationService);

        defaultTime = timeRepository.save(defaultTime);
        defualtTheme = themeRepository.save(defualtTheme);
    }

    @Test
    @DisplayName("예약 정보를 잘 저장하는지 확인한다.")
    void saveReservation() {
        //given
        LocalDate date = LocalDate.now().plusDays(1);

        //when
        ReservationResponse saveResponse = reservationController.saveReservation(DEFAULT_MEMBER.getId(),
                        new ReservationRequest(date, DEFAULT_MEMBER.getId(), TIME_ID, defualtTheme.getId()))
                .getBody();

        long id = Objects.requireNonNull(saveResponse).id();

        //then
        ReservationResponse expected = new ReservationResponse(id, DEFAULT_MEMBER.getName(), date,
                new ReservationTimeResponse(TIME_ID, TIME),
                new ThemeResponse(defualtTheme.getId(), defualtTheme.getName(), defualtTheme.getDescription(),
                        defualtTheme.getThumbnail()));

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
                new Reservation(DEFAULT_MEMBER, LocalDate.now(), defaultTime, defualtTheme));

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
