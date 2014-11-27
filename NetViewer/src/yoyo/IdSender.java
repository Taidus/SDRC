package yoyo;

public interface IdSender {
	
	public void whenAllResponsesReceived();
	public void sendMessageToOutgoingLinks(YoyoMessage message);

}
