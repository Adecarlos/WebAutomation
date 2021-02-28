package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TermosECondicoesPage {
	
	private WebDriver driver;
	
	private By tituloSessão = By.cssSelector("page-heading");
	
	private By primeiroSubtitulo = By.cssSelector("page-subheading");
	
	private By primeiroParagrafo = By.cssSelector(".bottom-indent:nth-of-type(1)");
			
	public TermosECondicoesPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public String obterTextoTituloSessao() {
		return driver.findElement(tituloSessão).getText();
	}
	
	public String obterTextoPrimeiroSubtitulo() {
		return driver.findElement(primeiroSubtitulo).getText();
	}
	
	public String obterTextoPrimeiroParagrafo() {
		return driver.findElement(primeiroParagrafo).getText();
	}
}
