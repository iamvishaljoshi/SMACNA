	function generateAndViewPdf(form) {
		var selectedOption=null;
		var radioButtons = document.getElementsByName("opt");
		for ( var x = 0; x < radioButtons.length; x++) {
			if (radioButtons[x].checked) {
				selectedOption = radioButtons[x].value;
			}
		}
		if(selectedOption == null)
			{alert("Please select the option");}
		else
			{
		var inputElement = document.createElement("input");
		inputElement.type = "hidden";
		inputElement.name = "opt";
		inputElement.value = selectedOption;
		form.appendChild(inputElement);

		postAjaxRequest(selectedOption);
	}
	}

	function postAjaxRequest(selectedOption)
	{
		try{
		$.ajax({
		    type: "POST",
		    url: contextPath + "/generatePdf.html",
		    data: "opt=" + selectedOption,
		     success: function(response){
		      // we have the response
			alert("inResponse");
		      if(response.status == "SUCCESS"){
				window.location.href = response.result;
			  }
		    },
		    error: function(e){

		    }
		  });
		}
		catch(error)
		{}

		}