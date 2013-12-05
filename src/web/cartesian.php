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

		//Center of the solutions
		var center;

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
	      	// xmlhttp.onreadystatechange = function() {
	      	// 	if (self.xmlhttp.readyState == 4) {
	      	// 		if (self.xmlhttp.status == 404) {
	      	// 			alert('File: ' + outputFileName + ' does not exist. Exiting...');
	      	// 			return;
	      	// 		}
	      	// 	}
	      	// }

	      	xmlhttp.send();

		    //Data received from the server
		    output= xmlhttp.responseText;

		    plotInputFlag = "<?php if (isset($_POST["element_5_1"])) { echo $_POST["element_5_1"]; } ?>";

		    if ( plotInputFlag == "yes"){
		    	var inputFilename = "<?php echo $_POST["element_1"] ?>";

			  //Open connecton to read the new file
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

				 xmlhttp.send();

			  //Receive the data from server
			  input = xmlhttp.responseText;

			}
		}

		function plotInput(){

			//poionts in the graph
		    //var points = new Array(new jsPoint(28,35),new jsPoint(52,16),new jsPoint(177,38),new jsPoint(149,85),new jsPoint(57,92));
		    var points = new Array();

		    //Replace all the newline with space and split the lines in spaces
		    var array = input.replace( /\n/g, " " ).split( " " )


		    //Release the memory allocated to data variable
		    delete input;

		    
		    //Process each word in the string
		    while (array.length > 0) {

        	//add the first two points from the string to the points array
        	var x = array[0];
        	var y = array[1];

        	// points.push( new jsPoint(parseFloat(x),parseFloat(y)) );
        	points.push( new jsPoint( truncate(parseFloat(x)),truncate(parseFloat(y)) ) );
        	
        	var position = array.indexOf(array[0]);

        	//Remove two element from the array
        	if (~position)
        		array.splice(position,2);

        }

       		//Create jsColor object
      		//var colBlack = new jsColor("black");

		    //Create jsPen object
		    // var pen = new jsPen(colBlack,1);

		    //Plot the points
		    plotPoints(points,2, "olive");


		}

		function plotResult(){

		    //Create jsGraphics object
		    gr = new jsGraphics(document.getElementById("canvas"));

		    //get the dynamic height and width of the canvas
		    var x = $('#canvas').width();
		    var y = $('#canvas').height();
		    // alert ( Math.ceil(x/2) + " " + Math.ceil(y/2));

		    x = Math.ceil(x/2);
		    y = Math.ceil(y/2);

		    //set the origin of the graph
		    gr.setOrigin(new jsPoint(x,y)); 
			//gr.setOrigin(new jsPoint(400,400)); 

    		//Set the coordinate system 
    		gr.setCoordinateSystem("cartecian"); 

		    //Draw the coordinates
		    gr.showGrid(scale);


		//poionts in the graph
		//var points = new Array(new jsPoint(28,35),new jsPoint(52,16),new jsPoint(177,38),new jsPoint(149,85),new jsPoint(57,92));
		var points = new Array();

		//Replace all the newline with space and split the lines in spaces
		//var array = output.replace( /\n/g, " " ).split( " " )


		// output=output.replace(/\n/g, " " );
		// output=String(output).replace( "\t", " " );
		// output=output.replace( '(', ' ' );
		// output=output.replace( ')', ' ' );

		//Release the memory allocated to data variable
		delete output;

		//Process each word in the string
		while (array.length > 0) {

        //add the first two points from the string to the points array
        var x = array[0];
        var y = array[1];

        points.push( new jsPoint( truncate(parseFloat(x)),truncate(parseFloat(y)) ) );

        var position = array.indexOf(array[0]);

        //Remove two element from the array
        if (~position)
        	array.splice(position,2);

    }

	    //Create jsColor object
	    var colRed = new jsColor("red");

	    //Create jsPen object
	    var pen = new jsPen(colRed,1);

	    //Plot the points
	    plotPoints(points,4,"red");

	    //draw the curve using the k points
	    //gr.drawClosedCurve(pen,points);


	   	//Center
	   	//var center = new jsPoint(187,181);

	   	gr.fillRectangle(new jsColor("black"), new jsPoint(center.x-2,center.y+2),2+2,2+2);

	   	plotGraph(center,points);
	   }

	   function run() {

		//Read the data from the serve
		loadXMLDoc();

		//Plot the results from the map reduce function
		plotResult();

		//If the input flag is set plot all the input points
		if ( plotInputFlag == "yes"){
			plotInput();
		}
	}

	// We'll run the AJAX query when the page loads.
	window.onload=run;

</script>
</body>
</html>
