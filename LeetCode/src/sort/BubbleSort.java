package sort;

public class BubbleSort {
    public int[] bubbleSort(int[] nums){
        int length = nums.length;
//        int temp;
//        for (int i = length -1;i > -1; i--){
//            for (int j = 1; j <= i;j++){
//                if(nums[j-1] > nums[j]){
//                    temp = nums[j-1];
//                    nums[j-1] = nums[j];
//                    nums[j] = temp;
//                }
//            }
//        }

        for (int end = length - 1;end > -1;end--){
            for (int i = 1;i <= end;i++){
                if (nums[i - 1] > nums[i]){
                    int temp = nums[i - 1];
                    nums[i - 1] = nums[i];
                    nums[i] = temp;
                }
            }
        }

        return nums;
    }
}
