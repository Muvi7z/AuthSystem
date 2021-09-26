package sample.Data;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Locale;
import java.util.Random;

public class Password {

    public static String hashingPass(String pass, byte[] salt){
        StringBuilder text = new StringBuilder();
        StringBuilder saltStr = new StringBuilder();

        try {

            KeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            for (byte b : hash) {
                String s = Integer.toHexString(0xff & b);

                s = (s.length() == 1) ? "0" + s : s;
                text.append(s);
            }
            return text.toString();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    public static Boolean checkingPass(String pass){
        String sym1 = "qwertyuiop";
        String sym2 = "asdfghjkl";
        String sym3 = "zxcvbnm";
        String num1 = "1234567890";
        pass = pass.toLowerCase();
        boolean isBadPass = false;
        if(pass.length()>0) {
            for (int i = 1; i < pass.length(); i++) {
                if (pass.charAt(i)==pass.charAt(i-1)){
                    return true;
                }
                int temp = 0;
                if(i>=2) {
                    if (sym1.contains(String.valueOf(pass.charAt(i)))) {
                        int id = sym1.indexOf(String.valueOf(pass.charAt(i)));
                        if (id >= 2) {
                            if (sym1.charAt(id - 2) == pass.charAt(i - 2) && sym1.charAt(id - 1) == pass.charAt(i - 1) ) {
                                return true;
                            }
                        }
                    }
                    if (sym2.contains(String.valueOf(pass.charAt(i)))) {
                        int id = sym2.indexOf(String.valueOf(pass.charAt(i)));
                        if (id >= 2) {
                            if (sym2.charAt(id - 2) == pass.charAt(i - 2) && sym2.charAt(id - 1) == pass.charAt(i - 1) ) {
                                return true;
                            }
                        }

                    }
                    if (sym3.contains(String.valueOf(pass.charAt(i)))) {
                        int id = sym3.indexOf(String.valueOf(pass.charAt(i)));
                        if (id >= 2) {
                            if (sym3.charAt(id - 2) == pass.charAt(i - 2) && sym3.charAt(id - 1) == pass.charAt(i - 1) ) {
                                return true;
                            }
                        }

                    }
                    if (num1.contains(String.valueOf(pass.charAt(i)))) {
                        int id = num1.indexOf(String.valueOf(pass.charAt(i)));
                        if (id >= 2) {
                            if (num1.charAt(id - 2) == pass.charAt(i - 2) && num1.charAt(id - 1) == pass.charAt(i - 1) ) {
                                return true;
                            }
                        }

                    }
                }
            }
        }
        return isBadPass;
    }

    public static String generateText(String sym, int n){
        final Random random = new Random();
        String symUpp= "";

        int old = 0;
        for (int i =0; i<n; i++){
            int id = random.nextInt(sym.length());
            String temp =symUpp+sym.charAt(id);
            while (checkingPass(temp)) {
                id = random.nextInt(sym.length());
                temp =symUpp+sym.charAt(id);
            }
            symUpp+= sym.charAt(id)+"";
            old =id;
        }
        return symUpp;
    }
    public static String generateNumber(int n){
        return generateText("1234567890",n);
    }
    public static String generatePassword(int nLog,int nPass){
        int q = nLog%6;
        int kol = nPass-2-q-1;
        String text;
        text = generateText("qwertyuiopasdfghjklzxcvbnm",kol+2);
        text = text.substring(0,2).toUpperCase(Locale.ROOT) + text.substring(2,text.length());
        String pass =text+generateNumber(nPass-(kol+2));
        System.out.println("Пароль успешно сгенерирован");
        return pass;
    }
}
