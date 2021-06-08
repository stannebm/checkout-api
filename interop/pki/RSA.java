/*
# These sample codes are provided for information purposes only. It does not
imply any recommendation or endorsement by anyone. These sample codes are
provided for FREE, and no additional support will be provided for these sample
pages. There is no warranty and no additional document. USE AT YOUR OWN RISK.
*/

package pki;

import java.io.*;
import java.security.*;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;

/* Usage
 * (pki.RSA/signData ...) */

public class RSA {

  static String errorCode = "ErrorCode : [03]";
  static int cerExpiryCount = 0;
  static char[] hexChar = {'0', '1', '2', '3', '4', '5', '6', '7',
                           '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

  static { Security.addProvider(new BouncyCastleProvider()); }

  public static String signData(String pvtKeyFileName, String dataToSign,
                                String signatureAlg)
      throws IOException, NoSuchAlgorithmException, NoSuchProviderException,
             InvalidKeyException, SignatureException {

    PrivateKey privateKey = getPrivateKey(pvtKeyFileName);
    Signature signature = Signature.getInstance(signatureAlg, "BC");
    signature.initSign(privateKey);

    signature.update(dataToSign.getBytes());
    byte[] signatureBytes = signature.sign();

    return byteArrayToHexString(signatureBytes);
  }

  public static String verifyData(String pubKeyFileName, String calcCheckSum,
                                  String checkSumFromMsg, String signatureAlg)
      throws NoSuchAlgorithmException, NoSuchProviderException, IOException,
             InvalidKeySpecException, InvalidKeyException, SignatureException,
             CertificateException, NumberFormatException, ParseException {

    boolean result = false;
    try {
      ArrayList<PublicKey> pubKeys = getFPXPublicKey(pubKeyFileName);
      Signature verifier = Signature.getInstance(signatureAlg, "BC");
      System.out.println("public keys:" + pubKeys.size());
      for (PublicKey pubKey : pubKeys) {
        verifier.initVerify(pubKey);
        verifier.update(calcCheckSum.getBytes());
        result = verifier.verify(HexStringToByteArray(checkSumFromMsg));
        System.out.println("result [" + result + "]");
        if (result)
          return "00";
        else
          return "Your Data cannot be verified against the Signature. ErrorCode :[09]";
      }

      if (pubKeys.size() == 0 && cerExpiryCount == 1) {
        return "One Certificate Found and Expired. ErrorCode : [07]";
      } else if (pubKeys.size() == 0 && cerExpiryCount == 2) {
        return "Both Certificates Expired . ErrorCode : [08]";
      }
      if (pubKeys.size() == 0) {
        return "Invalid Certificates. ErrorCode : [06]";
      }
    } catch (Exception e) {

      return "ErrorCode : [03]" + e.getMessage();
    } finally {
      cerExpiryCount = 0;
    }

    return errorCode;
  }

  private static PublicKey getPublicKey(X509Certificate X509Cert) {
    return X509Cert.getPublicKey();
  }

  private static PrivateKey getPrivateKey(String pvtKeyFileName)
      throws IOException {
    FileReader pvtFileReader = getPVTKeyFile(new File(pvtKeyFileName));
    PEMReader pvtPemReader = getPvtPemReader(pvtFileReader);
    KeyPair keyPair = (KeyPair)pvtPemReader.readObject();

    pvtFileReader.close();
    pvtFileReader = null;
    pvtPemReader.close();
    pvtPemReader = null;
    return keyPair.getPrivate();
  }

  private static FileReader getPVTKeyFile(File pvtFile) {
    FileReader pvtFileReader = null;
    try {
      pvtFileReader = new FileReader(pvtFile);
    } catch (FileNotFoundException e) {

      e.printStackTrace();
      pvtFileReader = null;
    }

    return pvtFileReader;
  }

  private static PEMReader getPvtPemReader(Reader pvtFile) {
    return new PEMReader(pvtFile);
  }

  public static String byteArrayToHexString(byte[] b) {
    StringBuffer sb = new StringBuffer(b.length * 2);
    for (int i = 0; i < b.length; i++) {
      sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
      sb.append(hexChar[b[i] & 0x0f]);
    }
    return sb.toString();
  }

  public static byte[] HexStringToByteArray(String strHex) {
    byte[] bytKey = new byte[(strHex.length() / 2)];
    int y = 0;
    String strbyte;
    for (int x = 0; x < bytKey.length; x++) {
      strbyte = strHex.substring(y, (y + 2));
      if (strbyte.equals("FF")) {
        bytKey[x] = (byte)0xFF;
      } else {
        bytKey[x] = (byte)Integer.parseInt(strbyte, 16);
      }
      y = y + 2;
    }
    return bytKey;
  }

  private static X509Certificate getX509Certificate(String pubKeyFileName)
      throws CertificateException, IOException {
    InputStream inStream = new FileInputStream(pubKeyFileName);
    CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
    X509Certificate cert =
        (X509Certificate)certFactory.generateCertificate(inStream);
    inStream.close();
    return cert;
  }

  private static ArrayList<PublicKey> getFPXPublicKey(String path)
      throws CertificateException, IOException, NumberFormatException,
             ParseException {
    int year =
        Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
    ArrayList<String> certFiles = new ArrayList<String>();
    certFiles.add(path + File.separator +
                  "fpxuat_current.cer");                 // Old Certificate
    certFiles.add(path + File.separator + "fpxuat.cer"); // New Certificate
    ArrayList<PublicKey> publicKeys = null;

    for (String file : certFiles) {
      publicKeys = checkCertExpiry(file);
      System.out.println(file + "<--->" + publicKeys.size());
      if (publicKeys.size() > 0)
        return publicKeys;
    }

    return publicKeys;
  }

  private static ArrayList<PublicKey> checkCertExpiry(String file)
      throws NumberFormatException, CertificateException, IOException,
             ParseException {
    ArrayList<PublicKey> publicKey = new ArrayList<PublicKey>();
    X509Certificate x509Cert = null;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
    int renamestatus;
    try {
      x509Cert = getX509Certificate(file);
    } catch (FileNotFoundException e) {
      System.out.println("*****" + e);
      return publicKey;
    }

    Calendar currentDate = Calendar.getInstance();
    currentDate.setTime(sdf.parse(sdf.format(new Date())));

    Calendar certExpiryDate = Calendar.getInstance();
    certExpiryDate.setTime(sdf.parse(sdf.format(x509Cert.getNotAfter())));
    certExpiryDate.add(Calendar.DAY_OF_MONTH, -1);

    SimpleDateFormat settleSdf =
        new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS");

    System.out.println(settleSdf.format(certExpiryDate.getTime()) + "<-->" +
                       settleSdf.format(currentDate.getTime()));
    System.out.println(certExpiryDate.getTime() + "<-->" +
                       currentDate.getTime() + "<->" +
                       certExpiryDate.compareTo(currentDate));

    if (certExpiryDate.compareTo(currentDate) ==
        0) // cert expiry and current date is same day so check is both
    // cert
    {
      System.out.println("Same day do check with both cert");
      String nextFile = getNextCertFile(file);
      File nextFileCheck = new File(nextFile);

      if (!file.contains("fpxuat_current.cer") && nextFileCheck.exists()) {
        renamestatus = certRollOver(nextFile);
        System.out.println("renstatus [" + renamestatus + "]");
      }
      System.out.println("cert1 [" + nextFile + "] cert2[" + file + "]");
      if (nextFileCheck.exists())
        publicKey.add(getPublicKey(getX509Certificate(nextFile)));

      publicKey.add(getPublicKey(x509Cert));

    } else if (certExpiryDate.compareTo(currentDate) ==
               1) // Not Expired(Still valid)
    {
      if (file.contains("fpxuat.cer")) {

        renamestatus = certRollOver(file);
        System.out.println("renstatus [" + renamestatus + "]");
      }

      System.out.println("Still valid  [" + file + "]");
      publicKey.add(getPublicKey(x509Cert));
    } else if (certExpiryDate.compareTo(currentDate) == -1) // Expired
    {

      cerExpiryCount = cerExpiryCount + 1;

      System.out.println("Expired [" + file + "]");
    }

    return publicKey;
  }

  private static int certRollOver(String file)
      throws NumberFormatException, IOException {
    File old_crt = new File(file);

    File new_crt = new File(old_crt.getParent() + "\\fpxuat_current.cer");
    String timestamp =
        new java.text.SimpleDateFormat("yyyyMMddhmmss").format(new Date());

    File newfile =
        new File(old_crt.getParent() + "\\fpxuat_current.cer" + timestamp);

    if (new_crt.exists()) {
      // FPX_CURRENT.cer to FPX_CURRENT.cer_<CURRENT TIMESTAMP>
      System.out.println(new_crt + "old_crt is" + newfile);

      if (new_crt.renameTo(newfile)) {
        System.out.println("File renamed");
      } else {
        System.out.println("Sorry! the file can't be renamed");
        return 01;
      }
    }

    if (!new_crt.exists() && old_crt.exists()) {
      // FPX.cer to FPX_CURRENT.cer
      System.out.println(new_crt + "old_cer is" + old_crt);
      if (old_crt.renameTo(new_crt)) {

        System.out.println("File renamed");

      } else {
        System.out.println("Sorry! the file can't be renamed");
        return 01;
      }
    }

    return 00;
  }

  private static String getNextCertFile(String strFile)
      throws NumberFormatException, IOException {
    File file = new File(strFile);
    String fileName = file.getName();
    String strYear = fileName.substring(fileName.lastIndexOf(".") - 4,
                                        fileName.lastIndexOf("."));
    return file.getParentFile() + File.separator + "fpxuat.cer";
  }
}
