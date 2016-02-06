/**
 *
 */
package com.smacna.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.smacna.constants.Constants;
import com.smacna.form.ContactDTO;
import com.smacna.model.InputScreen;
import com.smacna.model.OutPutScreen;
import com.smacna.service.TransverseConnectionService;

/**
 * @author sumit.v This utility class generates the content that should be
 *         written in the pdf of the fabrication report according to the
 *         selected option.
 */
public class FabricationReportGenerator {

	@Autowired
	private TransverseConnectionService connectionService;


	private static final Log logg = LogFactory
			.getLog(FabricationReportGenerator.class);

	/**
	 * @param path
	 * @param inputScreen
	 * @param outPutScreen
	 * @param contact
	 * @param date
	 *            The function generates the content of the fabrication report
	 */
	public static void generatePdf(String path, InputScreen inputScreen,
			OutPutScreen outPutScreen, ContactDTO contact, String date, HttpServletRequest request) {
		String user_name = "", company_name = "", posOrNeg;
		int height = 0, width = 0, joint_spacing = 0, pressure;
		if(!contact.getUserName().contains("User Name (Optional)"))
		{
			user_name = contact.getUserName();
		}
		if(!contact.getCompany().contains("Company (Optional)"))
		{
			company_name = contact.getCompany();
		}

		try {
			Document document = new Document();
			OutputStream file = new FileOutputStream(new File(path
					+ File.separator + "Fabrication_Report" + date + ".pdf"));
			PdfWriter writer = PdfWriter.getInstance(document, file);
			writer.setPdfVersion(PdfWriter.VERSION_1_6);
			document.open();

			WebApplicationContext webAppContext = RequestContextUtils.getWebApplicationContext(request);
	        MessageSource messageSource = (MessageSource)webAppContext.getBean("emailMessageSource");
	        String appUrl = messageSource.getMessage("application.url", null, Locale.ENGLISH);
			String logoUrl = messageSource.getMessage("logoUrl", null, Locale.ENGLISH);
			Image image = Image.getInstance(new URL(appUrl + logoUrl));
			image.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
			document.add(image);

			PdfContentByte cb = writer.getDirectContent();

			String connection1 = String.valueOf(inputScreen.getTransConnS1());
			String connection2 = String.valueOf(inputScreen.getTransConnS2());
			height = Integer.parseInt(inputScreen.getHeight());
			width = Integer.parseInt(inputScreen.getWidth());
			joint_spacing = inputScreen.getJointSpacing();
			pressure = inputScreen.getPressureClass();
			String pressCls = null;
			if (pressure == 1) {
				pressCls = "1/2";
			}
			if (pressure == 2) {
				pressCls = "1";
			}
			if (pressure == 3) {
				pressCls = "2";
			}
			if (pressure == 4) {
				pressCls = "3";
			}
			if (pressure == 5) {
				pressCls = "4";
			}
			if (pressure == 6) {
				pressCls = "6";
			}
			if (pressure == 7) {
				pressCls = "10";
			}
			if (inputScreen.isPosOrNeg()) {
				posOrNeg = "positive";
			} else {
				posOrNeg = "negative";
			}

			if (inputScreen.getTransConnS1() == 1) {
				connection1 = "T-1 Flat Drive";
			}
			if (inputScreen.getTransConnS1() == 2) {
				connection1 = "T-5 Flat Slip";
			}
			if (inputScreen.getTransConnS1() == 3) {
				connection1 = "T-10 Standing Slip";
			}
			if (inputScreen.getTransConnS1() == 4) {
				connection1 = "T-11 Standing Slip";
			}
			if (inputScreen.getTransConnS1() == 5) {
				connection1 = "T-12 Standing Slip";
			}
			if (inputScreen.getTransConnS1() == 6) {
				connection1 = "T25 a/b TDC/TDF";
			}
			if (inputScreen.getTransConnS2() == 1) {
				connection2 = "T-1 Flat Drive";
			}
			if (inputScreen.getTransConnS2() == 2) {
				connection2 = "T-5 Flat Slip";
			}
			if (inputScreen.getTransConnS2() == 3) {
				connection2 = "T-10 Standing Slip";
			}
			if (inputScreen.getTransConnS2() == 4) {
				connection2 = "T-11 Standing Slip";
			}
			if (inputScreen.getTransConnS2() == 5) {
				connection2 = "T-12 Standing Slip";
			}
			if (inputScreen.getTransConnS2() == 6) {
				connection2 = "T25 a/b TDC/TDF";
			}
			StringBuffer fabrication_report1 = new StringBuffer();
			fabrication_report1.append("Your duct that is ");
			int[] duct = DuctSides.getSides(height, width);
			fabrication_report1.append(String.valueOf(duct[0]) + " inches");
			fabrication_report1.append(" x ");
			fabrication_report1.append(String.valueOf(duct[1]) + " inches");
			fabrication_report1.append(" and nominally ");
			fabrication_report1.append(String.valueOf(joint_spacing));
			fabrication_report1.append(" ft long for ");
			fabrication_report1.append(posOrNeg);
			fabrication_report1.append(" pressure of ");
			fabrication_report1.append(pressCls);
			fabrication_report1.append(" in. water, ");
			fabrication_report1.append("column can be fabricated from:\n Use ");
			fabrication_report1.append(outPutScreen.getFinalDuctGage());
			fabrication_report1.append(" gage or heavier for the duct");

			if (outPutScreen.isExtAndInternalEachSide()) {
				fabrication_report1
						.append(", add an External reinforcement on side ");
				fabrication_report1.append(duct[0]);
				fabrication_report1.append(" inches");
				if (outPutScreen.isJtrForSideOne()) {
					fabrication_report1.append(" with JTR ");
				}

				fabrication_report1.append("\n");
				fabrication_report1.append("\t 1.) Reinforcement Class: ");
				fabrication_report1.append(outPutScreen
						.getReinClassForSideOne());
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 2.) Reinforcement Angle: ");
				fabrication_report1.append(outPutScreen
						.getExtRenfAngleSideOne());
				if (outPutScreen.isJtrForSideOne()) {
					fabrication_report1.append("\n");
					fabrication_report1
							.append("\t 3.) JTR Load: "
									+ ((int) (outPutScreen
											.getTieRoadLoadJTRForSideOne() * 100) / 100.0)
									+ " lbs");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 4.) JTR Size: "
							+ outPutScreen.getJtrSizeForS1());
					fabrication_report1.append(" EMT");
				}
				fabrication_report1.append("\n");
				fabrication_report1
						.append("and add an Internal reinforcement on side ");
				fabrication_report1.append(duct[1]);
				fabrication_report1.append(" inches");
				if (outPutScreen.isJtrForSideTwo()) {
					fabrication_report1.append(" with JTR ");
				}
				fabrication_report1.append("\n");
				if (outPutScreen.getNoTieRodForSideTwo() != 0) {

					fabrication_report1.append("\t 1.) Number of MPT: ");
					fabrication_report1.append(outPutScreen
							.getNoTieRodForSideTwo());
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 2.) MPT Load: ");
					fabrication_report1.append((int) (outPutScreen
							.getTieRodLoadForSideTwo() * 100) / 100.0 + " lbs");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 3.) Use: ");
					if (inputScreen.isPosOrNeg()) {
						fabrication_report1
								.append(outPutScreen.getTieRodForSideTwo()
										+ " EMT which is good for "
										+ ((int) (outPutScreen
												.getTieRodLoadForSideTwo() * 100) / 100.0)
										+ " pounds.");
					} else {
						fabrication_report1
								.append(outPutScreen.getTieRodForSideTwo()
										+ " EMT which is good for "
										+ ((int) (outPutScreen
												.getTieRodLoadForSideTwo() * 100) / 100.0)
										+ " pounds.");
					}
					fabrication_report1.append("\n");
					if (outPutScreen.isJtrForSideTwo()) {
						fabrication_report1
								.append("\t 4.) JTR Load: "
										+ ((int) (outPutScreen
												.getTieRoadLoadJTRForSideTwo() * 100) / 100.0)
										+ " lbs");
						fabrication_report1.append("\n");
						fabrication_report1.append("\t 5.) JTR Size: "
								+ outPutScreen.getJtrSizeForS2());
						fabrication_report1.append(" EMT");
					}
				} else {
					if (outPutScreen.isJtrForSideTwo()) {
						fabrication_report1
								.append("\t 1.) JTR Load: "
										+ ((int) (outPutScreen
												.getTieRoadLoadJTRForSideTwo() * 100) / 100.0));
						fabrication_report1.append("\n");
						fabrication_report1.append("\t 2.) JTR Size: "
								+ outPutScreen.getJtrSizeForS2());
						fabrication_report1.append(" EMT");
					}
				}
				if (outPutScreen.getDuctGageForSideOne() != null) {
					fabrication_report1.append("\n\n The ");
					fabrication_report1.append(connection1);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[0]));
					if (inputScreen.getTransConnS1() == 6) {
						fabrication_report1.append(" inches side\n ");
					} else {
						fabrication_report1.append(" inches side must be ");
						if ((inputScreen.getTransConnS1() == 3
								|| inputScreen.getTransConnS1() == 4 || inputScreen
								.getTransConnS1() == 5)
								&& outPutScreen.getReinClassForSideOne() != null) {
							fabrication_report1.append("a class ");
							fabrication_report1.append(outPutScreen
									.getReinClassForSideOne());
							fabrication_report1.append(" which is ");
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideOne());
							fabrication_report1.append(" or heavier\n ");
						} else {
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideOne());
							fabrication_report1.append(" or heavier\n ");
						}
					}
				}

				else {
					if (inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T25) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append(" The ");
						fabrication_report1.append(connection1);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n ");
					}
				}
				if (outPutScreen.getDuctGageForSideTwo() != null) {
					fabrication_report1.append("The ");
					fabrication_report1.append(connection2);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[1]));
					if (inputScreen.getTransConnS2() == 6) {
						fabrication_report1.append(" inches side\n ");
					} else {
						fabrication_report1.append(" inches side must be ");
						if ((inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T10
								|| inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T11 || inputScreen
								.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T12)
								&& outPutScreen.getReinClassForSideTwo() != null) {
							fabrication_report1.append("a class ");
							fabrication_report1.append(outPutScreen
									.getReinClassForSideTwo());
							fabrication_report1.append(" which is ");
						}
						fabrication_report1.append(outPutScreen
								.getDuctGageForSideTwo());
						fabrication_report1.append(" or heavier\n ");
					}
				} else {
					if (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T25) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append("The ");
						fabrication_report1.append(connection2);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n  ");
					}

				}
			}

			else if (outPutScreen.isIntAndExtOnSideOne()
					&& outPutScreen.isExternalReinforcement()) {
				fabrication_report1
						.append(", add external and internal reinforcement on side ");
				fabrication_report1.append(duct[0] + " inches");
				if (outPutScreen.isJtrForSideOne()) {
					fabrication_report1.append(" with JTR");
				}
				fabrication_report1.append("\n");
				fabrication_report1.append("External Reinforcement : ");
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 1.) Reinforcement Class: ");
				fabrication_report1.append(outPutScreen
						.getReinClassForSideOne());
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 2.) Reinforcement Angle: ");
				fabrication_report1.append(outPutScreen
						.getExtRenfAngleSideOne());
				fabrication_report1.append("\n");
				fabrication_report1.append("Internal Reinforcement : ");
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 1.) Number of MPT: ");
				fabrication_report1
						.append(outPutScreen.getNoTieRodForSideOne());
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 2.) MPT Load: ");
				fabrication_report1.append((int) (outPutScreen
						.getTieRodLoadForSideOne() * 100) / 100.0 + " lbs");
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 3.) Use: ");
				if (inputScreen.isPosOrNeg()) {
					fabrication_report1
							.append(outPutScreen.getTieRodForSideOne()
									+ " EMT which is good for "
									+ (int) (outPutScreen
											.getTieRodLoadForSideOne() * 100)
									/ 100.0 + " pounds.");
				} else {
					fabrication_report1
							.append(outPutScreen.getTieRodForSideOne()
									+ " EMT which is good for "
									+ (int) (outPutScreen
											.getTieRodLoadForSideOne() * 100)
									/ 100.0 + " pounds.");
				}
				if (outPutScreen.isJtrForSideOne()) {
					fabrication_report1.append("\n");
					fabrication_report1
							.append("\t 4.) JTR Load: "
									+ (int) (outPutScreen
											.getTieRoadLoadJTRForSideOne() * 100)
									/ 100.0 + " lbs");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 5.) JTR Size: "
							+ outPutScreen.getJtrSizeForS1());
					fabrication_report1.append(" EMT");
					fabrication_report1.append("\n");
				}
				fabrication_report1.append("\n");
				fabrication_report1
						.append("and add an External reinforcement on side ");
				fabrication_report1.append(duct[1] + " inches");
				if (outPutScreen.isJtrForSideTwo()) {
					fabrication_report1.append(" with JTR ");
				}
				fabrication_report1.append("\n");

				fabrication_report1.append("\t 1.) Reinforcement Class: ");
				fabrication_report1.append(outPutScreen
						.getReinClassForSideTwo());
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 2.) Reinforcement Angle: ");
				fabrication_report1.append(outPutScreen
						.getExtRenfAngleSideTwo());

				if (outPutScreen.isJtrForSideTwo()) {
					fabrication_report1.append("\n");
					fabrication_report1
							.append("\t 3.) JTR Load: "
									+ (int) (outPutScreen
											.getTieRoadLoadJTRForSideTwo() * 100)
									/ 100.0 + " lbs");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 4.) JTR Size: "
							+ outPutScreen.getJtrSizeForS2());
					fabrication_report1.append(" EMT");
				}
				if (outPutScreen.getDuctGageForSideOne() != null) {
					fabrication_report1.append("\n\n The ");
					fabrication_report1.append(connection1);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[0]));
					if (inputScreen.getTransConnS1() == 6) {
						fabrication_report1.append(" inches side\n ");
					} else {
						fabrication_report1.append(" inches side must be ");
						if ((inputScreen.getTransConnS1() == 3
								|| inputScreen.getTransConnS1() == 4 || inputScreen
								.getTransConnS1() == 5)
								&& outPutScreen.getReinClassForSideOne() != null) {
							fabrication_report1.append("a class ");
							fabrication_report1.append(outPutScreen
									.getReinClassForSideOne());
							fabrication_report1.append(" which is ");
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideOne());
							fabrication_report1.append(" or heavier\n ");
						} else {
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideOne());
							fabrication_report1.append(" or heavier\n ");
						}
					}
				}

				else {
					if (inputScreen.getTransConnS1() == 6) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append(" The ");
						fabrication_report1.append(connection1);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n ");
					}
				}
				if (outPutScreen.getDuctGageForSideTwo() != null) {
					fabrication_report1.append("The ");
					fabrication_report1.append(connection2);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[1]));
					if (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T25) {
						fabrication_report1.append(" inches side\n ");
					} else {
						fabrication_report1.append(" inches side must be ");
						if ((inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T10
								|| inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T11 || inputScreen
								.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T12)
								&& outPutScreen.getReinClassForSideTwo() != null) {
							fabrication_report1.append("a class ");
							fabrication_report1.append(outPutScreen
									.getReinClassForSideTwo());
							fabrication_report1.append(" which is ");
						}
						fabrication_report1.append(outPutScreen
								.getDuctGageForSideTwo());
						fabrication_report1.append(" or heavier\n ");
					}
				} else {
					if (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T25) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append("The ");
						fabrication_report1.append(connection2);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n  ");
					}

				}
			} else if (outPutScreen.isIntAndExtOnSideOne()
					&& outPutScreen.isInternalReinforcement()) {
				fabrication_report1
						.append(", add external and internal reinforcement on side ");
				fabrication_report1.append(duct[0] + " inches");
				if (outPutScreen.isJtrForSideOne()) {
					fabrication_report1.append(" with JTR");
				}
				fabrication_report1.append("\n");
				fabrication_report1.append("External Reinforcement : ");
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 1.) Reinforcement Class: ");
				fabrication_report1.append(outPutScreen
						.getReinClassForSideOne());
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 2.) Reinforcement Angle: ");
				fabrication_report1.append(outPutScreen
						.getExtRenfAngleSideOne());
				fabrication_report1.append("\n");
				fabrication_report1.append("Internal Reinforcement : ");
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 1.) Number of MPT: ");
				fabrication_report1
						.append(outPutScreen.getNoTieRodForSideOne());
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 2.) MPT Load: ");
				fabrication_report1.append((int) (outPutScreen
						.getTieRodLoadForSideOne() * 100) / 100.0 + " lbs");
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 3.) Use: ");
				if (inputScreen.isPosOrNeg()) {
					fabrication_report1
							.append(outPutScreen.getTieRodForSideOne()
									+ " EMT which is good for "
									+ (int) (outPutScreen
											.getTieRodLoadForSideOne() * 100)
									/ 100.0 + " pounds.");
				} else {
					fabrication_report1
							.append(outPutScreen.getTieRodForSideOne()
									+ " EMT which is good for "
									+ (int) (outPutScreen
											.getTieRodLoadForSideOne() * 100)
									/ 100.0 + " pounds.");
				}
				if (outPutScreen.isJtrForSideOne()) {
					fabrication_report1.append("\n");
					fabrication_report1
							.append("\t 4.) JTR Load: "
									+ (int) (outPutScreen
											.getTieRoadLoadJTRForSideOne() * 100)
									/ 100.0 + " lbs");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 5.) JTR Size: "
							+ outPutScreen.getJtrSizeForS1());
					fabrication_report1.append(" EMT");
					fabrication_report1.append("\n");
				}
				fabrication_report1.append("\n");
				fabrication_report1
						.append("and add an Internal reinforcement on side "
								+ duct[1] + " inches");
				if (outPutScreen.isJtrForSideTwo()) {
					fabrication_report1.append(" with JTR");
				}
				fabrication_report1.append("\n");
				if (outPutScreen.getNoTieRodForSideTwo() != 0) {
					fabrication_report1.append("\t 1.) Number of MPT: ");
					fabrication_report1.append(outPutScreen
							.getNoTieRodForSideTwo());
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 2.) MPT Load: ");
					fabrication_report1.append((int) (outPutScreen
							.getTieRodLoadForSideTwo() * 100) / 100.0 + " lbs");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 3.) Use: ");
					if (inputScreen.isPosOrNeg()) {
						fabrication_report1
								.append(outPutScreen.getTieRodForSideTwo()
										+ " EMT which is good for "
										+ (int) (outPutScreen
												.getTieRodLoadForSideTwo() * 100)
										/ 100.0 + " pounds.");
					} else {
						fabrication_report1
								.append(outPutScreen.getTieRodForSideTwo()
										+ " EMT which is good for "
										+ (int) (outPutScreen
												.getTieRodLoadForSideTwo() * 100)
										/ 100.0 + " pounds.");
					}
					fabrication_report1.append("\n");
					if (outPutScreen.isJtrForSideTwo()) {
						fabrication_report1
								.append("\t 4.) JTR Load: "
										+ ((int) (outPutScreen
												.getTieRoadLoadJTRForSideTwo() * 100) / 100.0)
										+ " lbs");
						fabrication_report1.append("\n");
						fabrication_report1.append("\t 5.) JTR Size: "
								+ outPutScreen.getJtrSizeForS2());
						fabrication_report1.append(" EMT");
						fabrication_report1.append("\n");
					}
				} else {
					if (outPutScreen.isJtrForSideTwo()) {
						fabrication_report1
								.append("\t 1.) JTR Load: "
										+ ((int) (outPutScreen
												.getTieRoadLoadJTRForSideTwo() * 100) / 100.0)
										+ " lbs");
						fabrication_report1.append("\n");
						fabrication_report1.append("\t 2.) JTR Size: "
								+ outPutScreen.getJtrSizeForS2());
						fabrication_report1.append(" EMT");
						fabrication_report1.append("\n");
					}
				}
				if (outPutScreen.getDuctGageForSideOne() != null) {
					fabrication_report1.append("\n\n The ");
					fabrication_report1.append(connection1);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[0]));
					if (inputScreen.getTransConnS1() == 6) {
						fabrication_report1.append(" inches side\n ");
					} else {
						fabrication_report1.append(" inches side must be ");
						if ((inputScreen.getTransConnS1() == 3
								|| inputScreen.getTransConnS1() == 4 || inputScreen
								.getTransConnS1() == 5)
								&& outPutScreen.getReinClassForSideOne() != null) {
							fabrication_report1.append("a class ");
							fabrication_report1.append(outPutScreen
									.getReinClassForSideOne());
							fabrication_report1.append(" which is ");
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideOne());
							fabrication_report1.append(" or heavier\n ");
						} else {
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideOne());
							fabrication_report1.append(" or heavier\n ");
						}
					}
				}

				else {
					if (inputScreen.getTransConnS1() == 6) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append("\n\n The ");
						fabrication_report1.append(connection1);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n  ");
					}
				}
				if (outPutScreen.getDuctGageForSideTwo() != null) {
					fabrication_report1.append("The ");
					fabrication_report1.append(connection2);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[1]));
					if (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T25) {
						fabrication_report1.append(" inches side\n ");
					} else {
						fabrication_report1.append(" inches side must be ");
						if ((inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T10
								|| inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T11 || inputScreen
								.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T12)
								&& outPutScreen.getReinClassForSideTwo() != null) {
							fabrication_report1.append("a class ");
							fabrication_report1.append(outPutScreen
									.getReinClassForSideTwo());
							fabrication_report1.append(" which is ");
						}
						fabrication_report1.append(outPutScreen
								.getDuctGageForSideTwo());
						fabrication_report1.append(" or heavier\n ");
					}
				} else {
					if (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T25) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append("The ");
						fabrication_report1.append(connection2);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n  ");
					}

				}
			} else if (outPutScreen.isIntAndExtEachSide()) {
				fabrication_report1
						.append(", add an Internal reinforcement on side ");
				fabrication_report1.append(duct[0] + " inches");
				if (outPutScreen.isJtrForSideOne()) {
					fabrication_report1.append(" with JTR ");
				}
				fabrication_report1.append("\n");
				if (outPutScreen.getNoTieRodForSideOne() != 0) {
					fabrication_report1.append("\t 1.) Number of MPT: ");
					fabrication_report1.append(outPutScreen
							.getNoTieRodForSideOne());
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 2.) MPT Load: ");
					fabrication_report1.append((int) (outPutScreen
							.getTieRodLoadForSideOne() * 100) / 100.0 + " lbs");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 3.)");
					fabrication_report1.append(" Use: ");
					fabrication_report1
							.append(outPutScreen.getTieRodForSideOne()
									+ " EMT which is good for "
									+ ((int) (outPutScreen
											.getTieRodLoadForSideOne() * 100) / 100.0)
									+ " pounds.");
					fabrication_report1.append("\n");
					if (outPutScreen.isJtrForSideOne()) {
						fabrication_report1.append("\t 4.) JTR Load: "
								+ (int) (outPutScreen
										.getTieRoadLoadJTRForSideOne() * 100)
								/ 100.0 + " lbs");
						fabrication_report1.append("\n");
						fabrication_report1.append("\t 5.) JTR Size: "
								+ outPutScreen.getJtrSizeForS1());
						fabrication_report1.append(" EMT");
						fabrication_report1.append("\n");
					}

				} else {
					if (outPutScreen.isJtrForSideOne()) {
						fabrication_report1.append("\n");
						fabrication_report1.append("\t 1.) JTR Load: "
								+ (int) (outPutScreen
										.getTieRoadLoadJTRForSideOne() * 100)
								/ 100.0 + " lbs");
						fabrication_report1.append("\n");
						fabrication_report1.append("\t 2.) JTR Size: "
								+ outPutScreen.getJtrSizeForS1());
						fabrication_report1.append(" EMT");
						fabrication_report1.append("\n");
					}
				}
				fabrication_report1.append("\n");
				fabrication_report1
						.append("and add an External reinforcement on side ");
				fabrication_report1.append(duct[1] + " inches");
				if (outPutScreen.isJtrForSideTwo()) {
					fabrication_report1.append(" with JTR ");
				}
				fabrication_report1.append("\n");

				fabrication_report1.append("\t 1.) Reinforcement Class: ");
				fabrication_report1.append(outPutScreen
						.getReinClassForSideTwo());
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 2.) Reinforcement Angle: ");
				fabrication_report1.append(outPutScreen
						.getExtRenfAngleSideTwo());

				if (outPutScreen.isJtrForSideTwo()) {
					fabrication_report1.append("\n");
					fabrication_report1
							.append("\t 3.) JTR Load: "
									+ (int) (outPutScreen
											.getTieRoadLoadJTRForSideTwo() * 100)
									/ 100.0 + " lbs");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 4.) JTR Size: "
							+ outPutScreen.getJtrSizeForS2());
					fabrication_report1.append(" EMT");
				}
				if (outPutScreen.getDuctGageForSideOne() != null) {
					fabrication_report1.append("\n\n The ");
					fabrication_report1.append(connection1);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[0]));
					if (inputScreen.getTransConnS1() == 6) {
						fabrication_report1.append(" inches side\n ");
					} else {
						fabrication_report1.append(" inches side must be ");
						if ((inputScreen.getTransConnS1() == 3
								|| inputScreen.getTransConnS1() == 4 || inputScreen
								.getTransConnS1() == 5)
								&& outPutScreen.getReinClassForSideOne() != null) {
							fabrication_report1.append("a class ");
							fabrication_report1.append(outPutScreen
									.getReinClassForSideOne());
							fabrication_report1.append(" which is ");
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideOne());
							fabrication_report1.append(" or heavier\n ");
						} else {
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideOne());
							fabrication_report1.append(" or heavier\n ");
						}
					}
				} else {
					if (inputScreen.getTransConnS1() == 6) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append("\n\n The ");
						fabrication_report1.append(connection1);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n  ");
					}

				}

				if (outPutScreen.getDuctGageForSideTwo() != null) {
					fabrication_report1.append("The ");
					fabrication_report1.append(connection2);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[1]));
					if (inputScreen.getTransConnS2() == 6) {
						fabrication_report1.append(" inches side\n ");
					} else {
						fabrication_report1.append(" inches side must be ");
						if ((inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T10
								|| inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T11 || inputScreen
								.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T12)
								&& outPutScreen.getReinClassForSideTwo() != null) {
							fabrication_report1.append("a class ");
							fabrication_report1.append(outPutScreen
									.getReinClassForSideTwo());
							fabrication_report1.append(" which is ");
						}
						fabrication_report1.append(outPutScreen
								.getDuctGageForSideTwo());
						fabrication_report1.append(" or heavier\n ");
					}
				}

				else {
					if (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T25) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append("The ");
						fabrication_report1.append(connection2);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n  ");
					}
				}
			}

			else if (outPutScreen.isInternalBothSide()) {

				fabrication_report1
						.append(", add an Internal reinforcement on side ");
				fabrication_report1.append(outPutScreen.getExtRenfSide());
				fabrication_report1.append(" inches");
				if (outPutScreen.isJtrForSideOne()) {
					fabrication_report1.append(" with JTR");
				}
				fabrication_report1.append("\n");
				if (outPutScreen.getNoTieRodForSideOne() != 0) {
					fabrication_report1.append("\t 1.) Number of MPT: ");
					fabrication_report1.append(outPutScreen
							.getNoTieRodForSideOne());
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 2.) MPT Load: ");
					fabrication_report1.append((int) (outPutScreen
							.getTieRodLoadForSideOne() * 100) / 100.0 + " lbs");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 3.) Use: ");
					if (inputScreen.isPosOrNeg()) {
						fabrication_report1
								.append(outPutScreen.getTieRodForSideOne()
										+ " EMT which is good for "
										+ (int) (outPutScreen
												.getTieRodLoadForSideOne() * 100)
										/ 100.0 + " pounds.");
					} else {
						fabrication_report1
								.append(outPutScreen.getTieRodForSideOne()
										+ " EMT which is good for "
										+ (int) (outPutScreen
												.getTieRodLoadForSideOne() * 100)
										/ 100.0 + " pounds.");
					}
					fabrication_report1.append("\n");
					if (outPutScreen.isJtrForSideOne()) {
						fabrication_report1.append("\t 4.) JTR Load: "
								+ (int) (outPutScreen
										.getTieRoadLoadJTRForSideOne() * 100)
								/ 100.0 + " lbs");
						fabrication_report1.append("\n");
						fabrication_report1.append("\t 5.) JTR Size: "
								+ outPutScreen.getJtrSizeForS1());
						fabrication_report1.append(" EMT");
						fabrication_report1.append("\n");
					}
				} else {
					if (outPutScreen.isJtrForSideOne()) {
						fabrication_report1.append("\t 1.) JTR Load: "
								+ (int) (outPutScreen
										.getTieRoadLoadJTRForSideOne() * 100)
								/ 100.0 + " lbs");
						fabrication_report1.append("\n");
						fabrication_report1.append("\t 2.) JTR Size: "
								+ outPutScreen.getJtrSizeForS1());
						fabrication_report1.append(" EMT");
						fabrication_report1.append("\n");
					}
				}
				fabrication_report1
						.append("and add an Internal reinforcement on side "
								+ duct[1] + " inches");
				if (outPutScreen.isJtrForSideTwo()) {
					fabrication_report1.append(" with JTR");
				}
				fabrication_report1.append("\n");
				if (outPutScreen.getNoTieRodForSideTwo() != 0) {
					fabrication_report1.append("\t 1.) Number of MPT: ");
					fabrication_report1.append(outPutScreen
							.getNoTieRodForSideTwo());
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 2.) MPT Load: ");
					fabrication_report1.append((int) (outPutScreen
							.getTieRodLoadForSideTwo() * 100) / 100.0 + " lbs");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 3.) Use: ");
					if (inputScreen.isPosOrNeg()) {
						fabrication_report1
								.append(outPutScreen.getTieRodForSideTwo()
										+ " EMT which is good for "
										+ (int) (outPutScreen
												.getTieRodLoadForSideTwo() * 100)
										/ 100.0 + " pounds.");
					} else {
						fabrication_report1
								.append(outPutScreen.getTieRodForSideTwo()
										+ " EMT which is good for "
										+ (int) (outPutScreen
												.getTieRodLoadForSideTwo() * 100)
										/ 100.0 + " pounds.");
					}
					fabrication_report1.append("\n");
					if (outPutScreen.isJtrForSideTwo()) {
						fabrication_report1
								.append("\t 4.) JTR Load: "
										+ ((int) (outPutScreen
												.getTieRoadLoadJTRForSideTwo() * 100) / 100.0)
										+ " lbs");
						fabrication_report1.append("\n");
						fabrication_report1.append("\t 5.) JTR Size: "
								+ outPutScreen.getJtrSizeForS2());
						fabrication_report1.append(" EMT");
						fabrication_report1.append("\n");
					}
				} else {
					if (outPutScreen.isJtrForSideTwo()) {
						fabrication_report1
								.append("\t 1.) JTR Load: "
										+ ((int) (outPutScreen
												.getTieRoadLoadJTRForSideTwo() * 100) / 100.0)
										+ " lbs");
						fabrication_report1.append("\n");
						fabrication_report1.append("\t 2.) JTR Size: "
								+ outPutScreen.getJtrSizeForS2());
						fabrication_report1.append(" EMT");
						fabrication_report1.append("\n");
					}
				}
				if (outPutScreen.getDuctGageForSideOne() != null) {
					fabrication_report1.append("\n\n The ");
					fabrication_report1.append(connection1);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[0]));
					if (inputScreen.getTransConnS1() == 6) {
						fabrication_report1.append(" inches side\n ");
					} else {
						fabrication_report1.append(" inches side must be ");
						if ((inputScreen.getTransConnS1() == 3
								|| inputScreen.getTransConnS1() == 4 || inputScreen
								.getTransConnS1() == 5)
								&& outPutScreen.getReinClassForSideOne() != null) {
							fabrication_report1.append("a class ");
							fabrication_report1.append(outPutScreen
									.getReinClassForSideOne());
							fabrication_report1.append(" which is ");
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideOne());
							fabrication_report1.append(" or heavier\n ");
						} else {
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideOne());
							fabrication_report1.append(" or heavier\n ");
						}
					}
				}

				else {
					if (inputScreen.getTransConnS1() == 6) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append("\n\n The ");
						fabrication_report1.append(connection1);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n  ");
					}
				}
				if (outPutScreen.getDuctGageForSideTwo() != null) {
					fabrication_report1.append("The ");
					fabrication_report1.append(connection2);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[1]));
					if (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T25) {
						fabrication_report1.append(" inches side\n ");
					} else {
						fabrication_report1.append(" inches side must be ");
						if ((inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T10
								|| inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T11 || inputScreen
								.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T12)
								&& outPutScreen.getReinClassForSideTwo() != null) {
							fabrication_report1.append("a class ");
							fabrication_report1.append(outPutScreen
									.getReinClassForSideTwo());
							fabrication_report1.append(" which is ");
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideTwo());
							fabrication_report1.append(" or heavier\n ");
						} else {
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideTwo());
							fabrication_report1.append(" or heavier\n ");
						}
					}
				} else {
					if (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T25) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append("The ");
						fabrication_report1.append(connection2);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n  ");
					}

				}

			}

			else if (outPutScreen.isInternalReinforcement()) {

				fabrication_report1
						.append(", add an Internal reinforcement on side ");
				fabrication_report1.append(outPutScreen.getExtRenfSide()
						+ " inches");
				fabrication_report1.append("\n");
				if (outPutScreen.getNoTieRodForSideOne() != 0) {
					fabrication_report1.append("\t 1.) Number of MPT: ");
					fabrication_report1.append(outPutScreen
							.getNoTieRodForSideOne());
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 2.) MPT Load: ");
					fabrication_report1.append((int) (outPutScreen
							.getTieRodLoadForSideOne() * 100) / 100.0 + " lbs");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 3.) Use: ");
					if (inputScreen.isPosOrNeg()) {
						fabrication_report1
								.append(outPutScreen.getTieRod()
										+ " EMT which is good for "
										+ (int) (outPutScreen
												.getTieRodLoadForSideOne() * 100)
										/ 100.0 + " pounds.");
					} else {
						fabrication_report1
								.append(outPutScreen.getTieRod()
										+ " EMT which is good for "
										+ (int) (outPutScreen
												.getTieRodLoadForSideOne() * 100)
										/ 100.0 + " pounds.");

					}
					if (outPutScreen.isJtrForSideOne()) {
						fabrication_report1.append("\n\t 4.) JTR Load: "
								+ (int) (outPutScreen
										.getTieRoadLoadJTRForSideOne() * 100)
								/ 100.0 + " lbs");
						fabrication_report1.append("\n");
						fabrication_report1.append("\t 5.) JTR Size: "
								+ outPutScreen.getJtrSizeForS1());
						fabrication_report1.append(" EMT");
					}
				} else {
					if (outPutScreen.isJtrForSideOne()) {
						fabrication_report1.append("\n\t 1.) JTR Load: "
								+ (int) (outPutScreen
										.getTieRoadLoadJTRForSideOne() * 100)
								/ 100.0 + " lbs");
						fabrication_report1.append("\n");
						fabrication_report1.append("\t 2.) JTR Size: "
								+ outPutScreen.getJtrSizeForS1());
						fabrication_report1.append(" EMT");
					}
				}
				if (outPutScreen.isJtrForSideTwo()) {
					fabrication_report1.append("\n and add JTR on side ");
					fabrication_report1.append(outPutScreen.getSideTwo());
					fabrication_report1.append(" inches ");
					fabrication_report1.append("\n\t 1.)JTR Load: ");
					fabrication_report1.append(outPutScreen
							.getTieRoadLoadJTRForSideTwo() + " lbs");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 2.) JTR Size: "
							+ outPutScreen.getJtrSizeForS2());
					fabrication_report1.append(" EMT");
				}
				if (outPutScreen.getDuctGageForSideOne() != null) {

					fabrication_report1.append("\n\n The ");
					fabrication_report1.append(connection1);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[0]));
					if (inputScreen.getTransConnS1() == 6) {
						fabrication_report1.append(" inches side\n ");
					} else {
						fabrication_report1.append(" inches side must be ");
						if ((inputScreen.getTransConnS1() == 3
								|| inputScreen.getTransConnS1() == 4 || inputScreen
								.getTransConnS1() == 5)
								&& outPutScreen.getReinClassForSideOne() != null) {
							fabrication_report1.append("a class ");
							fabrication_report1.append(outPutScreen
									.getReinClassForSideOne());
							fabrication_report1.append(" which is ");
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideOne());
							fabrication_report1.append(" or heavier\n ");
						} else {
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideOne());
							fabrication_report1.append(" or heavier\n ");
						}
					}
				} else {
					if (inputScreen.getTransConnS1() == 6) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append("\n\n The ");
						fabrication_report1.append(connection1);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n  ");
					}
				}
				if (outPutScreen.getDuctGageForSideTwo() != null) {
					fabrication_report1.append("The ");
					fabrication_report1.append(connection2);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[1]));
					if (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T25) {
						fabrication_report1.append(" inches side\n ");
					} else {
						fabrication_report1.append(" inches side must be ");
						if ((inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T10
								|| inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T11 || inputScreen
								.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T12)
								&& outPutScreen.getReinClassForSideTwo() != null) {
							fabrication_report1.append("a class ");
							fabrication_report1.append(outPutScreen
									.getReinClassForSideTwo());
							fabrication_report1.append(" which is ");
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideTwo());
							fabrication_report1.append(" or heavier\n ");
						} else {
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideTwo());
							fabrication_report1.append(" or heavier\n ");
						}
					}
				} else {
					if (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T25) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append("The ");
						fabrication_report1.append(connection2);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n  ");
					}
				}
			}

			else if (outPutScreen.isExternalBothSide()) {
				fabrication_report1
						.append(", add an external reinforcement on side ");
				fabrication_report1.append(outPutScreen.getExtRenfSide()
						+ " inches");
				if (outPutScreen.isJtrForSideOne()) {
					fabrication_report1.append(" with JTR");
				}

				fabrication_report1.append("\n");
				fabrication_report1.append("\t 1.) Reinforcement Class: ");
				fabrication_report1.append(outPutScreen
						.getReinClassForSideOne());
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 2.) Reinforcement Angle: ");
				fabrication_report1.append(outPutScreen
						.getExtRenfAngleSideOne());
				fabrication_report1.append("\n");
				if (outPutScreen.isJtrForSideOne()) {
					fabrication_report1
							.append("\t 3.) JTR Load: "
									+ (int) (outPutScreen
											.getTieRoadLoadJTRForSideOne() * 100)
									/ 100.0 + " lbs");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 4.) JTR Size: "
							+ outPutScreen.getJtrSizeForS1());
					fabrication_report1.append(" EMT");
					fabrication_report1.append("\n");
				}
				fabrication_report1
						.append(" and add an external reinforcement on side "
								+ duct[1] + " inches");
				if (outPutScreen.isJtrForSideTwo()) {
					fabrication_report1.append(" with JTR ");
				}
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 1.) Reinforcement Class: ");
				fabrication_report1.append(outPutScreen
						.getReinClassForSideTwo());
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 2.) Reinforcement Angle: ");
				fabrication_report1.append(outPutScreen
						.getExtRenfAngleSideTwo());
				if (outPutScreen.isJtrForSideTwo()) {
					fabrication_report1.append("\n");
					fabrication_report1
							.append("\t 3.) JTR Load: "
									+ (int) (outPutScreen
											.getTieRoadLoadJTRForSideTwo() * 100)
									/ 100.0 + " lbs");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 4.) JTR Size: "
							+ outPutScreen.getJtrSizeForS2());
					fabrication_report1.append(" EMT");

				}
				if (outPutScreen.getDuctGageForSideOne() != null) {
					fabrication_report1.append("\n\n The ");
					fabrication_report1.append(connection1);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[0]));
					if (inputScreen.getTransConnS1() == 6) {
						fabrication_report1.append(" inches side\n ");
					} else {
						fabrication_report1.append(" inches side must be ");
						if ((inputScreen.getTransConnS1() == 3
								|| inputScreen.getTransConnS1() == 4 || inputScreen
								.getTransConnS1() == 5)
								&& outPutScreen.getReinClassForSideOne() != null) {
							fabrication_report1.append("a class ");
							fabrication_report1.append(outPutScreen
									.getReinClassForSideOne());
							fabrication_report1.append(" which is ");
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideOne());
							fabrication_report1.append(" or heavier\n ");
						} else {
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideOne());
							fabrication_report1.append(" or heavier\n ");
						}
					}
				} else {
					if (inputScreen.getTransConnS1() == 6) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append("\n\n The ");
						fabrication_report1.append(connection1);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n  ");
					}
				}
				if (outPutScreen.getDuctGageForSideTwo() != null) {
					fabrication_report1.append("The ");
					fabrication_report1.append(connection2);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[1]));
					if (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T25) {
						fabrication_report1.append(" inches side\n ");
					} else {
						fabrication_report1.append(" inches side must be ");
						if ((inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T10
								|| inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T11 || inputScreen
								.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T12)
								&& outPutScreen.getReinClassForSideTwo() != null) {
							fabrication_report1.append("a class ");
							fabrication_report1.append(outPutScreen
									.getReinClassForSideTwo());
							fabrication_report1.append(" which is ");
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideTwo());
							fabrication_report1.append(" or heavier\n ");
						} else {
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideTwo());
							fabrication_report1.append(" or heavier\n ");
						}
					}
				} else {
					if (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T25) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append("The ");
						fabrication_report1.append(connection2);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n  ");
					}
				}
			}

			else if (outPutScreen.isExternalReinforcement()) {
				fabrication_report1
						.append(", add an external reinforcement on side ");
				fabrication_report1.append(outPutScreen.getExtRenfSide()
						+ " inches");
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 1.) Reinforcement Class: ");
				fabrication_report1.append(outPutScreen
						.getReinClassForSideOne());
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 2.) Reinforcement Angle: ");
				fabrication_report1.append(outPutScreen
						.getExtRenfAngleSideOne());
				if (outPutScreen.isJtrForSideOne()) {
					fabrication_report1.append("\n");
					fabrication_report1
							.append("\t 3.) JTR Load: "
									+ (int) (outPutScreen
											.getTieRoadLoadJTRForSideOne() * 100)
									/ 100.0 + " lbs");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 4.) JTR Size: "
							+ outPutScreen.getJtrSizeForS1());
					fabrication_report1.append(" EMT");
				}
				if (outPutScreen.isJtrForSideTwo()) {
					fabrication_report1.append("\n and add JTR on side ");
					fabrication_report1.append(outPutScreen.getSideTwo());
					fabrication_report1.append(" inches ");
					fabrication_report1.append("\n\t 1.) JTR Load: ");
					fabrication_report1.append(outPutScreen
							.getTieRoadLoadJTRForSideTwo() + " lbs");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 2.) JTR Size: "
							+ outPutScreen.getJtrSizeForS2());
					fabrication_report1.append(" EMT");
				}
				if (outPutScreen.getDuctGageForSideOne() != null) {
					fabrication_report1.append("\n\n The ");
					fabrication_report1.append(connection1);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[0]));
					if (inputScreen.getTransConnS1() == 6) {
						fabrication_report1.append(" inches side\n ");
					} else if ((inputScreen.getTransConnS1() == 3
							|| inputScreen.getTransConnS1() == 4 || inputScreen
							.getTransConnS1() == 5)
							&& outPutScreen.getReinClassForSideOne() != null) {
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append("a class ");
						fabrication_report1.append(outPutScreen
								.getReinClassForSideOne());
						fabrication_report1.append(" which is ");
						fabrication_report1.append(outPutScreen
								.getDuctGageForSideOne());
						fabrication_report1.append(" or heavier\n ");
					} else {
						fabrication_report1.append(outPutScreen
								.getDuctGageForSideOne());
						fabrication_report1.append(" or heavier\n  ");
					}
				} else {
					if (inputScreen.getTransConnS1() == 6) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append("\n\n The ");
						fabrication_report1.append(connection1);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n  ");
					}
				}
				if (outPutScreen.getDuctGageForSideTwo() != null) {
					fabrication_report1.append("The ");
					fabrication_report1.append(connection2);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[1]));
					if (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T25) {
						fabrication_report1.append(" inches side\n ");
					} else if ((inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T10
							|| inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T11 || inputScreen
							.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T12)
							&& outPutScreen.getReinClassForSideTwo() != null) {
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append("a class ");
						fabrication_report1.append(outPutScreen
								.getReinClassForSideTwo());
						fabrication_report1.append(" which is ");
						fabrication_report1.append(outPutScreen
								.getDuctGageForSideTwo());
						fabrication_report1.append(" or heavier\n ");
					} else {
						fabrication_report1.append(" inches side must be ");
						if (inputScreen.getTransConnS2() == 1
								|| inputScreen.getTransConnS2() == 2) {
							fabrication_report1.append(Math.min(
									outPutScreen.getFinalDuctGage() + 2, 24)
									+ " ga");
						} else {
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideTwo());
						}
						fabrication_report1.append(" or heavier\n ");
					}
				} else {
					if (inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T25) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append("The ");
						fabrication_report1.append(connection2);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n  ");
					}
				}

			} else if (outPutScreen.isIntAndExternalOnBothSide()) {

				fabrication_report1
						.append(", add external and internal reinforcement on side ");
				fabrication_report1.append(outPutScreen.getExtRenfSide()
						+ " inches");
				if (outPutScreen.isJtrForSideOne()) {
					fabrication_report1.append(" with JTR");
				}
				fabrication_report1.append("\n");
				fabrication_report1.append("External Reinforcement : ");
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 1.) Reinforcement Class: ");
				fabrication_report1.append(outPutScreen
						.getReinClassForSideOne());
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 2.) Reinforcement Angle: ");
				fabrication_report1.append(outPutScreen
						.getExtRenfAngleSideOne());
				fabrication_report1.append("\n");
				fabrication_report1.append("Internal Reinforcement : ");
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 1.) Number of MPT: ");
				fabrication_report1
						.append(outPutScreen.getNoTieRodForSideOne());
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 2.) MPT Load: ");
				fabrication_report1.append((int) (outPutScreen
						.getTieRodLoadForSideOne() * 100) / 100.0 + " lbs");
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 3.) Use: ");
				if (inputScreen.isPosOrNeg()) {
					fabrication_report1
							.append(outPutScreen.getTieRodForSideOne()
									+ " EMT which is good for "
									+ (int) (outPutScreen
											.getTieRodLoadForSideOne() * 100)
									/ 100.0 + " pounds.");
				} else {
					fabrication_report1
							.append(outPutScreen.getTieRodForSideOne()
									+ " EMT which is good for "
									+ (int) (outPutScreen
											.getTieRodLoadForSideOne() * 100)
									/ 100.0 + " pounds.");
				}
				if (outPutScreen.isJtrForSideOne()) {
					fabrication_report1.append("\n");
					fabrication_report1
							.append("\t 4.) JTR Load: "
									+ (int) (outPutScreen
											.getTieRoadLoadJTRForSideOne() * 100)
									/ 100.0 + " lbs");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 5.) JTR Size: "
							+ outPutScreen.getJtrSizeForS1());
					fabrication_report1.append(" EMT");
					fabrication_report1.append("\n");
				}
				fabrication_report1.append("\n");
				fabrication_report1
						.append(" and add external and internal reinforcement on side "
								+ duct[1] + " inches");
				if (outPutScreen.isJtrForSideTwo()) {
					fabrication_report1.append(" with JTR ");
				}
				fabrication_report1.append("\n");
				fabrication_report1.append("External Reinforcement : ");
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 1.) Reinforcement Class: ");
				fabrication_report1.append(outPutScreen
						.getReinClassForSideTwo());
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 2.) Reinforcement Angle: ");
				fabrication_report1.append(outPutScreen
						.getExtRenfAngleSideTwo());
				fabrication_report1.append("\n");
				fabrication_report1.append("Internal Reinforcement : ");
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 1.) Number of MPT: ");
				fabrication_report1
						.append(outPutScreen.getNoTieRodForSideTwo());
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 2.) MPT Load: ");
				fabrication_report1.append((int) (outPutScreen
						.getTieRodLoadForSideTwo() * 100) / 100.0);
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 3.) Use: ");
				if (inputScreen.isPosOrNeg()) {
					fabrication_report1
							.append(outPutScreen.getTieRodForSideTwo()
									+ " EMT which is good for "
									+ (int) (outPutScreen
											.getTieRodLoadForSideTwo() * 100)
									/ 100.0 + " pounds.");
				} else {
					fabrication_report1
							.append(outPutScreen.getTieRodForSideTwo()
									+ " EMT which is good for "
									+ (int) (outPutScreen
											.getTieRodLoadForSideTwo() * 100)
									/ 100.0 + " pounds.");
				}
				if (outPutScreen.isJtrForSideTwo()) {
					fabrication_report1.append("\n");
					fabrication_report1
							.append("\t 4.) JTR Load: "
									+ (int) (outPutScreen
											.getTieRoadLoadJTRForSideTwo() * 100)
									/ 100.0 + " lbs");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 5.) JTR Size: "
							+ outPutScreen.getJtrSizeForS2());
					fabrication_report1.append(" EMT");
				}
				if (outPutScreen.getDuctGageForSideOne() != null) {
					fabrication_report1.append("\n\n The ");
					fabrication_report1.append(connection1);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[0]));
					if (inputScreen.getTransConnS1() == 6) {
						fabrication_report1.append(" inches side\n ");
					} else if ((inputScreen.getTransConnS1() == 3
							|| inputScreen.getTransConnS1() == 4 || inputScreen
							.getTransConnS1() == 5)
							&& outPutScreen.getReinClassForSideOne() != null) {
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append("a class ");
						fabrication_report1.append(outPutScreen
								.getReinClassForSideOne());
						fabrication_report1.append(" which is ");
						fabrication_report1.append(outPutScreen
								.getDuctGageForSideOne());
						fabrication_report1.append(" or heavier\n ");
					} else {
						fabrication_report1.append(outPutScreen
								.getDuctGageForSideOne());
						fabrication_report1.append(" or heavier\n  ");
					}
				} else {
					if (inputScreen.getTransConnS1() == 6) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append("\n\n The ");
						fabrication_report1.append(connection1);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n  ");
					}
				}
				if (outPutScreen.getDuctGageForSideTwo() != null) {
					fabrication_report1.append("The ");
					fabrication_report1.append(connection2);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[1]));
					if (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T25) {
						fabrication_report1.append(" inches side\n ");
					} else if ((inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T10
							|| inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T11 || inputScreen
							.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T12)
							&& outPutScreen.getReinClassForSideTwo() != null) {
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append("a class ");
						fabrication_report1.append(outPutScreen
								.getReinClassForSideTwo());
						fabrication_report1.append(" which is ");
						fabrication_report1.append(outPutScreen
								.getDuctGageForSideTwo());
						fabrication_report1.append(" or heavier\n ");
					} else {
						fabrication_report1.append(" inches side must be ");
						if (inputScreen.getTransConnS2() == 1
								|| inputScreen.getTransConnS2() == 2) {
							fabrication_report1.append(Math.min(
									outPutScreen.getFinalDuctGage() + 2, 24)
									+ " ga");
						} else {
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideTwo());
						}
						fabrication_report1.append(" or heavier\n ");
					}
				} else {
					if (inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T25) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append("The ");
						fabrication_report1.append(connection2);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n  ");
					}
				}
			} else if (outPutScreen.isIntAndExtOnSideOne()) {
				fabrication_report1
						.append(", add external and internal reinforcement on side ");
				fabrication_report1.append(outPutScreen.getExtRenfSide()
						+ " inches");
				if (outPutScreen.isJtrForSideOne()) {
					fabrication_report1.append(" with JTR");
				}
				fabrication_report1.append("\n");
				fabrication_report1.append("External Reinforcement : ");
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 1.) Reinforcement Class: ");
				fabrication_report1.append(outPutScreen
						.getReinClassForSideOne());
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 2.) Reinforcement Angle: ");
				fabrication_report1.append(outPutScreen
						.getExtRenfAngleSideOne());
				fabrication_report1.append("\n");
				fabrication_report1.append("Internal Reinforcement : ");
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 1.) Number of MPT: ");
				fabrication_report1
						.append(outPutScreen.getNoTieRodForSideOne());
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 2.) MPT Load: ");
				fabrication_report1.append((int) (outPutScreen
						.getTieRodLoadForSideOne() * 100) / 100.0 + " lbs");
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 3.) Use: ");
				if (inputScreen.isPosOrNeg()) {
					fabrication_report1
							.append(outPutScreen.getTieRodForSideOne()
									+ " EMT which is good for "
									+ (int) (outPutScreen
											.getTieRodLoadForSideOne() * 100)
									/ 100.0 + " pounds.");
				} else {
					fabrication_report1
							.append(outPutScreen.getTieRodForSideOne()
									+ " EMT which is good for "
									+ (int) (outPutScreen
											.getTieRodLoadForSideOne() * 100)
									/ 100.0 + " pounds.");
				}
				if (outPutScreen.isJtrForSideOne()) {
					fabrication_report1.append("\n");
					fabrication_report1
							.append("\t 4.) JTR Load: "
									+ (int) (outPutScreen
											.getTieRoadLoadJTRForSideOne() * 100)
									/ 100.0 + " lbs");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 5.) JTR Size: "
							+ outPutScreen.getJtrSizeForS1());
					fabrication_report1.append(" EMT");
					fabrication_report1.append("\n");

				}
				if (outPutScreen.isJtrForSideTwo()) {
					fabrication_report1.append("\n");
					fabrication_report1.append(" and add JTR on side "
							+ outPutScreen.getSideTwo() + " inches");
					fabrication_report1.append("\n");
					fabrication_report1
							.append("\t 1.) JTR Load: "
									+ (int) (outPutScreen
											.getTieRoadLoadJTRForSideTwo() * 100)
									/ 100.0 + " lbs");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 2.) JTR Size: "
							+ outPutScreen.getJtrSizeForS2());
					fabrication_report1.append(" EMT");

				}
				if (outPutScreen.getDuctGageForSideOne() != null) {
					fabrication_report1.append("\n\n The ");
					fabrication_report1.append(connection1);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[0]));
					if (inputScreen.getTransConnS1() == 6) {
						fabrication_report1.append(" inches side\n ");
					} else if ((inputScreen.getTransConnS1() == 3
							|| inputScreen.getTransConnS1() == 4 || inputScreen
							.getTransConnS1() == 5)
							&& outPutScreen.getReinClassForSideOne() != null) {
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append("a class ");
						fabrication_report1.append(outPutScreen
								.getReinClassForSideOne());
						fabrication_report1.append(" which is ");
						fabrication_report1.append(outPutScreen
								.getDuctGageForSideOne());
						fabrication_report1.append(" or heavier\n ");
					} else {
						fabrication_report1.append(outPutScreen
								.getDuctGageForSideOne());
						fabrication_report1.append(" or heavier\n  ");
					}
				} else {
					if (inputScreen.getTransConnS1() == 6) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append("\n\n The ");
						fabrication_report1.append(connection1);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n  ");
					}
				}
				if (outPutScreen.getDuctGageForSideTwo() != null) {
					fabrication_report1.append("The ");
					fabrication_report1.append(connection2);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[1]));
					if (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T25) {
						fabrication_report1.append(" inches side\n ");
					} else if ((inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T10
							|| inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T11 || inputScreen
							.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T12)
							&& outPutScreen.getReinClassForSideTwo() != null) {
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append("a class ");
						fabrication_report1.append(outPutScreen
								.getReinClassForSideTwo());
						fabrication_report1.append(" which is ");
						fabrication_report1.append(outPutScreen
								.getDuctGageForSideTwo());
						fabrication_report1.append(" or heavier\n ");
					} else {
						fabrication_report1.append(" inches side must be ");
						if (inputScreen.getTransConnS2() == 1
								|| inputScreen.getTransConnS2() == 2) {
							fabrication_report1.append(Math.min(
									outPutScreen.getFinalDuctGage() + 2, 24)
									+ " ga");
						} else {
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideTwo());
						}
						fabrication_report1.append(" or heavier\n ");
					}
				} else {
					if (inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T25) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append("The ");
						fabrication_report1.append(connection2);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n  ");
					}
				}
			} else if (outPutScreen.isIntAndExtOnSideTwo()) {
				if (outPutScreen.isJtrForSideOne()) {
					fabrication_report1.append(" add JTR on side ");
					fabrication_report1.append(outPutScreen.getSideOne()
							+ " inches");
					fabrication_report1.append("\n");
					fabrication_report1
							.append("\t 1.) JTR Load: "
									+ (int) (outPutScreen
											.getTieRoadLoadJTRForSideOne() * 100)
									/ 100.0 + " lbs");
					fabrication_report1.append("\n");
				}
				fabrication_report1
						.append(" add external and internal reinforcement on side ");
				fabrication_report1.append(outPutScreen.getSideTwo()
						+ " inches");
				if (outPutScreen.isJtrForSideTwo()) {
					fabrication_report1.append(" with JTR");
				}
				fabrication_report1.append("\n");
				fabrication_report1.append("External Reinforcement : ");
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 1.) Reinforcement Class: ");
				fabrication_report1.append(outPutScreen
						.getReinClassForSideTwo());
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 2.) Reinforcement Angle: ");
				fabrication_report1.append(outPutScreen
						.getExtRenfAngleSideTwo());
				fabrication_report1.append("\n");
				fabrication_report1.append("Internal Reinforcement : ");
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 1.) Number of MPT: ");
				fabrication_report1
						.append(outPutScreen.getNoTieRodForSideTwo());
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 2.) MPT Load: ");
				fabrication_report1.append((int) (outPutScreen
						.getTieRodLoadForSideTwo() * 100) / 100.0 + " lbs");
				fabrication_report1.append("\n");
				fabrication_report1.append("\t 3.) Use: ");
				if (inputScreen.isPosOrNeg()) {
					fabrication_report1
							.append(outPutScreen.getTieRodForSideTwo()
									+ " EMT which is good for "
									+ (int) (outPutScreen
											.getTieRodLoadForSideTwo() * 100)
									/ 100.0 + " pounds.");
				} else {
					fabrication_report1
							.append(outPutScreen.getTieRodForSideTwo()
									+ " EMT which is good for "
									+ (int) (outPutScreen
											.getTieRodLoadForSideTwo() * 100)
									/ 100.0 + " pounds.");
				}
				if (outPutScreen.isJtrForSideTwo()) {
					fabrication_report1.append("\n");
					fabrication_report1
							.append("\t 4.) JTR Load: "
									+ (int) (outPutScreen
											.getTieRoadLoadJTRForSideTwo() * 100)
									/ 100.0 + " lbs");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 5.) JTR Size: "
							+ outPutScreen.getJtrSizeForS1());
					fabrication_report1.append(" EMT");
					fabrication_report1.append("\n");
				}

				if (outPutScreen.getDuctGageForSideOne() != null) {
					fabrication_report1.append("\n\n The ");
					fabrication_report1.append(connection1);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[0]));
					if (inputScreen.getTransConnS1() == 6) {
						fabrication_report1.append(" inches side\n ");
					} else if ((inputScreen.getTransConnS1() == 3
							|| inputScreen.getTransConnS1() == 4 || inputScreen
							.getTransConnS1() == 5)
							&& outPutScreen.getReinClassForSideOne() != null) {
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append("a class ");
						fabrication_report1.append(outPutScreen
								.getReinClassForSideOne());
						fabrication_report1.append(" which is ");
						fabrication_report1.append(outPutScreen
								.getDuctGageForSideOne());
						fabrication_report1.append(" or heavier\n ");
					} else {
						fabrication_report1.append(outPutScreen
								.getDuctGageForSideOne());
						fabrication_report1.append(" or heavier\n  ");
					}
				} else {
					if (inputScreen.getTransConnS1() == 6) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append("\n\n The ");
						fabrication_report1.append(connection1);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n  ");
					}
				}
				if (outPutScreen.getDuctGageForSideTwo() != null) {
					fabrication_report1.append("The ");
					fabrication_report1.append(connection2);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[1]));
					if (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T25) {
						fabrication_report1.append(" inches side\n ");
					} else if ((inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T10
							|| inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T11 || inputScreen
							.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T12)
							&& outPutScreen.getReinClassForSideTwo() != null) {
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append("a class ");
						fabrication_report1.append(outPutScreen
								.getReinClassForSideTwo());
						fabrication_report1.append(" which is ");
						fabrication_report1.append(outPutScreen
								.getDuctGageForSideTwo());
						fabrication_report1.append(" or heavier\n ");
					} else {
						fabrication_report1.append(" inches side must be ");
						if (inputScreen.getTransConnS2() == 1
								|| inputScreen.getTransConnS2() == 2) {
							fabrication_report1.append(Math.min(
									outPutScreen.getFinalDuctGage() + 2, 24)
									+ " ga");
						} else {
							fabrication_report1.append(outPutScreen
									.getDuctGageForSideTwo());
						}
						fabrication_report1.append(" or heavier\n ");
					}
				} else {
					if (inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T25) {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches\n ");
					} else {
						fabrication_report1.append("The ");
						fabrication_report1.append(connection2);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches side must be ");
						fabrication_report1.append(Math.min(
								outPutScreen.getFinalDuctGage() + 2, 24));
						fabrication_report1.append(" gage or heavier\n  ");
					}
				}
			} else if (outPutScreen.isNoRenforcement()) {

				if ((inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T1 || inputScreen
						.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T5)
						&& (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T1 || inputScreen
								.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T5)) {

					fabrication_report1
							.append(", without any additional reinforcement ");

					fabrication_report1.append("\n\n The ");
					fabrication_report1.append(connection1);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[0]));
					fabrication_report1.append(" inches side must be ");
					fabrication_report1.append(Math.min(
							outPutScreen.getMinDuctGageSOne() + 2, 24));
					fabrication_report1.append(" gage or heavier\n The ");
					fabrication_report1.append(connection2);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[1]));
					fabrication_report1.append(" inches side must be ");
					fabrication_report1.append(Math.min(
							outPutScreen.getMinDuctGageSTwo() + 2, 24));
					fabrication_report1.append(" gage or heavier\n ");

				} else if ((inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T10
						|| inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T11 || inputScreen
						.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T12)
						&& (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T10
								|| inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T11 || inputScreen
								.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T12)) {

					if (outPutScreen.getDuctGageForSideOne() != null) {
						fabrication_report1.append("\n\n");
						fabrication_report1.append(" The ");
						fabrication_report1.append(connection1);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1
								.append(" inches side must be a class ");
						fabrication_report1.append(outPutScreen
								.getReinClassForSideOne());
						fabrication_report1.append(" which is ");
						fabrication_report1.append(outPutScreen
								.getDuctGageForSideOne());
						fabrication_report1.append(" or heavier");
					} else {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches\n ");
					}
					if (outPutScreen.getDuctGageForSideTwo() != null) {
						fabrication_report1.append("\n");
						fabrication_report1.append(" The ");
						fabrication_report1.append(connection2);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1
								.append(" inches side must be a class ");
						fabrication_report1.append(outPutScreen
								.getReinClassForSideTwo());
						fabrication_report1.append(" which is ");
						fabrication_report1.append(outPutScreen
								.getDuctGageForSideTwo());
						fabrication_report1.append(" or heavier");
						fabrication_report1.append("\n");
					} else {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches\n ");
					}

				}

				else if (inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T25
						&& inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T25) {
					if (outPutScreen.isJtrForSideOne()) {
						fabrication_report1.append(" on side "
								+ outPutScreen.getSideOne() + ":");
						fabrication_report1.append("\n");
						fabrication_report1.append("\t 1.) JTR Load: "
								+ (int) (outPutScreen
										.getTieRoadLoadJTRForSideOne() * 100)
								/ 100.0 + " lbs");
						fabrication_report1.append("\n");
						fabrication_report1.append("\t 2.) JTR Size: "
								+ outPutScreen.getJtrSizeForS1());
						fabrication_report1.append(" EMT");
					}
					if (outPutScreen.isJtrForSideTwo()) {
						fabrication_report1.append("\n");
						fabrication_report1.append(" and add JTR on side "
								+ outPutScreen.getSideTwo() + " inches:");
						fabrication_report1.append("\n");
						fabrication_report1.append("\t 1.) JTR Load: "
								+ (int) (outPutScreen
										.getTieRoadLoadJTRForSideTwo() * 100)
								/ 100.0 + " lbs");
						fabrication_report1.append("\n");
						fabrication_report1.append("\t 2.) JTR Size: "
								+ outPutScreen.getJtrSizeForS2());
						fabrication_report1.append(" EMT");
					}
					if (outPutScreen.getDuctGageForSideOne() != null) {
						fabrication_report1.append("\n\n The ");
						fabrication_report1.append(connection1);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches side\n ");
					} else {
						fabrication_report1
								.append("\nNo additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[0]));
						fabrication_report1.append(" inches\n ");
					}
					if (outPutScreen.getDuctGageForSideTwo() != null) {
						fabrication_report1.append("The ");
						fabrication_report1.append(connection2);
						fabrication_report1.append(" on the ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches side\n ");
					} else {
						fabrication_report1
								.append("No additional reinforcement is required "
										+ "on the side ");
						fabrication_report1.append(String.valueOf(duct[1]));
						fabrication_report1.append(" inches\n ");
					}

				}

				else if ((inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T10
						|| inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T11 || inputScreen
						.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T12)
						&& (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T1 || inputScreen
								.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T5)) {
					fabrication_report1.append("\n");
					fabrication_report1.append(" The ");
					fabrication_report1.append(connection1);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[0]));
					fabrication_report1.append(" inches side must be a class ");
					fabrication_report1.append(outPutScreen
							.getReinClassForSideOne());
					fabrication_report1.append(" which is ");
					fabrication_report1.append(outPutScreen
							.getDuctGageForSideOne());
					fabrication_report1.append(" or heavier");
					fabrication_report1.append("\n");
					fabrication_report1.append("The ");
					fabrication_report1.append(connection2);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[1]));
					fabrication_report1.append(" inches side must be ");
					fabrication_report1.append(Math.min(
							outPutScreen.getMinDuctGageSTwo() + 2, 24));
					fabrication_report1.append(" gage or heavier\n ");

				}

				else if ((inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T1 || inputScreen
						.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T5)
						&& (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T10
								|| inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T11 || inputScreen
								.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T12)) {
					fabrication_report1.append("\n\n");
					fabrication_report1.append("The ");
					fabrication_report1.append(connection1);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[0]));
					fabrication_report1.append(" inches side must be ");
					fabrication_report1.append(Math.min(
							outPutScreen.getMinDuctGageSOne() + 2, 24));
					fabrication_report1.append(" gage or heavier\n ");
					fabrication_report1.append("The ");
					fabrication_report1.append(connection2);
					fabrication_report1.append(" on the ");
					fabrication_report1.append(String.valueOf(duct[1]));
					fabrication_report1.append(" inches side must be a class ");
					fabrication_report1.append(outPutScreen
							.getReinClassForSideTwo());
					fabrication_report1.append(" which is ");
					fabrication_report1.append(outPutScreen
							.getDuctGageForSideTwo());
					fabrication_report1.append(" or heavier\n");


				}

			}


			if ((inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T10
					|| inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T11
					|| inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T12 || inputScreen
					.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T25)
					&& (inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T1 || inputScreen
							.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T5)) {
				if ((inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T1 || inputScreen
						.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T5)
						&& (inputScreen.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T1 || inputScreen
								.getTransConnS2() == Constants.TRANSVERSE_CONNECTION_T5)) {
					fabrication_report1.append(Math.min(
							outPutScreen.getMinDuctGageSTwo() + 2, 24));
				} else if ((inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T10
						|| inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T11
						|| inputScreen.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T12 || inputScreen
							.getTransConnS1() == Constants.TRANSVERSE_CONNECTION_T25)) {
					System.out
							.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 1.) Reinforcement Class: ");
					fabrication_report1.append(outPutScreen
							.getReinforcementClass());
					fabrication_report1.append("\n");
					fabrication_report1.append("\t 2.) Reinforcement Angle: ");
					fabrication_report1.append(outPutScreen
							.getExternalRenfAngle());

				}
			}
			// fabrication_report1.append(" gage or heavier \n");
			fabrication_report1.append("\n Longitudinal Seam: \n");
			fabrication_report1.append(outPutScreen.getLongitudinalSeam());
			GenerateViewsInPdf generatePdf = null;

			logg.info("External on S1" + outPutScreen.isExternalReinforcement());
			logg.info("External on both Side"
					+ outPutScreen.isExternalBothSide());
			logg.info("External on S1 and Internal on S2"
					+ outPutScreen.isExtAndInternalEachSide());
			logg.info("Internal on S1" + outPutScreen.isInternalReinforcement());
			logg.info("Internal on S1 and External on S2"
					+ outPutScreen.isIntAndExtEachSide());
			logg.info("Internal on Both sides"
					+ outPutScreen.isInternalBothSide());

			logg.info("NoRenforcement : " + outPutScreen.isNoRenforcement());

			logg.info("Tie Road on S1" + outPutScreen.getNoTieRodForSideOne());
			logg.info("Tie Road on S2" + outPutScreen.getNoTieRodForSideTwo());
			if (outPutScreen.isExternalReinforcement()
					&& outPutScreen.isIntAndExtOnSideOne()) {
				generatePdf = new GenerateViewsInPdf(width, height,
						joint_spacing, user_name, company_name,
						fabrication_report1.toString(), document, connection1,
						connection2, true, true, true, false,
						outPutScreen.getNoTieRodForSideOne(),
						outPutScreen.getNoTieRodForSideTwo(),
						inputScreen.getTransConnS1(),
						outPutScreen.isJtrForSideOne(),
						outPutScreen.isJtrForSideTwo());
			}

			else if (outPutScreen.isIntAndExtOnSideOne()
					&& outPutScreen.isInternalReinforcement()) {
				generatePdf = new GenerateViewsInPdf(width, height,
						joint_spacing, user_name, company_name,
						fabrication_report1.toString(), document, connection1,
						connection2, true, false, true, true,
						outPutScreen.getNoTieRodForSideOne(),
						outPutScreen.getNoTieRodForSideTwo(),
						inputScreen.getTransConnS1(),
						outPutScreen.isJtrForSideOne(),
						outPutScreen.isJtrForSideTwo());
			}
			// External on S1
			else if (outPutScreen.isExternalReinforcement()) {
				generatePdf = new GenerateViewsInPdf(width, height,
						joint_spacing, user_name, company_name,
						fabrication_report1.toString(), document, connection1,
						connection2, true, false, false, false, 0, 0,
						inputScreen.getTransConnS1(),
						outPutScreen.isJtrForSideOne(),
						outPutScreen.isJtrForSideTwo());
			}
			// External on both Side
			else if (outPutScreen.isExternalBothSide()) {
				generatePdf = new GenerateViewsInPdf(width, height,
						joint_spacing, user_name, company_name,
						fabrication_report1.toString(), document, connection1,
						connection2, true, true, false, false, 0, 0,
						inputScreen.getTransConnS1(),
						outPutScreen.isJtrForSideOne(),
						outPutScreen.isJtrForSideTwo());
			}
			// External on S1 and Internal on S2
			else if (outPutScreen.isExtAndInternalEachSide()) {
				generatePdf = new GenerateViewsInPdf(width, height,
						joint_spacing, user_name, company_name,
						fabrication_report1.toString(), document, connection1,
						connection2, true, false, false, true, 0,
						outPutScreen.getNoTieRodForSideTwo(),
						inputScreen.getTransConnS1(),
						outPutScreen.isJtrForSideOne(),
						outPutScreen.isJtrForSideTwo());
			}
			// Internal on S1
			else if (outPutScreen.isInternalReinforcement()) {
				generatePdf = new GenerateViewsInPdf(width, height,
						joint_spacing, user_name, company_name,
						fabrication_report1.toString(), document, connection1,
						connection2, false, false, true, false,
						outPutScreen.getNoTieRodForSideOne(), 0,
						inputScreen.getTransConnS1(),
						outPutScreen.isJtrForSideOne(),
						outPutScreen.isJtrForSideTwo());
			}
			// Internal on S1 and External on S2
			else if (outPutScreen.isIntAndExtEachSide()) {
				generatePdf = new GenerateViewsInPdf(width, height,
						joint_spacing, user_name, company_name,
						fabrication_report1.toString(), document, connection1,
						connection2, false, true, true, false,
						outPutScreen.getNoTieRodForSideOne(), 0,
						inputScreen.getTransConnS1(),
						outPutScreen.isJtrForSideOne(),
						outPutScreen.isJtrForSideTwo());
			}
			// Internal on Both sides
			else if (outPutScreen.isInternalBothSide()) {
				generatePdf = new GenerateViewsInPdf(width, height,
						joint_spacing, user_name, company_name,
						fabrication_report1.toString(), document, connection1,
						connection2, false, false, true, true,
						outPutScreen.getNoTieRodForSideOne(),
						outPutScreen.getNoTieRodForSideTwo(),
						inputScreen.getTransConnS1(),
						outPutScreen.isJtrForSideOne(),
						outPutScreen.isJtrForSideTwo());
			} else if (outPutScreen.isNoRenforcement()) {
				generatePdf = new GenerateViewsInPdf(width, height,
						joint_spacing, user_name, company_name,
						fabrication_report1.toString(), document, connection1,
						connection2, false, false, false, false, 0, 0,
						inputScreen.getTransConnS1(),
						outPutScreen.isJtrForSideOne(),
						outPutScreen.isJtrForSideTwo());
				System.out.println("#####" + outPutScreen.isJtrForSideOne());
				System.out.println("#####" + outPutScreen.isJtrForSideTwo());
			} else if (outPutScreen.isIntAndExternalOnBothSide()) {
				generatePdf = new GenerateViewsInPdf(width, height,
						joint_spacing, user_name, company_name,
						fabrication_report1.toString(), document, connection1,
						connection2, true, true, true, true,
						outPutScreen.getNoTieRodForSideOne(),
						outPutScreen.getNoTieRodForSideTwo(),
						inputScreen.getTransConnS1(),
						outPutScreen.isJtrForSideOne(),
						outPutScreen.isJtrForSideTwo());

			} else if (outPutScreen.isIntAndExtOnSideOne()) {
				generatePdf = new GenerateViewsInPdf(width, height,
						joint_spacing, user_name, company_name,
						fabrication_report1.toString(), document, connection1,
						connection2, true, false, true, false,
						outPutScreen.getNoTieRodForSideOne(),
						outPutScreen.getNoTieRodForSideTwo(),
						inputScreen.getTransConnS1(),
						outPutScreen.isJtrForSideOne(),
						outPutScreen.isJtrForSideTwo());
			} else if (outPutScreen.isIntAndExtOnSideTwo()) {
				generatePdf = new GenerateViewsInPdf(width, height,
						joint_spacing, user_name, company_name,
						fabrication_report1.toString(), document, connection1,
						connection2, false, true, false, true,
						outPutScreen.getNoTieRodForSideOne(),
						outPutScreen.getNoTieRodForSideTwo(),
						inputScreen.getTransConnS1(),
						outPutScreen.isJtrForSideOne(),
						outPutScreen.isJtrForSideTwo());
			} else {
				generatePdf = new GenerateViewsInPdf(width, height,
						joint_spacing, user_name, company_name,
						fabrication_report1.toString(), document, connection1,
						connection2, false, false, false, false, 0, 0,
						inputScreen.getTransConnS1(),
						outPutScreen.isJtrForSideOne(),
						outPutScreen.isJtrForSideTwo());
			}

			// GenerateViewsInPdf generatePdf = new GenerateViewsInPdf(width,
			// height, joint_spacing, user_name, company_name,
			// fabrication_report1.toString(), document, connection1,
			// connection2, outPutScreen.isExternalReinforcement(),
			// outPutScreen.isInternalReinforcement(), outPutScreen
			// .getNoTieRod(), inputScreen.getTransConnS1());
			generatePdf.draw(cb);

			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
