package src.code;

import java.io.File;

public class TimeSpace{
    long startTime;
    long endTime;
    long startBytes;
    long endBytes;

    // Guarda tempo e espaço inicial
    public void startTimer(String filepath)
    {
        startTime();
        startCount(filepath);
    }

    // Guarda tempo e espaço final.
    public void stopTimer(String filepath)
    {
        stopTime();
        stopCount(filepath);
    }

    // Imprime dados.
    public void printTimer()
    {
        MyIO.println("Tempo decorrido  -- " + (endTime - startTime)/1000000 + "ms");
        MyIO.println("Taxa de Compressao  -- "+ ((double) endBytes)/startBytes);
        MyIO.println("Fator de Compressao  -- "+ ((double) startBytes)/endBytes);
        MyIO.println("Ganho de Compressao  -- "+ (100 * (Math.log(startBytes)/endBytes)));
        MyIO.println("Percentual de Reducao  -- "+ (100* 1 - ((double) endBytes)/startBytes));
    }

    // Imrpime apenas o tempo
    public void printTime()
    {
        MyIO.println("Tempo decorrido  -- " + (endTime - startTime)/1000000 + "ms");
    }

    private void startTime() {startTime = System.nanoTime();}
    private void stopTime()  {endTime = System.nanoTime();}

    private void startCount(String filePath)
    {
        File file = new File (filePath);
        startBytes = file.length();
    }

    private void stopCount(String filePath)
    {
        File file = new File (filePath);
        endBytes = file.length();
    }
}