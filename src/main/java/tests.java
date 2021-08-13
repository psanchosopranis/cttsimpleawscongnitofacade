import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tests {

    public static void main(String[] args) {
        List emails = new ArrayList<String>();
        emails.add("f41s4n@gmail.com");
        emails.add("user@domain.com");
        emails.add("user@domain.co.in");
        emails.add("user1@domain.com");
        emails.add("user.name@domain.com");
        emails.add("user_name@domain.co.in");
        emails.add("user-name@domain.co.in");
        emails.add("user@domaincom");

//Invalid emails
        emails.add("@yahoo.com");

        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

        Pattern pattern = Pattern.compile(regex);


        for(Object emailAsObj : emails){

            Matcher matcher = pattern.matcher((String) emailAsObj);
            System.out.println((String) emailAsObj +" : "+ matcher.matches());
        }
    }
}
