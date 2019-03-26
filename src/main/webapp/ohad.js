var PROPERTIES_URL =      './properties';

var mongoClusterData;

function populateServerData( mongoServer )
{
	$("#server_name").text( mongoServer );
}

$(document).ready(function() {
	$("#submit").click(function(){
		
		callBackendGetProperties();
	});
});


function callBackendGetProperties()
{
	var serverAddress =	$("#server_name_input").val();

	var requestData = {
			serverAddress: serverAddress
		};

	$.ajax({
		url: PROPERTIES_URL,
		type: 'GET',
		data: requestData,
        dataType: 'text',    //The type of data that you're expecting back from the server
        contentType: 'application/x-www-form-urlencoded',    //When sending data to the server, use this content type
		success: function(response, textStatus, jqXHR){
			populateServerData( $("#server_name_input").val() );
			var cases = JSON.parse(response);
			drawCasesOnGrid( cases );
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert('could not connect to mongo ' + serverAddress)
		}
	});
}