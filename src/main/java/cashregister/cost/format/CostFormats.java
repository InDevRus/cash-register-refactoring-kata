package cashregister.cost.format;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.javamoney.moneta.format.AmountFormatParams;
import org.javamoney.moneta.format.CurrencyStyle;

import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CostFormats {
    public static final MonetaryAmountFormat DEFAULT_FORMAT = MonetaryFormats.getAmountFormat(
            AmountFormatQueryBuilder.of(Locale.ENGLISH)
                    .set(CurrencyStyle.SYMBOL)
                    .set(AmountFormatParams.PATTERN, "###,###.## ¤")
                    .build());

    public static final MonetaryAmountFormat DEFAULT_WITHOUT_SYMBOL = MonetaryFormats.getAmountFormat(
            AmountFormatQueryBuilder.of(Locale.ENGLISH)
                    .set(CurrencyStyle.SYMBOL)
                    .set(AmountFormatParams.PATTERN, "###,###.##")
                    .build());
}
