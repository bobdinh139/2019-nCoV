import java.io.IOException;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.Scanner;

public class Main {
	
	public static void main(String args[]) throws IOException {
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
	public static void DisplayDataLast(String where, String eee, String about) {
        String chinaTotal = eee.substring(eee.indexOf(where));
	    String chinaTotalOff = 	chinaTotal.substring(chinaTotal.indexOf(":")+2);
        System.out.println(about + chinaTotalOff);
	}
	public static void DisplayData(String where, String eee, String about) {
        String chinaTotal = eee.substring(eee.indexOf(where));
	    String chinaTotalOff = 	chinaTotal.substring(chinaTotal.indexOf(":")+2, chinaTotal.indexOf(","));
        System.out.println(about + chinaTotalOff);
	}
}
