import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class GetUpdate {

	private int allCases =0;
	private String lastUpdateDate;
	private ArrayList<String> country = new ArrayList<>();
	private String parser = "";
	private String allstring="";
	private String parse="";
	private	String continent[] = {"Asia", "America","Oceania","Europe", "."};
	public GetUpdate() throws IOException {
		ParseEcdc() ;
	}
	
	
	private void ParseEcdc() throws IOException {
		URL url = new URL("https://www.ecdc.europa.eu/en/geographical-distribution-2019-ncov-cases");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("GET");
		con.connect();

		Scanner scn = new Scanner(url.openStream());
		while(scn.hasNext()) {
			parser+=scn.nextLine();
		}
		scn.close();
		parse = parser;
		allstring =  parser.substring(parser.indexOf("Asia"));

		parser = parser.substring(parser.indexOf("Asia")).replace(" and ", ", ");

		String listOfCountries[] = new String[4];

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
			country.set(i,  country.get(i).replace("<p>", ""));
			country.set(i,  country.get(i).replace("</p>", ""));
			country.set(i,  country.get(i).replace("<>", ""));
			
			for (int j=0; j< continent.length; j++) {
				country.set(i,country.get(i).replace(continent[j], ""));

			}
		}

	}

	public int getAllDeaths() throws IOException {
		String death = allstring.substring(allstring.indexOf(continent[3]), allstring.indexOf("deaths"));
		String death2 = death.substring(death.indexOf("<p>"));

		return ParseDeaths(death2);
	}

	public int getChinaDeaths() throws IOException {
		String chinadeath = allstring.substring(allstring.indexOf(continent[3]));
		String death2 = chinadeath.substring(chinadeath.indexOf("deaths"), chinadeath.indexOf("China"));

		return ParseDeaths(death2);
	}

	private int ParseDeaths(String death2) {

		char[] replacepraren = death2.toCharArray();
		String totaldeath = "";

		for (int i=0; i< replacepraren.length;i++) {
			if (replacepraren[i]>= '0' && replacepraren[i]<= '9') {
				totaldeath+=replacepraren[i];
			}
		}

		return Integer.parseInt(totaldeath);


	}

	public String getDateUpdated() throws IOException {
    String temp = parse.substring(parse.indexOf("<strong>"));
		lastUpdateDate = temp.substring(temp.indexOf("Situation"), temp.indexOf("CET")+3);
		return lastUpdateDate;
	}

	public int getAllCases() throws IOException {
		for (int i=0; i<GetAllInfections().size(); i++) {
			allCases+=GetAllInfections().get(i);
		}
		return allCases;
	}

	public ArrayList<Integer> GetAllInfections() throws IOException {
		ArrayList<Integer> listCountry = new ArrayList<>();

		for (int i=0; i<country.size(); i++) {
			country.set(i, country.get(i).substring(country.get(i).indexOf(":")+1));
			char[] replacepraren = country.get(i).toCharArray();
			String all = "";
			for (int j=0; j< replacepraren.length;j++) {
				all+=replacepraren[j];
			}
			if (!(all.equals("")||all.equals(null) || all.equals(" ") )){
				all=all.replace(" ","");
				listCountry.add(Integer.parseInt(all));
			}
		}
		return listCountry;

	}

	public ArrayList<String> GetAllCountries() throws IOException {
		ArrayList<String> listCountry = new ArrayList<>();
		for (int i=0; i<country.size(); i++) {
			char[] replacepraren = country.get(i).toCharArray();
			String all = "";
			for (int j=0; j< replacepraren.length;j++) {
				if (replacepraren[j] != ':') {
					all+=replacepraren[j];
				}else {
					listCountry.add(all.trim());
					break;
				}
			}

		}

		return listCountry;

	} 


}
