package Game.Logs;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.*;

public class LogManager {
    private static final BlockingDeque<String> queue = new LinkedBlockingDeque<>();
    private static ExecutorService Executor;
    private static BufferedWriter writer;
    private static boolean running;

    public static synchronized void startLogger(){
        if (running){
            return;
        }
        try{
            writer=new BufferedWriter(new FileWriter("logs.txt",true));
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
        String time= LocalDateTime.now().toString();
        queue.offer(time+" "+log);
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
        }
        catch (IOException e) {

        }
    }
}
