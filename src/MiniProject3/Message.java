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


	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public static int getId() {
		return id;
	}

	public static void setId(int id) {
		Message.id = id;
	}
	
	public String toString() {
		return content.toString();
	}

}

