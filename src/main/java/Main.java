import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Database WorldLanguagesDatabase = new Database();

        WorldLanguagesDatabase.initializeTable();
        HashMap<String, String> countryNameCodes = WorldLanguagesDatabase.fetchCountries();

        HashMap<String, String> officialLanguages = WorldLanguagesDatabase.fetchLanguages(true);
        HashMap<String, String> nonOfficialLanguages = WorldLanguagesDatabase.fetchLanguages(false);

        WorldLanguagesDatabase.insertLanguages(countryNameCodes, officialLanguages, nonOfficialLanguages);
    }
}
