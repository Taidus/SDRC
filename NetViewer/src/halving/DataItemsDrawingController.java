package halving;

import java.awt.Color;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import netViewer.TwoSitesNodeHalving;


public class DataItemsDrawingController {

	private static Map<TwoSitesNodeHalving, AttributedString> list = new HashMap<>();
	
	public static AttributedString getStringToDisplay(TwoSitesNodeHalving relativeTo) {
		return list.get(relativeTo);
	}
	
	public static void update(TwoSitesNodeHalving caller) {
		List<Integer> tooSmall = caller.getLeftDiscarded();
		List<Integer> tooBig = caller.getRightDiscarded();
		List<Integer> valid = caller.getData();

		Collections.sort(tooSmall);
		Collections.sort(valid);
		Collections.sort(tooBig);

		StringBuilder stringToDisplay = new StringBuilder();
		stringToDisplay.append("[");
		int firstCut;
		int secondCut;
		for (int i = 0; i < tooSmall.size(); i++) {
			stringToDisplay.append(formatNumber(tooSmall.get(i)) + " ");
		}
		firstCut = stringToDisplay.length();
		if (!tooBig.isEmpty()) {
			for (int i = 0; i < valid.size(); i++) {
				stringToDisplay.append(formatNumber(valid.get(i)) + " ");
			}
			secondCut = stringToDisplay.length();
			for (int i = 0; i < tooBig.size() - 1; i++) {
				stringToDisplay.append(formatNumber(tooBig.get(i)) + " ");
			}

			stringToDisplay.append(formatNumber(tooBig.get(tooBig.size() - 1))
					+ "]");
		} else {
			for (int i = 0; i < valid.size() - 1; i++) {
				stringToDisplay.append(formatNumber(valid.get(i)) + " ");
			}
			secondCut = stringToDisplay.length();
			stringToDisplay.append(formatNumber(valid.get(valid.size() - 1))
					+ "]");
		}

		AttributedString formattedString = new AttributedString(stringToDisplay.toString());
		if (!tooSmall.isEmpty()) {
			formattedString.addAttribute(TextAttribute.FOREGROUND, Color.red, 1, firstCut);
		}
		if (!tooBig.isEmpty()) {
			formattedString.addAttribute(TextAttribute.FOREGROUND, Color.red, secondCut,
					stringToDisplay.length() - 1);
		}
		
		list.put(caller, formattedString);
	}
	
	
	private static String formatNumber(Integer toFormat) {
		if (Integer.MAX_VALUE == toFormat) {
			return "Inf";
		}
		if (Integer.MIN_VALUE == toFormat) {
			return "-Inf";
		}
		return toFormat.toString();
	}
}
