import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    Database() {
        try {
            this.connection = DriverManager.getConnection("jdbc:" + Constants.host + "world", "guest", "wtEU422gigdXFF");
            System.out.println("Connection established!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    HashMap<String, String> getCountryNames() {
        HashMap<String, String> countries = new HashMap<>();

        try {
            preparedStatement = this.connection.prepareStatement("select * from country");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                String code = resultSet.getString("Code");

                countries.put( code, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return countries;
    }

    void initializeTable() {
        try {
            preparedStatement = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS `countryLanguageUsage` ( `Country` varchar(255), `Official Usage (%)` varchar(255), `Non-official Usage (%)` varchar(255), PRIMARY KEY ( Country ))");
            System.out.println("Creating table...");
            preparedStatement.executeUpdate();
            System.out.println("Table created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    HashMap<String, String> fetchCountries () {
        HashMap<String, String> countries = new HashMap<>();
        try {
            preparedStatement = this.connection.prepareStatement("SELECT Code, Name FROM `country` GROUP By Code");
            resultSet = preparedStatement.executeQuery();
            System.out.println("Countries data fetched successfully!");

            while (resultSet.next()) {
                String countryCode = resultSet.getString("Code");
                String countryName = resultSet.getString("Name");

                countries.put( countryCode, countryName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return countries;
    }

    HashMap<String, String> fetchLanguages(boolean isOfficial) {
        HashMap<String, String> languages = new HashMap<>();
        try {
            preparedStatement = this.connection.prepareStatement("SELECT CountryCode, SUM(Percentage) as " + (isOfficial ? "`Official Usage (%)`" : "`Non-official Usage (%)`") + " FROM `countrylanguage` WHERE IsOfficial = " + (isOfficial ? "'T'" : "'F'") + " GROUP By CountryCode");
            resultSet = preparedStatement.executeQuery();
            System.out.println("languages data fetched successfully!");

            while (resultSet.next()) {
                String countryCode = resultSet.getString("CountryCode");
                String percentageSpoken = resultSet.getString((isOfficial ? "Official Usage (%)" : "Non-official Usage (%)"));

                languages.put( countryCode, percentageSpoken);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return languages;
    }


    void insertLanguages(
            HashMap<String, String> countriesList,
            HashMap<String, String> officialLanguages,
            HashMap<String, String> nonOfficialLanguages
    ) {
        HashMap<String, ArrayList<String>> countriesData = new HashMap<>();

        countriesList.forEach((country, index) -> {
//            System.out.println("Country: " + countriesList.get(country));
//            System.out.println("Official %: " + officialLanguages.get(country));
//            System.out.println("Non official: % " + nonOfficialLanguages.get(country));

            String name = countriesList.get(country);
            ArrayList<String> languageData = new ArrayList<>();

            languageData.add((officialLanguages.get(country) == null ? "No Data" : officialLanguages.get(country)));
            languageData.add((nonOfficialLanguages.get(country) == null ? "No Data" : nonOfficialLanguages.get(country)));

            countriesData.put(name, languageData);
        });

        countriesData.forEach((entry, index) -> {
//            System.out.println("Eat kebabs " + entry);
//            System.out.println("Eat kebabs " + countriesData.get(entry).get(0));
//            System.out.println("Eat kebabs " + countriesData.get(entry).get(1));
            try {
                preparedStatement = this.connection.prepareStatement("INSERT Ignore into countryLanguageUsage (Country,`Official Usage (%)`,`Non-official Usage (%)`) VALUES ('" + entry + "','" + countriesData.get(entry).get(0) + "','" + countriesData.get(entry).get(1) + "')");
                System.out.println("Adding data...");
                preparedStatement.executeUpdate();
                System.out.println("Data added successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
