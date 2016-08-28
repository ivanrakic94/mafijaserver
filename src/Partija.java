import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;


public class Partija implements Runnable {
	
	private int idPartije;
	//ovo je lista u koju se ubacuju svi igraci kada se nakace na server
	LinkedList<Igrac> igraci = new LinkedList<Igrac>();
	//kada se nakupi 6 igraca u listi, kopiraju se u ovaj niz i lista se cisti
	Igrac[] pom = new Igrac[6];
	private int brGradjana = 2;
	private int brUbica = 2;
	//kraj partije
	private boolean kraj = false;
	private boolean uspesnoUlogovan = false;
	
	public Partija(LinkedList<Igrac> lista, int partijaId) {
		igraci = lista;
		idPartije = partijaId;
	}

	public void run() {
		//kopiranje iz liste u niz
		for (int i = 0; i < pom.length; i++) {
			pom[i] = igraci.get(i);
		}
		
		//ciscenje liste
		igraci.clear();
		
		
		
		//slanje svim igracima imena svih igraca
		for (int i = 0; i < pom.length; i++) {
			for (int j = 0; j < pom.length; j++) {
				pom[i].getIzlaz().println(pom[j].getIme());
			}
		}
		
		//randomiziranje niza
		Random rnd = new Random();
		for (int i = pom.length - 1; i > 0; i--)
		    {
		      int index = rnd.nextInt(i + 1);
		      Igrac a = pom[index];
		      pom[index] = pom[i];
		      pom[i] = a;
		    }
		
		//dodeljivanje uloga
		pom[0].getIzlaz().println("Ubica");
		pom[1].getIzlaz().println("Ubica");
		pom[2].getIzlaz().println("Gradjanin");
		pom[3].getIzlaz().println("Gradjanin");
		pom[4].getIzlaz().println("Lekar");
		pom[5].getIzlaz().println("Policajac");
		
		//ovo se salje da bi se ubicama ispisalo ko je drugi ubica
		for (int i = 0; i < pom.length; i++) {
			pom[i].getIzlaz().println(pom[0].getIme()+" i "+pom[1].getIme());
		}
		
		
		try {
			//odavde krece partija
			while(!kraj) {
			//ako prvi ubica nije izbacen, procitaj koga je ubio
			String ubijeni1 = "";
			if(pom[0].isuIgri()) {
				ubijeni1 = pom[0].getUlaz().readLine();
			}
			
			//isto za drugog
			String ubijeni2 = "";
			if(pom[1].isuIgri()) {
			ubijeni2 = pom[1].getUlaz().readLine();
			}
			
			//isto za lekara
			String izlecen = "";
			if(pom[4].isuIgri()) {
			izlecen = pom[4].getUlaz().readLine();
			}
			
			//isto za policajca
			String osumnjiceni = "";
			if(pom[5].isuIgri()) {
			osumnjiceni = pom[5].getUlaz().readLine();
			}
			
			//ako prvi slucajno nije uneo koga ce da ubije, prekopiraj od drugog ubice
			if (ubijeni1.equals("")) {
				ubijeni1 = ubijeni2;
			}
			
			//ako su razlicito uneli, izaberi random ko ce biti ubijen
			if (ubijeni1 != ubijeni2 && !ubijeni1.equals("") && !ubijeni2.equals("")) {
				String[] ubijeni = {ubijeni1, ubijeni2};
				ubijeni1 = ubijeni[(new Random()).nextInt(2)];
			}
			
			//ako je lekar izlecio ubijenog, onda nista
			if (ubijeni1.equals(izlecen)) {
				ubijeni1 = null;
			}
			
			//odgovori policajcu
			if(pom[5].isuIgri()) {
				if ((osumnjiceni.equals(pom[0].getIme()) || osumnjiceni.equals(pom[1].getIme()))) {
					pom[5].getIzlaz().println("Osumnjiceni jeste ubica!");
				} else {
					pom[5].getIzlaz().println("Osumnjiceni nije ubica.");
				}
			}
			
			//oznaci da je ubijeni ispao iz igre
			for (int i = 0; i < pom.length; i++) {
				if (pom[i].getIme().equals(ubijeni1)) {
					pom[i].setuIgri(false);
				}
			}
			
			//ako je ubijen gradjanin, smanji njihov broj
			if (pom[2].getIme().equals(ubijeni1) || pom[3].getIme().equals(ubijeni1)) {
				brGradjana--;
			}
			
			//odgovori svima ko je ubijen, ili ako niko nije, posalji null
			for (int i = 0; i < pom.length; i++) {
				pom[i].getIzlaz().println(ubijeni1);
			}
			
			//odgvori ubijenom da je ispao, svima ostalima da nisu
			for (int i = 0; i < pom.length; i++) {
				if (pom[i].getIme().equals(ubijeni1)) {
					pom[i].getIzlaz().println("izbacen");
				} else {
					pom[i].getIzlaz().println("nije");
				}
			}
			
			//ako nema vise gradjana, javi svima da je gotova partija, i prekini petlju
			if (brGradjana == 0) {
				kraj = true;
				for (int i = 0; i < pom.length; i++) {
					pom[i].getIzlaz().println("kraj");
				}
				continue;
			}
			
			//ako nije gotova partija, posalji svima da nije
			for (int i = 0; i < pom.length; i++) {
				pom[i].getIzlaz().println("nije");
			}
			
			//primi glas od svakog igraca koji je u igri i raspodeli glasove
			for (int i = 0; i < pom.length; i++) {
				if (pom[i].isuIgri()) {
					String glas = pom[i].getUlaz().readLine();
					for (int j = 0; j < pom.length; j++) {
						if (pom[j].getIme().equals(glas)) {
							pom[j].setBrGlasova(pom[j].getBrGlasova() + 1);
							break;
						}
					}
				}
			}
			
			//metoda izbaci racuna ko ima najvise glasova
			Igrac izbacen = izbaci(pom);
			//izbaci onog sa najvise glasova
			for (int i = 0; i < pom.length; i++) {
				if (izbacen == pom[i]) {
					pom[i].setuIgri(false);
				}
			}
			
			//posalji svima ko je izbacen
			for (int i = 0; i < pom.length; i++) {
				pom[i].getIzlaz().println(izbacen.getIme());
			}
			
			//posalji onom ko je izbacen da je izbacen, ostalima da nisu
			for (int i = 0; i < pom.length; i++) {
				if (!pom[i].isuIgri()) {
					pom[i].getIzlaz().println("izbacen");
				} else {
					pom[i].getIzlaz().println("nije");
				}
			}
			
			//smanji broj ubica ako je ubica izbacen
			if (izbacen == pom[0] || izbacen == pom[1]) {
				brUbica--;
			}
			
			//isto za gradjane
			if (izbacen == pom[2] || izbacen == pom[3]) {
				brGradjana--;
			}
			
			//ako nema gradjana ili ubica, javi svima da je kraj partije, ili da nije kraj
			if (brGradjana == 0 || brUbica == 0) {
				kraj = true;
				for (int i = 0; i < pom.length; i++) {
					pom[i].getIzlaz().println("kraj");
				}
			} else {
				for (int i = 0; i < pom.length; i++) {
					pom[i].getIzlaz().println("nije");
				}
			}
			//ako nije kraj, sve ispocetka
			}
			
			//ako je kraj i ako nema gradjana, javi da su pobedile ubice
			if (brGradjana == 0) {
				for (int i = 0; i < pom.length; i++) {
					pom[i].getIzlaz().println("ubice.");
				}
			} else {
				//obrnuto za gradjane
				for (int i = 0; i < pom.length; i++) {
					pom[i].getIzlaz().println("gradjani.");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  
		

	}
	
	//u principu sortira pomocni niz u rastucem redosledu i vraca prvog u nizu
	public Igrac izbaci(Igrac[] igraci) {
		Igrac[] niz = new Igrac[6];
		
		for (int i = 0; i < niz.length; i++) {
			niz[i] = igraci[i];
		}
		
		for (int i = 0; i < niz.length; i++) {
			for (int j = 0; j < niz.length; j++) {
				if (niz[i].isuIgri() && niz[j].isuIgri() && (niz[j].getBrGlasova() < niz[i].getBrGlasova())) {
					Igrac pom = niz[i];
					niz[i] = niz[j];
					niz[j] = pom;
				}
			}
		}
		
		return niz[0];
		
	}
}
