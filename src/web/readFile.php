<?php 
	// Prevent caching.
header('Cache-Control: no-cache, must-revalidate');
header('Expires: Mon, 01 Jan 1996 00:00:00 GMT');

	// The JSON standard MIME header.
header('Content-type: application/json');


	//set the file name
$filename = "file.txt";

	//Open the file and throw error in case of error
if (file_exists($filename) && is_readable ($filename)) {
	$fh = fopen($filename, "r");

		# Processing
	while (!feof($fh)){

		$line = fgets($fh);

			echo $line ;
			$words = explode(" ", $line);

			if (sizeof($words) > 1){
				if (isset($words[0])){
					//echo $words[0];
					$x = doubleval($words[0]);
					echo "x: ";
					echo $x;
				}	
				echo ' ';
				if (isset($words[1])){
					//echo $words[1];
					$y = doubleval($words[1]);
					echo "y: ";
					echo $y;
				}
				echo "<br>";
			}

			echo json_encode($line);
		echo "line read";
	}
	echo json_encode("abhijeet");

		//Close the file
	fclose($fh);
}else{
	echo "Error: Unable to open the file." ;
}

?>