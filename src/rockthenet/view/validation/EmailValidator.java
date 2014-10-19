package rockthenet.view.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Samuel on 29.09.2014.
 */
public class EmailValidator {
    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public EmailValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    /**
     * Validate an email-address with regular expression
     *
     * @param email email-address to validation
     * @return true if valid email; false otherwise
     */
    public boolean validate(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}