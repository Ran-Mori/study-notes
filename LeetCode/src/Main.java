import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> list = func("12345678");
        for (String s : list)
            System.out.println(s);
    }
    private static List<String> func(String input){
        int length = input.length();
        List<String> result = new ArrayList<>();

        if (length == 0)
            return result;

        int index = 8;
        while (index < length){
            result.add(input.substring(index - 8,index));
            index += 8;
        }

        StringBuilder builder = new StringBuilder(input.substring(index - 8,length));
        int tempLength = builder.length();
        for (int i = 0;i < 8 - tempLength;i++)
            builder.append("0");
        result.add(builder.toString());
        return result;
    }
}
























/*
ListNode node1 = new ListNode(1,null);
ListNode node2 = new ListNode(2,null);
ListNode node3 = new ListNode(3,null);
ListNode node4 = new ListNode(4,null);
ListNode node5 = new ListNode(5,null);
ListNode node6 = new ListNode(6,null);
ListNode node7 = new ListNode(7,null);

node1.next = node2;
node2.next = node3;
node3.next = node4;
node4.next = node5;
node5.next = node6;
node6.next = node7;
*/
/*
TreeNode node1 = new TreeNode(1,null,null);
TreeNode node2 = new TreeNode(2,null,null);
TreeNode node3 = new TreeNode(3,null,null);
TreeNode node4 = new TreeNode(4,null,null);
TreeNode node5 = new TreeNode(5,null,null);
TreeNode node6 = new TreeNode(6,null,null);
TreeNode node7 = new TreeNode(7,null,null);
*/
