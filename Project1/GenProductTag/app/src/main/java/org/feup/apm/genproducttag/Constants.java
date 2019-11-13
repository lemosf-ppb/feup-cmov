package org.feup.apm.genproducttag;

class Constants {
    static final String privateKeySupermarket = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAzlUdSX2dxJAhZg+x\n" +
            "CcBi/qMcPOYc9N1wksaznKq9/m0PIwz7olDrYv9BUBTaGEGuVF7HJd6NYEC5BNLI\n" +
            "cPFtMQIDAQABAkB3TmHz5629RfX59OLSl6rmQFyInipqMEzyofCji1r27QD9eUQa\n" +
            "OP5GeCShqpAtD/2hzSV9o/XQCjkB8QLMN56RAiEA6T7qmG9V6zUu4Ejh1WOht3qp\n" +
            "yVQ8DNXZUM4kWZEBZz0CIQDidhCp9bUVps8fLve4NykRwk6ylY/Oepv8IZBDx6Kd\n" +
            "BQIgXX6ozt2iFRxGZAUS8VR0mHWE5XzsbUCzUZ6wF272nwkCIQCaWHO1EvUPw7go\n" +
            "/b7yfA3uv6sZhlwUE3ba2hlUSO0o7QIhAJWOauMu0+kMrk3FnlLw2ytyseXfLPb5\n" +
            "PSPQ30EqdRR0\n" +
            "-----END PRIVATE KEY-----";

    static final String publicKeySupermarket = "-----BEGIN PUBLIC KEY-----\n" +
            "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAM5VHUl9ncSQIWYPsQnAYv6jHDzmHPTd\n" +
            "cJLGs5yqvf5tDyMM+6JQ62L/QVAU2hhBrlRexyXejWBAuQTSyHDxbTECAwEAAQ==\n" +
            "-----END PUBLIC KEY-----";

    static final String ENC_ALGO = "RSA/NONE/PKCS1Padding";
    static final String PUBLIC_KEY_BEGIN = "-----BEGIN PUBLIC KEY-----";
    static final String PUBLIC_KEY_END = "-----END PUBLIC KEY-----";
    static final String PRIVATE_KEY_BEGIN = "-----BEGIN PRIVATE KEY-----";
    static final String PRIVATE_KEY_END = "-----END PRIVATE KEY-----";

    static int tagId = 0x41636D65;        // equal to "Acme"
}
