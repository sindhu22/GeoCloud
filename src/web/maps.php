<!DOCTYPE html>

<html>
<head>

  <meta charset="utf-8">
    <!-- <title>Marker animations with <code>setTimeout()</code></title> -->

<style>

html, body, #map-canvas
{
height: 100%;
margin: 0px;
padding: 0px
}

#panel
{
position: absolute;
top: 5px;
left: 50%;
      margin-left: -180px;
      z-index: 5;
      background-color: #fff;
padding: 5px;
border: 1px solid #999;
}

</style>

<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>

  <script>
  // If you're adding a number of markers, you may want to
  // drop them on the map consecutively rather than all at once.
  // This example shows how to use setTimeout() to space
  // your markers' animation.

  var berlin = new google.maps.LatLng(47, -122.410186);


  // var neighborhoods = [
  //   new google.maps.LatLng(52.511467, 13.447179),
  //   new google.maps.LatLng(52.549061, 13.422975),
  //   new google.maps.LatLng(52.497622, 13.396110),
  //   new google.maps.LatLng(52.517683, 13.394393)
  // ];

  var neighborhoods = [];
  var lines = [];
  var line = [];

/*
   ======================================================
   functions to plot neighbours on the map
   ======================================================
   */

function main()
{
	var contentsOfFileAsString = FileHelper();

	// document.body.innerHTML = contentsOfFileAsString;
	lines = contentsOfFileAsString.split("\n");
	// document.write(l[0]);

	for (var i = 0; i < lines.length; i++)
	{
		line = lines[i].split(" ");
		neighborhoods[i] = new google.maps.LatLng(line[0], line[1]);
	};
}
function FileHelper()
{
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

	//check the existance of the file on the server
	//xmlhttp.onreadystatechange = function() {
	// if (self.xmlhttp.readyState == 4) {
	//  if (self.xmlhttp.status == 404) {
	//   alert('File: ' + outputFileName + ' does not exist. Exiting...');
	//  return;
	// }
	// }
	// }  

	xmlhttp.send();

	//Data received from the server
	return xmlhttp.responseText;
}
var markers = [];
var iterator = 0;

var map;

function initialize() {
	var mapOptions = {
	zoom: 6,
      	center: berlin
	};

	map = new google.maps.Map(document.getElementById('map-canvas'),
			mapOptions);
}

function drop() {
	drop1();
	main();
	for (var i = 0; i < neighborhoods.length; i++) {
		setTimeout(function() {
				addMarker();
				}, i * 500);
	}
}

function addMarker() {

	markers.push(new google.maps.Marker({
position: neighborhoods[iterator],
map: map,
draggable: false,
animation: google.maps.Animation.BOUNCE,
icon: 'http://maps.google.com/mapfiles/ms/icons/green-dot.png'
}));
iterator++;
}
/*
   ======================================================
   functions to plot all points on the map
   ======================================================
 */

var input = [];
var iplines = [];
var ipline = [];


function main1()
{

	var plotInputFlag = "<?php if (isset($_POST["element_5_1"])) { echo $_POST["element_5_1"]; } ?>";
	var inputFilename = "";

	if ( plotInputFlag == "yes"){
		inputFilename = "<?php echo $_POST["element_1"] ?>";

		if (window.XMLHttpRequest)
		{// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp=new XMLHttpRequest();
		}
		else
		{// code for IE6, IE5
			xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}

		//Open connecton to read the new file
		xmlhttp.open("POST", inputFilename , false);

		//Check the exitance of the file on the server
		// xmlhttp.onreadystatechange = function() {
		// if (self.xmlhttp.readyState == 4) {
		//   if (self.xmlhttp.status == 404) {
		//    alert('File: ' + inputFilename + ' does not exist. Exiting...');
		//    return;
		// }
		// }
		// }  

		xmlhttp.send();

		//Receive the data from server
		var contentsOfFileAsString = xmlhttp.responseText;


		iplines = contentsOfFileAsString.split("\n");
		// document.write(l[0]);

		for (var i = 0; i < iplines.length; i++) {
			ipline = iplines[i].split("\t");
			input[i] = new google.maps.LatLng(ipline[0], ipline[1]);
		};
	}
}

function FileHelper1()
{

}
// main1();
var mark = [];
var iter = 0;

function drop1() {
	main1();
	for (var i = 0; i < input.length; i++) {
		setTimeout(function() {
				addMarker1();
				}, i * 200);
	}
}

function addMarker1() {
	// marker.setIcon('http://maps.google.com/mapfiles/ms/icons/green-dot.png')
	markers.push(new google.maps.Marker({
position: input[iter],
map: map,
draggable: false,
animation: google.maps.Animation.DROP

}));
iter++;
}

</head>
</html>
