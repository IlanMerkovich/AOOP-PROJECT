package Game.Logs;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

public class LogManager {
    private static final BlockingDeque<String> queue = new LinkedBlockingDeque<>();
    private static ExecutorService Executor;
    private static BufferedWriter writer;
    private static boolean running;
    private static final String LOG_FILE = "logs.txt";
    private static final String SCORE_FILE = "player_score";

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
        try {
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Error");
        }
        Executor.shutdownNow();
    }
    public static void addLog(String log){
        if (!running){
            return;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String time=LocalDateTime.now().format(dateTimeFormatter);
        queue.offer("["+time+"] "+log);
    }
    private static void manageWriting(){
        try{
            while (running || !queue.isEmpty()){
                String newLog = queue.take();
                if (newLog!=null){
                    writer.write(newLog);
                    writer.newLine();
                    writer.flush();
                }
            }
        }
        catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        catch (IOException e){
            System.out.println("IO error");
        }
    }
    public static void writeScore(String score){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String time=LocalDateTime.now().format(dateTimeFormatter);

        try {
            FileWriter scoreFile = new FileWriter(SCORE_FILE, true);
            scoreFile.write("["+time+"] "+score+"\n");
            scoreFile.close();
        }
        catch (IOException e) {
            System.err.println("Could not open scores file!");
        }
    }
    public static String getLogs(){
        try {
            if (!Files.exists(Paths.get(LOG_FILE))) {
                return "No logs found - file doesn't exist";
            }
            return String.join("\n", Files.readAllLines(Paths.get(LOG_FILE)));
        }
        catch (IOException e) {
            System.err.println("Error reading logs file: " + e.getMessage());
            return "Error reading logs: " + e.getMessage();
        }
    }
    public static String getPlayerScores() {
        try {
            if (!Files.exists(Paths.get(SCORE_FILE))) {
                return "No scores found - play some games first!";
            }
            return String.join("\n", Files.readAllLines(Paths.get(SCORE_FILE)));
        } catch (IOException e) {
            System.err.println("Error reading scores file: " + e.getMessage());
            return "Error reading scores: " + e.getMessage();
        }
    }
}
