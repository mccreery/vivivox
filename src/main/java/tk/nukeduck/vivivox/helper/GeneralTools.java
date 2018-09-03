package tk.nukeduck.vivivox.helper;

import java.util.List;
import java.util.function.Predicate;

public class GeneralTools {
	public static <T> int count(List<T> values, Predicate<T> predicate) {
		return (int)values.stream().filter(predicate).count();
	}

	public static int count(List<Boolean> values) {
		return (int)count(values, Boolean::booleanValue);
	}

	public static int count(boolean... values) {
		int count = 0;

		for(boolean value : values) {
			if(value) ++count;
		}
		return count;
	}
}
