package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.CustomerDao;
import roomescape.dto.customer.LoginRequestDto;
import roomescape.model.Customer;

@Service
public class AuthenticationService {

    private final CustomerDao customerDao;

    public AuthenticationService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public void verifyLogin(LoginRequestDto loginRequestDto){
        Customer customer = loginRequestDto.convertToUser();
        boolean isExists = customerDao.existsByEmailAndPassword(customer);
    }
}
