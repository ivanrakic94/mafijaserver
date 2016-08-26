import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;


public class MafijaServer implements Runnable {
	
	static LinkedList<Igrac> igraci = new LinkedList<Igrac>();
	static LinkedList<ChatNitZaSve> igraci2 = new LinkedList<ChatNitZaSve>();
	static int partijaId = 0;
	
	public static void main(String[] args) {
		try {
			ServerSocket serverskiSoket = new ServerSocket(2906);
			new Thread(new MafijaServer()).start();
			while (true) {
				Socket klijentskiSoket = serverskiSoket.accept();
				BufferedReader ulaz = new BufferedReader(new InputStreamReader(klijentskiSoket.getInputStream()));
				
				igraci.add(new Igrac(ulaz.readLine(), klijentskiSoket));
				
				if (igraci.size() == 6) {
					Partija p = new Partija(igraci, partijaId);
					p.run();
				}
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run(){
		try {
			ServerSocket serSoket=new ServerSocket(2907);
			
			
			while(true){
				Socket klijentskiS = serSoket.accept();
				BufferedReader ulaz2 = new BufferedReader(new InputStreamReader(klijentskiS.getInputStream()));
				igraci2.add(new ChatNitZaSve(klijentskiS, igraci2, ulaz2.readLine()));
				igraci2.getLast().start();
				
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
