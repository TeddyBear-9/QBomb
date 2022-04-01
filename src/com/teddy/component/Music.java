package com.teddy.component;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Music {
    private File musicFile;
    private Clip clip;
    public Music(String location){
        musicFile = new File(location);
        if(!musicFile.exists()) System.out.println("location music error!");
    }
    //开启循环播放音乐
    public void playLoopMusic(){
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(musicFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    //关闭循环播放音乐
    public void stopLoopMusic(){
        clip.stop();
    }

    //播放单次音效
    public static void playSingleMusic(String location){
        File musicpath = new File(location);
        if(musicpath.exists()){
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicpath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }
}
