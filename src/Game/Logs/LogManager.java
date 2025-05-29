package Game.Logs;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

public class LogManager {
    private static final BlockingDeque<String> queue = new LinkedBlockingDeque<>();
    private static ExecutorService Executor;
    private static BufferedWriter writer;
    private static boolean running;
    private static final String LOG_FILE = "logs.txt";

    public static synchronized void startLogger(){
        if (running){
            return;
        }
        try{
            writer=new BufferedWriter(new FileWriter(LOG_FILE,true));
        }
        catch (IOException e) {
            System.err.println("No file found");
        }
        Executor= Executors.newSingleThreadExecutor(e->{
            Thread writingThread = new Thread(e,"loger");
            writingThread.setDaemon(true);
            return writingThread;
        });
        running=true;
        Executor.submit(LogManager::manageWriting);
    }
    public static synchronized void stop() {
        if (!running)
            return;
        running = false;
        Executor.shutdownNow();
    }
    public static void addLog(String log){
        if (!running){
            return;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String time=LocalDateTime.now().format(dateTimeFormatter);
        queue.offer("["+time+"]"+log);
    }
    private static void manageWriting(){
        try{
            while (running || !queue.isEmpty()){
                String newLog=queue.take();
                writer.write(newLog);
                writer.newLine();
                writer.flush();
            }
        }
        catch (InterruptedException e) {
            System.err.println("error");
        }
        catch (IOException e) {
            System.out.println("IO error");
        }
    }
}
