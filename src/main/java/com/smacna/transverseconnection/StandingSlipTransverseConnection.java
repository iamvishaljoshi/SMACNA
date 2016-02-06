package com.smacna.transverseconnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.smacna.constants.Constants;
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
 * This class generates the option for the combination
 * T10, T11 and T12 as Transverse connections
 */
@Service("standingSlipTranConn")
public class StandingSlipTransverseConnection extends
        TransverseConnections {

    public static final Log logg = LogFactory.getLog(StandingSlipTransverseConnection.class);
    public static boolean isPlus = false;
    @Override
    public List<OutPutScreen> getOptions(InputScreen inputScreen,
                                         int sides[],
                                         RectDuctRenfService renfService,
                                         MidPanelTieRodService tieRodService,
                                         TransverseConnectionService connectionService) throws SmacnaException {
        logg.info("[getOptions]:");
        int S1RSJSDuct = 0;
        int S2RSJSDuct = 0;
        int S1RSJSBy2Duct = 0;
        int S2RSJSBy2Duct = 0;
        List<OutPutScreen> outPutScreens = new ArrayList<OutPutScreen>();
        if ((inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T10 ||
                inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T11 || inputScreen
                .getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T12) &&
                (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T10 ||
                        inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T11 || inputScreen
                        .getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T12)) {
            logg.info("[getOptions]: TC ONE : " + inputScreen.getTransConnS1());
            logg.info("[getOptions]: TC TWO : " + inputScreen.getTransConnS2());

            // For S1, RS = JS
            String S1RSJS =
                    calculationForTtenToTtwelve(sides[0],
                                                inputScreen.getPressureClass(),
                                                inputScreen.getJointSpacing(),
                                                inputScreen.getTransConnS1(),
                                                true,
                                                renfService);

            // zeroth position contains External reinforcement
            // first position contains minimum duct gage
            // third position contains internal reinforcement
            String[] S1RSJSArray = StringsUtil.stripGarbage(S1RSJS);
            String EIRSJS = null;
            int minDuctGageRSJS = 0;
            String IRRSJS = null;
            if (S1RSJSArray[0].equals("NR")) {
            } else {
                inputScreen.setDesignAvailable("");
                if (S1RSJSArray.length == 3) {
                    EIRSJS = S1RSJSArray[0];
                    minDuctGageRSJS = Integer.parseInt(S1RSJSArray[1]);
                    IRRSJS = S1RSJSArray[2];
                } else {
                    EIRSJS = S1RSJSArray[0];
                    minDuctGageRSJS = Integer.parseInt(S1RSJSArray[1]);
                }
            }

            String ductGageRSJS1 = null;

            try {
                ductGageRSJS1 =
                        renfService.getGageFromRectDuctRenf(inputScreen
                                .getTransConnS1(), EIRSJS);
                if(ductGageRSJS1.contains("+") && !inputScreen.isPosOrNeg()){
                    StandingSlipTransverseConnection.isPlus = true;
                    return null;
                }
                reinAngleS1JSRS =
                        renfService.getMidSpanIntermediateReinforcement(EIRSJS);
            } catch (SmacnaException e) {
                logg.error("[ductGageRSJS1 in case of T1, T5, T10, T11, T12] : Exception Occurred: " +
                        e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                logg.error("[ductGageRSJS1 in case of T1, T5, T10, T11, T12] : Exception Occurred: " +
                                   e.getMessage(),
                           e);
            }

            if (ductGageRSJS1 == null || ductGageRSJS1.equals("NR")) {

                S1RSJSDuct = 0;
            } else {
                S1RSJSDuct = minDuctGageRSJS;
            }
            // Setting reinforcement class, angle and duct gage for S1, RS =
            // JS
            reinClassS1JSRS = EIRSJS;
            reinDuctS1JSRS = ductGageRSJS1;
            logg.info("[getOptions] :S1, RS = JS : ");
            logg.info("[getOptions] :##--##- Reinforcement Class -##--##--" +
                    reinClassS1JSRS);
            logg.info("[getOptions] :##--##- Reinforcement Angle -##--##--" +
                    reinAngleS1JSRS);
            logg.info("[getOptions] :##--##- Reinforcement Duct -##--##--" +
                    reinDuctS1JSRS);

            // For S2, RS = JS

            String S2RSJS =
                    calculationForTtenToTtwelve(sides[1],
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

            if (S2RSJSArray[0].equals("NR")) {

            } else {
                inputScreen.setDesignAvailable("");

                if (S2RSJSArray.length == 3) {
                    EI2RSJS = S2RSJSArray[0];
                    minDuctGageRSJS2 = Integer.parseInt(S2RSJSArray[1]);
                    IRRSJS2 = S2RSJSArray[2];
                    logg.info("SIZE THREE === " + minDuctGageRSJS2);
                } else {
                    EI2RSJS = S2RSJSArray[0];
                    minDuctGageRSJS2 = Integer.parseInt(S2RSJSArray[1]);
                    logg.info("SIZE TWO === " + minDuctGageRSJS2);
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

                reinAngleS2JSRS =
                        renfService
                                .getMidSpanIntermediateReinforcement(EI2RSJS);
            } catch (SmacnaException e) {
                logg.error("[ductGageRSJS2 in case of T1, T5, T10, T11, T12] : Exception Occurred: " +
                        e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                logg.error("[ductGageRSJS2 in case of T1, T5, T10, T11, T12] : Exception Occurred: " +
                                   e.getMessage(),
                           e);
            }
            if (ductGageRSJS2 == null || ductGageRSJS2.equals("NR")) {
                S2RSJSDuct = 0;
            } else {
                S2RSJSDuct = minDuctGageRSJS2;
            }
            // Setting reinforcement class, angle and duct gage for S2, RS =
            // JS
            reinClassS2JSRS = EI2RSJS;
            reinDuctS2JSRS = ductGageRSJS2;

            logg.info("[getOptions] :S2, RS = JS : ");
            logg.info("[getOptions] :##--##- Reinforcement Class -##--##--" +
                    reinClassS2JSRS);
            logg.info("[getOptions] :##--##- Reinforcement Angle -##--##--" +
                    reinAngleS2JSRS);
            logg.info("[getOptions]:##--##- Reinforcement Duct -##--##--" +
                    reinDuctS2JSRS);

            // For S1, RS = JS/2
            String S1RSJSBy2 =
                    calculationForTtenToTtwelve(sides[0],
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
            } else {
                inputScreen.setDesignAvailable("");
                if (S1Array.length == 3) {
                    EI1 = S1Array[0];
                    minDuctGage1 = Integer.parseInt(S1Array[1]);
                    IR1 = S1Array[2];
                } else {
                    EI1 = S1Array[0];
                    minDuctGage1 = Integer.parseInt(S1Array[1]);
                }
            }

            String ductGageS1RSJSBy2 = null;
            String midSpanInternediateRI = null;

            try {
                ductGageS1RSJSBy2 =
                        renfService.getGageFromRectDuctRenf(inputScreen
                                .getTransConnS1(), EI1);
                if(ductGageS1RSJSBy2.contains("+") && !inputScreen.isPosOrNeg()){
                    StandingSlipTransverseConnection.isPlus = true;
                    return null;
                }
                midSpanInternediateRI =
                        renfService.getMidSpanIntermediateReinforcement(EI1);
            } catch (SmacnaException e) {
                logg.error("[ductGageS1RSJSBy2 and midSpanInternediateRI in case of T1, T5, T10, T11, T12] : Exception Occurred: " +
                        e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                logg.error("[ductGageS1RSJSBy2 and midSpanInternediateRI in case of T1, T5, T10, T11, T12] : Exception Occurred: " +
                                   e.getMessage(),
                           e);
            }
            if (ductGageS1RSJSBy2 == null || ductGageS1RSJSBy2.equals("NR")) {

                S1RSJSBy2Duct = 0;
            } else {
                S1RSJSBy2Duct = minDuctGage1;
            }
            // Setting reinforcement class, angle and duct gage for S1, RS =
            // JS/2
            reinClassS1JSRSBy2 = EI1;
            reinDuctS1JSRSBy2 = ductGageS1RSJSBy2;
            reinAngleS1JSRSBy2 = midSpanInternediateRI;

            logg.info("[getOptions]:S1, RS = JS/2 : ");
            logg.info("[getOptions]:##--##- Reinforcement Class -##--##--" +
                    reinClassS1JSRSBy2);
            logg.info("[getOptions]:##--##- Reinforcement Angle -##--##--" +
                    reinAngleS1JSRSBy2);
            logg.info("[getOptions]:##--##- Reinforcement Duct -##--##--" +
                    reinDuctS1JSRSBy2);

            // For S2, RS = JS/2
            String S2RSJSBy2 =
                    calculationForTtenToTtwelve(sides[1],
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


            } else {
                inputScreen.setDesignAvailable("");
                if (S2Array.length == 3) {
                    EI2 = S2Array[0];
                    minDuctGage2 = Integer.parseInt(S2Array[1]);
                    IR2 = S2Array[2];
                } else {
                    EI2 = S2Array[0];
                    minDuctGage2 = Integer.parseInt(S2Array[1]);
                }
            }
            String midSpanInternediateRIS2 = null;
            String ductGageS2RSJSBy2 = null;

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
                logg.error("[ductGageS2RSJSBy2 and midSpanInternediateRIS2 in case of T1, T5, T10, T11, T12] : Exception Occurred: " +
                        e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                logg.error("[ductGageS2RSJSBy2 and midSpanInternediateRIS2 in case of T1, T5, T10, T11, T12] : Exception Occurred: " +
                                   e.getMessage(),
                           e);
            }
            if (ductGageS2RSJSBy2 == null || ductGageS2RSJSBy2.equals("NR")) {
                S2RSJSBy2Duct = 0;
            } else {
                S2RSJSBy2Duct = minDuctGage2;
            }

            // Setting reinforcement class, angle and duct gage for S2, RS =
            // JS/2
            reinClassS2JSRSBy2 = EI2;
            reinDuctS2JSRSBy2 = ductGageS2RSJSBy2;
            reinAngleS2JSRSBy2 = midSpanInternediateRIS2;

            logg.info("[getOptions]:S2, RS = JS/2 : ");
            logg.info("[getOptions]:##--##- Reinforcement Class -##--##--" +
                    reinClassS2JSRSBy2);
            logg.info("[getOptions]:##--##- Reinforcement Angle -##--##--" +
                    reinAngleS2JSRSBy2);
            logg.info("[getOptions]:##--##- Reinforcement Duct -##--##--" +
                    reinDuctS2JSRSBy2);

            logg.info("[getOptions]:S1RSJSDuct" + S1RSJSDuct);
            logg.info("[getOptions]:S2RSJSDuct" + S2RSJSDuct);
            logg.info("[getOptions]:S1RSJSBy2Duct" + S1RSJSBy2Duct);
            logg.info("[getOptions]:S2RSJSBy2Duct" + S2RSJSBy2Duct);

            int fianlDuctGageI = 0;
            int fianlDuctGageII = 0;
            int fianlDuctGageIII = 0;
            int fianlDuctGageIV = 0;
            // S1, RS=JS and S2 RS=JS

            fianlDuctGageI = Math.min(S1RSJSDuct, S2RSJSDuct);

            // S1, RS=JS/2 and S2 RS=JS External reinforcement is required
            // on S1 side

            fianlDuctGageII = Math.min(S1RSJSBy2Duct, S2RSJSDuct);
            // S1, RS=JS and S2 RS=JS/2
            fianlDuctGageIII = Math.min(S1RSJSDuct, S2RSJSBy2Duct);
            // S1, RS=JS/2 and S2 RS=JS/2
            fianlDuctGageIV = Math.min(S1RSJSBy2Duct, S2RSJSBy2Duct);

            logg.info("[getOptions]:fianlDuctGageI : " + fianlDuctGageI);
            logg.info("[getOptions]:fianlDuctGageII : " + fianlDuctGageII);
            logg.info("[getOptions]:fianlDuctGageIII : " + fianlDuctGageIII);
            logg.info("[getOptions]:fianlDuctGageIV : " + fianlDuctGageIV);



            if (fianlDuctGageI == 0 && fianlDuctGageII == 0 &&
                    fianlDuctGageIII == 0 && fianlDuctGageIV == 0) {

                return null;
            }

            OutPutScreen outPutScreenOne = new OutPutScreen();
            OutPutScreen outPutScreenTwo = new OutPutScreen();
            OutPutScreen outPutScreenThree = new OutPutScreen();
            OutPutScreen outPutScreenFour = new OutPutScreen();
            OutPutScreen outPutScreenFive = new OutPutScreen();

            OutPutScreen outPutScreenSix = new OutPutScreen();
            /*if(fianlDuctGageI == fianlDuctGageII && fianlDuctGageI == fianlDuctGageIII && fianlDuctGageI==fianlDuctGageIV)
            {
                float pc = 0;
                try {
                    pc =
                            connectionService.getStaticPC(inputScreen
                                    .getPressureClass());
                } catch (SmacnaException e) {
                    logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                            e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                                       e.getMessage(),
                               e);
                }
                outPutScreenOne.setSideOne(sides[0]);
                outPutScreenOne.setSideTwo(sides[1]);
                outPutScreenOne.setExternalReinforcement(false);
                outPutScreenOne.setInternalReinforcement(false);
                outPutScreenOne.setNoRenforcement(true);
                outPutScreenOne.setExternalRenfAngle("");
                outPutScreenOne.setMinDuctGageSOne(S1RSJSDuct);
                outPutScreenOne.setMinDuctGageSTwo(S1RSJSDuct);
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
                                .getPressureClass(), connectionService));
                outPutScreenOne.setNoTieRodForSideOne(0);
                outPutScreenOne.setNoTieRodForSideTwo(0);
                outPutScreenOne.setTieRodLoadForSideOne(0.0);
                outPutScreenOne.setTieRod("");
                outPutScreenOne.setExtRenfSide(String.valueOf(sides[0]));
                outPutScreens.add(outPutScreenOne);
            }
            else
            {*/

           /* if ((fianlDuctGageI == fianlDuctGageII) &&
                    (fianlDuctGageI == fianlDuctGageIII)) {
                if(fianlDuctGageI==0){
                    if (fianlDuctGageIV != 0) {

                                                int noOfTieRodForS1 =
                                                        calculateSideT25abFive(sides[0],
                                                                               inputScreen
                                                                                       .getPressureClass(),
                                                                               inputScreen
                                                                                       .getJointSpacing() / 2F,
                                                                               inputScreen
                                                                                       .getTransConnS1(),
                                                                               fianlDuctGageIV,
                                                                               tieRodService);

                                                int noOfTieRodForS2 =
                                                        calculateSideT25abFive(sides[0],
                                                                               inputScreen
                                                                                       .getPressureClass(),
                                                                               inputScreen
                                                                                       .getJointSpacing() / 2F,
                                                                               inputScreen
                                                                                       .getTransConnS2(),
                                                                               fianlDuctGageIV,
                                                                               tieRodService);

                                                logg.info("### --- NO OF TIE ROD For Side S1 : " +
                                                        noOfTieRodForS1);
                                                logg.info("### --- NO OF TIE ROD For Side S2 : " +
                                                        noOfTieRodForS2);
                                                float pc = 0;
                                                try {
                                                    pc =
                                                            connectionService.getStaticPC(inputScreen
                                                                    .getPressureClass());
                                                } catch (SmacnaException e) {
                                                    logg.error("[pc in case of T1, T5, T10, T11, T12] : Exception Occurred: " +
                                                            e.getMessage());
                                                    e.printStackTrace();
                                                } catch (Exception e) {
                                                    logg.error("[pc in case of T1, T5, T10, T11, T12] : Exception Occurred: " +
                                                                       e.getMessage(),
                                                               e);
                                                }

                                                double tieRodLoadForS1 = 0.0;
                                                if(noOfTieRodForS1!=0)
                                                {
                                                    tieRodLoadForS1 = SmacnaUtils
                                                                .getTieRodLoad(sides[0],
                                                                               (inputScreen
                                                                                       .getJointSpacing() * 12) / 2F,
                                                                               noOfTieRodForS1,
                                                                               pc);
                                                }

                                                double tieRodLoadForS2 = 0.0;
                                                if(noOfTieRodForS2!=0)
                                                {
                                                    tieRodLoadForS2 = SmacnaUtils
                                                                .getTieRodLoad(sides[1],
                                                                               (inputScreen
                                                                                       .getJointSpacing() * 12) / 2F,
                                                                               noOfTieRodForS2,
                                                                               pc);
                                                }

                                                String EMTForS1 = "No MPT Required";
                                                String EMTForS2 = "No MPT Required";
                                                if (inputScreen.isPosOrNeg()) {
                                                    EMTForS1 = SmacnaUtils.getEMT(tieRodLoadForS1);
                                                    EMTForS2 = SmacnaUtils.getEMT(tieRodLoadForS2);
                                                } else {
                                                    int[] dim =
                                                            DuctSides
                                                                    .getSides(Integer.parseInt(inputScreen
                                                                                      .getHeight()),
                                                                              Integer.parseInt(inputScreen
                                                                                      .getWidth()));
                                                    try {
                                                        EMTForS1 =
                                                                connectionService
                                                                        .getEmtForNegPC(dim[1],
                                                                                        tieRodLoadForS1);
                                                        EMTForS2 =
                                                                connectionService
                                                                        .getEmtForNegPC(dim[0],
                                                                                        tieRodLoadForS2);
                                                    } catch (SmacnaException e) {
                                                        logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                                           e.getMessage(),
                                                                   e);
                                                    }
                                                }

                                                if (EMTForS1 == null || EMTForS1.equals("error")) {

                                                    logg.info("[getOptions]:##########--E R R O R--##########");
                                                } else {

                                                    outPutScreenFour.setSideOne(sides[0]);
                                                    outPutScreenFour.setSideTwo(sides[1]);
                                                    outPutScreenFour.setExternalReinforcement(false);
                                                    outPutScreenFour.setInternalReinforcement(false);

                                                    outPutScreenFour.setExtAndInternalEachSide(true);

                                                    outPutScreenFour.setMinDuctGageSOne(S1RSJSBy2Duct);
                                                    outPutScreenFour.setMinDuctGageSTwo(S2RSJSBy2Duct);
                                                    outPutScreenFour.setFinalDuctGage(fianlDuctGageIV);
                                                    // Setting Reinforcement Class, Angle and Duct
                                                    // gage
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
                                                    outPutScreenFour.setTieRodLoadForSideOne(0.0);
                                                    outPutScreenFour
                                                            .setTieRodLoadForSideTwo(tieRodLoadForS2);
                                                    outPutScreenFour.setTieRodForSideOne(EMTForS1);
                                                    outPutScreenFour.setTieRodForSideTwo(EMTForS2);
                                                    outPutScreenFour.setExtRenfSide(String
                                                            .valueOf(sides[0]));
                                                    outPutScreens.add(outPutScreenFour);

                                                    // internal on side one and external on side two

                                                    outPutScreenSix.setSideOne(sides[0]);
                                                    outPutScreenSix.setSideTwo(sides[1]);
                                                    outPutScreenSix.setExternalReinforcement(false);
                                                    outPutScreenSix.setInternalReinforcement(false);

                                                    outPutScreenSix.setIntAndExtEachSide(true);
                                                    outPutScreenSix.setMinDuctGageSOne(S1RSJSBy2Duct);
                                                    outPutScreenSix.setMinDuctGageSTwo(S2RSJSBy2Duct);
                                                    outPutScreenSix.setFinalDuctGage(fianlDuctGageIV);
                                                    // Setting Reinforcement Class, Angle and Duct
                                                    // gage
                                                    // for
                                                    // S1, RS=JS/2 and S2, RS=JS/2

                                                    // Class:
                                                    outPutScreenSix
                                                            .setReinClassForSideOne(reinClassS1JSRSBy2);
                                                    outPutScreenSix
                                                            .setReinClassForSideTwo(reinClassS2JSRSBy2);
                                                    // Angle:
                                                    outPutScreenSix
                                                            .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                                                    outPutScreenSix
                                                            .setExtRenfAngleSideTwo(reinAngleS2JSRSBy2);
                                                    // Duct Gage:
                                                    outPutScreenSix
                                                            .setDuctGageForSideOne(reinDuctS1JSRSBy2);
                                                    outPutScreenSix
                                                            .setDuctGageForSideTwo(reinDuctS2JSRSBy2);

                                                    outPutScreenSix
                                                            .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                                                             .getPressureClass(),
                                                                                                     connectionService));
                                                    outPutScreenSix
                                                            .setNoTieRodForSideOne(noOfTieRodForS1);
                                                    outPutScreenSix.setNoTieRodForSideTwo(0);
                                                    outPutScreenSix
                                                            .setTieRodLoadForSideOne(tieRodLoadForS1);
                                                    outPutScreenSix.setTieRod("");
                                                    outPutScreenSix.setTieRodForSideOne(EMTForS1);
                                                    outPutScreenSix.setTieRodForSideTwo(EMTForS2);
                                                    outPutScreenSix.setExtRenfSide(String
                                                            .valueOf(sides[0]));
                                                    outPutScreens.add(outPutScreenSix);
                                                }
                    }

                }
                else {
                    outPutScreenOne.setSideOne(sides[0]);
                outPutScreenOne.setSideTwo(sides[1]);
                outPutScreenOne.setExternalReinforcement(false);
                outPutScreenOne.setInternalReinforcement(false);
                outPutScreenOne.setNoRenforcement(true);
                outPutScreenOne.setMinDuctGageSOne(S1RSJSDuct);
                outPutScreenOne.setMinDuctGageSTwo(S2RSJSDuct);
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
                                .getPressureClass(), connectionService));
                outPutScreenOne.setNoTieRodForSideOne(0);
                outPutScreenOne.setNoTieRodForSideTwo(0);
                outPutScreenOne.setTieRodLoadForSideOne(0.0);

                outPutScreenOne.setTieRod("");
                outPutScreenOne.setExtRenfSide(String.valueOf(sides[0]));
                outPutScreens.add(outPutScreenOne);

                // Last option with external on S1 and S2
                outPutScreenTwo.setSideOne(sides[0]);
                outPutScreenTwo.setSideTwo(sides[1]);
                outPutScreenTwo.setExternalReinforcement(false);
                outPutScreenTwo.setInternalReinforcement(false);
                outPutScreenTwo.setExternalBothSide(true);
                outPutScreenTwo.setMinDuctGageSOne(S1RSJSBy2Duct);
                outPutScreenTwo.setMinDuctGageSTwo(S2RSJSBy2Duct);
                outPutScreenTwo.setFinalDuctGage(fianlDuctGageIV);

                // Setting Reinforcement Class, Angle and Duct gage for
                // S1, RS=JS/2 and S2, RS=JS/2

                // Class:
                outPutScreenTwo.setReinClassForSideOne(reinClassS1JSRSBy2);
                outPutScreenTwo.setReinClassForSideTwo(reinClassS2JSRSBy2);
                // Angle:
                outPutScreenTwo.setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                outPutScreenTwo.setExtRenfAngleSideTwo(reinAngleS2JSRSBy2);
                // Duct Gage:
                outPutScreenTwo.setDuctGageForSideOne(reinDuctS1JSRSBy2);
                outPutScreenTwo.setDuctGageForSideTwo(reinDuctS2JSRSBy2);

                outPutScreenTwo
                        .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                .getPressureClass(), connectionService));
                outPutScreenTwo.setNoTieRodForSideOne(0);
                outPutScreenTwo.setNoTieRodForSideTwo(0);
                outPutScreenTwo.setTieRodLoadForSideOne(0.0);
                outPutScreenTwo.setTieRod("");
                outPutScreenTwo.setExtRenfSide(String.valueOf(sides[0]));
                outPutScreens.add(outPutScreenTwo);

                int noOfTieRodForS1 =
                        calculateSideT25abFive(sides[0],
                                               inputScreen.getPressureClass(),
                                               inputScreen.getJointSpacing() / 2F,
                                               inputScreen.getTransConnS1(),
                                               fianlDuctGageIV,
                                               tieRodService);

                int noOfTieRodForS2 =
                        calculateSideT25abFive(sides[0],
                                               inputScreen.getPressureClass(),
                                               inputScreen.getJointSpacing() / 2F,
                                               inputScreen.getTransConnS2(),
                                               fianlDuctGageIV,
                                               tieRodService);

                logg.info("### --- NO OF TIE ROD For Side S1 : " +
                        noOfTieRodForS1);
                logg.info("### --- NO OF TIE ROD For Side S2 : " +
                        noOfTieRodForS2);
                float pc = 0;
                try {
                    pc =
                            connectionService.getStaticPC(inputScreen
                                    .getPressureClass());
                } catch (SmacnaException e) {
                    logg.error("[pc in case of T1, T5, T10, T11, T12] : Exception Occurred: " +
                            e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    logg.error("[pc in case of T1, T5, T10, T11, T12] : Exception Occurred: " +
                                       e.getMessage(),
                               e);
                }
                double tieRodLoadForS1 = 0.0;
                if(noOfTieRodForS1!=0)
                {
                 tieRodLoadForS1 =
                        SmacnaUtils
                                .getTieRodLoad(sides[0],
                                               (inputScreen.getJointSpacing() * 12) / 2F,
                                               noOfTieRodForS1,
                                               pc);
                }

                double tieRodLoadForS2 = 0.0;

                        if(noOfTieRodForS2!=0)
                        {
                            tieRodLoadForS2 = SmacnaUtils
                                .getTieRodLoad(sides[1],
                                               (inputScreen.getJointSpacing() * 12) / 2F,
                                               noOfTieRodForS2,
                                               pc);
                }

                String EMTForS1 = "No MPT Required";
                String EMTForS2 = "No MPT Required";
                if (inputScreen.isPosOrNeg()) {
                    EMTForS1 = SmacnaUtils.getEMT(tieRodLoadForS1);
                    EMTForS2 = SmacnaUtils.getEMT(tieRodLoadForS2);
                } else {
                    int[] dim =
                            DuctSides.getSides(Integer.parseInt(inputScreen
                                    .getHeight()), Integer.parseInt(inputScreen
                                    .getWidth()));
                    try {
                        EMTForS1 =
                                connectionService
                                        .getEmtForNegPC(dim[1], tieRodLoadForS1);
                        EMTForS2 =
                                connectionService
                                        .getEmtForNegPC(dim[0], tieRodLoadForS2);
                    } catch (SmacnaException e) {
                        logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                           e.getMessage(),
                                   e);
                    }
                }

                if (EMTForS1 == null || EMTForS1.equals("error")) {

                    logg.error("[getOptions]:##########--E R R O R--##########");
                } else {
                    // Last option with internal on S1 and S2
                    outPutScreenThree.setSideOne(sides[0]);
                    outPutScreenThree.setSideTwo(sides[1]);
                    outPutScreenThree.setExternalReinforcement(false);
                    outPutScreenThree.setInternalReinforcement(false);
                    outPutScreenThree.setInternalBothSide(true);
                    outPutScreenThree
                            .setExternalRenfAngle(midSpanInternediateRIS2);
                    outPutScreenThree.setMinDuctGageSOne(S1RSJSBy2Duct);
                    outPutScreenThree.setMinDuctGageSTwo(S2RSJSBy2Duct);
                    outPutScreenThree.setFinalDuctGage(fianlDuctGageIV);

                    // Setting Reinforcement Class, Angle and Duct gage for
                    // S1, RS=JS/2 and S2, RS=JS/2

                    // Class:
                    outPutScreenThree
                            .setReinClassForSideOne(reinClassS1JSRSBy2);
                    outPutScreenThree
                            .setReinClassForSideTwo(reinClassS2JSRSBy2);
                    // Angle:
                    outPutScreenThree
                            .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                    outPutScreenThree
                            .setExtRenfAngleSideTwo(reinAngleS2JSRSBy2);
                    // Duct Gage:
                    outPutScreenThree.setDuctGageForSideOne(reinDuctS1JSRSBy2);
                    outPutScreenThree.setDuctGageForSideTwo(reinDuctS2JSRSBy2);

                    outPutScreenThree
                            .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                             .getPressureClass(),
                                                                     connectionService));
                    logg.info("[getOptions]:-------- TieRodForSideOne ----------" +
                            EMTForS1);
                    logg.info("[getOptions]:-------- TieRodForSideTwo ----------" +
                            EMTForS2);
                    outPutScreenThree.setTieRodForSideOne(EMTForS1);
                    outPutScreenThree.setTieRodForSideTwo(EMTForS2);
                    outPutScreenThree.setNoTieRodForSideOne(noOfTieRodForS1);
                    outPutScreenThree.setNoTieRodForSideTwo(noOfTieRodForS2);
                    if (noOfTieRodForS1 != 0) {
                        outPutScreenThree
                                .setTieRodLoadForSideOne(tieRodLoadForS1);
                    }
                    if (noOfTieRodForS2 != 0) {
                        outPutScreenThree
                                .setTieRodLoadForSideTwo(tieRodLoadForS2);
                    }
                    outPutScreenThree.setTieRod("");
                    outPutScreenThree.setExtRenfSide(String.valueOf(sides[0]));
                    outPutScreens.add(outPutScreenThree);

                    // Last option with internal on S1 and External on S2
                    outPutScreenFour.setSideOne(sides[0]);
                    outPutScreenFour.setSideTwo(sides[1]);
                    outPutScreenFour.setExternalReinforcement(false);
                    outPutScreenFour.setInternalReinforcement(false);
                    outPutScreenFour.setIntAndExtEachSide(true);

                    outPutScreenFour.setMinDuctGageSOne(S1RSJSBy2Duct);
                    outPutScreenFour.setMinDuctGageSTwo(S2RSJSBy2Duct);
                    outPutScreenFour.setFinalDuctGage(fianlDuctGageIV);

                    // Setting Reinforcement Class, Angle and Duct gage for
                    // S1, RS=JS/2 and S2, RS=JS/2

                    // Class:
                    outPutScreenFour.setReinClassForSideOne(reinClassS1JSRSBy2);
                    outPutScreenFour.setReinClassForSideTwo(reinClassS2JSRSBy2);
                    // Angle:
                    outPutScreenFour.setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                    outPutScreenFour.setExtRenfAngleSideTwo(reinAngleS2JSRSBy2);
                    // Duct Gage:
                    outPutScreenFour.setDuctGageForSideOne(reinDuctS1JSRSBy2);
                    outPutScreenFour.setDuctGageForSideTwo(reinDuctS2JSRSBy2);

                    outPutScreenFour
                            .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                             .getPressureClass(),
                                                                     connectionService));
                    outPutScreenFour.setNoTieRodForSideOne(noOfTieRodForS1);
                    outPutScreenFour.setNoTieRodForSideTwo(0);
                    if (noOfTieRodForS1 != 0) {
                        outPutScreenFour
                                .setTieRodLoadForSideOne(tieRodLoadForS1);
                    }
                    outPutScreenFour.setTieRodLoadForSideTwo(0.0);
                    outPutScreenFour.setTieRodForSideOne(EMTForS1);
                    outPutScreenFour.setExtRenfSide(String.valueOf(sides[0]));
                    outPutScreens.add(outPutScreenFour);

                    // Last option external on S1 and Internal on S2

                    outPutScreenSix.setSideOne(sides[0]);
                    outPutScreenSix.setSideTwo(sides[1]);
                    outPutScreenSix.setExternalReinforcement(false);
                    outPutScreenSix.setInternalReinforcement(false);
                    outPutScreenSix.setExtAndInternalEachSide(true);

                    outPutScreenSix.setMinDuctGageSOne(S1RSJSBy2Duct);
                    outPutScreenSix.setMinDuctGageSTwo(S2RSJSBy2Duct);
                    outPutScreenSix.setFinalDuctGage(fianlDuctGageIV);

                    // Setting Reinforcement Class, Angle and Duct gage for
                    // S1, RS=JS/2 and S2, RS=JS/2

                    // Class:
                    outPutScreenSix.setReinClassForSideOne(reinClassS1JSRSBy2);
                    outPutScreenSix.setReinClassForSideTwo(reinClassS2JSRSBy2);
                    // Angle:
                    outPutScreenSix.setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                    outPutScreenSix.setExtRenfAngleSideTwo(reinAngleS2JSRSBy2);
                    // Duct Gage:
                    outPutScreenSix.setDuctGageForSideOne(reinDuctS1JSRSBy2);
                    outPutScreenSix.setDuctGageForSideTwo(reinDuctS2JSRSBy2);

                    outPutScreenSix
                            .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                             .getPressureClass(),
                                                                     connectionService));
                    outPutScreenSix.setNoTieRodForSideOne(0);
                    if (noOfTieRodForS2 != 0) {
                        outPutScreenSix.setNoTieRodForSideTwo(noOfTieRodForS2);
                    }
                    outPutScreenSix.setTieRodLoadForSideOne(0.0);
                    logg.info("[getOptions]:--------------- Tie Rod For Side Two" +
                            EMTForS2);
                    outPutScreenSix.setTieRodLoadForSideTwo(tieRodLoadForS2);
                    outPutScreenSix.setTieRodForSideTwo(EMTForS2);
                    outPutScreenSix.setExtRenfSide(String.valueOf(sides[0]));
                    outPutScreens.add(outPutScreenSix);

                }
               }
            } else {*/

                if (fianlDuctGageI == fianlDuctGageIII) {

                    if (fianlDuctGageI != 0) {
                        outPutScreenOne.setSideOne(sides[0]);
                        outPutScreenOne.setSideTwo(sides[1]);
                        outPutScreenOne.setExternalReinforcement(false);
                        outPutScreenOne.setInternalReinforcement(false);
                        outPutScreenOne.setNoRenforcement(true);
                        outPutScreenOne.setMinDuctGageSOne(S1RSJSDuct);
                        outPutScreenOne.setMinDuctGageSTwo(S2RSJSDuct);
                        outPutScreenOne.setFinalDuctGage(fianlDuctGageI);
                        // Setting Reinforcement Class, Angle and Duct gage
                        // for
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
                        outPutScreenOne
                                .setExtRenfSide(String.valueOf(sides[0]));
                        outPutScreens.add(outPutScreenOne);
                    }
                } else {
                    if (fianlDuctGageI != 0) {
                        outPutScreenOne.setSideOne(sides[0]);
                        outPutScreenOne.setSideTwo(sides[1]);
                        outPutScreenOne.setExternalReinforcement(false);
                        outPutScreenOne.setInternalReinforcement(false);
                        outPutScreenOne.setNoRenforcement(true);
                        outPutScreenOne.setMinDuctGageSOne(S1RSJSDuct);
                        outPutScreenOne.setMinDuctGageSTwo(S2RSJSDuct);
                        outPutScreenOne.setFinalDuctGage(fianlDuctGageI);
                        // Setting Reinforcement Class, Angle and Duct gage
                        // for
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
                        outPutScreenOne
                                .setExtRenfSide(String.valueOf(sides[0]));
                        outPutScreens.add(outPutScreenOne);
                    }
                   /* if (fianlDuctGageIII != 0) {
                        outPutScreenThree.setSideOne(sides[0]);
                        outPutScreenThree.setSideTwo(sides[1]);
                        outPutScreenThree.setExternalReinforcement(false);
                        outPutScreenThree.setInternalReinforcement(false);
                        outPutScreenThree.setMinDuctGageSOne(S1RSJSDuct);
                        outPutScreenThree.setMinDuctGageSTwo(S2RSJSBy2Duct);
                        outPutScreenThree.setFinalDuctGage(fianlDuctGageIII);
                        // Setting Reinforcement Class, Angle and Duct gage
                        // for
                        // S1, RS=JS and S2, RS=JS/2

                        // Class:
                        outPutScreenThree
                                .setReinClassForSideOne(reinClassS1JSRS);
                        outPutScreenThree
                                .setReinClassForSideTwo(reinClassS2JSRSBy2);
                        // Angle:
                        outPutScreenThree
                                .setExtRenfAngleSideOne(reinAngleS1JSRS);
                        outPutScreenThree
                                .setExtRenfAngleSideTwo(reinAngleS2JSRSBy2);
                        // Duct Gage:
                        outPutScreenThree.setDuctGageForSideOne(reinDuctS1JSRS);
                        outPutScreenThree
                                .setDuctGageForSideTwo(reinDuctS2JSRSBy2);

                        outPutScreenThree
                                .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                                 .getPressureClass(),
                                                                         connectionService));
                        outPutScreenThree.setNoTieRodForSideOne(0);
                        outPutScreenThree.setNoTieRodForSideTwo(0);
                        outPutScreenThree.setTieRodLoadForSideOne(0.0);
                        outPutScreenThree.setTieRod("");
                        outPutScreenThree.setExtRenfSide(String
                                .valueOf(sides[1]));
                        outPutScreens.add(outPutScreenThree);
                    }*/
                }
               /* if (fianlDuctGageII == fianlDuctGageIV) {
                    // external reinforcement on S1
                    if (fianlDuctGageII != 0) {
                        outPutScreenTwo.setSideOne(sides[0]);
                        outPutScreenTwo.setSideTwo(sides[1]);
                        outPutScreenTwo.setExternalReinforcement(true);
                        outPutScreenTwo.setInternalReinforcement(false);

                        outPutScreenTwo.setMinDuctGageSOne(S1RSJSBy2Duct);
                        outPutScreenTwo.setMinDuctGageSTwo(S2RSJSDuct);
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
                } else {*/
                    if (fianlDuctGageII != 0) {
                        outPutScreenTwo.setSideOne(sides[0]);
                        outPutScreenTwo.setSideTwo(sides[1]);
                        outPutScreenTwo.setExternalReinforcement(true);
                        outPutScreenTwo.setInternalReinforcement(false);
                        outPutScreenTwo.setMinDuctGageSOne(S1RSJSBy2Duct);
                        outPutScreenTwo.setMinDuctGageSTwo(S2RSJSDuct);
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


                        //Option for internal reinforcement

                        outPutScreenThree.setSideOne(sides[0]);
                        outPutScreenThree.setSideTwo(sides[1]);
                        outPutScreenThree.setExternalReinforcement(false);
                        outPutScreenThree.setInternalReinforcement(true);

                        int noOfTieRod = calculateSideT25abFive(sides[0],
                                inputScreen.getPressureClass(), inputScreen
                                        .getJointSpacing() / 2F, inputScreen
                                        .getTransConnS1(), fianlDuctGageII, tieRodService);

                        float pc = 0;
                        try {
                            pc = connectionService.getStaticPC(inputScreen
                                    .getPressureClass());
                        } catch (SmacnaException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if (noOfTieRod != 0) {
                            double tieRodLoad = SmacnaUtils.getTieRodLoad(
                                    sides[0],
                                    (inputScreen.getJointSpacing() * 12) / 2F,
                                    noOfTieRod, pc);

                            String EMT = null;
                            if (inputScreen.isPosOrNeg()) {
                                EMT = SmacnaUtils.getEMT(tieRodLoad);
                            } else {
                                int[] dim = DuctSides.getSides(Integer
                                        .parseInt(inputScreen.getHeight()),
                                        Integer
                                                .parseInt(inputScreen
                                                        .getWidth()));
                                try {
                                    EMT = connectionService.getEmtForNegPC(
                                            dim[1], tieRodLoad);
                                } catch (SmacnaException e) {
                                    logg.error(
                                            "[Negative Pressure Class in case of T25a/b] : Exception Occurred: "
                                                    + e.getMessage(), e);
                                }
                            }
                            if (EMT.equals("error")) {

                                // To-Do code
                            } else {

                                outPutScreenThree
                                        .setMinDuctGageSOne(S1RSJSBy2Duct);
                                outPutScreenThree
                                        .setMinDuctGageSTwo(S2RSJSDuct);
                                outPutScreenThree
                                        .setFinalDuctGage(fianlDuctGageII);
                                // Setting Reinforcement Class, Angle and Duct
                                // gage
                                // for
                                // S1, RS=JS/2 and S2, RS=JS

                                // Class:
                                outPutScreenThree
                                        .setReinClassForSideOne(reinClassS1JSRSBy2);
                                // As Side Two is not required any
                                // reinforcement, So no
                                // need to add reinforcement class in object

                                // Angle:
                                outPutScreenThree
                                        .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);

                                outPutScreenThree
                                .setReinClassForSideTwo(reinClassS2JSRS);
                                // Angle:
                                outPutScreenThree
                                .setExtRenfAngleSideTwo(reinAngleS2JSRS);

                                outPutScreenThree
                                        .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                .getPressureClass(), connectionService));
                                outPutScreenThree
                                        .setNoTieRodForSideOne(noOfTieRod);
                                outPutScreenThree.setNoTieRodForSideTwo(0);
                                outPutScreenThree
                                        .setTieRodLoadForSideOne(tieRodLoad);
                                outPutScreenThree.setDuctGageForSideOne(reinDuctS1JSRSBy2);
                                outPutScreenThree.setDuctGageForSideTwo(reinDuctS2JSRS);
                                outPutScreenThree.setTieRod(EMT);
                                outPutScreenThree.setExtRenfSide(String
                                        .valueOf(sides[0]));

                                outPutScreens.add(outPutScreenThree);
                            }
                        }
                    }
                    if (fianlDuctGageIV != 0) {

                        int noOfTieRodForS1 =
                                calculateSideT25abFive(sides[0],
                                                       inputScreen
                                                               .getPressureClass(),
                                                       inputScreen
                                                               .getJointSpacing() / 2F,
                                                       inputScreen
                                                               .getTransConnS1(),
                                                       fianlDuctGageIV,
                                                       tieRodService);

                        int noOfTieRodForS2 =
                                calculateSideT25abFive(sides[1],
                                                       inputScreen
                                                               .getPressureClass(),
                                                       inputScreen
                                                               .getJointSpacing() / 2F,
                                                       inputScreen
                                                               .getTransConnS2(),
                                                       fianlDuctGageIV,
                                                       tieRodService);

                        logg.info("### --- NO OF TIE ROD For Side S1 : " +
                                noOfTieRodForS1);
                        logg.info("### --- NO OF TIE ROD For Side S2 : " +
                                noOfTieRodForS2);
                        float pc = 0;
                        try {
                            pc =
                                    connectionService.getStaticPC(inputScreen
                                            .getPressureClass());
                        } catch (SmacnaException e) {
                            logg.error("[pc in case of T1, T5, T10, T11, T12] : Exception Occurred: " +
                                    e.getMessage());
                            e.printStackTrace();
                        } catch (Exception e) {
                            logg.error("[pc in case of T1, T5, T10, T11, T12] : Exception Occurred: " +
                                               e.getMessage(),
                                       e);
                        }

                        double tieRodLoadForS1 = 0.0;
                        if(noOfTieRodForS1!=0)
                        {
                            tieRodLoadForS1 = SmacnaUtils
                                        .getTieRodLoad(sides[0],
                                                       (inputScreen
                                                               .getJointSpacing() * 12) / 2F,
                                                       noOfTieRodForS1,
                                                       pc);
                        }

                        double tieRodLoadForS2 = 0.0;
                        if(noOfTieRodForS2!=0)
                        {
                            tieRodLoadForS2 = SmacnaUtils
                                        .getTieRodLoad(sides[1],
                                                       (inputScreen
                                                               .getJointSpacing() * 12) / 2F,
                                                       noOfTieRodForS2,
                                                       pc);
                        }

                        String EMTForS1 = "No MPT Required";
                        String EMTForS2 = "No MPT Required";
                        if (inputScreen.isPosOrNeg()) {
                            EMTForS1 = SmacnaUtils.getEMT(tieRodLoadForS1);
                            EMTForS2 = SmacnaUtils.getEMT(tieRodLoadForS2);
                        } else {
                            int[] dim =
                                    DuctSides
                                            .getSides(Integer.parseInt(inputScreen
                                                              .getHeight()),
                                                      Integer.parseInt(inputScreen
                                                              .getWidth()));
                            try {
                                EMTForS1 =
                                        connectionService
                                                .getEmtForNegPC(dim[1],
                                                                tieRodLoadForS1);
                                EMTForS2 =
                                        connectionService
                                                .getEmtForNegPC(dim[0],
                                                                tieRodLoadForS2);
                            } catch (SmacnaException e) {
                                logg.error("[Negative Pressure Class in case of T25a/b] : Exception Occurred: " +
                                                   e.getMessage(),
                                           e);
                            }
                        }

                        if (EMTForS1 == null || EMTForS1.equals("error")) {

                            logg.error("[getOptions]:##########--E R R O R--##########");
                        } else {

                            outPutScreenFour.setSideOne(sides[0]);
                            outPutScreenFour.setSideTwo(sides[1]);
                            outPutScreenFour.setExternalReinforcement(false);
                            outPutScreenFour.setInternalReinforcement(false);

                            outPutScreenFour.setExtAndInternalEachSide(true);

                            outPutScreenFour.setMinDuctGageSOne(S1RSJSBy2Duct);
                            outPutScreenFour.setMinDuctGageSTwo(S2RSJSBy2Duct);
                            outPutScreenFour.setFinalDuctGage(fianlDuctGageIV);
                            // Setting Reinforcement Class, Angle and Duct
                            // gage
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
                            outPutScreenFour.setTieRodLoadForSideOne(0.0);
                            outPutScreenFour
                                    .setTieRodLoadForSideTwo(tieRodLoadForS2);
                            outPutScreenFour.setTieRodForSideOne(EMTForS1);
                            outPutScreenFour.setTieRodForSideTwo(EMTForS2);
                            outPutScreenFour.setExtRenfSide(String
                                    .valueOf(sides[0]));
                            outPutScreens.add(outPutScreenFour);

                            // internal on side one and external on side two

                            outPutScreenSix.setSideOne(sides[0]);
                            outPutScreenSix.setSideTwo(sides[1]);
                            outPutScreenSix.setExternalReinforcement(false);
                            outPutScreenSix.setInternalReinforcement(false);

                            outPutScreenSix.setIntAndExtEachSide(true);
                            outPutScreenSix.setMinDuctGageSOne(S1RSJSBy2Duct);
                            outPutScreenSix.setMinDuctGageSTwo(S2RSJSBy2Duct);
                            outPutScreenSix.setFinalDuctGage(fianlDuctGageIV);
                            // Setting Reinforcement Class, Angle and Duct
                            // gage
                            // for
                            // S1, RS=JS/2 and S2, RS=JS/2

                            // Class:
                            outPutScreenSix
                                    .setReinClassForSideOne(reinClassS1JSRSBy2);
                            outPutScreenSix
                                    .setReinClassForSideTwo(reinClassS2JSRSBy2);
                            // Angle:
                            outPutScreenSix
                                    .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                            outPutScreenSix
                                    .setExtRenfAngleSideTwo(reinAngleS2JSRSBy2);
                            // Duct Gage:
                            outPutScreenSix
                                    .setDuctGageForSideOne(reinDuctS1JSRSBy2);
                            outPutScreenSix
                                    .setDuctGageForSideTwo(reinDuctS2JSRSBy2);

                            outPutScreenSix
                                    .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                                     .getPressureClass(),
                                                                             connectionService));
                            outPutScreenSix
                                    .setNoTieRodForSideOne(noOfTieRodForS1);
                            outPutScreenSix.setNoTieRodForSideTwo(0);
                            outPutScreenSix
                                    .setTieRodLoadForSideOne(tieRodLoadForS1);
                            outPutScreenSix.setTieRod("");
                            outPutScreenSix.setTieRodForSideOne(EMTForS1);
                            outPutScreenSix.setTieRodForSideTwo(EMTForS2);
                            outPutScreenSix.setExtRenfSide(String
                                    .valueOf(sides[0]));
                            outPutScreens.add(outPutScreenSix);


                            //External on both sides
                            outPutScreenSix = new OutPutScreen();
                            outPutScreenSix.setSideOne(sides[0]);
                            outPutScreenSix.setSideTwo(sides[1]);
                            outPutScreenSix.setExternalReinforcement(false);
                            outPutScreenSix.setInternalReinforcement(false);
                            outPutScreenSix.setExternalBothSide(true);
                            outPutScreenSix.setMinDuctGageSOne(S1RSJSBy2Duct);
                            outPutScreenSix.setMinDuctGageSTwo(S2RSJSBy2Duct);
                            outPutScreenSix.setFinalDuctGage(fianlDuctGageIV);
                            // Setting Reinforcement Class, Angle and Duct
                            // gage
                            // for
                            // S1, RS=JS/2 and S2, RS=JS/2

                            // Class:
                            outPutScreenSix
                                    .setReinClassForSideOne(reinClassS1JSRSBy2);
                            outPutScreenSix
                                    .setReinClassForSideTwo(reinClassS2JSRSBy2);
                            // Angle:
                            outPutScreenSix
                                    .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                            outPutScreenSix
                                    .setExtRenfAngleSideTwo(reinAngleS2JSRSBy2);
                            // Duct Gage:
                            outPutScreenSix
                                    .setDuctGageForSideOne(reinDuctS1JSRSBy2);
                            outPutScreenSix
                                    .setDuctGageForSideTwo(reinDuctS2JSRSBy2);

                            outPutScreenSix
                                    .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                                     .getPressureClass(),
                                                                             connectionService));
                            outPutScreenSix
                                    .setNoTieRodForSideOne(noOfTieRodForS1);
                            outPutScreenSix.setNoTieRodForSideTwo(0);
                            outPutScreenSix
                                    .setTieRodLoadForSideOne(tieRodLoadForS1);
                            outPutScreenSix.setTieRod("");
                            outPutScreenSix.setTieRodForSideOne(EMTForS1);
                            outPutScreenSix.setTieRodForSideTwo(EMTForS2);
                            outPutScreenSix.setExtRenfSide(String
                                    .valueOf(sides[0]));
                            outPutScreens.add(outPutScreenSix);



                            outPutScreenThree = new OutPutScreen();
                            outPutScreenThree.setSideOne(sides[0]);
                            outPutScreenThree.setSideTwo(sides[1]);
                            outPutScreenThree.setExternalReinforcement(false);
                            outPutScreenThree.setInternalBothSide(true);


                            outPutScreenThree
                                    .setMinDuctGageSOne(S1RSJSBy2Duct);
                            outPutScreenThree
                                    .setMinDuctGageSTwo(S2RSJSBy2Duct);
                            outPutScreenThree
                                    .setFinalDuctGage(fianlDuctGageIV);
                            // Setting Reinforcement Class, Angle and Duct
                            // gage
                            // for
                            // S1, RS=JS/2 and S2, RS=JS

                            // Class:
                            outPutScreenThree
                                    .setReinClassForSideOne(reinClassS1JSRSBy2);
                            // As Side Two is not required any
                            // reinforcement, So no
                            // need to add reinforcement class in object

                            // Angle:
                            outPutScreenThree
                                    .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);

                            outPutScreenThree
                            .setReinClassForSideTwo(reinClassS2JSRSBy2);
                            // Angle:
                            outPutScreenThree
                            .setExtRenfAngleSideTwo(reinAngleS2JSRSBy2);

                            outPutScreenThree
                                    .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                            .getPressureClass(), connectionService));
                            outPutScreenThree
                                    .setNoTieRodForSideOne(noOfTieRodForS1);
                            outPutScreenThree.setNoTieRodForSideTwo(noOfTieRodForS2);
                            outPutScreenThree
                                    .setTieRodLoadForSideOne(tieRodLoadForS1);
                            outPutScreenThree
                            .setTieRodLoadForSideTwo(tieRodLoadForS2);
                            outPutScreenThree.setDuctGageForSideOne(reinDuctS1JSRSBy2);
                            outPutScreenThree.setDuctGageForSideTwo(reinDuctS2JSRSBy2);
                            outPutScreenThree.setTieRod(EMTForS1);
                            outPutScreenThree.setTieRodForSideOne(EMTForS1);
                            outPutScreenThree.setTieRodForSideTwo(EMTForS2);
                            outPutScreenThree.setExtRenfSide(String
                                    .valueOf(sides[0]));

                            outPutScreens.add(outPutScreenThree);
                                }


                    }
//                }
//            }
            /*if ((fianlDuctGageI == fianlDuctGageII) &&
                    (fianlDuctGageI == fianlDuctGageIII)) {

            } else {
                // Internal option S1, RS = JS/2 and S2, RS=JS (TieRod)
                if (fianlDuctGageII != 0) {
                    int noOfTieRod =
                            calculateSideT25abFive(sides[0],
                                                   inputScreen
                                                           .getPressureClass(),
                                                   inputScreen
                                                           .getJointSpacing() / 2F,
                                                   inputScreen.getTransConnS1(),
                                                   fianlDuctGageII,
                                                   tieRodService);

                    logg.info("[getOptions]:### --- NO OF TIE ROD : " + noOfTieRod);

                    float pc = 0;
                    try {
                        pc =
                                connectionService.getStaticPC(inputScreen
                                        .getPressureClass());
                    } catch (SmacnaException e) {
                        logg.error("[pc in case of T1, T5, T10, T11, T12] : Exception Occurred: " +
                                e.getMessage());
                        e.printStackTrace();
                    } catch (Exception e) {
                        logg.error("[pc in case of T1, T5, T10, T11, T12] : Exception Occurred: " +
                                           e.getMessage(),
                                   e);
                    }


                    double tieRodLoad = 0.0;
                    if(noOfTieRod!=0)
                    {
                        tieRodLoad = SmacnaUtils
                                    .getTieRodLoad(sides[0],
                                                   (inputScreen
                                                           .getJointSpacing() * 12) / 2F,
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
                        logg.error("[getOptions]:##########--E R R O R--##########");
                    } else {

                        outPutScreenFive.setSideOne(sides[0]);
                        outPutScreenFive.setSideTwo(sides[1]);

                        outPutScreenFive.setExternalReinforcement(false);
                        outPutScreenFive.setInternalReinforcement(true);

                        outPutScreenFive.setMinDuctGageSOne(S1RSJSBy2Duct);
                        outPutScreenFive.setMinDuctGageSTwo(S2RSJSDuct);
                        outPutScreenFive.setFinalDuctGage(fianlDuctGageII);

                        // Setting Reinforcement Class, Angle and Duct
                        // gage
                        // for
                        // S1, RS=JS/2 and S2, RS=JS

                        // Class:
                        outPutScreenFive
                                .setReinClassForSideOne(reinClassS1JSRSBy2);
                        outPutScreenFive
                                .setReinClassForSideTwo(reinClassS2JSRS);
                        // Angle:
                        outPutScreenFive
                                .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                        outPutScreenFive
                                .setExtRenfAngleSideTwo(reinAngleS2JSRS);
                        // Duct Gage:
                        outPutScreenFive
                                .setDuctGageForSideOne(reinDuctS1JSRSBy2);
                        outPutScreenFive.setDuctGageForSideTwo(reinDuctS2JSRS);

                        outPutScreenFive
                                .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                                                                 .getPressureClass(),
                                                                         connectionService));
                        outPutScreenFive.setNoTieRodForSideOne(noOfTieRod);
                        outPutScreenFive.setNoTieRodForSideTwo(0);
                        if (noOfTieRod != 0) {
                            outPutScreenFive
                                    .setTieRodLoadForSideOne(tieRodLoad);
                        }
                        outPutScreenFive.setTieRod(EMT);
                        outPutScreenFive.setExtRenfSide(String
                                .valueOf(sides[0]));
                        outPutScreens.add(outPutScreenFive);
                    }
                    }
                }
            }*/
//        }


        }
        else if((inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T10
                || inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T11 || inputScreen
                .getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T12)
                && (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T1 || inputScreen
                        .getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T5))
            {

            logg.info("[getOptions]:TC ONE --------- : "
                    + inputScreen.getTransConnS1());
            logg.info("[getOptions]:TC TWO : " + inputScreen.getTransConnS2());

            // For S1, RS = JS
            String S1RSJS = calculationForTtenToTtwelve(sides[0],
                    inputScreen.getPressureClass(), inputScreen
                            .getJointSpacing(), inputScreen
                            .getTransConnS1(), true, renfService);

            // zeroth position contains External reinforcement
            // first position contains minimum duct gage
            // third position contains internal reinforcement
            String[] S1RSJSArray = StringsUtil.stripGarbage(S1RSJS);
            String EIRSJS = null;
            int minDuctGageRSJS = 0;
            String IRRSJS = null;
            if (S1RSJSArray[0].equals("NR")) {
            return null;

            } else {
                inputScreen.setDesignAvailable("");
                if (S1RSJSArray.length == 3) {
                    EIRSJS = S1RSJSArray[0];
                    minDuctGageRSJS = Integer.parseInt(S1RSJSArray[1]);
                    IRRSJS = S1RSJSArray[2];
                } else {
                    EIRSJS = S1RSJSArray[0];
                    minDuctGageRSJS = Integer.parseInt(S1RSJSArray[1]);
                }
            }

            String ductGageRSJS1 = null;
            try {
                ductGageRSJS1 = renfService.getGageFromRectDuctRenf(
                        inputScreen.getTransConnS1(), EIRSJS);
                if(ductGageRSJS1.contains("+") && !inputScreen.isPosOrNeg()){
                    StandingSlipTransverseConnection.isPlus = true;
                    return null;
                }
            } catch (SmacnaException e) {
                logg
                        .error("[ductGageRSJS1 in case of T1, T5, T10, T11, T12] : Exception Occurred: "
                                + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                logg.error(
                        "[ductGageRSJS1 in case of T1, T5, T10, T11, T12] : Exception Occurred: "
                                + e.getMessage(), e);
            }

            if (ductGageRSJS1 == null || ductGageRSJS1.equals("NR")) {

                S1RSJSDuct = 0;
            } else {
                S1RSJSDuct = minDuctGageRSJS;
            }
            // Setting reinforcement class, angle and duct gage for S1, RS =
            // JS
            reinClassS1JSRS = EIRSJS;
            reinDuctS1JSRS = ductGageRSJS1;
            try {
                reinAngleS1JSRS = renfService
                        .getMidSpanIntermediateReinforcement(reinClassS1JSRS);
            } catch (SmacnaException e) {
                logg
                        .error("[midSpanInternediateRI in case of T10, T11, T12 = T1, T5] : Exception Occurred: "
                                + e.getMessage());
            } catch (Exception e) {
                logg
                        .error(
                                "[midSpanInternediateRI in case of T10, T11, T12 = T1, T5] : Exception Occurred : "
                                        + e.getMessage(), e);
            }

            // For S2, RS = JS

            S2RSJSDuct = calculateSide(sides[1], inputScreen
                    .getPressureClass(), inputScreen.getJointSpacing(),
                    inputScreen.getTransConnS2(), renfService);

            if (S2RSJSDuct == 0) {
                return null;

            }

            // For S1, RS = JS/2

            String S1RSJSBy2 = calculationForTtenToTtwelve(sides[0],
                    inputScreen.getPressureClass(), inputScreen
                            .getJointSpacing() / 2F, inputScreen
                            .getTransConnS1(), false, renfService);
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
                } else {
                    EI1 = S1Array[0];
                    minDuctGage1 = Integer.parseInt(S1Array[1]);
                }
            }
            String RenClassForNoRenforcement = EI1;
            String ductGageS1RSJSBy2 = null;
            String midSpanInternediateRI = null;
            try {
                ductGageS1RSJSBy2 = renfService.getGageFromRectDuctRenf(
                        inputScreen.getTransConnS1(), EI1);
                if(ductGageS1RSJSBy2.contains("+") && !inputScreen.isPosOrNeg()){
                    StandingSlipTransverseConnection.isPlus = true;
                    return null;
                }
                midSpanInternediateRI = renfService
                        .getMidSpanIntermediateReinforcement(EI1);
            } catch (SmacnaException e) {
                logg
                        .error("[ductGageS1RSJSBy2 and midSpanInternediateRI in case of T1, T5, T10, T11, T12] : Exception Occurred: "
                                + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                logg
                        .error(
                                "[ductGageS1RSJSBy2 and midSpanInternediateRI in case of T1, T5, T10, T11, T12] : Exception Occurred: "
                                        + e.getMessage(), e);
            }
            if (ductGageS1RSJSBy2 == null || ductGageS1RSJSBy2.equals("NR")) {

                S1RSJSBy2Duct = 0;
            } else {
                S1RSJSBy2Duct = minDuctGage1;
			}

            // Setting reinforcement class, angle and duct gage for S1, RS =
            // JS
            reinClassS1JSRSBy2 = EI1;
            reinDuctS1JSRSBy2 = ductGageS1RSJSBy2;
            reinAngleS1JSRSBy2 = midSpanInternediateRI;
            // For S2, RS = JS/2

            S2RSJSBy2Duct = S2RSJSDuct;

            int fianlDuctGageI = 0;
            int fianlDuctGageII = 0;
            int fianlDuctGageIII = 0;
            int fianlDuctGageIV = 0;
            // S1, RS=JS and S2 RS=JS

            fianlDuctGageI = Math.min(S1RSJSDuct, S2RSJSDuct);

            // S1, RS=JS/2 and S2 RS=JS External reinforcement is required
            // on S1 side

            fianlDuctGageII = Math.min(S1RSJSBy2Duct, S2RSJSDuct);
            // S1, RS=JS and S2 RS=JS/2
            fianlDuctGageIII = Math.min(S1RSJSDuct, S2RSJSBy2Duct);
            // S1, RS=JS/2 and S2 RS=JS/2
            fianlDuctGageIV = Math.min(S1RSJSBy2Duct, S2RSJSBy2Duct);

            logg.info("[getOptions]:S1RSJSDuct : " + S1RSJSDuct);
            logg.info("[getOptions]:S2RSJSDuct : " + S2RSJSDuct);
            logg.info("[getOptions]:S1RSJSBy2Duct : " + S1RSJSBy2Duct);
            logg.info("[getOptions]:S2RSJSBy2Duct : " + S2RSJSBy2Duct);

            logg.info("[getOptions]:fianlDuctGageI : " + fianlDuctGageI);
            logg.info("[getOptions]:fianlDuctGageII : " + fianlDuctGageII);
            logg.info("[getOptions]:fianlDuctGageIII : " + fianlDuctGageIII);
            logg.info("[getOptions]:fianlDuctGageIV : " + fianlDuctGageIV);

            if (fianlDuctGageI == 0 && fianlDuctGageII == 0
                    && fianlDuctGageIII == 0 && fianlDuctGageIV == 0) {
               return null;
            }

            OutPutScreen outPutScreenOne = new OutPutScreen();
            OutPutScreen outPutScreenTwo = new OutPutScreen();
            OutPutScreen outPutScreenThree = new OutPutScreen();
            OutPutScreen outPutScreenFour = new OutPutScreen();

            /*if(fianlDuctGageI == fianlDuctGageII && fianlDuctGageI == fianlDuctGageIII && fianlDuctGageI==fianlDuctGageIV)
            {

                float pc = 0;
                try {
                    pc =
                            connectionService.getStaticPC(inputScreen
                                    .getPressureClass());
                } catch (SmacnaException e) {
                    logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                            e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                                       e.getMessage(),
                               e);
                }
                outPutScreenOne.setSideOne(sides[0]);
                outPutScreenOne.setSideTwo(sides[1]);
                outPutScreenOne.setExternalReinforcement(false);
                outPutScreenOne.setInternalReinforcement(false);
                outPutScreenOne.setNoRenforcement(true);
                outPutScreenOne.setExternalRenfAngle("");
                outPutScreenOne.setMinDuctGageSOne(S1RSJSDuct);
                outPutScreenOne.setMinDuctGageSTwo(S1RSJSDuct);
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
                                .getPressureClass(), connectionService));
                outPutScreenOne.setNoTieRodForSideOne(0);
                outPutScreenOne.setNoTieRodForSideTwo(0);
                outPutScreenOne.setTieRodLoadForSideOne(0.0);
                outPutScreenOne.setTieRod("");
                outPutScreenOne.setExtRenfSide(String.valueOf(sides[0]));
                outPutScreens.add(outPutScreenOne);

            }
            else
            {*/

            if (fianlDuctGageI == fianlDuctGageIII) {

                if (fianlDuctGageI != 0) {
                    outPutScreenOne.setSideOne(sides[0]);
                    outPutScreenOne.setSideTwo(sides[1]);
                    outPutScreenOne.setExternalReinforcement(false);
                    outPutScreenOne.setInternalReinforcement(false);
                    outPutScreenOne.setNoRenforcement(true);
                    outPutScreenOne.setExternalRenfAngle("");
                    outPutScreenOne.setMinDuctGageSOne(S1RSJSDuct);
                    outPutScreenOne.setMinDuctGageSTwo(S2RSJSDuct);
                    outPutScreenOne.setFinalDuctGage(fianlDuctGageI);

                    // Setting Reinforcement Class, Angle and Duct gage
                    // for
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
                                    .getPressureClass(), connectionService));
                    outPutScreenOne.setNoTieRodForSideOne(0);
                    outPutScreenOne.setNoTieRodForSideTwo(0);
                    outPutScreenOne.setTieRodLoadForSideOne(0.0);
                    outPutScreenOne.setTieRod("");
                    outPutScreenOne
                            .setExtRenfSide(String.valueOf(sides[0]));
                    outPutScreens.add(outPutScreenOne);
                }
            } else {
                if (fianlDuctGageI != 0) {
                    outPutScreenOne.setSideOne(sides[0]);
                    outPutScreenOne.setSideTwo(sides[1]);
                    outPutScreenOne.setExternalReinforcement(false);
                    outPutScreenOne.setInternalReinforcement(false);
                    outPutScreenOne.setNoRenforcement(true);
                    outPutScreenOne.setExternalRenfAngle("");
                    outPutScreenOne.setMinDuctGageSOne(S1RSJSDuct);
                    outPutScreenOne.setMinDuctGageSTwo(S2RSJSDuct);
                    outPutScreenOne.setFinalDuctGage(fianlDuctGageI);
                    // Setting Reinforcement Class, Angle and Duct gage
                    // for
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
                                    .getPressureClass(), connectionService));
                    outPutScreenOne.setNoTieRodForSideOne(0);
                    outPutScreenOne.setNoTieRodForSideTwo(0);
                    outPutScreenOne.setTieRodLoadForSideOne(0.0);
                    outPutScreenOne.setTieRod("");
                    outPutScreenOne
                            .setExtRenfSide(String.valueOf(sides[0]));
                    outPutScreens.add(outPutScreenOne);
                }
               /* if (fianlDuctGageIII != 0) {
                    outPutScreenThree.setSideOne(sides[0]);
                    outPutScreenThree.setSideTwo(sides[1]);
                    outPutScreenThree.setExternalReinforcement(false);
                    outPutScreenThree.setInternalReinforcement(false);
                    outPutScreenThree.setNoRenforcement(true);
                    outPutScreenThree.setMinDuctGageSOne(S1RSJSDuct);
                    outPutScreenThree.setMinDuctGageSTwo(S2RSJSBy2Duct);
                    outPutScreenThree.setFinalDuctGage(fianlDuctGageIII);

                    // Setting Reinforcement Class, Angle and Duct gage
                    // for
                    // S1, RS=JS and S2, RS=JS/2

                    // Class:
                    outPutScreenThree
                            .setReinClassForSideOne(reinClassS1JSRS);
                    // As Side Two is not required any reinforcement, So no
                    // need to add reinforcement class in object

                    // Angle:
                    outPutScreenThree
                            .setExtRenfAngleSideOne(reinAngleS1JSRS);
                    // Duct Gage:
                    outPutScreenOne.setDuctGageForSideOne(reinDuctS1JSRS);

                    outPutScreenThree
                            .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                    .getPressureClass(), connectionService));
                    outPutScreenThree.setNoTieRodForSideOne(0);
                    outPutScreenThree.setNoTieRodForSideTwo(0);
                    outPutScreenThree.setTieRodLoadForSideOne(0.0);
                    outPutScreenThree.setTieRod("");
                    outPutScreenThree.setExtRenfSide(String
                            .valueOf(sides[1]));
                    outPutScreens.add(outPutScreenThree);
                }*/
            }
            /*if (fianlDuctGageII == fianlDuctGageIV) {
                // external reinforcement on S1
                if (fianlDuctGageII != 0) {
                    outPutScreenTwo.setSideOne(sides[0]);
                    outPutScreenTwo.setSideTwo(sides[1]);
                    outPutScreenTwo.setExternalReinforcement(true);
                    outPutScreenTwo.setInternalReinforcement(false);
                    outPutScreenTwo.setMinDuctGageSOne(S1RSJSBy2Duct);
                    outPutScreenTwo.setMinDuctGageSTwo(S2RSJSDuct);
                    outPutScreenTwo.setFinalDuctGage(fianlDuctGageII);

                    // Setting Reinforcement Class, Angle and Duct gage
                    // for
                    // S1, RS=JS/2 and S2, RS=JS

                    // Class:
                    outPutScreenTwo
                            .setReinClassForSideOne(reinClassS1JSRSBy2);
                    // As Side Two is not required any reinforcement, So no
                    // need to add reinforcement class in object

                    // Angle:
                    outPutScreenTwo
                            .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                    // Duct Gage:
                    outPutScreenTwo
                            .setDuctGageForSideOne(reinDuctS1JSRSBy2);

                    outPutScreenTwo
                            .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                    .getPressureClass(), connectionService));
                    outPutScreenTwo.setNoTieRodForSideOne(0);
                    outPutScreenTwo.setNoTieRodForSideTwo(0);
                    outPutScreenTwo.setTieRodLoadForSideOne(0.0);
                    outPutScreenTwo.setTieRod("");
                    outPutScreenTwo
                            .setExtRenfSide(String.valueOf(sides[0]));
                    outPutScreens.add(outPutScreenTwo);

                    outPutScreenThree.setSideOne(sides[0]);
                    outPutScreenThree.setSideTwo(sides[1]);
                    outPutScreenThree.setExternalReinforcement(false);
                    outPutScreenThree.setInternalReinforcement(true);
                    outPutScreenThree.setReinClassForSideOne(EI1);

                    int noOfTieRod = calculateSideT25abFive(sides[0],
                            inputScreen.getPressureClass(), inputScreen
                                    .getJointSpacing() / 2F, inputScreen
                                    .getTransConnS1(), fianlDuctGageII, tieRodService);

                    float pc = 0;
                    try {
                        pc = connectionService.getStaticPC(inputScreen
                                .getPressureClass());
                    } catch (SmacnaException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (noOfTieRod != 0) {
                        double tieRodLoad = SmacnaUtils.getTieRodLoad(
                                sides[0],
                                (inputScreen.getJointSpacing() * 12) / 2F,
                                noOfTieRod, pc);

                        String EMT = null;
                        if (inputScreen.isPosOrNeg()) {
                            EMT = SmacnaUtils.getEMT(tieRodLoad);
                        } else {
                            int[] dim = DuctSides.getSides(Integer
                                    .parseInt(inputScreen.getHeight()),
                                    Integer
                                            .parseInt(inputScreen
                                                    .getWidth()));
                            try {
                                EMT = connectionService.getEmtForNegPC(
                                        dim[1], tieRodLoad);
                            } catch (SmacnaException e) {
                                logg.error(
                                        "[Negative Pressure Class in case of T25a/b] : Exception Occurred: "
                                                + e.getMessage(), e);
                            }
                        }
                        if (EMT.equals("error")) {

                            // To-Do code
                        } else {

                            outPutScreenThree
                                    .setMinDuctGageSOne(S1RSJSBy2Duct);
                            outPutScreenThree
                                    .setMinDuctGageSTwo(S2RSJSDuct);
                            outPutScreenThree
                                    .setFinalDuctGage(fianlDuctGageII);
                            // Setting Reinforcement Class, Angle and Duct
                            // gage
                            // for
                            // S1, RS=JS/2 and S2, RS=JS

                            // Class:
                            outPutScreenThree
                                    .setReinClassForSideOne(reinClassS1JSRSBy2);
                            // As Side Two is not required any
                            // reinforcement, So no
                            // need to add reinforcement class in object

                            // Angle:
                            outPutScreenThree
                                    .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                            // Duct Gage:
                            outPutScreenThree
                                    .setDuctGageForSideOne(reinDuctS1JSRSBy2);

                            outPutScreenThree
                                    .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                            .getPressureClass(), connectionService));
                            outPutScreenThree
                                    .setNoTieRodForSideOne(noOfTieRod);
                            outPutScreenThree.setNoTieRodForSideTwo(0);
                            outPutScreenThree
                                    .setTieRodLoadForSideOne(tieRodLoad);
                            outPutScreenThree.setTieRod(EMT);
                            outPutScreenThree.setExtRenfSide(String
                                    .valueOf(sides[0]));

                            outPutScreens.add(outPutScreenThree);
                        }
                    }
                }
            } else {*/
                /*if (fianlDuctGageII != 0) {
                    outPutScreenTwo.setSideOne(sides[0]);
                    outPutScreenTwo.setSideTwo(sides[1]);
                    outPutScreenTwo.setExternalReinforcement(true);
                    outPutScreenTwo.setInternalReinforcement(false);
                    outPutScreenTwo.setMinDuctGageSOne(S1RSJSBy2Duct);
                    outPutScreenTwo.setMinDuctGageSTwo(S2RSJSDuct);
                    outPutScreenTwo.setFinalDuctGage(fianlDuctGageII);
                    // Setting Reinforcement Class, Angle and Duct gage
                    // for
                    // S1, RS=JS/2 and S2, RS=JS

                    // Class:
                    outPutScreenTwo
                            .setReinClassForSideOne(reinClassS1JSRSBy2);
                    // As Side Two is not required any reinforcement, So no
                    // need to add reinforcement class in object

                    // Angle:
                    outPutScreenTwo
                            .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                    // Duct Gage:
                    outPutScreenTwo
                            .setDuctGageForSideOne(reinDuctS1JSRSBy2);

                    outPutScreenTwo
                            .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                    .getPressureClass(), connectionService));
                    outPutScreenTwo.setNoTieRodForSideOne(0);
                    outPutScreenTwo.setNoTieRodForSideTwo(0);
                    outPutScreenTwo.setTieRodLoadForSideOne(0.0);
                    outPutScreenTwo.setTieRod("");
                    outPutScreenTwo
                            .setExtRenfSide(String.valueOf(sides[0]));
                    outPutScreens.add(outPutScreenTwo);
                }*/
                if (fianlDuctGageIV != 0) {
                    outPutScreenFour.setSideOne(sides[0]);
                    outPutScreenFour.setSideTwo(sides[1]);
                    outPutScreenFour.setExternalReinforcement(true);
                    outPutScreenFour.setInternalReinforcement(false);
                    outPutScreenFour.setMinDuctGageSOne(S1RSJSBy2Duct);
                    outPutScreenFour.setMinDuctGageSTwo(S2RSJSBy2Duct);
                    outPutScreenFour.setFinalDuctGage(fianlDuctGageIV);

                    // Setting Reinforcement Class, Angle and Duct gage
                    // for
                    // S1, RS=JS/2 and S2, RS=JS

                    // Class:
                    outPutScreenFour
                            .setReinClassForSideOne(reinClassS1JSRSBy2);
                    // As Side Two is not required any reinforcement, So no
                    // need to add reinforcement class in object

                    // Angle:
                    outPutScreenFour
                            .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                    // Duct Gage:
                    outPutScreenFour
                            .setDuctGageForSideOne(reinDuctS1JSRSBy2);

                    outPutScreenFour
                            .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                    .getPressureClass(), connectionService));
                    outPutScreenFour.setNoTieRodForSideOne(0);
                    outPutScreenFour.setNoTieRodForSideTwo(0);
                    outPutScreenFour.setTieRodLoadForSideOne(0.0);
                    outPutScreenFour.setTieRod("");
                    outPutScreenFour.setExtRenfSide(String
                            .valueOf(sides[0]));
                    outPutScreens.add(outPutScreenFour);



                  //Option for Internal Reienforcement

                    outPutScreenThree.setSideOne(sides[0]);
                    outPutScreenThree.setSideTwo(sides[1]);
                    outPutScreenThree.setExternalReinforcement(false);
                    outPutScreenThree.setInternalReinforcement(true);

                    int noOfTieRod = calculateSideT25abFive(sides[0],
                            inputScreen.getPressureClass(), inputScreen
                                    .getJointSpacing() / 2F, inputScreen
                                    .getTransConnS1(), fianlDuctGageIV, tieRodService);

                    float pc = 0;
                    try {
                        pc = connectionService.getStaticPC(inputScreen
                                .getPressureClass());
                    } catch (SmacnaException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (noOfTieRod != 0) {
                        double tieRodLoad = SmacnaUtils.getTieRodLoad(
                                sides[0],
                                (inputScreen.getJointSpacing() * 12) / 2F,
                                noOfTieRod, pc);

                        String EMT = null;
                        if (inputScreen.isPosOrNeg()) {
                            EMT = SmacnaUtils.getEMT(tieRodLoad);
                        } else {
                            int[] dim = DuctSides.getSides(Integer
                                    .parseInt(inputScreen.getHeight()),
                                    Integer
                                            .parseInt(inputScreen
                                                    .getWidth()));
                            try {
                                EMT = connectionService.getEmtForNegPC(
                                        dim[1], tieRodLoad);
                            } catch (SmacnaException e) {
                                logg.error(
                                        "[Negative Pressure Class in case of T25a/b] : Exception Occurred: "
                                                + e.getMessage(), e);
                            }
                        }
                        if (EMT.equals("error")) {

                            // To-Do code
                        } else {

                            outPutScreenThree
                                    .setMinDuctGageSOne(S1RSJSBy2Duct);
                            outPutScreenThree
                                    .setMinDuctGageSTwo(S2RSJSBy2Duct);
                            outPutScreenThree
                                    .setFinalDuctGage(fianlDuctGageIV);
                            // Setting Reinforcement Class, Angle and Duct
                            // gage
                            // for
                            // S1, RS=JS/2 and S2, RS=JS

                            // Class:
                            outPutScreenThree
                                    .setReinClassForSideOne(reinClassS1JSRSBy2);
                            // As Side Two is not required any
                            // reinforcement, So no
                            // need to add reinforcement class in object

                            // Angle:
                            outPutScreenThree
                                    .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);

                            outPutScreenThree
                            .setReinClassForSideTwo(reinClassS2JSRSBy2);
                            // Angle:
                            outPutScreenThree
                            .setExtRenfAngleSideTwo(reinAngleS2JSRSBy2);

                            outPutScreenThree
                                    .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                            .getPressureClass(), connectionService));
                            outPutScreenThree
                                    .setNoTieRodForSideOne(noOfTieRod);
                            outPutScreenThree.setNoTieRodForSideTwo(0);
                            outPutScreenThree
                                    .setTieRodLoadForSideOne(tieRodLoad);
                            outPutScreenThree.setDuctGageForSideOne(reinDuctS1JSRSBy2);
                            outPutScreenThree.setTieRod(EMT);
                            outPutScreenThree.setExtRenfSide(String
                                    .valueOf(sides[0]));

                            outPutScreens.add(outPutScreenThree);
                        }
                    }
                }
//            }

//            }
            }

		else if ((inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T1 || inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T5)
				&& (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T10
						|| inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T11 || inputScreen
						.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T12))            {

            logg.info("[getOptions]:TC ONE --------- : "
                    + inputScreen.getTransConnS1());
            logg.info("[getOptions]:TC TWO : " + inputScreen.getTransConnS2());

            // For S1, RS = JS
            S1RSJSDuct = calculateSide(sides[0], inputScreen
                    .getPressureClass(), inputScreen.getJointSpacing(),
                    inputScreen.getTransConnS1(), renfService);

            if (S1RSJSDuct == 0) {
                return null;

            }

         // S1, RS=JS/2 and S2 RS=JS
            S1RSJSBy2Duct = S1RSJSDuct;

            // For S2, RS = JS

            String S2RSJS =
            calculationForTtenToTtwelve(sides[1],
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

    if (S2RSJSArray[0].equals("NR")) {

    } else {
        inputScreen.setDesignAvailable("");

        if (S2RSJSArray.length == 3) {
            EI2RSJS = S2RSJSArray[0];
            minDuctGageRSJS2 = Integer.parseInt(S2RSJSArray[1]);
            IRRSJS2 = S2RSJSArray[2];
            logg.info("SIZE THREE === " + minDuctGageRSJS2);
        } else {
            EI2RSJS = S2RSJSArray[0];
            minDuctGageRSJS2 = Integer.parseInt(S2RSJSArray[1]);
            logg.info("SIZE TWO === " + minDuctGageRSJS2);
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

        reinAngleS2JSRS =
                renfService
                        .getMidSpanIntermediateReinforcement(EI2RSJS);
    } catch (SmacnaException e) {
        logg.error("[ductGageRSJS2 in case of T1, T5, T10, T11, T12] : Exception Occurred: " +
                e.getMessage());
        e.printStackTrace();
    } catch (Exception e) {
        logg.error("[ductGageRSJS2 in case of T1, T5, T10, T11, T12] : Exception Occurred: " +
                           e.getMessage(),
                   e);
    }
    if (ductGageRSJS2 == null || ductGageRSJS2.equals("NR")) {
        S2RSJSDuct = 0;
    } else {
        S2RSJSDuct = minDuctGageRSJS2;
    }
    // Setting reinforcement class, angle and duct gage for S2, RS =
    // JS
    reinClassS2JSRS = EI2RSJS;
    reinDuctS2JSRS = ductGageRSJS2;

    logg.info("[getOptions] :S2, RS = JS : ");
    logg.info("[getOptions] :##--##- Reinforcement Class -##--##--" +
            reinClassS2JSRS);
    logg.info("[getOptions] :##--##- Reinforcement Angle -##--##--" +
            reinAngleS2JSRS);
    logg.info("[getOptions]:##--##- Reinforcement Duct -##--##--" +
            reinDuctS2JSRS);
            // For S2, RS = JS/2

    String S2RSJSBy2 =
            calculationForTtenToTtwelve(sides[1],
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


    } else {
        inputScreen.setDesignAvailable("");
        if (S2Array.length == 3) {
            EI2 = S2Array[0];
            minDuctGage2 = Integer.parseInt(S2Array[1]);
            IR2 = S2Array[2];
        } else {
            EI2 = S2Array[0];
            minDuctGage2 = Integer.parseInt(S2Array[1]);
        }
    }
    String midSpanInternediateRIS2 = null;
    String ductGageS2RSJSBy2 = null;

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
        logg.error("[ductGageS2RSJSBy2 and midSpanInternediateRIS2 in case of T1, T5, T10, T11, T12] : Exception Occurred: " +
                e.getMessage());
        e.printStackTrace();
    } catch (Exception e) {
        logg.error("[ductGageS2RSJSBy2 and midSpanInternediateRIS2 in case of T1, T5, T10, T11, T12] : Exception Occurred: " +
                           e.getMessage(),
                   e);
    }
    if (ductGageS2RSJSBy2 == null || ductGageS2RSJSBy2.equals("NR")) {
        S2RSJSBy2Duct = 0;
    } else {
        S2RSJSBy2Duct = minDuctGage2;
    }

    // Setting reinforcement class, angle and duct gage for S2, RS =
    // JS/2
    reinClassS2JSRSBy2 = EI2;
    reinDuctS2JSRSBy2 = ductGageS2RSJSBy2;
    reinAngleS2JSRSBy2 = midSpanInternediateRIS2;

    logg.info("[getOptions]:S2, RS = JS/2 : ");
    logg.info("[getOptions]:##--##- Reinforcement Class -##--##--" +
            reinClassS2JSRSBy2);
    logg.info("[getOptions]:##--##- Reinforcement Angle -##--##--" +
            reinAngleS2JSRSBy2);
    logg.info("[getOptions]:##--##- Reinforcement Duct -##--##--" +
            reinDuctS2JSRSBy2);

    logg.info("[getOptions]:S1RSJSDuct" + S1RSJSDuct);
    logg.info("[getOptions]:S2RSJSDuct" + S2RSJSDuct);
    logg.info("[getOptions]:S1RSJSBy2Duct" + S1RSJSBy2Duct);
    logg.info("[getOptions]:S2RSJSBy2Duct" + S2RSJSBy2Duct);

            int fianlDuctGageI = 0;
            int fianlDuctGageII = 0;
            int fianlDuctGageIII = 0;
            int fianlDuctGageIV = 0;


            fianlDuctGageI = Math.min(S1RSJSDuct, S2RSJSDuct);

            // S1, RS=JS/2 and S2 RS=JS External reinforcement is required
            // on S1 side

            fianlDuctGageII = Math.min(S1RSJSBy2Duct, S2RSJSDuct);
            // S1, RS=JS and S2 RS=JS/2
            fianlDuctGageIII = Math.min(S1RSJSDuct, S2RSJSBy2Duct);
            // S1, RS=JS/2 and S2 RS=JS/2
            fianlDuctGageIV = Math.min(S1RSJSBy2Duct, S2RSJSBy2Duct);

            logg.info("[getOptions]:S1RSJSDuct : " + S1RSJSDuct);
            logg.info("[getOptions]:S2RSJSDuct : " + S2RSJSDuct);
            logg.info("[getOptions]:S1RSJSBy2Duct : " + S1RSJSBy2Duct);
            logg.info("[getOptions]:S2RSJSBy2Duct : " + S2RSJSBy2Duct);

            logg.info("[getOptions]:fianlDuctGageI : " + fianlDuctGageI);
            logg.info("[getOptions]:fianlDuctGageII : " + fianlDuctGageII);
            logg.info("[getOptions]:fianlDuctGageIII : " + fianlDuctGageIII);
            logg.info("[getOptions]:fianlDuctGageIV : " + fianlDuctGageIV);

            if (fianlDuctGageI == 0 && fianlDuctGageII == 0
                    && fianlDuctGageIII == 0 && fianlDuctGageIV == 0) {
               return null;
            }

            OutPutScreen outPutScreenOne = new OutPutScreen();
            OutPutScreen outPutScreenTwo = new OutPutScreen();
            OutPutScreen outPutScreenThree = new OutPutScreen();
            OutPutScreen outPutScreenFour = new OutPutScreen();

           /* if(fianlDuctGageI == fianlDuctGageII && fianlDuctGageI == fianlDuctGageIII && fianlDuctGageI==fianlDuctGageIV)
            {

                float pc = 0;
                try {
                    pc =
                            connectionService.getStaticPC(inputScreen
                                    .getPressureClass());
                } catch (SmacnaException e) {
                    logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                            e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    logg.error("[pc in case of T25 a/b] : Exception Occurred: " +
                                       e.getMessage(),
                               e);
                }
                outPutScreenOne.setSideOne(sides[0]);
                outPutScreenOne.setSideTwo(sides[1]);
                outPutScreenOne.setExternalReinforcement(false);
                outPutScreenOne.setInternalReinforcement(false);
                outPutScreenOne.setNoRenforcement(true);
                outPutScreenOne.setExternalRenfAngle("");
                outPutScreenOne.setMinDuctGageSOne(S1RSJSDuct);
                outPutScreenOne.setMinDuctGageSTwo(S1RSJSDuct);
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
                                .getPressureClass(), connectionService));
                outPutScreenOne.setNoTieRodForSideOne(0);
                outPutScreenOne.setNoTieRodForSideTwo(0);
                outPutScreenOne.setTieRodLoadForSideOne(0.0);
                outPutScreenOne.setTieRod("");
                outPutScreenOne.setExtRenfSide(String.valueOf(sides[0]));
                outPutScreens.add(outPutScreenOne);

            }
            else
            {*/

            if (fianlDuctGageI == fianlDuctGageIII) {

                if (fianlDuctGageI != 0) {
                    outPutScreenOne.setSideOne(sides[0]);
                    outPutScreenOne.setSideTwo(sides[1]);
                    outPutScreenOne.setExternalReinforcement(false);
                    outPutScreenOne.setInternalReinforcement(false);
                    outPutScreenOne.setNoRenforcement(true);
                    outPutScreenOne.setExternalRenfAngle("");
                    outPutScreenOne.setMinDuctGageSOne(S1RSJSDuct);
                    outPutScreenOne.setMinDuctGageSTwo(S2RSJSDuct);
                    outPutScreenOne.setFinalDuctGage(fianlDuctGageI);

                    // Setting Reinforcement Class, Angle and Duct gage
                    // for
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
                                    .getPressureClass(), connectionService));
                    outPutScreenOne.setNoTieRodForSideOne(0);
                    outPutScreenOne.setNoTieRodForSideTwo(0);
                    outPutScreenOne.setTieRodLoadForSideOne(0.0);
                    outPutScreenOne.setTieRod("");
                    outPutScreenOne
                            .setExtRenfSide(String.valueOf(sides[0]));
                    outPutScreens.add(outPutScreenOne);
                }
            } else {
                if (fianlDuctGageI != 0) {
                    outPutScreenOne.setSideOne(sides[0]);
                    outPutScreenOne.setSideTwo(sides[1]);
                    outPutScreenOne.setExternalReinforcement(false);
                    outPutScreenOne.setInternalReinforcement(false);
                    outPutScreenOne.setNoRenforcement(true);
                    outPutScreenOne.setExternalRenfAngle("");
                    outPutScreenOne.setMinDuctGageSOne(S1RSJSDuct);
                    outPutScreenOne.setMinDuctGageSTwo(S2RSJSDuct);
                    outPutScreenOne.setFinalDuctGage(fianlDuctGageI);
                    // Setting Reinforcement Class, Angle and Duct gage
                    // for
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
                                    .getPressureClass(), connectionService));
                    outPutScreenOne.setNoTieRodForSideOne(0);
                    outPutScreenOne.setNoTieRodForSideTwo(0);
                    outPutScreenOne.setTieRodLoadForSideOne(0.0);
                    outPutScreenOne.setTieRod("");
                    outPutScreenOne
                            .setExtRenfSide(String.valueOf(sides[0]));
                    outPutScreens.add(outPutScreenOne);
                }
                /*if (fianlDuctGageIII != 0) {
                    outPutScreenThree.setSideOne(sides[0]);
                    outPutScreenThree.setSideTwo(sides[1]);
                    outPutScreenThree.setExternalReinforcement(false);
                    outPutScreenThree.setInternalReinforcement(false);
                    outPutScreenThree.setNoRenforcement(true);
                    outPutScreenThree.setMinDuctGageSOne(S1RSJSDuct);
                    outPutScreenThree.setMinDuctGageSTwo(S2RSJSBy2Duct);
                    outPutScreenThree.setFinalDuctGage(fianlDuctGageIII);

                    // Setting Reinforcement Class, Angle and Duct gage
                    // for
                    // S1, RS=JS and S2, RS=JS/2

                    // Class:
                    outPutScreenThree
                            .setReinClassForSideOne(reinClassS1JSRS);
                    // As Side Two is not required any reinforcement, So no
                    // need to add reinforcement class in object

                    // Angle:
                    outPutScreenThree
                            .setExtRenfAngleSideOne(reinAngleS1JSRS);
                    // Duct Gage:
                    outPutScreenOne.setDuctGageForSideOne(reinDuctS1JSRS);

                    outPutScreenThree
                            .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                    .getPressureClass(), connectionService));
                    outPutScreenThree.setNoTieRodForSideOne(0);
                    outPutScreenThree.setNoTieRodForSideTwo(0);
                    outPutScreenThree.setTieRodLoadForSideOne(0.0);
                    outPutScreenThree.setTieRod("");
                    outPutScreenThree.setExtRenfSide(String
                            .valueOf(sides[1]));
                    outPutScreens.add(outPutScreenThree);
                }*/
            }
            /*if (fianlDuctGageII == fianlDuctGageIV) {
                // external reinforcement on S1
                if (fianlDuctGageII != 0) {
                    outPutScreenTwo.setSideOne(sides[0]);
                    outPutScreenTwo.setSideTwo(sides[1]);
                    outPutScreenTwo.setExternalReinforcement(true);
                    outPutScreenTwo.setInternalReinforcement(false);
                    outPutScreenTwo.setMinDuctGageSOne(S1RSJSBy2Duct);
                    outPutScreenTwo.setMinDuctGageSTwo(S2RSJSDuct);
                    outPutScreenTwo.setFinalDuctGage(fianlDuctGageII);

                    // Setting Reinforcement Class, Angle and Duct gage
                    // for
                    // S1, RS=JS/2 and S2, RS=JS

                    // Class:
                    outPutScreenTwo
                            .setReinClassForSideOne(reinClassS1JSRSBy2);
                    // As Side Two is not required any reinforcement, So no
                    // need to add reinforcement class in object

                    // Angle:
                    outPutScreenTwo
                            .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                    // Duct Gage:
                    outPutScreenTwo
                            .setDuctGageForSideOne(reinDuctS1JSRSBy2);

                    outPutScreenTwo
                            .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                    .getPressureClass(), connectionService));
                    outPutScreenTwo.setNoTieRodForSideOne(0);
                    outPutScreenTwo.setNoTieRodForSideTwo(0);
                    outPutScreenTwo.setTieRodLoadForSideOne(0.0);
                    outPutScreenTwo.setTieRod("");
                    outPutScreenTwo
                            .setExtRenfSide(String.valueOf(sides[0]));
                    outPutScreens.add(outPutScreenTwo);

                    outPutScreenThree.setSideOne(sides[0]);
                    outPutScreenThree.setSideTwo(sides[1]);
                    outPutScreenThree.setExternalReinforcement(false);
                    outPutScreenThree.setInternalReinforcement(true);

                    int noOfTieRod = calculateSideT25abFive(sides[0],
                            inputScreen.getPressureClass(), inputScreen
                                    .getJointSpacing() / 2F, inputScreen
                                    .getTransConnS1(), fianlDuctGageII, tieRodService);

                    float pc = 0;
                    try {
                        pc = connectionService.getStaticPC(inputScreen
                                .getPressureClass());
                    } catch (SmacnaException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (noOfTieRod != 0) {
                        double tieRodLoad = SmacnaUtils.getTieRodLoad(
                                sides[0],
                                (inputScreen.getJointSpacing() * 12) / 2F,
                                noOfTieRod, pc);

                        String EMT = null;
                        if (inputScreen.isPosOrNeg()) {
                            EMT = SmacnaUtils.getEMT(tieRodLoad);
                        } else {
                            int[] dim = DuctSides.getSides(Integer
                                    .parseInt(inputScreen.getHeight()),
                                    Integer
                                            .parseInt(inputScreen
                                                    .getWidth()));
                            try {
                                EMT = connectionService.getEmtForNegPC(
                                        dim[1], tieRodLoad);
                            } catch (SmacnaException e) {
                                logg.error(
                                        "[Negative Pressure Class in case of T25a/b] : Exception Occurred: "
                                                + e.getMessage(), e);
                            }
                        }
                        if (EMT.equals("error")) {

                            // To-Do code
                        } else {

                            outPutScreenThree
                                    .setMinDuctGageSOne(S1RSJSBy2Duct);
                            outPutScreenThree
                                    .setMinDuctGageSTwo(S2RSJSDuct);
                            outPutScreenThree
                                    .setFinalDuctGage(fianlDuctGageII);
                            // Setting Reinforcement Class, Angle and Duct
                            // gage
                            // for
                            // S1, RS=JS/2 and S2, RS=JS

                            // Class:
                            outPutScreenThree
                                    .setReinClassForSideOne(reinClassS1JSRSBy2);
                            // As Side Two is not required any
                            // reinforcement, So no
                            // need to add reinforcement class in object

                            // Angle:
                            outPutScreenThree
                                    .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);
                            // Duct Gage:
                            outPutScreenThree
                                    .setDuctGageForSideOne(reinDuctS1JSRSBy2);

                            outPutScreenThree
                                    .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                            .getPressureClass(), connectionService));
                            outPutScreenThree
                                    .setNoTieRodForSideOne(noOfTieRod);
                            outPutScreenThree.setNoTieRodForSideTwo(0);
                            outPutScreenThree
                                    .setTieRodLoadForSideOne(tieRodLoad);
                            outPutScreenThree.setTieRod(EMT);
                            outPutScreenThree.setExtRenfSide(String
                                    .valueOf(sides[0]));

                            outPutScreens.add(outPutScreenThree);
                        }
                    }
                }
            } else {*/
                /*if (fianlDuctGageII != 0) {
                    outPutScreenTwo.setSideOne(sides[0]);
                    outPutScreenTwo.setSideTwo(sides[1]);
                    outPutScreenTwo.setExternalReinforcement(false);
                    outPutScreenTwo.setInternalReinforcement(false);
                    outPutScreenTwo.setMinDuctGageSOne(S1RSJSBy2Duct);
                    outPutScreenTwo.setMinDuctGageSTwo(S2RSJSDuct);
                    outPutScreenTwo.setFinalDuctGage(fianlDuctGageII);
                    // Setting Reinforcement Class, Angle and Duct gage
                    // for
                    // S1, RS=JS/2 and S2, RS=JS

                    // Class:
                    outPutScreenTwo
                            .setReinClassForSideOne(reinClassS1JSRSBy2);

                    outPutScreenTwo
                    .setReinClassForSideTwo(reinClassS2JSRS);
                    // As Side Two is not required any reinforcement, So no
                    // need to add reinforcement class in object

                    // Angle:
                    outPutScreenTwo
                            .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);

                    outPutScreenTwo
                    .setExtRenfAngleSideTwo(reinAngleS2JSRS);

                    // Duct Gage:
                    outPutScreenTwo
                            .setDuctGageForSideOne(reinDuctS1JSRSBy2);

                    outPutScreenTwo
                            .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                    .getPressureClass(), connectionService));
                    outPutScreenTwo.setNoTieRodForSideOne(0);
                    outPutScreenTwo.setNoTieRodForSideTwo(0);
                    outPutScreenTwo.setDuctGageForSideOne(reinDuctS1JSRSBy2);
                    outPutScreenTwo.setDuctGageForSideTwo(reinDuctS2JSRS);
                    outPutScreenTwo.setTieRodLoadForSideOne(0.0);
                    outPutScreenTwo.setTieRod("");
                    outPutScreenTwo
                            .setExtRenfSide(String.valueOf(sides[0]));
                    outPutScreens.add(outPutScreenTwo);
                }*/
                if (fianlDuctGageIV != 0) {/*
                    outPutScreenFour.setSideOne(sides[0]);
                    outPutScreenFour.setSideTwo(sides[1]);
                    outPutScreenFour.setExternalReinforcement(true);
                    outPutScreenFour.setInternalReinforcement(false);
                    outPutScreenFour.setMinDuctGageSOne(S1RSJSBy2Duct);
                    outPutScreenFour.setMinDuctGageSTwo(S2RSJSBy2Duct);
                    outPutScreenFour.setFinalDuctGage(fianlDuctGageIV);

                    // Setting Reinforcement Class, Angle and Duct gage
                    // for
                    // S1, RS=JS/2 and S2, RS=JS

                    // Class:
                    outPutScreenFour
                            .setReinClassForSideOne(reinClassS2JSRSBy2);

                    outPutScreenFour
                    .setReinClassForSideTwo(reinClassS2JSRSBy2);
                    // As Side Two is not required any reinforcement, So no
                    // need to add reinforcement class in object

                    // Angle:
                    outPutScreenFour
                            .setExtRenfAngleSideOne(reinAngleS2JSRSBy2);

                    outPutScreenFour
                    .setExtRenfAngleSideTwo(reinAngleS2JSRSBy2);

                    // Duct Gage:
                    outPutScreenFour
                            .setDuctGageForSideOne(reinDuctS1JSRSBy2);

                    outPutScreenFour
                    .setDuctGageForSideTwo(reinDuctS2JSRSBy2);

                    outPutScreenFour
                            .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                    .getPressureClass(), connectionService));
                    outPutScreenFour.setNoTieRodForSideOne(0);
                    outPutScreenFour.setNoTieRodForSideTwo(0);
                    outPutScreenTwo.setDuctGageForSideOne(reinDuctS1JSRSBy2);
                    outPutScreenTwo.setDuctGageForSideTwo(reinDuctS2JSRSBy2);
                    outPutScreenFour.setTieRodLoadForSideOne(0.0);
                    outPutScreenFour.setTieRod("");
                    outPutScreenFour.setExtRenfSide(String
                            .valueOf(sides[1]));
                    outPutScreens.add(outPutScreenFour);


                    //Option for Internal Reienforcement

                    outPutScreenThree.setSideOne(sides[0]);
                    outPutScreenThree.setSideTwo(sides[1]);
                    outPutScreenThree.setExternalReinforcement(false);
                    outPutScreenThree.setInternalReinforcement(true);

                    int noOfTieRod = calculateSideT25abFive(sides[1],
                            inputScreen.getPressureClass(), inputScreen
                                    .getJointSpacing() / 2F, inputScreen
                                    .getTransConnS1(), fianlDuctGageIV, tieRodService);

                    float pc = 0;
                    try {
                        pc = connectionService.getStaticPC(inputScreen
                                .getPressureClass());
                    } catch (SmacnaException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (noOfTieRod != 0) {
                        double tieRodLoad = SmacnaUtils.getTieRodLoad(
                                sides[1],
                                (inputScreen.getJointSpacing() * 12) / 2F,
                                noOfTieRod, pc);

                        String EMT = null;
                        if (inputScreen.isPosOrNeg()) {
                            EMT = SmacnaUtils.getEMT(tieRodLoad);
                        } else {
                            int[] dim = DuctSides.getSides(Integer
                                    .parseInt(inputScreen.getHeight()),
                                    Integer
                                            .parseInt(inputScreen
                                                    .getWidth()));
                            try {
                                EMT = connectionService.getEmtForNegPC(
                                        dim[0], tieRodLoad);
                            } catch (SmacnaException e) {
                                logg.error(
                                        "[Negative Pressure Class in case of T25a/b] : Exception Occurred: "
                                                + e.getMessage(), e);
                            }
                        }
                        if (EMT.equals("error")) {

                            // To-Do code
                        } else {

                            outPutScreenThree
                                    .setMinDuctGageSOne(S1RSJSBy2Duct);
                            outPutScreenThree
                                    .setMinDuctGageSTwo(S2RSJSBy2Duct);
                            outPutScreenThree
                                    .setFinalDuctGage(fianlDuctGageII);
                            // Setting Reinforcement Class, Angle and Duct
                            // gage
                            // for
                            // S1, RS=JS/2 and S2, RS=JS

                            // Class:
                            outPutScreenThree
                                    .setReinClassForSideOne(reinClassS1JSRSBy2);
                            // As Side Two is not required any
                            // reinforcement, So no
                            // need to add reinforcement class in object

                            // Angle:
                            outPutScreenThree
                                    .setExtRenfAngleSideOne(reinAngleS1JSRSBy2);

                            outPutScreenThree
                            .setReinClassForSideTwo(reinClassS2JSRSBy2);
                            // Angle:
                            outPutScreenThree
                            .setExtRenfAngleSideTwo(reinAngleS2JSRSBy2);

                            outPutScreenThree
                                    .setLongitudinalSeam(getLongitudinalSeam(inputScreen
                                            .getPressureClass(), connectionService));
                            outPutScreenThree
                                    .setNoTieRodForSideOne(noOfTieRod);
                            outPutScreenThree.setNoTieRodForSideTwo(0);
                            outPutScreenThree
                                    .setTieRodLoadForSideOne(tieRodLoad);
                            outPutScreenThree.setDuctGageForSideOne(reinDuctS1JSRSBy2);
                            outPutScreenThree.setDuctGageForSideTwo(reinDuctS2JSRSBy2);
                            outPutScreenThree.setTieRod(EMT);
                            outPutScreenThree.setExtRenfSide(String
                                    .valueOf(sides[1]));

                            outPutScreens.add(outPutScreenThree);
                        }
                    }



                */}
//            }

//            }
            }

        return outPutScreens;
    }
}
