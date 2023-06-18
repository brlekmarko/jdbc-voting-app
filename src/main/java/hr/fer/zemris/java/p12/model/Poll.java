package hr.fer.zemris.java.p12.model;


import java.util.Objects;

public class Poll {
	
	private long id;
	private String title;
	private String message;
	
	public Poll() {
	}
	

	public Poll(long id, String title, String message) {
		super();
		this.id = id;
		this.title = title;
		this.message = message;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	@Override
	public int hashCode() {
		return Objects.hash(id, message, title);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Poll other = (Poll) obj;
		return id == other.id && Objects.equals(message, other.message) && Objects.equals(title, other.title);
	}
	
	
}
