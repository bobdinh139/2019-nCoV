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
	private String allstring2="";
	private String parse="";
	private	String continent[] = {"Africa","Asia", "America","Europe","Oceania","Other", "."};
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
		allstring2 =  parser.substring(parser.indexOf("Since"));
		parser = parser.substring(parser.indexOf(continent[0])).replace(" and ", ", ").replace("(PRC)", "").replace("(Special Administrative Region)", "").replace("(Japan)","--Japan");

		String listOfCountries[] = new String[6];

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
		String death = allstring2.substring(allstring2.indexOf("<strong>") +8, allstring2.indexOf("deaths"));
		String death2 = death.substring(death.indexOf("<strong>"));
		death2 = death2.substring(death2.indexOf("2020")+4);
		return ParseDeaths(death2);
	}


	public ArrayList<String> getCountryDeaths(){
		ArrayList<String> allCountryDeaths = new ArrayList<String>();
		String death = allstring2.substring(allstring2.indexOf("<strong>")+8);
		death = death.substring(death.indexOf("from")+4, death.indexOf("</p>"));
		death = death.replace("an international conveyance (Japan)", "an international conveyance--Japan");
		death = death.replace("(PRC)","");
		death = death.replace("(SAR)","");
		death = death.replace("(", ": ");
		death = death.replace(")", "");
		death = death.replace("and", "");
		death = death.replace("the", "");
		for (int i =0; i< 10; i++) {
			death = death.replace(Integer.toString(i), "");
		}
		death = death.replace(".", "");
		death = death.replace(",", "");
		char[] replacepraren = death.toCharArray();
		String countrydeath = "";

		for (int i=0; i < replacepraren.length; i++) {
			if (replacepraren[i] != ':') {
				countrydeath+=replacepraren[i];
			} else {
				allCountryDeaths.add(countrydeath.trim());
				countrydeath = "";
			}
		}
		return allCountryDeaths;
	}

	public ArrayList<Integer> getCountryDeathsNum(){
		ArrayList<Integer> allCountryDeaths = new ArrayList<Integer>();
		String death = allstring2.substring(allstring2.indexOf("<strong>")+8);
		death = death.substring(death.indexOf("from")+4, death.indexOf("</p>"));
		death = death.replace("an international conveyance (Japan)", "an international conveyance--Japan");
		death = death.replace("(SAR)","");
		death= death.replace("PRC","");
		death = death.replace("(", ": ");
		death = death.replace(")", "");
		death = death.replace("and", ",");
		death = death.replace("the", "");
		death = death.replace(":", "");
		death = death.replace(" ", "");
		death = death.replace(".", ",");
		death = death.replace("--", "");
		char[] replacepraren = death.toCharArray();
		String countrydeath = "";

		for (int i=0; i < replacepraren.length; i++) {
			if (replacepraren[i] >= '0' && replacepraren[i] <= '9') {
				countrydeath+=replacepraren[i];
			} else if (replacepraren[i]  == ',' && !countrydeath.equals("")){
				allCountryDeaths.add(Integer.parseInt(countrydeath));
				countrydeath = "";
			}
		}

		return allCountryDeaths;
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
		String temp = parse.substring(parse.indexOf("31 December"));
		lastUpdateDate = temp.substring(temp.indexOf("2019")+4, temp.indexOf(","));
		lastUpdateDate= lastUpdateDate.replace("and","");
		lastUpdateDate= lastUpdateDate.replace("as","");
		lastUpdateDate= lastUpdateDate.replace("of","");
		lastUpdateDate= lastUpdateDate.replace("<strong>","");
		return lastUpdateDate.trim();
	}

	public int getAllCases() throws IOException {
		String temp = parse.substring(parse.indexOf("Since"));
		String allcase="";
		char[] replacepraren = temp.substring(temp.indexOf(",")+1, temp.indexOf("COVID")).toCharArray();

		for (int i=0; i< replacepraren.length;i++) {
			if (replacepraren[i]>= '0' && replacepraren[i]<= '9') {
				allcase+=replacepraren[i];
			}
		}
		return Integer.parseInt(allcase);
	}

	public int getAllCasesAlt() throws IOException {
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
				try {
					listCountry.add(Integer.parseInt(all));
				}catch (Exception e){
					listCountry.add(ParseDeaths(all));
				}
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

		for (int i=0; i<listCountry.size(); i++) {
			listCountry.set(i, listCountry.get(i).replaceAll(" ", ""));
		}

		return listCountry;

	} 


}
