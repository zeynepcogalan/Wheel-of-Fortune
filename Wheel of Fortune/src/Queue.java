
public class Queue {
	private int rear , front;
	private Object[] elements;
	
	Queue(int capacity){
		elements = new Object[capacity];
		rear = -1;
		front = 0;
	}
	
	public void enqueue(Object data) {
		if(isFull()) {
			System.out.println ("Queue overflow");
		}
		else {
			rear++;
			elements[rear] = data;
		}
	}
	
	public Object dequeue() {
		if(isEmpty()) {
			System.out.println ("Queue is empty");
			 return null;
		}
		else {
			Object retData = elements[front];
			elements[front] = null;
			front++;
			return retData;
		}
	}
	
	public Object peek() {
		if(isFull()) {
			System.out.println ("Queue overflow");
			return null;
		}
		else {
			return elements[front];
		}
	}
	
	boolean isEmpty() {
	      return rear < front;
	}
	
	boolean isFull() {
	    return (rear + 1 == elements.length);
	}
	
	int size () {
	    return rear - front + 1;
	 }
}
