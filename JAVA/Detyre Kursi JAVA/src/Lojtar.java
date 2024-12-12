import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Lojtar extends JPanel implements ActionListener{
		int thesare = 0;
		private int xPos;
		private int yPos;
		double thesareRatio; 
		boolean eshtePrekurMur = false;
		private final Labirint lab;
		private final KontrolleriLojes kontrolleriLojes;
		
		public Lojtar(Labirint lab, KontrolleriLojes kontrolleriLojes){
			this.lab = lab;
			xPos = lab.hyrjeX;
			yPos = lab.hyrjeY;
			lab.l[lab.hyrjeX][lab.hyrjeY] = 5;
			this.kontrolleriLojes = kontrolleriLojes;
		}
		public Lojtar(Labirint lab, KontrolleriLojes kontrolleriLojes,String s){
			this.lab = lab;
			xPos = lab.lojtarix(lab.ngarkoLojen());
			yPos = lab.lojtariy(lab.ngarkoLojen());
			lab.l[xPos][yPos] = 5;
			this.kontrolleriLojes = kontrolleriLojes;
			this.thesare = ngarkoThesaret();
		}
		public void actionPerformed(ActionEvent e){
			;
		}

		public void leviz(Labirint lab, char ch) {
			switch(ch) {
			
			case 'm': levizMajtas(lab); break;
			case 'd': levizDjathtas(lab); break;
			case 'p': levizPoshte(lab); break;
			case 'l': levizLart(lab); break;
			}
		}

		private void levizMajtas(Labirint lab){
			if(lab.get(xPos, yPos-1) != 1 && lab.get(xPos, yPos-1) != 3){
				lab.set(xPos, yPos, 0);
				if(eshteNeDalje(lab, xPos, yPos-=1))	Fitore();
				else if(lab.get(xPos, yPos) == 2) {
					mblidhThesar(lab);
				}
				else 	lab.set(xPos, yPos, 5);
			}else if(lab.get(xPos, yPos-1) == 1) {
				eshtePrekurMur=true;
				Fitore();
			}
		}

		private void levizDjathtas(Labirint lab){
			if(lab.get(xPos, yPos+1) != 1 && lab.get(xPos, yPos+1) != 3){
				lab.set(xPos, yPos, 0);
				if(eshteNeDalje(lab, xPos, yPos+=1))	Fitore();
				else if(lab.get(xPos, yPos) == 2) {
					mblidhThesar(lab);
				}else 	lab.set(xPos, yPos, 5);
			}else if(lab.get(xPos, yPos+1) == 1) {
				eshtePrekurMur=true;
				Fitore();
			}
		}

		private void levizLart(Labirint lab){
			if(lab.get(xPos-1, yPos) != 1 && lab.get(xPos-1, yPos) != 3){
				lab.set(xPos, yPos, 0);
				if(eshteNeDalje(lab, xPos-=1, yPos))	Fitore();
				else if(lab.get(xPos, yPos) == 2) {
					mblidhThesar(lab);
				}
				else 	lab.set(xPos, yPos, 5);
			}else if(lab.get(xPos-1, yPos) == 1) {
				eshtePrekurMur=true;
				Fitore();
			}
		}

		private void levizPoshte(Labirint lab){
			if(lab.get(xPos+1, yPos) != 1 && lab.get(xPos+1, yPos) != 3){
				lab.set(xPos, yPos, 0);
				if(eshteNeDalje(lab, xPos+=1, yPos))	Fitore();
				else if(lab.get(xPos, yPos) == 2) {
					mblidhThesar(lab);
				}
				else 	lab.set(xPos, yPos, 5);
			}else if(lab.get(xPos+1, yPos) == 1) {
				eshtePrekurMur=true;
				Fitore();
			}
		}

		private boolean eshteNeDalje(Labirint lab, int x, int y) {
			return lab.get(x,y)==4;
		}
		
		private void mblidhThesar(Labirint lab) {
				thesare++;
				lab.set(xPos, yPos, 6);
				kontrolleriLojes.rregulloNrThesareve(thesare);
			
		}
		public void Fitore(){
			thesareRatio = (double)thesare/lab.thesareCount;
	        new KontrolleriLojes(thesare, thesareRatio, eshtePrekurMur);
		}

	public void ruajThesaret(){
		try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("files/thesare.dat"))) {
			dos.writeInt(thesare);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public int ngarkoThesaret(){
		int thesare = 0;
		try (DataInputStream dis = new DataInputStream(new FileInputStream("files/thesare.dat"))) {
			thesare = dis.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return thesare;
	}
}


