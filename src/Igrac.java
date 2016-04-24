import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;


public class Igrac {
	private String ime;
	private String idUloge;
	private int idPartije;
	private Socket soket;
	private BufferedReader ulaz;
	private PrintStream izlaz;
	private boolean uIgri = true;
	private int brGlasova = 0;
	
	public Igrac(String ime, Socket soket) {
		this.ime = ime;
		this.soket = soket;
		try {
			ulaz = new BufferedReader(new InputStreamReader(soket.getInputStream()));
			izlaz = new PrintStream(soket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void obrisiGlasove() {
		brGlasova = 0;
	}
	
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		if (!ime.isEmpty() && ime != null) this.ime = ime;
		else throw new RuntimeException();
	}
	public String getIdUloge() {
		return idUloge;
	}
	public void setIdUloge(String idUloge) {
		this.idUloge = idUloge;
	}
	public int getIdPartije() {
		return idPartije;
	}
	public void setIdPartije(int idPartije) {
		this.idPartije = idPartije;
	}

	public BufferedReader getUlaz() {
		return ulaz;
	}

	public PrintStream getIzlaz() {
		return izlaz;
	}

	public boolean isuIgri() {
		return uIgri;
	}

	public void setuIgri(boolean uIgri) {
		this.uIgri = uIgri;
	}

	public int getBrGlasova() {
		return brGlasova;
	}

	public void setBrGlasova(int brGlasova) {
		this.brGlasova = brGlasova;
	}
	
	
}
