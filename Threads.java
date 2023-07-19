import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
            numbers.add(i);
        }

        ArrayList<Integer> evenNumbers = new ArrayList<>();
        ArrayList<Integer> oddNumbers = new ArrayList<>();

        int chunkSize = numbers.size() / 4;
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 4; i++) {
            int startIndex = i * chunkSize;
            int endIndex = (i + 1) * chunkSize;
            ArrayList<Integer> subList = new ArrayList<>(numbers.subList(startIndex, endIndex));
            executorService.submit(new NumberProcessor(subList, evenNumbers, oddNumbers));
        }

        executorService.shutdown();

        while (!executorService.isTerminated()) {
            // Wait for all threads to finish
        }

        System.out.println("Even numbers: " + evenNumbers);
        System.out.println("Odd numbers: " + oddNumbers);
    }
}

class NumberProcessor implements Runnable {
    private final ArrayList<Integer> subList;
    private final ArrayList<Integer> evenNumbers;
    private final ArrayList<Integer> oddNumbers;

    public NumberProcessor(ArrayList<Integer> subList, ArrayList<Integer> evenNumbers, ArrayList<Integer> oddNumbers) {
        this.subList = subList;
        this.evenNumbers = evenNumbers;
        this.oddNumbers = oddNumbers;
    }

    @Override
    public void run() {
        for (int num : subList) {
            if (num % 2 == 0) {
                synchronized (evenNumbers) {
                    evenNumbers.add(num);
                }
            } else {
                synchronized (oddNumbers) {
                    oddNumbers.add(num);
                }
            }
        }
    }
}
