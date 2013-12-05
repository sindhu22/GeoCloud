<html>
<head>
	<script type="text/JavaScript" src="jsDraw2D.js"></script>
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
	<style type="text/css">
		div
		{
			background-color:#E0E5EB;
		}
	</style>
</head>
<body bgcolor="#336699">
	<div id="debug" ></div> 
	<form id="form_751134" class="appnitro"  method="post" action="index.html">
		<input id="goBack" class="button_text" type="submit" name="submit" value="Go Back" />
	</form>
	<div background-color="#E0E5EB" id="canvas" style="overflow:hidden;position:relative;width:100%;height:100%;"></div> 

	<script type="text/JavaScript">
		//Global graphics variable to draw lines and points
		var gr;

		//Variable to store the result of mapreduce output
		var output;

		//variable to store the input points
		var input;

		//Variable to hold the input point print flag
		var plotInputFlag="";

		//Extract the scale and draw them on canvas
		var scale = "<?php echo $_POST["element_4"] ?>";

		//Utitlity function to plot points
		function plotPoints(points , size, color){
			for(i in points){
				gr.fillRectangle(new jsColor(color),new jsPoint(points[i].x-size,points[i].y+size),size+2,size+2);
			}
		}

		function plotGraph(center, points){
			//Create jsColor object
			var col = new jsColor("red");

    		//Create jsPen object
    		var pen = new jsPen(col,2);

    		for(i in points){
    			gr.drawLine(pen, center, points[i] );
    		}
    	}

    	function truncate(_value)
    	{
    		if (_value<0) return Math.ceil(_value);
    		else return Math.floor(_value);
    	}

	    //Function to read the data from the remote server
	    function loadXMLDoc() {

	    	if (window.XMLHttpRequest)
	      	{// code for IE7+, Firefox, Chrome, Opera, Safari
	      		xmlhttp=new XMLHttpRequest();
	      	}
	      	else
	      	{// code for IE6, IE5
	      		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	      	}

	      	var outputFileName = "<?php echo $_POST["element_2"] ?>";

	      	xmlhttp.open("POST",outputFileName,false);

	      	//check if the file is present or not
	      	xmlhttp.onreadystatechange = function() {
	      		if (self.xmlhttp.readyState == 4) {
	      			if (self.xmlhttp.status == 404) {
	      				alert('File: ' + outputFileName + ' does not exist. Exiting...');
	      				return;
	      			}
	      		}
	      	}

	      	xmlhttp.send();

		    //Data received from the server
		    output= xmlhttp.responseText;

		    plotInputFlag = "<?php if (isset($_POST["element_5_1"])) { echo $_POST["element_5_1"]; } ?>";

		    if ( plotInputFlag == "yes"){
		    	var inputFilename = "<?php echo $_POST["element_1"] ?>";

			  //Open connecton to read the new file
			  xmlhttp.open("POST", inputFilename , false);


				 //check the existance of the file on server
				 xmlhttp.onreadystatechange = function() {
				 	if (self.xmlhttp.readyState == 4) {
				 		if (self.xmlhttp.status == 404) {
				 			alert('File: ' + inputFilename + ' does not exist. Exiting...');
				 			return;
				 		}
				 	}
				 }	

				 xmlhttp.send();

			  //Receive the data from server
			  input = xmlhttp.responseText;

			}
		}

	</script>
</body>
</html>
