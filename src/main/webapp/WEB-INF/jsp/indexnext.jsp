<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="title.smacna"></spring:message></title>
<link href="css/style.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="css/dd.css" />
<meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;">
<link href="css/android.css" rel="stylesheet" type="text/css"  media="only screen and (min-width: 0px) and (max-width: 240px)"/>
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(
hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
<meta name="apple-mobile-web-app-capable" content="yes" />
<!--[SELECT JS]-->
<link rel="stylesheet" href="css/sparkbox-select.css">
<script src="js/jquery.min.js"></script>
<script src="js/jquery.sparkbox-select.js"></script>
<script src="js/script.js"></script>

<script type="text/javascript">

var contextPath = "<c:out value="${pageContext.request.contextPath}" />";
var initialTC2Select = null;
var numericExpression = /^[0-9]+$/;
var onPageLoadChanged = true;

function onChangePc() {
	 var  pressureClass = document.getElementById("pressureClass");
    var initialPcValue=""+0;
    if(initialPcValue!=0)
        {
    	
    if(initialPcValue==pressureClass.selectedIndex && onPageLoadChanged)
        {

        }
    else
        {
   	 onPageLoadChanged = false;
   	 postAjaxRequest();
        }
        }
    else
        {
   	 postAjaxRequest();
        }

}
function postAjaxRequest()
{
	var shouldPostAjaxRequest = true;
	var  pressureClass = document.getElementById("pressureClass");
	 if(pressureClass.selectedIndex==0)
		 {
		 shouldPostAjaxRequest = false;
		 }
    var pcValue=pressureClass.options[pressureClass.selectedIndex].text;
    if(pcValue == "1/2"){
   	 pcValue = 0.5;
        }
    var width = document.getElementById("width").value;
    if(width<0 || width>120 ||(width !="Width" && !width.match(numericExpression)))
    {
    	var widthError = document.getElementById("widthErrors");
    	widthError.style.display="block";
    	if(document.getElementById("width.errors"))
        	{
    		document.getElementById("width.errors").style.display="none";
        	}
    	shouldPostAjaxRequest =  false;
    }
    else
        {
    	document.getElementById("widthErrors").style.display="none";
    	if(document.getElementById("width.errors"))
    	{
		document.getElementById("width.errors").style.display="none";
    	}
        }
    if(width == "Width")
    {
   	 width = 0;
    }

    var height = document.getElementById("height").value;
    if(height<0 || height>120 ||(height != "Height" && !height.match(numericExpression)))
    {
    	var heightError = document.getElementById("heightErrors");
    	heightError.style.display="block";
    	if(document.getElementById("height.errors"))
    	{
		document.getElementById("height.errors").style.display="none";
    	}
    	shouldPostAjaxRequest =  false;
    }
    else
        {
        document.getElementById("heightErrors").style.display="none";
        if(document.getElementById("height.errors"))
    	{
    	document.getElementById("height.errors").style.display="none";
    	}
        }

    if(height == "Height")
    {
   	 height = 0;
    }
if(shouldPostAjaxRequest)
	{
	
	try{
	$.ajax({
	    type: "POST",
	    url: contextPath + "/onChangePc.html",
	    data: "pcClass=" + pcValue + "&width=" + width + "&height="+ height,
	     success: function(response){
	      // we have the response
		//alert("inResponse");
	      if(response.status == "SUCCESS"){
	       document.getElementById("transConnS1").className += "";
	   	  initialTC2Select=response.result;
		  document.getElementById("transConnS1").options.length = 0;
		  document.getElementById("transConnS2").options.length = 0;
		  var tcSelect = document.getElementById("transConnS1");
		  var tcSelect2 = document.getElementById("transConnS2");
		  tcSelect.add(new Option("Select TC for Larger Side", "0"), 0);
		  tcSelect2.add(new Option("Select TC for Smaller Side", "0"), 0);
		  for(i=0 ; i<response.result.length ; i++)
		  {
		  tcSelect.options[i+1]=new Option(response.result[i].tc, response.result[i].id);
		  }
		  for(i=0 ; i<response.result.length ; i++)
		  {
		  tcSelect2.options[i+1]=new Option(response.result[i].tc, response.result[i].id);
		  }
		  }
	    },
	    error: function(e){
			//alert('Error');
	    }
	  });
	}
	catch(error)
	{ }

	}
}



function onChangeTc1()
{
	var  pressureClass = document.getElementById("pressureClass");
	var pcValue = pressureClass.options[pressureClass.selectedIndex].value;
	var tc1Select = document.getElementById("transConnS1");
	var tc1Value = tc1Select.options[tc1Select.selectedIndex].value;
  	if(pcValue!=0 && tc1Value!=0)
  	{
	$.ajax({
	    type: "POST",
	    url: contextPath + "/onChangeTc1.html",
	    data: "pc=" + pcValue + "&traConS1=" + tc1Value,
	     success: function(response){
	      // we have the response
		//alert("inResponse");
	      if(response.status == "SUCCESS"){
		  document.getElementById("transConnS2").options.length = 0;
		  var tcSelect2 = document.getElementById("transConnS2");
		  tcSelect2.add(new Option("Select TC for Smaller Side", "0"), 0);
		  for(i=0 ; i<response.result.length ; i++)
		  {
		  tcSelect2.options[i+1]=new Option(response.result[i].tc, response.result[i].id);
		  }
		  }
	    },
	    error: function(e){

	    }
	  });
  	}
}
</script>

<!--[END SELECT JS]-->
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
    <!--Form Start-->
	<p><font color="red">${inputScreen.designAvailable}</font></p>
	<h2><spring:message code="label.pressureClass"></spring:message></h2>
    <form:form method="post" action="submit" commandName="inputScreen">
    <form:errors path="pressureClass" cssClass="error" />
      <form:select path="pressureClass" cssClass="sparkbox-custom" onchange="onChangePc();">
		<form:option value="0" label="Select"></form:option>
		<form:options items="${staPcList}" itemValue="id" itemLabel="pc"/>
		</form:select>
      <p> <span class="left-txt"><spring:message code="label.posOrNeg"></spring:message></span> <span class="right-txt">
        <form:radiobutton path="posOrNeg" value="true"/>
        Pos
        <form:radiobutton path="posOrNeg" value="false"/>
        Neg </span> </p>
      <div class="clr"></div>
      <h2><spring:message code="label.ductDimensions"></spring:message></h2>
      <div id="widthErrors" class="error"> <spring:message code="message.widthError"></spring:message> </div>
      <form:errors path="width" cssClass="error" />
      <form:input path="width" name="" onfocus="if (this.value=='Width'){this.value=''};" onblur="if (this.value==''){this.value='Width'};" onchange="postAjaxRequest();" />
      <div id="heightErrors" class="error"> <spring:message code="message.heightError"></spring:message></div>
      <form:errors path="height" cssClass="error" />
      <form:input path="height" name="" onfocus="if (this.value=='Height'){this.value=''};" onblur="if (this.value==''){this.value='Height'};onChangePc()" onchange="postAjaxRequest();"/>
      <h2><spring:message code="label.jointSpacing"></spring:message></h2>
      <div>
      	<form:errors path="jointSpacing" cssClass="error" />
      	<div class="select selectMedium">
        <form:select path="jointSpacing">
        <form:option value="0" label="Select"></form:option>
        <form:option value="4" label="4"></form:option>
        <form:option value="5" label="5"></form:option>
        <form:option value="6" label="6"></form:option>
        </form:select>
        </div>
      </div>

      <h2><spring:message code="label.transverseConnection"></spring:message></h2>
      <div>
      	<form:errors path="transConnS1" cssClass="error" />
      	<div class="select selectMedium">
        <c:choose>
      	<c:when test="${!empty edit}">
      	<form:select path="transConnS1" onchange="onChangeTc1()">
		<form:option value="0" label="Select TC for Larger Side"></form:option>
		<form:options items="${traConnList}" itemValue="id" itemLabel="tc"/>
		</form:select>
		</c:when>
		<c:otherwise>
		<form:select path="transConnS1" onchange="onChangeTc1()">
		<form:option value="0" label="Select TC for Larger Side"></form:option>
		<form:options items="${traConnList_edit}" itemValue="id" itemLabel="tc"/>
		</form:select>
		</c:otherwise>
		</c:choose>
		</div>
      </div>
      <br/>
      <div>
      	<form:errors path="transConnS2" cssClass="error" />
      	<div class="select selectMedium">
        <c:choose>
      	<c:when test="${!empty edit}">
        <form:select path="transConnS2" >
		<form:option value="0" label="Select TC for Smaller Side"></form:option>
		<form:options items="${traConnList2}" itemValue="id" itemLabel="tc"/>
		</form:select>
		</c:when>
		<c:otherwise>
		<form:select path="transConnS2" >
		<form:option value="0" label="Select TC for Smaller Side"></form:option>
		<form:options items="${traConnList2_edit}" itemValue="id" itemLabel="tc"/>
		</form:select>
		</c:otherwise>
		</c:choose>
		</div>
      </div>
      <div class="submit">
        <input type="submit" title="Submit" value="Submit" class="btn"/>
      </div>

</form:form>
    <!--Form End-->
  </div>
</div>
<!-- Content Body End -->
<div id="footer">
		<div class="footer-container">
			<a href="http://www.smacna.org/bookstore/" class="bookstore" ><spring:message code="button.bookstore"></spring:message></a>
	    </div>
	</div>
<script type="text/javascript">
var width = document.getElementById("width");
var height = document.getElementById("height");
if(width.value==null||width.value==""){
	width.value="Width";
}
if(height.value==null||height.value==""){
	height.value="Height";
}
document.getElementById("widthErrors").style.display="none";
document.getElementById("heightErrors").style.display="none";
</script>

</body>
</html>
