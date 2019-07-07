package BallGame;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import javax.swing.*;

public class BallThread extends Thread {
    private int sleeptime=17;
    private float locx,locy,vx,vy;
    private float radius=25;
    private JPanel gamepanel;
    public JLabel scorelable;
    public JLabel ballnum;
    private Graphics2D g;
    private ArrayList<BallThread> ballList;
    private Color color=Color.yellow;
    boolean p=true;   //判断是否继续线程
    boolean player;   //玩家发出的球为true
    Score score;

    public BallThread(float locx, float locy, float vx, float vy, Color color,
                      boolean player, JPanel panel,JLabel scorelable,JLabel ballnum, Graphics g,
                      ArrayList<BallThread> ballList,Score score){
        gamepanel=panel;
        g=gamepanel.getGraphics();
        this.scorelable =scorelable;
        this.ballnum=ballnum;
        this.locx=locx;
        this.locy=locy;
        this.vx=vx;
        this.vy=vy;
        this.color=color;
        this.g=(Graphics2D) g;
        this.ballList=ballList;
        this.player=player;
        this.score=score;
    }

    public float getLocx() {
        return locx;
    }

    public float getLocy() {
        return locy;
    }

    public float getVx(){
        return vx;
    }

    public float getVy() {
        return vy;
    }

    public Color getColor() {
        return color;
    }

    public boolean isPlayer() {
        return player;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public void setPlayer(boolean player) {
        this.player = player;
    }

    public void setP(boolean p) {
        this.p = p;
    }

    public void rePaintIt(float x, float y, Color color){
        g.setColor(color);
        g.fill(new Ellipse2D.Float(x,y,2*radius,2*radius));
        if(color!=Color.BLACK){
            g.drawLine(0,570,840,570);
            g.drawLine(0,572,840,572);
            g.setFont(new Font("黑体",Font.BOLD,28));
            g.drawString("发球区",20,600);
            scorelable.setFont(new Font("黑体",Font.BOLD,28));
            scorelable.setForeground(color);
            scorelable.setText("当前分数："+score.getScore());
            ballnum.setFont(new Font("黑体",Font.BOLD,20));
            ballnum.setForeground(color);
            ballnum.setText("当前球数："+ballList.size());
        }
    }

    public int distance(float locx,float locy,float locx1,float locy1){     //locx当前球,locx1其他球
        int d=(int)Math.sqrt((locx1-locx)*(locx1-locx)+(locy1-locy)*(locy1-locy));
        return d;
    }

    public void run() {
        //P判断进程是否继续
        while(p==true){
            int thisNum=0;
            for(int i=0;i<ballList.size();i++){
                if(this==ballList.get(i))       //在ArrayList中查询当前进程的位置
                {
                    thisNum=i;
                }
            }

            for(int i=0;i<5;i++){
                boolean thereisFalse=false;                //判断每种颜色是否还有系统发球
                Color color=Color.RED;
                switch (i){
                    case 0:color=Color.RED; break;
                    case 1:color=Color.YELLOW; break;
                    case 2:color=Color.BLUE; break;
                    case 3:color=Color.GREEN; break;
                    case 4:color=Color.PINK; break;
                    case 5:color=Color.GRAY; break;
                }
                for(int j=0;j<ballList.size();j++){
                    if(ballList.get(j).isPlayer()==false&&ballList.get(j).getColor()==color){  //如果有由系统生成的，那么true
                        thereisFalse=true;
                    }
                }

                if(thereisFalse==false){                     //如果只剩玩家发射的球，将其变为false
                    for(int j=0;j<ballList.size();j++){
                        BallThread ball=ballList.get(j);
                        if(ball.getColor()==color){
                            ball.setPlayer(false);
                            ballList.set(j,ball);
                        }
                    }
                }

            }

            rePaintIt(locx,locy,Color.BLACK);   //用黑色覆盖之前轨迹

            locx+=vx;
            locy+=vy;

            for(int i=thisNum+1;i<ballList.size();i++){     //读取其他小球
                BallThread ball=ballList.get(i);
                float locx1= ball.getLocx();        //其他小球位置
                float locy1= ball.getLocy();
                float vx1=ball.getVx();             //其他小球速度
                float vy1=ball.getVy();
                //判断下一帧小球是否会碰撞，避免小球粘连问题
                int d=distance(locx+vx,locy+vy,locx1+vx1,locy1+vy1);
                if(d<=radius*2)
                {
                    if(this.color==ball.getColor()&&this.isPlayer()!=ball.isPlayer()){  //碰撞消除
                        score.addScore(10);     //加10分
                        this.setP(false);
                        ball.setP(false);
                    }
                    else {
                        if(this.color!=ball.getColor()&&this.isPlayer()!=ball.isPlayer())   //碰撞错误，变为false
                        {
                            score.addScore(-10);     //减10分
                            this.player=false;
                            ball.setPlayer(false);  //将玩家发射的球改为系统发射的球以防止玩家下次发球不会消除该球

                        }
                        float tempvx,tempvy;
                        tempvx=this.vx;
                        tempvy=this.vy;
                        this.vx=ball.getVx();
                        this.vy=ball.getVy();
                        ball.setVx(tempvx);
                        ball.setVy(tempvy);
                    }
                }
            }
            //检测到达边界反弹
            if(locx>=800-radius&&vx>0){
                vx=-vx;
            }
            if(locx<=7&&vx<0){
                vx=-vx;
            }
            if(locy<=7&&vy<0){   //y以下为正
                vy=-vy;
            }

            if(locy>=550-radius-7&&vy>0){       //发球区球只能向上发射
                vy=-vy;
            }
            rePaintIt(locx,locy,color);
            try {
                Thread.sleep(sleeptime);
            }
            catch (InterruptedException e){}
        }

        rePaintIt(locx,locy,Color.BLACK);
        ballList.remove(this);
        if(ballList.size()==0){
            scorelable.setFont(new Font("黑体",Font.BOLD,28));
            scorelable.setForeground(color);
            ballnum.setText("");
            scorelable.setText("游戏结束！分数："+score.getScore());
        }
    }
}



