public class queries {
    static String createTable = "CREATE TABLE IF NOT EXISTS countryLanguageUsage( `Country` varchar(255), `Official Usage (%)` varchar(255), `Non-official Usage (%)` varchar(255))";
    static String insertEntries = "INSERT INTO countryLanguageUsage, (Country,`Non-official Usage`,`Official Usage`) VALUES ('%s','%s','%s')";
    static String listCountries = "select * from country";
    static String listCountryLanguages = "select * from countrylanguage";
}
