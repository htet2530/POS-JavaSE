package com.jdc.util;

public class TaxRateSetting {
	public static int getTaxRate() {
		return Integer.parseInt(PosSettingUtil.get("pos.tax.rate"));
	}
}
