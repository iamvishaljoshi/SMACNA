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
	class="content-container"> <h2>Result</h2> 
	
	
	<c:forEach
	items="${outPutScreens}" var="output" varStatus="counter">
	<p style="display: block;"> <input type="radio" name="opt" value="${counter.count}" /> <spring:message
		code="label.gage" arguments="${output.finalDuctGage}"></spring:message> 
	<c:if
		test="${output.noRenforcement && output.jtrForSideOne && !output.jtrForSideTwo}">
		<spring:message code="message.withSingleJTR"
				arguments="${output.sideOne}"></spring:message>
	</c:if>
	<c:if
		test="${output.noRenforcement && !output.jtrForSideOne && output.jtrForSideTwo}">
		<spring:message code="message.withSingleJTR"
				arguments="${output.sideTwo}"></spring:message>
	</c:if>
	<c:set var="isSet" value="false"></c:set>


	<c:if test="${output.extRenfSide == output.sideOne}">
		<c:if test="${output.externalReinforcement && ((!output.jtrForSideOne && !output.jtrForSideTwo)||(output.jtrForSideOne && output.jtrForSideTwo))}">
			<spring:message code="message.withExtRenfSide"
				arguments="${output.extRenfSide}"></spring:message>
		</c:if>
		<c:if test="${output.internalReinforcement && ((!output.jtrForSideOne && !output.jtrForSideTwo)||(output.jtrForSideOne && output.jtrForSideTwo))}">
			<spring:message code="message.internalReinforcement"
				arguments="${output.extRenfSide}"></spring:message>
		</c:if>
			<c:set var="isSet" value="true"></c:set>
	</c:if>
	<c:if
		test="${!output.externalReinforcement && !output.internalReinforcement && output.externalBothSide && ((!output.jtrForSideOne && !output.jtrForSideTwo)||(output.jtrForSideOne && output.jtrForSideTwo))}">
		<spring:message code="message.withExtRenfBothSide"
			arguments="${output.sideOne},${output.sideTwo}" argumentSeparator=","></spring:message>
	</c:if>

	<!--<c:if test = "${output.externalReinforcement && output.intAndExtOnSideOne}">
		<spring:message code="message.extAndInterReinBothSideAndExtOnSideTwo"
			arguments="${output.sideOne},${output.sideTwo},${output.sideTwo}" argumentSeparator=","></spring:message>
	</c:if>-->

	<c:if
		test="${!output.externalReinforcement && !output.internalReinforcement && output.internalBothSide && ((!output.jtrForSideOne && !output.jtrForSideTwo)||(output.jtrForSideOne && output.jtrForSideTwo))}">
		<spring:message code="message.internalReinforcementBothSide"
			arguments="${output.sideOne},${output.sideTwo}" argumentSeparator=","></spring:message>
	</c:if>
	<c:if
		test="${!output.externalReinforcement && !output.internalReinforcement && output.extAndInternalEachSide && ((!output.jtrForSideOne && !output.jtrForSideTwo)||(output.jtrForSideOne && output.jtrForSideTwo))}">
		<spring:message code="message.extAndInterReinBothSide"
			arguments="${output.sideOne},${output.sideTwo}" argumentSeparator=","></spring:message>
	</c:if>
	<c:if
		test="${!output.externalReinforcement && !output.internalReinforcement && output.intAndExtEachSide && ((!output.jtrForSideOne && !output.jtrForSideTwo)||(output.jtrForSideOne && output.jtrForSideTwo))}">
		<spring:message code="message.internalAndExtReinBothSide"
			arguments="${output.sideOne},${output.sideTwo}" argumentSeparator=","></spring:message>
	</c:if>
	 <c:if
		test="${output.intAndExternalOnBothSide && ((!output.jtrForSideOne && !output.jtrForSideTwo)||(output.jtrForSideOne && output.jtrForSideTwo))}">
		<spring:message code="message.intAndExternalOnBothSide"
			arguments="${output.sideOne},${output.sideTwo}" argumentSeparator=","></spring:message>
	</c:if>

	<c:if
		test="${output.intAndExtOnSideOne && ((!output.jtrForSideOne && !output.jtrForSideTwo)||(output.jtrForSideOne && output.jtrForSideTwo))}">
		<spring:message code="message.intAndExtOnSideOne"
			arguments="${output.sideOne}"></spring:message>
	</c:if>

	<c:if test="${output.extRenfSide == output.sideTwo && !isSet}">
		<c:if test="${output.externalReinforcement && ((!output.jtrForSideOne && !output.jtrForSideTwo)||(output.jtrForSideOne && output.jtrForSideTwo))}">
			<spring:message code="message.withExtRenfSide"
				arguments="${output.extRenfSide}"></spring:message>
		</c:if>
		<c:if test="${output.internalReinforcement && ((!output.jtrForSideOne && !output.jtrForSideTwo)||(output.jtrForSideOne && output.jtrForSideTwo))}">
			<spring:message code="message.internalReinforcement"
				arguments="${output.extRenfSide}"></spring:message>
		</c:if>
	</c:if>
	<c:set var="isSet" value="true"></c:set>
	<c:if test="${output.jtrForSideOne && output.jtrForSideTwo}">
	<spring:message code="message.withBothJTR"
	arguments="${output.sideOne},${output.sideTwo}" argumentSeparator=","></spring:message>
	</c:if>
<!-- 	<c:if test="${output.jtrForSideOne && !output.jtrForSideTwo}">
	<spring:message code="message.withSingleJTR"
	arguments="${output.sideOne}"></spring:message>
	</c:if>

	<c:if test="${!output.jtrForSideOne && output.jtrForSideTwo}">
	<spring:message code="message.withSingleJTR" arguments="${output.sideTwo}"></spring:message>
	</c:if>
 -->
<!-- Options when JTR on side one will implemented -->
	<c:if test="${output.extRenfSide == output.sideOne}">
		<c:if test="${output.externalReinforcement && output.jtrForSideOne && !output.jtrForSideTwo}">
	 		<spring:message code="message.withExtRenfSide"
				arguments="${output.extRenfSide}"></spring:message>
			<spring:message code="message.withJTR"></spring:message>
		</c:if>

		<c:if test="${output.internalReinforcement && output.jtrForSideOne && !output.jtrForSideTwo}">
			<spring:message code="message.internalReinforcement"
				arguments="${output.extRenfSide}"></spring:message>
			<spring:message code="message.withJTR"></spring:message>
		</c:if>
		<c:set var="isSet" value="true"></c:set>
	</c:if>
	<c:if
		test="${!output.externalReinforcement && !output.internalReinforcement && output.externalBothSide && output.jtrForSideOne && !output.jtrForSideTwo}">
		<spring:message code="message.withExtRenfBothSideWithJTRSideOne"
			arguments="${output.sideOne},${output.sideTwo}" argumentSeparator=","></spring:message>
	</c:if>

	<c:if
		test="${!output.externalReinforcement && !output.internalReinforcement && output.internalBothSide && output.jtrForSideOne && !output.jtrForSideTwo}">
		<spring:message code="message.internalReinforcementBothSideWithJTRSideOne"
			arguments="${output.sideOne},${output.sideTwo}" argumentSeparator=","></spring:message>
	</c:if>

	<c:if
		test="${!output.externalReinforcement && !output.internalReinforcement && output.extAndInternalEachSide && output.jtrForSideOne && !output.jtrForSideTwo}">
		<spring:message code="message.extAndInterReinBothSideWithJTRSideOne"
			arguments="${output.sideOne},${output.sideTwo}" argumentSeparator=","></spring:message>
	</c:if>
	<c:if
		test="${!output.externalReinforcement && !output.internalReinforcement && output.intAndExtEachSide && output.jtrForSideOne && !output.jtrForSideTwo}">
		<spring:message code="message.internalAndExtReinBothSideWithJTRSideOne"
			arguments="${output.sideOne},${output.sideTwo}" argumentSeparator=","></spring:message>
	</c:if>

	<c:if
		test="${output.intAndExternalOnBothSide && output.jtrForSideOne && !output.jtrForSideTwo}">
		<spring:message code="message.intAndExternalOnBothSideWithJTRSideOne"
			arguments="${output.sideOne},${output.sideTwo}" argumentSeparator=","></spring:message>
	</c:if>

	<c:if
		test="${output.intAndExtOnSideOne && output.jtrForSideOne && !output.jtrForSideTwo}">
		<spring:message code="message.intAndExtOnSideOne"
			arguments="${output.sideOne}"></spring:message>
		<spring:message code="message.withJTR"
			arguments=""></spring:message>
	</c:if>
	<c:if
		test="${output.intAndExtOnSideTwo && ((!output.jtrForSideOne && !output.jtrForSideTwo)||(output.jtrForSideOne && output.jtrForSideTwo))}">
		<spring:message code="message.intAndExtOnSideOne"
			arguments="${output.sideTwo}"></spring:message>
	</c:if>
	<c:if
		test="${output.intAndExtOnSideTwo && output.jtrForSideOne && !output.jtrForSideTwo}">
		<spring:message code="message.intAndExtOnSideOne"
			arguments="${output.sideTwo}"></spring:message>
		<spring:message code="message.withJTR"
			arguments=""></spring:message>
	</c:if>
	<c:if test="${output.extRenfSide == output.sideTwo && !isSet}">
		<c:if test="${output.externalReinforcement && output.jtrForSideOne && !output.jtrForSideTwo}">
	 		<spring:message code="message.withExtRenfSide"
				arguments="${output.extRenfSide}"></spring:message>
			<spring:message code="message.withJTR"></spring:message>
		</c:if>

		<c:if test="${output.internalReinforcement && output.jtrForSideOne && !output.jtrForSideTwo}">
			<spring:message code="message.internalReinforcement"
				arguments="${output.extRenfSide}"></spring:message>
			<spring:message code="message.withJTR"></spring:message>
		</c:if>
	</c:if>
	<c:set var="isSet" value="false"></c:set>
	<!-- Options when JTR on side two will implemented -->
	<c:if test="${output.extRenfSide == output.sideOne}">
		<c:if test="${output.externalReinforcement && !output.jtrForSideOne && output.jtrForSideTwo}">
			<spring:message code="message.withExtRenfSide"
				arguments="${output.extRenfSide}"></spring:message>
			<spring:message code="message.withSingleJTR"
				arguments="${output.sideTwo}"></spring:message>
		</c:if>

		<c:if test="${output.internalReinforcement && !output.jtrForSideOne && output.jtrForSideTwo}">
			<spring:message code="message.internalReinforcement"
				arguments="${output.extRenfSide}"></spring:message>
			<spring:message code="message.withSingleJTR"
				arguments="${output.sideTwo}"></spring:message>
		</c:if>
		<c:set var="isSet" value="true"></c:set>
	</c:if>

	<c:if
		test="${!output.externalReinforcement && !output.internalReinforcement && output.externalBothSide && !output.jtrForSideOne && output.jtrForSideTwo}">
		<spring:message code="message.withExtRenfBothSide"
			arguments="${output.sideOne},${output.sideTwo}" argumentSeparator=","></spring:message>
		<spring:message code="message.withJTR"></spring:message>
	</c:if>
	<c:if
		test="${!output.externalReinforcement && !output.internalReinforcement && output.internalBothSide && !output.jtrForSideOne && output.jtrForSideTwo}">
		<spring:message code="message.internalReinforcementBothSide"
			arguments="${output.sideOne},${output.sideTwo}" argumentSeparator=","></spring:message>
		<spring:message code="message.withJTR"></spring:message>
	</c:if>
	<c:if
		test="${!output.externalReinforcement && !output.internalReinforcement && output.extAndInternalEachSide && !output.jtrForSideOne && output.jtrForSideTwo}">
		<spring:message code="message.extAndInterReinBothSide"
			arguments="${output.sideOne},${output.sideTwo}" argumentSeparator=","></spring:message>
		<spring:message code="message.withJTR"></spring:message>
	</c:if>
	<c:if
		test="${!output.externalReinforcement && !output.internalReinforcement && output.intAndExtEachSide && !output.jtrForSideOne && output.jtrForSideTwo}">
		<spring:message code="message.internalAndExtReinBothSide"
			arguments="${output.sideOne},${output.sideTwo}" argumentSeparator=","></spring:message>
		<spring:message code="message.withJTR"></spring:message>
	</c:if>
	<c:if
		test="${output.intAndExternalOnBothSide && !output.jtrForSideOne && output.jtrForSideTwo}">
		<spring:message code="message.intAndExternalOnBothSide"
			arguments="${output.sideOne},${output.sideTwo}" argumentSeparator=","></spring:message>
		<spring:message code="message.withJTR"></spring:message>
	</c:if>
	<c:if
		test="${output.intAndExtOnSideOne && !output.jtrForSideOne && output.jtrForSideTwo}">
		<spring:message code="message.intAndExtOnSideOne"
			arguments="${output.sideOne}"></spring:message>
		<spring:message code="message.withSingleJTR"
			arguments="${output.sideTwo}"></spring:message>
	</c:if>
	<c:if
		test="${output.intAndExtOnSideTwo && !output.jtrForSideOne && output.jtrForSideTwo}">
		<spring:message code="message.intAndExtOnSideOne"
			arguments="${output.sideTwo}"></spring:message>
		<spring:message code="message.withSingleJTR"
			arguments="${output.sideTwo}"></spring:message>
	</c:if>
	<c:if test="${output.extRenfSide == output.sideTwo && !isSet}">
		<c:if test="${output.externalReinforcement && !output.jtrForSideOne && output.jtrForSideTwo}">
			<spring:message code="message.withExtRenfSide"
				arguments="${output.extRenfSide}"></spring:message>
			<spring:message code="message.withSingleJTR"
				arguments="${output.sideTwo}"></spring:message>
		</c:if>

		<c:if test="${output.internalReinforcement && !output.jtrForSideOne && output.jtrForSideTwo}">
			<spring:message code="message.internalReinforcement"
				arguments="${output.extRenfSide}"></spring:message>
			<spring:message code="message.withSingleJTR"
				arguments="${output.sideTwo}"></spring:message>
		</c:if>
	</c:if>
	<c:set var="isSet" value="false"></c:set>

	</p>
</c:forEach> <!-- Button Start --> <div class="result-btn"> <form:form
	commandName="contact" action="editIndex">
	<input type="hidden" value="editUserInputs" name="editDetails" />
	<span><input type="button" title="Edit" value="Edit" class="btn"
		onclick="form.submit();" /></span>
</form:form> <form:form action="mailPopUp">
	<input type="hidden" id="hiddenOption" name="hiddenOpt" />
	<span> <input type="button" title="Generate Report &amp; Email"
		value="Generate Report &amp; Email" class="btn"
		onclick="openMailPopUp(this.form);" /></span>
</form:form> 
<form:form action="generatePdf">
	<span><input type="button" title="Generate Report &amp; View"
		value="Generate Report &amp; View" class="btn"
		onclick="generateAndViewPdf(this.form);" /></span>
</form:form>
<form:form action="showMoreOptions" commandName="inputscreen">
	<span><input type="button" title="Show More Options"
		value="Show More Options" class="btn"
		onclick="showMoreOptions(this.form);" /></span>
</form:form>

 </div> <!-- Button End --> </div> </div> <!-- Content Body End --> <script
	type="text/javascript">
	var radioButtons = document.getElementsByName("opt");
	radioButtons[0].checked = true;
	</script>
	<div id="footer">
		<div class="footer-container">
			<a href="http://www.smacna.org/bookstore/" class="bookstore" ><spring:message code="button.bookstore"></spring:message></a>
	    </div>
	</div>
	</body> </html>
