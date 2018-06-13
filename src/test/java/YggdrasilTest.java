import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


@Test
public class YggdrasilTest {
    RestTemplate restTemplate = new RestTemplate();
    String url = "https://demo.yggdrasilgaming.com/game.web/service";
    AuthenticateResponse authenticateResponse;
    PlayResponse playResponse;
    int gameCount = 1;
    int step = 1;
    private static final Logger LOGGER = Logger.getLogger(YggdrasilTest.class.getName());


    @BeforeClass(alwaysRun = true)
    public void authenticate() {
        authenticateResponse = restTemplate.getForObject(getAuthenticate(), AuthenticateResponse.class);
        Assert.assertNotNull(authenticateResponse.data.sessid);
        Assert.assertNotNull(authenticateResponse.data.userid);
        LOGGER.log(Level.INFO, "SessionId = " + authenticateResponse.data.sessid);
        LOGGER.log(Level.INFO, "UserId = " + authenticateResponse.data.userid);
    }

    @Test
    public void winGame() throws InterruptedException{
        ResponseEntity<String> gameResponse
                = restTemplate.getForEntity(getGame(), String.class);
        LOGGER.log(Level.INFO, gameResponse.toString());
        ResponseEntity<String> wagersResponse
                = restTemplate.getForEntity(getWagers(), String.class);
        LOGGER.log(Level.INFO, wagersResponse.toString());
        ResponseEntity<String> promotionsResponse
                = restTemplate.getForEntity(getPromotions(), String.class);
        LOGGER.log(Level.INFO, promotionsResponse.toString());
        ResponseEntity<String> tournamentResponse
                = restTemplate.getForEntity(getTournament(), String.class);
        LOGGER.log(Level.INFO, tournamentResponse.toString());
        ResponseEntity<String> cashraceResponse
                = restTemplate.getForEntity(getCashrace(), String.class);
        LOGGER.log(Level.INFO, cashraceResponse.toString());
        playResponse
                = restTemplate.getForObject(getFirstPlay(), PlayResponse.class);
        LOGGER.log(Level.INFO, "Won Amount = "+playResponse.data.wager.bets.get(0).wonamount);
        while(playResponse.data.wager.bets.get(0).eventdata.nextCmds==null){
            playResponse
                    = restTemplate.getForObject(getPlay(), PlayResponse.class);
            LOGGER.log(Level.INFO, "Won Amount = "+playResponse.data.wager.bets.get(0).wonamount);
            gameCount++;
        }
        playResponse
                = restTemplate.getForObject(getPlayFinal(), PlayResponse.class);
        LOGGER.log(Level.INFO, "Won Amount = "+playResponse.data.wager.bets.get(0).wonamount);
        Assert.assertTrue(Double.parseDouble(playResponse.data.wager.bets.get(0).wonamount)>0.00);
        LOGGER.log(Level.INFO, "We won after "+gameCount+ " games.");

    }

    private String getRandomHexString(int numchars) {
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while (sb.length() < numchars) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, numchars);
    }

    private String generateId() {
        StringBuffer sb = new StringBuffer();
        sb.append(getRandomHexString(8));
        sb.append("-");
        sb.append(getRandomHexString(4));
        sb.append("-");
        sb.append(getRandomHexString(4));
        sb.append("-");
        sb.append(getRandomHexString(4));
        sb.append("-");
        sb.append(getRandomHexString(12));
        return sb.toString();
    }

    private URI getAuthenticate() {
        UriComponentsBuilder authenticate = UriComponentsBuilder.fromUriString(url)
                .queryParam("fn", "authenticate")
                .queryParam("org", "Demo")
                .queryParam("lang", "en")
                .queryParam("gameid", "7316")
                .queryParam("channel", "pc")
                .queryParam("currency", "EUR")
                .queryParam("userName", "")
                .queryParam("crid", generateId())
                .queryParam("csid", generateId());
        LOGGER.log(Level.INFO, authenticate.build().toString());
        return authenticate.build().toUri();
    }

    private URI getGame() {
        UriComponentsBuilder game = UriComponentsBuilder.fromUriString(url)
                .queryParam("fn", "game")
                .queryParam("currency", "EUR")
                .queryParam("gameid", "7316")
                .queryParam("org", "Demo")
                .queryParam("crid", generateId())
                .queryParam("csid", generateId());
        LOGGER.log(Level.INFO, game.build().toString());
        return game.build().toUri();
    }

    private URI getWagers() {
        UriComponentsBuilder wagers = UriComponentsBuilder.fromUriString(url)
                .queryParam("fn", "wagers")
                .queryParam("status", "Pending")
                .queryParam("limit", "1")
                .queryParam("sessid", authenticateResponse.data.sessid)
                .queryParam("gameid", "7316")
                .queryParam("crid", generateId())
                .queryParam("csid", generateId());
        LOGGER.log(Level.INFO, wagers.build().toString());
        return wagers.build().toUri();
    }

    private URI getPromotions() {
        UriComponentsBuilder promotions = UriComponentsBuilder.fromUriString(url)
                .queryParam("fn", "promotions")
                .queryParam("sessid", authenticateResponse.data.sessid)
                .queryParam("gameid", "7316")
                .queryParam("currency", "EUR")
                .queryParam("org", "Demo")
                .queryParam("crid", generateId())
                .queryParam("csid", generateId());
        LOGGER.log(Level.INFO, promotions.build().toString());
        return promotions.build().toUri();
    }

    private URI getTournament() {
        UriComponentsBuilder tournament = UriComponentsBuilder.fromUriString(url)
                .queryParam("fn", "tournament")
                .queryParam("sessid", authenticateResponse.data.sessid)
                .queryParam("org", "Demo")
                .queryParam("gameid", "7316")
                .queryParam("crid", generateId())
                .queryParam("csid", generateId());
        LOGGER.log(Level.INFO, tournament.build().toString());
        return tournament.build().toUri();
    }

    private URI getCashrace() {
        UriComponentsBuilder cashrace = UriComponentsBuilder.fromUriString(url)
                .queryParam("fn", "cashrace")
                .queryParam("sessid", authenticateResponse.data.sessid)
                .queryParam("gameid", "7316")
                .queryParam("crid", generateId())
                .queryParam("csid", generateId());
        LOGGER.log(Level.INFO, cashrace.build().toString());
        return cashrace.build().toUri();
    }

    private URI getFirstPlay() {
        UriComponentsBuilder play = UriComponentsBuilder.fromUriString(url)
                .queryParam("fn", "play")
                .queryParam("currency", "EUR")
                .queryParam("gameid", "7316")
                .queryParam("sessid", authenticateResponse.data.sessid)
                .queryParam("log", "DefB,0.05/BetChd,0.05/CurrChd,true/BPanl,0.05/")
                .queryParam("gameHistorySessionId", "seesion")
                .queryParam("gameHistoryTicketId", "ticket")
                .queryParam("amount", "1.25")
                .queryParam("lines", "1111111111111111111111111")
                .queryParam("coin", "0.05")
                .queryParam("clientinfo", authenticateResponse.data.userid)
                .queryParam("channelID", "")
                .queryParam("crid", generateId())
                .queryParam("csid", generateId());
        LOGGER.log(Level.INFO, play.build().toString());
        return play.build().toUri();
    }

    private URI getPlay() {
        UriComponentsBuilder play = UriComponentsBuilder.fromUriString(url)
                .queryParam("fn", "play")
                .queryParam("currency", "EUR")
                .queryParam("gameid", "7316")
                .queryParam("sessid", authenticateResponse.data.sessid)
                .queryParam("log", "")
                .queryParam("gameHistorySessionId", "seesion")
                .queryParam("gameHistoryTicketId", "ticket")
                .queryParam("amount", "1.25")
                .queryParam("lines", "1111111111111111111111111")
                .queryParam("coin", "0.05")
                .queryParam("clientinfo", authenticateResponse.data.userid)
                .queryParam("channelID", "")
                .queryParam("crid", generateId())
                .queryParam("csid", generateId());
        LOGGER.log(Level.INFO, play.build().toString());
        return play.build().toUri();
    }

    private URI getPlayFinal() {
        UriComponentsBuilder play = UriComponentsBuilder.fromUriString(url)
                .queryParam("fn", "play")
                .queryParam("currency", "EUR")
                .queryParam("gameid", "7316")
                .queryParam("sessid", authenticateResponse.data.sessid)
                .queryParam("log", "")
                .queryParam("gameHistorySessionId", "seesion")
                .queryParam("gameHistoryTicketId", "ticket")
                .queryParam("amount", "0")
                .queryParam("wagerid", playResponse.data.wager.wagerId)
                .queryParam("betid", "1")
                .queryParam("step", "2")
                .queryParam("cmd", "C")
                .queryParam("channelID", "")
                .queryParam("crid", generateId())
                .queryParam("csid", generateId());
        LOGGER.log(Level.INFO, play.build().toString());
        return play.build().toUri();
    }
}
