package com.cn21.onekit.lib.utils;

import com.cn21.onekit.lib.BuildConfig;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by Administrator on 2017/8/28.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RSAUtilsTest {
    private String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDAPl44qWrBCkXC6B6ENB5nd3/J\n" +
            "sJLSkgv+ad3qx+oe8h38KByRxEXVSi1S4BickPti4z1in5QMh6bx4qqpLEvgmiuY\n" +
            "QVKc2tBdePeY5YVoeiP/J2SCqai4hhC1prAI407XhAkTuwp2/pykDnt18BjXRKR+\n" +
            "Ca/H7r9N9ozORP/ghQIDAQAB";
    private String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMA+XjipasEKRcLo\n" +
            "HoQ0Hmd3f8mwktKSC/5p3erH6h7yHfwoHJHERdVKLVLgGJyQ+2LjPWKflAyHpvHi\n" +
            "qqksS+CaK5hBUpza0F1495jlhWh6I/8nZIKpqLiGELWmsAjjTteECRO7Cnb+nKQO\n" +
            "e3XwGNdEpH4Jr8fuv032jM5E/+CFAgMBAAECgYBdcHL/e2kF9uY9e/EJNQPqKm0U\n" +
            "LRzP+GiX+gEBErY64FeurhhkyU9gAjPRalbMErP9NpIpk4K0LGL6gr+MhplB9BEW\n" +
            "CfkBSFs82b2i8eR4NKLUL1pJWyM6DurAOZAobpSGDKyFS8iZQo9XjVT2D02IlnCd\n" +
            "7d/cOdqtXkL5ul9pwQJBAOe9NUv7ddaaKUTuRxhVfq7C0LXMAw4gIE9oD8zeGXG8\n" +
            "xalx6Ert9b+IEx20IjJbCA43KbgsiFy96VtpuVTludkCQQDUXqZgozvFssdUqU7Z\n" +
            "CbTGlNnAetZkYZttUVQNF0aWWtIU0dJPEL8zJipjIuK+VuSKyUxLOs4ULRYpHCub\n" +
            "RiSNAkEAjQbFR385QCdDFDLZy0JEPQBL2UbVWSRN5WUTsH49B810kODEF81mr2+V\n" +
            "Co+T/AcQCO6jEfTv61ibicj2Q2cmSQJBANKE+R028xupe5tbMoRH8OIh0DHUEw7z\n" +
            "CyY0xsMrXZfSdAqbbIVgLok/vikD/g19CZTiszSoGH07rIoYGMoVoEkCQQCe7N5N\n" +
            "o2NGyBlvkrhIs9/wCpMuNJnDrUSAKwrrZhxbtK0rNy70VceFAM+kalZK8mwgTwR3\n" +
            "WjnRU+GqxVlcmtas";

    private String sourceStr = "onekit sdk at 2017/08/28 by mengyuan zhang @ 21cn";

    private String miwen = "BR9GT1RpYhjVuMMNsoMNX2WHgeAca0EwYVMBkehLUhj9yjLqQ2FPcuHgPIaP7j924rKk0RxiDBZsm7dxfBUuXdhAKuv3TtNoi9qM+ALKixicAXQelgasT+z1TwJHBMHQxILnpnJSvKBM5CTP48lVouE/cVK2uf573/Zsxeg9L/E=\n";

    /**
     * 测试私钥加密，公钥解密（一般公钥加密私钥解密）
     */
    @Test
    public void testEncrypt() throws Exception {
        PrivateKey priKey = RSAUtils.getPrivateKey(privateKey);
        Assert.assertNotNull(priKey);
        String str = RSAUtils.encryptWithBase64(sourceStr, priKey);
        System.out.println(str);
    }

    @Test
    public void testDecrypt() throws Exception {
        PublicKey pubKey = RSAUtils.getPublicKey(publicKey);
        Assert.assertNotNull(pubKey);
        String str = RSAUtils.decryptWithBase64(miwen, pubKey);
        Assert.assertEquals(str, sourceStr);
    }
}
