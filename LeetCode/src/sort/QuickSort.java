package sort;

public class QuickSort {
    public int[] quickSort(int[] nums){
        sort(nums,0,nums.length-1);
        return nums;
    }

    public void sort(int[] nums,int left,int right){
        if (left < right){
            int key = nums[left];
            int leftStore = left;
            int rightStore = right;
            while (left < right){
                while (left < right && nums[right] > key)
                    right--;
                if (left < right){
                    nums[left] = nums[right];
                    left++;
                }
                while (left < right && nums[left] < key)
                    left++;
                if (left < right){
                    nums[right] = nums[left];
                    right--;
                }
            }
            nums[left] = key;
            sort(nums,leftStore,left-1);
            sort(nums,left+1,rightStore);
        }
    }
}
