package com.geocloud.rtree;

/* Original R-Tree Paper */
/* http://www-db.deis.unibo.it/courses/SI-LS/papers/Gut84.pdf */
/* R-Tree for indexing 2 dimensional points will be implemented */
/* in RTree class. Below contains possible APIs that might be */
/* required for implementing R-Trees. This will be implemented */
/* in stages. The index will be written to disk. */

public class RTree {

    private class Node {
        Node Parent;
        Node left;
        Node Right;
        int x;
        int y;
    }

    public void RTree() {
    }

    public void Insert() {
    }

    public void Delete() {
    }

    public void Adjust() {
    }

    public void Search() {
    }

    public void Destroy() {
    }

    public int GetSize() {
        return 0;
    }

    public int GetMinEntries(){
        return 0;
    }

    public int GetMaxEntries() {
        return 0;
    }

    public void WriteToFile() {
    }

    public void ReadFromFile() {
    }

    private void _getNode() {
    }

    private void _chooseLeaf() {
    }

    private void _findLeaf() {
    }

    private void _condenseTree(){
    }

    private void _splitNode() {
    }

    private void _deleteNode() {
    }

    private void _quadSplit() {
    }

    private void _linearSplit() {
    }

    private void pickSeeds() {
    }

    private void pickNext() {
    }

    public static void main(String[] args) {
        System.out.println("R-Tree Implementation");
    }
}
