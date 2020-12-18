package com.currency.exchange.rate.nbp;

public class NbpEndpointBuilder {

    public static String nbpUrl() {
        return "http://api.nbp.pl";
    }

    public static String nbpTableA() {
        return nbpUrl() + "/api/exchangerates/tables/a/";
    }

    public static String nbpTableB() {
        return nbpUrl() + "/api/exchangerates/tables/b/";
    }
}
