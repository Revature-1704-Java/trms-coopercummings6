function loadIncompleteForms() 
{
	var xhttp = new XMLHttpRequest();
	xhttp.open("POST", "http://localhost:8080/trmsFrontEnd/getIncompleteForms", true);
	xhttp.setRequestHeader('Content-Type', 'application/json');
	xhttp.onload = function() {
		if (this.status === 200) {
			let jsonFormArray = JSON.parse(xhttp.responseText);
			let oldTable = document.getElementById("incompleteFormsTable");
			let newTable = oldTable.cloneNode();
			
			for(var form in jsonFormArray) {
				let keyNames = Object.keys(form);
				let tr = document.createElement('TR');
				for (var key in keyNames) {
					let td = document.createElement('TD');
					td.appendChild(document.createTextNode(form.key));
					tr.appendChild(td);
				}
				newTable.appendChild(tr);
			}
			oldTable.parentNode.replaceChild(newTable, oldTable);
		}
		else {
			console.log("browser is broken")
		}
	};
	xhttp.send();
}