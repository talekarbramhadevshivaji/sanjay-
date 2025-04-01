package api.endpoint;

public class Routes {
    
    // Base URL for the API - Added "https://" to make it a valid URL
    public static String base_url = "https://petstore.swagger.io/v2"; 
    
    // Corrected concatenation: Removed quotes around base_url so the variable is used
    public static String post_url = base_url + "/user"; 
    
    // Corrected concatenation and used variable reference instead of string literal
    public static String get_url = base_url + "/user/{username}"; 
    
    public static String put_url = base_url + "/user/{username}"; 
    
    public static String del_url = base_url + "/user/{username}"; 
}
