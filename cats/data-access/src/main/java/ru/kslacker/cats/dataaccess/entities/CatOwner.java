package ru.kslacker.cats.dataaccess.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import ru.kslacker.cats.dataaccess.exceptions.CatOwnerException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "cat_owners")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CatOwner {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Setter(AccessLevel.NONE)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Cat> cats;

	public CatOwner(String name, LocalDate dateOfBirth) {
		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.cats = new ArrayList<>();
	}

	public List<Cat> getCats() {
		return Collections.unmodifiableList(cats);
	}

	public void addCat(Cat cat) {
		if (cats.contains(cat)) {
			throw CatOwnerException.catAlreadyExists(this, cat);
		}

		cats.add(cat);
		cat.setOwner(this);
	}

	public void removeCat(Cat cat) {
		if (!cats.remove(cat)) {
			throw CatOwnerException.catNotFound(this, cat);
		}
		cat.setOwner(null);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		CatOwner catOwner = (CatOwner) o;
		return id != null && Objects.equals(id, catOwner.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
