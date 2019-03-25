var CONNECT_MONGODB_AND_CASE_URL = './connectToMongoServerAndGetCases';
var EXTRACTIONS_URL =      './extractions';

// Load the Visualization API and the corechart package.
google.charts.load('current', {'packages':['corechart', 'table']});

// Set a callback to run when the Google Visualization API is loaded.
//google.charts.setOnLoadCallback(drawChart);

var mongoClusterData;
var mongoClusterTable;

function populateServerData( mongoServer )
{
	$("#server_name").text( mongoServer );
}

function initCasesTable() 
{
    mongoClusterTable = new google.visualization.Table(document.getElementById('cases_table_div'));
}

      
$(document).ready(function() {
	$("#submit").click(function(){
		
		callBackendConnectAndGetCases();
	});
});


function callBackendConnectAndGetCases()
{
	var serverAddress =	$("#server_name_input").val();

	var requestData = {
			serverAddress: serverAddress
		};

	$.ajax({
		url: CONNECT_MONGODB_AND_CASE_URL,
		type: 'POST',
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


/*
 * the json is in this format:
 * "[{"_id":"6234854c-b7c0-470f-a3c3-131aaaa947b8","name":"New case01","status":"Active"},{"_id":"ca5188b0-0fdd-4966-949f-59f9d39c08b5","name":"New case02","status":"Active"}]"
 */
function drawCasesOnGrid(cases) {

	//first of all, clear the table from previous cases:
    mongoClusterData = new google.visualization.DataTable();
    mongoClusterData.addColumn('string', 'Name');
    mongoClusterData.addColumn('string', 'CaseId');
//    mongoClusterData.addColumn('boolean', 'Primary');
    mongoClusterData.addColumn('string', 'Status');


	for (var i in cases)
	{
		var node = cases[i];
		//find the node in the table:
		var rows = mongoClusterData.getFilteredRows([{column: 0, value: node.name}]);
		if(rows.length !== 0) //if we did not find by filter, create a new row
		{
			var rowIndex = rows[0];	//expect a single row with this server name;
			mongoClusterData.setCell(rowIndex, 1, node._id);
			mongoClusterData.setCell(rowIndex, 2, node.status);
		}
		else
		{
		    mongoClusterData.addRow(
		        [node.name, node._id, node.status]  );
		}

		mongoClusterTable.draw(mongoClusterData, {showRowNumber: true});

	}
}

function exportExtractionsMetricsForSelectedCase()
{
	var selections = mongoClusterTable.getSelection();
	if(selections.length === 0)
		return;
	var caseId = mongoClusterData.getFormattedValue(selections[0].row, 1);
	var requestData = {
			caseId: caseId
		};

	$.ajax({
		url: EXTRACTIONS_URL,
        type: 'POST',
		data: requestData,
//        dataType: 'text',    //The type of data that you're expecting back from the server
        contentType: 'application/x-www-form-urlencoded',    //When sending data to the server, use this content type
		success: function(response, textStatus, jqXHR){
			var cases = response;
			window.open('./outputs/' + caseId + '.csv');
		},
		error: function(jqXHR, textStatus, errorThrown){
			//do something?
		}
	});
	
}