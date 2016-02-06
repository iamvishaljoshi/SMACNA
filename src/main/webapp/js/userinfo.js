var username = document.getElementById("userName");
var email = document.getElementById("email");
var company = document.getElementById("company");
var contactno= document.getElementById("contactNo");
if(username.value==null||username.value=="")
	{
	username.value="User Name (Optional)";
	}
if(company.value==null||company.value=="")
	{
	company.value="Company (Optional)";
	}
if(contactno.value==null||contactno.value=="")
{
	contactno.value="Contact Number (Optional)";
}
if(email.value==null||email.value=="")
{
	email.value="Email (Optional)";
}
function userNameOnFocus()
{
	var username = document.getElementById("userName");
	if (username.value=="User Name (Optional)"){username.value=''};
}
function userNameOnBlur()
{
	var username = document.getElementById("userName");
	if (username.value==''){username.value="User Name (Optional)"};
}

function validate() {
	var valid = validateAllFields();
	if (valid) {
		document.forms["contact"].submit();
	}
}
function validateAllFields() {

	return true;

}