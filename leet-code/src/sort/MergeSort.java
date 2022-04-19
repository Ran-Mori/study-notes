package sort;

public class MergeSort {
    public int[] mergeSort(int[] nums){
        sort(nums,0,nums.length-1);
        return nums;
    }

    public void sort(int[] nums,int left,int right){
        if (left < right){
            int mid = (left + right) / 2;
            sort(nums,left,mid);
            sort(nums,mid+1,right);
            merge(nums,left,right);
        }
    }
    public void merge(int[] nums,int left,int right){
        if (left == right)
            return;
        int mid = (left + right) / 2;
        int i = left;
        int j = mid + 1;

        int[] temp = new int[right - left + 1];
        int index = 0;
        while (i <= mid && j <= right){
            if (nums[i] < nums[j])
                temp[index++] = nums[i++];
            else
                temp[index++] = nums[j++];
        }
        while (i <= mid)
            temp[index++] = nums[i++];
        while (j <= right)
            temp[index++] = nums[j++];
        index = 0;
        while (left <= right)
            nums[left++] = temp[index++];
    }
}
