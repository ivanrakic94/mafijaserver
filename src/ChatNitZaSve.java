import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;


public class ChatNitZaSve extends Thread {
BufferedReader ulazniTokOdKlijenta=null;
PrintStream izlazniTokKaKlijentu=null;
Socket soketZaKom=null;
String ime=" ";
LinkedList<ChatNitZaSve> igraci = new LinkedList<ChatNitZaSve>();

public ChatNitZaSve(Socket soketZaKom,LinkedList<ChatNitZaSve> igraci,String ime) {
	this.soketZaKom = soketZaKom;
	this.igraci=igraci;
	this.ime=ime;
}

public void run(){
	String linija;

	try {
		ulazniTokOdKlijenta=new BufferedReader(new InputStreamReader(soketZaKom.getInputStream()));
		izlazniTokKaKlijentu=new PrintStream(soketZaKom.getOutputStream());
		
		while(true){
			linija=ulazniTokOdKlijenta.readLine();
			if(linija.startsWith("kraj")){
				break;
				
			}
			for (int i = 0; i < igraci.size(); i++) {
				igraci.get(i).getIzlazniTokKaKlijentu().println("<"+ ime +"> "+linija);
				
			}
			
		}
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
}

public BufferedReader getUlazniTokOdKlijenta() {
	return ulazniTokOdKlijenta;
}

public void setUlazniTokOdKlijenta(BufferedReader ulazniTokOdKlijenta) {
	this.ulazniTokOdKlijenta = ulazniTokOdKlijenta;
}

public PrintStream getIzlazniTokKaKlijentu() {
	return izlazniTokKaKlijentu;
}

public void setIzlazniTokKaKlijentu(PrintStream izlazniTokKaKlijentu) {
	this.izlazniTokKaKlijentu = izlazniTokKaKlijentu;
}

	
	
	
}
