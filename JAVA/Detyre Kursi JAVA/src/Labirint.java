import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Random;

public class Labirint extends JPanel{
    protected int m;
    private final Random random ;
    protected int thesareCount=0;
    protected int hyrjeX, hyrjeY;
    protected int daljeX, daljeY;
    protected int[][] l;

    public Labirint(int m)  {
        this.m = m;
        this.l = new int[m][m];
        this.random = new Random();
        inicializoLabirint();
        int anaHyrjes = shtoHyrje();
        gjeneroLabirint(hyrjeX, hyrjeY);
        shtoDalje(anaHyrjes);
        thesareCount = numeroThesare();
    }
    public Labirint(){
        this.l = ngarkoLojen();
        this.m =l.length;
        thesareCount = numeroThesare();
        this.random = new Random();
        this.thesareCount = ngarkothesarecount();
    }
    
    public int get(int x, int y){
		return l[x][y];
	}

    private void inicializoLabirint() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                l[i][j] = 1; 
            }
        }
    }
    private int shtoHyrje() {
        int anaHyrjes = random.nextInt(4); // 0: Siper, 1: Poshte, 2: Majtas, 3: Djathtas

        switch (anaHyrjes) {
            case 0:
                hyrjeX = 1;
                do {
                    hyrjeY = random.nextInt(m - 2) + 1;
                } while (hyrjeY % 2 != 1);
                l[hyrjeX-1][hyrjeY] = 3; 
                break;

            case 1:
                hyrjeX = m - 2;

                do {
                    hyrjeY = random.nextInt(m - 2) + 1;
                } while (hyrjeY % 2 != 1);
                l[hyrjeX+1][hyrjeY] = 3; 
                break;

            case 2:
                do {
                    hyrjeX = random.nextInt(m - 2) + 1;
                } while (hyrjeX % 2 != 1);
                hyrjeY = 1;
                l[hyrjeX][hyrjeY-1] = 3; 
                break;

            case 3:
                do {
                    hyrjeX = random.nextInt(m - 2) + 1;
                } while (hyrjeX % 2 != 1);
                hyrjeY = m - 2;
                l[hyrjeX][hyrjeY+1] = 3; 
                break;
        }
        return anaHyrjes;
}
    
    private void shtoDalje(int anaHyrjes) {
    	int anaDaljes;
        do {
            anaDaljes = random.nextInt(4);
        } while (anaHyrjes == anaDaljes);
        switch (anaDaljes) {
            case 0:
            	daljeX = 0;
                do {
                    do {
                        daljeY = random.nextInt(m - 2) + 1;
                    } while (daljeY % 2 != 1);
                } while (l[daljeX + 1][daljeY] != 0);
                l[daljeX][daljeY] = 4; 
                break;

            case 1:
                daljeX = m - 1;

                do {
                    do {
                        daljeY = random.nextInt(m - 2) + 1;
                    } while (daljeY % 2 != 1);
                } while (l[daljeX - 1][daljeY] != 0);
                l[daljeX][daljeY] = 4; 
                break;

            case 2:
            	daljeY = 0;
                do {
                    do {
                        daljeX = random.nextInt(m - 2) + 1;
                    } while (daljeX % 2 != 1);
                } while (l[daljeX][daljeY + 1] != 0);

                l[daljeX][daljeY] = 4; 
                break;

            case 3:
            	daljeY = m - 1;
                do {
                    do {
                        daljeX = random.nextInt(m - 2) + 1;
                    } while (daljeX % 2 != 1);
                } while (l[daljeX][daljeY - 1] != 0);
                
                l[daljeX][daljeY] = 4; 
                break;
        }
    }
    
    
    private void gjeneroLabirint(int x, int y) {
        if(eshteThesar(x, y))
    	    l[x][y] = 0;
       
        char[] drejtimet = {'L', 'P', 'M', 'D'}; //Lart, poshte, majtas, djathtas
        perzieVektorin(drejtimet);

        for (char c : drejtimet) {

            int newX = x + 2 * (c == 'M' ? 1 : (c == 'D' ? -1 : 0));
            int newY = y + 2 * (c == 'L' ? 1 : (c == 'P' ? -1 : 0));
            int midX = (x + newX) / 2;
            int midY = (y + newY) / 2;
            if (newX > 0 && newX < m - 1 && newY > 0 && newY < m - 1 && eshteMur(newX, newY)) {

                int randomThesar = random.nextInt(7);

                if (randomThesar == 5 && nukKaThesarePerreth(newX, newY))
                    l[newX][newY] = 2;
                else l[newX][newY] = 0;


                randomThesar = random.nextInt(6);

                if (randomThesar == 5 && nukKaThesarePerreth(midX, midY))
                    l[midX][midY] = 2;
                else l[midX][midY] = 0;

                gjeneroLabirint(newX, newY);
            }
        }
    }
 
    private boolean nukKaThesarePerreth(int x, int y) {
    	return eshteThesar(x, y - 1) && eshteThesar(x, y + 1) && eshteThesar(x - 1, y) && eshteThesar(x + 1, y);
    }


    private void perzieVektorin(char[] v) {
        for (int i = v.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            char temp = v[i];
            v[i] = v[index];
            v[index] = temp;
        }
    }
    
    public boolean eshteMur(int x, int y) {
    	return l[x][y]==1;
    	
    }
    public boolean eshteThesar(int x, int y) {
    	return l[x][y] != 2;
    	
    }

    private int numeroThesare() {
    	int thesareCount=0;
    	for(int i=0; i<m; i++) {
    		for(int j=0; j<m; j++) {
        		if(l[i][j]==2) thesareCount++;
        	}
    	}
    	return thesareCount;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int madhesiaQeliza = 900/(m+10);
        int xOffset = (getWidth() - m * madhesiaQeliza) / 2;
        int yOffset = (getHeight() - m * madhesiaQeliza) / 2;

        for (int i = 0; i < m; i++) {
            for (int k = 0; k < m; k++) {
                int x = k * madhesiaQeliza + xOffset;
                int y = i * madhesiaQeliza + yOffset;

                if (l[i][k] == 1) {
                    g.setColor(Color.black);
                    g.fillRect(x, y, madhesiaQeliza, madhesiaQeliza);
                    
                } else if (l[i][k] == 4) {
                  
                    drawImage(g, "photos/door_gold.png", x, y, madhesiaQeliza, madhesiaQeliza);
                } else if (l[i][k] == 5) {
                    g.setColor(Color.gray);
                    g.fillRect(x, y, madhesiaQeliza, madhesiaQeliza);
                    drawImage(g, "photos/oldman_down_1.png", x, y, madhesiaQeliza, madhesiaQeliza);
                } else if (l[i][k] == 3) {

                    drawImage(g, "photos/door.png", x, y, madhesiaQeliza, madhesiaQeliza);
                } else if (l[i][k] == 2) {

                    g.setColor(Color.gray);
                    g.fillRect(x, y, madhesiaQeliza, madhesiaQeliza);
                    drawImage(g, "photos/coins.png", x, y, madhesiaQeliza, madhesiaQeliza);

                } else if (l[i][k] == 6) {
                    g.setColor(Color.gray);
                    g.fillRect(x, y, madhesiaQeliza, madhesiaQeliza);
                    drawImage(g, "photos/oldman_down_glow.png", x, y, madhesiaQeliza, madhesiaQeliza);
                }
                else {
                    g.setColor(Color.gray);
                    g.fillRect(x, y, madhesiaQeliza, madhesiaQeliza);
                }
            }
        }
    }
    public void set(int x, int y, int vlera){
		l[x][y] = vlera;
		repaint();
	}
    public  void drawImage(Graphics g, String Path, int x, int y, int width, int height) {
        try {
            Image image = ImageIO.read(new File(Path));
            g.drawImage(image, x, y, width, height, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int lojtarix(int[][] m){
        int a = 0;
        for (int i= 0 ;i < m.length;i++){
            for (int j=0;j<m[i].length;j++){
                if (m[i][j]==5||m[i][j]==6){
                    a = i;
                    break;
                }
            }
        }
        return a;
    }
    public int lojtariy(int[][] m){
        int a = 0;
        for (int[] ints : m) {
            for (int j = 0; j < ints.length; j++) {
                if (ints[j] == 5 || ints[j] == 6) {
                    a = j;
                    break;
                }
            }
        }
        return a;
    }
    public void ruajLojen() {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("files/loja.dat"))) {
            dos.writeInt(m);
            for (int i = 0; i < m; i++) {
                for (int j= 0; j < m; j++) {
                    dos.writeInt(l[i][j]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int[][] ngarkoLojen() {
        int[][] m = null;
        try (DataInputStream dis = new DataInputStream(new FileInputStream("files/loja.dat"))) {
            int rreshtat = dis.readInt();
            m = new int[rreshtat][rreshtat];
            for (int i = 0; i < rreshtat; i++) {
                for (int j = 0; j < rreshtat; j++) {
                    m[i][j] = dis.readInt();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return m;
    }
    public void ruajthesarecount() {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("files/thesarecount.dat"))) {
            dos.writeInt(thesareCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int ngarkothesarecount() {
        int thesarecount = 0;
        try (DataInputStream dis = new DataInputStream(new FileInputStream("files/thesarecount.dat"))) {

            thesarecount = dis.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return thesarecount;
    }
}
