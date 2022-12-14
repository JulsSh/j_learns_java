package ru.stqa.pft.mantis.tests;

import com.google.protobuf.ServiceException;
import org.openqa.selenium.remote.BrowserType;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.stqa.pft.mantis.ManagerApp.ApplicationManager;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class TestBase {

  protected static final ApplicationManager app = new ApplicationManager(
          System.getProperty("browser", BrowserType.FIREFOX));

  @BeforeSuite
  public void setUp() throws Exception {
    app.init();
    app.ftp().upload(new File("src/test/resources/config_inc.php"), "config_inc.php", "config_inc.php.backup");
  }

  @AfterSuite
  public void tearDown() throws Exception {
    app.ftp().restore("config_inc.php.backup", "config_inc.php");
    app.stop();
  }
  public boolean isIssueOpen(int issueId) throws MalformedURLException, ServiceException, RemoteException, javax.xml.rpc.ServiceException {
    String status = app.soap().getIssueStatus(issueId);
    return !status.equals("закрыта");
  }

  public void skipIfNotFixed(int issueId) throws RemoteException, ServiceException, MalformedURLException, javax.xml.rpc.ServiceException {
    if (isIssueOpen(issueId)) {
      throw new SkipException("Ignored because of issue " + issueId);
    }
  }

}
