package roomescape.fake;

import roomescape.global.exception.EntityNotFoundException;
import roomescape.member.entity.Member;
import roomescape.member.entity.Role;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.entity.Theme;
import roomescape.time.entity.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ReservationFakeRepository implements ReservationRepository {

    private final Map<Long, Reservation> reservations = new HashMap<>();
    private final Map<Long, Member> members = new HashMap<>();
    private final Map<Long, ReservationTime> reservationTimes = new HashMap<>();
    private final Map<Long, Theme> themes = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ReservationFakeRepository() {
        Member defaultMember = new Member(1L, "abcd@email.com", "12345", "회원1", Role.USER);
        members.put(1L, defaultMember);

        ReservationTime defaultTime = new ReservationTime(1L, LocalTime.MAX);
        reservationTimes.put(1L, defaultTime);

        Theme defaultTheme = new Theme(
                1L,
                "레벨2 탈출",
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        themes.put(1L, defaultTheme);

        long reservationId = idGenerator.getAndIncrement();
        Reservation defaultReservation = new Reservation(
                reservationId,
                defaultMember,
                LocalDate.MAX,
                defaultTime,
                defaultTheme);
        reservations.put(reservationId, defaultReservation);
    }

    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(reservations.values());
    }

    @Override
    public Reservation save(Reservation reservation) {
        Member member = reservation.getMember();
        if (!members.containsKey(member.getId())) {
            throw new EntityNotFoundException("회원을 찾을 수 없습니다: " + member.getId());
        }

        Member savedMember = members.get(member.getId());

        Long timeId = reservation.getTime().getId();
        if (!reservationTimes.containsKey(timeId)) {
            throw new EntityNotFoundException("예약 시간을 찾을 수 없습니다: " + timeId);
        }

        Long themeId = reservation.getTheme().getId();
        if (!themes.containsKey(themeId)) {
            throw new EntityNotFoundException("테마를 찾을 수 없습니다: " + themeId);
        }

        ReservationTime time = reservationTimes.get(timeId);
        Theme theme = themes.get(themeId);

        long newId = idGenerator.getAndIncrement();

        Reservation savedReservation = new Reservation(
                newId,
                savedMember,
                reservation.getDate(),
                time,
                theme);

        reservations.put(newId, savedReservation);

        return savedReservation;
    }

    @Override
    public void deleteById(long id) {
        if (!reservations.containsKey(id)) {
            throw new EntityNotFoundException("예약 데이터를 찾을 수 없습니다:" + id);
        }

        reservations.remove(id);
    }

    @Override
    public List<Long> findBookedTimeIdsByDateAndThemeId(LocalDate date, Long themeId) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getDate().equals(date))
                .filter(reservation -> reservation.getTheme().getId().equals(themeId))
                .map(Reservation::getTime)
                .map(ReservationTime::getId)
                .toList();
    }

    @Override
    public List<Reservation> findAllByFilter(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        return reservations.values().stream()
                .filter(res -> res.getTheme().getId().equals(themeId))
                .filter(res -> res.getMember().getId().equals(memberId))
                .filter(res -> !res.getDate().isBefore(dateFrom) && !res.getDate().isAfter(dateTo))
                .toList();
    }
}
