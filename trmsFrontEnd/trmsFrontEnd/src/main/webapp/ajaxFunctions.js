function loadIncompleteForms() 
{
	var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() 
		{
			if (this.readyState == 4 && this.status == 200) 
			{
				document.getElementById("incompleteFormsDiv").innerHTML = this.responseText;
			}
		};
	xhttp.open("POST", "http://localhost:8080/trmsFrontEnd/getIncompleteForms", true);
	xhttp.send();
}