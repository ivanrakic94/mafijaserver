import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.sql.*;

public class MafijaServer implements Runnable {

	static LinkedList<Igrac> igraci = new LinkedList<Igrac>();
	static LinkedList<ChatNitZaSve> igraci2 = new LinkedList<ChatNitZaSve>();
	static LinkedList<Partija> partije=new LinkedList<Partija>();
	static int partijaId = 0;
	static boolean uspesnoUlogovan = false;

	public static void main(String[] args) {
		try {
			ServerSocket serverskiSoket = new ServerSocket(2906);
			new Thread(new MafijaServer()).start();

			// dva polja za unos sa pocetnog prozora.
			String user = null;
			String password = null;

			while (true) {
				Socket klijentskiSoket = serverskiSoket.accept();
				BufferedReader ulaz = new BufferedReader(new InputStreamReader(klijentskiSoket.getInputStream()));
				PrintStream izlaz = new PrintStream(klijentskiSoket.getOutputStream());
				
				//petlja se vrti dok se uspesno ne izvrsi registrovanje ili login novog igraca
				while (!uspesnoUlogovan) {
					String[] data = ulaz.readLine().split(";");
					if (data.length >= 1) {
						user = data[0];
					}
					if (data.length >= 2) {
						password = data[1];
					}
					// proeverava da li klijent zeli da se registruje.
					boolean registruj = false;
					if (data.length == 3) {
						if (data[2].equals("REGISTRUJ")) {
							registruj = true;
						}
					}

					// konekncija na bazu i te zafrkancije
					Connection con;
					Statement st;
					ResultSet rs;

					try {

						String db = "jdbc:ucanaccess://baza.accdb";
						con = DriverManager.getConnection(db);
						st = con.createStatement();
						String sql;

						// proces registracije se obavlja ako je registruj == true u suprotnom se radi proces login-ovonja
						if (registruj) {
							sql = "insert into Table1 values('" + user + "','" + password + "')";
							// provera da li postoji vec registrovan korisnik sa
							// tim imenom
							String sqlDaProveriDaLiPostojiNekoSaTimImenom = "select USERNAME from Table1 where USERNAME = '"
									+ user + "'";
							rs = st.executeQuery(sqlDaProveriDaLiPostojiNekoSaTimImenom);

							int count1 = 0;
							while (rs.next()) {
								count1++;
							}
							
							//Ne postoji neko u bazi, sa tim imenom
							if (count1 == 0) {
								

								// executeUpdate() se koristi kod insert u
								// tabelu, vraca broj koji govori koliko novih
								// redova je ubaceno u tabelu
								int pom = st.executeUpdate(sql);

								if (pom == 1) {
									// System.out.println(user + ", USPESNO STE SE REGISTROVALI.");
									izlaz.println("OK");
									uspesnoUlogovan = true;
								} else {
									//System.out.println("Nije uspela registracija");
									izlaz.println("NOTOK");

								}

							} else {
								//System.out.println("Korisnik " + user + "  vec postoji u bazi.");
								izlaz.println("NOTOK");

							}

						} else {// ovo je obican proces logovanja, bez registracije
							sql = "select USERNAME,PASSWORD from Table1 where USERNAME = '" + user
									+ "' AND PASSWORD = '" + password + "'";
							// executeQuery() se koristi za citanje podataka.
							rs = st.executeQuery(sql);

							int count = 0;
							while (rs.next()) {
								count = count + 1;
							}

							if (count == 1) {
								System.out.println("Uspesno ste se ulogovali.");
								izlaz.println("OK");
								uspesnoUlogovan = true;
							} else {
								System.out.println("Korisnik " + user + " sa tim passwordom ne postoji u bazi.");
								izlaz.println("NOTOK");

							}
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				uspesnoUlogovan = false;

				igraci.add(new Igrac(user, klijentskiSoket));

				if (igraci.size()%6 == 0) {
					partije.add(new Partija(igraci, partijaId));
					new Thread(partije.getLast()).start();
					partijaId++;
					
				}
			}

		} catch (BindException e) {
			System.out.println("SERVER JE VEC POKRENUT");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			ServerSocket serSoket = new ServerSocket(2907);

			while (true) {
				Socket klijentskiS = serSoket.accept();
				BufferedReader ulaz2 = new BufferedReader(new InputStreamReader(klijentskiS.getInputStream()));
				String ime=ulaz2.readLine();
				int partID=0;
				boolean b=false;
			
				for (int i = 0; i < partije.size(); i++) {
					for (int j = 0; j < partije.get(i).getPom().length; j++) {
						if(partije.get(i).getPom()[j].getIme().equals(ime)){
							partID=i;
							System.out.println(partID);
							b=true;
							break;
							
						}
						
					}
					if(b==true){break;}
				}
				
				igraci2.add(new ChatNitZaSve(klijentskiS, igraci2, ime,partID));
				igraci2.getLast().start();


			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
