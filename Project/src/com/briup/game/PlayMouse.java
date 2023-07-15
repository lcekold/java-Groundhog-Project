package com.briup.game;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * 打地鼠 
 *  1.布局
 *  2.功能
 * @author niurui
 *
 */
public class PlayMouse implements ActionListener{
	//构建容器
	private JFrame jFrame;
	//构建面板 northPanel centerPanel
	private JPanel northPanel,centerPanel;
	//构建六个标签，分别用来显示等级、时间、分数、音效
	private JLabel levelLabel,timeLabel,timeVal,centLabel,centVal,musicLabel;
	//构建下拉框，分别用来显示等级、音效
	private JComboBox<String> box,musicBox;
	//构建开始按钮
	private JButton startBtn;
	//构建按钮组，保存十六个按钮
	private JButton[] btns;
	//构建时间定时器、老鼠定时器
	private Timer timer,mouseTimer;
	//构建老鼠、老鼠洞图片
	private ImageIcon holeImg,mouseImg,mouse1,mouse2,hit;
	//记录上一次老鼠出现的位置
	private int lastIndex;
	//设置游戏等级  
	//初级：900  中级：600  高级：100
	private int level;
	//记录老鼠是否加过分 
	//false:新老鼠(否)  true:旧老鼠（是）
	private boolean flag;
	//鼠标形状
	private Cursor cursor1,cursor2;
	//文件对象
	private File file;
	//背景音乐对象
	private MusicPlay play;
	//老鼠被打音乐对象
	private MusicPlay hitmusic;
	
	
	//初始化JFrame
	public PlayMouse() {
		//1.创建容器 
		jFrame = new JFrame("打地鼠");
		//2.创建面板
		northPanel = new JPanel();
		centerPanel = new JPanel();
		//3.设置布局管理器
		//JFrame默认边界布局管理器
		//JPanel默认流式布局管理器
		//northPanel.setLayout(new FlowLayout());
		centerPanel.setLayout(new GridLayout(4,4));
		//4.创建组件
		levelLabel  = new JLabel("等级：");
		box = new JComboBox<String>(new String[] {"初级","中级","高级"});
		//等级选择框绑定点击事件
		box.addActionListener(this);
		timeLabel = new JLabel("time：");
		timeVal = new JLabel("10");
		centLabel = new JLabel("cent：");
		centVal = new JLabel("0");
		musicLabel = new JLabel("音效：");
		musicBox = new JComboBox<String>(new String[] {"开","关"});
		//音效选择框绑定点击事件
		musicBox.addActionListener(this);
		startBtn = new JButton("开始");
		
		
		
		
		mouse1 = new ImageIcon("src/com/briup/game/mouse1.png");
	    mouse2 = new ImageIcon("src/com/briup/game/mouse2.png");
	    hit = new ImageIcon("src/com/briup/game/hit.png");
	    
	    
	    
		holeImg = new ImageIcon("src/com/briup/game/hole.png");
		mouseImg = new ImageIcon("src/com/briup/game/mouse.png");
		cursor1 = createMyCursor("src/com/briup/game/hammer1.png");
		cursor2 = createMyCursor("src/com/briup/game/hammer2.png");
		//点击开始按钮，游戏开始
		startBtn.addActionListener(this);
		btns = new JButton[16];
		for(int i=0;i<btns.length;i++) {
			btns[i] = new JButton();
			//禁用十六个按钮
			btns[i].setEnabled(false);
			//给十六个按钮绑定点击事件
			btns[i].addActionListener(this);
			//给按钮绑定鼠标按下松开事件
			btns[i].addMouseListener(new MyMouseListener(btns[i]));
			//添加组件到面板中
			centerPanel.add(btns[i]);
		}
		//创建时间定时器
		timer = new Timer(1000,this);
		level = 900;
		//创建老鼠定时器
		mouseTimer = new Timer(level,this);
		//5.添加组件到面板中
		northPanel.add(levelLabel);
		northPanel.add(box);
		northPanel.add(timeLabel);
		northPanel.add(timeVal);
		northPanel.add(centLabel);
		northPanel.add(centVal);
		northPanel.add(musicLabel);
		northPanel.add(musicBox);
		northPanel.add(startBtn);
		//6.添加面板到容器中
		jFrame.add(northPanel, BorderLayout.NORTH);
		jFrame.add(centerPanel, BorderLayout.CENTER);
		//7.设置容器宽高
		jFrame.setSize(500, 600);
		//8.设置容器窗口大小不可变
		jFrame.setResizable(false);
		//9.设置容器居中
		jFrame.setLocationRelativeTo(null);
		//10.设置容器可见性
		jFrame.setVisible(true);	
		//11.设置关闭窗口
		jFrame.setDefaultCloseOperation(3);
		//12.设置缩放图片，图片大小适配按钮大小
		mouse1.setImage(mouse1.getImage().getScaledInstance(btns[0].getWidth(), btns[0].getHeight(), Image.SCALE_DEFAULT));
		mouse2.setImage(mouse2.getImage().getScaledInstance(btns[0].getWidth(), btns[0].getHeight(), Image.SCALE_DEFAULT));
		holeImg.setImage(holeImg.getImage().getScaledInstance(btns[0].getWidth(), btns[0].getHeight(), Image.SCALE_DEFAULT));
		//十六个按钮设置背景图片为老鼠洞
		for(int i=0;i<btns.length;i++) {
			btns[i].setIcon(holeImg);
		}
		try {
			//13.开启背景音乐
			//创建文件对象
			file = new File("src/com/briup/game/打地鼠背景音乐.wav");
			//创建背景音乐对象
			play = new MusicPlay(file.toURL());
			//开启背景音乐
			play.start();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	
	//开启或禁用十六按钮
	public void setBtnsEnable(boolean b) {
		for(int i=0;i<btns.length;i++) {
			btns[i].setEnabled(b);
		}
	}
	
	
	//创建鼠标的形状
	private Cursor createMyCursor(String imagePath) {
		Image myCursor = new ImageIcon(imagePath).getImage();
		//参数一：自定义鼠标样式对应的图片对象
		//参数二：对应的位置坐标
		//参数三：字符串，给自定义鼠标样式起个名字
		return Toolkit.getDefaultToolkit().createCustomCursor(myCursor, new Point(0,0), null);
	}
	
	//鼠标按下和松开形状
	//类--》类-----》内部类(匿名内部类 实例内部类 局部内部类 静态内部类)
	private class MyMouseListener extends MouseAdapter{
		private JButton btn;
		
		public MyMouseListener(JButton btn) {
			this.btn = btn;
		}
		
		//鼠标按下
		@Override
		public void mousePressed(MouseEvent e) {
			btn.setCursor(cursor2);
		}
		
		//鼠标松开
		@Override
		public void mouseReleased(MouseEvent e) {
			btn.setCursor(cursor1);
		}
		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//获取事件源
		Object obj = e.getSource();
		//点击开始按钮，游戏开始
		if(obj == startBtn) {
			//音效下拉框默认选中开
			musicBox.setSelectedIndex(0);
			//随机产生一只老鼠
			Random random = new Random();
			int index = random.nextInt(btns.length);
			flag = false;
			//记录第一只老鼠出现的位置
			lastIndex = index;
			btns[index].setIcon(mouse1);
			//开启时间定时器
			timer.start();
			//开启老鼠定时器
			mouseTimer.start();
			//开启十六个按钮
			setBtnsEnable(true);
			//禁用等级下拉框
			box.setEnabled(false);
			//禁用开始按钮
			startBtn.setEnabled(false);
			//重置时间，分数
			timeVal.setText("10");
			centVal.setText("0");
		}else if(obj == timer) {
			//开启时间定时器后执行
			//获取时间文本值 String->int
			int num = Integer.parseInt(timeVal.getText());
			//如果时间>0
			if(num > 0 ) {
				//数值-1
				num--;
				//重新设置时间
				timeVal.setText(num+"");
			}else {
				//游戏结束
				//清除掉最后一只老鼠，将老鼠图片设置为老鼠洞
				btns[lastIndex].setIcon(holeImg);
				//关闭时间定时器
				timer.stop();
				//关闭老鼠定时器
				mouseTimer.stop();
				//开启开始按钮
				startBtn.setEnabled(true);
				//开启等级下拉框
				box.setEnabled(true);
				//禁用十六个按钮
				setBtnsEnable(false);
				//关闭背景音乐
				play.stop();
			}
		}else if(obj == mouseTimer) {
			//开启老鼠定时器执行
			//将上一次老鼠出现的位置设置为老鼠洞
			btns[lastIndex].setIcon(holeImg);
			//每隔1s随机产生一只老鼠
			Random random = new Random();
			int index = random.nextInt(btns.length);
			lastIndex = index;
			flag = false;
			//将按钮的背景图设置为老鼠
			
			
			
			
			btns[index].setIcon(mouse1);
			// 设置延时，让mouse1图片显示一段时间后再显示mouse2图片
		    Timer delayTimer = new Timer(100, new ActionListener() {
		        @Override
		        public void actionPerformed(ActionEvent e) {
		            btns[index].setIcon(mouse2);
		        }
		    });
		    delayTimer.setRepeats(false);
		    delayTimer.start();
		    
		    
		    
		}else if(obj == box) {
			//获取下拉框中选择的文本值
			String item = (String) box.getSelectedItem();
			//判断哪个等级
			if("高级".equals(item)) {
				level = 300;
			}else if("中级".equals(item)) {
				level = 600;
			}else if("初级".equals(item)) {
				level = 900;
			}
			//重新创建老鼠定时器
			mouseTimer = new Timer(level, this);
		}
		else if(obj == musicBox) {
			//获取下拉框中选择的文本值
			String choose = (String)musicBox.getSelectedItem();
			if("关".equals(choose)) {
				play.stop();
			}else if("开".equals(choose)) {
				play.start();
			}
		}
		else {
			//点击十六个按钮
			for(int i=0;i<btns.length;i++) {
				//判断是否点击老鼠的按钮并且该老鼠没加过分(新老鼠) flag=false
				if(obj == btns[i] && btns[i].getIcon() == mouse2&&btns[i].getIcon()!=hit && !flag) {
					try{
					file = new File("src/com/briup/game/地鼠被打.wav");
					//创建背景音乐对象
					hitmusic = new MusicPlay(file.toURL());
					//开启背景音乐
					hitmusic.start();
					}catch (MalformedURLException a) {
						a.printStackTrace();
					}
					btns[i].setIcon(hit);
		           
					//获取分数文本值 String->int
					int cent = Integer.parseInt(centVal.getText());
					//分值+1
					cent++;
					flag = true;
					//设置分数值
					centVal.setText(cent+"");
				}
			}
		}
		
	}
	
	
	public static void main(String[] args) {
		new PlayMouse();
	}

}
