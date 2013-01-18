import java.util.LinkedList;


public class MultiThread {
	
	public MultiThread(){
		new ThreadOne().start();
		new ThreadTwo().start();
		new ThreadThree().start();
		for(int i = 0; i < 5; i++){
			
				System.out.println("The main thread!");

			
		}
	}

	class ThreadOne extends Thread implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			for(int i = 0; i < 10; i++){
				try {
					System.out.println("Thread one!");
					Thread.sleep(500);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	class ThreadTwo extends Thread implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			for(int i = 0; i < 5; i++){
				try {
					System.out.println("Thread two!");
					Thread.sleep(1000);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	class ThreadThree extends Thread implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			for(int i = 0; i < 40; i++){
				try {
					System.out.println("Thread three!");
					Thread.sleep(2);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static void main(String[] args){
		//new MultiThread();
		LinkedList<Integer> a = new LinkedList<Integer>();
		a.add(1);
		a.add(2);
		a.add(3);
		a.add(4);
		System.out.println("before size: " + a.size() + "   " + a.pollLast() + " size: " + a.size());
	}
	
}
