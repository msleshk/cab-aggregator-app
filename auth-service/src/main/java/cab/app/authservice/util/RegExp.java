package cab.app.authservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegExp {

    public static final String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static final String PHONE_NUMBER_PATTERN = "\\+375\\((29|33|44|25)\\)\\d{3}-\\d{2}-\\d{2}";
    public static final String GENDER_PATTERN = "^(MALE|FEMALE)$";
    public static final String ROLE_PATTERN = "^(DRIVER|PASSENGER)$";
}
