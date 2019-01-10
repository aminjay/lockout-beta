package com.LockOut.Server.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.LockOut.Server.Models.BaseMessage;
import com.LockOut.Server.Models.GroupMessage;
import com.LockOut.Server.Models.Message;
import com.LockOut.Server.Repositories.GroupMessageRepository;
import com.LockOut.Server.Repositories.MessageRepository;
import com.LockOut.Server.Utils.Util;
import com.google.gson.Gson;

@Component
public class SocketHandler extends TextWebSocketHandler{
	List sessions = new CopyOnWriteArrayList<Object>();
	List<MessagePoller> pollers = new ArrayList<MessagePoller>();
	@Autowired
	MessageRepository messageRepository;
	@Autowired
	GroupMessageRepository groupMessageRepository;
	
	protected class messageHandler implements Runnable
	{
		
		String message;
		WebSocketSession session;
		public messageHandler(String msg, WebSocketSession s)
		{
			session = s;
			message = msg;
		}

		@Override
		public void run() {
			ArrayList<String> tempList = (ArrayList<String>) Util.parser(message, ",");
			if (tempList.get(0).equals("MESSAGE")||tempList.get(0).equals("GROUPMESSAGE"))
			{
				message(tempList);
			}
			if (tempList.get(0).equals("USERNAME"))
			{
				username(tempList);
			}
			if (tempList.get(0).equals("c"))
			{
				try {
					session.close();
				}
				catch (IOException e)
				{
					return;
				}
			}
		}
		
		public void message(ArrayList<String> tempList)
		{
			BaseMessage msg = new Message();
			msg.setMessage(tempList.get(2));
			msg.setRecipient(tempList.get(1));
			msg.setSender(tempList.get(0));
			if (tempList.get(0).equals("MESSAGE"))
			{
				messageRepository.save( (Message) msg);
			}
			else
			{
				groupMessageRepository.save( (GroupMessage) msg);
			}
		}
		
		public void username(ArrayList<String> tempList)
		{
			MessagePoller poller = new MessagePoller(tempList.get(1), session);
			pollers.add(poller);
			poller.run();
		}

	}
	
	protected class MessagePoller implements Runnable
	{
		private WebSocketSession session;
		private String username;
		
		public MessagePoller(String name, WebSocketSession sess)
		{
			name = username;
			session = sess;
		}
		
		public void setUserName(String name)
		{
			username = name;
		}
		
		@Override
		public void run()
		{
			while (session.isOpen())
			{
				try
				{
					Thread.sleep(5000);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				ArrayList<Message> tempList = (ArrayList<Message>) messageRepository.findByRecipient(username);
				for (int i=0; i<tempList.size(); ++i)
				{
					if (!session.isOpen())
					{
						return;
					}
					if (!tempList.get(i).wasSent())
					{
						ArrayList<String> msg = new ArrayList<String>();
						msg.add(tempList.get(i).getSender());
						msg.add(tempList.get(i).getRecipient());
						msg.add(tempList.get(i).getMessage());
						String tempmsg = Util.listToString(msg, ",");
						TextMessage txt = new TextMessage(tempmsg);
						try {
							session.sendMessage(txt);
						}
						catch (IOException e)
						{
							System.out.println(e.getCause());
						return;
						}
					}
				}
		}	}
	}
	

	
	
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {
				Map value = new Gson().fromJson(message.getPayload(), Map.class);
				session.sendMessage(new TextMessage("Hello " + value.get("name") + " !"));
				//messageHandler thread = new messageHandler(value, session);
				//thread.run();
				session.sendMessage(new TextMessage("Message Sent"));
		
	}
	
	
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
	}
	

	
}
