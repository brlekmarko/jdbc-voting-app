package hr.fer.zemris.java.p12.model;


import java.util.Objects;

public class PollOption {
	
	private long id;
	private String optionTitle;
	private String optionLink;
	private long pollID;
	private long votesCount;
	
	public PollOption() {
	}

	public PollOption(long id, String optionTitle, String optionLink, long pollID, long votesCount) {
		super();
		this.id = id;
		this.optionTitle = optionTitle;
		this.optionLink = optionLink;
		this.pollID = pollID;
		this.votesCount = votesCount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOptionTitle() {
		return optionTitle;
	}

	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	public String getOptionLink() {
		return optionLink;
	}

	public void setOptionLink(String optionLink) {
		this.optionLink = optionLink;
	}

	public long getPollID() {
		return pollID;
	}

	public void setPollID(long pollID) {
		this.pollID = pollID;
	}

	public long getVotesCount() {
		return votesCount;
	}

	public void setVotesCount(long votesCount) {
		this.votesCount = votesCount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, optionLink, optionTitle, pollID, votesCount);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PollOption other = (PollOption) obj;
		return id == other.id && Objects.equals(optionLink, other.optionLink)
				&& Objects.equals(optionTitle, other.optionTitle) && pollID == other.pollID
				&& votesCount == other.votesCount;
	}
	
	
}
