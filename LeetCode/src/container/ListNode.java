package container;

public class ListNode implements Cloneable{
    public int val;
    public ListNode next;
    public ListNode() {}
    public ListNode(int val) { this.val = val; }
    public ListNode(int val, ListNode next) { this.val = val; this.next = next; }

    @Override
    public Object clone(){
        return new ListNode(this.val,this.next);
    }
}
