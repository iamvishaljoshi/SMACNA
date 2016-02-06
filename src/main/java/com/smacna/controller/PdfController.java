/**
 *
 */
package com.smacna.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.smacna.form.ContactDTO;
import com.smacna.model.InputScreen;
import com.smacna.model.JsonResponse;
import com.smacna.model.MailPopUp;
import com.smacna.model.OutPutScreen;
import com.smacna.util.FabricationReportGenerator;
import com.smacna.util.SendMailUtil;
import com.smacna.util.StringsUtil;
import com.smacna.validator.MailPopUpValidator;

/**
 * @author sumit.v
 * This class is used to show the fabrication report as pdf on screen
 * and to mail it.
 */
@Controller
public class PdfController {

    private static final Log logg = LogFactory
            .getLog(UserInputController.class);

    @Autowired
	private static MessageSource messageSource;

    /**
     * @param session
     * @param opt
     * @param request
     * @return view that shows the generated pdf
     * This mapping  shows the fabrication report as pdf on screen
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/generatePdf")
    public @ResponseBody
    JsonResponse generatePdf(HttpSession session,@RequestParam("opt") String opt,HttpServletRequest request)
    {
        logg.info("[generatePdf]: pdf controller");
        JsonResponse res = new JsonResponse();
        ContactDTO contact = (ContactDTO)session.getAttribute("contact_edit");
        String date = String.valueOf(new java.util.Date().getTime());
        int selectedOpt=Integer.parseInt(opt);
        InputScreen inputScreen = (InputScreen)session.getAttribute("inputScreen_edit");
        List<OutPutScreen> outPutScreens = new ArrayList<OutPutScreen>();
        outPutScreens = (List<OutPutScreen>)session.getAttribute("outPutScreens_edit");
        WebApplicationContext webAppContext = RequestContextUtils.getWebApplicationContext(request);
        MessageSource messageSource = (MessageSource)webAppContext.getBean("emailMessageSource");
        //String path = System.getenv("PDF_PATH");
        String path = messageSource.getMessage("pdf.path", null, Locale.ENGLISH);
		logg.debug("PDF_PATH = " + path);
        OutPutScreen outPutScreen = outPutScreens.get(selectedOpt-1) ;
        FabricationReportGenerator.generatePdf(path, inputScreen, outPutScreen, contact, date, request);
        String appUrl = messageSource.getMessage("application.url", null, Locale.ENGLISH);
        String repPath = messageSource.getMessage("fabricationReport.url", null, Locale.ENGLISH);
        String pdfURL = appUrl + repPath + date+".pdf";
        res.setStatus("SUCCESS");
        res.setResult(pdfURL);
        return res;
    }

    /**
     * @param mailPopUp
     * @param hiddenOpt
     * @param map
     * @param session
     * @param result
     * @return view that will show the mail screen
     * This mapping  shows the mail screen to the user
     */
    @RequestMapping("/mailPopUp")
    public ModelAndView mailPopUp(@ModelAttribute MailPopUp mailPopUp, @RequestParam("hiddenOpt") String hiddenOpt,Map<String, Object> map,HttpSession session, BindingResult result)
    {
        logg.info("[mailPopUp]: Mail popup controller");
        ContactDTO contact = (ContactDTO)session.getAttribute("contact_edit");
        ModelAndView view;
        if(contact == null)
        {   contact = new ContactDTO();
            view = new ModelAndView("index","contact",contact);
            return view;
        }
        if(!contact.getEmail().contains("Email (Optional)")){
        	mailPopUp.setTo(contact.getEmail());
        }
        else{
        mailPopUp.setTo("");
        }
        map.put("hiddenOpt", hiddenOpt);
        view = new ModelAndView("mailPopup",map);
        return view;
    }

    
    /**
     * @param mailPopUp
     * @param hiddenOpt
     * @param map
     * @param session
     * @param result
     * @return view that will show the mail screen
     * This mapping  shows the mail screen to the user
     */
    @RequestMapping("/showMoreOptions")
    public ModelAndView showMoreOptions(

            @ModelAttribute("inputScreen") InputScreen inputScreen,
            BindingResult result,
            Map<String, Object> map,
            HttpSession session)
    {
        logg.info("[showMoreOptions]: showMoreOptions popup controller");
        ModelAndView view;
        System.out.println("showmoreoptions---DDD");
        view = new ModelAndView("showMoreOptions");
        return view;
    }
    
    /**
     * @param mailPopUp
     * @param session
     * @param hiddenOpt
     * @param result
     * @param request
     * @return view of the mail screen
     * This mapping mails the fabrication report
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/mailFabricationReport")
    public ModelAndView  mailFabricationReport(@ModelAttribute MailPopUp mailPopUp, HttpSession session,@RequestParam("hiddenOpt") String hiddenOpt,BindingResult result,HttpServletRequest request)
    {
        logg.info("[mailFabricationReport]: mail fabrication report controller");
        MailPopUpValidator mailPopUpValidator = new MailPopUpValidator();
        mailPopUpValidator.validate(mailPopUp, result);
        ModelAndView view;
        if(result.hasErrors())
        {
            view = new ModelAndView("mailPopup");
            view.addObject("hiddenOpt", hiddenOpt);
            return view;
        }

        String to = mailPopUp.getTo();
        String cc = mailPopUp.getCc();
        String bcc = mailPopUp.getBcc();
        String toArray[]= StringsUtil.stripRecipient(to);
        String ccArray[];
        String bccArray[];
        if(cc.equals("Cc")||cc.equals(""))
        {
            ccArray = null;
        }
        else
        {
            ccArray = StringsUtil.stripRecipient(cc);
        }
        if(bcc.equals("Bcc")||cc.equals(""))
        {
            bccArray = null;
        }
        else
        {
        bccArray = StringsUtil.stripRecipient(bcc);
        }
        ContactDTO contact = (ContactDTO)session.getAttribute("contact_edit");
        String date = String.valueOf(new java.util.Date().getTime());
        int selectedOpt=Integer.parseInt(hiddenOpt);
        InputScreen inputScreen = (InputScreen)session.getAttribute("inputScreen_edit");
        List<OutPutScreen> outPutScreens = new ArrayList<OutPutScreen>();
        outPutScreens = (List<OutPutScreen>)session.getAttribute("outPutScreens_edit");
        WebApplicationContext webAppContext = RequestContextUtils.getWebApplicationContext(request);
        MessageSource messageSource = (MessageSource)webAppContext.getBean("emailMessageSource");
//        String path = System.getenv("PDF_PATH");
        String path = messageSource.getMessage("pdf.path", null, Locale.ENGLISH);
        OutPutScreen outPutScreen = outPutScreens.get(selectedOpt-1) ;
        FabricationReportGenerator.generatePdf(path, inputScreen, outPutScreen, contact, date,request);
        String attachment = path+File.separator+"Fabrication_Report"+date+".pdf";
        String host = messageSource.getMessage("email.host", null, Locale.ENGLISH);
        String username = messageSource.getMessage("email.username", null, Locale.ENGLISH);
        String password= messageSource.getMessage("email.password", null, Locale.ENGLISH);
        String subject = messageSource.getMessage("email.fabricationReport.subject", null, Locale.ENGLISH);
        String text = messageSource.getMessage("email.fabricationReport.text", null, Locale.ENGLISH);
        try
        {
        SendMailUtil.send(contact.getEmail(), toArray , host, username, password, subject, text,attachment, ccArray, bccArray);
        }
        catch(Exception e)
        {
           /* result.rejectValue("to", "error.reportNotSend" , "Report not send. Please enter a vaild email Id");
            view = new ModelAndView("mailPopup");
            view.addObject("hiddenOpt", hiddenOpt);
            return view;*/
        	throw new IllegalArgumentException(
                    e.getMessage());
        }
        view = new ModelAndView("redirect:/submit");
        return view;
    }
}
