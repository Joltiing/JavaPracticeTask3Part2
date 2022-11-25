import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageLoader {
    private final String request;
    private final int imagesCount;

    public ImageLoader(String query, int pagesNeeded) {
        this.request = query;
        this.imagesCount = pagesNeeded;
    }

    public void parseAndLoad() throws IOException {
        WebDriverManager.edgedriver().setup();
        var webDriver = new EdgeDriver();

        var url = "https://images.google.com/";
        webDriver.get(url);

        var xpathInput = "/html/body/div[1]/div[3]/form/div[1]/div[1]/div[1]/div/div[2]/input";
        var element = webDriver.findElement(By.xpath(xpathInput));
        element.sendKeys(request);
        element.sendKeys(Keys.ENTER);

        var elementsIterate=imagesCount;
        var numberImageNow = 1;

        for (int i = 1; i <= elementsIterate; i++) {
            if (i % 25 != 0) {
                elementsIterate++;
                continue;
            }
            String xpathImageElement = "//*[@id=\"islrg\"]/div[1]/div[" + i + "]/a[1]/div[1]/img";
            var imageElement = webDriver.findElement(By.xpath(xpathImageElement));

            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true)", imageElement);
            var tabs = openImageInNewTab(webDriver, imageElement);

            saveImage(webDriver, numberImageNow, request);
            numberImageNow++;

            webDriver.close();
            webDriver.switchTo().window(tabs.get(0));
        }

        webDriver.quit();
    }

    private static void saveImage(EdgeDriver webDriver, int numberImage, String query) throws IOException {
        var image = webDriver.findElement(By.tagName("img"));
        var screenshot = image.getScreenshotAs(OutputType.FILE);

        new File("out/" + query).mkdir();
        FileUtils.copyFile(screenshot, new File("out/" + query + "/" + numberImage + ".jpeg"));
    }

    private static ArrayList<String> openImageInNewTab(EdgeDriver webDriver, WebElement imageElement) {
        var link = imageElement.getAttribute("src");
        ((JavascriptExecutor)webDriver).executeScript("window.open()");

        var tabs = new ArrayList<>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(1));
        webDriver.get(link);

        return tabs;
    }
}
