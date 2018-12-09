function requestServer(callback)
{
	$.ajax({
			url: "http://localhost/backend/json.php",
			method: "get",
			success: function (result){
				console.log(result);
				callback(result);
			} 
	}
	)
}
var dps = []; // dataPoints

var chart; 

function getGatewaySelect ()
{
	/* Select gateway field html*/
	let gateway = $("#GatewayID");

	/* Get current selected gateway*/
	var selectedGateway;

	/* Gateway list already set */
	if ($.trim(gateway.html()) != '' )
	{
		selectedGateway = gateway.children("option:selected").val();
		
	}
	/* return selected Gateway for chart render*/
	return selectedGateway;
}

function updateData (Gatewaylist)
{
	/* Select gateway field html*/
	let gateway = $("#GatewayID");

	let selectedGateway = getGatewaySelect(); 
	
	
	var selText= '';

	/* Set new list of Gateway in database */
	for (var i = 0; i < Gatewaylist.Gateways.length; i++)
	{
		/* Each gateway is a option in select element */
		let GatewayID = Gatewaylist.Gateways[i].GatewayID;
		selText += "<option value='" + GatewayID + "'> Gateway " + GatewayID +"</option>"; 

		if ( typeof selectedGateway !== 'undefined' && selectedGateway == GatewayID )
		{
			/* Enable which sensor have data in response JSON */
			let SensorList = Gatewaylist.Gateways[i].GatewayData;

			for (var j = 0; j < SensorList.length; j++)
			{
				$(".sensor:eq(" + SensorList[j].NodeID +")").prop('disabled', false);
				$(".checkbox > label:eq(" + SensorList[j].NodeID +")").css('color', 'Black');

			}
		}
	}

	/* Set new list of Gateway */
	gateway.html(selText);

	/* Reselect selected gateway */
	gateway.val(selectedGateway);


}

function clearOldData() 
{
	/* Disable all sensor */
	$(".sensor").prop('disabled', true);
	$(".checkbox > label").css('color', 'DarkGray');
	$(".sensor").prop('checked', false);
}

$("#GatewayID").change(function(){clearOldData(); requestServer(updateData)});

var updateChart = function (Gatewaylist) {
	
	/* Data lenght of data */
	var chartData = chart.data;
	if (chartData != null )
	{
		for (var i = 0; i < chartData.length ; i++)
		{
			chart.options.data.pop();
		}
	}

	/* Update data for left menu */
	let selectedGateway = getGatewaySelect();

	/* Sensor data */
	var GatewayData; 
	for (var i = 0 ; i < Gatewaylist.Gateways.length; i++)
	{
		if (Gatewaylist.Gateways[i].GatewayID == selectedGateway)
		{
			GatewayData = Gatewaylist.Gateways[i].GatewayData;
		}		
	}

	for (var i = 0; i < 10; i++) 
	{
		if ($(".sensor:eq(" + i + ")").is(':checked'))
		{
			let SensorData;

			for (var j = 0; j < GatewayData.length; j++)
			{
				if (GatewayData[j].NodeID == i)
				{
					SensorData = GatewayData[j].NodeData;
				}
			}
			
			var sensorDPS = [];

			for (var k = 0; k < SensorData.length; k++)
			{
				sensorDPS.push(
						{	
							x: new Date(SensorData[k].time * 1000),
							y: SensorData[k].humidnity
						}
				)
			}
			
			let sensor = i + 1;
			var newSeries = 
				{
					type: "line",
					showInLegend: true,
					xValueType: "dateTime",
					legendText: "Sensor " + sensor,
					dataPoints: sensorDPS	
				};
			chart.options.data.push(newSeries);

		}
	}
	chart.render();
};

window.onload = function () {
	chart = new CanvasJS.Chart("chartContainer", {
	title :{
				text: "Gateway Data"
	},
	axisY: {
				includeZero: false,
				title: "Humidinity",
				suffix: "%"
	},      
	data: dps 
});
	requestServer(updateData);
   	requestServer(updateChart);
	setInterval(function(){ requestServer(updateData); requestServer(updateChart); }, 3000);
}



