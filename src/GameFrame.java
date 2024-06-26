import javax.swing.JFrame;

public class GameFrame extends JFrame {

    GameFrame(){
         this.add(new GamePanel()); // 添加一个GamePanel到框架中
         this.setTitle("Snake"); // 设置窗口标题
         this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置默认关闭操作
         this.setResizable(false); // 设置窗口不可调整大小
         this.pack(); // 调整窗口大小以适应其内容
         this.setVisible(true); // 使窗口可见
         this.setLocationRelativeTo(null); // 将窗口居中显示
    }
}


