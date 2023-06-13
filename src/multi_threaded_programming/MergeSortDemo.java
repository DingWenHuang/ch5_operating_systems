package multi_threaded_programming;

public class MergeSortDemo {

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




    public static void main(String[] args) {
        int[] list = {4, 3, 5, 7, -21, 95, 9, 20, 600, 0, 1, 85, 77, 89};
        mergeSort(list);
        for (int n : list) {
            System.out.print(n + " ");
            //-21 0 1 3 4 5 7 9 20 77 85 89 95 600
        }
    }
}
