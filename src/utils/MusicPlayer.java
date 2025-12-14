package utils;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class MusicPlayer {
    private Clip clip;

    public void playBackgroundMusic(String musicFile) {
        try {
            URL url = getClass().getResource("/resources/background.wav");
            if (url == null) throw new IllegalArgumentException("音频文件未找到: " + musicFile);

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // 无限循环
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("背景音乐播放失败: " + e.getMessage());
        }
    }
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}
