package ClientWithSockets.client;

import java.net.URI;

import org.glassfish.tyrus.websockets.WebSocket;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
       final client user1 = new client(new URI("ws://localhost:8080/name"));
       System.out.println(user1.session.getId());
       user1.addMessageHandler(new client.MessageHandler() {
    	   public void handleMessage(String message) { 
			/*JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject();
			String userName = jsonObject.getString("user");
			if (! "bot".equals(userName))
			{
				user1.sendMessage(getMessage("Hello" + userName + ", How are you?"));
			}*/
    		  System.out.println(message);
			
		}
	});
       while(true)
       {
    	   /*user1.sendMessage(getMessage("Hi There!!"));*/
    	   if (user1.session == null)
    	   {
    		   System.out.println("null");
    	   }
    	   else
    	   {
    		   System.out.println("h");
    	   }
    	   user1.sendMessage("Hi There");
    	   //Thread.sleep(30000);
       }
    }
    
    /*private static String getMessage(String message) {
    	return Json.createObjectBuilder().add("user", "bot").add("message", message).build().toString();
    }*/
}
