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
int partija;
LinkedList<ChatNitZaSve> igraci = new LinkedList<ChatNitZaSve>();

public ChatNitZaSve(Socket soketZaKom,LinkedList<ChatNitZaSve> igraci,String ime,int partija) {
	this.soketZaKom = soketZaKom;
	this.igraci=igraci;
	this.ime=ime;
	this.partija=partija;
}

public void run(){
	String linija;
	int donjiOpsegSlanja;
	int gornjiOpsegSlanja;
	if(partija==0){
		donjiOpsegSlanja=partija;
		gornjiOpsegSlanja=5;
	}else {donjiOpsegSlanja=partija*6;
	 gornjiOpsegSlanja=donjiOpsegSlanja+5;
	}

	try {
		ulazniTokOdKlijenta=new BufferedReader(new InputStreamReader(soketZaKom.getInputStream()));
		izlazniTokKaKlijentu=new PrintStream(soketZaKom.getOutputStream());
		
		while(true){
			linija=ulazniTokOdKlijenta.readLine();
			if(linija.startsWith("kraj")){
				break;
				
			}
			for (int i = donjiOpsegSlanja; i <=gornjiOpsegSlanja; i++) {
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

