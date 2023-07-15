package com.briup.game;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 开始游戏界面
 * @author niurui
 *
 */
public class StartGame implements ActionListener{
	//构建容器
	private JFrame jFrame;
	//构建面板
	private JPanel jPanel;
	//构建按钮
	private JButton btn;
	//构建文件对象
	private File file;
	//构建背景音乐对象
	private MusicPlay play;
	//构建背景图片
	private ImageIcon startImg;
	
	//构造器 初始化jFrame
	public StartGame() {
		//1.创建容器
		jFrame = new JFrame("开始界面");
		//2.创建面板  默认流式布局管理器
		jPanel = new JPanel();
		//3.面板设置布局管理器 网格布局管理器
		jPanel.setLayout(new GridLayout(1, 1));
		//4.创建组件
		btn = new JButton();
		btn.addActionListener(this);
		//5.添加组件到面板中
		jPanel.add(btn);
		//6.添加面板到容器中
		jFrame.add(jPanel);
		//7.设置容器大小
		jFrame.setSize(500, 600);
		//8.设置容器居中
		jFrame.setLocationRelativeTo(null);
		//9.设置可见性
		jFrame.setVisible(true);
		//11.设置窗口不可拖动
		jFrame.setResizable(false);
		//12.设置关闭窗口
		jFrame.setDefaultCloseOperation(3);
		//13.设置背景图片
		startImg = new ImageIcon("src/com/briup/game/start.jpg");
		startImg.setImage(startImg.getImage().getScaledInstance(btn.getWidth(), btn.getHeight(), Image.SCALE_DEFAULT));
		btn.setIcon(startImg);
		//14.开启背景音乐
		try {
			file = new File("src/com/briup/game/开始游戏背景音乐.wav");
			play = new MusicPlay(file.toURL());
			play.start();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//关闭背景音乐
		play.stop();
		//关闭当前窗口
		jFrame.dispose();
		//开启游戏窗口
		new PlayMouse();
	}
	
	public static void main(String[] args) {
		new StartGame();
	}

	
	
}
