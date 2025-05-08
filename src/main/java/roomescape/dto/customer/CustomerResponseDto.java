package roomescape.dto.customer;

import roomescape.model.Customer;

public record CustomerResponseDto(
        String name
) {
    public static CustomerResponseDto from(Customer customer){
        return new CustomerResponseDto(customer.getName());
    }
}
