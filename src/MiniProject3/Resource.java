package MiniProject3;

public class Resource implements java.io.Serializable {

	int id;
	String element;
	
	public Resource(int id, String element){
		id = this.id;
		element = this.element;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	
	
}
