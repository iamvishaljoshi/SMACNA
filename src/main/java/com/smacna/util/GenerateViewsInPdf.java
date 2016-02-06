package com.smacna.util;

import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.draw.LineSeparator;

/**
 *
 * @author minakshi.b
 * @desc This class is used for generating pdf based on fabrication report.
 */
public class GenerateViewsInPdf {

	private static int cut_point = 1;
	private static int x_axis, y_axis, z_axis, start_x, start_y;
	private static int temp_height, temp_slant, nTieRodsSide1, nTieRodsSide2;
	private static int actX, actY, actZ;
	private static float multiple;
	private static boolean isExternalEnforcementS1,isExternalEnforcementS2, isInternalEnforcementS1,  isInternalEnforcementS2, isJTRS1, isJTRS2;

	/**
	 *
	 * @param <width> width of duct
	 * @param <height> height of duct
	 * @param <joint_spacing> joint spacing of duct
	 * @param <user_name> user name
	 * @param <company_name> company name
	 * @param <fabrication_report> fabrication report generated based on output
	 * @param <document> pdf document in which we need to append the contents
	 * @param <connection1> transverse connection for S1
	 * @param <connection2> transverse connection for S2
	 * @param <externalEnforcement> indicates weather duct support external reinforcement or not.
	 * @param <internalEnforcement> indicates weather duct support internal reinforcement or not.
	 * @param <nTieRods> number of tierods
	 * @param <transverseConn> transverse connection value
	 * @param <JTRS1> is Joint tie tods applied on side 1
	 * @param <JTRS2> is Joint tie tods applied on side 2
	 */
	public GenerateViewsInPdf(float width, float height, float joint_spacing,
			String user_name, String company_name, String fabrication_report,
			Document document, String connection1, String connection2,
			boolean externalEnforcementS1,boolean externalEnforcementS2, boolean internalEnforcementS1,boolean internalEnforcementS2,
			int nTieRodsS1,int nTieRodsS2, int transverseConn, boolean JTRS1, boolean JTRS2) {
		if (height > 80 | width > 80)
			multiple = 1.5f;
		else if (height > 40 | width > 40)
			multiple = 2;
		else
			multiple = 4;

		// Use maximum value between height and width as Side1 and minimum as
		// Side2
		float temp = width; // Temporary variable to swap height and width
		if (height > width) {
			width = height;
			height = temp;
		} else {
			// No change required
		}
		z_axis = (int) (height * multiple);
		x_axis = (int) (width * multiple);
		actX = (int) width;
		actZ = (int) height;
		if (transverseConn == 6) {
			y_axis = (int) (((joint_spacing * 12) - 4) * multiple);
			actY = (int) ((joint_spacing * 12) - 4);
		} else {
			y_axis = (int) (joint_spacing * 12 * multiple);
			actY = (int) (joint_spacing * 12);
		}
		start_x = 100;
		start_y = 550;
		isExternalEnforcementS1 = externalEnforcementS1;
		isExternalEnforcementS2 = externalEnforcementS2;
		isInternalEnforcementS1 = internalEnforcementS1;
		isInternalEnforcementS2 = internalEnforcementS2;
		nTieRodsSide1 = nTieRodsS1;
		nTieRodsSide2 = nTieRodsS2;
		isJTRS1=JTRS1;
		isJTRS2=JTRS2;

		try {
			document.add(new Paragraph());
			LineSeparator ls = new LineSeparator();
			document.add(new Chunk(ls));
			Paragraph para = new Paragraph("Fabrication Report", FontFactory
					.getFont(BaseFont.TIMES_BOLD, 14, Font.BOLD));
			para.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
			document.add(para);
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("User Name : " + user_name));
			document.add(new Paragraph("Company Name : " + company_name));
			document.add(new Paragraph("\n"));

			document.add(new Paragraph(fabrication_report));
			document.newPage();
		} catch (DocumentException exception) {
			// TODO: handle exception
		}
	}

	/**
	 *
	 * @param <cb> instance of content byte to which pdf contents are to be added.
	 */
	public static void draw(PdfContentByte cb) {
		drawTopView(cb, start_x, start_y);
		drawFrontView(cb, start_x, start_y - y_axis - 45);
		drawSideView(cb, start_x + x_axis + 50, start_y - y_axis - 45);
		drawIsometricView(cb, start_x + x_axis + 60, start_y - y_axis + 10);
		drawNote(cb, 20, start_y - y_axis - 120 - z_axis);
	}

	/**
	 *
	 * @param <cb> instance of content byte to which pdf contents are to be added.
     * @param <sX> starting x coordinate.
     * @param <sY> starting y coordinate.
	 */
	private static void drawNote(PdfContentByte cb, float sX, float sY)
	{
	    cb.beginText();
        BaseFont bf = null , noteBf = null;
        try {
            bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252,
                    BaseFont.EMBEDDED);
            noteBf = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.CP1252,
                                         BaseFont.EMBEDDED);

        } catch (DocumentException exception) {
        } catch (IOException exception) {
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        cb.setFontAndSize(bf, 10);

        String text="Note : "+"Please consult the appropriate SMACNA standard for all requirements and for options not covered by this application.";

        cb.moveText(cut_point * sX , cut_point
                            * sY);
        cb.showText(text);
        cb.endText();
	}

	/**
	 *
	 * @param <cb> instance of content byte to which pdf contents are to be added.
	 * @param <sX> starting x coordinate.
	 * @param <sY> starting y coordinate.
	 */
	public static void drawTopView(PdfContentByte cb, float sX, float sY) {
		Rectangle rect = new Rectangle(cut_point * sX, cut_point * (sY - y_axis),
				cut_point * (sX + x_axis), cut_point * sY);
		rect.setBorder(Rectangle.BOX);
		rect.setBorderWidth(1);
		rect.setBorderColor(BaseColor.BLACK);
		cb.rectangle(rect);

		if (isExternalEnforcementS1 == true) {
			// middle line
			cb.moveTo(cut_point * (sX), cut_point * (sY - (y_axis / 2)));
			cb.setLineWidth(1.3f);
			cb.lineTo(cut_point * (sX + x_axis), cut_point * (sY - (y_axis / 2)));
			cb.stroke();


			drawVerticalDimensionLines(cb, String.valueOf(actY / 2), cut_point
					* (sX - 20), cut_point * (sY - (y_axis / 2)), y_axis / 2);
		}

		if(isInternalEnforcementS2 == true){
			//---------------------------For JTR Dotted Lines---------------------------------------------
			cb.setLineDash(6,2);
			cb.moveTo(cut_point	* (sX), cut_point * (sY - (y_axis / 2)));
			cb.lineTo(cut_point	* (sX + x_axis), cut_point * (sY - (y_axis / 2)));
//---------------------------------------------------------------------------------------------
			cb.stroke();
			cb.setLineDash(0) ;
		}
		if (isInternalEnforcementS1 == true) {
			float prev = sX;
			float next = 0;
			int radius = 3;
			cb.setLineWidth(1);
			for (int i = 1; i <= nTieRodsSide1; i++) {
				next = prev + x_axis / nTieRodsSide1;
				cb.circle(cut_point * ((prev + next) / 2), cut_point
						* (sY - (y_axis / 2)), radius);
				prev = next;
			}
			cb.stroke();
			drawVerticalDimensionLines(cb, String.valueOf(actY / 2), cut_point
					* (sX - 20), cut_point * (sY - (y_axis / 2)), y_axis / 2);
			// ---------------------------For JTR Dotted
			// Lines---------------------------------------------
//			cb.setLineDash(6, 2);
//			cb.moveTo(cut_point * (sX), cut_point * (sY - (y_axis / 2)));
//			cb.lineTo(cut_point * (sX + x_axis), cut_point
//					* (sY - (y_axis / 2)));
//			// ---------------------------------------------------------------------------------------------
//			cb.stroke();
//			cb.setLineDash(0);
		}




		if(isJTRS1)
		{
			int radius=3;

			cb.circle(cut_point * (sX + x_axis/2), cut_point
					* (sY - 8), radius);

			cb.circle(cut_point * (sX + x_axis/2), cut_point
					* (sY - y_axis + 8), radius);

			cb.stroke();
		}

		if(isJTRS2)
		{
			cb.setLineDash(6, 2);
			cb.moveTo(cut_point * (sX), cut_point * (sY - 8));
			cb.lineTo(cut_point * (sX + x_axis), cut_point * (sY - 8));

			cb.moveTo(cut_point * (sX), cut_point * (sY - y_axis + 8));
			cb.lineTo(cut_point * (sX + x_axis), cut_point * (sY - y_axis + 8));
			cb.stroke();
			cb.setLineDash(0);
		}

		drawVerticalDimensionLines(cb, String.valueOf(actY), cut_point
				* (sX + x_axis + 15), cut_point * sY, y_axis);
		drawHorizontalDimensionLines(cb, String.valueOf(actX),
				cut_point * (sX), cut_point * (sY - y_axis - 5), x_axis);


//		if (isExternalEnforcementS1 == true && isExternalEnforcementS2 == true) {
		if (isExternalEnforcementS2 == true) {
			cb.setLineWidth(1.4f);
			cb.moveTo(cut_point * (sX - 15) , cut_point * (sY - (y_axis / 2)));
			cb.lineTo(cut_point * (sX - 3) , cut_point * (sY - (y_axis / 2)));
			cb.lineTo(cut_point * (sX - 3), cut_point * (sY - (y_axis / 2) + 12));

			cb.moveTo(cut_point * (sX + x_axis + 15) , cut_point * (sY - (y_axis / 2)));
			cb.lineTo(cut_point * (sX + x_axis + 3) , cut_point * (sY - (y_axis / 2)));
			cb.lineTo(cut_point * (sX + x_axis + 3) ,  cut_point * (sY - (y_axis / 2) + 12));
			cb.stroke();
			cb.setLineWidth(1);
		}


		cb.beginText();
		BaseFont bf = null;
		try {
			bf = BaseFont.createFont(BaseFont.TIMES_BOLDITALIC,
					BaseFont.CP1252, BaseFont.EMBEDDED);
		} catch (DocumentException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		} catch (IOException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		cb.setFontAndSize(bf, 10);

		cb.moveText(cut_point * sX, cut_point * (sY - y_axis - 25));
		cb.showText(" a) Top View ");
		cb.endText();
	}

	/**
	 *
	 * @param <cb> instance of content byte to which pdf contents are to be added.
	 * @param <sX> starting x coordinate.
	 * @param <sY> starting y coordinate.
	 */
	private static void drawFrontView(PdfContentByte cb, float sX, float sY) {
		Rectangle rect = new Rectangle(cut_point * sX, cut_point * (sY - z_axis),
				cut_point * (sX + x_axis), cut_point * sY);
		rect.setBorder(Rectangle.BOX);
		rect.setBorderWidth(1);
		rect.setBorderColor(BaseColor.BLACK);
		cb.rectangle(rect);
		cb.stroke();

		if (isInternalEnforcementS1 == true) {
			float prev = sX;
			float next = 0;
			int radius = 1;
			cb.setLineWidth(1);
			cb.setLineDash(6,2);
			for (int i = 1; i <= nTieRodsSide1; i++) {
				next = prev + x_axis / nTieRodsSide1;
				cb.moveTo(cut_point * (((prev + next) / 2) - radius), cut_point
						* (sY));
				cb.lineTo(cut_point * (((prev + next) / 2) - radius), cut_point
						* (sY - z_axis));

				cb.moveTo(cut_point * (((prev + next) / 2) + radius), cut_point
						* (sY));
				cb.lineTo(cut_point * (((prev + next) / 2) + radius), cut_point
						* (sY - z_axis));
				prev = next;
			}
			cb.stroke();
			cb.setLineDash(0);
		}

		if (isInternalEnforcementS2 == true) {
			float prev = sY;
			float next = 0;
			int radius = 1;
			cb.setLineWidth(1);
			cb.setLineDash(6,2);
			for (int i = 1; i <= nTieRodsSide2; i++) {
				next = prev - z_axis / nTieRodsSide2;
				cb.moveTo( cut_point * (sX), cut_point * (((prev + next) / 2) - radius));
				cb.lineTo( cut_point * (sX + x_axis) ,cut_point * (((prev + next) / 2) - radius));

				cb.moveTo(cut_point * (sX) , cut_point * (((prev + next) / 2) + radius));
				cb.lineTo(cut_point	* (sX + x_axis) , cut_point * (((prev + next) / 2) + radius));
				prev = next;
			}
			cb.stroke();
			cb.setLineDash(0);
		}

		if(isJTRS1)
		{
			int radius = 1;
			cb.setLineDash(6,2);
			cb.moveTo(cut_point * (sX + x_axis/2 - radius), cut_point * sY);
			cb.lineTo(cut_point * (sX + x_axis/2 - radius), cut_point * (sY - z_axis));

			cb.moveTo(cut_point * (sX + x_axis/2 + radius), cut_point * sY);
			cb.lineTo(cut_point * (sX + x_axis/2 + radius), cut_point * (sY - z_axis));
			cb.stroke();
			cb.setLineDash(0);
		}

		if(isJTRS2)
		{
			int radius = 1;
			cb.setLineDash(6,2);
			cb.moveTo(cut_point * (sX), cut_point * (sY - z_axis/2 + radius));
			cb.lineTo(cut_point * (sX + x_axis), cut_point * (sY - z_axis/2 + radius));

			cb.moveTo(cut_point * (sX), cut_point * (sY - z_axis/2 - radius));
			cb.lineTo(cut_point * (sX + x_axis), cut_point * (sY - z_axis/2 - radius));

			cb.stroke();
			cb.setLineDash(0);
		}

		drawHorizontalDimensionLines(cb, String.valueOf(actX),
				cut_point * (sX), cut_point * (sY - 5 - z_axis), x_axis);
		drawVerticalDimensionLines(cb, String.valueOf(actZ), cut_point
				* (sX + x_axis + 5), cut_point * (sY), z_axis);

		cb.beginText();
		BaseFont bf = null;
		try {
			bf = BaseFont.createFont(BaseFont.TIMES_BOLDITALIC,
					BaseFont.CP1252, BaseFont.EMBEDDED);
		} catch (DocumentException exception) {
			exception.printStackTrace();
		} catch (IOException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
		cb.setFontAndSize(bf, 10);

		cb.moveText(cut_point * (sX), cut_point * (sY - 25 - z_axis));
		cb.showText(" b) Front View ");
		cb.endText();
	}

	/**
	 *
	 * @param <cb> instance of content byte to which pdf contents are to be added.
	 * @param <sX> starting x coordinate.
	 * @param <sY> starting y coordinate.
	 */
	private static void drawSideView(PdfContentByte cb, float sX, float sY) {
		Rectangle rect = new Rectangle(cut_point * (sX), cut_point * (sY - z_axis),
				cut_point * (sX + y_axis), cut_point * (sY));
		rect.setBorder(Rectangle.BOX);
		rect.setBorderWidth(1);
		rect.setBorderColor(BaseColor.BLACK);
		cb.rectangle(rect);
		cb.stroke();
//		if (isExternalEnforcementS1 == true && isExternalEnforcementS2 == true) {
		if (isExternalEnforcementS1 == true) {
			// middle line

			cb.setLineWidth(1.4f);
			cb.moveTo(cut_point * (sX + (y_axis / 2)), cut_point * (sY + 15));
			cb.lineTo(cut_point * (sX + (y_axis / 2)), cut_point * (sY + 3));
			cb.lineTo(cut_point * (sX + (y_axis / 2) + 12), cut_point * (sY + 3));

			cb.moveTo(cut_point * (sX + (y_axis / 2)), cut_point * (sY - z_axis - 15));
			cb.lineTo(cut_point * (sX + (y_axis / 2)), cut_point * (sY - z_axis - 3));
			cb.lineTo(cut_point * (sX + (y_axis / 2) + 12), cut_point * (sY - z_axis - 3));
			cb.stroke();
			cb.setLineWidth(1);

			drawHorizontalDimensionLines(cb, String.valueOf(actY / 2),
					cut_point * (sX), cut_point * (sY + 20), y_axis / 2);
		}
		if (isInternalEnforcementS1 == true || isExternalEnforcementS2 == true) {
			cb.moveTo(cut_point * (sX + (y_axis / 2)), cut_point * (sY));
			if(isInternalEnforcementS1)
				cb.setLineDash(6, 2);
			if(isExternalEnforcementS2){
				cb.setLineDash(0);
				cb.setLineWidth(1.3f);
			}
			cb.lineTo(cut_point * (sX + (y_axis / 2)), cut_point * (sY - z_axis));
			cb.stroke();

			cb.setLineDash(0);
			drawHorizontalDimensionLines(cb, String.valueOf(actY / 2),
					cut_point * (sX), cut_point * (sY + 20), y_axis / 2);
		}

		if(isInternalEnforcementS2)
		{
			float prev = sY;
			float next = 0;
			int radius = 3;
			cb.setLineWidth(1);
			for (int i = 1; i <= nTieRodsSide2; i++) {
				next = prev - z_axis / nTieRodsSide2;
				cb.circle( cut_point * (sX + (y_axis / 2)),cut_point * ((prev + next) / 2), radius);
				prev = next;
			}
			cb.stroke();
		}

		if(isJTRS2)
		{
			int radius=3;

			cb.circle(cut_point * (sX + 8), cut_point
					* (sY - z_axis/2), radius);

			cb.circle(cut_point * (sX + y_axis - 8), cut_point
					* (sY - z_axis/2), radius);

			cb.stroke();
		}

		if(isJTRS1)
		{
			cb.setLineDash(6, 2);
			cb.moveTo(cut_point * (sX + 8), cut_point * (sY));
			cb.lineTo(cut_point * (sX + 8), cut_point * (sY - z_axis));

			cb.moveTo(cut_point * (sX + y_axis - 8), cut_point * (sY));
			cb.lineTo(cut_point * (sX + y_axis - 8), cut_point * (sY - z_axis));
			cb.stroke();
			cb.setLineDash(0);
		}

		drawHorizontalDimensionLines(cb, String.valueOf(actY),
				cut_point * (sX), cut_point * (sY - 20 - z_axis), y_axis);

		cb.beginText();
		BaseFont bf = null;
		try {
			bf = BaseFont.createFont(BaseFont.TIMES_BOLDITALIC,
					BaseFont.CP1252, BaseFont.EMBEDDED);
		} catch (DocumentException exception) {
			exception.printStackTrace();
		} catch (IOException exception) {
			exception.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		cb.setFontAndSize(bf, 10);

		cb.moveText(cut_point * (sX), cut_point * (sY - 40 - z_axis));
		cb.showText(" c) Side View ");
		cb.endText();
	}

	/**
	 *
	 * @param <cb> instance of content byte to which pdf contents are to be added.
	 * @param <sX> starting x coordinate.
	 * @param <sY> starting y coordinate.
	 */
	private static void drawIsometricView(PdfContentByte cb, float sX, float sY) {
	    int ty = y_axis / 2;
	    Rectangle rect = new Rectangle(cut_point * (sX), cut_point * (sY),
	                                   cut_point * (sX + x_axis), cut_point * (sY + z_axis));
	    rect.setBorder(Rectangle.BOX);
	    rect.setBorderWidth(1);
	    rect.setBorderColor(BaseColor.BLACK);
	    rect.setBackgroundColor(BaseColor.WHITE);
	    cb.rectangle(rect);
	    cb.stroke();

	    if (multiple == 1.5)
	        temp_slant = cut_point * 25;
	    else if (multiple == 2)
	        temp_slant = cut_point * 40;
	    else
	        temp_slant = cut_point * 50;

	    temp_height = (int) Math.sqrt(((cut_point * ty) * (cut_point * ty))
	                                  - (cut_point * (temp_slant) * (cut_point * temp_slant)));

	    // left side rectangle line
	    cb.setLineWidth(1);
	    cb.setLineJoin(PdfContentByte.LINE_JOIN_MITER);
	    cb.moveTo(cut_point * (sX), cut_point * (sY));
	    cb
	    .lineTo(cut_point * (sX + temp_slant), cut_point
	            * (sY + temp_height));
	    cb.lineTo(cut_point * (sX + temp_slant), cut_point
	              * (sY + z_axis + temp_height));
	    cb
	    .moveTo(cut_point * (sX + temp_slant), cut_point
	            * (sY + temp_height));
	    cb.lineTo(cut_point * (sX + x_axis + temp_slant), cut_point
	              * (sY + temp_height));
	    cb.stroke();

	    if (isInternalEnforcementS1 == true) {
	        float prev = sX;
	        float next = 0;
	        int radius = 1;
	        cb.setLineWidth(1);
	        for (int i = 1; i <= nTieRodsSide1; i++) {
	            next = prev + x_axis / nTieRodsSide1;
	            cb.setLineDash(6, 2);

	            cb.moveTo(cut_point
	                      * (((prev + next + temp_slant) / 2) - radius - 1),
	                      cut_point * (sY + z_axis + (temp_height / 2)));
	            cb.lineTo(cut_point
	                      * (((prev + next + temp_slant) / 2) - radius - 1),
	                      cut_point * (sY + (temp_height / 2)));

	            cb.moveTo(cut_point
	                      * (((prev + next + temp_slant) / 2) + radius - 1),
	                      cut_point * (sY + z_axis + (temp_height / 2)));
	            cb.lineTo(cut_point
	                      * (((prev + next + temp_slant) / 2) + radius - 1),
	                      cut_point * (sY + (temp_height / 2)));

	            cb.circle(cut_point * ((prev + next + temp_slant) / 2 - 1),
	                      cut_point * (sY + (temp_height / 2)), radius);

	            prev = next;
	        }
	        cb.stroke();
	    }

	    if (isInternalEnforcementS2 == true) {
	        float prev = sY;
	        float next = 0;
	        int radius = 1;
	        cb.setLineWidth(1);
	        for (int i = 1; i <= nTieRodsSide2; i++) {
	            next = prev + z_axis / nTieRodsSide2;
	            cb.setLineDash(6, 2);
	            cb.moveTo(cut_point * (sX + x_axis + (temp_slant / 2)) , cut_point * (((prev + next + temp_height) / 2)+radius - 1));
	            cb.lineTo(cut_point * (sX + (temp_slant / 2)) , cut_point * (((prev + next + temp_height) / 2)+radius - 1));

	            cb.moveTo(cut_point * (sX + x_axis + (temp_slant / 2)) , cut_point * (((prev + next + temp_height) / 2)-radius - 1));
	            cb.lineTo(cut_point * (sX + (temp_slant / 2)) , cut_point * (((prev + next + temp_height) / 2)-radius - 1));
	            cb.circle(cut_point * (sX + (temp_slant / 2)) , cut_point * ((prev + next + temp_height) / 2 - 1) , radius);

	            prev = next;
	        }
	        cb.stroke();
	    }

	    cb.setLineDash(0);

	    if (isJTRS1) {

	        int radius = 1;
	        cb.setLineWidth(1);

	        cb.setLineDash(4,2);
	        cb.moveTo(cut_point * (sX + x_axis/2 - radius + 5),
	                  cut_point * (sY + z_axis + 5));
	        cb.lineTo(cut_point * (sX + x_axis/2 - radius + 5),
	                  cut_point * (sY + 5));

	        cb.moveTo(cut_point * (sX + x_axis/2 + radius + 5),
	                  cut_point * (sY + z_axis + 5));
	        cb.lineTo(cut_point * (sX + x_axis/2 + radius + 5),
	                  cut_point * (sY + 5));

	        cb.moveTo(cut_point * (sX + x_axis/2 -5 - radius + temp_slant),
	                  cut_point * (sY + z_axis - 5 + temp_height ));
	        cb.lineTo(cut_point * (sX + x_axis/2 - 5 -radius + temp_slant),
	                  cut_point * (sY - 5 + temp_height));

	        cb.moveTo(cut_point * (sX + x_axis/2 - 5 + radius + temp_slant),
	                  cut_point * (sY + z_axis - 5 + temp_height));
	        cb.lineTo(cut_point * (sX + x_axis/2 -5 + radius + temp_slant),
	                  cut_point * (sY - 5 + temp_height));

	        cb.stroke();

	        cb.setLineDash(0);

	        cb.circle(cut_point * (sX + x_axis/2 + 5),
	                  cut_point * (sY + 5 + radius), radius);
	        cb.circle(cut_point * (sX + x_axis/2 - 5 + temp_slant),
	                  cut_point * (sY - 5 + temp_height - radius), radius);
	        cb.stroke();
	    }

	    if (isJTRS2) {
	        int radius = 1;
	        cb.setLineWidth(1);
	        cb.setLineDash(4,2);
	        cb.moveTo(cut_point * (sX + 5),
	                  cut_point * (sY + z_axis/2 -radius + 5));
	        cb.lineTo(cut_point * (sX + x_axis + 5),
	                  cut_point * (sY + z_axis/2 -radius + 5));

	        cb.moveTo(cut_point * (sX + 5),
	                  cut_point * (sY + z_axis/2 + radius + 5));
	        cb.lineTo(cut_point * (sX + x_axis + 5),
	                  cut_point * (sY + z_axis/2 + radius + 5));

	        cb.moveTo(cut_point * (sX + temp_slant - 5),
	                  cut_point * (sY + z_axis/2 -radius + temp_height - 5));
	        cb.lineTo(cut_point * (sX + x_axis - 5 + temp_slant),
	                  cut_point * (sY + z_axis/2 -radius + temp_height - 5));

	        cb.moveTo(cut_point * (sX + temp_slant - 5),
	                  cut_point * (sY + z_axis/2 + radius + temp_height - 5));
	        cb.lineTo(cut_point * (sX + x_axis - 5 + temp_slant),
	                  cut_point * (sY + z_axis/2 + radius + temp_height - 5));

	        cb.stroke();

	        cb.setLineDash(0);

	        cb.circle(cut_point * (sX + 5 + radius),
	                  cut_point * (sY + z_axis/2 + 5), radius);

	        cb.circle(cut_point * (sX - 5 + temp_slant - radius),
	                  cut_point * (sY + z_axis/2 + temp_height - 5), radius);

	        cb.stroke();
	    }
	    cb.setLineDash(0);

	    // right side rectangle
	    cb.moveTo(cut_point * (sX + x_axis), cut_point * (sY));
	    cb.lineTo(cut_point * (sX + x_axis + temp_slant), cut_point
	              * (sY + temp_height));
	    cb.lineTo(cut_point * (sX + x_axis + temp_slant), cut_point
	              * (sY + temp_height + z_axis));
	    cb.lineTo(cut_point * (sX + x_axis), cut_point * ((sY) + z_axis));
	    cb.setColorStroke(BaseColor.BLACK);
	    cb.setColorFill(BaseColor.WHITE);
	    cb.closePathFillStroke();

	    // top rectangle
	    cb.moveTo(cut_point * (sX + x_axis), cut_point * ((sY) + z_axis));
	    cb.lineTo(cut_point * (sX + x_axis + temp_slant), cut_point
	              * (sY + z_axis + temp_height));
	    cb.lineTo(cut_point * (sX + temp_slant), cut_point
	              * (sY + z_axis + temp_height));
	    cb.lineTo(cut_point * (sX), cut_point * (sY + z_axis));
	    cb.setColorStroke(BaseColor.BLACK);
	    cb.setColorFill(BaseColor.WHITE);
	    cb.closePathFillStroke();
		if (isJTRS1)
	     {
	         int radius=1;

	         cb.circle(cut_point * (sX + x_axis/2 + 5),
	                   cut_point * (sY + z_axis - radius + 5), radius);
	         cb.circle(cut_point * (sX + x_axis/2 + temp_slant - 5),
	                   cut_point * (sY + z_axis - 5 + temp_height + radius), radius);
	         cb.stroke();
	     }

	     if(isJTRS2)
	     {
	         int radius=1;
	         cb.circle(cut_point * (sX + x_axis + 5 - radius),
	                   cut_point * (sY + z_axis/2 + 5), radius);
	         cb.circle(cut_point * (sX + x_axis - 5 + temp_slant + radius),
	                   cut_point * (sY + z_axis/2 - 5 + temp_height), radius);
	         cb.stroke();
	     }

	     if (isInternalEnforcementS1 == true) {
	         float prev = sX;
	         float next = 0;
	         int radius = 1;
	         cb.setLineWidth(1);
	         for (int i = 1; i <= nTieRodsSide1; i++) {
	             next = prev + x_axis / nTieRodsSide1;

	             cb.circle(cut_point * ((prev + next + temp_slant) / 2 - 1),
	                       cut_point * (sY + z_axis + (temp_height / 2)), radius);

	             prev = next;
	         }
	         cb.stroke();
	     }

	     if (isInternalEnforcementS2 == true) {
	         float prev = sY;
	         float next = 0;
	         int radius = 1;
	         cb.setLineWidth(1);
	         for (int i = 1; i <= nTieRodsSide2; i++) {
	             next = prev + z_axis / nTieRodsSide2;
	             cb.circle(cut_point * (sX + x_axis + (temp_slant / 2)) , cut_point * ((prev + next + temp_height) / 2 - 1) , radius);
	             prev = next;
	         }
	         cb.stroke();
	     }



	     if (isExternalEnforcementS1 == true) {
	         System.out.println("external s1 true");
	         // top lines showing half cuboid
	         cb.moveTo(cut_point * (sX + (temp_slant / 2)), cut_point
	                   * (sY + z_axis + (temp_height / 2)));
	         cb.setLineWidth(1.3f);


	         cb.lineTo(cut_point * (sX + x_axis + (temp_slant / 2)), cut_point
	                   * (sY + z_axis + (temp_height / 2)));
	         //          cb.lineTo(cut_point * (sX + x_axis + (temp_slant / 2)), cut_point
	         //                  * (sY + (temp_height / 2)));
	         cb.stroke();

	     }else
	         System.out.println("external s1 false");
	     if(isExternalEnforcementS2==true)
	     {
	         System.out.println("external s2 true");
	         cb.moveTo(cut_point * (sX + x_axis + (temp_slant / 2)), cut_point
	                   * (sY + z_axis + (temp_height / 2)));
	         cb.lineTo(cut_point * (sX + x_axis + (temp_slant / 2)), cut_point
	                   * (sY + (temp_height / 2)));
	         cb.stroke();
	     }else
	         System.out.println("external s2 false");

	     cb.beginText();
	     cb.setColorStroke(BaseColor.BLACK);
	     cb.setColorFill(BaseColor.BLACK);
	     BaseFont bf = null;
	     try {
	         bf = BaseFont.createFont(BaseFont.TIMES_BOLDITALIC,
	                                  BaseFont.CP1252, BaseFont.EMBEDDED);
	     } catch (DocumentException exception) {
	         exception.printStackTrace();
	     } catch (IOException exception) {
	         exception.printStackTrace();
	     } catch (Exception exception) {
	         // TODO Auto-generated catch block
	         exception.printStackTrace();
	     }
	     cb.setFontAndSize(bf, 10);

	     cb.moveText(cut_point * (sX), cut_point * (sY - 15));
	     cb.showText(" d) Isometric View ");
	     cb.endText();

	     cb.stroke();
	}

	private static void drawHorizontalDimensionLines(PdfContentByte cb,
			String text, float spX, float spY, float dW) {
		cb.moveTo(cut_point * spX, cut_point * spY);
		cb.setLineWidth(0.2f);
		cb.lineTo(cut_point * spX, cut_point * (spY - 10));

		cb.moveTo(cut_point * spX, cut_point * (spY - 5));
		cb.setLineWidth(0.2f);
		cb.lineTo(cut_point * (spX + (dW / 2) - 7), cut_point * (spY - 5));
		cb.stroke();

		cb.moveTo(cut_point * spX, cut_point * (spY - 5));
		cb.setLineWidth(0.2f);
		cb.lineTo(cut_point * (spX) + 4, cut_point * (spY - 5) - 1);
		cb.lineTo(cut_point * (spX) + 4, cut_point * (spY - 5) + 1);
		cb.closePath();
		cb.setColorFill(BaseColor.BLACK);
		cb.fillStroke();

		cb.moveTo(cut_point * (spX + dW), cut_point * spY);
		cb.setLineWidth(0.2f);
		cb.lineTo(cut_point * (spX + dW), cut_point * (spY - 10));

		cb.moveTo(cut_point * (spX + dW), cut_point * (spY - 5));
		cb.setLineWidth(0.2f);
		cb.lineTo(cut_point * (spX + (dW / 2) + 9), cut_point * (spY - 5));
		cb.stroke();

		cb.moveTo(cut_point * (spX + dW), cut_point * (spY - 5));
		cb.setLineWidth(0.2f);
		cb.lineTo(cut_point * (spX + dW) - 4, cut_point * (spY - 5) - 1);
		cb.lineTo(cut_point * (spX + dW) - 4, cut_point * (spY - 5) + 1);
		cb.closePath();
		cb.setColorFill(BaseColor.BLACK);
		cb.fillStroke();

		cb.beginText();
		BaseFont bf = null;
		try {
			bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252,
					BaseFont.EMBEDDED);
		} catch (DocumentException exception) {
			exception.printStackTrace();
		} catch (IOException exception) {
			exception.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		cb.setFontAndSize(bf, 8);
		if (text.length() > 2)
			cb.moveText(cut_point * (spX + (dW / 2) - 5), cut_point
							* (spY - 8));
		else
			cb.moveText(cut_point * (spX + (dW / 2) - 2), cut_point
							* (spY - 8));
		cb.showText(text);
		cb.endText();
	}

	private static void drawVerticalDimensionLines(PdfContentByte cb,
			String text, float spX, float spY, float dH) {
		cb.moveTo(cut_point * spX, cut_point * spY);
		cb.setLineWidth(0.2f);
		cb.lineTo(cut_point * (spX + 10), cut_point * spY);

		cb.moveTo(cut_point * (spX + 5), cut_point * spY);
		cb.setLineWidth(0.2f);
		cb.lineTo(cut_point * (spX + 5), cut_point * (spY + 5 - (dH / 2)));
		cb.stroke();

		cb.moveTo(cut_point * (spX + 5), cut_point * spY);
		cb.setLineWidth(0.2f);
		cb.lineTo(cut_point * (spX + 5) + 1, cut_point * (spY) - 4);
		cb.lineTo(cut_point * (spX + 5) - 1, cut_point * (spY) - 4);
		cb.closePath();
		cb.setColorFill(BaseColor.BLACK);
		cb.fillStroke();

		cb.moveTo(cut_point * (spX), cut_point * (spY - dH));
		cb.setLineWidth(0.2f);
		cb.lineTo(cut_point * (spX + 10), cut_point * (spY - dH));

		cb.moveTo(cut_point * (spX + 5), cut_point * (spY - dH));
		cb.setLineWidth(0.2f);
		cb.lineTo(cut_point * (spX + 5), cut_point * (spY - 5 - (dH / 2)));
		cb.stroke();

		cb.moveTo(cut_point * (spX + 5), cut_point * (spY - dH));
		cb.setLineWidth(0.2f);
		cb.lineTo(cut_point * (spX + 5) + 1, cut_point * (spY - dH) + 4);
		cb.lineTo(cut_point * (spX + 5) - 1, cut_point * (spY - dH) + 4);
		cb.closePath();
		cb.setColorFill(BaseColor.BLACK);
		cb.fillStroke();

		cb.beginText();
		BaseFont bf = null;
		try {
			bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252,
					BaseFont.EMBEDDED);
		} catch (DocumentException exception) {
			exception.printStackTrace();
		} catch (IOException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
		cb.setFontAndSize(bf, 8);
		cb.moveText(cut_point * (spX + 2), cut_point * (spY - (dH / 2) - 3));
		cb.showText(text);
		cb.endText();
	}
}
