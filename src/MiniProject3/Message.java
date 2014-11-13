package MiniProject3;

enum Type { PUT, GET, PRESENTATION }

public class Message implements java.io.Serializable {
	Type type;
	Object content;
	static int id=0;
	
	public Message(Type type,Object content)
	{	
		id++;
		this.type=type;
		this.content=content;
	}
	
	public String toString() {
		return content.toString();
	}
}

