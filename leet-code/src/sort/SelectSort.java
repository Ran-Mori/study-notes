package sort;

public class SelectSort {
    public int[] selectSort(int[] nums){
        int length = nums.length;
        if (length <= 1)
            return nums;

        int smallestIndex = -1;
        int temp = -1;
        for (int i = 0; i < length;i++){
            smallestIndex = i;
            for (int j = i; j < length;j++){
                if (nums[j] < nums[smallestIndex])
                    smallestIndex = j;
            }
            temp = nums[i];
            nums[i] = nums[smallestIndex];
            nums[smallestIndex] = temp;
        }
        return nums;
    }
}
