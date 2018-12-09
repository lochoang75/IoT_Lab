function httpGetAsync(url, callback) 
{
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		 // check status of request
		 if (this.readyState == 4 && this.status == 200) {
				 // Parse json to object
		   		 var myObj = JSON.parse(this.responseText);
				 callback(myObj);
		 }

	} 
	xmlhttp.open("GET", url, true);
	xmlhttp.send();

};

function forecast(myObj) {
		 // Print json data
		 console.log(myObj);

		 // Create list of forecast
		 var forecast = [];

		 // j for loop to end of list and i to count 10 unit to add to forecast
		 var i = 0;
		 var j = 0;

		 // Get current timestamp , div 1000 to get in seccond
		 var current_time = Date.now()/1000;

		 while (i < 4 && j < 40){
				 if (current_time < myObj.list[j].dt) {
						 var temp = parseInt(myObj.list[j].main.temp) - 273;
						 var humid = parseInt(myObj.list[j].main.humidity);
						 // create object and add data 
						  var data = {time: myObj.list[j].dt, temp: temp, humidity: humid, weather: myObj.list[j].weather[0].main};
						 // append data to list
						 forecast[i] = data;
						 i++;
				 }
				 j++;
		 }	
		 // Check forecast list
		 console.log(forecast);

		 for (var i = 0; i < 4; i++) 
		 {
				 document.getElementsByClassName("Temp")[i].innerHTML = forecast[i].temp + "&deg;C";
				 document.getElementsByClassName("Humid")[i].innerHTML = forecast[i].humidity + "%";
				 document.getElementsByClassName("Weather")[i].innerHTML = forecast[i].weather;
				 var timestamp = parseInt(forecast[i].time);
		 		 var date = new Date(timestamp*1000);
		 		 var hours = date.getHours();
		 		 var minutes = date.getMinutes();
		 		 // Padding zero
		 		 if ((minutes / 10) < 1)
		 		 {
						 minutes = "0" + minutes;
		 		 }

		 		 if ((hours / 10) < 1)
		 		 {
						 hours = "0" + hours;
		 		 }

				 document.getElementsByClassName("Time")[i].innerHTML = hours + ":" + minutes;
		 }

}

function Information(Obj) {
		 // print json data
		 console.log(Obj);

		 // replace data in html
		 // get latest update
		 var timestamp = parseInt(Obj.dt);
		 var date = new Date(timestamp*1000);
		 var hours = date.getHours();
		 var minutes = date.getMinutes();
		 
		 // get temp, humidity, weather
		 var temp = parseInt(Obj.main.temp) - 273;
		 var humid = Obj.main.humidity;
		 var weather = Obj.weather[0].main;
		 
		 // Padding zero
		 if ((minutes / 10) < 1)
		 {
				 minutes = "0" + minutes;
		 }

		 if ((hours / 10) < 1)
		 {
				 hours = "0" + hours;
		 }

		 // Replace in html
		 document.getElementById("lasttime").innerHTML = hours +" : "+ minutes; 
		 document.getElementById("curTemp").innerHTML = temp + "&deg;C";

		 document.getElementById("curHumid").innerHTML = humid + "%";

		 document.getElementById("weather").innerHTML = weather;
		 		 
}
httpGetAsync("http://api.openweathermap.org/data/2.5/forecast?q=Thanh%20pho%20Ho%20Chi%20Minh&APPID=677bdf261808ee261bd7c63b67528cc4",forecast);
httpGetAsync("http://api.openweathermap.org/data/2.5/weather?q=Thanh%20pho%20Ho%20Chi%20Minh&APPID=677bdf261808ee261bd7c63b67528cc4", Information);

setInterval(function(){
	httpGetAsync("http://api.openweathermap.org/data/2.5/forecast?q=Thanh%20pho%20Ho%20Chi%20Minh&APPID=677bdf261808ee261bd7c63b67528cc4",forecast);
	httpGetAsync("http://api.openweathermap.org/data/2.5/weather?q=Thanh%20pho%20Ho%20Chi%20Minh&APPID=677bdf261808ee261bd7c63b67528cc4", Information);
}, 3600000
)


