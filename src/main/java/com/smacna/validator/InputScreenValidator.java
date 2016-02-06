/**
 *
 */
package com.smacna.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.smacna.model.InputScreen;

/**
 * @author sumit.v
 * This class perform the validations for the input screen.
 */
public class InputScreenValidator extends LocalValidatorFactoryBean implements
        Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return InputScreen.class.isAssignableFrom(clazz);
    }

    /**
     * @param target  It is object on which the validations are to be applied
     * @param errors  It contains the errors during validation
     */
    @Override
    public void validate(Object target, Errors errors) {
        InputScreen inputScreen = (InputScreen) target;
        try {
            if (inputScreen.getHeight() == null) {
                errors.rejectValue("height",
                                   "height.required",
                                   "Please enter the Height");
            } else if (Integer.parseInt(inputScreen.getHeight()) < 0 ||
                    Integer.parseInt(inputScreen.getHeight()) > 120) {
                errors.rejectValue("height",
                                   "height.valid",
                                   "Please enter height in numeric only between 0 and 121");
            }

        } catch (NumberFormatException e) {
            errors.rejectValue("height",
                               "height.valid",
                               "Please enter height in numeric only between 0 and 121");
        }
        try {
            if (inputScreen.getWidth() == null) {
                errors.rejectValue("width",
                                   "width.required",
                                   "Please enter the Width");
            } else if (Integer.parseInt(inputScreen.getWidth()) < 0 ||
                    Integer.parseInt(inputScreen.getWidth()) > 120) {
                errors.rejectValue("width",
                                   "width.valid",
                                   "Please enter width in numeric only between 0 and 121");
            }
        } catch (Exception e) {

            errors.rejectValue("width", "width.valid", "Please enter width in numeric only between 0 and 121");
        }
        if (inputScreen.getPressureClass() == 0) {
            errors.rejectValue("pressureClass",
                               "pressureClass.required",
                               "Please Select the Pressure Class");
        }
        if (inputScreen.getJointSpacing() == 0) {
            errors.rejectValue("jointSpacing",
                               "jointSpacing.required",
                               "Please Select the Joint Spacing");
        }
        if (inputScreen.getTransConnS1() == 0) {
            errors.rejectValue("transConnS1",
                               "transConnS1.required",
                               "Please Select the TC for S1");
        }
        if (inputScreen.getTransConnS2() == 0) {
            errors.rejectValue("transConnS2",
                               "transConnS2.required",
                               "Please Select the TC for S2");
        }
    }
}
