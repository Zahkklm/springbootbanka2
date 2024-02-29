package com.prj.calisma.constants;

import java.util.regex.Pattern;

public class constants {

    public static final String SUCCESS =
            "İşlem başarıyla tamamlandı.";
    public static final String NO_ACCOUNT_FOUND =
            "Hesap no ve Sort kod uyuşan bir hesap bulunamadı.";
    public static final String INVALID_SEARCH_CRITERIA =
            "Girilen sort kod ve hesap no istenen biçimde değil.";

    public static final String INSUFFICIENT_ACCOUNT_BALANCE =
            "Yetersiz bakiye";

    public static final String SORT_CODE_PATTERN_STRING = "[0-9]{2}-[0-9]{2}-[0-9]{2}";

    public static final String ACCOUNT_NUMBER_PATTERN_STRING = "[0-9]{8}";
    public static final Pattern SORT_CODE_PATTERN = Pattern.compile("^[0-9]{2}-[0-9]{2}-[0-9]{2}$");
    public static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile("^[0-9]{8}$");

    public static final String INVALID_TRANSACTION =
            "Hesap bilgisi geçersiz veya güvenlik sebeplerinden dolayı işlem reddedildi.";
    public static final String CREATE_ACCOUNT_FAILED =
            "Hesap oluşturma sırasında bilinmeyen bir hata meydana geldi.";
}
