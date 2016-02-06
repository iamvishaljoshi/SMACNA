package com.smacna.transverseconnection;

import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.smacna.controller.CalculateController;
import com.smacna.exception.SmacnaException;
import com.smacna.model.InputScreen;
import com.smacna.model.OutPutScreen;
import com.smacna.service.MidPanelTieRodService;
import com.smacna.service.RectDuctRenfService;
import com.smacna.service.TransverseConnectionService;

abstract public class TransverseConnections {

    public static final Log logg = LogFactory.getLog(TransverseConnections.class);

    /**
     * MDGS1 stands for minimum duct gage for side one
     */
    public int MDGS1 = 0;
    /**
     * MDGS2 stands for minimum duct gage for side two
     */
    public int MDGS2 = 0;

    /**
     * FDG stands for final duct gage
     */
    public int FDG = 0;

    /**
     * longitudinalSeam could be L1, L2 or both
     */
    public String longitudinalSeam = null;

    boolean isJTRForSideOne = false;

    boolean isJTRForSideTwo = false;

    boolean isJTRforS1RSJS = false;

    boolean isJTRforS2RSJS = false;

    boolean isJTRforS1RSJSBy2 = false;

    boolean isJTRforS2RSJSBy2 = false;

    String reinClassS1JSRS = null;

    String reinAngleS1JSRS = null;

    String reinDuctS1JSRS = null;

    String reinClassS2JSRS = null;

    String reinAngleS2JSRS = null;

    String reinDuctS2JSRS = null;

    String reinClassS1JSRSBy2 = null;

    String reinAngleS1JSRSBy2 = null;

    String reinDuctS1JSRSBy2 = null;

    String reinClassS2JSRSBy2 = null;

    String reinAngleS2JSRSBy2 = null;

    String reinDuctS2JSRSBy2 = null;

    boolean sideOneT = false;

    boolean sideTwoT = false;

    /**
    *
    * @param sideX
    * @param pressureClass
    * @param jointSpacing
    * @param transverseConnection
    * @return integer minimum duct gage.
    *
    *         This method is use to get the minimum gage for constructing duct.
    */
    public int calculateSide(int sideX,
                             int pressureClass,
                             int jointSpacing,
                             int transverseConnection,
                             RectDuctRenfService renfService) {
        logg.info("[calculateSide] : sideX = " + sideX + ", pressureClass = " +
                pressureClass + ", jointSpacing = " + jointSpacing +
                ", transverseConnection = " + transverseConnection);

        try {
            return renfService.getRectDuctRenfForTOne(pressureClass,
                                                      sideX,
                                                      jointSpacing,
                                                      transverseConnection);
        } catch (SmacnaException e) {
            logg.error("[calculateSide] : Exception Occurred: " +
                    e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logg.error("[calculateSide] : Exception Occurred: " +
                               e.getMessage(),
                       e);
        }
        return 0;
    }

    /**
     *
     * @param sideX
     * @param pressureClass
     * @param jointSpacing
     * @param transverseConnection
     * @return integer This method is use in case of : S1, RS = JS and S2, RS =
     *         JS/2 This will required external reinforcement.
     */
    public String calculateSideT25abTwo(int sideX,
                                        int pressureClass,
                                        float jointSpacing,
                                        int transverseConnection,
                                        boolean RSequalsJS,
                                        RectDuctRenfService renfService) {
        logg.info("[calculateSideT25ab] : sideX = " + sideX +
                ", pressureClass = " + pressureClass + ", jointSpacing = " +
                jointSpacing + ", transverseConnection = " +
                transverseConnection);
        try {
            return renfService.getRectDuctRenfForT25(pressureClass,
                                                     sideX,
                                                     jointSpacing,
                                                     transverseConnection,
                                                     RSequalsJS);
        } catch (SmacnaException e) {
            logg.error("[calculateSideT25ab] : Exception Occurred: " +
                    e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logg.error("[calculateSideT25ab] : Exception Occurred: " +
                               e.getMessage(),
                       e);
        }

        return null;

    }

    /**
     *
     * @param sideX
     * @param pressureClass
     * @param jointSpacing
     * @param transverseConnection
     * @return integer This method is use in case of and TieRod and will give
     *         you the number of TieRod is required
     *
     */
    public int calculateSideT25abFive(int sideX,
                                      int pressureClass,
                                      float jointSpacing,
                                      int transverseConnection,
                                      int ductGage,
                                      MidPanelTieRodService tieRodService) {
        logg.info("[calculateSideT25abFive] : sideX = " + sideX +
                ", pressureClass = " + pressureClass + ", jointSpacing = " +
                jointSpacing + ", transverseConnection = " +
                transverseConnection + ", ductGage" + ductGage);
        try {
            int noTieRod =
                    tieRodService.getNumberOfTieRod(pressureClass,
                                                    jointSpacing,
                                                    ductGage,
                                                    sideX);
            return noTieRod;

        } catch (SmacnaException e) {
            logg.error("[calculateSideT25abFive] : Exception Occurred: " +
                    e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logg.error("[calculateSideT25abFive] : Exception Occurred: " +
                               e.getMessage(),
                       e);
        }
        return 0;
    }

    /**
     *
     * @param sideX
     * @param pressureClass
     * @param jointSpacing
     * @param transverseConnection
     * @param RSequalsJS
     * @return String
     */
    public String calculationForTtenToTtwelve(int sideX,
                                              int pressureClass,
                                              float jointSpacing,
                                              int transverseConnection,
                                              boolean RSequalsJS,
                                              RectDuctRenfService renfService) {
        logg.info("[calculationForTtenToTtwelve] : sideX = " + sideX +
                ", pressureClass = " + pressureClass + ", jointSpacing = " +
                jointSpacing + ", transverseConnection = " +
                transverseConnection);
        try {
            return renfService
                    .calculationForTtenToTtwelve(pressureClass,
                                                 sideX,
                                                 jointSpacing,
                                                 transverseConnection,
                                                 RSequalsJS);
        } catch (SmacnaException e) {
            logg.error("[calculationForTtenToTtwelve] : Exception Occurred: " +
                    e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logg.error("[calculationForTtenToTtwelve] : Exception Occurred: " +
                    e.getMessage(), e);
        }

        return null;
    }

    /**
     *
     * @param pressureClass
     * @return Longitudinal Seam
     *
     *         This method is use to get the information about Longitudinal
     *         Seam.
     */
    @Autowired
    private MessageSource messageSource;

    public String getLongitudinalSeam(int pressureClass,
                                      TransverseConnectionService connectionService) {

        double pc = 0.0;

        String longitudinalSeam;
        try {
            pc = connectionService.getStaticPC(pressureClass);
            longitudinalSeam = null;
            if (pc <= 4) {
                longitudinalSeam = messageSource.getMessage("message.longitudinalSeam1", null, Locale.ENGLISH);
                logg.info("[getLongitudinalSeam]: longitudinalSeam for pc<=4" + longitudinalSeam);

            } else if (pressureClass > 4) {
                longitudinalSeam = messageSource.getMessage("message.longitudinalSeam2", null, Locale.ENGLISH);
                logg.info("[getLongitudinalSeam]: longitudinalSeam for pc>4" + longitudinalSeam);
            }
            return longitudinalSeam;
        } catch (SmacnaException e) {
            logg.error("[getLongitudinalSeam] : Exception Occurred: " +
                    e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logg.error("[getLongitudinalSeam] : Exception Occurred: " +
                               e.getMessage(),
                       e);
        }
        return null;
    }

    public abstract List<OutPutScreen> getOptions(InputScreen inputScreen,
                                         int sides[],
                                         RectDuctRenfService renfService,
                                         MidPanelTieRodService tieRodService,
                                         TransverseConnectionService connectionService) throws SmacnaException;

}
