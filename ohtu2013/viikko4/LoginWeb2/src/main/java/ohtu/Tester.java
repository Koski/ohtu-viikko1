
package ohtu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class Tester {
    public static void main(String[] args) {
        WebDriver driver = new HtmlUnitDriver();

        driver.get("http://localhost:8080");
        WebElement create = driver.findElement(By.linkText("register new user"));
        create.click();
        System.out.println(driver.getPageSource());
        create = driver.findElement(By.name("username"));
        create.sendKeys("kolli");
        
        create = driver.findElement(By.name("password"));
        create.sendKeys("illokolli1");
        
        create = driver.findElement(By.name("passwordConfirmation"));
        create.sendKeys("illokolli1");
        
        create = driver.findElement(By.name("add"));
        create.submit();
        System.out.println(driver.getPageSource());
        System.out.println("\n=======\n");
        
        driver.get("http://localhost:8080");
        WebElement login = driver.findElement(By.linkText("login"));
        login.click();
        
        login = driver.findElement(By.name("username"));
        login.sendKeys("kolli");
        
        login = driver.findElement(By.name("password"));
        login.sendKeys("illokolli1");
        
        login = driver.findElement(By.name("login"));
        login.submit();
        System.out.println(driver.getPageSource());
//        System.out.println( driver.getPageSource() );
//        WebElement element = driver.findElement(By.linkText("login"));       
//        element.click(); 
//        
//        System.out.println("==");
//        
//        System.out.println( driver.getPageSource() );
//        element = driver.findElement(By.name("username"));
//        element.sendKeys("pekka");
//        element = driver.findElement(By.name("password"));
//        element.sendKeys("akkep");
//        element = driver.findElement(By.name("login"));
//        element.submit();
//        
//        System.out.println("==");
//        System.out.println( driver.getPageSource() );
        
    }
}
