package ohtu;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

public class Main {

    public static void main(String[] args) throws IOException {
        String studentNr = "14045381";
        if (args.length > 0) {
            studentNr = args[0];
        }

        String url = "http://ohtustats-2013.herokuapp.com/opiskelija/" + studentNr + ".json";

        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);
        client.executeMethod(method);

        InputStream stream = method.getResponseBodyAsStream();

        String bodyText = IOUtils.toString(stream);

//        System.out.println("json-muotoinen data:");
//        System.out.println(bodyText);

        Gson mapper = new Gson();
        Palautukset palautukset = mapper.fromJson(bodyText, Palautukset.class);

//        System.out.println("oliot:");
        System.out.println("Opiskelijanumero " + palautukset.getPalautukset().get(0).getOpiskelijanumero());
        int yhteensaTehtavia = 0;
        int yhteensaTunteja = 0;
        
        for (Palautus palautus : palautukset.getPalautukset()) {
            yhteensaTehtavia +=palautus.getTehtavia();
            yhteensaTunteja +=palautus.getTunteja();
            System.out.println("viikko 1: " + palautus.getTehtavia() + " tehtävää " + palautus.getTehtavat() + "    aikaa kului: " + palautus.getTunteja());
        }
        System.out.println("yhteensä " + yhteensaTehtavia + " tehtävää " + yhteensaTunteja + " tuntia");
        
    }
}
