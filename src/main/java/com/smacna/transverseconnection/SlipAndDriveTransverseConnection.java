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

/**
 * @author sumit.v
 * This class generate the option for the combination of T1 and T5
 * as Transverse connections
 */
@Service("slipAndDriveTranConn")
public class SlipAndDriveTransverseConnection extends
        TransverseConnections {

    public static final Log logg = LogFactory.getLog(SlipAndDriveTransverseConnection.class);
    @Override
    public List<OutPutScreen> getOptions(InputScreen inputScreen,
                                         int sides[],
                                         RectDuctRenfService renfService,
                                         MidPanelTieRodService tieRodService,
                                         TransverseConnectionService connectionService)throws SmacnaException {
        logg.info("[getOptions]:");
        List<OutPutScreen> outPutScreens = new ArrayList<OutPutScreen>();
        // Side one Calculation
        MDGS1 =
                calculateSide(sides[0],
                              inputScreen.getPressureClass(),
                              inputScreen.getJointSpacing(),
                              inputScreen.getTransConnS1(),
                              renfService);

        // Side two Calculation
        MDGS2 =
                calculateSide(sides[1],
                              inputScreen.getPressureClass(),
                              inputScreen.getJointSpacing(),
                              inputScreen.getTransConnS2(),
                              renfService);

        OutPutScreen outPutScreen = new OutPutScreen();

        outPutScreen.setExternalReinforcement(false);
        outPutScreen.setInternalReinforcement(false);
        outPutScreen.setNoRenforcement(true);
        outPutScreen.setExternalRenfAngle("");
        FDG = Math.min(MDGS1, MDGS2);
        outPutScreen.setMinDuctGageSOne(MDGS1);
        outPutScreen.setMinDuctGageSTwo(MDGS2);
        outPutScreen.setFinalDuctGage(FDG);
        longitudinalSeam =
                getLongitudinalSeam(inputScreen.getPressureClass(),
                                    connectionService);
        outPutScreen.setLongitudinalSeam(longitudinalSeam);
        outPutScreen.setNoTieRodForSideOne(0);
        outPutScreen.setNoTieRodForSideTwo(0);
        outPutScreen.setReinforcementClass("");
        outPutScreen.setTieRod("");
        outPutScreen.setTieRodLoadForSideOne(0.0);
        outPutScreen.setSideOne(sides[0]);
        outPutScreen.setSideTwo(sides[1]);

        logg.debug("[addCalculation] : Minimum Duct Gage for side one = " +
                MDGS1 + ", and for side two = " + MDGS2 + "");
        outPutScreens.add(outPutScreen);

        if (FDG == 0) {
            return null;
        }
        inputScreen.setDesignAvailable("");
        return outPutScreens;
    }

}
