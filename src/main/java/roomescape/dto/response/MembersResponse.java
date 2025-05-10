package roomescape.dto.response;

public record MembersResponse(
        Long id,
        String name
) {
    public static MembersResponse from() {
        return new MembersResponse(null, null);
    }

        //todo members 찾기부터 다시하기.
}
