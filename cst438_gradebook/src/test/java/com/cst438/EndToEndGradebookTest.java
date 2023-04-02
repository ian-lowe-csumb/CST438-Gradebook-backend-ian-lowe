package com.cst438;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentRepository;


@SpringBootTest
public class EndToEndGradebookTest {

	public static final String CHROME_DRIVER_FILE_LOCATION = "C:/chromedriver/chromedriver.exe";

	public static final String URL = "http://localhost:3000";
	public static final int SLEEP_DURATION = 1000; // 1 second.
	public static final String TEST_ASSIGNMENT_NAME = "Test Assignment e2e KJ2H34JHDJHFD817$$"; // random string to prevent naming collisions
	public static final String TEST_DUE_DATE = "2022-01-01";
	public static final String TEST_COURSE_ID = "123456";

	@Autowired
	AssignmentRepository assignmentRepository;

	@Test
	public void addAssignmentTest() throws Exception {
		
		// set up chromedriver
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);		
		ChromeOptions ops = new ChromeOptions();
		ops.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(ops);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		// navigate to test page
		driver.get(URL);
		Thread.sleep(SLEEP_DURATION);
		
		// click the 'ADD' button to show modal
		WebElement addAssignmentButton = driver.findElement(By.xpath("//*[@data-e2e='addAssignmentButton']"));
		addAssignmentButton.click();
		
		// input assignment name
		WebElement assignmentNameInput = driver.findElement(By.xpath("//*[@data-e2e='assignmentNameInput']"));
		assignmentNameInput.sendKeys(TEST_ASSIGNMENT_NAME);
		
		// input due date
		WebElement dueDateInput = driver.findElement(By.xpath("//*[@data-e2e='dueDateInput']"));
		dueDateInput.sendKeys(TEST_DUE_DATE);
		
		// input course id
		WebElement courseIdInput = driver.findElement(By.xpath("//*[@data-e2e='courseIdInput']"));
		courseIdInput.sendKeys(TEST_COURSE_ID);
		
		// make sure the assignment doesn't already exist in the database
		Assignment a = assignmentRepository.findByName(TEST_ASSIGNMENT_NAME).orElse(null);
		assertNull(a);
		
		// click submit button
		WebElement newAssignmentSubmitButton = driver.findElement(By.xpath("//*[@data-e2e='newAssignmentSubmitButton']"));
		newAssignmentSubmitButton.click();
		
		// confirm that assignment gets added to the database
		a = assignmentRepository.findByName(TEST_ASSIGNMENT_NAME).orElse(null);
		assertEquals(a.getName(), TEST_ASSIGNMENT_NAME);
		assertEquals(a.getDueDate().toString(), TEST_DUE_DATE);
		assertEquals(String.valueOf(a.getCourse().getCourse_id()), TEST_COURSE_ID);

		try {

		} catch (Exception ex) {
			throw ex;
		} finally {
			
			// delete assignment created by test
			if(a != null) {
				assignmentRepository.delete(a);
			}
			
			driver.close();
			driver.quit();
		}

	}
}
