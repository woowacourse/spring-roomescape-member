package roomescape.reservation.dto;

public record MemberReservationRequest(String date, long themeId, long timeId, String name) {
    @Override
    public String toString() {
        return "MemberReservationRequest{" +
                "date='" + date + '\'' +
                ", themeId=" + themeId +
                ", timeId=" + timeId +
                ", name='" + name + '\'' +
                '}';
    }
}
