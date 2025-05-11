package roomescape.domain.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.currentdate.CurrentDateTime;
import roomescape.domain.member.model.Member;
import roomescape.domain.reservation.dao.ReservationDao;
import roomescape.domain.reservation.dto.request.AdminReservationRequestDto;
import roomescape.domain.reservation.dto.request.ReservationRequestDto;
import roomescape.domain.reservation.dto.response.AdminReservationResponse;
import roomescape.domain.reservation.dto.response.ReservationResponseDto;
import roomescape.domain.reservation.model.Reservation;
import roomescape.domain.reservation.model.ReservationDate;
import roomescape.domain.reservationtime.dao.ReservationTimeDao;
import roomescape.domain.reservationtime.model.ReservationTime;
import roomescape.domain.theme.dao.ThemeDao;
import roomescape.domain.theme.model.Theme;
import roomescape.global.exception.InvalidReservationException;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final CurrentDateTime currentDateTime;

    public ReservationService(
        ReservationDao reservationDao,
        ReservationTimeDao reservationTimeDao,
        ThemeDao themeDao,
        CurrentDateTime currentDateTime) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.currentDateTime = currentDateTime;
    }

    public List<ReservationResponseDto> getAllReservations() {
        return reservationDao.findAll().stream()
            .map(ReservationResponseDto::from)
            .toList();
    }

    public void deleteReservation(Long id) {
        boolean isDeleted = reservationDao.delete(id);
        if (!isDeleted) {
            throw new IllegalArgumentException("존재하지 않는 예약 ID 입니다.");
        }
    }

    public void findReservationBy(Long id) {
        reservationDao.findById(id).
            orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 ID 입니다."));
    }

    public ReservationResponseDto saveReservationOfMember(
        ReservationRequestDto request, Member member) {
        Reservation reservation = saveReservation(
            member, request.date(), request.timeId(), request.themeId());
        return ReservationResponseDto.from(reservation);
    }

    public AdminReservationResponse saveReservationOfAdmin(
        AdminReservationRequestDto request, Member member) {
        Reservation reservation = saveReservation(
            member, request.date(), request.timeId(), request.themeId());
        return new AdminReservationResponse(reservation.getId());
    }

    private Reservation saveReservation(Member member, String date, long timeId, long themeId) {
        Reservation reservation = createReservation(member, date, timeId, themeId);
        validateDateTimeAndSaveReservation(reservation, themeId);
        return reservation;
    }

    private Reservation createReservation(Member member, String date, long timeId, long themeId) {
        LocalDateTime currentDateTimeInfo = currentDateTime.get();
        ReservationDate reservationDate = new ReservationDate(LocalDate.parse(date));
        reservationDate.validateDate(currentDateTimeInfo.toLocalDate());
        ReservationTime reservationTime = findReservationTimeBy(timeId);
        Theme theme = findThemeBy(themeId);
        return new Reservation(member, reservationDate, reservationTime, theme);
    }

    private Theme findThemeBy(long themeId) {
        return themeDao.findById(themeId)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 테마를 찾을 수 없습니다"));
    }

    private ReservationTime findReservationTimeBy(long timeId) {
        return reservationTimeDao.findById(timeId)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 예약시간을 찾을 수 없습니다"));
    }

    private void validateDateTimeAndSaveReservation(Reservation reservation, long timeId) {
        reservation.validateDateTime(currentDateTime.get());
        validateAlreadyExistDateTime(reservation.getReservationDate(), timeId);
        long savedId = reservationDao.save(reservation);
        reservation.setId(savedId);
    }

    private void validateAlreadyExistDateTime(ReservationDate date, long timeId) {
        if (reservationDao.existReservationOf(date, timeId)) {
            throw new InvalidReservationException("중복된 날짜와 시간을 예약할 수 없습니다.");
        }
    }

    public List<ReservationResponseDto> getAllReservationsOf(
        String dateFrom, String dateTo, Long memberId, Long themeId) {
        List<Reservation> reservations = reservationDao.findOf(dateFrom, dateTo, memberId, themeId);
        return reservations.stream()
            .map(ReservationResponseDto::from)
            .toList();
    }
}
