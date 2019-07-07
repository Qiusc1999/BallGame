package BallGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class BallFrame extends JFrame {
    private JPanel ballPanel,buttonpanel,titlePanel,scorePanel;
    private Graphics g;
    private JButton stop,start,cusbutton,a,b,c,d,e,f,exit;
    private JRadioButton easy,middle,difficult;
    private ButtonGroup levelGroup;
    private int level=5;
    public JLabel scorelable,ballnum,colorlable,title,title1,title2,title3,label,label1,label2;//lable填充空白
    private float Sx,Sy,Ex,Ey;
    private float vx,vy;
    private ArrayList<BallThread> ballList=new ArrayList<BallThread>();
    private Color color;
    private int selectColor;
    private Random random =new Random();
    private Score score=new Score(0);
    private boolean customize=false;

    public void setScorelable(JLabel scorelable) {
        this.scorelable = scorelable;
    }




    public BallFrame(){
        super("BallGame");

        scorelable =new JLabel();
        ballnum=new JLabel();
        colorlable=new JLabel();
        title=new JLabel();
        title1=new JLabel();
        title2=new JLabel();
        title3=new JLabel();
        label=new JLabel();
        label1=new JLabel();
        label2=new JLabel();


        stop =new JButton("结束");
        start=new JButton("开始游戏");
        cusbutton=new JButton("自定义游戏");
        a=new JButton("红");
        b=new JButton("黄");
        c=new JButton("蓝");
        d=new JButton("绿");
        e=new JButton("粉");
        f=new JButton("灰");
        exit=new JButton("退出");

        easy=new JRadioButton("简单");
        middle=new JRadioButton("中等");
        difficult=new JRadioButton("困难");

        levelGroup =new ButtonGroup();
        levelGroup.add(easy);
        levelGroup.add(middle);
        levelGroup.add(difficult);

        ballPanel =new JPanel();
        buttonpanel=new JPanel();
        titlePanel=new JPanel();
        scorePanel=new JPanel();



        //panel设置
        Dimension bp=new Dimension(825,700);        //存储ballPanel的长宽
        Dimension bup=new Dimension(825,50);       //存储buttonpanel的长宽
        Dimension sp=new Dimension(825,50);

        titlePanel.setBackground(Color.BLACK);
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.add(title);
        titlePanel.add(title1);
        titlePanel.add(title2);
        titlePanel.add(title3);

        ballPanel.setPreferredSize(bp);
        ballPanel.setLayout(new GridLayout(5,1));
        ballPanel.setBackground(Color.BLACK);
        ballPanel.add(label);           //填充空白
        ballPanel.add(label1);           //填充空白
        ballPanel.add(titlePanel);
        ballPanel.add(label2);           //填充空白
        ballPanel.add(scorelable);

        buttonpanel.setPreferredSize(bup);
        buttonpanel.setBackground(Color.BLACK);
        buttonpanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonpanel.add(start);
        buttonpanel.add(cusbutton);
        buttonpanel.add(easy);
        easy.setSelected(true);
        buttonpanel.add(middle);
        buttonpanel.add(difficult);
        buttonpanel.add(exit);

        scorePanel.setBackground(Color.BLACK);
        scorePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        scorePanel.setPreferredSize(sp);
        scorePanel.add(scorelable);
        //panel设置

        //开始标题
        title.setFont(new Font("黑体",Font.BOLD,60));
        title.setForeground(Color.RED);
        title.setText("动");
        title1.setFont(new Font("黑体",Font.BOLD,60));
        title1.setForeground(Color.YELLOW);
        title1.setText("感");
        title2.setFont(new Font("黑体",Font.BOLD,60));
        title2.setForeground(Color.BLUE);
        title2.setText("弹");
        title3.setFont(new Font("黑体",Font.BOLD,60));
        title3.setForeground(Color.GREEN);
        title3.setText("球");

        BorderLayout borderLayout=new BorderLayout();

        //容器设置
        Container container=new Container();
        container=getContentPane();
        container.setLayout(borderLayout);
        container.add(ballPanel,borderLayout.NORTH);
        container.add(scorePanel,borderLayout.CENTER);
        container.add(buttonpanel,borderLayout.SOUTH);
        container.setBackground(Color.black);

        //鼠标监听器
        ballPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                for(int i=0;i<ballList.size();i++){
                    BallThread ball=ballList.get(i);
                    if(!ball.isAlive())         //检查并删除死亡进程
                        ballList.remove(i);
                }
                Sx=e.getX();
                Sy=e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Ex=e.getX();
                Ey=e.getY();
                boolean player = true;

                if(Sy>=570&&Ey>=570) {
                    player = true;
                }
                else {
                    player=false;
                }

                if((player==true&&customize==false)||(customize==true)){    //在非自定义游戏中，只能在发球区发球；在自定义游戏中，可以在任意地方发球
                    vx=(float)((Sx-Ex)/5.0);
                    vy=(float)((Sy-Ey)/5.0);
                    if(Math.sqrt(vx*vx+vy*vy)>=10) {
                        double n=Math.sqrt(vx*vx+vy*vy)/10;
                        vx=(float)((double)vx/n);
                        vy=(float)((double)vy/n);
                    }

                    if(vx==0&&vy==0||color==null){       //x,y方向速度均为零不生成
                        return;
                    }
                    else{
                        BallThread ball=new BallThread(e.getX(),e.getY(),vx,vy,color,player, ballPanel, scorelable,ballnum,g,ballList,score);
                        ballList.add(ball);
                        ball.start();
                        if(player==true)
                            System.out.println("(Sx,Sy)=("+Sx+","+Sy+") "+" (Ex,Ey)=("+Ex+","+Ey+") vx="+vx+" vy="+vy+" player:true");
                        else
                            System.out.println("(Sx,Sy)=("+Sx+","+Sy+") "+" (Ex,Ey)=("+Ex+","+Ey+") vx="+vx+" vy="+vy+" player:false");

                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        //鼠标监听器

        //添加监听器
        ActionEventHandler handler=new ActionEventHandler();
        RadioButtonHandler radioButtonHandler=new RadioButtonHandler();
        stop.addActionListener(handler);
        start.addActionListener(handler);
        cusbutton.addActionListener(handler);
        a.addActionListener(handler);
        b.addActionListener(handler);
        c.addActionListener(handler);
        d.addActionListener(handler);
        e.addActionListener(handler);
        f.addActionListener(handler);
        exit.addActionListener(handler);
        easy.addItemListener(radioButtonHandler);
        middle.addItemListener(radioButtonHandler);
        difficult.addItemListener(radioButtonHandler);
        //添加监听器

        setSize(850,830);
        setVisible(true);

    }

    //距离计算
    public float distance(float locx,float locy,float locx1,float locy1){     //locx当前球,locx1其他球
        float d=(float)Math.sqrt((locx1-locx)*(locx1-locx)+(locy1-locy)*(locy1-locy));
        return d;
    }

    //单选按钮监听器
    private class RadioButtonHandler implements ItemListener {
        public void itemStateChanged(ItemEvent event){
            if(event.getItem()==easy)
                level=5;
            else if(event.getItem()==middle)
                level=10;
            else if (event.getItem()==difficult)
                level=15;
        }
    }

    //按钮监听器
    public class ActionEventHandler implements ActionListener{
        public void actionPerformed(ActionEvent event){
            if(event.getSource()== stop){
                //覆盖之前按钮残余，显示新按钮,重绘所有界面
                scorelable.setText("");
                ballnum.setText("");
                buttonpanel.removeAll();
                buttonpanel.repaint();
                buttonpanel.add(start);
                buttonpanel.add(cusbutton);
                buttonpanel.add(easy);
                buttonpanel.add(middle);
                buttonpanel.add(difficult);
                buttonpanel.add(exit);
                ballPanel.repaint();

                //重绘title
                title.setFont(new Font("黑体",Font.BOLD,60));
                title.setForeground(Color.RED);
                title.setText("动");
                title1.setFont(new Font("黑体",Font.BOLD,60));
                title1.setForeground(Color.YELLOW);
                title1.setText("感");
                title2.setFont(new Font("黑体",Font.BOLD,60));
                title2.setForeground(Color.BLUE);
                title2.setText("弹");
                title3.setFont(new Font("黑体",Font.BOLD,60));
                title3.setForeground(Color.GREEN);
                title3.setText("球");

                for(int i=0;i<ballList.size();i++){     //ballList.size()逐步减小
                    ballList.get(i).setP(false);
                }
                customize=false;
            }
            if(event.getSource()==start){
                customize=false;
                //覆盖之前按钮残余，显示新按钮
                buttonpanel.removeAll();
                buttonpanel.repaint();
                buttonpanel.add(colorlable);
                buttonpanel.add(a);
                buttonpanel.add(b);
                buttonpanel.add(c);
                buttonpanel.add(d);
                buttonpanel.add(e);
                buttonpanel.add(f);
                buttonpanel.add(stop);
                buttonpanel.add(ballnum);


                score.setScore(0);
                title.setText("");
                title1.setText("");
                title2.setText("");
                title3.setText("");

                for(int i=0;i<level;i++){
                    boolean can=false;
                    float vx=0,vy=0,x=0,y=0;
                    int c= random.nextInt(6);
                    Color setitcolor=Color.RED;
                    switch (c){
                        case 0:setitcolor=Color.RED; break;
                        case 1:setitcolor=Color.YELLOW; break;
                        case 2:setitcolor=Color.BLUE; break;
                        case 3:setitcolor=Color.GREEN; break;
                        case 4:setitcolor=Color.PINK; break;
                        case 5:setitcolor=Color.GRAY; break;
                    }
                    if(i==0){                           //第一个球，防止ArrayList为空
                        vx=random.nextInt(6)-3;
                        vy=random.nextInt(6)-3;
                        x=random.nextInt(700)+60;
                        y=random.nextInt(300)+60;
                        BallThread ball=new BallThread(x,y,vx,vy,setitcolor,false, ballPanel, scorelable,ballnum,g,ballList,score);
                        ballList.add(ball);
                        ball.start();
                    }
                    else{
                        while (can==false){
                            can=true;
                            vx=random.nextInt(16)-8;
                            vy=random.nextInt(16)-8;
                            x=random.nextInt(700)+60;
                            y=random.nextInt(300)+60;
                            for(int j=0;j<ballList.size();j++){
                                BallThread ball=ballList.get(j);
                                if(distance(x,y,ball.getLocx()+ball.getVx(),ball.getLocy()+ball.getVy())<70){
                                    can=false;
                                    break;
                                }
                            }
                        }
                        BallThread ball=new BallThread(x,y,vx,vy,setitcolor,false, ballPanel, scorelable,ballnum,g,ballList,score);
                        ballList.add(ball);
                        ball.start();
                    }
                }

            }
            if(event.getSource()==cusbutton){
                customize=true;

                //覆盖之前按钮残余，显示新按钮
                buttonpanel.removeAll();
                buttonpanel.repaint();
                buttonpanel.add(colorlable);
                buttonpanel.add(a);
                buttonpanel.add(b);
                buttonpanel.add(c);
                buttonpanel.add(d);
                buttonpanel.add(e);
                buttonpanel.add(f);
                buttonpanel.add(stop);
                buttonpanel.add(ballnum);
                ballnum.setText("");

                score.setScore(0);
                title.setText("");
                title1.setText("");
                title2.setText("");
                title3.setText("");
                scorelable.setForeground(Color.WHITE);
                scorelable.setFont(new Font("黑体",Font.BOLD,28));
                scorelable.setText("自定义游戏，请任意发射小球");
            }
            if(event.getSource()==a){
                color=Color.red;
                colorlable.setFont(new Font("黑体",Font.BOLD,20));
                colorlable.setForeground(color);
                colorlable.setText("已选择红色");
            }

            if(event.getSource()==b){
                color=Color.yellow;
                colorlable.setFont(new Font("黑体",Font.BOLD,20));
                colorlable.setForeground(color);
                colorlable.setText("已选择黄色");
            }

            if(event.getSource()==c){
                color=Color.blue;
                colorlable.setFont(new Font("黑体",Font.BOLD,20));
                colorlable.setForeground(color);
                colorlable.setText("已选择蓝色");
            }

            if(event.getSource()==d){
                color=Color.green;
                colorlable.setFont(new Font("黑体",Font.BOLD,20));
                colorlable.setForeground(color);
                colorlable.setText("已选择绿色");
            }

            if(event.getSource()==e){
                color=Color.PINK;
                colorlable.setFont(new Font("黑体",Font.BOLD,20));
                colorlable.setForeground(color);
                colorlable.setText("已选择粉色");
            }
            if(event.getSource()==f){
                color=Color.GRAY;
                colorlable.setFont(new Font("黑体",Font.BOLD,20));
                colorlable.setForeground(color);
                colorlable.setText("已选择灰色");
            }
            if(event.getSource()==exit)
                System.exit(0);
        }
    }

    public static void main(String args[]){
        BallFrame application = new BallFrame();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}



