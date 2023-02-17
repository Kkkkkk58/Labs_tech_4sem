package ru.kslacker.banks.eventargs;

import ru.kslacker.banks.tools.eventhandling.EventArgs;

import java.time.LocalDateTime;

public record DateChangedEventArgs(LocalDateTime date) implements EventArgs {
}
