package ru.kslacker.cats.dataaccess.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

@Entity
@Table(name = "cat_owners")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CatOwner {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@OneToMany(fetch = FetchType.LAZY)
	private List<Cat> cats;

	public Cat addCat(Cat cat) {
		if (cats.contains(cat)) {
			throw new RuntimeException();
		}

		cats.add(cat);
		return cat;
	}

	public void removeCat(Cat cat) {
		if (!cats.remove(cat)) {
			throw new RuntimeException();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
			return false;
		CatOwner catOwner = (CatOwner) o;
		return id != null && Objects.equals(id, catOwner.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
