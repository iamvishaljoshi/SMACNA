package com.smacna.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.smacna.form.ContactDTO;

public class ContactUsValidator extends LocalValidatorFactoryBean implements
Validator{

	 @Override
	    public boolean supports(Class<?> clazz) {
	        return ContactDTO.class.isAssignableFrom(clazz);
	    }
	  @Override
	    public void validate(Object target, Errors errors) {
	        ContactDTO user = (ContactDTO) target;
	        Pattern emailPttern =
	                Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
	        Matcher emailMatcher = emailPttern.matcher(user.getEmail());

	        Pattern phonePattern =
	                Pattern.compile("^[*+0-9]\\d{10,15}");
	        Matcher phoneMatcher = phonePattern.matcher(user.getContactNo());

	        Pattern userNamePattern = Pattern.compile("^[a-zA-Z][\\sa-zA-Z]*");
	        Matcher userNameMatcher = userNamePattern.matcher(user.getUserName());

	        Pattern companyPattern = Pattern.compile("^[a-zA-Z][\\sa-zA-Z]*");
	        Matcher companyMatcher = companyPattern.matcher(user.getCompany());

	        //String emailExp = "/^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/";

		// To check whether the user has entered the email address or not

	        if (user != null) {
	            if ((user.getEmail().equals("Email"))) {
	                errors.rejectValue("email", "email.required", "email is required");
	            } else {

				// To check whether the given email address is valid or not.

	                if (!emailMatcher.matches()) {
	                    errors.rejectValue("email",
	                                       "email.valid",
	                                       "Please Enter Valid Email Id");
	                }
	            }

			// To check whether the user has entered the user name or not

	            if ((user.getUserName().equals("User Name")) ||
	                    (user.getUserName() == null)) {
	                errors.rejectValue("userName",
	                                   "username.required",
	                                   "User Name is required");
	            } else {

	                if (user.getUserName().trim().equals("")) {
	                    errors.rejectValue("userName",
	                                       "username.required",
	                                       "User Name is required");
	                }

				if (!userNameMatcher.matches()) {/*
												 * errors.rejectValue("userName",
												 * "username.valid",
												 * "User's Name must be in characters"
												 * );
												 */
				}
	            }
			// To check whether the user has entered the Contact Number or not
	            if ((user.getContactNo().equals("Contact Number"))) {
	                errors.rejectValue("contactNo",
	                                   "contact.required",
	                                   "Contact number is required");
	            } else {/*

	                if (!phoneMatcher.matches()) {
	                    errors.rejectValue("contactNo",
	                                       "required",
	                                       "Please Enter Valid Contact Number");
	                }else if(user.getContactNo().length()<9)
 {
															 * errors.rejectValue
															 * ("contactNo",
															 * "contact.min",
															 * "Please Enter Valid Contact Number"
															 * );

				}
	            */}

			// To check whether the user has entered the Company name or not
	            if ((user.getCompany().equals("Company")) ||
	                    (user.getCompany() == null)) {
	                errors.rejectValue("company", "company.required", "Company is required");
	            } else {
	                if (user.getCompany().trim() == "") {
	                    errors.rejectValue("company",
	                                       "company.required",
	                                       "Company is required");
	                }
				if (!companyMatcher.matches()) {/*
												 * errors.rejectValue("company",
												 * "company.valid",
												 * "Company Name must be in characters"
												 * );
												 */
				}
	            }

			/*
			 * if(!user.isAgree()){ errors.rejectValue("agree",
			 * "agree.required",
			 * "Please accept the terms and condition of usage"); }
			 */

	        }

	    }
}
