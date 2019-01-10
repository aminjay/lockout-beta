package stest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;



@Component
public class SocketHandler extends TextWebSocketHandler {
	List sessions = new CopyOnWriteArrayList<Object>();
	
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
	throws InterruptedException, IOException {
		Map value = new Gson().fromJson(message.getPayload(), Map.class);
		session.sendMessage(new TextMessage("Hello " + value.get("name")));
		
	}
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
	}
}
