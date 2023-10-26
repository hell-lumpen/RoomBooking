package org.mai.roombooking.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Аккаунт был заблокирован")
@Getter
public class AccountIsLockedException extends RuntimeException {
    public AccountIsLockedException() {
        super("Account will be locked by admin");
    }
}
