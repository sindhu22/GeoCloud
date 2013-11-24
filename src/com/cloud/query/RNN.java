package com.cloud.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import PAM.PAM;

public class RNN {

        private float t =1;
        public int deletedPoints=0;
       
       
        /**
         * list of current elements/clusters
         */
        private ArrayList<Element> listElements;

        public RNN(float[][] datas) {

                listElements = new ArrayList<RNN.Element>(datas.length);

                for (float[] f : datas) {
                        listElements.add(new CentroidCluster(f));
                }
        }

        public void cluster() {

                // current chain
                ArrayList<Element> chain = new ArrayList<RNN.Element>();
                ArrayList<Element> elementsLeft = new ArrayList<RNN.Element>();
                ArrayList<Float> sim = new ArrayList<Float>();

                // start point for chain
                int currentElementIndex = 0;
                while (currentElementIndex < listElements.size()
                                && listElements.size() > 1) {
         //               System.out.println(currentElementIndex+"\tof\t"+listElements.size());
                        // initialize chains
                        chain.clear();
                        sim.clear();
                        // elementsLeft.clear();
                        elementsLeft = (ArrayList<Element>) listElements.clone();

                        // initialize first two start elements
                        Element currentElement = listElements.get(currentElementIndex);
                        chain.add(currentElement);
                        elementsLeft.remove(currentElement);

                        NNContainer firstNN = this.getNN(elementsLeft, currentElement);
                        chain.add(firstNN.e);
                        sim.add(firstNN.sim);
                        elementsLeft.remove(firstNN.e);

                        if(currentElement.points.length==1&&firstNN.sim>t){
                               
                                listElements.remove(currentElementIndex);
                                                               
                                deletedPoints++;
       //                         System.out.println("deleted: "+deletedPoints+"\t nrOfElements: "+listElements.size());
                                continue;
                        }
                       
                        // fill chain
                        // for(int
                        // nnIndex=0;nnIndex<elementsLeft.size()&&chain.size()>1;nnIndex++){
                        while (elementsLeft.size() > 0 && chain.size() > 1) {
                                currentElement = chain.get(chain.size() - 1);
                                NNContainer nn = this.getNN(elementsLeft, currentElement);

                                float lastsim = sim.get(sim.size() - 1);
                                if (nn.sim < lastsim) {
                                        // No RNN -> Add to NN chain
                                        chain.add(nn.e);
                                        sim.add(nn.sim);
                                        elementsLeft.remove(nn.e);
                                       
                                } else {
                                        // found RNNs -> agglomerate last two chain links
                                        if (lastsim < t) {
                                        	//System.out.println("found RNN");
                                                // create new cluster
                                                Element c1 = chain.get(chain.size() - 1);
                                                Element c2 = chain.get(chain.size() - 2);
                                                Element newCluster = (c1.agglomerate(c2));

                                                // new index value for next chain if possible
                                                if (listElements.indexOf(c1) <= currentElementIndex)
                                                        currentElementIndex--;
                                                if (listElements.indexOf(c2) <= currentElementIndex)
                                                        currentElementIndex--;
                                               
                                                // delete old cluster every where
                                                chain.remove(c1);
                                                chain.remove(c2);
                                               
                                                sim.remove(sim.size()-1);                                              
                                               
                                                listElements.remove(c1);
                                                listElements.remove(c2);

                                                // add new cluster to chains
                                                elementsLeft.add(newCluster);
                                                listElements.add(newCluster);
                                        } else {
                                                // begin new chain
                                                System.out.println("break chain");
                                                break;                                          
                                        }
                                }

                                // chain.add(currentElement);
                                // elementsLeft.remove(currentElement);
                        }
                        currentElementIndex++;
                }

        }

        private NNContainer getNN(List<Element> chain, Element e) {

                float bestSim = Float.MAX_VALUE;
                Element bestElement = null;

                // for (int i = startIndex; i < chain.size(); i++) {
                for (Element elementOfList : chain) {
                        // Element elementOfList = chain.get(i);

                        if (elementOfList != e) {
                                float sim;
                               
                                sim = e.sim(elementOfList);
                               

                                if (sim < bestSim) {
                                        bestSim = sim;
                                        bestElement = elementOfList;
                                }

                        }
                }

                if(bestElement==null)
                        System.out.println("oh ne");
               
                return new NNContainer(bestElement, bestSim);

        }

        private class NNContainer {
                public Element e;
                public float sim;

                public NNContainer(Element e, float sim) {
                        this.e = e;
                        this.sim = sim;
                }
        }

        /**
         * @return the t
         */
        public float getT() {
                return t;
        }

        /**
         * @param t
         *            the t to set
         */
        public void setT(float t) {
                this.t = t;
        }

        public static abstract class Element {
               
                public float[][] points;
               
                public abstract float sim(Element cluster);

                public float sim(float[] p1, float[] p2) {

                        float sum = 0;
                        for (int i = 0; i < p1.length; i++) {
                                sum += Math.pow(p2[i] - p1[i], 2);
                        }

                        return (float) Math.sqrt(sum);
                }
                                       
                public abstract Element agglomerate( Element c1);

        }

        public static class GroupAvgCluster extends Element {
                public float[][] points;

                public GroupAvgCluster(float[] point) {
                        this.points = new float[1][];
                        this.points[0] = point;
                }

                public GroupAvgCluster(float[][] listPoints) {
                        this.points = listPoints;
                }

                @Override
                public float sim(Element cluster) {
                        GroupAvgCluster c= (GroupAvgCluster) cluster;
                        float sum = 0;

                        for (float[] c1 : points)
                                for (float[] c2 : c.points) {
                                        sum += sim(c1, c2);
                                }

                        sum = sum / (c.points.length);

                        return sum;
                }

               
                public GroupAvgCluster agglomerate(Element cluster) {
                        GroupAvgCluster c1=(GroupAvgCluster) cluster;
                        float[][] newPointsList = new float[c1.points.length
                                        + points.length][];

                        System.arraycopy(c1.points, 0, newPointsList, 0, c1.points.length);
                        System.arraycopy(points, 0, newPointsList, c1.points.length,
                                        points.length);

                        GroupAvgCluster c = new GroupAvgCluster(newPointsList);
     //                   System.out.println("agglomerate to "+newPointsList.length);
                        return c;
                }

                @Override
                public String toString() {
                        String txt = "";
   //                     txt += "size: " + points.length + "\n";

//                      for (float[] f : points) {
//                              txt += "\t" + Arrays.toString(f) + "\n";
//                      }
                        txt += "\n";

                        return txt;
                }

        }


        public static class CentroidCluster extends Element {
               
                public float[] centroid;
               
                public CentroidCluster(float[] point) {
                        this.points = new float[1][];
                        this.points[0] = point;
                        centroid=point;
                }

                public CentroidCluster(float[][] listPoints) {
                        this.points = listPoints;
                       
                        centroid=new float[listPoints[0].length];
                        for(int pIndex=0;pIndex<listPoints.length;pIndex++){
                                float[] point = listPoints[pIndex];
                                for(int i=0;i<centroid.length;i++){
                                        centroid[i]+=point[i];
                                }
                        }
                        for(int i=0;i<centroid.length;i++){
                                centroid[i]*=1.0f/listPoints.length;
                        }                      
                }

                public CentroidCluster(float[][] listPoints, float[] centroid) {
                        this.points = listPoints;
                       
                        this.centroid=centroid;                
                }
               
                @Override
                public float sim(Element cluster) {
                        CentroidCluster c= (CentroidCluster) cluster;
                       
                        return sim(c.centroid, centroid);
                }

               
                public CentroidCluster agglomerate(Element cluster) {
                        CentroidCluster c1= (CentroidCluster) cluster;
                        float[][] newPointsList = new float[c1.points.length
                                        + points.length][];

                        System.arraycopy(c1.points, 0, newPointsList, 0, c1.points.length);
                        System.arraycopy(points, 0, newPointsList, c1.points.length,
                                        points.length);

                        float[] newCentroid= new float[centroid.length];
                        float sum=c1.points.length+points.length;
                        for(int i=0;i<centroid.length;i++){
                                newCentroid[i]+=c1.points.length*c1.centroid[i]+points.length*centroid[i];
                                newCentroid[i]/=sum;
                        }
                       
                        CentroidCluster c = new CentroidCluster(newPointsList);

 //                       System.out.println("agglomerate to "+newPointsList.length);
                        return c;
                }

                @Override
                public String toString() {
                        String txt = "size: " + points.length + "\n";
//                      txt += Arrays.toString(centroid)+"\n";
//                      for (float[] f : points) {
//                              txt += "\t" + Arrays.toString(f) + "\n";
//                      }
//                      txt += "\n";

                        return txt;
                }
        }
       

        public static float[][] reduceArray(float[][] array, int limit){
                float[][] newArray= new float[limit][];
                System.arraycopy(array, 0, newArray, 0, limit);
               
                return newArray;
        }
       
        /**
         * @param args
         */
        public static void main(String[] args) {
                float[][] datas = new float[13][2];

                datas[0] = new float[] { 0.1f, 0.1f };
                datas[1] = new float[] { 0.1f, 0.2f };
                datas[2] = new float[] { 0.2f, 0.1f };
                datas[3] = new float[] { 0.2f, 0.2f };

                datas[4] = new float[] { 0.8f, 0.8f };
                datas[5] = new float[] { 0.8f, 0.9f };
                datas[6] = new float[] { 0.9f, 0.8f };
                datas[7] = new float[] { 0.9f, 0.9f };
                datas[8] = new float[] { 0.85f, 0.85f };

                datas[9] = new float[] { 0.1f, 0.8f };
                datas[10] = new float[] { 0.1f, 0.9f };
                datas[11] = new float[] { 0.2f, 0.8f };
                datas[12] = new float[] { 0.2f, 0.9f };

                //datas=PAM.loadClusterData("toCluster.data");
                datas=RNN.reduceArray(datas, 13);
                               
                RNN rnn = new RNN(datas);
                //rnn.setT(25000);
//              rnn.setT(0.4f);
               
                rnn.cluster();
               
//        System.out.println(datas[0].length);
//        System.out.println(Arrays.toString(datas[0]));
                for (Object o : rnn.listElements) {
                        System.out.println(o.toString());
                }
                for (Element e : rnn.listElements) {
                	
					//float [][] temp=e1.points;
                	//for(int i=0; i<temp.length;i++){
                	//	for(int j=0;j<temp[i].l)
                	//}
                	float [][] temp=e.points;
                	for(int i=0; i<temp.length;i++){
                		//System.out.println("float size : "+temp[i].length);
                		//for(int j=0;j<temp[i].length;j++)
                			System.out.println("point is: "+temp[i][0]+", "+temp[i][1]);
                    }
                    //System.out.println("e.sim is : "+e.sim(e));
                    //System.out.println("e.points is : "+e.points);
                }
                
                System.out.println("\nAnzahl cluster(No of clusters):\t"+rnn.listElements.size());
                System.out.println("gelöschte Objecte(deleted objects): "+rnn.deletedPoints);
                // System.out.println("average siluette:\t"+pam.averageSilhuette());
                // System.out.println("empty distanzes:\t"+pam.distanceMatrix.findPercentageOfNotCalcDistance()+" %");

        }

}
