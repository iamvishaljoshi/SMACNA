package com.smacna.util;

import java.text.DecimalFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author vishal.joshi
 * @version 1.0
 * This class contains the calculations which are required
 * while generation the options
 */
public class SmacnaUtils {

    private static final Log logg = LogFactory.getLog(SmacnaUtils.class);

    /**
     *
     * @param sideX
     * @param renforcementSpacing
     * @param noOfTieRod
     * @param pressureClass
     * @return double value which will hold the Tie Rod Load.
     * This function calculates the Tie Rod Load.
     */
    public static double getTieRodLoad(int sideX, float renforcementSpacing,
                                       int noOfTieRod, float pressureClass) {
        logg.info("[getTieRodLoad] : sideX = " + sideX
                  + ", renforcementSpacing = " + renforcementSpacing
                  + ", noOfTieRodLoad = " + noOfTieRod+", pressureClass = "+pressureClass);
        double tieRodLoad = ((renforcementSpacing * sideX)/144
                * pressureClass * 5.2) / noOfTieRod;
        System.out.println(" Tie Rod Load A: " + tieRodLoad);
        DecimalFormat df =  new DecimalFormat("#.##");
        return Math.ceil(Double.parseDouble( df.format(tieRodLoad)));
    }
    /**
     *
     * @param sideX
     * @param renforcementSpacing
     * @param noOfTieRod
     * @param pressureClass
     * @return double value which will hold the Tie Rod Load of JTR.
     * This function calculates the JTR Load.
     */
    public static double getTieRodLoadForJTR(int sideX, float renforcementSpacing,
                                             int noOfTieRod, float pressureClass){
        logg.info("[getTieRodLoadForJTR] : sideX = " + sideX
                  + ", renforcementSpacing = " + renforcementSpacing
                  + ", noOfTieRodLoad = " + noOfTieRod+", pressureClass = "+pressureClass);
		double tieRodLoad = (((renforcementSpacing * (float) sideX) / 144
				* pressureClass * 5.2) * 0.75) * 0.5;
        System.out.println(" Tie Rod Load B: " + tieRodLoad);
        DecimalFormat df =  new DecimalFormat("#.##");
        return Math.ceil(Double.parseDouble( df.format(tieRodLoad)));
    }

    /**
     * @param sideX
     * @param renforcementSpacing
     * @param noOfTieRod
     * @param pressureClass
     * @return double value which will hold the Tie Rod Load of JTR.
     * This function calculates the JTR Load in case of T with the side.
     */
    public static double getTieRodLoadWithT(int sideX, float renforcementSpacing,
                                       int noOfTieRod, float pressureClass) {

        logg.info("[getTieRodLoadWithT] : sideX = " + sideX
                  + ", renforcementSpacing = " + renforcementSpacing
                  + ", noOfTieRodLoad = " + noOfTieRod+", pressureClass = "+pressureClass);
        double tieRodLoad = (((renforcementSpacing * sideX)/144
                * pressureClass * 5.2) / noOfTieRod) * 0.75;
        System.out.println(" Tie Rod Load C: " + tieRodLoad);
        DecimalFormat df =  new DecimalFormat("#.##");
        return Math.ceil(Double.parseDouble( df.format(tieRodLoad)));
    }

    /**
     *
     * @param tieRodLoad
     * @return String which will hold the EMT shape and size value
     * This function generates the EMT in case of positive selection
     */
    public static String getEMT(double tieRodLoad) {

        String result = null;
        if (tieRodLoad <= 900) {

            result = "1/2 inch";
        } else if (tieRodLoad <= 1340) {
            result = "3/4 inch";
        } else if (tieRodLoad <= 1980) {
            result = "2 inch";
        } else if (tieRodLoad > 1980) {
            result = "error";
        }

        return result;
    }

    /**
     *
     * @param EMT
     * @return String poundDetails
     */
    public static String getPoundsDetails(String EMT) {
        String result = null;
        if (EMT.equals("1/2 EMT")) {
            result = "up to 900";
        } else if (EMT.equals("3/4 EMT")) {
            result = "between 901 and 1340";
        } else if (EMT.equals("2 EMT")) {
            result = "between 1342 and 1980";
        }
        return result;

    }

    public static void main(String[] args) {
        //getTieRodLoad(29, 6 / 2F, 1, 5);
        float i =(4 * 12 - 4 )/ 2F;
        System.out.println(i);
        System.out.println(getTieRodLoad(28, ((5*12)), 1, 3));
        System.out.println((int)(Double.parseDouble("4.55")*100/100.00));
        double d = Double.parseDouble("249.60000000000002");int r = (int)(d * 100);double f = r / 100.0;System.out.println(f);
    }

}
