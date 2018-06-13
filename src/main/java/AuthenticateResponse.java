import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticateResponse {

    @JsonProperty("data")
    Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Data {
        @JsonProperty("userid")
        String userid;
        @JsonProperty("sessid")
        String sessid;
    }

}
