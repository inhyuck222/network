package thread;

public class MultiThreadEx {

	public static void main(String[] args) {
		Thread thread1 = new AlphabetThread();
		Thread thread2 = new Thread(new DigitThread());

		thread1.start();
		thread2.start();
	}

}
