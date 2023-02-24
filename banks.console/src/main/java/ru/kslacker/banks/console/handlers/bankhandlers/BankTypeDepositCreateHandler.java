package ru.kslacker.banks.console.handlers.bankhandlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.experimental.ExtensionMethod;
import ru.kslacker.banks.bankaccounts.accounttypes.api.DepositAccountType;
import ru.kslacker.banks.console.extensions.StringExtensions;
import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.entities.api.NoTransactionalBank;
import ru.kslacker.banks.models.InterestOnBalanceLayer;
import ru.kslacker.banks.models.InterestOnBalancePolicy;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.services.api.CentralBank;
import ru.kslacker.banks.tools.extensions.StreamExtensions;

@ExtensionMethod({StreamExtensions.class, StringExtensions.class})
public class BankTypeDepositCreateHandler extends HandlerImpl {

	private final CentralBank centralBank;
	private final BufferedReader reader;
	private final BufferedWriter writer;


	public BankTypeDepositCreateHandler(CentralBank centralBank, BufferedReader reader,
		BufferedWriter writer) {
		super("create");
		this.centralBank = centralBank;
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		NoTransactionalBank bank = getBank();
		DepositAccountType type = getType(bank);

		writer.write("Successfully created deposit type " + type.getId());
		writer.newLine();
		writer.flush();
	}

	private NoTransactionalBank getBank() throws IOException {
		writer.write("Enter bank id: ");
		writer.flush();
		UUID bankId = UUID.fromString(reader.readLine());

		return centralBank
			.getBanks()
			.stream()
			.single(b -> b.getId().equals(bankId));
	}

	private DepositAccountType getType(NoTransactionalBank bank) throws IOException {
		InterestOnBalancePolicy interestOnBalancePolicy = getInterestOnBalancePolicy();

		writer.write("Enter deposit term: ");
		writer.flush();
		Period term = Period.parse(reader.readLine());
		writer.write("Enter interest calculation period: ");
		writer.flush();
		Period period = Period.parse(reader.readLine());

		return bank.getAccountTypeManager().createDepositAccountType(term, interestOnBalancePolicy, period);
	}

	private InterestOnBalancePolicy getInterestOnBalancePolicy() throws IOException {
		List<InterestOnBalanceLayer> interestOnBalancePolicyLayers = new ArrayList<>();
		writer.write("Enter layer: ");
		writer.flush();
		String input = reader.readLine();
		while (!input.isEmpty())
		{
			String[] values = input.split(" ");
			if (values.length != 3) {
				throw new IllegalArgumentException();
			}

			MoneyAmount requiredBalance = String.join(" ", values[0], values[1]).toMoneyAmount();
			BigDecimal interest = new BigDecimal(values[2]);
			InterestOnBalanceLayer layer = new InterestOnBalanceLayer(requiredBalance, interest);
			interestOnBalancePolicyLayers.add(layer);
			writer.write("Enter layer: ");
			writer.flush();
			input = reader.readLine();
		}

		return new InterestOnBalancePolicy(interestOnBalancePolicyLayers);
	}

}
