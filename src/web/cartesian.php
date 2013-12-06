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
		// Global graphics variable to draw lines and points
		var gr;

		// Variable to store the result of mapreduce output
		var output;

		// variable to store the input points
		var input;

		// Variable to hold the input point print flag
		var plotInputFlag="";

		// Extract the scale and draw them on canvas
		var scale = "<?php echo $_POST["element_4"] ?>";

		// Center of the solutions
		var center;

		/*
		* Utitlity function to plot points
		*/
		function plotPoints(points , size, color)
		{
			// Plot each point sequencially one by one
			for(i in points)
			{
				gr.fillRectangle(new jsColor(color),new jsPoint(points[i].x-size,points[i].y+size),size+2,size+2);
			}
		}

		/*
		* Function to plot the graph from center point to the results
		*/
		function plotGraph(center, points)
		{
			// Create jsColor object
			var col = new jsColor("red");

    		// Create jsPen object
    		var pen = new jsPen(col,2);

    		// Connect Center point to all the results of the KNN or RNN
    		for(i in points)
    		{
    			gr.drawLine(pen, center, points[i] );
    		}
    	}

    	/*
    	*  Function to truncate the double value to int value and return
    	*/
    	function truncate(_value)
    	{
    		if (_value<0) 
    			return Math.ceil(_value);
    		else 
    			return Math.floor(_value);
    	}

	    /*
	    *  Function to read the data from the remote server
	    */
	    function loadXMLDoc() {

	    	if (window.XMLHttpRequest)
	      	{
	      		// code for IE7+, Firefox, Chrome, Opera, Safari
	      		xmlhttp=new XMLHttpRequest();
	      	}
	      	else
	      	{
	      		// code for IE6, IE5
	      		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	      	}

	      	// Read the file name from the global POST which is received from the form input by user
	      	var outputFileName = "<?php echo $_POST["element_2"] ?>";

	      	// Open a connection to web server to read the file
	      	xmlhttp.open("POST",outputFileName,false);

	      	//check if the file is present or not
	      	// xmlhttp.onreadystatechange = function() {
	      	// 	if (self.xmlhttp.readyState == 4) {
	      	// 		if (self.xmlhttp.status == 404) {
	      	// 			alert('File: ' + outputFileName + ' does not exist. Exiting...');
	      	// 			return;
	      	// 		}
	      	// 	}
	      	// }

	      	// send command to the web serve to stream data to client web-server
	      	xmlhttp.send();

		    // Data received from the server
		    output= xmlhttp.responseText;

		    // Get the option if we need to print the input points
		    plotInputFlag = "<?php if (isset($_POST["element_5_1"])) { echo $_POST["element_5_1"]; } ?>";

		    // If user selected to print input then read the file name
		    if ( plotInputFlag == "yes")
		    {
		    	// Get the intput file name from the form
		    	var inputFilename = "<?php echo $_POST["element_1"] ?>";

			  	// Open connecton to read the new file
			  	xmlhttp.open("POST", inputFilename , false);

				//check the existance of the file on server
				// xmlhttp.onreadystatechange = function() {
				// 	if (self.xmlhttp.readyState == 4) {
				// 		if (self.xmlhttp.status == 404) {
				// 			alert('File: ' + inputFilename + ' does not exist. Exiting...');
				// 			return;
				// 		}
				// 	}
				// }	

				// Send request to server to trasfer the data trasfer
				xmlhttp.send();

			  	// Receive the data from server
			  	input = xmlhttp.responseText;

			}
		}

		/*
		*	Function to plot all the input points received from server on client web canvas
		*/
		function plotInput(){

			// Initialize the array of points
		    var points = new Array();

		    // Replace all the newline with space and split the lines at space
		    var array = input.replace( /\n/g, " " ).split( " " )


		    // Release the memory allocated to data variable
		    delete input;

		    // Process each word in the string
		    while (array.length > 0) 
		    {

	        	// Add the first two points from the string to the points array
	        	var x = array[0];
	        	var y = array[1];

	        	// points.push( new jsPoint(parseFloat(x),parseFloat(y)) );
	        	points.push( new jsPoint( truncate(parseFloat(x)),truncate(parseFloat(y)) ) );
	        	
	        	// Position of the array from  which we will remove two elements
	        	var position = array.indexOf(array[0]);

	        	// Remove two element from the array
	        	if (~position)
	        	{
	        		array.splice(position,2);
	        	}

        	}

       		//Create jsColor object
      		//var colBlack = new jsColor("black");

		    //Create jsPen object
		    // var pen = new jsPen(colBlack,1);

		    //Plot the points
		    plotPoints(points,2, "olive");

		}

		/*
		*	Function to plot all the result points for KNN or RNN
		*/
		function plotResult(){

		    // Create jsGraphics object
		    gr = new jsGraphics(document.getElementById("canvas"));

		    // Get the dynamic height and width of the canvas
		    var x = $('#canvas').width();
		    var y = $('#canvas').height();
		    

		    x = Math.ceil(x/2);
		    y = Math.ceil(y/2);

		    // Set the origin of the graph
		    gr.setOrigin(new jsPoint(x,y)); 
			

    		// Set the coordinate system 
    		gr.setCoordinateSystem("cartecian"); 

		    // Draw the coordinates
		    gr.showGrid(scale);


			// Initialize an array which will hold all the input points
			var points = new Array();

			// Replace all the newline with space and split the lines in spaces
			var string = output.replace( /(\n|\r\n|\t)/gm, " " );

			// Release memory allocated to output
			delete output;

			var array = string.split( " " );

			// Variable to hold store center x and y
			var cx = array[0];
			var cy = array[1];
			
			// Process each word in the string
			while (array.length > 0) {

		        // Add the first two points from the string to the points array
		        var x = array[2];
		        var y = array[3];

		        // Save the point object in the point array
		        points.push( new jsPoint( truncate(parseFloat(x)),truncate(parseFloat(y)) ) );

		        var position = array.indexOf(array[0]);

		        // Remove four element from the array
		        if (~position)
		        	array.splice(position,4);

	    	}

		    // Create jsColor object used for painting result points
		    var colRed = new jsColor("red");

		    //Create jsPen object
		    var pen = new jsPen(colRed,1);

		    //Plot the points
		    plotPoints(points,4,"red");

		    //draw the curve using the k points
		    //gr.drawClosedCurve(pen,points);

		   	//Set the center point
		   	center = new jsPoint(cx,cy);

		   	// Draw the center point
		   	gr.fillRectangle(new jsColor("black"), new jsPoint(center.x-3,center.y+3),3+2,3+2);

		   	// Draw lines from the center to the resultant points from KNN or RNN
		   	plotGraph(center,points);
	   }

	   /*
	   *   Main function which will be called when the page is loaded by the browser
	   */
	   function run() {

		//Read the data from the serve
		loadXMLDoc();

		//Plot the results from the map reduce function
		plotResult();

		//If the input flag is set plot all the input points
		if ( plotInputFlag == "yes"){
			plotInput();
		}

		//gr.fillRectangle(new jsColor("black"), new jsPoint(center.x-3,center.y-3),3+2,3+2);
	}

	// We'll run the AJAX query when the page loads.
	window.onload=run;

</script>
</body>
</html>
