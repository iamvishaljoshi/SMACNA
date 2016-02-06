package com.smacna.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.smacna.model.MailPopUp;

/**
 * @author sumit.v
 * This class perform the validations for the mail to be send.
 */
@Component
public class MailPopUpValidator extends LocalValidatorFactoryBean implements
        Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return MailPopUp.class.isAssignableFrom(clazz);
    }

    /**
     * @param target  It is object on which the validations are to be applied
     * @param errors  It contains the errors during validation
     */
    @Override
    public void validate(Object target, Errors errors) {
        MailPopUp mailPopUp = (MailPopUp) target;
        Pattern emailPttern =
                Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        Matcher toEmailMatcher = emailPttern.matcher(mailPopUp.getTo());
        if (mailPopUp.getTo().equals("To")) {
            errors.rejectValue("to", "to.required", "To is required");
        }

    }
}