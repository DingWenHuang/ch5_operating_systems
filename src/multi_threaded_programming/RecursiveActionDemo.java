package multi_threaded_programming;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

class MergeSort {
    public static void mergeSort(int[] list) {
        if (list.length > 1) {
            //merge sort the first half
            int[] firstHalf = new int[list.length / 2];
            System.arraycopy(list, 0, firstHalf, 0, list.length / 2);
            //從list的起點開始複製到firstHalf裡面，複製過去的位置從firstHalf的起點開始，長度為list的一半
            mergeSort(firstHalf);
            //merge sort the second half
            int secondHalfLength = list.length - list.length / 2;
            int[] secondHalf = new int[secondHalfLength];
            System.arraycopy(list, list.length / 2, secondHalf, 0, secondHalfLength);
            mergeSort(secondHalf);

            //merge
            merge(firstHalf, secondHalf, list);
        }
    }


    public static void merge(int[] list1,int[] list2, int tmp[]) {
        //將list1跟list2比較作排序的結果存在tmp裡面
        int i = 0;//要給list1用的
        int j = 0;//要給list2用的
        int k = 0;//要給tmp用的
        while (i < list1.length && j < list2.length) {
            if (list1[i] < list2[j]) {
                tmp[k] = list1[i];
                i++;
            } else {
                tmp[k] = list2[j];
                j++;
            }
            k++;
        }

        while (i < list1.length) {
            tmp[k] = list1[i];
            k++;
            i++;
        }
        while (j < list2.length) {
            tmp[k] = list2[j];
            k++;
            j++;
        }

    }
}



public class RecursiveActionDemo {
    private static class SortTask extends RecursiveAction {

        private final int THRESHOLD = 500;
        private int[] list;

        public SortTask(int[] list) {
            this.list = list;
        }

        @Override
        protected void compute() {
            if (list.length < THRESHOLD) {
                Arrays.sort(list);
            } else {
                //obtain the firsthalf
                int[] firstHalf = new int[list.length / 2];
                System.arraycopy(list, 0, firstHalf, 0, list.length / 2);
                //從list的起點開始複製到firstHalf裡面，複製過去的位置從firstHalf的起點開始，長度為list的一半
                //merge sort the second half
                int secondHalfLength = list.length - list.length / 2;
                int[] secondHalf = new int[secondHalfLength];
                System.arraycopy(list, list.length / 2, secondHalf, 0, secondHalfLength);

                invokeAll(new SortTask(firstHalf), new SortTask(secondHalf));
                MergeSort.merge(firstHalf, secondHalf, list);
            }
        }
    }

    public static void parelleMergSort(int[] list) {
        RecursiveAction recursiveAction = new SortTask(list);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(recursiveAction);
    }

    public static void main(String[] args) {
        final int SIZE = 7000000;
        int[] list1 = new int[SIZE];
        int[] list2 = new int[SIZE];

        for (int i = 0; i < list1.length; i++) {
            list1[i] = (int) (Math.random()*100000);
            list2[i] = (int) (Math.random()*100000);

        }

        long startTime = System.currentTimeMillis();
        parelleMergSort(list1);
        long endTime = System.currentTimeMillis();
        System.out.println("Parallel time with " + Runtime.getRuntime().availableProcessors() + " processors is " + (endTime - startTime) + " milliseconds");

        startTime = System.currentTimeMillis();
        MergeSort.mergeSort(list2);
        endTime = System.currentTimeMillis();
        System.out.println("Sequential time is " + (endTime - startTime) + " milliseconds");
    }
}
