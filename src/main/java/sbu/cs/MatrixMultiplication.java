package sbu.cs;

import java.util.ArrayList;
import java.util.List;

public class MatrixMultiplication {

    // You are allowed to change all code in the BlockMultiplier class
    public static class BlockMultiplier implements Runnable
    {
        List<List<Integer>> tempMatrixProduct;
        List<List<Integer>> matrix_A;
        List<List<Integer>> matrix_B;
        int p;
        int q;
        int r;
        public BlockMultiplier(List<List<Integer>> matrix_A, List<List<Integer>> matrix_B) {
            // TODO
            this.p = matrix_A.size();
            this.q = matrix_B.size();
            this.r = matrix_B.get(0).size();
            this.tempMatrixProduct = new ArrayList<>();
            this.matrix_A = matrix_A;
            this.matrix_B = matrix_B;
        }

        @Override
        public void run() {
            /*
            TODO
                Perform the calculation and store the final values in tempMatrixProduct
            */
            for (int i = 0; i < p; i++) {
                List<Integer> tempRow = new ArrayList<>();
                for (int j = 0; j < r; j++) {
                    Integer tempNum = 0;
                    for (int k = 0; k < q; k++) {
                        tempNum += matrix_A.get(i).get(k) * matrix_B.get(k).get(j);
                    }
                    tempRow.add(tempNum);
                }
                this.tempMatrixProduct.add(tempRow);
            }
        }
        public List<List<Integer>> getTempMatrixProduct() {
            return tempMatrixProduct;
        }
    }

    /*
    Matrix A is of the form p x q
    Matrix B is of the form q x r
    both p and r are even numbers
    */

    // a function to get subMatrix to dividing the ans matrix between 4 sids
    public static List<List<Integer>> getSubMatrix(List<List<Integer>> matrix, int left, int right, int top, int bottom) {
        List<List<Integer>> subMatrix = new ArrayList<>();

        for (int i = top; i < bottom; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = left; j < right; j++) {
                row.add(matrix.get(i).get(j));
            }
            subMatrix.add(row);
        }

        return subMatrix;
    }
    public static List<List<Integer>> ParallelizeMatMul(List<List<Integer>> matrix_A, List<List<Integer>> matrix_B)
    {
        /*
        TODO
            Parallelize the matrix multiplication by dividing tasks between 4 threads.
            Each thread should calculate one block of the final matrix product. Each block should be a quarter of the final matrix.
            Combine the 4 resulting blocks to create the final matrix product and return it.
         */

        List<List<Integer>> northWest_A = getSubMatrix(matrix_A, 0, matrix_A.get(0).size(), 0, matrix_A.size() / 2); // --> p/2 & q  ,  q & r/2
        List<List<Integer>> northWest_B = getSubMatrix(matrix_B, 0, matrix_B.get(0).size() / 2, 0, matrix_B.size());
        BlockMultiplier c1 = new BlockMultiplier(northWest_A, northWest_B);
        Thread t1 = new Thread(c1);

        List<List<Integer>> northEast_A = getSubMatrix(matrix_A, 0, matrix_A.get(0).size(), 0, matrix_A.size() / 2); // --> p/2 & q  ,  q & r - r/2
        List<List<Integer>> northEast_B = getSubMatrix(matrix_B, matrix_B.get(0).size() / 2, matrix_B.get(0).size(), 0, matrix_B.size());
        BlockMultiplier c2 = new BlockMultiplier(northEast_A, northEast_B);
        Thread t2 = new Thread(c2);

        List<List<Integer>> southWest_A= getSubMatrix(matrix_A, 0, matrix_A.get(0).size(), matrix_A.size() / 2, matrix_A.size()); // --> p - p/2 & q  ,  q & r/2
        List<List<Integer>> southWest_B = getSubMatrix(matrix_B, 0, matrix_B.get(0).size() / 2, 0, matrix_B.size());
        BlockMultiplier c3 = new BlockMultiplier(southWest_A, southWest_B);
        Thread t3 = new Thread(c3);

        List<List<Integer>> southEast_A = getSubMatrix(matrix_A, 0, matrix_A.get(0).size(), matrix_A.size() / 2, matrix_A.size()); // --> p - p/2 & q  ,  q & r - r/2
        List<List<Integer>> southEast_B = getSubMatrix(matrix_B, matrix_B.get(0).size() / 2, matrix_B.get(0).size(), 0, matrix_B.size());
        BlockMultiplier c4 = new BlockMultiplier(southEast_A, southEast_B);
        Thread t4 = new Thread(c4);


        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        }
        catch (InterruptedException i) {
            i.printStackTrace();
        }

        List<List<Integer>> resultMatrix = new ArrayList<>();

        List<List<Integer>> tempMatrixProduct1 = c1.getTempMatrixProduct();
        List<List<Integer>> tempMatrixProduct2 = c2.getTempMatrixProduct();
        List<List<Integer>> tempMatrixProduct3 = c3.getTempMatrixProduct();
        List<List<Integer>> tempMatrixProduct4 = c4.getTempMatrixProduct();

        for (int i = 0; i < matrix_A.size() / 2; i++) {
            resultMatrix.add(new ArrayList<>(tempMatrixProduct1.get(i)));
            resultMatrix.get(i).addAll(tempMatrixProduct2.get(i));
        }

        for (int i = 0; i < matrix_B.get(0).size() / 2; i++) {
            resultMatrix.add(new ArrayList<>(tempMatrixProduct3.get(i)));
            resultMatrix.get(i + matrix_A.size() / 2).addAll(tempMatrixProduct4.get(i));
        }

        return resultMatrix;
    }

    public static void main(String[] args) {
        // Test your code here
    }
}
