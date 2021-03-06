GeoCloud
========

Geospatial Query Processing. EEL6935 Final Project


Team Members:
1. Abhijeet Nayak
2. Dhananjay Bhirud
3. Kumar Sadhu
4. Sindhu Suryanarayana


Geospatial queries (GQ) have been used in a wide variety of applications such as decision support systems,
profile-based marketing, bioinformatics and GIS. Most of the existing query-answering approaches assume
centralized processing on a single machine although GQs are intrinsically parallelizable. There are some
approaches that have been designed for parallel databases and cluster systems; however, these only
apply to the systems with limited parallel processing capability, far from that of the cloud-based platforms. 
In this paper, we study the problem of parallel geospatial query processing with the MapReduce programming model. 
Our proposed approach creates a spatial index, KNN diagram, for given data points in 2D space and enables
efficient processing of a wide range of GQs. We implemented two KNN and RNN query processing using two approaches.
In the first approach, called distance vector method we use eucledian distance between two points as a vector and
compute the nearest neighbors. In the second approach, using RTree, we query KNN and RNN using minimum bounding 
rectangles. We evaluated the performance of our proposed techniques and correspondingly compared them with their 
closest related work while varying the number of employed nodes. The project also has a UI tp show the results of
KNN and RNN query through maps API and JS 2D API. A real life application for search on starbucks USA locations
has been implemented.

