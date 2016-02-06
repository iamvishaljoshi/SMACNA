<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml"> <head> <meta
	http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="title.smacna"></spring:message></title>
<link href="css/style.css" type="text/css" rel="stylesheet" /> <meta
	name="viewport"
	content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;">
<script type="application/x-javascript">

</script> <meta name="apple-mobile-web-app-capable" content="yes" /> <script
	src="js/jquery.js"></script>

<script	type="text/javascript">
var contextPath = "<c:out value="${pageContext.request.contextPath}" />";
</script>
<script src="js/viewreport.js"></script>

<script src="js/mailreport.js"></script>

</head> <body> <!-- Header Start --> <div id="header"> <!-- Logo Start -->
<div id="logo"> <h1> <a href="index.html"> <img
	src="images/logo.png" alt="HOME" /> </a> </h1> </div> <!-- Logo End --> <!-- Navigation Start -->
<div id="nav"> <ul> <li><a class="first current"
	href="index.html"><spring:message code="button.home"></spring:message></a></li> <li><a href="aboutUs.html"><spring:message code="button.aboutUs"></spring:message></a></li>
<li class="last"><a href="contactUs.html" class="last"><spring:message code="button.contactUs"></spring:message></a></li> </ul> </div> <!-- Navigation End --> <div class="clr"></div> </div> <!-- Header End -->
<!-- Content Body Start --> <div id="content-body"> <div
	class="content-container"> <h2>Show More Options</h2> 
	
 <!-- Button Start --> <div class="result-btn"> 
 
 <form:form
	commandName="contact" action="submit">
	<input type="hidden" value="editUserInputs" name="editDetails" />
	<span><input type="submit" name="option" value="See Only Internal Reinforcements" title="See Only Internal Reinforcements" class="showMoreOptionbut"
		/></span>
	<span> <input type="submit" name="option"
		value="See Only External Reinforcements" title="See Only External Reinforcements" class="showMoreOptionbut"
		 /></span>
	<span><input type="submit" name="option"
		value="See All Results" title="See All Results" class="showMoreOptionbut"
		/></span>
</form:form> </div> <!-- Button End --> </div> </div> <!-- Content Body End --> 

	<div id="footer">
		<div class="footer-container">
			<a href="http://www.smacna.org/bookstore/" class="bookstore" ><spring:message code="button.bookstore"></spring:message></a>
	    </div>
	</div>
	</body> </html>
