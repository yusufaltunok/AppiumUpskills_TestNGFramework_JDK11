package generalstore.utils;

import com.aventstack.extentreports.Status;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;

import static generalstore.utils.Driver.*;
import static generalstore.utils.ExtentReport.*;
import static java.lang.System.getProperty;

public class Listener implements ITestListener {

    public void onStart(ITestContext context) {
        serverBaslat(getProperty("localIPAdres"), Integer.parseInt(getProperty("localPort")));
        raporOlustur();
    }

//    @Override
//    public void onStart(ITestContext context) { //Testler run edilmeden önce burada yazacağımız metot çalışır
//        raporOlustur();
//        test.info("Test başladı.");
//    }

    @Override
    public void onTestStart(ITestResult result) {
        testOlustur(result.getMethod().getMethodName()); // çalışacak metodun ismini dinamik olarak aldık.
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS,"Test başarıyla tamamlandı");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // Hata mesajı
        test.fail(result.getThrowable().getMessage());

        // Ekran görüntüsü alma
        File dosya = driver.getScreenshotAs(OutputType.FILE);
        String dosyaYolu = getProperty("User.dir") + File.separator + "raporlar" + File.separator + result.getMethod().getMethodName() + ".png";
        try {
            FileUtils.copyFile(dosya,new File(dosyaYolu));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Ekran görüntüsünü rapora ekleme
        try {
            test.addScreenCaptureFromPath(dosyaYolu);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        uygulamayiKapat();
    }

    @Override
    public void onFinish(ITestContext context) {
        uygulamayiKapat();
        raporuKaydet();
    }
}
