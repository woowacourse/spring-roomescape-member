package roomescape.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.dto.ReservationCreateRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.dto.ReservationUpdateRequest;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.exception.CustomException;
import roomescape.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    ReservationQueryingDao reservationQueryingDao;

    @Mock
    ReservationUpdatingDao reservationUpdatingDao;

    @Mock
    ReservationTimeQueryingDao reservationTimeQueryingDao;

    @Mock
    ThemeQueryingDao themeQueryingDao;

    @InjectMocks
    ReservationService reservationService;


    Long reservationTimeId = 1L;
    LocalTime startAt = LocalTime.of(10, 0);
    ReservationTime reservationTime = new ReservationTime(reservationTimeId, startAt);

    Long themeId = 1L;
    String themeName = "인형의 집";
    String description = "공포 테마의 클래식, 밤마다 살아 움직이는 인형들이 가득한 저택을 탈출하세요.";
    String url = "https://example.com/1";
    Theme theme = new Theme(themeId, themeName, description, url);

    Long reservationId = 1L;
    String name = "브라운";
    LocalDate date = LocalDate.now().plusDays(3);

    Reservation reservation = new Reservation(reservationId, name, date, reservationTime, theme, LocalDateTime.now(), LocalDateTime.now());

    @Test
    @DisplayName("예약을 생성할 수 있다.")
    void 예약_생성_성공() {
        // given
        ReservationCreateRequest request = new ReservationCreateRequest(name, date, reservationTimeId, themeId);

        when(reservationTimeQueryingDao.findReservationTimeById(anyLong()))
                .thenReturn(Optional.of(reservationTime));

        when(themeQueryingDao.findThemeById(anyLong()))
                .thenReturn(Optional.of(theme));

        when(reservationQueryingDao.findReservationByThemeAndDateAndTime(eq(themeId), eq(date), eq(reservationTimeId)))
                .thenReturn(Optional.empty());

        when(reservationUpdatingDao.save(any()))
                .thenReturn(reservationId);

        when(reservationQueryingDao.findReservationById(reservationId))
                .thenReturn(Optional.of(reservation));

        // when
        ReservationResponse reservationResponse = reservationService.create(request);

        // then
        Assertions.assertEquals(request.getDate(), reservationResponse.getDate());
        Assertions.assertEquals(request.getName(), reservationResponse.getName());
    }

    @Test
    @DisplayName("예약을 생성할 때 존재하지 않는 예약 시간인 경우 에러를 발생한다.")
    void 예약_생성_에러_예약시간_없음() {
        // given
        ReservationCreateRequest request = new ReservationCreateRequest(name, date, reservationTimeId, themeId);

        when(reservationTimeQueryingDao.findReservationTimeById(anyLong()))
                .thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(CustomException.class,
                () -> reservationService.create(request));
        verify(themeQueryingDao, never()).findThemeById(themeId);
    }

    @Test
    @DisplayName("예약을 생성할 때 존재하지 않는 테마인 경우 에러를 발생한다.")
    void 예약_생성_에러_테마_없음() {
        // given
        ReservationCreateRequest request = new ReservationCreateRequest(name, date, reservationTimeId, themeId);

        when(reservationTimeQueryingDao.findReservationTimeById(anyLong()))
                .thenReturn(Optional.of(reservationTime));

        when(themeQueryingDao.findThemeById(anyLong()))
                .thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(CustomException.class,
                () -> reservationService.create(request));
        verify(reservationQueryingDao, never()).findReservationByThemeAndDateAndTime(eq(themeId), eq(date), eq(reservationTimeId));
    }

    @Test
    @DisplayName("예약을 생성할 때 현재보다 이전의 날짜를 예약하는 경우 에러를 발생한다.")
    void 예약_생성_에러_과거_날짜_예약() {
        // given
        ReservationCreateRequest request = new ReservationCreateRequest(name, LocalDate.now().minusDays(3), reservationTimeId, themeId);

        when(reservationTimeQueryingDao.findReservationTimeById(anyLong()))
                .thenReturn(Optional.of(reservationTime));

        when(themeQueryingDao.findThemeById(anyLong()))
                .thenReturn(Optional.of(theme));

        // when & then
        Assertions.assertThrows(CustomException.class,
                () -> reservationService.create(request));
        verify(reservationQueryingDao, never()).findReservationByThemeAndDateAndTime(eq(themeId), eq(date), eq(reservationTimeId));
    }

    @Test
    @DisplayName("예약을 생성할 때 이미 예약된 시간인 경우 에러를 발생한다.")
    void 예약_생성_에러_이미_예약() {
        // given
        ReservationCreateRequest request = new ReservationCreateRequest(name, date, reservationTimeId, themeId);

        when(reservationTimeQueryingDao.findReservationTimeById(anyLong()))
                .thenReturn(Optional.of(reservationTime));

        when(themeQueryingDao.findThemeById(anyLong()))
                .thenReturn(Optional.of(theme));

        when(reservationQueryingDao.findReservationByThemeAndDateAndTime(eq(themeId), eq(date), eq(reservationTimeId)))
                .thenReturn(Optional.of(reservation));

        // when & then
        Assertions.assertThrows(CustomException.class,
                () -> reservationService.create(request));
        verify(reservationUpdatingDao, never()).save(any());
    }

    @Test
    @DisplayName("예약을 생성할 때 현재보다 과거인 시간 예약 시 에러가 발생한다.")
    void 예약_생성_에러_과거_시간_예약() {
        // given
        Long reservationTimeId2 = 2L;
        LocalTime startAt2 = LocalTime.of(10, 0);
        ReservationTime reservationTime2 = new ReservationTime(reservationTimeId2, startAt2);

        Long reservationId2 = 1L;
        String name2 = "브라운";
        LocalDate date2 = LocalDate.now();
        LocalDateTime createdAt = LocalDateTime.of(date2, startAt2.plusMinutes(10));

        Reservation reservation2 = new Reservation(reservationId2, name2, date2, reservationTime2, theme, createdAt, createdAt);

        ReservationCreateRequest request = new ReservationCreateRequest(name2, date2, reservationTimeId2, themeId);

        when(reservationTimeQueryingDao.findReservationTimeById(anyLong()))
                .thenReturn(Optional.of(reservationTime2));

        when(themeQueryingDao.findThemeById(anyLong()))
                .thenReturn(Optional.of(theme));

        when(reservationQueryingDao.findReservationByThemeAndDateAndTime(eq(themeId), eq(date2), eq(reservationTimeId2)))
                .thenReturn(Optional.empty());

        when(reservationQueryingDao.findReservationById(anyLong()))
                .thenReturn(Optional.of(reservation2));

        // when & then
        Assertions.assertThrows(CustomException.class,
                () -> reservationService.create(request));
    }

    @Test
    @DisplayName("예약을 생성할 때 현재 시간 예약 시 에러가 발생한다.")
    void 예약_생성_에러_현재_시간_예약() {
        // given
        Long reservationTimeId2 = 2L;
        LocalTime startAt2 = LocalTime.of(10, 0);
        ReservationTime reservationTime2 = new ReservationTime(reservationTimeId2, startAt2);

        Long reservationId2 = 1L;
        String name2 = "브라운";
        LocalDate date2 = LocalDate.now();
        LocalDateTime createdAt = LocalDateTime.of(date2, startAt2);

        Reservation reservation2 = new Reservation(reservationId2, name2, date2, reservationTime2, theme, createdAt, createdAt);

        ReservationCreateRequest request = new ReservationCreateRequest(name2, date2, reservationTimeId2, themeId);

        when(reservationTimeQueryingDao.findReservationTimeById(anyLong()))
                .thenReturn(Optional.of(reservationTime2));

        when(themeQueryingDao.findThemeById(anyLong()))
                .thenReturn(Optional.of(theme));

        when(reservationQueryingDao.findReservationByThemeAndDateAndTime(eq(themeId), eq(date2), eq(reservationTimeId2)))
                .thenReturn(Optional.empty());

        when(reservationQueryingDao.findReservationById(anyLong()))
                .thenReturn(Optional.of(reservation2));

        // when & then
        Assertions.assertThrows(CustomException.class,
                () -> reservationService.create(request));
    }

    @Test
    @DisplayName("예약 데이터가 존재하는 경우 예약 조회할 수 있다.")
    void 예약_조회_성공() {
        // given
        when(reservationQueryingDao.findReservationById(reservationId))
                .thenReturn(Optional.of(reservation));

        // when
        ReservationResponse response = reservationService.read(reservationId);

        // then
        Assertions.assertEquals(reservationId, response.getId());
        Assertions.assertEquals(name, response.getName());
        Assertions.assertEquals(date, response.getDate());
    }

    @Test
    @DisplayName("예약 데이터가 존재하지 않는 경우 에러를 발생한다.")
    void 예약_조회_에러() {
        // given
        when(reservationQueryingDao.findReservationById(reservationId))
                .thenReturn(Optional.empty());

        // when && then
        Assertions.assertThrows(CustomException.class, () -> reservationService.read(reservationId));
    }

    @Test
    @DisplayName("예약 데이터들이 존재하는 경우 목록 조회할 수 있다.")
    void 예약_목록_조회_성공() {
        // given
        Long reservationId2 = 2L;
        String name2 = "검프";
        LocalDate date2 = LocalDate.now().plusDays(5);

        Reservation reservation2 = new Reservation(reservationId2, name2, date2, reservationTime, theme, LocalDateTime.now(), LocalDateTime.now());

        when(reservationQueryingDao.findAllReservations())
                .thenReturn(List.of(reservation, reservation2));

        // when
        List<ReservationResponse> responses = reservationService.readAll();

        // then
        Assertions.assertEquals(2, responses.size());

        Assertions.assertEquals(reservationId, responses.getFirst().getId());
        Assertions.assertEquals(name, responses.getFirst().getName());
        Assertions.assertEquals(date, responses.getFirst().getDate());

        Assertions.assertEquals(reservationId2, responses.get(1).getId());
        Assertions.assertEquals(name2, responses.get(1).getName());
        Assertions.assertEquals(date2, responses.get(1).getDate());
    }

    @Test
    @DisplayName("예약을 수정할 수 있다.")
    void 예약_수정_성공() {
        // given
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(name, date, reservationTimeId, themeId);

        when(reservationQueryingDao.existsById(reservationId))
                .thenReturn(true);

        when(reservationQueryingDao.findReservationById(reservationId))
                .thenReturn(Optional.of(reservation));

        when(reservationTimeQueryingDao.findReservationTimeById(reservationTimeId))
                .thenReturn(Optional.of(reservationTime));

        // when
        ReservationResponse reservationResponse = reservationService.update(reservationId, updateRequest);

        // then
        Assertions.assertEquals(updateRequest.getDate(), reservationResponse.getDate());
        Assertions.assertEquals(updateRequest.getName(), reservationResponse.getName());
    }

    @Test
    @DisplayName("예약을 수정할 때 현재보다 이전의 날짜를 예약하는 경우 에러를 발생한다.")
    void 예약_수정_에러_과거_날짜_예약() {
        // given
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(name, LocalDate.now().minusDays(3), reservationTimeId, themeId);

        // when & then
        Assertions.assertThrows(CustomException.class,
                () -> reservationService.update(reservationId, updateRequest));
        verify(reservationQueryingDao, never()).existsById(reservationId);
    }

    @Test
    @DisplayName("예약을 수정할 때 존재하지 않는 예약인 경우 에러를 발생한다.")
    void 예약_수정_에러_예약_없음() {
        // given
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(name, date, reservationTimeId, themeId);

        when(reservationQueryingDao.existsById(anyLong()))
                .thenReturn(false);

        // when & then
        Assertions.assertThrows(CustomException.class,
                () -> reservationService.update(reservationId, updateRequest));
        verify(reservationUpdatingDao, never()).update(reservationId, updateRequest);
    }

    @Test
    @DisplayName("예약을 수정할 때 존재하지 않는 예약 시간인 경우 에러를 발생한다.")
    void 예약_수정_에러_예약시간_없음() {
        // given
        Long reservationTimeId2 = 2L;
        LocalTime startAt2 = LocalTime.of(10, 0);
        ReservationTime reservationTime2 = new ReservationTime(reservationTimeId2, startAt2);

        Long reservationId2 = 1L;
        String name2 = "브라운";
        LocalDate date2 = LocalDate.now();
        LocalDateTime createdAt = LocalDateTime.of(date2, startAt2.plusMinutes(10));

        Reservation reservation2 = new Reservation(reservationId2, name2, date2, reservationTime2, theme, createdAt, createdAt);

        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(name2, date2, reservationTimeId2, themeId);

        when(reservationQueryingDao.existsById(anyLong()))
                .thenReturn(true);

        when(reservationQueryingDao.findReservationById(reservationId2))
                .thenReturn(Optional.of(reservation2));

        when(reservationTimeQueryingDao.findReservationTimeById(reservationTimeId2))
                .thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(CustomException.class,
                () -> reservationService.update(reservationId2, updateRequest));
    }

    @Test
    @DisplayName("예약을 수정할 때 현재보다 과거인 시간 예약 시 에러가 발생한다.")
    void 예약_수정_에러_과거_시간_예약() {
        // given
        Long reservationTimeId2 = 2L;
        LocalTime startAt2 = LocalTime.of(10, 0);
        ReservationTime reservationTime2 = new ReservationTime(reservationTimeId2, startAt2);

        Long reservationId2 = 1L;
        String name2 = "브라운";
        LocalDate date2 = LocalDate.now();
        LocalDateTime createdAt = LocalDateTime.of(date2, startAt2.plusMinutes(10));

        Reservation reservation2 = new Reservation(reservationId2, name2, date2, reservationTime2, theme, createdAt, createdAt);

        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(name2, date2, reservationTimeId2, themeId);

        when(reservationQueryingDao.existsById(anyLong()))
                .thenReturn(true);

        when(reservationQueryingDao.findReservationById(reservationId2))
                .thenReturn(Optional.of(reservation2));

        when(reservationTimeQueryingDao.findReservationTimeById(reservationTimeId2))
                .thenReturn(Optional.of(reservationTime2));

        // when & then
        Assertions.assertThrows(CustomException.class,
                () -> reservationService.update(reservationId2, updateRequest));
    }

    @Test
    @DisplayName("예약을 삭제할 수 있다.")
    void 예약_삭제_성공() {
        // given
        when(reservationQueryingDao.existsById(reservationTimeId))
                .thenReturn(true);

        // when
        reservationService.delete(reservationTimeId);

        // then
        verify(reservationUpdatingDao, times(1)).delete(reservationTimeId);
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제할 경우 에러를 발생한다.")
    void 예약_삭제_에러() {
        // given
        when(reservationQueryingDao.existsById(reservationTimeId))
                .thenReturn(false);

        // when && then
        Assertions.assertThrows(CustomException.class, () -> reservationService.delete(reservationTimeId));
    }
}
