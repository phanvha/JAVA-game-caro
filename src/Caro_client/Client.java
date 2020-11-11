package Caro_client;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Client {

    public static JFrame f;
    JButton[][] bt;
    static boolean flat = false;
    boolean winner;
    
    JTextArea content, info;
    JTextField nhap,enterchat;
    JButton send;
    Timer thoigian;
    Integer second, minute;
    JLabel demthoigian, thongtintrandau;
    TextField textField;
    JScrollPane scrll;
    JPanel p;
    String temp = "", winn="";
    String strNhan = "";
    int xx, yy, x, y, count = 0;
    int[][] matran;
    int[][] matrandanh;
    
    // Server Socket
    ServerSocket serversocket;
    Socket socket;
    OutputStream os;// ....
    InputStream is;// ......
    ObjectOutputStream oos;// .........
    ObjectInputStream ois;// 
    String time;
    
    
    //MenuBar
    MenuBar menubar;
    
    public Client() {
        f = new JFrame();
        f.setTitle("Game Caro Client");
        f.setSize(750, 500);
        f.setBackground(Color.orange);
        x = 25;
        y = 25;
        f.getContentPane().setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //f.setVisible(true);
        f.setResizable(false);
        
        matran = new int[x][y];
        matrandanh = new int[x][y];
        menubar = new MenuBar();
        p = new JPanel();
        p.setBounds(10, 30, 400, 400);
        p.setLayout(new GridLayout(x, y));
        //p.setBackground(Color.orange);
        f.add(p);
        // tao menubar cho frame
        f.setMenuBar(menubar);
        Menu game = new Menu("Game");
        menubar.add(game);
        Menu help = new Menu("Help");
        menubar.add(help);
        MenuItem helpItem = new MenuItem("Help");
        help.add(helpItem);
        MenuItem about = new MenuItem("About ..");
        help.add(about);
        help.addSeparator();
        MenuItem newItem = new MenuItem("New Game");
        game.add(newItem);
        MenuItem exit = new MenuItem("Exit");
        game.add(exit);
        game.addSeparator();
      
        newItem.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                        newgame();
                        try {
                                oos.writeObject("newgame");
                        } catch (IOException ie) {
                                //
                        }
                }

        });
        exit.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                }
        });
        about.addActionListener(new ActionListener()
        {
            @Override
                public void actionPerformed(ActionEvent e) {
                    //Object[] options = {"OK"};
                 JOptionPane.showConfirmDialog(f,"Phan văn hà - lập trình game caro TCP/IP", "Information",
                                JOptionPane.CLOSED_OPTION); 
                }
        });
        help.addActionListener(new ActionListener()
        {
            @Override
                public void actionPerformed(ActionEvent e) {
                    //Object[] options = {"OK"};
                 JOptionPane.showConfirmDialog(f,
                                "Mỗi người được đánh một lượt.\n"
                                        + "Theo hàng ngang và theo đường chéo, nếu như đủ 4  sẽ dành chiến thắng.\n"
                                		+"Bạn chỉ có thời gian là 10 giây để suy nghĩ và click, nếu quá 10 giây, bạn sẽ thua!\n"
                                        +"Hãy cố gắng dành chiến thắng!!","Luật chơi",JOptionPane.CLOSED_OPTION); 
                }
        });
        
      //khung info trận đấu
        Font font = new Font("Arial",Font.ITALIC,12);
        info = new JTextArea();
        info.setFont(font);
        info.setBackground(Color.white);
        info.setEditable(false);
        scrll = new JScrollPane(info);
        scrll.setBounds(430, 30, 200, 100);
        thongtintrandau = new JLabel("Thống kê:");
        thongtintrandau.setFont(new Font("TimesRoman", Font.ITALIC, 16));
        thongtintrandau.setForeground(Color.BLACK);
        thongtintrandau.setBounds(430, 10, 300, 20);
        f.add(thongtintrandau);
        
        f.add(scrll);
        scrll.setVisible(false);
        thongtintrandau.setVisible(false);
        
        //khung chat
        Font fo = new Font("Arial",Font.ITALIC,12);
        content = new JTextArea();
        content.setFont(fo);
        content.setBackground(Color.white);
        
        content.setEditable(false);
        JScrollPane sp = new JScrollPane(content);
        sp.setBounds(430,170,300,180);
        send = new JButton("Gui");
        send.setBounds(640, 390, 70, 40);
        nhap = new JTextField(30);
        nhap.setFont(fo);
        enterchat = new JTextField("");
        enterchat.setFont(fo);
        enterchat.setBounds(430, 390, 200, 40);
        enterchat.setBackground(Color.white);
        f.add(enterchat);
        f.add(send);
        f.add(sp);
        f.setVisible(true);
        //gửi gói tin tới server
        send.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource().equals(send))
		        {
		            try
		            {
		                time = getCurrentTimeStamp();
		                temp+="You: " + enterchat.getText() + "\n"+time +"\n";
		                content.setText(temp);          
		                oos.writeObject("chat," + enterchat.getText() +","+ time);
		                enterchat.setText("");
		                //temp = "";
		                enterchat.requestFocus();
		                content.setVisible(false);
		                content.setVisible(true);
		                
		            }
		            catch (Exception r)
		            {
		                r.printStackTrace();
		            }
		        }
            }
        });

        
        demthoigian = new JLabel("Thời gian: ");
        demthoigian.setFont(new Font("TimesRoman", Font.ITALIC, 16));
        demthoigian.setForeground(Color.BLACK);
        f.add(demthoigian);
        demthoigian.setBounds(430, 120, 300, 50);
        second = 0;
        minute = 0;
        thoigian = new Timer(1000, new ActionListener() {

	        @Override
	        public void actionPerformed(ActionEvent e) {
	            String temp = minute.toString();
	            String temp1 = second.toString();
	            if (temp.length() == 1) {
	                temp = "0" + temp;
	            }
	            if (temp1.length() == 1) {
	                temp1 = "0" + temp1;
	            }
	            /*if (second == 59) {
	                    demthoigian.setText("Thá»?i Gian:" + temp + ":" + temp1);
	                    minute++;
	                    second = 0;
	            } else {
	                    demthoigian.setText("Thá»?i Gian:" + temp + ":" + temp1);
	                    second++;
	            }*/
	            if (second == 10) {
	                try {
	                	oos.writeObject("checkwin");
	                } catch (IOException ex) {
	                }
	                Object[] options = { "Dong y", "Huy bo" };
	                int m = JOptionPane.showConfirmDialog(f,
                            "Bạn đã thua?\n"+"Bạn có muốn chơi lại không?", "Thong bao",
                            JOptionPane.YES_NO_OPTION);
	                checkWin("enemy");
	                if (m == JOptionPane.YES_OPTION) {
	                    second = 0;
	                    minute = 0;
	                    setVisiblePanel(p);
	                    newgame();
	                    try {
	                        oos.writeObject("newgame");
	                    } catch (IOException ie) {
	                            //
	                    }
	                } else if (m == JOptionPane.NO_OPTION) {
	                	thoigian.stop();
	                }
	            } else {
	                demthoigian.setText("Thời gian: " + temp + ":" + temp1);
	                second++;
	            }
	        }
        });
        //button clicked
        bt = new JButton[x][y];
        for(int i = 0; i < x; i++)
        {
            for(int j = 0; j < y; j++)
            {
                final int a = i, b =j;
                bt[a][b] = new JButton();
                bt[a][b].setBackground(Color.LIGHT_GRAY);
                bt[a][b].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    flat = true;// server da click
                    thoigian.start();
                    
                    second = 0;
                    minute = 0;
                    
                    matrandanh[a][b] = 1;
                    bt[a][b].setEnabled(false);                        
                    //bt[a][b].setIcon(new ImageIcon("C:\\Users\\ADMIN\\eclipse-workspace\\Caro_sourcecode\\src\\Caro_Server\\x.png"));
                    bt[a][b].setBackground(Color.BLUE);
                    try{
                        oos.writeObject("caro," + a + "," + b);
                        setEnableButton(false);
                    }
                    catch(Exception ie)
                    {
                        ie.printStackTrace();
                    }
                    thoigian.stop();
                  }

                });                
                p.add(bt[a][b]);
                p.setVisible(false);
                p.setVisible(true);
            }
        }
        //nhận response từ server
        try {
            socket = new Socket("localhost",1234);
            System.out.println("Da ket noi toi server!");
            os=socket.getOutputStream();
            is=socket.getInputStream();
            oos= new ObjectOutputStream(os);
            ois= new ObjectInputStream(is);
            while(true) {
                String stream = ois.readObject().toString();
                String[] data = stream.split(",");
                if (data[0].equals("chat")) {
                    temp += "Your friends: " + data[1] +'\n'+ data[2]+'\n';
                    content.setText(temp);
                } else if (data[0].equals("caro")) {
                    thoigian.start();
                    second = 0;
                    minute = 0;
                    caro(data[1],data[2]);
                    setEnableButton(true);
                    if (winner == false) {
                            setEnableButton(true);
                    }
                }
                else if (data[0].equals("newgame")) {
                    newgame();
                    second = 0;
                    minute = 0;
                } 
                else if (data[0].equals("checkwin")) {
                    thoigian.stop();
                    checkWin("you");
                    JOptionPane.showConfirmDialog(f,
                            "Ban đã thắng?", "Thong bao",
                            JOptionPane.DEFAULT_OPTION);
                }
            }
        } catch (Exception ie) {
                // ie.printStackTrace();
        } //finally {
          //      socket.close();
          //      serversocket.close();
        //}
        textField = new TextField();
        
    }
    
  //kiểm tra người thắng
    public void checkWin(String a) {
    	if(a=="you") {
    		count++;
    		winn+="Trận "+count+": Bạn thắng!\n";
    		info.setText(winn);
    		thongtintrandau.setVisible(true);
    		info.setVisible(true);
    		scrll.setVisible(true);
    	}else if(a == "enemy") {
    		count++;
    		winn+="Trận "+count+": Đối phương thắng!\n";
    		info.setForeground(Color.blue);
    		info.setText(winn);
    		thongtintrandau.setVisible(true);
    		info.setVisible(true);
    		scrll.setVisible(true);
    	}
    }
    
    //game mới
    public void newgame() {
        for (int i = 0; i < x; i++)
        {
            for (int j = 0; j < y; j++) {
                    bt[i][j].setBackground(Color.LIGHT_GRAY);
                    matran[i][j] = 0;
                    matrandanh[i][j] = 0;
            }
        }
        setEnableButton(true);
        second = 0;
        minute = 0;
        thoigian.stop();
    }
       
    public void setVisiblePanel(JPanel pHienthi) {
		f.add(pHienthi);
		pHienthi.setVisible(true);
		pHienthi.updateUI();// ......

    }
    
    public void setEnableButton(boolean b) {
        for (int i = 0; i < x; i++)
        {
            for (int j = 0; j < y; j++) {
                    if (matrandanh[i][j] == 0)
                            bt[i][j].setEnabled(b);
            }
        }
    }
    
    //thuat toan tinh Kiểm tra thắng thua
    //kiểm tra hàng ngang
    public int checkHang() {
        int win = 0, hang = 0, n = 0, k = 0;
        boolean check = false;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (check) {
                    if (matran[i][j] == 1) {
                        hang++;
                        if (hang > 4) {
                                win = 1;
                                break;
                        }
                        continue;
                    } else {
                        check = false;
                        hang = 0;
                    }
                }
                if (matran[i][j] == 1) {
                    check = true;
                    hang++;
                } else {
                    check = false;
                }
            }
            hang = 0;
        }
        return win;
    }
    //kiểm tra cột dọc
    public int checkCot() {
        int win = 0, cot = 0;
        boolean check = false;
        for (int j = 0; j < y; j++) {
            for (int i = 0; i < x; i++) {
                if (check) {
                    if (matran[i][j] == 1) {
                        cot++;
                        if (cot > 4) {
                                win = 1;
                                break;
                        }
                        continue;
                    } else {
                        check = false;
                        cot = 0;
                    }
                }
                if (matran[i][j] == 1) {
                    check = true;
                    cot++;
                } else {
                    check = false;
                }
            }
            cot = 0;
        }
        return win;
    }
    //kiểm tra chéo phải
    public int checkCheoPhai() {
        int win = 0, cheop = 0, n = 0, k = 0;
        boolean check = false;
        for (int i = x - 1; i >= 0; i--) {
        	for (int j = 0; j < y; j++) {
                if (check) {
                    if (matran[n - j][j] == 1) {
                        cheop++;
                        if (cheop > 4) {
                            win = 1;
                            break;
                        }
                        continue;
                    } else {
                        check = false;
                        cheop = 0;
                    }
                }
                if (matran[i][j] == 1) {
                    n = i + j;
                    check = true;
                    cheop++;
                } else {
                    check = false;
                }
            }
            cheop = 0;
            check = false;
        }
        return win;
    }
    //kiểm tra chéo trái
    public int checkCheoTrai() {
        int win = 0, cheot = 0, n = 0;
        boolean check = false;
        for (int i = 0; i < x; i++) {
            for (int j = y - 1; j >= 0; j--) {
                if (check) {
                    if (matran[n - j - 2 * cheot][j] == 1) {
                        cheot++;
                        System.out.print("+" + j);
                        if (cheot > 4) {
                                win = 1;
                                break;
                        }
                        continue;
                    } else {
                        check = false;
                        cheot = 0;
                    }
                }
                if (matran[i][j] == 1) {
                    n = i + j;
                    check = true;
                    cheot++;
                } else {
                    check = false;
                }
            }
            n = 0;
            cheot = 0;
            check = false;
        }
        return win;
    }
    //chat game
    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");//dd/MM/yyyy
        java.util.Date now = new java.util.Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
    
    public void caro(String x, String y)
    {	
    	//vị trí đánh 
    	System.out.println("vi tri:" + x + ", " + y );
        xx = Integer.parseInt(x);
        yy = Integer.parseInt(y);
        
        // danh dau vi tri danh
        matran[xx][yy] = 1;
        matrandanh[xx][yy] = 1;
        bt[xx][yy].setEnabled(false);
        //bt[xx][yy].setIcon(new ImageIcon("x.png"));
        //bt[xx][yy].setIcon(new ImageIcon("C:\\Users\\ADMIN\\eclipse-workspace\\Caro_sourcecode\\src\\Caro_Server\\x.png"));
        bt[xx][yy].setBackground(Color.RED);
        
        // Kiem tra thang hay chua
        System.out.println("CheckH:" + checkHang());
        System.out.println("CheckC:" + checkCot());
        System.out.println("CheckCp:" + checkCheoPhai());
        System.out.println("CheckCt:" + checkCheoTrai());
        winner = (checkHang() == 1 || checkCot() == 1 || checkCheoPhai() == 1 || checkCheoTrai() == 1);
        if (checkHang() == 1 || checkCot() == 1 || checkCheoPhai() == 1 || checkCheoTrai() == 1) {
            setEnableButton(false);
            thoigian.stop();
            try {
                    oos.writeObject("checkwin");
            } catch (IOException ex) 
                                          {
            }
            Object[] options = { "Dong y", "Huy bo" };
            int m = JOptionPane.showConfirmDialog(f,
                        "Ban da thua.Ban co muon choi lai khong?", "Thong bao",
                        JOptionPane.YES_NO_OPTION);
            checkWin("enemy");
            if (m == JOptionPane.YES_OPTION) {
                second = 0;
                minute = 0;
                setVisiblePanel(p);
                newgame();
                try {
                    oos.writeObject("newgame");
                } catch (IOException ie) {
                        //
                }
            } else if (m == JOptionPane.NO_OPTION) {
                thoigian.stop();
            }
        }
        
    }
    
    public static void main(String[] args) {
        new Client();
    }
    
}
