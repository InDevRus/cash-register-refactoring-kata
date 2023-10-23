package cashregister;

import cashregister.cost.format.CostFormats;
import cashregister.credit.CreditsCounter;
import cashregister.credit.CreditsCounterByProductType;
import cashregister.discount.Discounter;
import cashregister.discount.DiscounterByProductType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;

import java.text.MessageFormat;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@RequiredArgsConstructor
public class Customer {
    @Getter(AccessLevel.PUBLIC)
    private final String name;

    private final ConcurrentMap<Product, OrderEntry> orderEntries = new ConcurrentHashMap<>();

    // TODO: Injection
    private final Discounter discounter = new DiscounterByProductType();

    // TODO: Injection
    private final CreditsCounter creditsCounter = new CreditsCounterByProductType();

    public void addProduct(Product product, int requiredAmount) {
        if (requiredAmount <= 0) {
            throw new IllegalArgumentException("Amount is expected to be positive.");
        }

        orderEntries.put(product, new OrderEntry(product, requiredAmount));
    }

    public void dropProduct(Product product) {
        orderEntries.remove(product);
    }

    private static final String DEFAULT_STATEMENT_DELIMITER = "---" + System.lineSeparator();
    private static final int DEFAULT_STATEMENT_CONTENT_INDENTATION_SIZE = 4;
    private static final String DEFAULT_STATEMENT_CONTENT_INDENTATION = " ".repeat(DEFAULT_STATEMENT_CONTENT_INDENTATION_SIZE);

    public String formStatement() {
        return formStatement(DEFAULT_STATEMENT_DELIMITER);
    }

    public String formStatement(@NonNull String delimiter) {
        var totalCost = Money.zero(Product.DEFAULT_UNIT);
        var accumulatedCredits = 0;
        var itemsCount = 0;

        var prefix = MessageFormat.format("Statement for {0}{1}", getName(), System.lineSeparator());
        var result = new StringJoiner(delimiter, prefix, "");

        for (OrderEntry currentEntry : orderEntries.values()) {
            var currentCost = discounter.findDiscount(currentEntry);

            accumulatedCredits += creditsCounter.countCredits(currentEntry);

            var statementLine = MessageFormat.format("{0}{1}: {2} x {3} = {4} {5}",
                    DEFAULT_STATEMENT_CONTENT_INDENTATION,
                    getName(),
                    currentEntry.quantity(),
                    CostFormats.DEFAULT_WITHOUT_SYMBOL.format(currentEntry.product().price()),
                    CostFormats.DEFAULT_FORMAT.format(currentCost),
                    System.lineSeparator());
            result.add(statementLine);

            // show figures for each order line
            itemsCount += currentEntry.quantity();
            totalCost = totalCost.add(currentCost);
        }

        result.add(MessageFormat.format("Number of items: {0}{1}", itemsCount, System.lineSeparator()));
        result.add(MessageFormat.format("Credits accumulated in this purchase: {0}{1}", accumulatedCredits, System.lineSeparator()));
        result.add(MessageFormat.format("Total amount: {0} {1}",
                CostFormats.DEFAULT_FORMAT.format(totalCost),
                System.lineSeparator()));

        return result.toString();
    }
}