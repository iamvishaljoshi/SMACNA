package com.smacna.transverseconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.smacna.exception.SmacnaException;
import com.smacna.model.InputScreen;
import com.smacna.model.OutPutScreen;
import com.smacna.service.MidPanelTieRodService;
import com.smacna.service.RectDuctRenfService;
import com.smacna.service.TransverseConnectionService;
import com.smacna.util.DuctSides;
import com.smacna.util.SmacnaUtils;
import com.smacna.util.StringsUtil;

/**
 * @author sumit.v
 * This class generate the option for the Transverse connection T25 a/b TDC/TDF
 */
@Service("tdcTdfConnection")
public class TdcTdfConnection extends TransverseConnections {

    public static final Log logg = LogFactory.getLog(TdcTdfConnection.class);
    @Override
    public List<OutPutScreen> getOptions(InputScreen inputScreen,
                                         int sides[],
                                         RectDuctRenfService renfService,
                                         MidPanelTieRodService tieRodService,
                                         TransverseConnectionService connectionService) throws SmacnaException{
        logg.info("[getOptions]:");
        List<OutPutScreen> outPutScreens = new ArrayList<OutPutScreen>();

        isJTRForSideOne = false;

        isJTRForSideTwo = false;

        sideOneT = false;

        sideTwoT = false;
        
        boolean isRenforcementClassIsGForS1RSJS = false;
        boolean isRenforcementClassIsGForS2RSJS = false;
        // For S1, RS = JS
        String S1RSJS =
                calculateSideT25abTwo(sides[0],
                                      inputScreen.getPressureClass(),
                                      inputScreen.getJointSpacing(),
                                      inputScreen.getTransConnS1(),
                                      true,
                                      renfService);

        // zeroth position contains External reinforcement
        // first position contains minimum duct gage
        // third position contains internal reinforcement or T (internal and
        // external on both side)
        String[] S1RSJSArray = StringsUtil.stripGarbage(S1RSJS);
        String EIRSJS = null;
        int minDuctGageRSJS = 0;
        String IRRSJS = null;
        if (S1RSJSArray[0].equals("NR")) {

            // NR for RS=JS
            minDuctGageRSJS = 0;

        } else {
            inputScreen.setDesignAvailable("");
            if (S1RSJSArray.length == 3) {
                EIRSJS = S1RSJSArray[0];
                minDuctGageRSJS = Integer.parseInt(S1RSJSArray[1]);
                IRRSJS = S1RSJSArray[2];
            } else if (S1RSJSArray.length == 2) {
                EIRSJS = S1RSJSArray[0];
                minDuctGageRSJS = Integer.parseInt(S1RSJSArray[1]);
            }

            else if (S1RSJSArray.length == 1) {
                minDuctGageRSJS = Integer.parseInt(S1RSJSArray[0]);
            }
            
            if(EIRSJS.equals("G")){
            	isRenforcementClassIsGForS1RSJS = true;
            }
        }
        logg.debug("##### -- IRRSJS -- ######" + IRRSJS);
        String ductGageRSJS1 = null;
        try {
            ductGageRSJS1 =
                    renfService.getGageFromRectDuctRenf(inputScreen
                            .getTransConnS1(), EIRSJS);
            if(ductGageRSJS1.contains("+") && !inputScreen.isPosOrNeg()){
                StandingSlipTransverseConnection.isPlus = true;
                return null;
            }
        } catch (SmacnaException e) {
            logg.error("[ductGageRSJS1 in case of T25a/b] : Exception Occurred: " +
                    e.getMessage());
        } catch (Exception e) {
            logg.error("[ductGageRSJS1 in case of T25a/b] : Exception Occurred : " +
                               e.getMessage(),
                       e);
        }
        String[] ductGageS1RSJ = null;
        if (ductGageRSJS1 == null || ductGageRSJS1.equals("NR")) {


        }
        else
        {
            ductGageS1RSJ = StringsUtil.stripGarbageT25(ductGageRSJS1);
        }
        inputScreen.setDesignAvailable("");
        // zeroth position contains duct gage
        // first position contains unit

        int fianlDuctGageOne = 0;
        if (ductGageS1RSJ != null) {
            fianlDuctGageOne =
                    Math.min(minDuctGageRSJS,
                             Integer.parseInt(ductGageS1RSJ[0]));
        } else {
            fianlDuctGageOne = Math.max(minDuctGageRSJS, 0);
        }
        //to check JTR for side one
        if (ductGageS1RSJ != null && ductGageS1RSJ.length == 3) {
            if (ductGageS1RSJ[2].equals("(R)")) {
                logg.info("########------JTR SIDE one------###############");
                isJTRforS1RSJS = true;
            }
        }
        // Setting reinforcement class, angle and duct gage for S1, RS = JS
        reinClassS1JSRS = EIRSJS;
        reinDuctS1JSRS = ductGageRSJS1;
        try {
            reinAngleS1JSRS =
                    renfService
                            .getMidSpanIntermediateReinforcement(reinClassS1JSRS);
        } catch (SmacnaException e) {
            logg.error("[midSpanInternediateRI in case of T25a/b] : Exception Occurred: " +
                    e.getMessage());
        } catch (Exception e) {
            logg.error("[midSpanInternediateRI in case of T25a/b] : Exception Occurred : " +
                               e.getMessage(),
                       e);
        }

        logg.info("S1, RS = JS : ");
        logg.debug("##--##- Reinforcement Class -##--##--" +
                reinClassS1JSRS);
        logg.debug("##--##- Reinforcement Angle -##--##--" +
                reinAngleS1JSRS);
        logg.debug("##--##- Reinforcement Duct -##--##--" +
                reinDuctS1JSRS);
        logg.debug("fianlDuctGageOne In case S1, RS = JS: " +
                fianlDuctGageOne);

        // For S2, RS = JS
        String S2RSJS =
                calculateSideT25abTwo(sides[1],
                                      inputScreen.getPressureClass(),
                                      inputScreen.getJointSpacing(),
                                      inputScreen.getTransConnS2(),
                                      true,
                                      renfService);

        // zeroth position contains External reinforcement
        // first position contains minimum duct gage
        // third position contains internal reinforcement
        String[] S2RSJSArray = StringsUtil.stripGarbage(S2RSJS);
        String EI2RSJS = null;
        int minDuctGageRSJS2 = 0;
        String IRRSJS2 = null;

        // to check whehter the value of guage is coming out to be zero.
        if (S2RSJSArray[0].equals("NR")) {
            // NR for RS=JS for S2
            minDuctGageRSJS2 = 0;
        } else {
            inputScreen.setDesignAvailable("");

            if (S2RSJSArray.length == 3) {
                EI2RSJS = S2RSJSArray[0];
                minDuctGageRSJS2 = Integer.parseInt(S2RSJSArray[1]);
                IRRSJS2 = S2RSJSArray[2];
            } else if (S2RSJSArray.length == 2) {
                EI2RSJS = S2RSJSArray[0];
                minDuctGageRSJS2 = Integer.parseInt(S2RSJSArray[1]);
            } else if (S2RSJSArray.length == 1) {
                minDuctGageRSJS2 = Integer.parseInt(S2RSJSArray[0]);
            }
            
            if(EI2RSJS.equals("G")){
            	isRenforcementClassIsGForS2RSJS = true;
            }
        }

        String ductGageRSJS2 = null;
        try {
            ductGageRSJS2 =
                    renfService.getGageFromRectDuctRenf(inputScreen
                            .getTransConnS2(), EI2RSJS);
            if(ductGageRSJS2.contains("+") && !inputScreen.isPosOrNeg()){
                StandingSlipTransverseConnection.isPlus = true;
                return null;
            }
        } catch (SmacnaException e) {
            logg.error("[ductGageRSJS2 in case of T25a/b] : Exception Occurred: " +
                    e.getMessage());
        } catch (Exception e) {
            logg.error("[ductGageRSJS2 in case of T25a/b] : Exception Occurred : " +
                               e.getMessage(),
                       e);
        }
        String[] ductGageS2RSJ = null;

        if (ductGageRSJS2 == null || ductGageRSJS2.equals("NR")) {


        }
        else
        {
            ductGageS2RSJ = StringsUtil.stripGarbageT25(ductGageRSJS2);
        }
        inputScreen.setDesignAvailable("");


        int fianlDuctGageTwo = 0;
        if (ductGageS2RSJ != null) {
            fianlDuctGageTwo =
                    Math.min(minDuctGageRSJS2,
                             Integer.parseInt(ductGageS2RSJ[0]));
        } else {
            fianlDuctGageTwo = Math.max(minDuctGageRSJS2, 0);
            logg.info("[getOptions]: ###########--fianlDuctGageTwo--################" +
                            fianlDuctGageTwo);
        }
        logg.debug("fianlDuctGageTwo In case of S2, Rs=Js: " +
                fianlDuctGageTwo);
        if (ductGageS2RSJ != null && ductGageS2RSJ.length == 3) {
            if (ductGageS2RSJ[2].equals("(R)")) {
                logg.info("[getOptions]: ########------JTR SIDE one------###############");
                isJTRforS2RSJS = true;
            }
        } else {
            isJTRforS2RSJS = false;
        }

        // Setting reinforcement class, angle and duct gage for S2, RS = JS
        reinClassS2JSRS = EI2RSJS;
        reinDuctS2JSRS = ductGageRSJS2;
        try {
            reinAngleS2JSRS =
                    renfService
                            .getMidSpanIntermediateReinforcement(reinClassS2JSRS);
        } catch (SmacnaException e) {
            logg.error("[midSpanInternediateRI in case of T25a/b] : Exception Occurred: " +
                    e.getMessage());
        } catch (Exception e) {
            logg.error("[midSpanInternediateRI in case of T25a/b] : Exception Occurred : " +
                               e.getMessage(),
                       e);
        }
        logg.info("[getOptions]: S2, RS = JS : ");
        logg.debug("[getOptions]:##--##- Reinforcement Class -##--##--" +
                reinClassS2JSRS );
        logg.debug("[getOptions]:##--##- Reinforcement Angle -##--##--" +
                reinAngleS2JSRS );
        logg.debug("[getOptions]: ##--##- Reinforcement Duct -##--##--" +
                reinDuctS2JSRS);

        // For S1, RS = JS/2
        String S1RSJSBy2 =
                calculateSideT25abTwo(sides[0],
                                      inputScreen.getPressureClass(),
                                      inputScreen.getJointSpacing() / 2F,
                                      inputScreen.getTransConnS1(),
                                      false,
                                      renfService);
        // zeroth position contains External reinforcement
        // first position contains minimum duct gage
        // third position contains internal reinforcement
        String[] S1Array = StringsUtil.stripGarbage(S1RSJSBy2);
        String EI1 = null;
        int minDuctGage1 = 0;
        String IR1 = null;

        if (S1Array[0].equals("NR")) {

            return null;
        } else {
            inputScreen.setDesignAvailable("");
            if (S1Array.length == 3) {
                EI1 = S1Array[0];
                minDuctGage1 = Integer.parseInt(S1Array[1]);
                IR1 = S1Array[2];
                if (IR1.equals("T")) {
                    sideOneT = true;
                }
            } else {
                EI1 = S1Array[0];
                minDuctGage1 = Integer.parseInt(S1Array[1]);
            }
        }
        logg.debug("[getOptions]:##### -- IR1 ON S1, RS=JS/2-- ######" + IR1);

        String ductGageS1RSJSBy2 = null;
        try {
            ductGageS1RSJSBy2 =
                    renfService.getGageFromRectDuctRenf(inputScreen
                            .getTransConnS1(), EI1);
            if(ductGageS1RSJSBy2.contains("+") && !inputScreen.isPosOrNeg()){
                StandingSlipTransverseConnection.isPlus = true;
                return null;
            }
        } catch (SmacnaException e) {
            logg.error("[ductGageS1RSJSBy2 in case of T25a/b] : Exception Occurred: " +
                    e.getMessage());
        } catch (Exception e) {
            logg.error("[ductGageS1RSJSBy2 in case of T25a/b] : Exception Occurred : " +
                               e.getMessage(),
                       e);
        }

        String[] ductGageS1RSJSby2 = null;
        if (ductGageS1RSJSBy2 == null || ductGageS1RSJSBy2.equals("NR")) {

        }
        else
        {
            ductGageS1RSJSby2 =
                    StringsUtil.stripGarbageT25(ductGageS1RSJSBy2);
        }
        inputScreen.setDesignAvailable("");


        String midSpanInternediateRI = null;
        try {
            midSpanInternediateRI =
                    renfService.getMidSpanIntermediateReinforcement(EI1);
        } catch (SmacnaException e) {
            logg.error("[midSpanInternediateRI in case of T25a/b] : Exception Occurred: " +
                    e.getMessage());
        } catch (Exception e) {
            logg.error("[midSpanInternediateRI in case of T25a/b] : Exception Occurred : " +
                               e.getMessage(),
                       e);
        }
        int fianlDuctGageThree = 0;
        if(ductGageS1RSJSby2!=null)
        {
        fianlDuctGageThree =
                Math.min(minDuctGage1, Integer.parseInt(ductGageS1RSJSby2[0]));
        }

        else
        {
            fianlDuctGageThree =
                    Math.max(minDuctGage1, 0);
        }

        if (ductGageS1RSJSby2!=null && ductGageS1RSJSby2.length == 3) {
            if (ductGageS1RSJSby2[2].equals("(R)")) {
                isJTRforS1RSJSBy2 = true;
            }
        }
        logg.debug("[getOptions]: fianlDuctGageThree In case S1, RS = JS/2: " +
                fianlDuctGageThree);

        // Setting reinforcement class, angle and duct gage for S1, RS =
        // JS/2
        reinClassS1JSRSBy2 = EI1;
        reinDuctS1JSRSBy2 = ductGageS1RSJSBy2;
        reinAngleS1JSRSBy2 = midSpanInternediateRI;
        logg.debug("[getOptions]: S1, RS = JS/2 : ");
        logg.debug("[getOptions]: ##--##- Reinforcement Class -##--##--" +
                reinClassS1JSRSBy2);
        logg.debug("[getOptions]: ##--##- Reinforcement Angle -##--##--" +
                reinAngleS1JSRSBy2);
        logg.debug("[getOptions]: ##--##- Reinforcement Duct -##--##--" +
                reinDuctS1JSRSBy2);

        // For S2, RS = JS/2
        String S2RSJSBy2 =
                calculateSideT25abTwo(sides[1],
                                      inputScreen.getPressureClass(),
                                      inputScreen.getJointSpacing() / 2F,
                                      inputScreen.getTransConnS2(),
                                      false,
                                      renfService);

        // zeroth position contains External reinforcement
        // first position contains minimum duct gage
        // third position contains internal reinforcement
        String[] S2Array = StringsUtil.stripGarbage(S2RSJSBy2);
        String EI2 = null;
        int minDuctGage2 = 0;
        String IR2 = null;

        if (S2Array[0].equals("NR")) {

            return null;
        } else {
            inputScreen.setDesignAvailable("");
            if (S2Array.length == 3) {
                EI2 = S2Array[0];
                minDuctGage2 = Integer.parseInt(S2Array[1]);
                IR2 = S2Array[2];
                if (IR2.equals("T")) {
                    sideTwoT = true;
                }
            } else {
                EI2 = S2Array[0];
                minDuctGage2 = Integer.parseInt(S2Array[1]);
            }
        }
        logg.debug("[getOptions]: ##### -- IR2 ON S2, RS=JS/2-- ######" + IR2);
        logg.debug("[getOptions]: ###############################" + EI2);
        String ductGageS2RSJSBy2 = null;
        String midSpanInternediateRIS2 = null;
        try {
            ductGageS2RSJSBy2 =
                    renfService.getGageFromRectDuctRenf(inputScreen
                            .getTransConnS2(), EI2);
            if(ductGageS2RSJSBy2.contains("+") && !inputScreen.isPosOrNeg()){
                StandingSlipTransverseConnection.isPlus = true;
                return null;
            }
            midSpanInternediateRIS2 =
                    renfService.getMidSpanIntermediateReinforcement(EI2);

        } catch (SmacnaException e) {
            logg.error("[ductGageS2RSJSBy2 and midSpanInternediateRIS2 in case of T25a/b] : Exception Occurred: " +
                    e.getMessage());
        } catch (Exception e) {
            logg.error("[ductGageS2RSJSBy2 and midSpanInternediateRIS2 in case of T25a/b] : Exception Occurred: " +
                               e.getMessage(),
                       e);
        }

        String[] ductGageS2RSJSby2 = null;
        if (ductGageS2RSJSBy2 == null || ductGageS2RSJSBy2.equals("NR")) {

        }
        else
        {
            ductGageS2RSJSby2 =
                    StringsUtil.stripGarbageT25(ductGageS2RSJSBy2);
        }
        inputScreen.setDesignAvailable("");

        int fianlDuctGageFour = 0;
        if(ductGageS2RSJSby2!=null)
        {
        fianlDuctGageFour  = Math.min(minDuctGage2, Integer.parseInt(ductGageS2RSJSby2[0]));
        }
        else
        {
            fianlDuctGageFour  = Math.max(minDuctGage2, 0);
        }

        if (ductGageS2RSJSby2!=null && ductGageS2RSJSby2.length == 3) {
            if (ductGageS2RSJSby2[2].equals("(R)")) {
                logg.debug("[getOptions]: ########------JTR SIDE Two------###############");
                isJTRforS2RSJSBy2 = true;
            }
        }
        // Setting reinforcement class, angle and duct gage for S2, RS =
        // JS/2
        reinClassS2JSRSBy2 = EI2;
        reinDuctS2JSRSBy2 = ductGageS2RSJSBy2;
        reinAngleS2JSRSBy2 = midSpanInternediateRIS2;
        logg.info("[getOptions]: S2, RS = JS/2 : ");
        logg.debug("[getOptions]:##--##- Reinforcement Class -##--##--" +
                reinClassS2JSRSBy2);
        logg.debug("[getOptions]:##--##- Reinforcement Angle -##--##--" +
                reinAngleS2JSRSBy2);
        logg.debug("[getOptions]:##--##- Reinforcement Duct -##--##--" +
                reinDuctS2JSRSBy2);
        logg.debug("[getOptions]:fianlDuctGageFour In case S2, RS = JS/2: " +
                fianlDuctGageFour);

        // S1, RS=JS and S2 RS=JS
        int fianlDuctGageI = Math.min(fianlDuctGageOne, fianlDuctGageTwo);
        // S1, RS=JS/2 and S2 RS=JS External reinforcement is required

        int fianlDuctGageII = Math.min(fianlDuctGageThree, fianlDuctGageTwo);
        // S1, RS=JS and S2 RS=JS/2
        int fianlDuctGageIII = Math.min(fianlDuctGageOne, fianlDuctGageFour);
        // S1, RS=JS/2 and S2 RS=JS/2
        int fianlDuctGageIV = Math.min(fianlDuctGageThree, fianlDuctGageFour);

        OutPutScreen outPutScreenOne = new OutPutScreen();
        OutPutScreen outPutScreenTwo = new OutPutScreen();
        OutPutScreen outPutScreenThree = new OutPutScreen();
        OutPutScreen outPutScreenFour = new OutPutScreen();
        OutPutScreen outPutScreenSix = new OutPutScreen();
        OutPutScreen outPutScreenSeven = new OutPutScreen();

        // Use only for Tie Rod
        OutPutScreen outPutScreenFive = new OutPutScreen();

        logg.info("[getOptions]:fianlDuctGageI : " + fianlDuctGageI);
        logg.info("[getOptions]:fianlDuctGageII : " + fianlDuctGageII);
        logg.info("[getOptions]:fianlDuctGageIII : " + fianlDuctGageIII);
        logg.info("[getOptions]:fianlDuctGageIV : " + fianlDuctGageIV);

        
            float pc = 0;
            try {
                pc =
                        connectionService.getStaticPC(inputScreen
                                .getPressureClass());
            } catch (SmacnaException e) {
                logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                        e.getMessage());
            } catch (Exception e) {
                logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                                   e.getMessage(),
                           e);
            }
            double tieRodLoadJTRSideOne = 0.0;
            double tieRodLoadJTRSideTwo = 0.0;
            String IsJTRSideOne[] = null;
            String IsJTRSideTwo[] = null;
            if (reinDuctS1JSRS != null) {
                IsJTRSideOne = StringsUtil.stripGarbageT25(reinDuctS1JSRS);
            }
            if (reinDuctS2JSRS != null) {
                IsJTRSideTwo = StringsUtil.stripGarbageT25(reinDuctS2JSRS);
            }

//            if (fianlDuctGageI == fianlDuctGageIII) {

                if (fianlDuctGageI != 0) {

                    outPutScreenOne.setSideOne(sides[0]);
                    outPutScreenOne.setSideTwo(sides[1]);
                    outPutScreenOne.setExternalReinforcement(false);
                    outPutScreenOne.setInternalReinforcement(false);
                    outPutScreenOne.setNoRenforcement(true);

                    if (IsJTRSideOne != null && IsJTRSideOne.length == 3) {
                        if (IsJTRSideOne[2].equals("(R)")) {
                            outPutScreenOne.setJtrForSideOne(true);
                        } else {
                            outPutScreenOne.setJtrForSideOne(false);
                        }
                    }
                    if (IsJTRSideTwo != null && IsJTRSideTwo.length == 3) {
                        if (IsJTRSideTwo[2].equals("(R)")) {
                            outPutScreenOne.setJtrForSideTwo(true);
                        } else {
                            outPutScreenOne.setJtrForSideTwo(false);
                        }
                    }

                    if (outPutScreenOne.isJtrForSideOne()) {
                        tieRodLoadJTRSideOne =
                                SmacnaUtils
                                        .getTieRodLoadForJTR(sides[0],
                                                             (inputScreen
                                                                     .getJointSpacing() * 12 - 4) ,
                                                             0,
                                                             pc);
                        String jtrSizeForS1 = "";
                        if (inputScreen.isPosOrNeg()) {

                            jtrSizeForS1 =
                                        SmacnaUtils.getEMT(tieRodLoadJTRSideOne);

                        } else {
                            int[] dim =
                                    DuctSides.getSides(Integer
                                            .parseInt(inputScreen
                                                    .getHeight()), Integer
                                            .parseInt(inputScreen
                                                    .getWidth()));
                            try {

                                jtrSizeForS1 =
                                            connectionService
                                                    .getEmtForNegPC(dim[1],
                                                                    tieRodLoadJTRSideOne);

                            } catch (SmacnaException e) {
                                logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                   e.getMessage(),
                                           e);
                            }
                        }
                        outPutScreenOne.setJtrSizeForS1(jtrSizeForS1);


                    }
                    if (outPutScreenOne.isJtrForSideTwo()) {
                        tieRodLoadJTRSideTwo =
                                SmacnaUtils
                                        .getTieRodLoadForJTR(sides[1],
                                                             (inputScreen
                                                                     .getJointSpacing() * 12 - 4) ,
                                                             0,
                                                             pc);
                        String jtrSizeForS2 = "";
                        if (inputScreen.isPosOrNeg()) {

                            jtrSizeForS2 =
                                        SmacnaUtils.getEMT(tieRodLoadJTRSideTwo);

                        } else {
                            int[] dim =
                                    DuctSides.getSides(Integer
                                            .parseInt(inputScreen
                                                    .getHeight()), Integer
                                            .parseInt(inputScreen
                                                    .getWidth()));
                            try {

                                jtrSizeForS2 =
                                            connectionService
                                                    .getEmtForNegPC(dim[0],
                                                                    tieRodLoadJTRSideTwo);

                            } catch (SmacnaException e) {
                                logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                   e.getMessage(),
                                           e);
                            }
                        }
                        outPutScreenOne.setJtrSizeForS2(jtrSizeForS2);
                    }
                    outPutScreenOne
                            .setTieRoadLoadJTRForSideOne(tieRodLoadJTRSideOne);
                    outPutScreenOne
                            .setTieRoadLoadJTRForSideTwo(tieRodLoadJTRSideTwo);
                    outPutScreenOne.setExternalRenfAngle("");
                    outPutScreenOne.setMinDuctGageSOne(fianlDuctGageOne);
                    outPutScreenOne.setMinDuctGageSTwo(fianlDuctGageTwo);
                    outPutScreenOne.setFinalDuctGage(fianlDuctGageI);

                    // Setting Reinforcement Class, Angle and Duct gage for
                    // S1, RS=JS and S2, RS=JS

                    // Class:
                    outPutScreenOne.setReinClassForSideOne(reinClassS1JSRS);
                    outPutScreenOne.setReinClassForSideTwo(reinClassS2JSRS);
                    // Angle:
                    outPutScreenOne.setExtRenfAngleSideOne(reinAngleS1JSRS);
                    outPutScreenOne.setExtRenfAngleSideTwo(reinAngleS2JSRS);
                    // Duct Gage:
                    outPutScreenOne.setDuctGageForSideOne(reinDuctS1JSRS);
                    outPutScreenOne.setDuctGageForSideTwo(reinDuctS2JSRS);

                    outPutScreenOne
                            .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                             .getPressureClass(),
                                                                     connectionService));
                    outPutScreenOne.setNoTieRodForSideOne(0);
                    outPutScreenOne.setNoTieRodForSideTwo(0);
                    outPutScreenOne.setTieRodLoadForSideOne(0.0);
                    outPutScreenOne.setTieRod("");
                    outPutScreenOne.setExtRenfSide(String.valueOf(sides[0]));
                    outPutScreens.add(outPutScreenOne);
                }

                if (fianlDuctGageII != 0) {
                    int noOfTieRodForS1 = 0;
                    int noOfTieRodForS2 = 0;
                    if (sideOneT || sideTwoT) {
                        if (sideOneT && sideTwoT) {

                            logg.info("[getOptions]: INTERNAL AND EXTERNAL ON SAME SIDE---ONE");
                            noOfTieRodForS1 = 0;
                            noOfTieRodForS2 = 0;
                            noOfTieRodForS1 =
                                    calculateSideT25abFive(sides[0],
                                                           inputScreen
                                                                   .getPressureClass(),
                                                           inputScreen
                                                                   .getJointSpacing() / 2F,
                                                           inputScreen
                                                                   .getTransConnS1(),
                                                           fianlDuctGageII,
                                                           tieRodService);

                            noOfTieRodForS2 =
                                    calculateSideT25abFive(sides[1],
                                                           inputScreen
                                                                   .getPressureClass(),
                                                           inputScreen
                                                                   .getJointSpacing() / 2F,
                                                           inputScreen
                                                                   .getTransConnS2(),
                                                           fianlDuctGageII,
                                                           tieRodService);
                            if (noOfTieRodForS1 == 0) {
                                noOfTieRodForS1 = 1;
                            }
                            if (noOfTieRodForS2 == 0) {
                                noOfTieRodForS2 = 1;
                            }

                            try {
                                pc =
                                        connectionService
                                                .getStaticPC(inputScreen
                                                        .getPressureClass());
                            } catch (SmacnaException e) {
                                logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                                        e.getMessage());
                            } catch (Exception e) {
                                logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                                                   e.getMessage(),
                                           e);
                            }
                            double tieRodLoadForS1 = 0.0;
                            if (noOfTieRodForS1 != 0) {
                                tieRodLoadForS1 =
                                        SmacnaUtils
                                                .getTieRodLoadWithT(sides[0],
                                                                     (inputScreen
                                                                             .getJointSpacing() * 12 - 4) / 2F,
                                                                     noOfTieRodForS1,
                                                                     pc);
                            }
                            tieRodLoadJTRSideOne = 0.0;
                            tieRodLoadJTRSideTwo = 0.0;

                            double tieRodLoadForS2 = 0.0;
                            if (noOfTieRodForS2 != 0) {
                                tieRodLoadForS2 =
                                        SmacnaUtils
                                                .getTieRodLoadWithT(sides[1],
                                                                     (inputScreen
                                                                             .getJointSpacing() * 12 - 4) / 2F,
                                                                     noOfTieRodForS2,
                                                                     pc);
                            }
                            logg.info("[getOptions]: ####-----####--TIE ROD LOAD S1---" +
                                            tieRodLoadForS1);
                            logg.info("[getOptions]: ####-----####--TIE ROD LOAD S2---" +
                                            tieRodLoadForS2);

                            logg.info("[getOptions]: ####-----####--TIE ROD LOAD S1 JTR---" +
                                            tieRodLoadJTRSideOne);
                            logg.info("[getOptions]: ####-----####--TIE ROD LOAD S2 JTR---" +
                                            tieRodLoadJTRSideTwo);
                            String EMTForS1 = "No MPT Required";
                            String EMTForS2 = "No MPT Required";
                            if (inputScreen.isPosOrNeg()) {
                                if (noOfTieRodForS1 != 0) {
                                    EMTForS1 =
                                            SmacnaUtils.getEMT(tieRodLoadForS1);
                                }
                                if (noOfTieRodForS2 != 0) {
                                    EMTForS2 =
                                            SmacnaUtils.getEMT(tieRodLoadForS2);
                                }
                            } else {
                                int[] dim =
                                        DuctSides.getSides(Integer
                                                .parseInt(inputScreen
                                                        .getHeight()), Integer
                                                .parseInt(inputScreen
                                                        .getWidth()));
                                try {
                                    if (noOfTieRodForS1 != 0) {
                                        EMTForS1 =
                                                connectionService
                                                        .getEmtForNegPC(dim[1],
                                                                        tieRodLoadForS1);
                                    }
                                    if (noOfTieRodForS2 != 0) {
                                        EMTForS2 =
                                                connectionService
                                                        .getEmtForNegPC(dim[0],
                                                                        tieRodLoadForS2);
                                    }
                                } catch (SmacnaException e) {
                                    logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                       e.getMessage(),
                                               e);
                                }
                            }

                            if (EMTForS1 == null || EMTForS1.equals("error")) {

                                logg.error("[getOptions]: ##########--E R R O R--##########");
                            } else {

                                outPutScreenTwo.setSideOne(sides[0]);
                                outPutScreenTwo.setSideTwo(sides[1]);
                                outPutScreenTwo.setExternalReinforcement(false);
                                outPutScreenTwo
                                        .setIntAndExternalOnBothSide(true);




                                        outPutScreenTwo.setJtrForSideOne(true);


                                        outPutScreenTwo.setJtrForSideTwo(true);

                                if (outPutScreenTwo.isJtrForSideOne()) {
                                    tieRodLoadJTRSideOne =
                                            SmacnaUtils
                                                    .getTieRodLoadForJTR(sides[0],
                                                                         (inputScreen
                                                                                 .getJointSpacing() * 12 - 4) / 2F,
                                                                         noOfTieRodForS1,
                                                                         pc);
                                    String jtrSizeForS1 = "";
                                    if (inputScreen.isPosOrNeg()) {

                                        jtrSizeForS1 =
                                                    SmacnaUtils.getEMT(tieRodLoadJTRSideOne);

                                    } else {
                                        int[] dim =
                                                DuctSides.getSides(Integer
                                                        .parseInt(inputScreen
                                                                .getHeight()), Integer
                                                        .parseInt(inputScreen
                                                                .getWidth()));
                                        try {

                                            jtrSizeForS1 =
                                                        connectionService
                                                                .getEmtForNegPC(dim[1],
                                                                                tieRodLoadJTRSideOne);

                                        } catch (SmacnaException e) {
                                            logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                               e.getMessage(),
                                                       e);
                                        }
                                    }
                                    outPutScreenTwo.setJtrSizeForS1(jtrSizeForS1);
                                }
                                if (outPutScreenTwo.isJtrForSideTwo()) {
                                    tieRodLoadJTRSideTwo =
                                            SmacnaUtils
                                                    .getTieRodLoadForJTR(sides[1],
                                                                         (inputScreen
                                                                                 .getJointSpacing() * 12 - 4) ,
                                                                         noOfTieRodForS2,
                                                                         pc);
                                    String jtrSizeForS2 = "";
                                    if (inputScreen.isPosOrNeg()) {

                                        jtrSizeForS2 =
                                                    SmacnaUtils.getEMT(tieRodLoadJTRSideTwo);

                                    } else {
                                        int[] dim =
                                                DuctSides.getSides(Integer
                                                        .parseInt(inputScreen
                                                                .getHeight()), Integer
                                                        .parseInt(inputScreen
                                                                .getWidth()));
                                        try {

                                            jtrSizeForS2 =
                                                        connectionService
                                                                .getEmtForNegPC(dim[0],
                                                                                tieRodLoadJTRSideTwo);

                                        } catch (SmacnaException e) {
                                            logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                               e.getMessage(),
                                                       e);
                                        }
                                    }
                                    outPutScreenTwo.setJtrSizeForS2(jtrSizeForS2);
                                }
                                outPutScreenTwo
                                        .setTieRoadLoadJTRForSideOne(tieRodLoadJTRSideOne);
                                outPutScreenTwo
                                        .setTieRoadLoadJTRForSideTwo(tieRodLoadJTRSideTwo);
                                outPutScreenTwo.setInternalReinforcement(false);

                                outPutScreenTwo
                                        .setMinDuctGageSOne(fianlDuctGageThree);
                                outPutScreenTwo
                                        .setMinDuctGageSTwo(fianlDuctGageTwo);
                                outPutScreenTwo
                                        .setFinalDuctGage(fianlDuctGageII);

                                // Setting Reinforcement Class, Angle and
                                // Duct gage
                                // for
                                // S1, RS=JS/2 and S2, RS=JS

                                // Class:
                                outPutScreenTwo
                                        .setReinClassForSideOne(reinClassS1JSRSBy2);
                                outPutScreenTwo
                                        .setReinClassForSideTwo(reinClassS2JSRS);
                                // Angle:
                                outPutScreenTwo
                                        .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                                outPutScreenTwo
                                        .setExtRenfAngleSideTwo(reinAngleS2JSRS);
                                // Duct Gage:
                                outPutScreenTwo
                                        .setDuctGageForSideOne(reinDuctS1JSRSBy2);
                                outPutScreenTwo
                                        .setDuctGageForSideTwo(reinDuctS2JSRS);

                                outPutScreenTwo
                                        .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                                         .getPressureClass(),
                                                                                 connectionService));
                                outPutScreenTwo
                                        .setNoTieRodForSideOne(noOfTieRodForS1);

                                outPutScreenTwo
                                        .setNoTieRodForSideTwo(noOfTieRodForS2);
                                outPutScreenTwo
                                        .setTieRodLoadForSideOne(tieRodLoadForS1);
                                outPutScreenTwo
                                        .setTieRodLoadForSideTwo(tieRodLoadForS2);
                                outPutScreenTwo.setTieRod("");
                                outPutScreenTwo.setExtRenfSide(String
                                        .valueOf(sides[0]));
                                outPutScreens.add(outPutScreenTwo);

                            }

                        } else {

                            if (sideOneT) {

                                noOfTieRodForS1 = 0;
                                noOfTieRodForS2 = 0;
                                noOfTieRodForS1 =
                                        calculateSideT25abFive(sides[0],
                                                               inputScreen
                                                                       .getPressureClass(),
                                                               inputScreen
                                                                       .getJointSpacing() / 2F,
                                                               inputScreen
                                                                       .getTransConnS1(),
                                                               fianlDuctGageII,
                                                               tieRodService);

                                noOfTieRodForS2 =
                                        calculateSideT25abFive(sides[1],
                                                               inputScreen
                                                                       .getPressureClass(),
                                                               inputScreen
                                                                       .getJointSpacing() / 2F,
                                                               inputScreen
                                                                       .getTransConnS2(),
                                                               fianlDuctGageII,
                                                               tieRodService);
                                if (noOfTieRodForS1 == 0) {
                                    noOfTieRodForS1 = 1;
                                }
                                if (noOfTieRodForS2 == 0) {
                                    noOfTieRodForS2 = 1;
                                }

                                try {
                                    pc =
                                            connectionService
                                                    .getStaticPC(inputScreen
                                                            .getPressureClass());
                                } catch (SmacnaException e) {
                                    logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                                            e.getMessage());
                                } catch (Exception e) {
                                    logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                                                       e.getMessage(),
                                               e);
                                }
                                double tieRodLoadForS1 = 0.0;
                                if (noOfTieRodForS1 != 0) {
                                    tieRodLoadForS1 =
                                            SmacnaUtils
                                                    .getTieRodLoadWithT(sides[0],
                                                                         (inputScreen
                                                                                 .getJointSpacing() * 12 - 4) / 2F,
                                                                         noOfTieRodForS1,
                                                                         pc);
                                }
                                tieRodLoadJTRSideOne = 0.0;
                                tieRodLoadJTRSideTwo = 0.0;
                                String EMTForS1 = "No MPT Required";
                                String EMTForS2 = "No MPT Required";
                                if (inputScreen.isPosOrNeg()) {
                                    if (noOfTieRodForS1 != 0) {
                                        EMTForS1 =
                                                SmacnaUtils
                                                        .getEMT(tieRodLoadForS1);
                                    }
                                } else {
                                    int[] dim =
                                            DuctSides
                                                    .getSides(Integer.parseInt(inputScreen
                                                                      .getHeight()),
                                                              Integer.parseInt(inputScreen
                                                                      .getWidth()));
                                    try {
                                        if (noOfTieRodForS1 != 0) {
                                            EMTForS1 =
                                                    connectionService
                                                            .getEmtForNegPC(dim[1],
                                                                            tieRodLoadForS1);
                                        }

                                    } catch (SmacnaException e) {
                                        logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                           e.getMessage(),
                                                   e);
                                    }
                                }

                                if (EMTForS1 == null ||
                                        EMTForS1.equals("error")) {

                                    logg.error("[getOptions]: ##########--E R R O R--##########");
                                } else {

                                    outPutScreenTwo.setSideOne(sides[0]);
                                    outPutScreenTwo.setSideTwo(sides[1]);
                                    outPutScreenTwo
                                            .setExternalReinforcement(false);
                                    outPutScreenTwo.setIntAndExtOnSideOne(true);


                                    if (reinDuctS2JSRS != null) {
                                        IsJTRSideTwo =
                                                StringsUtil
                                                        .stripGarbageT25(reinDuctS2JSRS);
                                    }

                                            outPutScreenTwo
                                                    .setJtrForSideOne(true);

                                    if (IsJTRSideTwo != null &&
                                            IsJTRSideTwo.length == 3) {
                                        if (IsJTRSideTwo[2].equals("(R)")) {
                                            outPutScreenTwo
                                                    .setJtrForSideTwo(true);
                                        } else {
                                            outPutScreenTwo
                                                    .setJtrForSideTwo(false);
                                        }
                                    } else {
                                        outPutScreenTwo.setJtrForSideTwo(false);
                                    }
                                    if (outPutScreenTwo.isJtrForSideOne()) {
                                        tieRodLoadJTRSideOne =
                                                SmacnaUtils
                                                        .getTieRodLoadForJTR(sides[0],
                                                                             (inputScreen
                                                                                     .getJointSpacing() * 12 - 4) / 2F,
                                                                             noOfTieRodForS1,
                                                                             pc);
                                        String jtrSizeForS1 = "";
                                        if (inputScreen.isPosOrNeg()) {

                                            jtrSizeForS1 =
                                                        SmacnaUtils.getEMT(tieRodLoadJTRSideOne);

                                        } else {
                                            int[] dim =
                                                    DuctSides.getSides(Integer
                                                            .parseInt(inputScreen
                                                                    .getHeight()), Integer
                                                            .parseInt(inputScreen
                                                                    .getWidth()));
                                            try {

                                                jtrSizeForS1 =
                                                            connectionService
                                                                    .getEmtForNegPC(dim[1],
                                                                                    tieRodLoadJTRSideOne);

                                            } catch (SmacnaException e) {
                                                logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                                   e.getMessage(),
                                                           e);
                                            }
                                        }
                                        outPutScreenTwo.setJtrSizeForS1(jtrSizeForS1);
                                    }
                                    if (outPutScreenTwo.isJtrForSideTwo()) {
                                        tieRodLoadJTRSideTwo =
                                                SmacnaUtils
                                                        .getTieRodLoadForJTR(sides[1],
                                                                             (inputScreen
                                                                                     .getJointSpacing() * 12 - 4) ,
                                                                             noOfTieRodForS1,
                                                                             pc);
                                        String jtrSizeForS2 = "";
                                        if (inputScreen.isPosOrNeg()) {

                                            jtrSizeForS2 =
                                                        SmacnaUtils.getEMT(tieRodLoadJTRSideTwo);

                                        } else {
                                            int[] dim =
                                                    DuctSides.getSides(Integer
                                                            .parseInt(inputScreen
                                                                    .getHeight()), Integer
                                                            .parseInt(inputScreen
                                                                    .getWidth()));
                                            try {

                                                jtrSizeForS2 =
                                                            connectionService
                                                                    .getEmtForNegPC(dim[0],
                                                                                    tieRodLoadJTRSideTwo);

                                            } catch (SmacnaException e) {
                                                logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                                   e.getMessage(),
                                                           e);
                                            }
                                        }
                                        outPutScreenTwo.setJtrSizeForS2(jtrSizeForS2);
                                    }
                                    outPutScreenTwo
                                            .setTieRoadLoadJTRForSideOne(tieRodLoadJTRSideOne);
                                    outPutScreenTwo
                                            .setTieRoadLoadJTRForSideTwo(tieRodLoadJTRSideTwo);
                                    outPutScreenTwo
                                            .setInternalReinforcement(false);
                                    outPutScreenTwo
                                            .setTieRodForSideOne(EMTForS1);
                                    outPutScreenTwo
                                            .setMinDuctGageSOne(fianlDuctGageThree);
                                    outPutScreenTwo
                                            .setMinDuctGageSTwo(fianlDuctGageTwo);
                                    outPutScreenTwo
                                            .setFinalDuctGage(fianlDuctGageII);

                                    // Setting Reinforcement Class, Angle
                                    // and
                                    // Duct gage for
                                    // S1, RS=JS/2 and S2, RS=JS

                                    // Class:
                                    outPutScreenTwo
                                            .setReinClassForSideOne(reinClassS1JSRSBy2);
                                    outPutScreenTwo
                                            .setReinClassForSideTwo(reinClassS2JSRS);
                                    // Angle:
                                    outPutScreenTwo
                                            .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                                    outPutScreenTwo
                                            .setExtRenfAngleSideTwo(reinAngleS2JSRS);
                                    // Duct Gage:
                                    outPutScreenTwo
                                            .setDuctGageForSideOne(reinDuctS1JSRSBy2);
                                    outPutScreenTwo
                                            .setDuctGageForSideTwo(reinDuctS2JSRS);

                                    outPutScreenTwo
                                            .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                                             .getPressureClass(),
                                                                                     connectionService));
                                    outPutScreenTwo
                                            .setNoTieRodForSideOne(noOfTieRodForS1);

                                    outPutScreenTwo
                                            .setNoTieRodForSideTwo(noOfTieRodForS2);
                                    outPutScreenTwo
                                            .setTieRodLoadForSideOne(tieRodLoadForS1);
                                    outPutScreenTwo.setTieRod("");
                                    outPutScreenTwo.setExtRenfSide(String
                                            .valueOf(sides[0]));
                                    outPutScreens.add(outPutScreenTwo);
                                }

                            } else if (sideTwoT) {

                                noOfTieRodForS1 = 0;
                                noOfTieRodForS2 = 0;
                                noOfTieRodForS1 =
                                        calculateSideT25abFive(sides[0],
                                                               inputScreen
                                                                       .getPressureClass(),
                                                               inputScreen
                                                                       .getJointSpacing() / 2F,
                                                               inputScreen
                                                                       .getTransConnS1(),
                                                               fianlDuctGageII,
                                                               tieRodService);

                                noOfTieRodForS2 =
                                        calculateSideT25abFive(sides[1],
                                                               inputScreen
                                                                       .getPressureClass(),
                                                               inputScreen
                                                                       .getJointSpacing() / 2F,
                                                               inputScreen
                                                                       .getTransConnS2(),
                                                               fianlDuctGageII,
                                                               tieRodService);
                                if (noOfTieRodForS1 == 0) {
                                    noOfTieRodForS1 = 1;
                                }
                                if (noOfTieRodForS2 == 0) {
                                    noOfTieRodForS2 = 1;
                                }

                                try {
                                    pc =
                                            connectionService
                                                    .getStaticPC(inputScreen
                                                            .getPressureClass());
                                } catch (SmacnaException e) {
                                    logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                                            e.getMessage());
                                } catch (Exception e) {
                                    logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                                                       e.getMessage(),
                                               e);
                                }
                                double tieRodLoadForS2 = 0.0;
                                if (noOfTieRodForS2 != 0) {
                                    tieRodLoadForS2 =
                                            SmacnaUtils
                                                    .getTieRodLoadWithT(sides[1],
                                                                         (inputScreen
                                                                                 .getJointSpacing() * 12 - 4) / 2F,
                                                                         noOfTieRodForS2,
                                                                         pc);
                                }
                                tieRodLoadJTRSideOne = 0.0;
                                tieRodLoadJTRSideTwo = 0.0;

                                String EMTForS1 = "No MPT Required";
                                String EMTForS2 = "No MPT Required";
                                if (inputScreen.isPosOrNeg()) {
                                    if (noOfTieRodForS2 != 0) {
                                        EMTForS2 =
                                                SmacnaUtils
                                                        .getEMT(tieRodLoadForS2);
                                    }
                                } else {
                                    int[] dim =
                                            DuctSides
                                                    .getSides(Integer.parseInt(inputScreen
                                                                      .getHeight()),
                                                              Integer.parseInt(inputScreen
                                                                      .getWidth()));
                                    try {
                                        if (noOfTieRodForS2 != 0) {
                                            EMTForS2 =
                                                    connectionService
                                                            .getEmtForNegPC(dim[0],
                                                                            tieRodLoadForS2);
                                        }

                                    } catch (SmacnaException e) {
                                        logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                           e.getMessage(),
                                                   e);
                                    }
                                }

                                if (EMTForS2 == null ||
                                        EMTForS2.equals("error")) {

                                    logg.error("[getOptions]: ##########--E R R O R--##########");
                                } else {

                                    outPutScreenTwo.setSideOne(sides[0]);
                                    outPutScreenTwo.setSideTwo(sides[1]);
                                    outPutScreenTwo
                                            .setExternalReinforcement(false);
                                    outPutScreenTwo.setIntAndExtOnSideTwo(true);

                                    if (reinDuctS1JSRSBy2 != null) {
                                        IsJTRSideOne =
                                                StringsUtil
                                                        .stripGarbageT25(reinDuctS1JSRSBy2);
                                    }

                                    if (IsJTRSideOne != null &&
                                            IsJTRSideOne.length == 3) {
                                        if (IsJTRSideOne[2].equals("(R)")) {
                                            outPutScreenTwo
                                                    .setJtrForSideOne(true);
                                        } else {
                                            outPutScreenTwo
                                                    .setJtrForSideOne(false);
                                        }
                                    } else {
                                        outPutScreenTwo.setJtrForSideOne(false);
                                    }

                                            outPutScreenTwo
                                                    .setJtrForSideTwo(true);

                                    tieRodLoadJTRSideOne = 0.0;
                                    if (outPutScreenTwo.isJtrForSideOne()) {
                                        tieRodLoadJTRSideOne =
                                                SmacnaUtils
                                                        .getTieRodLoadForJTR(sides[0],
                                                                             (inputScreen
                                                                                     .getJointSpacing() * 12 - 4) / 2F,
                                                                             noOfTieRodForS1,
                                                                             pc);
                                        String jtrSizeForS1 = "";
                                        if (inputScreen.isPosOrNeg()) {

                                            jtrSizeForS1 =
                                                        SmacnaUtils.getEMT(tieRodLoadJTRSideOne);

                                        } else {
                                            int[] dim =
                                                    DuctSides.getSides(Integer
                                                            .parseInt(inputScreen
                                                                    .getHeight()), Integer
                                                            .parseInt(inputScreen
                                                                    .getWidth()));
                                            try {

                                                jtrSizeForS1 =
                                                            connectionService
                                                                    .getEmtForNegPC(dim[1],
                                                                                    tieRodLoadJTRSideOne);

                                            } catch (SmacnaException e) {
                                                logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                                   e.getMessage(),
                                                           e);
                                            }
                                        }
                                        outPutScreenTwo.setJtrSizeForS1(jtrSizeForS1);
                                    }
                                    tieRodLoadJTRSideTwo = 0.0;
                                    if (outPutScreenTwo.isJtrForSideTwo()) {
                                        tieRodLoadJTRSideTwo =
                                                SmacnaUtils
                                                        .getTieRodLoadForJTR(sides[1],
                                                                             (inputScreen
                                                                                     .getJointSpacing() * 12 - 4),
                                                                             noOfTieRodForS2,
                                                                             pc);
                                        String jtrSizeForS2 = "";
                                        if (inputScreen.isPosOrNeg()) {

                                            jtrSizeForS2 =
                                                        SmacnaUtils.getEMT(tieRodLoadJTRSideTwo);

                                        } else {
                                            int[] dim =
                                                    DuctSides.getSides(Integer
                                                            .parseInt(inputScreen
                                                                    .getHeight()), Integer
                                                            .parseInt(inputScreen
                                                                    .getWidth()));
                                            try {

                                                jtrSizeForS2 =
                                                            connectionService
                                                                    .getEmtForNegPC(dim[0],
                                                                                    tieRodLoadJTRSideTwo);

                                            } catch (SmacnaException e) {
                                                logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                                   e.getMessage(),
                                                           e);
                                            }
                                        }
                                        outPutScreenTwo.setJtrSizeForS2(jtrSizeForS2);
                                    }
                                    outPutScreenTwo
                                            .setTieRoadLoadJTRForSideOne(tieRodLoadJTRSideOne);
                                    outPutScreenTwo
                                            .setTieRoadLoadJTRForSideTwo(tieRodLoadJTRSideTwo);
                                    outPutScreenTwo
                                            .setInternalReinforcement(false);
                                    outPutScreenTwo
                                            .setTieRodForSideTwo(EMTForS2);
                                    outPutScreenTwo
                                            .setMinDuctGageSOne(fianlDuctGageThree);
                                    outPutScreenTwo
                                            .setMinDuctGageSTwo(fianlDuctGageTwo);
                                    outPutScreenTwo
                                            .setFinalDuctGage(fianlDuctGageII);

                                    // Setting Reinforcement Class, Angle
                                    // and
                                    // Duct gage for
                                    // S1, RS=JS/2 and S2, RS=JS

                                    // Class:
                                    outPutScreenTwo
                                            .setReinClassForSideOne(reinClassS1JSRSBy2);
                                    outPutScreenTwo
                                            .setReinClassForSideTwo(reinClassS2JSRS);
                                    // Angle:
                                    outPutScreenTwo
                                            .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                                    outPutScreenTwo
                                            .setExtRenfAngleSideTwo(reinAngleS2JSRS);
                                    // Duct Gage:
                                    outPutScreenTwo
                                            .setDuctGageForSideOne(reinDuctS1JSRSBy2);
                                    outPutScreenTwo
                                            .setDuctGageForSideTwo(reinDuctS2JSRS);

                                    outPutScreenTwo
                                            .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                                             .getPressureClass(),
                                                                                     connectionService));
                                    outPutScreenTwo.setNoTieRodForSideOne(0);

                                    outPutScreenTwo
                                            .setNoTieRodForSideTwo(noOfTieRodForS2);

                                    outPutScreenTwo
                                            .setTieRodLoadForSideOne(0.0);
                                    outPutScreenTwo
                                            .setTieRodLoadForSideTwo(tieRodLoadForS2);
                                    outPutScreenTwo
                                            .setTieRodForSideTwo(EMTForS2);
                                    outPutScreenTwo.setTieRod("");
                                    outPutScreenTwo.setExtRenfSide(String
                                            .valueOf(sides[0]));
                                    outPutScreens.add(outPutScreenTwo);
                                }

                            }
                        }
                    } else {

                        outPutScreenTwo.setSideOne(sides[0]);
                        outPutScreenTwo.setSideTwo(sides[1]);
                        outPutScreenTwo.setExternalReinforcement(true);
                        outPutScreenTwo.setInternalReinforcement(false);
                        if (reinDuctS1JSRSBy2 != null) {
                            IsJTRSideOne =
                                    StringsUtil
                                            .stripGarbageT25(reinDuctS1JSRSBy2);
                        }
                        if (reinDuctS2JSRS != null) {
                            IsJTRSideTwo =
                                    StringsUtil.stripGarbageT25(reinDuctS2JSRS);
                        }
                        if (IsJTRSideOne != null && IsJTRSideOne.length == 3) {
                            if (IsJTRSideOne[2].equals("(R)")) {
                                outPutScreenTwo.setJtrForSideOne(true);
                            } else {
                                outPutScreenTwo.setJtrForSideOne(false);
                            }
                        } else {
                            outPutScreenTwo.setJtrForSideOne(false);
                        }
                        if (IsJTRSideTwo != null && IsJTRSideTwo.length == 3) {
                            if (IsJTRSideTwo[2].equals("(R)")) {
                                outPutScreenTwo.setJtrForSideTwo(true);
                            } else {
                                outPutScreenTwo.setJtrForSideTwo(false);
                            }
                        } else {
                            outPutScreenTwo.setJtrForSideTwo(false);
                        }
                        if (outPutScreenTwo.isJtrForSideOne()) {
                            tieRodLoadJTRSideOne =
                                    SmacnaUtils
                                            .getTieRodLoadForJTR(sides[0],
                                                                 (inputScreen
                                                                         .getJointSpacing() * 12 - 4) / 2F,
                                                                 noOfTieRodForS1,
                                                                 pc);
                            String jtrSizeForS1 = "";
                            if (inputScreen.isPosOrNeg()) {

                                jtrSizeForS1 =
                                            SmacnaUtils.getEMT(tieRodLoadJTRSideOne);

                            } else {
                                int[] dim =
                                        DuctSides.getSides(Integer
                                                .parseInt(inputScreen
                                                        .getHeight()), Integer
                                                .parseInt(inputScreen
                                                        .getWidth()));
                                try {

                                    jtrSizeForS1 =
                                                connectionService
                                                        .getEmtForNegPC(dim[1],
                                                                        tieRodLoadJTRSideOne);

                                } catch (SmacnaException e) {
                                    logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                       e.getMessage(),
                                               e);
                                }
                            }
                            outPutScreenTwo.setJtrSizeForS1(jtrSizeForS1);
                        }
                        if (outPutScreenTwo.isJtrForSideTwo()) {
                            tieRodLoadJTRSideTwo =
                                    SmacnaUtils
                                            .getTieRodLoadForJTR(sides[1],
                                                                 (inputScreen
                                                                         .getJointSpacing() * 12 - 4) ,
                                                                 noOfTieRodForS2,
                                                                 pc);
                            String jtrSizeForS2 = "";
                            if (inputScreen.isPosOrNeg()) {

                                jtrSizeForS2 =
                                            SmacnaUtils.getEMT(tieRodLoadJTRSideTwo);

                            } else {
                                int[] dim =
                                        DuctSides.getSides(Integer
                                                .parseInt(inputScreen
                                                        .getHeight()), Integer
                                                .parseInt(inputScreen
                                                        .getWidth()));
                                try {

                                    jtrSizeForS2 =
                                                connectionService
                                                        .getEmtForNegPC(dim[0],
                                                                        tieRodLoadJTRSideTwo);

                                } catch (SmacnaException e) {
                                    logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                       e.getMessage(),
                                               e);
                                }
                            }
                            outPutScreenTwo.setJtrSizeForS2(jtrSizeForS2);
                        }
                        outPutScreenTwo
                                .setTieRoadLoadJTRForSideOne(tieRodLoadJTRSideOne);
                        outPutScreenTwo
                                .setTieRoadLoadJTRForSideTwo(tieRodLoadJTRSideTwo);
                        outPutScreenTwo.setMinDuctGageSOne(fianlDuctGageThree);
                        outPutScreenTwo.setMinDuctGageSTwo(fianlDuctGageTwo);
                        outPutScreenTwo.setFinalDuctGage(fianlDuctGageII);

                        // Setting Reinforcement Class, Angle and Duct gage
                        // for
                        // S1, RS=JS/2 and S2, RS=JS

                        // Class:
                        outPutScreenTwo
                                .setReinClassForSideOne(reinClassS1JSRSBy2);
                        outPutScreenTwo.setReinClassForSideTwo(reinClassS2JSRS);
                        // Angle:
                        outPutScreenTwo
                                .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                        outPutScreenTwo.setExtRenfAngleSideTwo(reinAngleS2JSRS);
                        // Duct Gage:
                        outPutScreenTwo
                                .setDuctGageForSideOne(reinDuctS1JSRSBy2);
                        outPutScreenTwo.setDuctGageForSideTwo(reinDuctS2JSRS);

                        outPutScreenTwo
                                .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                                 .getPressureClass(),
                                                                         connectionService));
                        outPutScreenTwo.setNoTieRodForSideOne(0);
                        outPutScreenTwo.setNoTieRodForSideTwo(0);
                        outPutScreenTwo.setTieRodLoadForSideOne(0.0);
                        outPutScreenTwo.setTieRod("");
                        outPutScreenTwo
                                .setExtRenfSide(String.valueOf(sides[0]));
                        outPutScreens.add(outPutScreenTwo);

                    }
                }
                if (fianlDuctGageIV != 0) {
                    int noOfTieRodForS1 = 0;
                    int noOfTieRodForS2 = 0;
                    if (sideOneT || sideTwoT) {

                        if (sideOneT && sideTwoT) {

                            logg.info("[getOptions]: INTERNAL AND EXTERNAL ON SAME SIDE---ONE");
                            noOfTieRodForS1 = 0;
                            noOfTieRodForS2 = 0;
                            noOfTieRodForS1 =
                                    calculateSideT25abFive(sides[0],
                                                           inputScreen
                                                                   .getPressureClass(),
                                                           inputScreen
                                                                   .getJointSpacing() / 2F,
                                                           inputScreen
                                                                   .getTransConnS1(),
                                                           fianlDuctGageIV,
                                                           tieRodService);

                            noOfTieRodForS2 =
                                    calculateSideT25abFive(sides[1],
                                                           inputScreen
                                                                   .getPressureClass(),
                                                           inputScreen
                                                                   .getJointSpacing() / 2F,
                                                           inputScreen
                                                                   .getTransConnS2(),
                                                           fianlDuctGageIV,
                                                           tieRodService);
                            if (noOfTieRodForS1 == 0) {
                                noOfTieRodForS1 = 1;
                            }
                            if (noOfTieRodForS2 == 0) {
                                noOfTieRodForS2 = 1;
                            }

                            try {
                                pc =
                                        connectionService
                                                .getStaticPC(inputScreen
                                                        .getPressureClass());
                            } catch (SmacnaException e) {
                                logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                                        e.getMessage());
                            } catch (Exception e) {
                                logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                                                   e.getMessage(),
                                           e);
                            }
                            double tieRodLoadForS1 = 0.0;
                            if (noOfTieRodForS1 != 0) {
                                tieRodLoadForS1 =
                                        SmacnaUtils
                                                .getTieRodLoadWithT(sides[0],
                                                                     (inputScreen
                                                                             .getJointSpacing() * 12 - 4) / 2F,
                                                                     noOfTieRodForS1,
                                                                     pc);
                            }
                            tieRodLoadJTRSideOne = 0.0;
                            tieRodLoadJTRSideTwo = 0.0;

                            double tieRodLoadForS2 = 0.0;
                            if (noOfTieRodForS2 != 0) {
                                tieRodLoadForS2 =
                                        SmacnaUtils
                                                .getTieRodLoadWithT(sides[1],
                                                                     (inputScreen
                                                                             .getJointSpacing() * 12 - 4) / 2F,
                                                                     noOfTieRodForS2,
                                                                     pc);
                            }
                            logg.info("[getOptions]: ####-----####--TIE ROD LOAD S1---" +
                                            tieRodLoadForS1);
                            logg.info("[getOptions]: ####-----####--TIE ROD LOAD S2---" +
                                            tieRodLoadForS2);
                            logg.info("[getOptions]: ####-----####--TIE ROD LOAD S1 JTR---" +
                                            tieRodLoadJTRSideOne);
                            logg.info("[getOptions]: ####-----####--TIE ROD LOAD S2 JTR---" +
                                            tieRodLoadJTRSideTwo);
                            String EMTForS1 = "No MPT Required";
                            String EMTForS2 = "No MPT Required";
                            if (inputScreen.isPosOrNeg()) {
                                if (noOfTieRodForS1 != 0) {
                                    EMTForS1 =
                                            SmacnaUtils.getEMT(tieRodLoadForS1);
                                }
                                if (noOfTieRodForS2 != 0) {
                                    EMTForS2 =
                                            SmacnaUtils.getEMT(tieRodLoadForS2);
                                }
                            } else {
                                int[] dim =
                                        DuctSides.getSides(Integer
                                                .parseInt(inputScreen
                                                        .getHeight()), Integer
                                                .parseInt(inputScreen
                                                        .getWidth()));
                                try {
                                    if (noOfTieRodForS1 != 0) {
                                        EMTForS1 =
                                                connectionService
                                                        .getEmtForNegPC(dim[1],
                                                                        tieRodLoadForS1);
                                    }
                                    if (noOfTieRodForS2 != 0) {
                                        EMTForS2 =
                                                connectionService
                                                        .getEmtForNegPC(dim[0],
                                                                        tieRodLoadForS2);
                                    }
                                } catch (SmacnaException e) {
                                    logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                       e.getMessage(),
                                               e);
                                }
                            }

                            if (EMTForS1 == null || EMTForS1.equals("error")) {

                                logg.error("[getOptions]: ##########--E R R O R--##########");
                            } else {

                                outPutScreenFour.setSideOne(sides[0]);
                                outPutScreenFour.setSideTwo(sides[1]);
                                outPutScreenFour
                                        .setExternalReinforcement(false);
                                outPutScreenFour
                                        .setIntAndExternalOnBothSide(true);



                                        outPutScreenFour.setJtrForSideOne(true);

                                        outPutScreenFour.setJtrForSideTwo(true);

                                if (outPutScreenFour.isJtrForSideOne()) {
                                    tieRodLoadJTRSideOne =
                                            SmacnaUtils
                                                    .getTieRodLoadForJTR(sides[0],
                                                                         (inputScreen
                                                                                 .getJointSpacing() * 12 - 4) / 2F,
                                                                         noOfTieRodForS1,
                                                                         pc);
                                    String jtrSizeForS1 = "";
                                    if (inputScreen.isPosOrNeg()) {

                                        jtrSizeForS1 =
                                                    SmacnaUtils.getEMT(tieRodLoadJTRSideOne);

                                    } else {
                                        int[] dim =
                                                DuctSides.getSides(Integer
                                                        .parseInt(inputScreen
                                                                .getHeight()), Integer
                                                        .parseInt(inputScreen
                                                                .getWidth()));
                                        try {

                                            jtrSizeForS1 =
                                                        connectionService
                                                                .getEmtForNegPC(dim[1],
                                                                                tieRodLoadJTRSideOne);

                                        } catch (SmacnaException e) {
                                            logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                               e.getMessage(),
                                                       e);
                                        }
                                    }
                                    outPutScreenFour.setJtrSizeForS1(jtrSizeForS1);
                                }
                                if (outPutScreenFour.isJtrForSideTwo()) {
                                    tieRodLoadJTRSideTwo =
                                            SmacnaUtils
                                                    .getTieRodLoadForJTR(sides[1],
                                                                         (inputScreen
                                                                                 .getJointSpacing() * 12 - 4) / 2F,
                                                                         noOfTieRodForS1,
                                                                         pc);
                                    String jtrSizeForS2 = "";
                                    if (inputScreen.isPosOrNeg()) {

                                        jtrSizeForS2 =
                                                    SmacnaUtils.getEMT(tieRodLoadJTRSideTwo);

                                    } else {
                                        int[] dim =
                                                DuctSides.getSides(Integer
                                                        .parseInt(inputScreen
                                                                .getHeight()), Integer
                                                        .parseInt(inputScreen
                                                                .getWidth()));
                                        try {

                                            jtrSizeForS2 =
                                                        connectionService
                                                                .getEmtForNegPC(dim[0],
                                                                                tieRodLoadJTRSideTwo);

                                        } catch (SmacnaException e) {
                                            logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                               e.getMessage(),
                                                       e);
                                        }
                                    }
                                    outPutScreenFour.setJtrSizeForS2(jtrSizeForS2);
                                }
                                outPutScreenFour
                                        .setTieRoadLoadJTRForSideOne(tieRodLoadJTRSideOne);
                                outPutScreenFour
                                        .setTieRoadLoadJTRForSideTwo(tieRodLoadJTRSideTwo);
                                outPutScreenFour
                                        .setInternalReinforcement(false);

                                outPutScreenFour
                                        .setMinDuctGageSOne(fianlDuctGageThree);
                                outPutScreenFour
                                        .setMinDuctGageSTwo(fianlDuctGageFour);
                                outPutScreenFour
                                        .setFinalDuctGage(fianlDuctGageIV);

                                // Setting Reinforcement Class, Angle and
                                // Duct gage
                                // for
                                // S1, RS=JS/2 and S2, RS=JS/2

                                // Class:
                                outPutScreenFour
                                        .setReinClassForSideOne(reinClassS1JSRSBy2);
                                outPutScreenFour
                                        .setReinClassForSideTwo(reinClassS2JSRSBy2);
                                // Angle:
                                outPutScreenFour
                                        .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                                outPutScreenFour
                                        .setExtRenfAngleSideTwo(reinAngleS2JSRSBy2);
                                // Duct Gage:
                                outPutScreenFour
                                        .setDuctGageForSideOne(reinDuctS1JSRSBy2);
                                outPutScreenFour
                                        .setDuctGageForSideTwo(reinDuctS2JSRSBy2);

                                outPutScreenFour
                                        .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                                         .getPressureClass(),
                                                                                 connectionService));
                                outPutScreenFour
                                        .setNoTieRodForSideOne(noOfTieRodForS1);

                                outPutScreenFour
                                        .setNoTieRodForSideTwo(noOfTieRodForS2);
                                outPutScreenFour
                                        .setTieRodLoadForSideOne(tieRodLoadForS1);
                                outPutScreenFour
                                        .setTieRodLoadForSideTwo(tieRodLoadForS2);
                                outPutScreenFour.setTieRod("");
                                outPutScreenFour.setTieRodForSideOne(EMTForS1);
                                outPutScreenFour.setTieRodForSideTwo(EMTForS2);
                                outPutScreenFour.setExtRenfSide(String
                                        .valueOf(sides[0]));
                                outPutScreens.add(outPutScreenFour);

                            }

                        } else {

                            if (sideOneT) {

                                noOfTieRodForS1 = 0;
                                noOfTieRodForS2 = 0;
                                double tieRodLoadForS2 = 0;
                                noOfTieRodForS1 =
                                        calculateSideT25abFive(sides[0],
                                                               inputScreen
                                                                       .getPressureClass(),
                                                               inputScreen
                                                                       .getJointSpacing() / 2F,
                                                               inputScreen
                                                                       .getTransConnS1(),
                                                               fianlDuctGageIV,
                                                               tieRodService);

                                noOfTieRodForS2 =
                                        calculateSideT25abFive(sides[1],
                                                               inputScreen
                                                                       .getPressureClass(),
                                                               inputScreen
                                                                       .getJointSpacing() / 2F,
                                                               inputScreen
                                                                       .getTransConnS2(),
                                                               fianlDuctGageIV,
                                                               tieRodService);
                                if (noOfTieRodForS1 == 0) {
                                    noOfTieRodForS1 = 1;
                                }
//                                if (noOfTieRodForS2 == 0) {
//                                    noOfTieRodForS2 = 1;
//                                }

                                try {
                                    pc =
                                            connectionService
                                                    .getStaticPC(inputScreen
                                                            .getPressureClass());
                                } catch (SmacnaException e) {
                                    logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                                            e.getMessage());
                                } catch (Exception e) {
                                    logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                                                       e.getMessage(),
                                               e);
                                }
                                double tieRodLoadForS1 = 0.0;
                                if (noOfTieRodForS1 != 0) {
                                    tieRodLoadForS1 =
                                            SmacnaUtils
                                                    .getTieRodLoadWithT(sides[0],
                                                                         (inputScreen
                                                                                 .getJointSpacing() * 12 - 4) / 2F,
                                                                         noOfTieRodForS1,
                                                                         pc);
                                }
                                if (noOfTieRodForS2 != 0) {
                                	tieRodLoadForS2 =
                                            SmacnaUtils
                                                    .getTieRodLoad(sides[1],
                                                                         (inputScreen
                                                                                 .getJointSpacing() * 12 - 4) / 2F,
                                                                         noOfTieRodForS2,
                                                                         pc);
                                }
                                tieRodLoadJTRSideOne = 0.0;
                                tieRodLoadJTRSideTwo = 0.0;
                                String EMTForS1 = "No MPT Required";
                                String EMTForS2 = "No MPT Required";
                                if (inputScreen.isPosOrNeg()) {
                                    if (noOfTieRodForS1 != 0) {
                                        EMTForS1 =
                                                SmacnaUtils
                                                        .getEMT(tieRodLoadForS1);
                                    }
                                    if (noOfTieRodForS2 != 0) {
                                        EMTForS2 =
                                                SmacnaUtils.getEMT(tieRodLoadForS2);
                                    }
                                } else {
                                    int[] dim =
                                            DuctSides
                                                    .getSides(Integer.parseInt(inputScreen
                                                                      .getHeight()),
                                                              Integer.parseInt(inputScreen
                                                                      .getWidth()));
                                    try {
                                        if (noOfTieRodForS1 != 0) {
                                            EMTForS1 =
                                                    connectionService
                                                            .getEmtForNegPC(dim[1],
                                                                            tieRodLoadForS1);
                                        }
                                        if (noOfTieRodForS2 != 0) {
                                            EMTForS2 =
                                                    connectionService
                                                            .getEmtForNegPC(dim[0],
                                                                            tieRodLoadForS2);
                                        }

                                    } catch (SmacnaException e) {
                                        logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                           e.getMessage(),
                                                   e);
                                    }
                                }
///////////////////////////////////////////////////////////////////////////////////////////////////


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


                                if (EMTForS1 == null ||
                                        EMTForS1.equals("error")) {

                                    logg.error("[getOptions]: ##########--E R R O R--##########");
                                } else {

                                    outPutScreenFour.setSideOne(sides[0]);
                                    outPutScreenFour.setSideTwo(sides[1]);
                                    outPutScreenFour
                                            .setIntAndExtOnSideOne(true);
                                    outPutScreenFour
                                            .setExternalReinforcement(true);

                                    if (reinDuctS2JSRSBy2 != null) {
                                        IsJTRSideTwo =
                                                StringsUtil
                                                        .stripGarbageT25(reinDuctS2JSRSBy2);
                                    }

                                            outPutScreenFour
                                                    .setJtrForSideOne(true);

                                    if (IsJTRSideTwo != null &&
                                            IsJTRSideTwo.length == 3) {
                                        if (IsJTRSideTwo[2].equals("(R)")) {
                                            outPutScreenFour
                                                    .setJtrForSideTwo(true);
                                        } else {
                                            outPutScreenFour
                                                    .setJtrForSideTwo(false);
                                        }
                                    } else {
                                        outPutScreenFour
                                                .setJtrForSideTwo(false);
                                    }
                                    tieRodLoadJTRSideOne = 0.0;
                                    tieRodLoadJTRSideTwo = 0.0;
                                    if (outPutScreenFour.isJtrForSideOne()) {
                                        tieRodLoadJTRSideOne =
                                                SmacnaUtils
                                                        .getTieRodLoadForJTR(sides[0],
                                                                             (inputScreen
                                                                                     .getJointSpacing() * 12 - 4) / 2F,
                                                                             noOfTieRodForS1,
                                                                             pc);
                                        String jtrSizeForS1 = "";
                                        if (inputScreen.isPosOrNeg()) {

                                            jtrSizeForS1 =
                                                        SmacnaUtils.getEMT(tieRodLoadJTRSideOne);

                                        } else {
                                            int[] dim =
                                                    DuctSides.getSides(Integer
                                                            .parseInt(inputScreen
                                                                    .getHeight()), Integer
                                                            .parseInt(inputScreen
                                                                    .getWidth()));
                                            try {

                                                jtrSizeForS1 =
                                                            connectionService
                                                                    .getEmtForNegPC(dim[1],
                                                                                    tieRodLoadJTRSideOne);

                                            } catch (SmacnaException e) {
                                                logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                                   e.getMessage(),
                                                           e);
                                            }
                                        }
                                        outPutScreenFour.setJtrSizeForS1(jtrSizeForS1);
                                    }
                                    if (outPutScreenFour.isJtrForSideTwo()) {
                                        tieRodLoadJTRSideTwo =
                                                SmacnaUtils
                                                        .getTieRodLoadForJTR(sides[1],
                                                                             (inputScreen
                                                                                     .getJointSpacing() * 12 - 4) / 2F,
                                                                             noOfTieRodForS1,
                                                                             pc);
                                        String jtrSizeForS2 = "";
                                        if (inputScreen.isPosOrNeg()) {

                                            jtrSizeForS2 =
                                                        SmacnaUtils.getEMT(tieRodLoadJTRSideTwo);

                                        } else {
                                            int[] dim =
                                                    DuctSides.getSides(Integer
                                                            .parseInt(inputScreen
                                                                    .getHeight()), Integer
                                                            .parseInt(inputScreen
                                                                    .getWidth()));
                                            try {

                                                jtrSizeForS2 =
                                                            connectionService
                                                                    .getEmtForNegPC(dim[0],
                                                                                    tieRodLoadJTRSideTwo);

                                            } catch (SmacnaException e) {
                                                logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                                   e.getMessage(),
                                                           e);
                                            }
                                        }
                                        outPutScreenFour.setJtrSizeForS2(jtrSizeForS2);
                                    }
                                    outPutScreenFour
                                            .setTieRoadLoadJTRForSideOne(tieRodLoadJTRSideOne);
                                    outPutScreenFour
                                            .setTieRoadLoadJTRForSideTwo(tieRodLoadJTRSideTwo);
                                    outPutScreenFour
                                            .setInternalReinforcement(false);
                                    outPutScreenFour
                                            .setTieRodForSideOne(EMTForS1);
                                    outPutScreenFour
                                            .setMinDuctGageSOne(fianlDuctGageThree);
                                    outPutScreenFour
                                            .setMinDuctGageSTwo(fianlDuctGageFour);
                                    outPutScreenFour
                                            .setFinalDuctGage(fianlDuctGageIV);

                                    // Setting Reinforcement Class, Angle
                                    // and
                                    // Duct gage
                                    // for
                                    // S1, RS=JS/2 and S2, RS=JS/2

                                    // Class:
                                    outPutScreenFour
                                            .setReinClassForSideOne(reinClassS1JSRSBy2);
                                    outPutScreenFour
                                            .setReinClassForSideTwo(reinClassS2JSRSBy2);
                                    // Angle:
                                    outPutScreenFour
                                            .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                                    outPutScreenFour
                                            .setExtRenfAngleSideTwo(reinAngleS2JSRSBy2);
                                    // Duct Gage:
                                    outPutScreenFour
                                            .setDuctGageForSideOne(reinDuctS1JSRSBy2);
                                    outPutScreenFour
                                            .setDuctGageForSideTwo(reinDuctS2JSRSBy2);

                                    outPutScreenFour
                                            .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                                             .getPressureClass(),
                                                                                     connectionService));
                                    outPutScreenFour
                                            .setNoTieRodForSideOne(noOfTieRodForS1);

                                    outPutScreenFour
                                            .setNoTieRodForSideTwo(noOfTieRodForS2);
                                    outPutScreenFour
                                            .setTieRodLoadForSideOne(tieRodLoadForS1);
                                    outPutScreenFour.setTieRod("");
                                    outPutScreenFour.setExtRenfSide(String
                                            .valueOf(sides[1]));
                                    outPutScreens.add(outPutScreenFour);


                                    // Second Option
                                    outPutScreenFour = new OutPutScreen();
                                    outPutScreenFour.setSideOne(sides[0]);
                                    outPutScreenFour.setSideTwo(sides[1]);
                                    outPutScreenFour
                                            .setIntAndExtOnSideOne(true);
                                    outPutScreenFour
                                            .setInternalReinforcement(true);

                                    if (reinDuctS2JSRSBy2 != null) {
                                        IsJTRSideTwo =
                                                StringsUtil
                                                        .stripGarbageT25(reinDuctS2JSRSBy2);
                                    }

                                            outPutScreenFour
                                                    .setJtrForSideOne(true);

                                    if (IsJTRSideTwo != null &&
                                            IsJTRSideTwo.length == 3) {
                                        if (IsJTRSideTwo[2].equals("(R)")) {
                                            outPutScreenFour
                                                    .setJtrForSideTwo(true);
                                        } else {
                                            outPutScreenFour
                                                    .setJtrForSideTwo(false);
                                        }
                                    } else {
                                        outPutScreenFour
                                                .setJtrForSideTwo(false);
                                    }
                                    tieRodLoadJTRSideOne = 0.0;
                                    tieRodLoadJTRSideTwo = 0.0;
                                    if (outPutScreenFour.isJtrForSideOne()) {
                                        tieRodLoadJTRSideOne =
                                                SmacnaUtils
                                                        .getTieRodLoadForJTR(sides[0],
                                                                             (inputScreen
                                                                                     .getJointSpacing() * 12 - 4) / 2F,
                                                                             noOfTieRodForS1,
                                                                             pc);
                                        String jtrSizeForS1 = "";
                                        if (inputScreen.isPosOrNeg()) {

                                            jtrSizeForS1 =
                                                        SmacnaUtils.getEMT(tieRodLoadJTRSideOne);

                                        } else {
                                            int[] dim =
                                                    DuctSides.getSides(Integer
                                                            .parseInt(inputScreen
                                                                    .getHeight()), Integer
                                                            .parseInt(inputScreen
                                                                    .getWidth()));
                                            try {

                                                jtrSizeForS1 =
                                                            connectionService
                                                                    .getEmtForNegPC(dim[1],
                                                                                    tieRodLoadJTRSideOne);

                                            } catch (SmacnaException e) {
                                                logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                                   e.getMessage(),
                                                           e);
                                            }
                                        }
                                        outPutScreenFour.setJtrSizeForS1(jtrSizeForS1);
                                    }
                                    if (outPutScreenFour.isJtrForSideTwo()) {
                                        tieRodLoadJTRSideTwo =
                                                SmacnaUtils
                                                        .getTieRodLoadForJTR(sides[1],
                                                                             (inputScreen
                                                                                     .getJointSpacing() * 12 - 4) / 2F,
                                                                             noOfTieRodForS1,
                                                                             pc);
                                        String jtrSizeForS2 = "";
                                        if (inputScreen.isPosOrNeg()) {

                                            jtrSizeForS2 =
                                                        SmacnaUtils.getEMT(tieRodLoadJTRSideTwo);

                                        } else {
                                            int[] dim =
                                                    DuctSides.getSides(Integer
                                                            .parseInt(inputScreen
                                                                    .getHeight()), Integer
                                                            .parseInt(inputScreen
                                                                    .getWidth()));
                                            try {

                                                jtrSizeForS2 =
                                                            connectionService
                                                                    .getEmtForNegPC(dim[0],
                                                                                    tieRodLoadJTRSideTwo);

                                            } catch (SmacnaException e) {
                                                logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                                   e.getMessage(),
                                                           e);
                                            }
                                        }
                                        outPutScreenFour.setJtrSizeForS2(jtrSizeForS2);
                                    }
                                    outPutScreenFour
                                            .setTieRoadLoadJTRForSideOne(tieRodLoadJTRSideOne);
                                    outPutScreenFour
                                            .setTieRoadLoadJTRForSideTwo(tieRodLoadJTRSideTwo);
                                    outPutScreenFour
                                            .setInternalReinforcement(true);
                                    outPutScreenFour
                                            .setTieRodForSideOne(EMTForS1);
                                    outPutScreenFour
                                    	.setTieRodForSideTwo(EMTForS2);
                                    outPutScreenFour
                                            .setMinDuctGageSOne(fianlDuctGageThree);
                                    outPutScreenFour
                                            .setMinDuctGageSTwo(fianlDuctGageFour);
                                    outPutScreenFour
                                            .setFinalDuctGage(fianlDuctGageIV);

                                    // Setting Reinforcement Class, Angle
                                    // and
                                    // Duct gage
                                    // for
                                    // S1, RS=JS/2 and S2, RS=JS/2

                                    // Class:
                                    outPutScreenFour
                                            .setReinClassForSideOne(reinClassS1JSRSBy2);
                                    outPutScreenFour
                                            .setReinClassForSideTwo(reinClassS2JSRSBy2);
                                    // Angle:
                                    outPutScreenFour
                                            .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                                    outPutScreenFour
                                            .setExtRenfAngleSideTwo(reinAngleS2JSRSBy2);
                                    // Duct Gage:
                                    outPutScreenFour
                                            .setDuctGageForSideOne(reinDuctS1JSRSBy2);
                                    outPutScreenFour
                                            .setDuctGageForSideTwo(reinDuctS2JSRSBy2);

                                    outPutScreenFour
                                            .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                                             .getPressureClass(),
                                                                                     connectionService));
                                    outPutScreenFour
                                            .setNoTieRodForSideOne(noOfTieRodForS1);

                                    outPutScreenFour
                                            .setNoTieRodForSideTwo(noOfTieRodForS2);
                                    outPutScreenFour
                                            .setTieRodLoadForSideOne(tieRodLoadForS1);
                                    outPutScreenFour
                                    	.setTieRodLoadForSideTwo(tieRodLoadForS2);
                                    outPutScreenFour.setExtRenfSide(String
                                            .valueOf(sides[1]));
                                    outPutScreens.add(outPutScreenFour);
                                }

                            } else if (sideTwoT) {

                                noOfTieRodForS1 = 0;
                                noOfTieRodForS2 = 0;
                                noOfTieRodForS1 =
                                        calculateSideT25abFive(sides[0],
                                                               inputScreen
                                                                       .getPressureClass(),
                                                               inputScreen
                                                                       .getJointSpacing() / 2F,
                                                               inputScreen
                                                                       .getTransConnS1(),
                                                               fianlDuctGageIV,
                                                               tieRodService);

                                noOfTieRodForS2 =
                                        calculateSideT25abFive(sides[1],
                                                               inputScreen
                                                                       .getPressureClass(),
                                                               inputScreen
                                                                       .getJointSpacing() / 2F,
                                                               inputScreen
                                                                       .getTransConnS2(),
                                                               fianlDuctGageIV,
                                                               tieRodService);
                                if (noOfTieRodForS1 == 0) {
                                    noOfTieRodForS1 = 1;
                                }
                                if (noOfTieRodForS2 == 0) {
                                    noOfTieRodForS2 = 1;
                                }

                                try {
                                    pc =
                                            connectionService
                                                    .getStaticPC(inputScreen
                                                            .getPressureClass());
                                } catch (SmacnaException e) {
                                    logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                                            e.getMessage());
                                } catch (Exception e) {
                                    logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                                                       e.getMessage(),
                                               e);
                                }
                                double tieRodLoadForS2 = 0.0;
                                if (noOfTieRodForS2 != 0) {
                                    tieRodLoadForS2 =
                                            SmacnaUtils
                                                    .getTieRodLoadWithT(sides[1],
                                                                         (inputScreen
                                                                                 .getJointSpacing() * 12 - 4) / 2F,
                                                                         noOfTieRodForS2,
                                                                         pc);
                                }
                                tieRodLoadJTRSideOne = 0.0;
                                tieRodLoadJTRSideTwo = 0.0;

                                String EMTForS1 = "No MPT Required";
                                String EMTForS2 = "No MPT Required";
                                if (inputScreen.isPosOrNeg()) {
                                    if (noOfTieRodForS2 != 0) {
                                        EMTForS2 =
                                                SmacnaUtils
                                                        .getEMT(tieRodLoadForS2);
                                    }
                                } else {
                                    int[] dim =
                                            DuctSides
                                                    .getSides(Integer.parseInt(inputScreen
                                                                      .getHeight()),
                                                              Integer.parseInt(inputScreen
                                                                      .getWidth()));
                                    try {
                                        if (noOfTieRodForS2 != 0) {
                                            EMTForS2 =
                                                    connectionService
                                                            .getEmtForNegPC(dim[0],
                                                                            tieRodLoadForS2);
                                        }

                                    } catch (SmacnaException e) {
                                        logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                           e.getMessage(),
                                                   e);
                                    }
                                }

                                if (EMTForS2 == null ||
                                        EMTForS2.equals("error")) {

                                    logg.error("[getOptions]: ##########--E R R O R--##########");
                                } else {

                                    outPutScreenFour.setSideOne(sides[0]);
                                    outPutScreenFour.setSideTwo(sides[1]);
                                    outPutScreenFour
                                            .setExternalReinforcement(false);
                                    outPutScreenFour
                                            .setIntAndExtOnSideTwo(true);
                                    if (reinDuctS1JSRSBy2 != null) {
                                        IsJTRSideOne =
                                                StringsUtil
                                                        .stripGarbageT25(reinDuctS1JSRSBy2);
                                    }

                                    if (IsJTRSideOne != null &&
                                            IsJTRSideOne.length == 3) {
                                        if (IsJTRSideOne[2].equals("(R)")) {
                                            outPutScreenFour
                                                    .setJtrForSideOne(true);
                                        } else {
                                            outPutScreenFour
                                                    .setJtrForSideOne(false);
                                        }
                                    } else {
                                        outPutScreenFour
                                                .setJtrForSideOne(false);
                                    }

                                            outPutScreenFour
                                                    .setJtrForSideTwo(true);

                                    if (outPutScreenFour.isJtrForSideOne()) {
                                        tieRodLoadJTRSideOne =
                                                SmacnaUtils
                                                        .getTieRodLoadForJTR(sides[0],
                                                                             (inputScreen
                                                                                     .getJointSpacing() * 12 - 4) / 2F,
                                                                             noOfTieRodForS1,
                                                                             pc);
                                        String jtrSizeForS1 = "";
                                        if (inputScreen.isPosOrNeg()) {

                                            jtrSizeForS1 =
                                                        SmacnaUtils.getEMT(tieRodLoadJTRSideOne);

                                        } else {
                                            int[] dim =
                                                    DuctSides.getSides(Integer
                                                            .parseInt(inputScreen
                                                                    .getHeight()), Integer
                                                            .parseInt(inputScreen
                                                                    .getWidth()));
                                            try {

                                                jtrSizeForS1 =
                                                            connectionService
                                                                    .getEmtForNegPC(dim[1],
                                                                                    tieRodLoadJTRSideOne);

                                            } catch (SmacnaException e) {
                                                logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                                   e.getMessage(),
                                                           e);
                                            }
                                        }
                                        outPutScreenFour.setJtrSizeForS1(jtrSizeForS1);
                                    }
                                    if (outPutScreenFour.isJtrForSideTwo()) {
                                        tieRodLoadJTRSideTwo =
                                                SmacnaUtils
                                                        .getTieRodLoadForJTR(sides[1],
                                                                             (inputScreen
                                                                                     .getJointSpacing() * 12 - 4) / 2F,
                                                                             noOfTieRodForS2,
                                                                             pc);
                                        String jtrSizeForS2 = "";
                                        if (inputScreen.isPosOrNeg()) {

                                            jtrSizeForS2 =
                                                        SmacnaUtils.getEMT(tieRodLoadJTRSideTwo);

                                        } else {
                                            int[] dim =
                                                    DuctSides.getSides(Integer
                                                            .parseInt(inputScreen
                                                                    .getHeight()), Integer
                                                            .parseInt(inputScreen
                                                                    .getWidth()));
                                            try {

                                                jtrSizeForS2 =
                                                            connectionService
                                                                    .getEmtForNegPC(dim[0],
                                                                                    tieRodLoadJTRSideTwo);

                                            } catch (SmacnaException e) {
                                                logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                                   e.getMessage(),
                                                           e);
                                            }
                                        }
                                        outPutScreenFour.setJtrSizeForS2(jtrSizeForS2);
                                    }
                                    outPutScreenFour
                                            .setTieRoadLoadJTRForSideOne(tieRodLoadJTRSideOne);
                                    outPutScreenFour
                                            .setTieRoadLoadJTRForSideTwo(tieRodLoadJTRSideTwo);
                                    outPutScreenFour
                                            .setInternalReinforcement(false);
                                    outPutScreenFour
                                            .setTieRodForSideTwo(EMTForS2);
                                    outPutScreenFour
                                            .setMinDuctGageSOne(fianlDuctGageThree);
                                    outPutScreenFour
                                            .setMinDuctGageSTwo(fianlDuctGageFour);
                                    outPutScreenFour
                                            .setFinalDuctGage(fianlDuctGageIV);

                                    // Setting Reinforcement Class, Angle
                                    // and Duct gage
                                    // for
                                    // S1, RS=JS/2 and S2, RS=JS/2

                                    // Class:
                                    outPutScreenFour
                                            .setReinClassForSideOne(reinClassS1JSRSBy2);
                                    outPutScreenFour
                                            .setReinClassForSideTwo(reinClassS2JSRSBy2);
                                    // Angle:
                                    outPutScreenFour
                                            .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                                    outPutScreenFour
                                            .setExtRenfAngleSideTwo(reinAngleS2JSRSBy2);
                                    // Duct Gage:
                                    outPutScreenFour
                                            .setDuctGageForSideOne(reinDuctS1JSRSBy2);
                                    outPutScreenFour
                                            .setDuctGageForSideTwo(reinDuctS2JSRSBy2);

                                    outPutScreenFour
                                            .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                                             .getPressureClass(),
                                                                                     connectionService));
                                    outPutScreenFour.setNoTieRodForSideOne(0);

                                    outPutScreenFour
                                            .setNoTieRodForSideTwo(noOfTieRodForS2);

                                    outPutScreenFour
                                            .setTieRodLoadForSideOne(0.0);
                                    outPutScreenFour
                                            .setTieRodLoadForSideTwo(tieRodLoadForS2);
                                    outPutScreenFour
                                            .setTieRodForSideTwo(EMTForS2);
                                    outPutScreenFour.setTieRod("");
                                    outPutScreenFour.setExtRenfSide(String
                                            .valueOf(sides[0]));
                                    outPutScreens.add(outPutScreenFour);
                                }

                            }
                        }

                    } else {

                    	/**
                    	 *
                    	 * External on Side 1 and Side 2
                    	 *
                    	 **/
                        outPutScreenFour.setSideOne(sides[0]);
                        outPutScreenFour.setSideTwo(sides[1]);
                        outPutScreenFour.setExternalBothSide(true);
                        outPutScreenFour.setInternalBothSide(false);
                        outPutScreenFour.setExternalReinforcement(false);
                        outPutScreenFour.setInternalReinforcement(false);

                        if (reinDuctS1JSRSBy2 != null) {
                            IsJTRSideOne =
                                    StringsUtil
                                            .stripGarbageT25(reinDuctS1JSRSBy2);
                        }
                        if (reinDuctS2JSRSBy2 != null) {
                            IsJTRSideTwo =
                                    StringsUtil
                                            .stripGarbageT25(reinDuctS2JSRSBy2);
                        }
                        if (IsJTRSideOne != null && IsJTRSideOne.length == 3) {
                            if (IsJTRSideOne[2].equals("(R)")) {
                                outPutScreenFour.setJtrForSideOne(true);
                            } else {
                                outPutScreenFour.setJtrForSideOne(false);
                            }
                        } else {
                            outPutScreenFour.setJtrForSideOne(false);
                        }
                        if (IsJTRSideTwo != null && IsJTRSideTwo.length == 3) {
                            if (IsJTRSideTwo[2].equals("(R)")) {
                                outPutScreenFour.setJtrForSideTwo(true);
                            } else {
                                outPutScreenFour.setJtrForSideTwo(false);
                            }
                        } else {
                            outPutScreenFour.setJtrForSideTwo(false);
                        }
                        tieRodLoadJTRSideOne = 0.0;
                        tieRodLoadJTRSideTwo = 0.0;
                        if (outPutScreenFour.isJtrForSideOne()) {
                            tieRodLoadJTRSideOne =
                                    SmacnaUtils
                                            .getTieRodLoadForJTR(sides[0],
                                                                 (inputScreen
                                                                         .getJointSpacing() * 12 - 4) / 2F,
                                                                 noOfTieRodForS1,
                                                                 pc);
                            String jtrSizeForS1 = "";
                            if (inputScreen.isPosOrNeg()) {

                                jtrSizeForS1 =
                                            SmacnaUtils.getEMT(tieRodLoadJTRSideOne);

                            } else {
                                int[] dim =
                                        DuctSides.getSides(Integer
                                                .parseInt(inputScreen
                                                        .getHeight()), Integer
                                                .parseInt(inputScreen
                                                        .getWidth()));
                                try {

                                    jtrSizeForS1 =
                                                connectionService
                                                        .getEmtForNegPC(dim[1],
                                                                        tieRodLoadJTRSideOne);

                                } catch (SmacnaException e) {
                                    logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                       e.getMessage(),
                                               e);
                                }
                            }
                            outPutScreenFour.setJtrSizeForS1(jtrSizeForS1);
                        }
                        if (outPutScreenFour.isJtrForSideTwo()) {
                            tieRodLoadJTRSideTwo =
                                    SmacnaUtils
                                            .getTieRodLoadForJTR(sides[1],
                                                                 (inputScreen
                                                                         .getJointSpacing() * 12 - 4) / 2F,
                                                                 noOfTieRodForS2,
                                                                 pc);
                            String jtrSizeForS2 = "";
                            if (inputScreen.isPosOrNeg()) {

                                jtrSizeForS2 =
                                            SmacnaUtils.getEMT(tieRodLoadJTRSideTwo);

                            } else {
                                int[] dim =
                                        DuctSides.getSides(Integer
                                                .parseInt(inputScreen
                                                        .getHeight()), Integer
                                                .parseInt(inputScreen
                                                        .getWidth()));
                                try {

                                    jtrSizeForS2 =
                                                connectionService
                                                        .getEmtForNegPC(dim[0],
                                                                        tieRodLoadJTRSideTwo);

                                } catch (SmacnaException e) {
                                    logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                       e.getMessage(),
                                               e);
                                }
                            }
                            outPutScreenFour.setJtrSizeForS2(jtrSizeForS2);
                        }

                        outPutScreenFour
                                .setTieRoadLoadJTRForSideOne(tieRodLoadJTRSideOne);
                        outPutScreenFour
                                .setTieRoadLoadJTRForSideTwo(tieRodLoadJTRSideTwo);
                        outPutScreenFour.setMinDuctGageSOne(fianlDuctGageThree);
                        outPutScreenFour.setMinDuctGageSTwo(fianlDuctGageFour);
                        outPutScreenFour.setFinalDuctGage(fianlDuctGageIV);

                        // Setting Reinforcement Class, Angle and Duct gage
                        // for
                        // S1, RS=JS/2 and S2, RS=JS/2

                        // Class:
                        outPutScreenFour
                                .setReinClassForSideOne(reinClassS1JSRSBy2);
                        outPutScreenFour
                                .setReinClassForSideTwo(reinClassS2JSRSBy2);
                        // Angle:
                        outPutScreenFour
                                .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                        outPutScreenFour
                                .setExtRenfAngleSideTwo(reinAngleS2JSRSBy2);
                        // Duct Gage:
                        outPutScreenFour
                                .setDuctGageForSideOne(reinDuctS1JSRSBy2);
                        outPutScreenFour
                                .setDuctGageForSideTwo(reinDuctS2JSRSBy2);

                        outPutScreenFour
                                .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                                 .getPressureClass(),
                                                                         connectionService));
                        outPutScreenFour.setNoTieRodForSideOne(0);
                        outPutScreenFour.setNoTieRodForSideTwo(0);
                        outPutScreenFour.setTieRodLoadForSideOne(0.0);
                        outPutScreenFour.setTieRod("");
                        outPutScreenFour.setExtRenfSide(String
                                .valueOf(sides[0]));
                        outPutScreens.add(outPutScreenFour);


                        // Internal option S1, RS = JS/2 and S2, RS=JS (TieRod)
                        if (fianlDuctGageII != 0) {
                            int noOfTieRod =
                                    calculateSideT25abFive(sides[0],
                                                           inputScreen.getPressureClass(),
                                                           inputScreen.getJointSpacing() / 2F,
                                                           inputScreen.getTransConnS1(),
                                                           fianlDuctGageII,
                                                           tieRodService);

                            pc = 0;
                            if (noOfTieRod != 0) {
                                try {
                                    pc =
                                            connectionService.getStaticPC(inputScreen
                                                    .getPressureClass());
                                } catch (SmacnaException e) {
                                    // TODO Auto-generated catch block
                                }

                                double tieRodLoad =
                                        SmacnaUtils
                                                .getTieRodLoad(sides[0],
                                                               (inputScreen
                                                                       .getJointSpacing() * 12 - 4) / 2F,
                                                               noOfTieRod,
                                                               pc);

                                String EMT = null;
                                if (inputScreen.isPosOrNeg()) {
                                    EMT = SmacnaUtils.getEMT(tieRodLoad);
                                } else {
                                    int[] dim =
                                            DuctSides.getSides(Integer.parseInt(inputScreen
                                                    .getHeight()), Integer
                                                    .parseInt(inputScreen.getWidth()));
                                    try {
                                        EMT =
                                                connectionService
                                                        .getEmtForNegPC(dim[1], tieRodLoad);
                                    } catch (SmacnaException e) {
                                        logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                           e.getMessage(),
                                                   e);
                                    }
                                }
                                if (EMT.equals("error")) {

                                    // To-Do code
                                } else {

                                    outPutScreenThree.setSideOne(sides[0]);
                                    outPutScreenThree.setSideTwo(sides[1]);

                                    outPutScreenThree.setExternalReinforcement(false);
                                    outPutScreenThree.setInternalReinforcement(true);
                                    IsJTRSideOne = null;
                                    IsJTRSideTwo = null;
                                    if (reinDuctS1JSRSBy2 != null) {
                                        IsJTRSideOne =
                                                StringsUtil
                                                        .stripGarbageT25(reinDuctS1JSRSBy2);
                                    }
                                    if (reinDuctS2JSRS != null) {
                                        IsJTRSideTwo =
                                                StringsUtil.stripGarbageT25(reinDuctS2JSRS);
                                    }
                                    if (IsJTRSideOne != null && IsJTRSideOne.length == 3) {
                                        if (IsJTRSideOne[2].equals("(R)")) {
                                            outPutScreenThree.setJtrForSideOne(true);
                                        } else {
                                            outPutScreenThree.setJtrForSideOne(false);
                                        }
                                    } else {
                                        outPutScreenThree.setJtrForSideOne(false);
                                    }
                                    if (IsJTRSideTwo != null && IsJTRSideTwo.length == 3) {
                                        if (IsJTRSideTwo[2].equals("(R)")) {
                                            outPutScreenThree.setJtrForSideTwo(true);
                                        } else {
                                            outPutScreenThree.setJtrForSideTwo(false);
                                        }
                                    } else {
                                        outPutScreenThree.setJtrForSideTwo(false);
                                    }
                                     tieRodLoadJTRSideOne = 0.0;
                                     tieRodLoadJTRSideTwo = 0.0;
                                    if (outPutScreenThree.isJtrForSideOne()) {
                                        tieRodLoadJTRSideOne =
                                                SmacnaUtils
                                                        .getTieRodLoadForJTR(sides[0],
                                                                             (inputScreen
                                                                                     .getJointSpacing() * 12 - 4) / 2F,
                                                                             1,
                                                                             pc);
                                        String jtrSizeForS1 = "";
                                        if (inputScreen.isPosOrNeg()) {

                                            jtrSizeForS1 =
                                                        SmacnaUtils.getEMT(tieRodLoadJTRSideOne);

                                        } else {
                                            int[] dim =
                                                    DuctSides.getSides(Integer
                                                            .parseInt(inputScreen
                                                                    .getHeight()), Integer
                                                            .parseInt(inputScreen
                                                                    .getWidth()));
                                            try {

                                                jtrSizeForS1 =
                                                            connectionService
                                                                    .getEmtForNegPC(dim[1],
                                                                                    tieRodLoadJTRSideOne);

                                            } catch (SmacnaException e) {
                                                logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                                   e.getMessage(),
                                                           e);
                                            }
                                        }
                                        outPutScreenThree.setJtrSizeForS1(jtrSizeForS1);
                                    }
                                    if (outPutScreenThree.isJtrForSideTwo()) {
                                        tieRodLoadJTRSideTwo =
                                                SmacnaUtils
                                                        .getTieRodLoadForJTR(sides[1],
                                                                             (inputScreen
                                                                                     .getJointSpacing() * 12 - 4) ,
                                                                             1,
                                                                             pc);
                                        String jtrSizeForS2 = "";
                                        if (inputScreen.isPosOrNeg()) {

                                            jtrSizeForS2 =
                                                        SmacnaUtils.getEMT(tieRodLoadJTRSideTwo);

                                        } else {
                                            int[] dim =
                                                    DuctSides.getSides(Integer
                                                            .parseInt(inputScreen
                                                                    .getHeight()), Integer
                                                            .parseInt(inputScreen
                                                                    .getWidth()));
                                            try {

                                                jtrSizeForS2 =
                                                            connectionService
                                                                    .getEmtForNegPC(dim[0],
                                                                                    tieRodLoadJTRSideTwo);

                                            } catch (SmacnaException e) {
                                                logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                                   e.getMessage(),
                                                           e);
                                            }
                                        }
                                        outPutScreenThree.setJtrSizeForS2(jtrSizeForS2);
                                    }

                                    outPutScreenThree
                                            .setTieRoadLoadJTRForSideOne(tieRodLoadJTRSideOne);
                                    outPutScreenThree
                                            .setTieRoadLoadJTRForSideTwo(tieRodLoadJTRSideTwo);

                                    outPutScreenThree.setExternalRenfAngle("");
                                    outPutScreenThree.setMinDuctGageSOne(fianlDuctGageThree);
                                    outPutScreenThree.setMinDuctGageSTwo(fianlDuctGageTwo);
                                    outPutScreenThree.setFinalDuctGage(fianlDuctGageII);

                                    // Setting Reinforcement Class, Angle and Duct gage
                                    // for
                                    // S1, RS=JS/2 and S2, RS=JS

                                    // Class:
                                    outPutScreenThree
                                            .setReinClassForSideOne(reinClassS1JSRSBy2);
                                    outPutScreenThree
                                            .setReinClassForSideTwo(reinClassS2JSRS);
                                    // Angle:
                                    outPutScreenThree
                                            .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                                    outPutScreenThree
                                            .setExtRenfAngleSideTwo(reinAngleS2JSRS);
                                    // Duct Gage:
                                    outPutScreenThree
                                            .setDuctGageForSideOne(reinDuctS1JSRSBy2);
                                    outPutScreenThree.setDuctGageForSideTwo(reinDuctS2JSRS);

                                    outPutScreenThree
                                            .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                                             .getPressureClass(),
                                                                                     connectionService));
                                    outPutScreenThree.setNoTieRodForSideOne(noOfTieRod);
                                    outPutScreenThree.setNoTieRodForSideTwo(0);
                                    if (noOfTieRod != 0) {
                                        outPutScreenThree
                                                .setTieRodLoadForSideOne(tieRodLoad);
                                    }
                                    outPutScreenThree.setTieRod(EMT);
                                    outPutScreenThree.setExtRenfSide(String
                                            .valueOf(sides[0]));
                                    outPutScreens.add(outPutScreenThree);
                                }
                            }
                        }



                        int noOfTieRod =
                                calculateSideT25abFive(sides[0],
                                                       inputScreen.getPressureClass(),
                                                       inputScreen.getJointSpacing() / 2F,
                                                       inputScreen.getTransConnS1(),
                                                       fianlDuctGageIV,
                                                       tieRodService);
                        int noOfTieRodSideTwo =
                                calculateSideT25abFive(sides[1],
                                                       inputScreen.getPressureClass(),
                                                       inputScreen.getJointSpacing() / 2F,
                                                       inputScreen.getTransConnS2(),
                                                       fianlDuctGageIV,
                                                       tieRodService);
                         pc = 0;
                        if (noOfTieRod != 0 && noOfTieRodSideTwo != 0) {
                            try {
                                pc =
                                        connectionService.getStaticPC(inputScreen
                                                .getPressureClass());
                            } catch (SmacnaException e) {
                                // TODO Auto-generated catch block
                            }

                            double tieRodLoad =
                                    SmacnaUtils
                                            .getTieRodLoad(sides[0],
                                                           (inputScreen
                                                                   .getJointSpacing() * 12 - 4) / 2F,
                                                           noOfTieRod,
                                                           pc);
                            ////////////////////////////////////////////////////////////////////////////
                            double tieRodLoadSide2 =
                                    SmacnaUtils
                                            .getTieRodLoad(sides[1],
                                                           (inputScreen
                                                                   .getJointSpacing() * 12 - 4) / 2F,
                                                                   noOfTieRodSideTwo,
                                                           pc);



                            ////////////////////////////////////////////////////////////////////////////
                            String EMT = null;
                            String sideTwoEMT = null;
                            if (inputScreen.isPosOrNeg()) {
                                EMT = SmacnaUtils.getEMT(tieRodLoad);
                                sideTwoEMT = SmacnaUtils.getEMT(tieRodLoadSide2);
                            } else {
                                int[] dim =
                                        DuctSides.getSides(Integer.parseInt(inputScreen
                                                .getHeight()), Integer
                                                .parseInt(inputScreen.getWidth()));
                                try {
                                    EMT =
                                            connectionService
                                                    .getEmtForNegPC(dim[1], tieRodLoad);
                                    sideTwoEMT =
                                            connectionService
                                                    .getEmtForNegPC(dim[0], tieRodLoadSide2);
                                } catch (SmacnaException e) {
                                    logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                       e.getMessage(),
                                               e);
                                }
                            }
                            if (EMT.equals("error")) {

                                // To-Do code
                            } else {
                            	/**
                           	 *
                           	 * Internal on Side 1 and Side 2
                           	 *
                           	 **/
                                outPutScreenFive.setSideOne(sides[0]);
                                outPutScreenFive.setSideTwo(sides[1]);
                                outPutScreenFive.setExternalBothSide(false);
                                outPutScreenFive.setInternalBothSide(true);
                                outPutScreenFive.setExternalReinforcement(false);
                                outPutScreenFive.setInternalReinforcement(false);

                                if (reinDuctS1JSRSBy2 != null) {
                                    IsJTRSideOne =
                                            StringsUtil
                                                    .stripGarbageT25(reinDuctS1JSRSBy2);
                                }
                                if (reinDuctS2JSRSBy2 != null) {
                                    IsJTRSideTwo =
                                            StringsUtil.stripGarbageT25(reinDuctS2JSRSBy2);
                                }
                                if (IsJTRSideOne != null && IsJTRSideOne.length == 3) {
                                    if (IsJTRSideOne[2].equals("(R)")) {
                                        outPutScreenFive.setJtrForSideOne(true);
                                    } else {
                                        outPutScreenFive.setJtrForSideOne(false);
                                    }
                                } else {
                                    outPutScreenFive.setJtrForSideOne(false);
                                }
                                if (IsJTRSideTwo != null && IsJTRSideTwo.length == 3) {
                                    if (IsJTRSideTwo[2].equals("(R)")) {
                                        outPutScreenFive.setJtrForSideTwo(true);
                                    } else {
                                        outPutScreenFive.setJtrForSideTwo(false);
                                    }
                                } else {
                                    outPutScreenFive.setJtrForSideTwo(false);
                                }
                                tieRodLoadJTRSideOne = 0.0;
                                tieRodLoadJTRSideTwo = 0.0;
                                if (outPutScreenFive.isJtrForSideOne()) {
                                    tieRodLoadJTRSideOne =
                                            SmacnaUtils
                                                    .getTieRodLoadForJTR(sides[0],
                                                                         (inputScreen
                                                                                 .getJointSpacing() * 12 - 4) / 2F,
                                                                         1,
                                                                         pc);
                                    String jtrSizeForS1 = "";
                                    if (inputScreen.isPosOrNeg()) {

                                        jtrSizeForS1 =
                                                    SmacnaUtils.getEMT(tieRodLoadJTRSideOne);

                                    } else {
                                        int[] dim =
                                                DuctSides.getSides(Integer
                                                        .parseInt(inputScreen
                                                                .getHeight()), Integer
                                                        .parseInt(inputScreen
                                                                .getWidth()));
                                        try {

                                            jtrSizeForS1 =
                                                        connectionService
                                                                .getEmtForNegPC(dim[1],
                                                                                tieRodLoadJTRSideOne);

                                        } catch (SmacnaException e) {
                                            logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                               e.getMessage(),
                                                       e);
                                        }
                                    }
                                    outPutScreenFive.setJtrSizeForS1(jtrSizeForS1);
                                }
                                if (outPutScreenFive.isJtrForSideTwo()) {
                                    tieRodLoadJTRSideTwo =
                                            SmacnaUtils
                                                    .getTieRodLoadForJTR(sides[1],
                                                                         (inputScreen
                                                                                 .getJointSpacing() * 12 - 4) / 2F,
                                                                         1,
                                                                         pc);
                                    String jtrSizeForS2 = "";
                                    if (inputScreen.isPosOrNeg()) {

                                        jtrSizeForS2 =
                                                    SmacnaUtils.getEMT(tieRodLoadJTRSideTwo);

                                    } else {
                                        int[] dim =
                                                DuctSides.getSides(Integer
                                                        .parseInt(inputScreen
                                                                .getHeight()), Integer
                                                        .parseInt(inputScreen
                                                                .getWidth()));
                                        try {

                                            jtrSizeForS2 =
                                                        connectionService
                                                                .getEmtForNegPC(dim[0],
                                                                                tieRodLoadJTRSideTwo);

                                        } catch (SmacnaException e) {
                                            logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                               e.getMessage(),
                                                       e);
                                        }
                                    }
                                    outPutScreenFive.setJtrSizeForS2(jtrSizeForS2);
                                }

                                outPutScreenFive
                                        .setTieRoadLoadJTRForSideOne(tieRodLoadJTRSideOne);
                                outPutScreenFive
                                        .setTieRoadLoadJTRForSideTwo(tieRodLoadJTRSideTwo);

                                outPutScreenFive.setExternalRenfAngle("");
                                outPutScreenFive.setMinDuctGageSOne(fianlDuctGageThree);
                                outPutScreenFive.setMinDuctGageSTwo(fianlDuctGageFour);
                                outPutScreenFive.setFinalDuctGage(fianlDuctGageIV);

                                // Setting Reinforcement Class, Angle and Duct gage
                                // for
                                // S1, RS=JS/2 and S2, RS=JS

                                // Class:
                                outPutScreenFive
                                        .setReinClassForSideOne(reinClassS1JSRSBy2);
                                outPutScreenFive
                                        .setReinClassForSideTwo(reinClassS2JSRSBy2);
                                // Angle:
                                outPutScreenFive
                                        .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                                outPutScreenFive
                                        .setExtRenfAngleSideTwo(reinAngleS2JSRSBy2);
                                // Duct Gage:
                                outPutScreenFive
                                        .setDuctGageForSideOne(reinDuctS1JSRSBy2);
                                outPutScreenFive.setDuctGageForSideTwo(reinDuctS2JSRSBy2);

                                outPutScreenFive
                                        .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                                         .getPressureClass(),
                                                                                 connectionService));
                                outPutScreenFive.setNoTieRodForSideOne(noOfTieRod);
                                outPutScreenFive.setNoTieRodForSideTwo(noOfTieRodSideTwo);
                                if (noOfTieRod != 0) {
                                    outPutScreenFive
                                            .setTieRodLoadForSideOne(tieRodLoad);
                                    outPutScreenFive.setTieRodLoadForSideTwo(tieRodLoadSide2);
                                }
                                outPutScreenFive.setTieRodForSideOne(EMT);
                                outPutScreenFive.setTieRodForSideTwo(sideTwoEMT);
                                outPutScreenFive.setExtRenfSide(String
                                        .valueOf(sides[0]));
                                outPutScreens.add(outPutScreenFive);
                            }
                        }













                        	//Output Screen Six
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        /**
                   	 *
                   	 * External on Side 1 and Internal Side 2
                   	 *
                   	 **/

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        outPutScreenSix.setSideOne(sides[0]);
                        outPutScreenSix.setSideTwo(sides[1]);
                        outPutScreenSix.setExternalReinforcement(false);
                        outPutScreenSix.setInternalReinforcement(false);
                        outPutScreenSix.setExtAndInternalEachSide(true);


                        outPutScreenSix.setMinDuctGageSOne(fianlDuctGageThree);
                        outPutScreenSix.setMinDuctGageSTwo(fianlDuctGageTwo);
                        outPutScreenSix.setFinalDuctGage(fianlDuctGageIV);

                        // Setting Reinforcement Class, Angle and Duct gage
                        // for
                        // S1, RS=JS/2 and S2, RS=JS

                        // Class:
                        outPutScreenSix
                                .setReinClassForSideOne(reinClassS1JSRSBy2);
                        outPutScreenSix.setReinClassForSideTwo(reinClassS2JSRSBy2);
                        // Angle:
                        outPutScreenSix
                                .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                        outPutScreenSix.setExtRenfAngleSideTwo(reinAngleS2JSRSBy2);
                        // Duct Gage:
                        outPutScreenSix
                                .setDuctGageForSideOne(reinDuctS1JSRSBy2);
                        outPutScreenSix.setDuctGageForSideTwo(reinClassS2JSRSBy2);

                        outPutScreenSix
                                .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                                 .getPressureClass(),
                                                                         connectionService));
                        outPutScreenSix
                                .setExtRenfSide(String.valueOf(sides[0]));

                        /*noOfTieRod =
                                calculateSideT25abFive(sides[0],
                                                       inputScreen.getPressureClass(),
                                                       inputScreen.getJointSpacing() / 2F,
                                                       inputScreen.getTransConnS1(),
                                                       fianlDuctGageIV,
                                                       tieRodService);*/
                        noOfTieRodSideTwo =
                                calculateSideT25abFive(sides[1],
                                                       inputScreen.getPressureClass(),
                                                       inputScreen.getJointSpacing() / 2F,
                                                       inputScreen.getTransConnS2(),
                                                       fianlDuctGageIV,
                                                       tieRodService);
                         pc = 0;
                        if (noOfTieRodSideTwo != 0) {
                            try {
                                pc =
                                        connectionService.getStaticPC(inputScreen
                                                .getPressureClass());
                            } catch (SmacnaException e) {
                                // TODO Auto-generated catch block
                            }

                           /* double tieRodLoad =
                                    SmacnaUtils
                                            .getTieRodLoad(sides[0],
                                                           (inputScreen
                                                                   .getJointSpacing() * 12 - 4) / 2F,
                                                           noOfTieRod,
                                                           pc);*/
                            ////////////////////////////////////////////////////////////////////////////
                            double tieRodLoadSide2 =
                                    SmacnaUtils
                                            .getTieRodLoad(sides[1],
                                                           (inputScreen
                                                                   .getJointSpacing() * 12 - 4) / 2F,
                                                                   noOfTieRodSideTwo,
                                                           pc);


                            if (reinDuctS1JSRSBy2 != null) {
                                IsJTRSideOne =
                                        StringsUtil
                                                .stripGarbageT25(reinDuctS1JSRSBy2);
                            }
                            if (reinDuctS2JSRSBy2 != null) {
                                IsJTRSideTwo =
                                        StringsUtil.stripGarbageT25(reinDuctS2JSRSBy2);
                            }
                            if (IsJTRSideOne != null && IsJTRSideOne.length == 3) {
                                if (IsJTRSideOne[2].equals("(R)")) {
                                    outPutScreenSix.setJtrForSideOne(true);
                                } else {
                                    outPutScreenSix.setJtrForSideOne(false);
                                }
                            } else {
                                outPutScreenSix.setJtrForSideOne(false);
                            }
                            if (IsJTRSideTwo != null && IsJTRSideTwo.length == 3) {
                                if (IsJTRSideTwo[2].equals("(R)")) {
                                    outPutScreenSix.setJtrForSideTwo(true);
                                } else {
                                    outPutScreenSix.setJtrForSideTwo(false);
                                }
                            } else {
                                outPutScreenSix.setJtrForSideTwo(false);
                            }
                            if (outPutScreenSix.isJtrForSideOne()) {
                                tieRodLoadJTRSideOne =
                                        SmacnaUtils
                                                .getTieRodLoadForJTR(sides[0],
                                                                     (inputScreen
                                                                             .getJointSpacing() * 12 - 4) / 2F,
                                                                     noOfTieRod,
                                                                     pc);
                                String jtrSizeForS1 = "";
                                if (inputScreen.isPosOrNeg()) {

                                    jtrSizeForS1 =
                                                SmacnaUtils.getEMT(tieRodLoadJTRSideOne);

                                } else {
                                    int[] dim =
                                            DuctSides.getSides(Integer
                                                    .parseInt(inputScreen
                                                            .getHeight()), Integer
                                                    .parseInt(inputScreen
                                                            .getWidth()));
                                    try {

                                        jtrSizeForS1 =
                                                    connectionService
                                                            .getEmtForNegPC(dim[1],
                                                                            tieRodLoadJTRSideOne);

                                    } catch (SmacnaException e) {
                                        logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                           e.getMessage(),
                                                   e);
                                    }
                                }
                                outPutScreenSix.setJtrSizeForS1(jtrSizeForS1);
                            }
                            if (outPutScreenSix.isJtrForSideTwo()) {
                                tieRodLoadJTRSideTwo =
                                        SmacnaUtils
                                                .getTieRodLoadForJTR(sides[1],
                                                                     (inputScreen
                                                                             .getJointSpacing() * 12 - 4) / 2F ,
                                                                     noOfTieRodForS2,
                                                                     pc);
                                String jtrSizeForS2 = "";
                                if (inputScreen.isPosOrNeg()) {

                                    jtrSizeForS2 =
                                                SmacnaUtils.getEMT(tieRodLoadJTRSideTwo);

                                } else {
                                    int[] dim =
                                            DuctSides.getSides(Integer
                                                    .parseInt(inputScreen
                                                            .getHeight()), Integer
                                                    .parseInt(inputScreen
                                                            .getWidth()));
                                    try {

                                        jtrSizeForS2 =
                                                    connectionService
                                                            .getEmtForNegPC(dim[0],
                                                                            tieRodLoadJTRSideTwo);

                                    } catch (SmacnaException e) {
                                        logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                           e.getMessage(),
                                                   e);
                                    }
                                }
                                outPutScreenSix.setJtrSizeForS2(jtrSizeForS2);
                            }
                            outPutScreenSix
                                    .setTieRoadLoadJTRForSideOne(tieRodLoadJTRSideOne);
                            outPutScreenSix
                                    .setTieRoadLoadJTRForSideTwo(tieRodLoadJTRSideTwo);

                            ////////////////////////////////////////////////////////////////////////////
                            String EMT = null;
                            String sideTwoEMT = null;
                            if (inputScreen.isPosOrNeg()) {
//                                EMT = SmacnaUtils.getEMT(tieRodLoad);
                                sideTwoEMT = SmacnaUtils.getEMT(tieRodLoadSide2);
                            } else {
                                int[] dim =
                                        DuctSides.getSides(Integer.parseInt(inputScreen
                                                .getHeight()), Integer
                                                .parseInt(inputScreen.getWidth()));
                                try {
//                                    EMT =
//                                            connectionService
//                                                    .getEmtForNegPC(dim[1], tieRodLoad);
                                    sideTwoEMT =
                                            connectionService
                                                    .getEmtForNegPC(dim[0], tieRodLoadSide2);
                                } catch (SmacnaException e) {
                                    logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                       e.getMessage(),
                                               e);
                                }
                            }
                            outPutScreenSix.setNoTieRodForSideOne(0);
                            outPutScreenSix.setNoTieRodForSideTwo(noOfTieRodSideTwo);
//                        if (noOfTieRod != 0) {
                        	outPutScreenSix
                                    .setTieRodLoadForSideTwo(tieRodLoadSide2);
//                        }
                        outPutScreenSix.setTieRodForSideTwo(sideTwoEMT);
                        outPutScreenSix.setExtRenfSide(String
                                .valueOf(sides[0]));
                        outPutScreens.add(outPutScreenSix);



                        //Output screen option 7

                    	//Output Screen Seven
                        /**
	                   	 *
	                   	 * Internal on Side 1 and External on Side 2
	                   	 *
	                   	 **/
                        outPutScreenSeven.setSideOne(sides[0]);
                        outPutScreenSeven.setSideTwo(sides[1]);
                        outPutScreenSeven.setExternalReinforcement(false);
                        outPutScreenSeven.setInternalReinforcement(false);
                        outPutScreenSeven.setExtAndInternalEachSide(false);
                        outPutScreenSeven.setIntAndExtEachSide(true);
                        if (reinDuctS1JSRSBy2 != null) {
                            IsJTRSideOne =
                                    StringsUtil
                                            .stripGarbageT25(reinDuctS1JSRSBy2);
                        }
                        if (reinDuctS2JSRSBy2 != null) {
                            IsJTRSideTwo =
                                    StringsUtil.stripGarbageT25(reinDuctS2JSRSBy2);
                        }
                        if (IsJTRSideOne != null && IsJTRSideOne.length == 3) {
                            if (IsJTRSideOne[2].equals("(R)")) {
                                outPutScreenSeven.setJtrForSideOne(true);
                            } else {
                                outPutScreenSeven.setJtrForSideOne(false);
                            }
                        } else {
                            outPutScreenSeven.setJtrForSideOne(false);
                        }
                        if (IsJTRSideTwo != null && IsJTRSideTwo.length == 3) {
                            if (IsJTRSideTwo[2].equals("(R)")) {
                                outPutScreenSeven.setJtrForSideTwo(true);
                            } else {
                                outPutScreenSeven.setJtrForSideTwo(false);
                            }
                        } else {
                            outPutScreenSeven.setJtrForSideTwo(false);
                        }
                        if (outPutScreenSeven.isJtrForSideOne()) {
                            tieRodLoadJTRSideOne =
                                    SmacnaUtils
                                            .getTieRodLoadForJTR(sides[0],
                                                                 (inputScreen
                                                                         .getJointSpacing() * 12 - 4) / 2F,
                                                                 noOfTieRodForS1,
                                                                 pc);
                            String jtrSizeForS1 = "";
                            if (inputScreen.isPosOrNeg()) {

                                jtrSizeForS1 =
                                            SmacnaUtils.getEMT(tieRodLoadJTRSideOne);

                            } else {
                                int[] dim =
                                        DuctSides.getSides(Integer
                                                .parseInt(inputScreen
                                                        .getHeight()), Integer
                                                .parseInt(inputScreen
                                                        .getWidth()));
                                try {

                                    jtrSizeForS1 =
                                                connectionService
                                                        .getEmtForNegPC(dim[1],
                                                                        tieRodLoadJTRSideOne);

                                } catch (SmacnaException e) {
                                    logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                       e.getMessage(),
                                               e);
                                }
                            }
                            outPutScreenSeven.setJtrSizeForS1(jtrSizeForS1);
                        }
                        if (outPutScreenSeven.isJtrForSideTwo()) {
                            tieRodLoadJTRSideTwo =
                                    SmacnaUtils
                                            .getTieRodLoadForJTR(sides[1],
                                                                 (inputScreen
                                                                         .getJointSpacing() * 12 - 4) / 2F,
                                                                 noOfTieRodForS2,
                                                                 pc);
                            String jtrSizeForS2 = "";
                            if (inputScreen.isPosOrNeg()) {

                                jtrSizeForS2 =
                                            SmacnaUtils.getEMT(tieRodLoadJTRSideTwo);

                            } else {
                                int[] dim =
                                        DuctSides.getSides(Integer
                                                .parseInt(inputScreen
                                                        .getHeight()), Integer
                                                .parseInt(inputScreen
                                                        .getWidth()));
                                try {

                                    jtrSizeForS2 =
                                                connectionService
                                                        .getEmtForNegPC(dim[0],
                                                                        tieRodLoadJTRSideTwo);

                                } catch (SmacnaException e) {
                                    logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                       e.getMessage(),
                                               e);
                                }
                            }
                            outPutScreenSeven.setJtrSizeForS2(jtrSizeForS2);
                        }
                        outPutScreenSeven
                                .setTieRoadLoadJTRForSideOne(tieRodLoadJTRSideOne);
                        outPutScreenSeven
                                .setTieRoadLoadJTRForSideTwo(tieRodLoadJTRSideTwo);
                        outPutScreenSeven.setMinDuctGageSOne(fianlDuctGageThree);
                        outPutScreenSeven.setMinDuctGageSTwo(fianlDuctGageTwo);
                        outPutScreenSeven.setFinalDuctGage(fianlDuctGageIV);

                        // Setting Reinforcement Class, Angle and Duct gage
                        // for
                        // S1, RS=JS/2 and S2, RS=JS

                        // Class:
                        outPutScreenSeven
                                .setReinClassForSideOne(reinClassS1JSRSBy2);
                        outPutScreenSeven.setReinClassForSideTwo(reinClassS2JSRSBy2);
                        // Angle:
                        outPutScreenSeven
                                .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                        outPutScreenSeven.setExtRenfAngleSideTwo(reinAngleS2JSRSBy2);
                        // Duct Gage:
                        outPutScreenSeven
                                .setDuctGageForSideOne(reinDuctS1JSRSBy2);
                        outPutScreenSeven.setDuctGageForSideTwo(reinClassS2JSRSBy2);

                        outPutScreenSeven
                                .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                                 .getPressureClass(),
                                                                         connectionService));
                        outPutScreenSeven
                                .setExtRenfSide(String.valueOf(sides[0]));

                        noOfTieRod =
                                calculateSideT25abFive(sides[0],
                                                       inputScreen.getPressureClass(),
                                                       inputScreen.getJointSpacing() / 2F,
                                                       inputScreen.getTransConnS1(),
                                                       fianlDuctGageIV,
                                                       tieRodService);
//                        noOfTieRodSideTwo =
//                                calculateSideT25abFive(sides[1],
//                                                       inputScreen.getPressureClass(),
//                                                       inputScreen.getJointSpacing() / 2F,
//                                                       inputScreen.getTransConnS2(),
//                                                       fianlDuctGageIV,
//                                                       tieRodService);
                         pc = 0;
                        if (noOfTieRod != 0) {
                            try {
                                pc =
                                        connectionService.getStaticPC(inputScreen
                                                .getPressureClass());
                            } catch (SmacnaException e) {
                                // TODO Auto-generated catch block
                            }

                            double tieRodLoad =
                                    SmacnaUtils
                                            .getTieRodLoad(sides[0],
                                                           (inputScreen
                                                                   .getJointSpacing() * 12 - 4) / 2F,
                                                           noOfTieRod,
                                                           pc);
                            ////////////////////////////////////////////////////////////////////////////
//                            tieRodLoadSide2 =
//                                    SmacnaUtils
//                                            .getTieRodLoad(sides[1],
//                                                           (inputScreen
//                                                                   .getJointSpacing() * 12 - 4) / 2F,
//                                                                   noOfTieRodSideTwo,
//                                                           pc);



                            ////////////////////////////////////////////////////////////////////////////
                            EMT = null;
//                            sideTwoEMT = null;
                            if (inputScreen.isPosOrNeg()) {
                                EMT = SmacnaUtils.getEMT(tieRodLoad);
//                                sideTwoEMT = SmacnaUtils.getEMT(tieRodLoadSide2);
                            } else {
                                int[] dim =
                                        DuctSides.getSides(Integer.parseInt(inputScreen
                                                .getHeight()), Integer
                                                .parseInt(inputScreen.getWidth()));
                                try {
                                    EMT =
                                            connectionService
                                                    .getEmtForNegPC(dim[1], tieRodLoad);
//                                    sideTwoEMT =
//                                            connectionService
//                                                    .getEmtForNegPC(dim[0], tieRodLoadSide2);
                                } catch (SmacnaException e) {
                                    logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                       e.getMessage(),
                                               e);
                                }
                            }
                            outPutScreenSeven.setNoTieRodForSideOne(noOfTieRod);
                            outPutScreenSeven.setNoTieRodForSideTwo(0);
//                        if (noOfTieRod != 0) {
                        	outPutScreenSeven
                                    .setTieRodLoadForSideOne(tieRodLoad);
//                        }
                        outPutScreenSeven.setTieRodForSideOne(EMT);
                        outPutScreenSeven.setExtRenfSide(String
                                .valueOf(sides[1]));
                        outPutScreens.add(outPutScreenSeven);



                    }
                }
                }
                }

                
		if (isRenforcementClassIsGForS1RSJS) {
			outPutScreens.add(getOptionForClassG(inputScreen,renfService, minDuctGageRSJS, sides, connectionService, "S1"));
		}
		if (isRenforcementClassIsGForS2RSJS) {
			outPutScreens.add(getOptionForClassG(inputScreen,renfService, minDuctGageRSJS2, sides, connectionService, "S2"));
		}
                
        return outPutScreens;

    }
    
    private OutPutScreen getOptionForClassG(InputScreen inputScreen,RectDuctRenfService renfService, int minDuctGage, int sides[], TransverseConnectionService connectionService, String side){
    	OutPutScreen outPutScreenOne = new OutPutScreen();
    	String classGTransverseJoinReinforcement = "22 ga (R)";
    	
    	String[] ductGageS1RSJ = null;
            ductGageS1RSJ = StringsUtil.stripGarbageT25(classGTransverseJoinReinforcement);
        inputScreen.setDesignAvailable("");
        // zeroth position contains duct gage
        // first position contains unit
        int fianlDuctGageOne = 0;
        if (ductGageS1RSJ != null) {
            fianlDuctGageOne =
                    Math.min(minDuctGage,
                             Integer.parseInt(ductGageS1RSJ[0]));
        } else {
            fianlDuctGageOne = Math.max(minDuctGage, 0);
        }
        //to check JTR for side one
        if (ductGageS1RSJ != null && ductGageS1RSJ.length == 3) {
            if (ductGageS1RSJ[2].equals("(R)")) {
                logg.info("########------JTR SIDE one------###############");
                isJTRforS1RSJS = true;
            }
        }
        // Setting reinforcement class, angle and duct gage for S1, RS = JS
        reinClassS1JSRS = "G";
        try {
            reinAngleS1JSRS =
                    renfService
                            .getMidSpanIntermediateReinforcement(reinClassS1JSRS);
        } catch (SmacnaException e) {
            logg.error("[midSpanInternediateRI in case of T25a/b] : Exception Occurred: " +
                    e.getMessage());
        } catch (Exception e) {
            logg.error("[midSpanInternediateRI in case of T25a/b] : Exception Occurred : " +
                               e.getMessage(),
                       e);
        }
    	
        outPutScreenOne.setSideOne(sides[0]);
        outPutScreenOne.setSideTwo(sides[1]);
        outPutScreenOne.setExternalReinforcement(false);
        outPutScreenOne.setInternalReinforcement(false);
        outPutScreenOne.setNoRenforcement(true);
        
        float pc = 0;
        try {
            pc =
                    connectionService.getStaticPC(inputScreen
                            .getPressureClass());
        } catch (SmacnaException e) {
            logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                    e.getMessage());
        } catch (Exception e) {
            logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                               e.getMessage(),
                       e);
        }
        
        double tieRodLoadJTRSideOne = 0.0;
        double tieRodLoadJTRSideTwo = 0.0;

        String IsJTRSideOne[] = null;
        String IsJTRSideTwo[] = null;
        if (side.equals("S1") && classGTransverseJoinReinforcement != null) {
            IsJTRSideOne = StringsUtil.stripGarbageT25(classGTransverseJoinReinforcement);
        }
        if (side.equals("S2") && classGTransverseJoinReinforcement != null) {
            IsJTRSideTwo = StringsUtil.stripGarbageT25(classGTransverseJoinReinforcement);
        }
        if(side.equals("S1")){
	        if (IsJTRSideOne != null && IsJTRSideOne.length == 3) {
	            if (IsJTRSideOne[2].equals("(R)")) {
	                outPutScreenOne.setJtrForSideOne(true);
	            } else {
	                outPutScreenOne.setJtrForSideOne(false);
	            }
	        }
        }else{
        	outPutScreenOne.setJtrForSideOne(false);
        }
        if(side.equals("S2")){
	        if (IsJTRSideTwo != null && IsJTRSideTwo.length == 3) {
	            if (IsJTRSideTwo[2].equals("(R)")) {
	                outPutScreenOne.setJtrForSideTwo(true);
	            } else {
	                outPutScreenOne.setJtrForSideTwo(false);
	            }
	        }
        }else{
        	outPutScreenOne.setJtrForSideTwo(false);
        }
        if (outPutScreenOne.isJtrForSideOne()) {
            tieRodLoadJTRSideOne =
                    SmacnaUtils
                            .getTieRodLoadForJTR(sides[0],
                                                 (inputScreen
                                                         .getJointSpacing() * 12 - 4) ,
                                                 0,
                                                 pc);
            String jtrSizeForS1 = "";
            if (inputScreen.isPosOrNeg()) {

                jtrSizeForS1 =
                            SmacnaUtils.getEMT(tieRodLoadJTRSideOne);

            } else {
                int[] dim =
                        DuctSides.getSides(Integer
                                .parseInt(inputScreen
                                        .getHeight()), Integer
                                .parseInt(inputScreen
                                        .getWidth()));
                try {

                    jtrSizeForS1 =
                                connectionService
                                        .getEmtForNegPC(dim[1],
                                                        tieRodLoadJTRSideOne);

                } catch (SmacnaException e) {
                    logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                       e.getMessage(),
                               e);
                }
            }
            outPutScreenOne.setJtrSizeForS1(jtrSizeForS1);


        }
        if (outPutScreenOne.isJtrForSideTwo()) {
            tieRodLoadJTRSideTwo =
                    SmacnaUtils
                            .getTieRodLoadForJTR(sides[1],
                                                 (inputScreen
                                                         .getJointSpacing() * 12 - 4) ,
                                                 0,
                                                 pc);
            String jtrSizeForS2 = "";
            if (inputScreen.isPosOrNeg()) {

                jtrSizeForS2 =
                            SmacnaUtils.getEMT(tieRodLoadJTRSideTwo);

            } else {
                int[] dim =
                        DuctSides.getSides(Integer
                                .parseInt(inputScreen
                                        .getHeight()), Integer
                                .parseInt(inputScreen
                                        .getWidth()));
                try {

                    jtrSizeForS2 =
                                connectionService
                                        .getEmtForNegPC(dim[0],
                                                        tieRodLoadJTRSideTwo);

                } catch (SmacnaException e) {
                    logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                       e.getMessage(),
                               e);
                }
            }
            outPutScreenOne.setJtrSizeForS2(jtrSizeForS2);
        }
        outPutScreenOne
                .setTieRoadLoadJTRForSideOne(tieRodLoadJTRSideOne);
        outPutScreenOne
                .setTieRoadLoadJTRForSideTwo(tieRodLoadJTRSideTwo);
        outPutScreenOne.setExternalRenfAngle("");
        outPutScreenOne.setMinDuctGageSOne(fianlDuctGageOne);
        outPutScreenOne.setMinDuctGageSTwo(0);
        outPutScreenOne.setFinalDuctGage(fianlDuctGageOne);

        // Setting Reinforcement Class, Angle and Duct gage for
        // S1, RS=JS and S2, RS=JS

        // Class:
        outPutScreenOne.setReinClassForSideOne(reinClassS1JSRS);
        outPutScreenOne.setReinClassForSideTwo(reinClassS2JSRS);
        // Angle:
        outPutScreenOne.setExtRenfAngleSideOne(reinAngleS1JSRS);
        outPutScreenOne.setExtRenfAngleSideTwo(reinAngleS2JSRS);
        // Duct Gage:
        outPutScreenOne.setDuctGageForSideOne(reinDuctS1JSRS);
        outPutScreenOne.setDuctGageForSideTwo(reinDuctS2JSRS);

        outPutScreenOne
                .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                 .getPressureClass(),
                                                         connectionService));
        outPutScreenOne.setNoTieRodForSideOne(0);
        outPutScreenOne.setNoTieRodForSideTwo(0);
        outPutScreenOne.setTieRodLoadForSideOne(0.0);
        outPutScreenOne.setTieRod("");
        outPutScreenOne.setExtRenfSide(String.valueOf(sides[0]));
      
    
        
        
    	return outPutScreenOne;
    }
}
