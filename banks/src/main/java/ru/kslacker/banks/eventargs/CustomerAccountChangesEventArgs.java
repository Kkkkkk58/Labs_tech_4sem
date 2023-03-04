package ru.kslacker.banks.eventargs;

import ru.kslacker.banks.models.Message;
import ru.kslacker.banks.tools.eventhandling.EventArgs;

public record CustomerAccountChangesEventArgs(Message message) implements EventArgs {

}
