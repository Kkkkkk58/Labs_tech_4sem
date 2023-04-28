package ru.kslacker.cats.services.mapping;

import lombok.experimental.UtilityClass;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.entities.UserAccount;
import ru.kslacker.cats.services.dto.CatOwnerDto;
import ru.kslacker.cats.services.dto.UserDto;

@UtilityClass
public class UserMapping {

	public static UserDto asDto(UserAccount userAccount) {
		CatOwner owner = userAccount.getOwner();
		CatOwnerDto catOwnerDto = (owner == null) ? null : CatOwnerMapping.asDto(userAccount.getOwner());
		return new UserDto(userAccount.getId(), userAccount.getUsername(), catOwnerDto);
	}

}
