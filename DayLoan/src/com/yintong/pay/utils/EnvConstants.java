
package com.yintong.pay.utils;

public class EnvConstants {
    private EnvConstants() {
    }

    /**
     * TODO 商户号，商户MD5 key 配置。本测试Demo里的“PARTNER”；强烈建议将私钥配置到服务器上，以免泄露。“MD5_KEY”字段均为测试字段。正式接入需要填写商户自己的字段
     */

    public static final String PARTNER = "201707111001880525";

    // 商户（RSA）私钥 TODO 强烈建议将私钥配置到服务器上，否则有安全隐患
//    public static final String RSA_PRIVATE =
//    "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAOdpx62+CE7OJLtD"+
//    "yZ1dSclFwCJQqhH6Lxn5p/eBqdW9DARcOxVwLq59szrxtcdcbY8Cn0hnDWH506ql"+
//    "QcaGzneWxmk05D/pxUFB/91RZ9LFpNb2MGpI8oL7TezjNnTO85lbges6wZY/N57+"+
//    "Y4TDdtXJMkThcJcgSLaiEHFpxoQdAgMBAAECgYBIz1NwKvZaNSoBcZah2JQ6q6OE"+
//    "KHUaKule9toOWdGh2lVJmetNF3rYk9wsk5hqHX6rHFwZX7MqPHyImG5VlFypUqKF"+
//    "Je3JGaxTEAH6N1UC1YgndMZt7dI4U7PPaoFpqBoSvoi6aBL1VbCCp5SG1725KWFJ"+
//    "i3Fw+W9+IsduVRXrgQJBAPo4KYMcyLD/yCnBNjWSBKaU4V2g6/FER1ONfV9Zat+a"+
//    "i5Vb0dAt4+D/n4f1pBBIPh+duJPEH653EbhvkaMR1XUCQQDswmUiBnKCv3R43wdV"+
//    "pk2Ex5d6Szn09CDdSLcMEmv+5Rxd3kdK7R4sSXlgyZIVgLhAOErKx/kr/YymaxP2"+
//    "hpcJAkEAw6z18b2pyJlOvDCHpx8YmfjlwSWePeAqc1G70LwJkvG15MiYbNfmVEcz"+
//    "R8y2T7FfO7RI6u295N7isbw2RTMmHQJBAK38mVdsx3//9DbTJ0+w26ylBFNRrvCs"+
//    "6u3S/vl3HDf/5F2y+E8+e3ruL62J07nLkxtWgtgMJrdGJ7MwY0Y6WAkCQQCyKhzo"+
//    "kdz50T/GjtoAwPXmz3C1oIHfHUTVeXhHbiIcmLxDXu+HXRlR7UzZefHaIW/BURfu"+
//    "LQRPRgUroht+V1vp";
    public static final String RSA_PRIVATE ="" +
    "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALPoMsWGg5gIyk/z"+
    "Wa4wcMok3Z4fW0A1ZBnPLY3wmJ7FCzyxoTGpCIvWXtT4A73HPiZN7Qzqj8HxM0lB"+
    "/jyy+WHtuyjSwG2cKO+IGTGI8LAV58gnV0M9PrzOBOKE6lzQF7a3L5R9u4qS7NHH"+
    "xGTAbYAdffzyI479LawQgFE37BVBAgMBAAECgYBPOhlpzUQUZwKZVOSQhjqVesiy"+
    "AsMPsrODfi5kjKjZepLpRpxjHzppQp1+kj4rjBu9iKG1B3MJiKv6Pfq1Rmf1z5B0"+
   "g+ycsuvcu8WFGVmIPdNhsiybf48aKf+1JB1A1+zFXcrsiLNGd475PCQNMcfVvq5u"+
    "6E+ir2yC6tPYY4U8hQJBAOrFoiT8Y9+go6QOsxihOum/tQ51fC4IwWoWC0tXKhxt"+
    "GZI/SgAiljqCKst+kDq6wTlyE84dxZ9Oi+jk+NTtOwMCQQDELJUEs1SBtbSfkge7"+
    "Nnlowqq+kFpa10T+WoyTYZsOlqLDT80kRLRrhxwB9v+kLPZOfrqcRQ6bLhNKQWsS"+
    "oHlrAkAP3a1YjIn/We7VLn0iA/tkQqVsxbnPrp3LmpPG0qww4ZqhzI8mtS+r4pIb"+
    "0IDUxzw5sqDuBAsP+hHwelDqquGbAkAUnv8XHGawr9IJyAbqBgLjITtjhrcIv4Iw"+
    "HoKSZ3suIGWBlFzjCBnTB8PI7RbYQiWuAKJLFPNBGqnKb2/66EV7AkBxlcWYW0+8"+
    "dqwun9gtueYYtnQWD/+aKD81KK2/MttY9+KVK8f3ksw8D0cGy85AoyzrivQEWNdd"+
    "4Pvqll9NWYhS";
    // 银通支付（RSA）公钥
    public static final String RSA_YT_PUBLIC =
    		"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDnacetvghOziS7Q8mdXUnJRcAi"+
    		"UKoR+i8Z+af3ganVvQwEXDsVcC6ufbM68bXHXG2PAp9IZw1h+dOqpUHGhs53lsZp"+
    		"NOQ/6cVBQf/dUWfSxaTW9jBqSPKC+03s4zZ0zvOZW4HrOsGWPzee/mOEw3bVyTJE"+
    		"4XCXIEi2ohBxacaEHQIDAQAB";

}
