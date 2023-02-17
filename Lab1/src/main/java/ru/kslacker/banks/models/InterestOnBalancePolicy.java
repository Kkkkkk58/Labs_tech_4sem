package ru.kslacker.banks.models;

import java.util.Collection;
import java.util.Comparator;
import lombok.Getter;
import lombok.experimental.ExtensionMethod;
import ru.kslacker.banks.exceptions.InterestOnBalancePolicyException;
import ru.kslacker.banks.tools.extensions.StreamExtensions;

@Getter
@ExtensionMethod(StreamExtensions.class)
public class InterestOnBalancePolicy {

	private final Collection<InterestOnBalanceLayer> layers;

	public InterestOnBalancePolicy(Collection<InterestOnBalanceLayer> layers) {
		if (haveIntersections(layers)) {
			throw InterestOnBalancePolicyException.layersWithIntersectionsByInitialBalance();
		}

		this.layers = layers.stream()
			.sorted(Comparator.comparing(InterestOnBalanceLayer::requiredInitialBalance))
			.toList();
	}

	private static boolean haveIntersections(Collection<InterestOnBalanceLayer> layers) {
		return layers.stream().distinctBy(InterestOnBalanceLayer::requiredInitialBalance).count() != layers.size();
	}
}
