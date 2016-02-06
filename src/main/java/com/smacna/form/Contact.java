package com.smacna.form;

import javax.validation.constraints.Size;



public class Contact {


	private String  userName;

	@Size(min=10,max=15)
	private String  contactNo;


	private String company;

    private String email;

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






}
