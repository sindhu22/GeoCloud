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

		

	</script>
</body>
</html>
