import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;


public class Partija implements Runnable {
	
	private int idPartije;
	LinkedList<Igrac> igraci = new LinkedList<Igrac>();
	Igrac[] pom = new Igrac[6];
	private int brGradjana = 2;
	private int brUbica = 2;
	private boolean kraj = false;
	
	public Partija(LinkedList<Igrac> lista, int partijaId) {
		igraci = lista;
		idPartije = partijaId;
	}

	public void run() {
		for (int i = 0; i < pom.length; i++) {
			pom[i] = igraci.get(i);
		}
		
		igraci.clear();
		
		for (int i = 0; i < pom.length; i++) {
			for (int j = 0; j < pom.length; j++) {
				pom[i].getIzlaz().println(pom[j].getIme());
			}
		}
		
		Random rnd = new Random();
		for (int i = pom.length - 1; i > 0; i--)
		    {
		      int index = rnd.nextInt(i + 1);
		      Igrac a = pom[index];
		      pom[index] = pom[i];
		      pom[i] = a;
		    }
		
		pom[0].getIzlaz().println("Ubica");
		pom[1].getIzlaz().println("Ubica");
		pom[2].getIzlaz().println("Gradjanin");
		pom[3].getIzlaz().println("Gradjanin");
		pom[4].getIzlaz().println("Lekar");
		pom[5].getIzlaz().println("Policajac");
		
		
		try {
			while(!kraj) {
			String ubijeni1 = "";
			if(pom[0].isuIgri()) {
				ubijeni1 = pom[0].getUlaz().readLine();
			}
			
			String ubijeni2 = "";
			if(pom[1].isuIgri()) {
			ubijeni2 = pom[1].getUlaz().readLine();
			}
			
			String izlecen = "";
			if(pom[4].isuIgri()) {
			izlecen = pom[4].getUlaz().readLine();
			}
			
			String osumnjiceni = "";
			if(pom[5].isuIgri()) {
			osumnjiceni = pom[5].getUlaz().readLine();
			}
			
			if (ubijeni1.equals("")) {
				ubijeni1 = ubijeni2;
			}
			
			if (ubijeni1 != ubijeni2 && !ubijeni1.equals("") && !ubijeni2.equals("")) {
				String[] ubijeni = {ubijeni1, ubijeni2};
				ubijeni1 = ubijeni[(new Random()).nextInt(2)];
			}
			
			if (ubijeni1.equals(izlecen)) {
				ubijeni1 = null;
			}
			
			if(pom[5].isuIgri()) {
				if ((osumnjiceni.equals(pom[0].getIme()) || osumnjiceni.equals(pom[1].getIme()))) {
					pom[5].getIzlaz().println("Osumnjiceni jeste ubica!");
				} else {
					pom[5].getIzlaz().println("Osumnjiceni nije ubica.");
				}
			}
			
			for (int i = 0; i < pom.length; i++) {
				if (pom[i].getIme().equals(ubijeni1)) {
					pom[i].setuIgri(false);
				}
			}
			
			if (pom[2].getIme().equals(ubijeni1) || pom[3].getIme().equals(ubijeni1)) {
				brGradjana--;
			}
			
			proveriStanje();
			
			for (int i = 0; i < pom.length; i++) {
				if (pom[i].getIme().equals(ubijeni1)) {
					pom[i].getIzlaz().println("izbacen");
				} else {
					pom[i].getIzlaz().println("nije");
				}
			}
			
			if (brGradjana == 0) {
				kraj = true;
				for (int i = 0; i < pom.length; i++) {
					pom[i].getIzlaz().println("kraj");
				}
				continue;
			}
			
			for (int i = 0; i < pom.length; i++) {
				pom[i].getIzlaz().println("nije");
			}
			
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
			
			Igrac izbacen = izbaci(pom);
			for (int i = 0; i < pom.length; i++) {
				if (izbacen == pom[i]) {
					pom[i].setuIgri(false);
				}
			}
			
			proveriStanje();
			
			for (int i = 0; i < pom.length; i++) {
				if (!pom[i].isuIgri()) {
					pom[i].getIzlaz().println("izbacen");
				} else {
					pom[i].getIzlaz().println("nije");
				}
			}
			
			if (izbacen == pom[0] || izbacen == pom[1]) {
				brUbica--;
			}
			
			if (izbacen == pom[2] || izbacen == pom[3]) {
				brGradjana--;
			}
			
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
			
			}
			
			if (brGradjana == 0) {
				for (int i = 0; i < pom.length; i++) {
					pom[i].getIzlaz().println("ubice.");
				}
			} else {
				for (int i = 0; i < pom.length; i++) {
					pom[i].getIzlaz().println("gradjani.");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  
		

	}
	
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
	
	public void proveriStanje() {
		for (int i = 0; i < pom.length; i++) {
			for (int j = 0; j < pom.length; j++) {
				if(pom[j].isuIgri()) {
					pom[i].getIzlaz().println(pom[j].getIme() + " je u igri.");
				} else {
					pom[i].getIzlaz().println(pom[j].getIme() + " nije u igri.");
				}
			}
		}
	}
	
	
}
