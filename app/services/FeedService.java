package services;

import data.FeedResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import play.libs.ws.WS;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.twirl.api.Xml;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class FeedService {
    public FeedResponse getFeedByQuery(String query){
        FeedResponse feedresponseObj = new FeedResponse();
        try{
            WSRequest   queryRequest=WS.url("https://news.google.com/news");
            CompletionStage<WSResponse> responsePromise = queryRequest
                    .setQueryParameter("q","srh")
                    .setQueryParameter("output","rss")
            .get();
            Document feedresponse =responsePromise.thenApply(WSResponse::asXml).toCompletableFuture().get();
            Node item = feedresponse.getFirstChild().getChildNodes().item(10);
            feedresponseobj.title=item.getChildNodes().item(0).getNodeValue();
            feedresponseobj.pubdate=item.getChildNodes().item(3).getNodeValue();
            feedresponseobj.description=item.getChildNodes().item(4).getNodeValue();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return feedresponseObj;
    }


}
