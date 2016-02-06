<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="title.smacna"></spring:message></title>
<link href="css/style.css" type="text/css" rel="stylesheet" />
<meta name="viewport"
	content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;">
<script type="application/x-javascript">

</script>
<meta name="apple-mobile-web-app-capable" content="yes" />


</head>
<body>
	<!-- Header Start -->
	<div id="header">
		<!-- Logo Start -->
		<div id="logo">
			<h1>
				<a href="index.html"> <img src="images/logo.png" alt="HOME" />
				</a>
			</h1>
		</div>
		<!-- Logo End -->
		<!-- Navigation Start -->
		<div id="nav">
			<ul>
				<li><a class="first current" href="index.html"><spring:message code="button.home"></spring:message></a></li>
				<li><a href="aboutUs"><spring:message code="button.aboutUs"></spring:message></a></li>
				<li class="last"><a href="contactUs" class="last"><spring:message code="button.contactUs"></spring:message></a></li>
			</ul>
		</div>
		<!-- Navigation End -->
		<div class="clr"></div>
	</div>
	<!-- Header End -->
	<!-- Content Body Start -->
	<div id="content-body">
		<div class="content-container">
			<h2><spring:message code="label.userInformation"></spring:message></h2>
			<c:choose>
			<c:when test="${!empty edit}">
			<c:set var="action">editnext.html</c:set>
			</c:when>
			<c:otherwise>
			<c:set var="action">next.html</c:set>
			</c:otherwise>
			</c:choose>
			<form:form method="post" action="${action}" commandName="contact"
				onsubmit="validate()">
				<form:errors path="userName" cssClass="error" />
				<form:input path="userName" name=""
					onfocus="userNameOnFocus();"
					onblur="userNameOnBlur();" />
				<form:errors path="contactNo" cssClass="error" />
				<form:input path="contactNo" name=""
					onfocus="if (this.value=='Contact Number (Optional)'){this.value=''};"
					onblur="if (this.value==''){this.value='Contact Number (Optional)'};" />
				<form:errors path="company" cssClass="error" />
				<form:input path="company" name=""
					onfocus="if (this.value=='Company (Optional)'){this.value=''};"
					onblur="if (this.value==''){this.value='Company (Optional)'};" />
				<form:errors path="email" cssClass="error" />
				<form:input path="email" name=""
					onfocus="if (this.value=='Email (Optional)'){this.value=''};"
					onblur="if (this.value==''){this.value='Email (Optional)'};" />
				<!-- Next Button Start -->
				<form:errors path="agree" cssClass="error" />
				</br>
				<form:checkbox path="agree" id="checkbox3" />
				<font color="white">I agree to the SMACNA <a href="http://www.smacna.org/terms/cdcs/" class="error wrapLink">"Terms of Use"</a></font>
				
				<div class="next-btn">
					<a href="#" class="next"
						onclick="validate()" ><spring:message code="label.next"></spring:message></a>
				</div>



			</form:form>
			<!-- Next Button End -->
		</div>
	</div>
	<!-- Content Body End -->
	<div id="footer">
		<div class="footer-container">
			<a href="http://www.smacna.org/bookstore/" class="bookstore" ><spring:message code="button.bookstore"></spring:message></a>
	    </div>
	</div>
<script src="js/userinfo.js">
</script>

</body>
</html>
