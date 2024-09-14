package personalproject.social.network.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "friends", uniqueConstraints = @UniqueConstraint(columnNames = { "sender_id", "receiver_id" }))
public class Friends {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "sender_id", referencedColumnName = "id")
	private User sender;

	@ManyToOne
	@JoinColumn(name = "receiver_id", referencedColumnName = "id")
	private User receiver;

	@Column(name = "Request_date")
	private String requestDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private EFriendStatus status = EFriendStatus.PENDING;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public EFriendStatus getStatus() {
		return status;
	}

	public void setStatus(EFriendStatus status) {
		this.status = status;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

}
