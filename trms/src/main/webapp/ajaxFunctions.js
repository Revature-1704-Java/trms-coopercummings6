function loadIncompleteForms() 
{
	var xhttp = new XMLHttpRequest();
	xhttp.open("POST", "http://localhost:8080/trms/getIncompleteForms", true);
	xhttp.setRequestHeader('Content-Type', 'application/json');
	xhttp.onload = function() 
	{
		if (this.status === 200) 
		{
			let jsonFormArray = JSON.parse(xhttp.responseText);
			let table = document.getElementById("incompleteFormsTable");
			console.log(jsonFormArray);
			for(let i = 0; i < jsonFormArray.length; i++) 
			{
				let tr = document.createElement('TR');
				
				let td = document.createElement('TD');
				td.appendChild(document.createTextNode(jsonFormArray[i]["requestID"]));
				tr.appendChild(td);
				td = document.createElement('TD');
				td.appendChild(document.createTextNode(jsonFormArray[i]["dateSubmitted"]));
				tr.appendChild(td);
				td = document.createElement('TD');
				td.appendChild(document.createTextNode(jsonFormArray[i]["location"]));
				tr.appendChild(td);
				td = document.createElement('TD');
				td.appendChild(document.createTextNode(jsonFormArray[i]["gradingFormat"]));
				tr.appendChild(td);
				td = document.createElement('TD');
				td.appendChild(document.createTextNode(jsonFormArray[i]["eventType"]));
				tr.appendChild(td);
				td = document.createElement('TD');
				td.appendChild(document.createTextNode(jsonFormArray[i]["description"]));
				tr.appendChild(td);
				td = document.createElement('TD');
				td.appendChild(document.createTextNode(jsonFormArray[i]["cost"]));
				tr.appendChild(td);
				td = document.createElement('TD');
				td.appendChild(document.createTextNode(jsonFormArray[i]["workTimeMissed"]));
				tr.appendChild(td);
				td = document.createElement('TD');
				let input = document.createElement('INPUT');
				input.setAttribute("type", "text");
				input.setAttribute("label", `form${jsonFormArray[i]["requestID"]}grade`)
				td.appendChild(input);
				tr.appendChild(td);
				
				table.appendChild(tr);
			}
		}
	};
	xhttp.send();
}