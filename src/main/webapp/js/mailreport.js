
	function mailFabricationReport(form)
	{
	var selectedOption = 1;
	var radioButtons = document.getElementsByName("opt");
	for ( var x = 0; x < radioButtons.length; x++) {
		if (radioButtons[x].checked) {
			selectedOption = radioButtons[x].value;
		}
	}
	var inputElement = document.createElement("input");
	inputElement.type = "hidden";
	inputElement.name = "opt";
	inputElement.value = selectedOption;
	form.appendChild(inputElement);
	form.submit();
	}

	function openMailPopUp(form)
	{
		var selectedOption = 1;
		var radioButtons = document.getElementsByName("opt");
		for ( var x = 0; x < radioButtons.length; x++) {
			if (radioButtons[x].checked) {
				selectedOption = radioButtons[x].value;
			}
		}
		var hiddenElement = document.getElementById("hiddenOption");

		hiddenElement.value = selectedOption;
		form.submit();

	}
	
	function showMoreOptions(form){
		form.submit();
	}
