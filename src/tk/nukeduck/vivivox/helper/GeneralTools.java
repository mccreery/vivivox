package tk.nukeduck.vivivox.helper;

public class GeneralTools {
	public static <T> int indexOf(T[] haystack, T needle) {
		for (int i = 0; i < haystack.length; i++) {
			if (haystack[i] != null && haystack[i].equals(needle) || needle == null && haystack[i] == null) return i;
		}
		return -1;
	}
}