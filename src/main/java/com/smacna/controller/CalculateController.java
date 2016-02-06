package com.smacna.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.smacna.constants.Constants;
import com.smacna.exception.SmacnaException;
import com.smacna.model.InputScreen;
import com.smacna.model.OutPutScreen;
import com.smacna.service.MidPanelTieRodService;
import com.smacna.service.RectDuctRenfService;
import com.smacna.service.TransverseConnectionService;
import com.smacna.transverseconnection.StandingSlipTransverseConnection;
import com.smacna.transverseconnection.TransverseConnections;
import com.smacna.util.DuctSides;
import com.smacna.util.OutPutScreenSorting;
import com.smacna.util.PrioritySorting;
import com.smacna.validator.InputScreenValidator;

/**
 *
 * @author vishal.joshi
 * @version 1.0 This class is used for calculation and provide the best possible
 *          options for constructing the duct.
 */
@Controller
public class CalculateController {

    private static final Log logg = LogFactory
            .getLog(CalculateController.class);

    @Autowired
    private RectDuctRenfService renfService;

    @Autowired
    private MidPanelTieRodService tieRodService;

    @Autowired
    private TransverseConnectionService connectionService;

    @Autowired
    private TransverseConnections tdcTdfConnection;

    @Autowired
    private TransverseConnections slipAndDriveTranConn;

    @Autowired
    private TransverseConnections standingSlipTranConn;

    @Autowired
    private MessageSource messageSource;

    private String errorMsg= "";

    /**
     *
     * @param inputScreen
     * @param result
     * @return view which holds the options to be displayed
     */
    @RequestMapping(value = "/submit")
    public ModelAndView addCalculation(
    								   @ModelAttribute("option") String option,
                                       @ModelAttribute("inputScreen") InputScreen inputScreen,
                                       BindingResult result,
                                       Map<String, Object> map,
                                       HttpSession session) {

        if (inputScreen.getHeight() == null) {
            inputScreen =
                    (InputScreen) session.getAttribute("inputScreen_edit");
        }
        InputScreenValidator inputScreenValidator = new InputScreenValidator();
        inputScreenValidator.validate(inputScreen, result);
        ModelAndView view;
        if (result.hasErrors()) {
            view = new ModelAndView("indexnext");
            return view;
        }
        view = new ModelAndView("result");
        List<OutPutScreen> outPutScreens = new ArrayList<OutPutScreen>();
        logg.info("[addCalculation] : " + inputScreen.toString());
        session.setAttribute("inputScreen", inputScreen);
        session.setAttribute("inputScreen_edit", inputScreen);

        int sides[] =
                DuctSides.getSides(Integer.parseInt(inputScreen.getHeight()),
                                   Integer.parseInt(inputScreen.getWidth()));

        logg.debug("[addCalculation] : S1 = " + sides[0] + ", S2 = " +
                sides[1] + "");

//      For T25a/b
        if (inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T25 ||
                inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T25) {
            try
            {
            outPutScreens =
                    tdcTdfConnection.getOptions(inputScreen,
                                                sides,
                                                renfService,
                                                tieRodService,
                                                connectionService);
            logg.debug("[addCalculation] : List Size"+outPutScreens.size());
            }
            catch(SmacnaException e)
            {
                view = new ModelAndView("redirect:/next.html");
                errorMsg = messageSource.getMessage("error.internalError", null, Locale.ENGLISH);
                inputScreen.setDesignAvailable(errorMsg);
                session.setAttribute("error",errorMsg);
                map.put("inputScreen", inputScreen);
                view.addObject("inputScreen", inputScreen);
                return view;
            }
            if (outPutScreens == null || outPutScreens.size() == 0) {
                view = new ModelAndView("redirect:/next.html");
                if(StandingSlipTransverseConnection.isPlus){
                    errorMsg = messageSource.getMessage("error.isPlus", null, Locale.ENGLISH);
                    session.setAttribute("error",errorMsg);
                    inputScreen.setDesignAvailable(errorMsg);
                    map.put("inputScreen", inputScreen);
                    view.addObject("inputScreen", inputScreen);
                    StandingSlipTransverseConnection.isPlus = false;
                }
                else{
                    errorMsg = messageSource.getMessage("error.design", null, Locale.ENGLISH);
                    session.setAttribute("error", errorMsg);
                    inputScreen.setDesignAvailable(errorMsg);
                    map.put("inputScreen", inputScreen);
                    view.addObject("inputScreen", inputScreen);
                }
                return view;
            }
        } else

        // if condition for T1 and T5
        if ((inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T1|| inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T5) &&
                (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T1 || inputScreen
                        .getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T5)) {
            try
            {
            outPutScreens =
                    slipAndDriveTranConn.getOptions(inputScreen,
                                                    sides,
                                                    renfService,
                                                    tieRodService,
                                                    connectionService);
            }
            catch(SmacnaException e)
            {
                view = new ModelAndView("redirect:/next.html");
                errorMsg = messageSource.getMessage("error.internalError", null, Locale.ENGLISH);
                inputScreen.setDesignAvailable(errorMsg);
                session.setAttribute("error",errorMsg);
                map.put("inputScreen", inputScreen);
                view.addObject("inputScreen", inputScreen);
                return view;
            }
            if (outPutScreens == null || outPutScreens.size() == 0) {
                view = new ModelAndView("redirect:/next.html");
                if(StandingSlipTransverseConnection.isPlus){
                    errorMsg = messageSource.getMessage("error.isPlus", null, Locale.ENGLISH);
                    session.setAttribute("error",errorMsg);
                    inputScreen.setDesignAvailable(errorMsg);
                    map.put("inputScreen", inputScreen);
                    view.addObject("inputScreen", inputScreen);
                    StandingSlipTransverseConnection.isPlus = false;
                }
                else{
                    errorMsg = messageSource.getMessage("error.design", null, Locale.ENGLISH);
                    session.setAttribute("error", errorMsg);
                    inputScreen.setDesignAvailable(errorMsg);
                    map.put("inputScreen", inputScreen);
                    view.addObject("inputScreen", inputScreen);
                }
                return view;
            }

        }
        // if condition for T1, T5, T10, T11, T12
        else if ((inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T10 ||
                inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T11 ||
                inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T12) &&
                (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T10 ||
                inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T11 ||
                inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T12)) {
            try
            {
            outPutScreens =
                    standingSlipTranConn.getOptions(inputScreen,
                                                    sides,
                                                    renfService,
                                                    tieRodService,
                                                    connectionService);
            }
            catch(SmacnaException e)
            {
                view = new ModelAndView("redirect:/next.html");
                errorMsg = messageSource.getMessage("error.internalError", null, Locale.ENGLISH);
                inputScreen.setDesignAvailable(errorMsg);
                session.setAttribute("error",errorMsg);
                map.put("inputScreen", inputScreen);
                view.addObject("inputScreen", inputScreen);
                return view;
            }
            if (outPutScreens == null || outPutScreens.size() == 0) {
                view = new ModelAndView("redirect:/next.html");
                if(StandingSlipTransverseConnection.isPlus){
                    errorMsg = messageSource.getMessage("error.isPlus", null, Locale.ENGLISH);
                    session.setAttribute("error",errorMsg);
                    inputScreen.setDesignAvailable(errorMsg);
                    map.put("inputScreen", inputScreen);
                    view.addObject("inputScreen", inputScreen);
                    StandingSlipTransverseConnection.isPlus = false;
                }
                else{
                    errorMsg = messageSource.getMessage("error.design", null, Locale.ENGLISH);
                    session.setAttribute("error", errorMsg);
                    inputScreen.setDesignAvailable(errorMsg);
                    map.put("inputScreen", inputScreen);
                    view.addObject("inputScreen", inputScreen);
                }
                return view;
            }
        }
        else if ((inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T10
                || inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T11 || inputScreen
                .getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T12)
                && (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T1 || inputScreen
                        .getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T5)) {

            try {
                outPutScreens =
                        standingSlipTranConn.getOptions(inputScreen,
                                                        sides,
                                                        renfService,
                                                        tieRodService,
                                                        connectionService);
            } catch (SmacnaException e) {
                view = new ModelAndView("redirect:/next.html");
                errorMsg = messageSource.getMessage("error.internalError", null, Locale.ENGLISH);
                inputScreen.setDesignAvailable(errorMsg);
                session.setAttribute("error",errorMsg);
                map.put("inputScreen", inputScreen);
                view.addObject("inputScreen", inputScreen);
                return view;
            }
            if (outPutScreens == null || outPutScreens.size() == 0) {
                view = new ModelAndView("redirect:/next.html");
                if(StandingSlipTransverseConnection.isPlus){
                    errorMsg = messageSource.getMessage("error.isPlus", null, Locale.ENGLISH);
                    session.setAttribute("error",errorMsg);
                    inputScreen.setDesignAvailable(errorMsg);
                    map.put("inputScreen", inputScreen);
                    view.addObject("inputScreen", inputScreen);
                    StandingSlipTransverseConnection.isPlus = false;
                }
                else{
                    errorMsg = messageSource.getMessage("error.design", null, Locale.ENGLISH);
                    session.setAttribute("error", errorMsg);
                    inputScreen.setDesignAvailable(errorMsg);
                    map.put("inputScreen", inputScreen);
                    view.addObject("inputScreen", inputScreen);
                }
                return view;
            }
        }

		else if ((inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T1 || inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T5)
				&& (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T10
						|| inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T11 || inputScreen
						.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T12)) {

            try {
                outPutScreens =
                		standingSlipTranConn.getOptions(inputScreen,
                                                        sides,
                                                        renfService,
                                                        tieRodService,
                                                        connectionService);
            } catch (SmacnaException e) {
                view = new ModelAndView("redirect:/next.html");
                errorMsg = messageSource.getMessage("error.internalError", null, Locale.ENGLISH);
                inputScreen.setDesignAvailable(errorMsg);
                session.setAttribute("error",errorMsg);
                map.put("inputScreen", inputScreen);
                view.addObject("inputScreen", inputScreen);
                return view;
            }
            if (outPutScreens == null || outPutScreens.size() == 0) {
                view = new ModelAndView("redirect:/next.html");
                if(StandingSlipTransverseConnection.isPlus){
                    errorMsg = messageSource.getMessage("error.isPlus", null, Locale.ENGLISH);
                    session.setAttribute("error",errorMsg);
                    inputScreen.setDesignAvailable(errorMsg);
                    map.put("inputScreen", inputScreen);
                    view.addObject("inputScreen", inputScreen);
                    StandingSlipTransverseConnection.isPlus = false;
                }
                else{
                    errorMsg = messageSource.getMessage("error.design", null, Locale.ENGLISH);
                    session.setAttribute("error", errorMsg);
                    inputScreen.setDesignAvailable(errorMsg);
                    map.put("inputScreen", inputScreen);
                    view.addObject("inputScreen", inputScreen);
                }
                return view;
            }
        }
        map.put("inputScreen", inputScreen);
        List<OutPutScreen> finalOutPutScreen = getScoringSystemForOutPutScreen(outPutScreens);

		if (option != null && !option.equals("")) {
			if (option.equals(Constants.SEE_ALL_RESULTS)) {
				Collections.sort(finalOutPutScreen, new OutPutScreenSorting());
				Collections.sort(finalOutPutScreen, new PrioritySorting());
				session.setAttribute("outPutScreens", finalOutPutScreen);
				session.setAttribute("outPutScreens_edit", finalOutPutScreen);
			} else if (option.equals(Constants.SEE_ONLY_INTERNAL_REINFORCEMENT)) {
				List<OutPutScreen> InternalOutPutScreen = getOnlyInternalReinforcementOptions(finalOutPutScreen);
				Collections.sort(InternalOutPutScreen,
						new OutPutScreenSorting());
				Collections.sort(InternalOutPutScreen,
						new PrioritySorting());
				session.setAttribute("outPutScreens", InternalOutPutScreen);
				session.setAttribute("outPutScreens_edit", InternalOutPutScreen);
			} else if (option.equals(Constants.SEE_ONLY_EXTERNAL_REINFORCEMENT)) {
				List<OutPutScreen> externalOutPutScreen = getOnlyExternalReinforcementOptions(finalOutPutScreen);
				Collections.sort(externalOutPutScreen,
						new OutPutScreenSorting());
				Collections.sort(externalOutPutScreen,
						new PrioritySorting());
				session.setAttribute("outPutScreens", externalOutPutScreen);
				session.setAttribute("outPutScreens_edit", externalOutPutScreen);
			}
		} else {
			List<OutPutScreen> firstList = getFinalOutPutScreen(finalOutPutScreen);
			Collections.sort(firstList, new OutPutScreenSorting());
			Collections.sort(firstList, new PrioritySorting());
			session.setAttribute("outPutScreens", firstList);
			session.setAttribute("outPutScreens_edit", firstList);
		}
        session.setAttribute("inputScreen", inputScreen);
        session.setAttribute("inputScreen_edit", inputScreen);
        view.addObject(inputScreen);
        return view;
    }
    
	private List<OutPutScreen> getOnlyInternalReinforcementOptions(
			List<OutPutScreen> outPutScreens) {

		List<OutPutScreen> outPutScreenForInternalReinforcementOnly = new ArrayList<OutPutScreen>();
		for (OutPutScreen outPutScreen : outPutScreens) {

			if (outPutScreen.isInternalReinforcement()
					|| outPutScreen.isInternalBothSide()||outPutScreen.isJtrForSideOne()||outPutScreen.isJtrForSideTwo()) {
				outPutScreen.setScore(outPutScreen.getInternalScore());
				outPutScreenForInternalReinforcementOnly.add(outPutScreen);
			}
		}

		return outPutScreenForInternalReinforcementOnly;

	}

	private List<OutPutScreen> getOnlyExternalReinforcementOptions(
			List<OutPutScreen> outPutScreens) {

		List<OutPutScreen> outPutScreenForExternalReinforcementOnly = new ArrayList<OutPutScreen>();
		for (OutPutScreen outPutScreen : outPutScreens) {

			if (outPutScreen.isExternalReinforcement()
					|| outPutScreen.isExternalBothSide()) {
				outPutScreen.setScore(outPutScreen.getExternalScore());
				outPutScreenForExternalReinforcementOnly.add(outPutScreen);
			}
		}

		return outPutScreenForExternalReinforcementOnly;

	}
    
    private List<OutPutScreen> getFinalOutPutScreen(List<OutPutScreen> outPutScreens){
    	
    	boolean isScoreZero = false;
    	for (OutPutScreen outPutScreen : outPutScreens) {
    		System.out.println("Score");
    		outPutScreen.setScore(outPutScreen.getInternalScore()+outPutScreen.getExternalScore());
			if(outPutScreen.getScore()==0){
				isScoreZero = true;
			}
		}
    	
    	if(isScoreZero){
    		return outPutScreens.subList(0, outPutScreens.size()>5?5:outPutScreens.size());
    	}else{
    		List<OutPutScreen> list = new ArrayList<OutPutScreen>();
    		for (OutPutScreen outPutScreen : outPutScreens) {
				if(outPutScreen.getInternalScore()==1&&outPutScreen.getScore()==1){
					list.add(outPutScreen);
				}
				if(outPutScreen.getExternalScore()==1&&outPutScreen.getScore()==1){
					list.add(outPutScreen);
				}
			}
    		return list;
    	}
    	
    }
    
    
    private List<OutPutScreen> getScoringSystemForOutPutScreen(List<OutPutScreen> outPutScreens){
    	List<OutPutScreen> outPutScreenList =  new ArrayList<OutPutScreen>();
    	for (OutPutScreen outPutScreen : outPutScreens) {
    		outPutScreen.setInternalScore(calculateInternalScore(outPutScreen));
    		outPutScreen.setExternalScore(calculateExternalScore(outPutScreen));
    		outPutScreen.setScore(outPutScreen.getInternalScore()+outPutScreen.getExternalScore());
    		outPutScreen.setPriority(setPriority(outPutScreen));
    		outPutScreenList.add(outPutScreen);
		}
    	
    	return outPutScreenList;
    }
    
    
    private int setPriority(OutPutScreen outPutScreen){
    	if (outPutScreen.isNoRenforcement()&&!outPutScreen.isJtrForSideOne()&&!outPutScreen.isJtrForSideTwo()) {
			return 0;
		}else{

			
			if (outPutScreen.isInternalBothSide()) {
				return 3;
			}
			if (outPutScreen.isJtrForSideOne()) {
				return 2;
			}
			if (outPutScreen.isJtrForSideTwo()) {
				return 2;
			}
			if (outPutScreen.isInternalReinforcement()) {
				return 1;
			}if(outPutScreen.isIntAndExtEachSide()){
				return 5;
			}if(outPutScreen.isExtAndInternalEachSide()){
				return 6;
			}
			
		
		
			if (outPutScreen.isExternalBothSide()) {

				return 4;
			}if (outPutScreen.isExternalReinforcement()) {
				return 2;
			}if (outPutScreen.isExtAndInternalEachSide()) {
				return 6;
			}if(outPutScreen.isIntAndExtEachSide()){
				return 5;
			}
	}
    	return 7;
    }
    
    private int calculateInternalScore(OutPutScreen outPutScreen) {

		int internalScore = 0;

		if (outPutScreen.isNoRenforcement()&&!outPutScreen.isJtrForSideOne()&&!outPutScreen.isJtrForSideTwo()) {
			return internalScore;
		} else {
			
			if (outPutScreen.isInternalBothSide()) {
				internalScore = internalScore + 2;
			}
			if (outPutScreen.isJtrForSideOne()) {
				internalScore = internalScore + 2;
			}
			if (outPutScreen.isJtrForSideTwo()) {
				internalScore = internalScore + 2;
			}
			if (outPutScreen.isInternalReinforcement()) {
				internalScore = internalScore + 1;
			}if(outPutScreen.isIntAndExtEachSide()){
				internalScore = internalScore + 1;
			}if(outPutScreen.isExtAndInternalEachSide()){
				internalScore = internalScore + 1;
			}
			
		}

		return internalScore;
	}
	
	private int calculateExternalScore(OutPutScreen outPutScreen){
		
		int externalScore = 0;
		if (outPutScreen.isNoRenforcement()) {
			return externalScore;
		} else {
			if (outPutScreen.isExternalBothSide()) {

				externalScore = externalScore + 2;
			}if (outPutScreen.isExternalReinforcement()) {
				externalScore = externalScore + 1;
			}if (outPutScreen.isExtAndInternalEachSide()) {
				externalScore = externalScore + 1;
			}if(outPutScreen.isIntAndExtEachSide()){
				externalScore = externalScore + 1;
			}
	}
		return externalScore;
	}
	
}
