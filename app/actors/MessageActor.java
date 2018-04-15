package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.FeedResponse;
import data.Message;
import data.NewsAgentResponse;
import services.FeedService;
import services.NewsAgentService;

import java.util.UUID;

public class MessageActor extends UntypedActor {
    //self - reference the actor
    public static Props props(ActorRef out) {
        return Props.create(MessageActor.class, out);
        //props
    }

    public MessageActor(ActorRef out) {
        this.out = out;
    }

    private final ActorRef out;
    //object of feedService
    public FeedService feedservice = new FeedService();
    public NewsAgentService newsAgentService = new NewsAgentService();
    public NewsAgentResponse newsAgentResponse = new NewsAgentResponse();
    private FeedResponse feedResponse = new FeedResponse();


    //object of newsAgentService
    //define another actor response

    @Override
    public void onReceive(Object message) throws Throwable {
        ObjectMapper objectMapper = new ObjectMapper();
        if (message instanceof String) {
            Message messageObject = new Message();
            messageObject.text = (String) message;
            messageObject.sender = Message.Sender.USER;
            out.tell(objectMapper.writeValueAsString(messageObject), self());
            //newsAgentService.getNewsAgentResponse(messageObject.text,UUID.randomUUID());
            String query = newsAgentService.getNewsAgentResponse("Find " + message, UUID.randomUUID()).query;
            FeedResponse feedresponse = feedservice.getFeedByQuery(newsAgentResponse.query);
            messageObject.text = (feedResponse.title == null) ? "No results found" : "Showing results for: " + newsAgentResponse.query;
            messageObject.feedResponse = feedResponse();
            messageObject.sender = Message.Sender.BOT;
            out.tell(objectMapper.writeValueAsString(messageObject), self());
        }

        //send back the response

    }

    private FeedResponse feedResponse() {
    }


}