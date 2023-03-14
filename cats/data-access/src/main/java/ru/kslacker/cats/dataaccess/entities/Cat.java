package ru.kslacker.cats.dataaccess.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ManyToAny;
import ru.kslacker.cats.dataaccess.models.Breed;
import ru.kslacker.cats.dataaccess.models.FurColor;

@Entity
@Table(name = "cats")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cat {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "date_of_birth", nullable = false)
	private LocalDate dateOfBirth;

	@Column(name = "breed", nullable = false)
	@Enumerated(EnumType.STRING)
	private Breed breed;

	@Column(name = "fur_color", nullable = false)
	@Enumerated(EnumType.STRING)
	private FurColor furColor;

	@ManyToOne(fetch = FetchType.LAZY)
	private CatOwner owner;

	@ManyToAny(fetch = FetchType.LAZY)
	private List<Cat> friends = new ArrayList<>();

	public List<Cat> getFriends() {
		return Collections.unmodifiableList(friends);
	}

	public Cat addFriend(Cat cat) {
		if (friends.contains(cat)) {
			throw new RuntimeException();
		}

		friends.add(cat);
		return cat;
	}

	public void removeFriend(Cat cat) {
		if (!friends.remove(cat)) {
			throw new RuntimeException();
		}
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
