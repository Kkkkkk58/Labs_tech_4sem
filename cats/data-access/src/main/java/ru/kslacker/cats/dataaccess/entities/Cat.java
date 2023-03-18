package ru.kslacker.cats.dataaccess.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.ToString.Exclude;
import org.hibernate.Hibernate;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.dataaccess.exceptions.CatException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "cats")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
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
	@Setter(AccessLevel.PACKAGE)
	@Exclude
	private CatOwner owner;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "cat_friends",
		joinColumns = @JoinColumn(name = "cat_id"),
		inverseJoinColumns = @JoinColumn(name = "friend_id"))
	@Exclude
	private List<Cat> friends;

	public Cat(String name, LocalDate dateOfBirth, String breed, FurColor furColor,
		CatOwner owner) {
		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.breed = breed;
		this.furColor = furColor;
		this.owner = owner;
		this.owner.addCat(this);
		this.friends = new ArrayList<>();
	}

	public List<Cat> getFriends() {
		return Collections.unmodifiableList(friends);
	}

	public void addFriend(Cat cat) {

		if (equals(cat)) {
			throw CatException.makingFriendsWithSelf();
		}
		if (friends.contains(cat)) {
			throw CatException.friendAlreadyExists(this, cat);
		}

		friends.add(cat);
		cat.friends.add(this);
	}

	public void removeFriend(Cat cat) {
		if (!friends.remove(cat) | !cat.friends.remove(this)) {
			throw CatException.friendNotFound(cat);
		}
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		Cat cat = (Cat) o;
		return id != null && Objects.equals(id, cat.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
