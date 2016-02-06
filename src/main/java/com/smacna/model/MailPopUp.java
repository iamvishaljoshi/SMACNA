/**
 *
 */
package com.smacna.model;

/**
 * @author sumit.v
 * @version 1.0
 */
public class MailPopUp {

    String to;
    String cc;
    String bcc;
    /**
     * @return the bcc
     */
    public String getBcc() {
        return bcc;
    }
    /**
     * @param bcc the bcc to set
     */
    public void setBcc(String bcc) {
        this.bcc = bcc;
    }
    /**
     * @return the to
     */
    public String getTo() {
        return to;
    }
    /**
     * @param to the to to set
     */
    public void setTo(String to) {
        this.to = to;
    }
    /**
     * @return the cc
     */
    public String getCc() {
        return cc;
    }
    /**
     * @param cc the cc to set
     */
    public void setCc(String cc) {
        this.cc = cc;
    }

}
