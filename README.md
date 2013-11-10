GeoCloud
========

Geospatial Query Processing. EEL6935 Final Project

Geospatial queries (GQ) have been used in a wide variety of applications such as decision support systems,
profile-based marketing, bioinformatics and GIS. Most of the existing query-answering approaches assume
centralized processing on a single machine although GQs are intrinsically parallelizable. There are some
approaches that have been designed for parallel databases and cluster systems; however, these only
apply to the systems with limited parallel processing capability, far from that of the cloud-based platforms. 
In this paper, we study the problem of parallel geospatial query processing with the MapReduce programming model. 
Our proposed approach creates a spatial index, KNN diagram, for given data points in 2D space and enables
efficient processing of a wide range of GQs. We evaluated the performance of our proposed techniques and
correspondingly compared them with their closest related work while varying the number of employed nodes. 
