package ru.kslacker.banks.eventargs;

import java.time.LocalDateTime;
import ru.kslacker.banks.tools.eventhandling.EventArgs;

public record DateChangedEventArgs(LocalDateTime date) implements EventArgs {

}
