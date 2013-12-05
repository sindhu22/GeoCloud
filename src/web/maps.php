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

</head>
</html>
