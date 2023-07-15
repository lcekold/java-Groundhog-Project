package com.briup.game;

import java.io.IOException;
import java.net.URL;

import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

/**
 * 背景音乐
 *   IO流：
 *     类型：字节流  字符流
 *     流向：输入流  输出流
 *   异常：
 *     Error:错误 
 *     Exception:异常
 *        -编译时异常 
 *        -运行时异常 
 * 
 * @author niurui
 *
 */
public class MusicPlay {
	//单次播放声音
	private AudioStream as;
	//循环播放声音
	private ContinuousAudioDataStream cas;
	
	//构造器
	public MusicPlay(URL url) {
		//打开一个声音文件流作为输入
		try {
			as = new AudioStream(url.openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//一次播放 开始
	public void start() {
		if(as == null) {
			System.out.println("AudioStream object is not created");
			return;
		}else {
			AudioPlayer.player.start(as);
		}
	}
	
	//一次播放 停止
	public void stop() {
		if(as == null) {
			System.out.println("AudioStream object is not created");
			return;
		}else {
			AudioPlayer.player.stop(as);
		}
	}
	
	//循环播放 开始
	public void continueStart() {
		AudioData data = null;
		try {
			 data = as.getData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		cas = new ContinuousAudioDataStream(data);
		AudioPlayer.player.start(cas);
	}
	
	//循环播放 停止
	public void continueStop() {
		if(cas == null) {
			System.out.println("ContinuousAudioDataStream object is not created");
			return;
		}else {
			AudioPlayer.player.stop(cas);
		}
	}
}
