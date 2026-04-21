package com.fixit.user.application.port.in;

import com.fixit.user.domain.model.AuthResponseModel;
import com.fixit.user.domain.model.AuthenticationModel;

public interface IAuthServicePort {

    AuthResponseModel authenticate(AuthenticationModel auth);
}
