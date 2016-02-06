/**
 *
 */
package com.smacna.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.smacna.form.ContactDTO;
import com.smacna.util.SendMailUtil;
import com.smacna.util.StringsUtil;
import com.smacna.validator.ContactUsValidator;

/**
 * Description
 * @author sumit.v
 * @version 1.0 This class is used for the general functionalities such as about us informations
 *          or the contact information
 * @since 2012
 *
 */
@Controller
public class GeneralController {

    private static final Log logger = LogFactory
            .getLog(GeneralController.class);

    /**
     * @return about us view
     * This mapping is used when the user click the about us button
     */
    @RequestMapping("/aboutUs")
    public String aboutUs() {
        logger.info("[aboutUs]: aboutUs controller");
        return "aboutUs";
    }

    /**
     * @param contactUs
     * @param result
     * @param request
     * @param send
     * @return view
     * The control will shift to this mapping when the user clicks the
     * contact us  button
     */
    @RequestMapping("/contactUs")
    public ModelAndView contactUs(
                                  @ModelAttribute("contactUs") ContactDTO contactUs,
                                  BindingResult result, HttpServletRequest request,
			@RequestParam(required = false) String send, HttpSession session) {
        logger.info("[contactUs]: contactUs controller");
        ModelAndView view = new ModelAndView("contactUs");
		ContactDTO contact = (ContactDTO) session.getAttribute("contact");
		if (contact != null) {
			if(!contact.getUserName().contains("User Name (Optional)")){
			contactUs.setUserName(contact.getUserName());
			}
			if(!contact.getCompany().contains("Company (Optional)")){
			contactUs.setCompany(contact.getCompany());
			}
			if(!contact.getEmail().contains("Email (Optional)")){
			contactUs.setEmail(contact.getEmail());
			}
			if(!contact.getContactNo().contains("Contact Number (Optional)")){
			contactUs.setContactNo(contact.getContactNo());
			}
		}
        if (send != null && send.equals("1")) {
            WebApplicationContext webAppContext = RequestContextUtils
                    .getWebApplicationContext(request);
            MessageSource messageSource = (MessageSource) webAppContext
                    .getBean("messageSource");
            String messageSend = messageSource.getMessage(
                                                          "email.deliveryMessage", null, Locale.ENGLISH);
            view.addObject("message", messageSend);
        }

        /*if (send != null && send.equals("2")) {
            WebApplicationContext webAppContext = RequestContextUtils
                    .getWebApplicationContext(request);
            MessageSource messageSource = (MessageSource) webAppContext
                    .getBean("messageSource");
            String messageSend = messageSource.getMessage(
                                                          "email.sendFailMessage", null, Locale.ENGLISH);
            view.addObject("message", messageSend);
        }*/
        return view;

    }

    /**
     * @param contactUs
     * @param result
     * @param request
     * @return view
     * This functions is used to send the mail.
     */
    @RequestMapping("/sendMail")
    public ModelAndView sendEmail(
                                  @ModelAttribute("contactUs") ContactDTO contactUs,
                                  BindingResult result, HttpServletRequest request) {
        logger.info("[sendEmail]: sendEmail controller");
        ContactUsValidator contactUsValidator = new ContactUsValidator();
        contactUsValidator.validate(contactUs, result);
        ModelAndView view;
        if (result.hasErrors()) {
            view = new ModelAndView("contactUs");
            return view;
        }
        Object[] contactObject = new Object[5];
        contactObject[0] = contactUs.getUserName();
        contactObject[1] = contactUs.getCompany();
        contactObject[2] = contactUs.getEmail();
        contactObject[3] = contactUs.getContactNo();
        WebApplicationContext webAppContext = RequestContextUtils
                .getWebApplicationContext(request);
        MessageSource messageSource = (MessageSource) webAppContext
                .getBean("emailMessageSource");
        String to = messageSource.getMessage("email.to", contactObject,
                                             Locale.ENGLISH);
        String toArray[] = StringsUtil.stripRecipient(to);
        String host = messageSource.getMessage("email.host", contactObject,
                                               Locale.ENGLISH);
        String username = messageSource.getMessage("email.username",
                                                   contactObject, Locale.ENGLISH);
        String password = messageSource.getMessage("email.password",
                                                   contactObject, Locale.ENGLISH);
        String subject = messageSource.getMessage("email.contactUs.subject",
                                                  contactObject, Locale.ENGLISH);
        String text = messageSource.getMessage("email.contactUs.text",
                                               contactObject, Locale.ENGLISH);
        try {
            SendMailUtil.send(contactUs.getEmail(), toArray, host, username,
                              password, subject, text, null, null, null);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    e.getMessage());

//        	view = new ModelAndView("redirect:/contactUs?send=2");
//        	return view;
        }
        view = new ModelAndView("redirect:/contactUs?send=1");
        return view;
    }

}
