package annotations.processor;

import annotations.Password;
import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {

    public static int MIN_PASSWORD_LENGTH = 8;
    public static int MAX_PASSWORD_LENGTH = 100;

    @Override
    public void initialize(Password constraintAnnotation) {}

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
            // At least 8 characters and a max length is 30 characters
            new LengthRule(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH),

            // At least one lowercase character
            new CharacterRule(EnglishCharacterData.LowerCase, 1),

            // At least one uppercase character
            new CharacterRule(EnglishCharacterData.UpperCase, 1),

            // At least one digit character
            new CharacterRule(EnglishCharacterData.Digit, 1),

            // The password cannot has a whitespaces
            new WhitespaceRule()
        ));

        RuleResult result = validator.validate(new PasswordData(password));
        // Valid the password
        if (result.isValid()) {
            return true;
        }

        // In this point, the password is invalid
        List<String> messages = validator.getMessages(result);
        String messageTemplate = messages.stream().collect(Collectors.joining(","));
        constraintValidatorContext.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}
