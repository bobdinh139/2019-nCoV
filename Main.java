import java.io.IOException;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;


public class Main {

	public static void main(String args[]) throws IOException {
		ParseEcdc2();
	}



	private static void ParseEcdc2() throws IOException {
		URL url = new URL("https://www.ecdc.europa.eu/en/geographical-distribution-2019-ncov-cases");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		String parser = "";
		con.setRequestMethod("GET");
		con.connect();

		Scanner scn = new Scanner(url.openStream());
		while(scn.hasNext()) {
			parser+=scn.nextLine();
		}
		scn.close();
		String allstring =  parser.substring(parser.indexOf("Asia"));

		System.out.println(parser.substring(parser.indexOf("Situation"), parser.indexOf("CET")+3));
		parser = parser.substring(parser.indexOf("Asia")).replace(" and ", ", ");
		String continent[] = {"Asia", "Europe","America","Oceania", "."};
		String listOfCountries[] = new String[4];
		ArrayList<String> country = new ArrayList<>();

		for (int i=0; i< continent.length-1;i++) {
			parser = parser.substring(parser.indexOf(continent[i]));
			listOfCountries[i]= parser.substring(parser.indexOf(continent[i]), parser.indexOf(continent[i+1]));
		}

		for (int i=0; i< listOfCountries.length;i++) {

			for (int j =0; j< listOfCountries[i].length(); j++) {
				try {
					country.add(listOfCountries[i].substring(0, listOfCountries[i].indexOf(",")));
				} catch (Exception e) {
					country.add(listOfCountries[i].substring(0, listOfCountries[i].length()));
				}
				listOfCountries[i] = listOfCountries[i].substring(listOfCountries[i].indexOf(",")+1);		
			}
		}

		Set<String> set = new LinkedHashSet<>(country);
		country.clear();
		country.addAll(set);

		for (int i=0; i< country.size(); i++) {
			country.set(i,  country.get(i).replace("strong", ""));
			country.set(i,  country.get(i).replace("</>", ""));
			country.set(i,  country.get(i).replace("<br /><>", ""));
			country.set(i,  country.get(i).replace(":", ""));
			country.set(i,  country.get(i).replace("(", ": "));
			country.set(i,  country.get(i).replace(")", ""));
			//country.set(i,  country.get(i).replace(" and ", "\n "));

			for (int j=0; j< continent.length; j++) {
				country.set(i,country.get(i).replace(continent[j], ""));

			}
		}

		for (int i=0; i< country.size(); i++) {
			System.out.println(country.get(i));
		}

		String death = allstring.substring(allstring.indexOf("Oceania"), allstring.indexOf("deaths"));
		String death2 = death.substring(death.indexOf("<p>"));
		getDeaths(death2, "Total");
		String chinadeath = allstring.substring(allstring.indexOf("Oceania"));
		death2 = chinadeath.substring(chinadeath.indexOf("deaths"), chinadeath.indexOf("China"));
		getDeaths(death2, "China");

		System.out.println("All countries");

		ArrayList<String> allCountries = GetAllCountries(country);
		for (int i=0; i<allCountries.size(); i++) {
			System.out.println(allCountries.get(i));
		}

		System.out.println("All numbers");
		ArrayList<String> allInfections = GetAllInfections(country);
		for (int i=0; i<allInfections.size(); i++) {
			System.out.println(allInfections.get(i));
		}


	}

	public static void getDeaths(String death2, String whereisit) {
		char[] replacepraren = death2.toCharArray();
		String totaldeath = "";

		for (int i=0; i< replacepraren.length;i++) {
			if (replacepraren[i]>= '0' && replacepraren[i]<= '9') {
				totaldeath+=replacepraren[i];
			}
		}
		System.out.println(whereisit + " deaths: "+ totaldeath);
	}


	public static ArrayList<String> GetAllInfections(ArrayList<String> country) {
		ArrayList<String> listCountry = new ArrayList<>();

		for (int i=0; i<country.size(); i++) {
			country.set(i, country.get(i).substring(country.get(i).indexOf(":")+1));
			char[] replacepraren = country.get(i).toCharArray();
			String all = "";
			for (int j=0; j< replacepraren.length;j++) {
				all+=replacepraren[j];
			}
			if(!(all.equals("") || all.equals(" ") || all.equals(null)))
			listCountry.add(all);
		}

		return listCountry;

		/*
		for (int i=0; i<listCountry.size(); i++) {
			System.out.println(listCountry.get(i));
		}
		 */

	}

	public static ArrayList<String> GetAllCountries(ArrayList<String> country) {
		ArrayList<String> listCountry = new ArrayList<>();
		for (int i=0; i<country.size(); i++) {
			char[] replacepraren = country.get(i).toCharArray();
			String all = "";
			for (int j=0; j< replacepraren.length;j++) {
				if (replacepraren[j] != ':') {
					all+=replacepraren[j];
				}else {
					listCountry.add(all);
					break;
				}
			}

		}

		return listCountry;

		/*
		for (int i=0; i<listCountry.size(); i++) {
			System.out.println(listCountry.get(i));
		}
		 */

	}

	/*deprecated
	public static void ParseEcdcWebsite() throws IOException {
		URL url = new URL("https://www.ecdc.europa.eu/en/geographical-distribution-2019-ncov-cases");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		String parser = "";
		con.setRequestMethod("GET");
		con.connect();

		Scanner scn = new Scanner(url.openStream());
		while(scn.hasNext()) {
			parser+=scn.nextLine();
		}
		scn.close();

		int d = parser.indexOf("strong");
		String eee = parser.substring(d);
		String fff = eee.substring(eee.indexOf("strong"), eee.indexOf("/div"));
		//System.out.println(fff);
		String country[] = {"China", "Thailand", "Malaysia", "Singapore", "Japan", "Australia", "United States", "Germany", "France", "Republic of Korea", "Canada", "Vietnam", "Nepal", "Cambodia", "Sri Lanka"};
		for (int i=0; i<country.length; i++) {
			readData(fff, country[i]);
		}

	}
	//deprecated
	public static void readData(String fff, String location) {
		String china = fff.substring(fff.indexOf(location));
		String Achina = china.substring(china.indexOf(location), china.indexOf(")"));
		char[] replacepraren = Achina.toCharArray();
		for (int i=0; i<replacepraren.length; i++) {
			if (replacepraren[i] == '(') {
				replacepraren[i] = ':';
			}
		}
		Achina = String.valueOf(replacepraren);
		System.out.println(Achina);
	}
	//deprecated
	public static void parseWebsite() throws IOException {
		URL url = new URL("http://health.people.com.cn/GB/26466/431463/431576/index.html");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		String parser = "";
		con.setRequestMethod("GET");
		con.connect();

		Scanner scn = new Scanner(url.openStream());
		while(scn.hasNext()) {
			parser+=scn.nextLine();
		}
		scn.close();
		int d = parser.indexOf("data:");
		String eee = parser.substring(d);
		DisplayData("gntotal:", eee, "Total cases in china: ");
		DisplayData("deathtotal:", eee, "Total death in China: ");
		DisplayData("sustotal:", eee, "Suspects in China: ");
		DisplayData("curetotal:", eee, "Recovered cases in China: ");
		System.out.println();

		String fff = eee.substring(eee.indexOf("\"otherlist\":"), eee.indexOf("\"historylist\""));

		String country[] = {"Thailand", "Malaysia", "Singapore", "Japan", "Australia", "United States", "Germany", "France", "South Korea", "Canada", "Vietnam", "Nepal", "Cambodia", "Sri Lanka"};
		String namecountry[] = {"Thailand", "Malaysia", "Singapore", "Japan", "Australia", "United States", "Germany", "France", "South Korea", "Canada", "Vietnam", "Nepal", "Cambodia", "Sri Lanka"};

		for (int i =0; i<country.length; i++) {
			fff=fff.substring(fff.indexOf("{")+1);
			country[i] = fff;
			country[i] = country[i].substring(country[i].indexOf("\""), country[i].indexOf("}"));
		}

		fff = fff.substring(fff.indexOf("value"));
		for (int i =0; i<country.length; i++) {
			DisplayData("value\":", country[i], "Total cases in "+ namecountry[i]+ ": ");
			DisplayData("deathNum\":", country[i], "Death cases in "+ namecountry[i]+ ": ");
			DisplayDataLast("cureNum\":", country[i], "Recovered cases in "+ namecountry[i]+ ": ");
			System.out.println();
		}
	}
	//deprecated
	public static void DisplayDataLast(String where, String eee, String about) {
		String chinaTotal = eee.substring(eee.indexOf(where));
		String chinaTotalOff = 	chinaTotal.substring(chinaTotal.indexOf(":")+2);
		System.out.println(about + chinaTotalOff);
	}
	//deprecated
	public static void DisplayData(String where, String eee, String about) {
		String chinaTotal = eee.substring(eee.indexOf(where));
		String chinaTotalOff = 	chinaTotal.substring(chinaTotal.indexOf(":")+2, chinaTotal.indexOf(","));
		System.out.println(about + chinaTotalOff);
	}
	*/
}
