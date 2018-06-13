import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Bet {
    @JsonProperty("wonamount")
    String wonamount;
    @JsonProperty("eventdata")
    EventData eventdata;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class EventData {
        @JsonProperty("nextCmds")
        String nextCmds;
    }
}
