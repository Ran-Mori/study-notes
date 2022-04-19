package sort;

public class InsertSort {
    public int[] insertSort(int [] nums){
        int length = nums.length;
        if (length == 1)
            return nums;
        int key = 0;
        for (int i = 1;i < length;i++){
            key = nums[i];
            for (int j = i -1; j > -1; j--){
                if (nums[j] > key)
                    nums[j+1] = nums[j];
                else {
                    nums[j+1] = key;
                    break;
                }
            }
        }
        return nums;
    }
}

