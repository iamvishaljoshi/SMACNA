<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="title.smacna"></spring:message></title>
<link href="css/style.css" type="text/css" rel="stylesheet" />
<meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;">
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(
hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
<meta name="apple-mobile-web-app-capable" content="yes" />
<script type="text/javascript">
function mailFabricationReport(form)
	{
	form.submit();
	}
	</script>
</head>
<body>
<!-- Header Start -->
<div id="header">
  <!-- Logo Start -->
  <div id="logo">
    <h1> <a href="index.html"> <img src="images/logo.png" alt="HOME" /> </a> </h1>
  </div>
  <!-- Logo End -->
  <!-- Navigation Start -->
  <div id="nav">
    <ul>
      <li><a class="first current" href="index.html"><spring:message code="button.home"></spring:message></a></li>
      <li><a href="aboutUs.html"><spring:message code="button.aboutUs"></spring:message></a></li>
      <li class="last"><a href="contactUs.html" class="last"><spring:message code="button.contactUs"></spring:message></a></li>
    </ul>
  </div>
  <!-- Navigation End -->
  <div class="clr"></div>
</div>
<!-- Header End -->
<!-- Content Body Start -->
<div id="content-body">
  <div class="content-container">
    <div class="pop-up-container">
    <a href="#" class="close-icon"></a>
     <form:form action="mailFabricationReport" commandName="mailPopUp">
     <spring:message code="email.separator"></spring:message><br/>
     <form:errors path="to" cssClass="error" />
     <form:input path="to" name="" onfocus="if (this.value=='To'){this.value=''};" onblur="if (this.value==''){this.value='To'};"/>
     <form:input path="cc" name="" onfocus="if (this.value=='Cc'){this.value=''};" onblur="if (this.value==''){this.value='Cc'};"/>
    <form:input path="bcc" name="" onfocus="if (this.value=='Bcc'){this.value=''};" onblur="if (this.value==''){this.value='Bcc'};"/>
     <div class="send">
        <input type="hidden" name="hiddenOpt" value="${hiddenOpt}" />
        <input type="submit" title="Send" value="Send" class="btn" onclick="mailFabricationReport(this.form);"/>
      </div>

    </form:form>
    </div>
    </div>
</div>
<!-- Content Body End -->
	<div id="footer">
		<div class="footer-container">
			<a href="http://www.smacna.org/bookstore/" class="bookstore" ><spring:message code="button.bookstore"></spring:message></a>
	    </div>
	</div>
<script type="text/javascript">
var to = document.getElementById("to");
var cc = document.getElementById("cc");
var bcc = document.getElementById("bcc");
if(to.value==null||to.value==""){
	to.value="To";
}
if(cc.value==null||cc.value==""){
	cc.value="Cc";
}
if(bcc.value==null||bcc.value==""){
	bcc.value="Bcc";
}
</script>
</body>
</html>
