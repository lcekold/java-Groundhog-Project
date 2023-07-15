package com.briup.game;

import java.io.File;
import java.net.MalformedURLException;

public class MusicPlayTest {

	public static void main(String[] args) {
		try {
			//1.创建文件对象
			File file = new File("src/com/briup/game/打地鼠背景音乐.wav");
			//2.创建MusicPlay对象
			MusicPlay play = new MusicPlay(file.toURL());
			//3.一次背景音乐 开启
			play.start();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}
}
