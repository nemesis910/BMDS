package MiniProject3;

enum Type { PUT, GET, PRESENTATION }

public class Message implements java.io.Serializable {
	Type type;
	Object content;
	
	public Message(Type type,Object content)
	{		
		this.type=type;
		this.content=content;
	}
}

