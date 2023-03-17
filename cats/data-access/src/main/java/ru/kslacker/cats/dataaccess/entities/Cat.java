package ru.kslacker.cats.dataaccess.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import ru.kslacker.cats.common.models.FurColor;

@Entity
@Table(name = "cats")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cat {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Setter(AccessLevel.NONE)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "date_of_birth", nullable = false)
	private LocalDate dateOfBirth;

	@Column(name = "breed", nullable = false)
	private String breed;

	@Column(name = "fur_color", nullable = false)
	@Enumerated(EnumType.STRING)
	private FurColor furColor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	private CatOwner owner;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "cat_friends",
		joinColumns = @JoinColumn(name = "cat_id"),
		inverseJoinColumns = @JoinColumn(name = "friend_id"))
	private List<Cat> friends;

	public Cat(String name, LocalDate dateOfBirth, String breed, FurColor furColor, CatOwner owner) {
		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.breed = breed;
		this.furColor = furColor;
		this.owner = owner;
		this.friends = new ArrayList<>();
	}

	public List<Cat> getFriends() {
		return Collections.unmodifiableList(friends);
	}

	public void addFriend(Cat cat) {

		if (equals(cat)) {
			throw new RuntimeException();
		}
		if (friends.contains(cat)) {
			throw new RuntimeException();
		}

		friends.add(cat);
		cat.addFriend(this);
	}

	public void removeFriend(Cat cat) {
		if (!friends.remove(cat)) {
			throw new RuntimeException();
		}
		cat.removeFriend(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
			return false;
		Cat cat = (Cat) o;
		return id != null && Objects.equals(id, cat.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
