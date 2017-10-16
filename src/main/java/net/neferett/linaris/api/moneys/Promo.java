package net.neferett.linaris.api.moneys;

import java.util.Date;

public class Promo {
    public Date end;
    public double multiply;
    public String message = null;

    public Promo(String promoData) {
        String[] split = promoData.split(":");
        multiply = Double.parseDouble(split[0]);
        end = new Date();
        end.setTime(Long.parseLong(split[1]));
        if (split.length > 2)
            message = split[2];
    }

    public Promo() {

    }

    public String toString() {
        return multiply + ":" + end.getTime() + ((message != null) ? ":" + message : "");
    }
}
