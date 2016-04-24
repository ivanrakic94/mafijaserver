import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;


public class MafijaServer {
	
	static LinkedList<Igrac> igraci = new LinkedList<Igrac>();
	static int partijaId = 0;
	
	public static void main(String[] args) {
		try {
			ServerSocket serverskiSoket = new ServerSocket(2906);
			
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

}
