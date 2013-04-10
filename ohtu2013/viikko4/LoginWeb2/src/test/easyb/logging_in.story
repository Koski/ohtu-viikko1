import ohtu.*
import ohtu.authentication.*
import org.openqa.selenium.*
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

description 'User can log in with valid username/password-combination'

scenario "user can login with correct password", {
    given 'login selected', {
        driver = new HtmlUnitDriver();
        driver.get("http://localhost:8080");
        element = driver.findElement(By.linkText("login"));       
        element.click();       
    }

    when 'a valid username and password are given', {
        element = driver.findElement(By.name("username"));
        element.sendKeys("pekka");
        element = driver.findElement(By.name("password"));
        element.sendKeys("akkep");
        element = driver.findElement(By.name("login"));
        element.submit();
    }
 
    then 'user will be logged in to system', {
        driver.getPageSource().contains("Welcome to Ohtu Application!").shouldBe true
    }
}

scenario "user can not login with incorrect password", {
    given 'command login selected', {
    	driver = new HtmlUnitDriver();
    	driver.get("http://localhost:8080");
    	element = driver.findElement(By.linkText("login"));
    	element.click();
    }
    when 'a valid username and incorrect password are given', {
        element = driver.findElement(By.name("username"));
        element.sendKeys("pekka");
        element = driver.findElement(By.name("password"));
        element.sendKeys("akk");
        element = driver.findElement(By.name("login"));
        element.submit();
    }
    then 'user will not be logged in to system', {
        driver.getPageSource().contains("Welcome to Ohtu Application!").shouldNotBe true
    }
}

scenario "nonexistent user can not login to system", {
    given 'command login selected', {
    	driver = new HtmlUnitDriver();
    	driver.get("http://localhost:8080");
    	element = driver.findElement(By.linkText("login"));
    	element.click();
    }
    when 'a nonexistent username and some password are given', {
        element = driver.findElement(By.name("username"));
        element.sendKeys("pekkamies");
        element = driver.findElement(By.name("password"));
        element.sendKeys("akkep");
        element = driver.findElement(By.name("login"));
        element.submit();
    }
    then 'user will not be logged in to system', {
        driver.getPageSource().contains("Welcome to Ohtu Application!").shouldNotBe true
    }
}

scenario "creation fails with correct username and too short password", {
    given 'command new user is selected', {
    	driver = new HtmlUnitDriver();
    	driver.get("http://localhost:8080");
    	element = driver.findElement(By.linkText("register new user"));
    	element.click();
    }
    when 'a valid username and too short password are entered', {
	element = driver.findElement(By.name("username"));
        element.sendKeys("kolli");
        
        element = driver.findElement(By.name("password"));
        element.sendKeys("ill");
        
        element = driver.findElement(By.name("passwordConfirmation"));
        element.sendKeys("ill");
        
        element = driver.findElement(By.name("add"));
        element.submit();
    }
    then 'new user is not be registered to system', {
    	driver.getPageSource().contains("Create username and give password").shouldBe true
    }
}
scenario "creation fails with correct username and pasword consisting of letters", {
    given 'command new user is selected', {
    	driver = new HtmlUnitDriver();
    	driver.get("http://localhost:8080");
    	element = driver.findElement(By.linkText("register new user"));
    	element.click();
    }
    when 'a valid username and password consisting of letters are entered', {
	element = driver.findElement(By.name("username"));
        element.sendKeys("kolli");
        
        element = driver.findElement(By.name("password"));
        element.sendKeys("illoillol");
        
        element = driver.findElement(By.name("passwordConfirmation"));
        element.sendKeys("illoillol");
        
        element = driver.findElement(By.name("add"));
        element.submit();
    }
    then 'new user is not be registered to system', {
    	driver.getPageSource().contains("must contain one character that is not a letter").shouldBe true
    }
}
scenario "creation fails with too short username and valid pasword", {
    given 'command new user is selected', {
    	driver = new HtmlUnitDriver();
    	driver.get("http://localhost:8080");
    	element = driver.findElement(By.linkText("register new user"));
    	element.click();
    }
    when 'a too sort username and valid password are entered', {
	element = driver.findElement(By.name("username"));
        element.sendKeys("ko");
        
        element = driver.findElement(By.name("password"));
        element.sendKeys("illoillol1");
        
        element = driver.findElement(By.name("passwordConfirmation"));
        element.sendKeys("illoillol1");
        
        element = driver.findElement(By.name("add"));
        element.submit();
    }
    then 'new user is not be registered to system', {
    	driver.getPageSource().contains("length 5-10").shouldBe true
    }
}
scenario "creation fails with already taken username and valid pasword", {
    given 'command new user is selected', {
    	driver = new HtmlUnitDriver();
    	driver.get("http://localhost:8080");
    	element = driver.findElement(By.linkText("register new user"));
    	element.click();
    }
    when 'a already taken username and valid password are entered', {
	element = driver.findElement(By.name("username"));
        element.sendKeys("pekka");
        
        element = driver.findElement(By.name("password"));
        element.sendKeys("illoillol1");
        
        element = driver.findElement(By.name("passwordConfirmation"));
        element.sendKeys("illoillol1");
        
        element = driver.findElement(By.name("add"));
        element.submit();
    }
    then 'new user is not be registered to system', {
    	driver.getPageSource().contains("username or password invalid").shouldBe true
    }
}

scenario "can not login with account that is not succesfully created", {
    given 'command new user is selected', {
        driver = new HtmlUnitDriver();
    	driver.get("http://localhost:8080");
    	element = driver.findElement(By.linkText("register new user"));
    	element.click();
        
        element = driver.findElement(By.name("username"));
        element.sendKeys("pek");
        
        element = driver.findElement(By.name("password"));
        element.sendKeys("illoillol1");
        
        element = driver.findElement(By.name("passwordConfirmation"));
        element.sendKeys("illoillol1");
        
        element = driver.findElement(By.name("add"));
        element.submit();
        driver.get("http://localhost:8080");
             
    }
    when 'a invalid username/password are entered', {
        element = driver.findElement(By.linkText("login"));
        element.click();

	element = driver.findElement(By.name("username"));
        element.sendKeys("pek");

        element = driver.findElement(By.name("password"));
        element.sendKeys("illoillo1");

        element = driver.findElement(By.name("login"));
        element.submit();
    }
    then  'new credentials do not allow logging in to system', {
       driver.getPageSource().contains("Welcome to Ohtu Application!").shouldNotBe true
    }
}




