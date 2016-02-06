package com.smacna.controller;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.smacna.constants.Constants;
import com.smacna.exception.SmacnaException;
import com.smacna.form.ContactDTO;
import com.smacna.form.StaticPressureClassDTO;
import com.smacna.form.TransverseConnDTO;
import com.smacna.model.InputScreen;
import com.smacna.model.JsonResponse;
import com.smacna.service.TransverseConnectionService;
import com.smacna.util.DuctSides;
import com.smacna.validator.UserValidator;

/**
 * Description
 *
 * @author sumit.v
 * @version 1.0 This is the controller class used to map the request mappings.
 */
@Controller
public class UserInputController {

	@Autowired
	private TransverseConnectionService traConnSer;

    @Autowired
    private MessageSource messageSource;

    private String errorMsg= "";

	private static final Log logg = LogFactory
			.getLog(UserInputController.class);

	/**
	 *
	 * @param contact
	 * @return
	 *
	 *         This mapping is matched to the user input screen or homepage
	 *
	 */
	@RequestMapping("/index")
	public ModelAndView userForm(Map<String, Object> map, HttpSession session) {
		logg.info("[userForm]: index controller");
		ContactDTO contact = (ContactDTO) session.getAttribute("contact");
		if(contact==null){
			contact = new ContactDTO();
			session.removeAttribute("contact");
		}

		session.removeAttribute("inputScreen");
		session.removeAttribute("outPutScreens");
		session.removeAttribute("traConnList");
		session.removeAttribute("traConnList2");
		session.removeAttribute("staPcList");
		map.put("contact", contact);
		ModelAndView view = new ModelAndView("index", map);
		return view;
	}

	/**
	 *
	 * @param map
	 * @param session
	 * @param edit
	 * @return
	 *
	 *         This mapping is called when the user click in edit button on the
	 *         result page
	 */
	@RequestMapping("/editIndex")
	public ModelAndView editUserForm(Map<String, Object> map,
			HttpSession session, @RequestParam(required = false,value="editDetails") String edit) {
		logg.info("[editUserForm]: editIndex controller");
		ContactDTO contact = (ContactDTO) session.getAttribute("contact_edit");
		if (contact == null) {
			contact = new ContactDTO();
		}
		map.put("contact", contact);
		InputScreen inputScreen = (InputScreen) session
				.getAttribute("inputScreen_edit");
		if(inputScreen==null){
			ModelAndView view = new ModelAndView("index", map);
			return view;
		}
		int pc = inputScreen.getPressureClass();
		List<TransverseConnDTO> traConnList2 = new ArrayList<TransverseConnDTO>();
		int traConS1 = inputScreen.getTransConnS1();
		if (traConS1 == Constants.TRANSVERSE_CONNECTION_T25) {
			try {
				traConnList2 = traConnSer.getSelectTcList(6, true);
			} catch (SmacnaException e) {
				e.printStackTrace();
			}
		}
		// show the Transverse connection T2 option in second drop down after
		// selecting the vlaue form first one.

		if (traConS1 == Constants.TRANSVERSE_CONNECTION_T1 || traConS1 == Constants.TRANSVERSE_CONNECTION_T5 || traConS1 == Constants.TRANSVERSE_CONNECTION_T10 || traConS1 == Constants.TRANSVERSE_CONNECTION_T11
				|| traConS1 == Constants.TRANSVERSE_CONNECTION_T12) {
			if (pc > 3) {
				try {
					traConnList2 = traConnSer.getSelectTcList(2, false);
				} catch (SmacnaException e) {
					logg.error("[editUserForm] : Exception occured"
							+ e.getMessage());
					//e.printStackTrace();

					ModelAndView view = new ModelAndView("redirect:/next.html");
	                errorMsg = messageSource.getMessage("error.internalError", null, Locale.ENGLISH);
	                inputScreen.setDesignAvailable(errorMsg);
	                session.setAttribute("error",errorMsg);
	                map.put("inputScreen", inputScreen);
	                view.addObject("inputScreen", inputScreen);
	                return view;

				}
				traConnList2.remove(4);
			} else {
				try {
					traConnList2 = traConnSer.getSelectTcList(6, false);
				} catch (SmacnaException e) {
                    logg.error("[editUserForm] : Exception occured"
                            + e.getMessage());
                    //e.printStackTrace();

                    ModelAndView view = new ModelAndView("redirect:/next.html");
                    errorMsg = messageSource.getMessage("error.internalError", null, Locale.ENGLISH);
                    inputScreen.setDesignAvailable(errorMsg);
                    session.setAttribute("error",errorMsg);
                    map.put("inputScreen", inputScreen);
                    view.addObject("inputScreen", inputScreen);
                    return view;

                }
			}
		}
		session.setAttribute("traConnList2", traConnList2);
		session.setAttribute("traConnList2_edit", traConnList2);
		ModelAndView view = new ModelAndView("index", map);
		view.addObject("edit", "editmode");
		return view;
	}

	/**
	 *
	 * @param map
	 * @param request
	 * @param contact
	 * @return This method is used when the user click the next submit button.
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping("/next")
	public String next(Map<String, Object> map, HttpServletRequest request,
			@ModelAttribute("contact") ContactDTO contact, HttpSession session,
			BindingResult result) {
		logg.info("[next]: next controller");
		if (contact.getUserName() == null) {
			contact = (ContactDTO) session.getAttribute("contact");
			if (contact == null) {
				return "index";
			}
		}
		UserValidator userValidator = new UserValidator();
		userValidator.validate(contact, result);
		if (result.hasErrors()) {
			return "index";
		}
		List<TransverseConnDTO> traConnList = (List<TransverseConnDTO>) session
				.getAttribute("traConnList");
		List<TransverseConnDTO> traConnList2 = (List<TransverseConnDTO>) session
				.getAttribute("traConnList2");

		List<StaticPressureClassDTO> staPcList = null;
		if (traConnList == null) {
			try {
				traConnList = traConnSer.getTcList();
				traConnList2 = traConnList;
				staPcList = traConnSer.getStaticPCList();
				session.setAttribute("traConnList", traConnList);
				session.setAttribute("traConnList_edit", traConnList);
				session.setAttribute("traConnList2", traConnList2);
				session.setAttribute("traConnList2_edit", traConnList2);
				session.setAttribute("staPcList", staPcList);
				session.setAttribute("staPcList_edit", staPcList);
			} catch (SmacnaException e) {

                logg.error("[next] : Exception occured"
                        + e.getMessage());

                errorMsg = messageSource.getMessage("error.internalError", null, Locale.ENGLISH);
                InputScreen inputScreen = new InputScreen();
                inputScreen.setDesignAvailable(errorMsg);
                session.setAttribute("error",errorMsg);
                map.put("inputScreen", inputScreen);
                return "next";
			}
		}
		staPcList = (List<StaticPressureClassDTO>) session
				.getAttribute("staPcList");
		if (contact.getUserName() == null) {
			contact = (ContactDTO) session.getAttribute("contact");
			if (contact == null) {
				return "index";
			}
		}
		session.setAttribute("contact", contact);
		session.setAttribute("contact_edit", contact);
		map.put("traConnList", traConnList);
		map.put("traConnList2", traConnList2);
		map.put("staPcList", staPcList);
		InputScreen inputScreen = (InputScreen) session
				.getAttribute("inputScreen");
		if (inputScreen == null) {
			inputScreen = new InputScreen();
		}
		map.put("inputScreen", inputScreen);
		map.put("contact1", contact);

		return "indexnext";
	}

	/**
	 *
	 * @param map
	 * @param request
	 * @param contact
	 * @param session
	 * @param result
	 * @return This will return the view in which the user enter the dimensions
	 *         after clicking the next button on homepage
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/editnext")
	public String editnext(Map<String, Object> map, HttpServletRequest request,
			@ModelAttribute("contact") ContactDTO contact, HttpSession session,
			BindingResult result) {
		logg.info("[next]: next controller");
		if (contact.getUserName() == null) {
			contact = (ContactDTO) session.getAttribute("contact_edit");
			if (contact == null) {
				return "index";
			}
		}
		UserValidator userValidator = new UserValidator();
		userValidator.validate(contact, result);
		if (result.hasErrors()) {
			return "index";
		}
		List<TransverseConnDTO> traConnList = (List<TransverseConnDTO>) session
				.getAttribute("traConnList_edit");
		List<TransverseConnDTO> traConnList2 = (List<TransverseConnDTO>) session
				.getAttribute("traConnList2_edit");

		List<StaticPressureClassDTO> staPcList = null;
		if (traConnList == null) {
			try {
				traConnList = traConnSer.getTcList();
				traConnList2 = traConnList;
				staPcList = traConnSer.getStaticPCList();
				session.setAttribute("traConnList", traConnList);
				session.setAttribute("traConnList_edit", traConnList);
				session.setAttribute("traConnList2", traConnList2);
				session.setAttribute("traConnList2_edit", traConnList2);
				session.setAttribute("staPcList", staPcList);
				session.setAttribute("staPcList_edit", staPcList);
			} catch (SmacnaException e) {

                logg.error("[editnext] : Exception occured"
                        + e.getMessage());

                errorMsg = messageSource.getMessage("error.internalError", null, Locale.ENGLISH);
                InputScreen inputScreen = new InputScreen();
                inputScreen.setDesignAvailable(errorMsg);
                session.setAttribute("error",errorMsg);
                map.put("inputScreen", inputScreen);
                return "next";
			}
		}
		staPcList = (List<StaticPressureClassDTO>) session
				.getAttribute("staPcList_edit");
		session.setAttribute("staPcList", staPcList);
		if (contact.getUserName() == null) {
			contact = (ContactDTO) session.getAttribute("contact");
			if (contact == null) {
				return "index";
			}
		}
		session.setAttribute("contact", contact);
		session.setAttribute("contact_edit", contact);
		map.put("traConnList", traConnList);
		map.put("traConnList2", traConnList2);
		map.put("staPcList", staPcList);
		InputScreen inputScreen = (InputScreen) session
				.getAttribute("inputScreen_edit");
		if (inputScreen == null) {
			inputScreen = new InputScreen();
		}
		map.put("inputScreen", inputScreen);
		map.put("contact1", contact);
		map.put("edit", "editmode");
		return "indexnext";

	}

	/**
	 *
	 * @param pcClass
	 * @param width
	 * @param height
	 * @param session
	 * @return This method will return the list of transverse connection for S1
	 *         and S2 on the change of PC Class
	 */
	@RequestMapping(value = "/onChangePc", method = RequestMethod.POST)
	public @ResponseBody
	JsonResponse onChangePc(@RequestParam String pcClass,
			@RequestParam String width, @RequestParam String height,
			HttpSession session) {
		logg.info("[onChangePc]: onChangePc controller");
		JsonResponse res = new JsonResponse();
		logg.info("[onChangePc]:pcClass= " + pcClass);
		List<TransverseConnDTO> traConnList = new ArrayList<TransverseConnDTO>();
		double pc = 0.0;
		int w = Integer.parseInt(width);
		int h = Integer.parseInt(height);
		logg.info("[onChangePc]:width=" + w);
		logg.info("[onChangePc]:height=" + h);
		int[] sides = DuctSides.getSides(h, w);
		int s1 = sides[0];
		int s2 = sides[1];
		logg.info("[onChangePc]:s1=" + s1);
		logg.info("[onChangePc]:s2=" + s2);
		if (pcClass.equals("1/2")) {
			pc = .5;
		} else {
			pc = Double.parseDouble(pcClass);
		}
		if (pc > 4) {
			try {
				traConnList = traConnSer.getSelectTcList(6, true);
			} catch (SmacnaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			res.setStatus("SUCCESS");
			session.setAttribute("traConnList", traConnList);
			session.setAttribute("traConnList_edit", traConnList);
			res.setResult(traConnList);
			return res;
		}
		try {
			if ((pc <= 2) || (pc <= 3 && s1 <= 36 && s2 <= 36)
					|| (pc <= 4 && s1 <= 30 && s2 <= 30)) {

				if (pc > 2) {
					traConnList = traConnSer.getSelectTcList(2, false);
				} else {
					traConnList = traConnSer.getTcList();
					res.setResult(traConnList);
				}
				res.setStatus("SUCCESS");
				res.setResult(traConnList);
				session.setAttribute("traConnList", traConnList);
				session.setAttribute("traConnList_edit", traConnList);
				return res;
			} else {
				traConnList = traConnSer.getSelectTcList(6, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		res.setStatus("SUCCESS");

		res.setResult(traConnList);
		session.setAttribute("traConnList_edit", traConnList);
		session.setAttribute("traConnList_edit", traConnList);
		return res;
	}

	/**
	 *
	 * @param pc
	 * @param traConS1
	 * @param session
	 * @return This method will return the list of Trasverse Connection for S2
	 *         on basis of change in S1
	 */
	@RequestMapping(value = "/onChangeTc1", method = RequestMethod.POST)
	@ResponseBody
	JsonResponse onChangeTc2(@RequestParam int pc, @RequestParam int traConS1,
			HttpSession session) {
		logg.info("[onChangeTc2]: onChangeTc1 controller");
		JsonResponse res = new JsonResponse();
		List<TransverseConnDTO> traConnList2 = new ArrayList<TransverseConnDTO>();

		try {
			if (traConS1 == Constants.TRANSVERSE_CONNECTION_T25) {
				traConnList2 = traConnSer.getSelectTcList(6, true);
			}

			else if (traConS1 == Constants.TRANSVERSE_CONNECTION_T10 || traConS1 == Constants.TRANSVERSE_CONNECTION_T11 || traConS1 == Constants.TRANSVERSE_CONNECTION_T12) {
				if (pc > 3) {
					traConnList2 = traConnSer.getSelectTcList(2, false);
					traConnList2.remove(4);
				} else {
					traConnList2 = traConnSer.getSelectTcList(6, false);
				}
			}
			else if (traConS1 == Constants.TRANSVERSE_CONNECTION_T1 || traConS1 == Constants.TRANSVERSE_CONNECTION_T5) {
				if (pc > 3) {
					traConnList2 = traConnSer.getSelectTcList(2, false);
					traConnList2.remove(4);
				} else {
					traConnList2 = traConnSer.getSelectTcList(6, false);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		session.setAttribute("traConnList2", traConnList2);
		session.setAttribute("traConnList2_edit", traConnList2);
		res.setStatus("SUCCESS");
		res.setResult(traConnList2);
		return res;
	}

}
