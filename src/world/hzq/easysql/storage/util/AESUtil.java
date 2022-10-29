package world.hzq.easysql.storage.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/*
 * AES对称加密和解密
 * 如果自己指定加密规则,则解密是需要同样的解密规则
 */
public class AESUtil {
    //默认加密规则
    private static final String DEFAULT_RULE = "da0ab6a2-ed6f-4c8e-ab88-285815fa34c4";
    private static final String DEFAULT_CHARSET = "utf-8";
    //默认随机源位数
    private static final int DEFAULT_BIT = 128;
    //密钥生成器
    private static KeyGenerator keygen;

    //重载方法列表
    public static String encode(String content){
        return encode(DEFAULT_RULE,DEFAULT_CHARSET,content);
    }

    public static String encode(String charset,String content){
        return encode(DEFAULT_RULE,charset,content);
    }

    public static String decode(String content){
        return decode(DEFAULT_RULE,DEFAULT_CHARSET,content);
    }

    public static String decode(String charset,String content){
        return decode(DEFAULT_RULE,charset,content);
    }

    static {
        try {
            keygen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /*
     * 加密
     * 1.构造密钥生成器
     * 2.根据encodeRule规则初始化密钥生成器
     * 3.产生密钥
     * 4.创建和初始化密码器
     * 5.内容加密
     * 6.返回字符串
     */
    public static String encode(String encodeRule,String charset,String content){
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            //2.根据encodeRule规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            keygen.init(DEFAULT_BIT, new SecureRandom(encodeRule.getBytes()));
            //3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte [] raw = original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES");
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte [] byte_encode = content.getBytes(charset);
            //9.根据密码器的初始化方式--加密：将数据加密
            byte [] byte_AES=cipher.doFinal(byte_encode);
            //10.将加密后的数据转换为字符串
            //这里用Base64Encoder中会找不到包
            //解决办法：
            //在项目的Build path中先移除JRE System Library，再添加库JRE System Library，重新编译后就一切正常了。
            //11.将字符串返回
            return new BASE64Encoder().encode(byte_AES);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //如果有错就返回null
        return null;
    }

    /*
     * 解密
     * 解密过程：
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数组
     * 3.将加密内容解密
     */
    public static String decode(String encodeRule,String charset,String content){
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            //2.根据encodeRule规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            keygen.init(DEFAULT_BIT, new SecureRandom(encodeRule.getBytes()));
            //3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte [] raw = original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES");
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.DECRYPT_MODE, key);
            //8.将加密并编码后的内容解码成字节数组
            byte [] byte_content = new BASE64Decoder().decodeBuffer(content);
            /*
             * 解密
             */
            byte [] byte_decode = cipher.doFinal(byte_content);
            return new String(byte_decode,charset);
        } catch (NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | IOException | InvalidKeyException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        //如果有错就返回null
        return null;
    }
}