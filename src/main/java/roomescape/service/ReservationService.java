package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationThemeDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.dto.AdminReservationRequestDto;
import roomescape.dto.ReservationRequestDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ReservationThemeDao reservationThemeDao;
    private final MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ReservationThemeDao reservationThemeDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.reservationThemeDao = reservationThemeDao;
        this.memberDao = memberDao;
    }

    public List<Reservation> getAllReservations() {
        return reservationDao.findAll();
    }

    @Transactional
    public Reservation insertReservation(AdminReservationRequestDto adminReservationRequestDto) {
        ReservationTime reservationTime = reservationTimeDao.findById(adminReservationRequestDto.timeId())
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 입력입니다."));
        LocalDate date = adminReservationRequestDto.date();
        validatePast(date, LocalTime.parse(reservationTime.getStartAt()));
        validateDuplicated(date, adminReservationRequestDto.timeId(), adminReservationRequestDto.themeId());
        Member member = memberDao.findById(adminReservationRequestDto.memberId())
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 입력입니다."));
        Long id = reservationDao.insert(
                adminReservationRequestDto.date().toString(), adminReservationRequestDto.timeId(), adminReservationRequestDto.themeId(), member.getId());
        ReservationTheme reservationTheme = reservationThemeDao.findById(adminReservationRequestDto.themeId())
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 입력입니다."));
        return new Reservation(id, adminReservationRequestDto.date().toString(), reservationTime, reservationTheme, member);
    }

    private void validatePast(LocalDate localDate, LocalTime localTime) {
        LocalDateTime inputDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId kst = ZoneId.of("Asia/Seoul");
        if (LocalDateTime.now(kst).isAfter(inputDateTime)) {
            throw new IllegalArgumentException("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
        }
    }

    private void validateDuplicated(LocalDate date, Long timeId, Long themeId) {
        if (reservationDao.count(date.toString(), timeId, themeId) != 0) {
            throw new IllegalArgumentException("이미 해당 시간에 예약이 존재합니다.");
        }
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteById(id);
    }
}
