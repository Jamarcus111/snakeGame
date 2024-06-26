import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600; // 屏幕宽度
    static final int SCREEN_HEIGHT = 600; // 屏幕高度
    static final int UNIT_SIZE = 25; // 单位大小
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE; // 游戏单位数量
    static final int DELAY = 75; // 刷新间隔
    final int x[] = new int[GAME_UNITS]; // 蛇的x坐标数组
    final int y[] = new int[GAME_UNITS]; // 蛇的y坐标数组
    int bodyParts = 6; // 蛇的初始身体部分数量
    int applesEaten; // 吃掉的苹果数量
    int appleX; // 苹果的x坐标
    int appleY; // 苹果的y坐标
    char direction = 'R'; // 蛇的初始移动方向
    boolean running = false; // 游戏是否进行中
    Timer timer; // 计时器
    Random random; // 随机数生成器

    GamePanel(){
        random = new Random(); // 创建随机数生成器实例
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); // 设置面板尺寸
        this.setBackground(Color.black); // 设置背景颜色为黑色
        this.setFocusable(true); // 设置面板可获得焦点
        this.addKeyListener(new MyKeyAdapter()); // 添加键盘监听器
        startGame(); // 启动游戏
    }

    public void startGame(){
        newApple(); // 生成新苹果
        running = true; // 设置游戏进行中
        timer = new Timer(DELAY, this); // 创建计时器
        timer.start(); // 启动计时器
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g); // 调用父类的paintComponent方法
        draw(g); // 调用draw方法绘制游戏内容
    }

    public void draw(Graphics g){
        if(running) {
            for(int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++){
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); // 绘制网格线（竖线）
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE); // 绘制网格线（横线）
            }

            g.setColor(Color.red); // 设置颜色为红色
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); // 绘制苹果

            for(int i = 0; i < bodyParts; i++){
                if(i == 0){
                    g.setColor(Color.green); // 设置颜色为绿色（蛇头）
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); // 绘制蛇头
                }
                else{
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255))); // 设置随机颜色（蛇身）
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); // 绘制蛇身
                }
            }

            g.setColor(Color.red); // 设置颜色为红色
            g.setFont(new Font("Ink Free", Font.BOLD, 25)); // 设置字体
            FontMetrics metrics = getFontMetrics(g.getFont()); // 获取字体度量
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize()); // 绘制分数
        }
        else{
            gameOver(g); // 游戏结束时调用gameOver方法
        }
    }

    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE; // 生成新苹果的x坐标
        appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE; // 生成新苹果的y坐标
    }

    public void move(){
        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i - 1]; // 更新蛇身体部分的x坐标
            y[i] = y[i - 1]; // 更新蛇身体部分的y坐标
        }
        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE; // 向上移动
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE; // 向下移动
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE; // 向左移动
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE; // 向右移动
                break;
        }
    }

    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++; // 增加身体长度
            applesEaten++; // 增加吃掉的苹果数量
            newApple(); // 生成新苹果
        } 
    }

    public void checkCollisions(){
        // 检查蛇头是否撞到身体
        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false; // 停止游戏
            }
        }
        // 检查蛇头是否碰到边界
        if(x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT){
            running = false; // 停止游戏
        }
        if(!running){
            timer.stop(); // 停止计时器
        }
    }

    public void gameOver(Graphics g){
        // 显示游戏结束的文字
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
        // 显示分数
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       if(running) {
        move(); // 移动蛇
        checkApple(); // 检查是否吃到苹果
        checkCollisions(); // 检查是否发生碰撞
       }
       repaint(); // 重新绘制面板
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L'; // 向左移动
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R'; // 向右移动
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U'; // 向上移动
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D'; // 向下移动
                    }
                    break;
            }
        }
    }
}



