package br.com.urlshortenersample;

import com.google.common.hash.BloomFilter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Slf4j
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class GenerateKeyUtil {

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new Random();

    public static String generateShortKey(String url) throws NoSuchAlgorithmException {
        //Gerando hash da url com MD5
        final var hash = generateHash(url);

        //Transformando o hash em um inteiro
        final var bigInt = new BigInteger(1, hash);

        //Transformando o inteiro em base 62 com 2 caracteres
        return toBase62(bigInt);

    }

    private static byte[] generateHash(String url) throws NoSuchAlgorithmException {
        final var md = MessageDigest.getInstance("MD5");
        return md.digest(url.getBytes(StandardCharsets.UTF_8));
    }

    private static String toBase62(BigInteger value) {

        final var result = new StringBuilder();
        final var base = BigInteger.valueOf(62);

        while (value.compareTo(BigInteger.ZERO) > 0 && result.length() < 2) {

            int index = value.mod(base).intValue();
            result.append(BASE62.charAt(index));
            value = value.divide(base);

        }

        return result.reverse().toString();

    }

    public static void loadExistingKeysIntoBloomFilter(BloomFilter<String> bloomFilter, CacheMemory cache) {
        for (int i = 0; i < 10; i++) {
            String key;
            do {
                key = generateRandomKey();
            } while (bloomFilter.mightContain(key));

            log.info("[loadExistingKeysIntoBloomFilter] Adding key: {}", key);
            bloomFilter.put(key);
            cache.put(key, "https://github.com/vitormoschetti");
        }
    }

    private static String generateRandomKey() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            int index = RANDOM.nextInt(BASE62.length());
            sb.append(BASE62.charAt(index));
        }
        return sb.toString();
    }

}
