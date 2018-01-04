function loadIncompleteForms() 
{
	var xhttp = new XMLHttpRequest();
	xhttp.open("POST", "http://localhost:8080/trms/getIncompleteForms", true);
	xhttp.setRequestHeader('Content-Type', 'application/json');
	xhttp.onload = function() 
	{
		if (this.status === 200) 
		{
			let jsonFormArray = JSON.parse(xhttp.responseText);//get json from servlet
			let oldTable = document.getElementById("incompleteFormsTable");//get the old table
			let newTable = oldTable.cloneNode();//clone it
			//console.log(jsonFormArray);
			
			//manually insert table headers
			let tr = document.createElement('TR');
			let th = document.createElement('TH');
			th.appendChild(document.createTextNode("Request ID"));
			tr.appendChild(th);
			th = document.createElement('TH');
			th.appendChild(document.createTextNode("Date Submitted"));
			tr.appendChild(th);
			th = document.createElement('TH');
			th.appendChild(document.createTextNode("Location"));
			tr.appendChild(th);
			th = document.createElement('TH');
			th.appendChild(document.createTextNode("Grading Format"));
			tr.appendChild(th);
			th = document.createElement('TH');
			th.appendChild(document.createTextNode("Event Type"));
			tr.appendChild(th);
			th = document.createElement('TH');
			th.appendChild(document.createTextNode("Description"));
			tr.appendChild(th);
			th = document.createElement('TH');
			th.appendChild(document.createTextNode("Cost"));
			tr.appendChild(th);
			th = document.createElement('TH');
			th.appendChild(document.createTextNode("Work Time Missed"));
			tr.appendChild(th);
			th = document.createElement('TH');
			th.appendChild(document.createTextNode("Final Grade"));
			tr.appendChild(th);
			newTable.appendChild(tr);
			
			for(let i = 0; i < jsonFormArray.length; i++) //iterate through json array and output all of the needed fields
			{
				tr = document.createElement('TR');
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
				input.setAttribute("name", `form${jsonFormArray[i]["requestID"]}grade`)
				td.appendChild(input);
				tr.appendChild(td);
				
				newTable.appendChild(tr);
			}
			let oldParent = oldTable.parentNode;//replace old table with new
			oldParent.replaceChild(newTable, oldTable);
		}
	};
	xhttp.send();
}