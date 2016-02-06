package com.smacna.form;

import javax.validation.constraints.Pattern;

import org.springmodules.validation.bean.conf.loader.annotation.handler.Email;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Size;


/**
 * 
 * @author sumit.v
 * @version 1.0
 */
public class ContactDTO {


	private String  userName;

	@Size(min=10,max=15)
	private String  contactNo;


	private String company;

    @Email
    @Pattern(regexp="(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    private String email;
    
    private boolean agree;

	/**
     * @return the username
     */
    public String getUserName() {
        return userName;
    }


    /**
     * @param username the username to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }


    /**
     * @return the contactno
     */
    public String getContactNo() {
        return contactNo;
    }


    /**
     * @param contactno the contactno to set
     */
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }


    /**
     * @return the company
     */
    public String getCompany() {
        return company;
    }


    /**
     * @param company the company to set
     */
    public void setCompany(String company) {
        this.company = company;
    }


    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }


    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }


	public boolean isAgree() {
		return agree;
	}


	public void setAgree(boolean agree) {
		this.agree = agree;
	}






}
