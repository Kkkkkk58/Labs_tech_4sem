package ru.kslacker.cats.dataaccess.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
import lombok.ToString;
import org.hibernate.Hibernate;
import ru.kslacker.cats.dataaccess.exceptions.CatOwnerException;

@Entity
@Table(name = "cat_owners")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
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
	@Setter(AccessLevel.PROTECTED)
	@ToString.Exclude
	private List<Cat> cats = new ArrayList<>();

	public CatOwner(String name, LocalDate dateOfBirth) {
		this.name = name;
		this.dateOfBirth = dateOfBirth;
	}

	public List<Cat> getCats() {
		return Collections.unmodifiableList(cats);
	}

	public void addCat(Cat cat) {
		if (cats.contains(cat)) {
			return;
		}

		cats.add(cat);
		if (cat.getOwner() != this) {
			cat.setOwner(this);
		}
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
