package roomescape.dto.customer;

import roomescape.model.Member;

public record CustomerResponseDto(
        String name
) {
    public static CustomerResponseDto from(Member member){
        return new CustomerResponseDto(member.getName());
    }
}
