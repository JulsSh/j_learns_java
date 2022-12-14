package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.List;

public class ContactHelper extends HelperBase {
  public boolean acceptNextAlert = true;


  public ContactHelper(WebDriver wd) {
    super(wd);
  }

  public void submitContactCreation() {
    click(By.xpath("//input[@name='submit']"));
  }

  public void fillContactDetails(ContactData contactData, boolean CreateGroup) {


    type(By.name("firstname"), contactData.getUsername());
    type(By.name("middlename"), contactData.getMiddle());
    type(By.name("lastname"), contactData.getLastname());
    type(By.name("company"), contactData.getComp());
    type(By.name("address"), contactData.getAddrr());
    type(By.name("home"), contactData.getPhonenum1());
    type(By.name("mobile"), contactData.getPhonenum2());
    type(By.name("work"), contactData.getPhonenum3());
    type(By.name("email"), contactData.getEmail1());
    type(By.name("email2"), contactData.getEmail2());
    type(By.name("email3"), contactData.getEmail3());
    //attach(By.name("photo"), contactData.getPhoto());
    if (CreateGroup) {
      if (contactData.getGroups().size() > 0) {
        Assert.assertTrue(contactData.getGroups().size() == 1);
        new Select(wd.findElement(By.name("new_group"))).selectByVisibleText(contactData.getGroups().iterator().next().getGroupName());
      }

    } else {
      Assert.assertFalse(isElementPresent(By.name("new_group")));
    }
    //new Select(wd.findElement(By.name("new group"))).selectByVisibleText(contactData.getGroup());
  }

  public void delete() {
    click(By.xpath("//input[@value='Delete']"));
  }

  public void selectContactbyId(int id) {
    wd.findElement(By.cssSelector("input[value='" + id + "']")).click();
  }

  public void initModification(int index) {
    wd.findElement(By.xpath("//img[@alt='Edit']")).click();
  }

  public String closeAlertAndGetItsText() {
    try {
      Alert alert = wd.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }


  public void initContactCreation() {
    click(By.xpath("//a[contains(text(),'add new')]"));
  }

  public void submitContactModification() {
    click(By.xpath("//input[@value='Update']"));
  }

  public void returnToHomePage() {
    wd.findElement(By.linkText("home page")).click();
  }

  public void homePage() {
    if (isElementPresent(By.id("maintable"))) {
      return;
    }
    click(By.linkText("home"));
  }

  public void create(ContactData contact) {
    initContactCreation();
    fillContactDetails(contact, true);
    submitContactCreation();
    homePage();
  }

  public void modify(ContactData contact) {
    initModification(contact.getId());
    fillContactDetails(contact, false);
    submitContactModification();
    returnToHomePage();
  }


  public void delete(ContactData contact) {
    homePage();
    selectContactbyId(contact.getId());
    delete();
    acceptNextAlert = true;
    closeAlertAndGetItsText().matches("^Delete 1 addresses[\\s\\S]$");
    homePage();
  }


  public Contacts all() {
    Contacts contacts = new Contacts();
    List<WebElement> elements = wd.findElements(By.cssSelector("tr[name=entry]"));
    for (WebElement element : elements) {
      String username = element.findElement(By.xpath(".//td[3]")).getText();
      String lastname = element.findElement(By.xpath(".//td[2]")).getText();
      String addr = element.findElement(By.xpath(".//td[4]")).getText();
      String allphones = element.findElement(By.xpath(".//td[6]")).getText();
      String allemails = element.findElement(By.xpath(".//td[5]")).getText();
      int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value"));
      contacts.add(new ContactData().withId(id).withUsername(username).withLastname(lastname).withAddrr(addr)
              .withAllPhones(allphones).setAllEmails(allemails));
    }
    return contacts;
  }

  public ContactData infoFromEditForm(ContactData contact) {
    initContactModificationById(contact.getId());
    String username = wd.findElement(By.name("firstname")).getAttribute("value");
    String middlename = wd.findElement(By.name("middle")).getAttribute("value");
    String lastname = wd.findElement(By.name("lastname")).getAttribute("value");
    String company = wd.findElement(By.name("company")).getAttribute("value");
    String addr = wd.findElement(By.name("address")).getAttribute("value");
    String phone1home = wd.findElement(By.name("home")).getAttribute("value");
    String phone2mobile = wd.findElement(By.name("mobile")).getAttribute("value");
    String phone3work = wd.findElement(By.name("work")).getAttribute("value");
    String phone4Homesec = wd.findElement(By.name("phone2")).getAttribute("value");
    String email1 = wd.findElement(By.name("email")).getAttribute("value");
    String email2 = wd.findElement(By.name("email2")).getAttribute("value");
    String email3 = wd.findElement(By.name("email3")).getAttribute("value");
    wd.navigate().back();
    return new ContactData().withId(contact.getId()).withUsername(username).withMiddle(middlename).withLastname(lastname)
            .withAddrr(addr).withComp(company)
            .withPhonenum1(phone1home).withPhonenum2(phone2mobile).withPhonenum3(phone3work)
            .withSec_phone4(phone4Homesec).withEmail1(email1).withEmail2(email2).withEmail3(email3);
  }

  public void initContactModificationById(int id) {
    WebElement checkbox = wd.findElement(By.cssSelector(String.format("input[value='%s'", id)));
    WebElement row = checkbox.findElement(By.xpath("./../.."));
    List<WebElement> cells = row.findElements(By.tagName("td"));
    cells.get(7).findElement(By.tagName("a")).click();
    // wd.findElement(By.xpath(String.format("//input[value='%s']/../..td[8]/a", id))).click();
    //wd.findElement(By.xpath(String.format("//tr[.//input[value='%s']/td[8]/a", id))).click();
    //wd.findElement(By.cssSelector(String.format("a[href='edit.php?id=%s']", id))).click();
  }

  public int count() {
    return wd.findElements(By.name("selected[]")).size();
  }

  public void setGroupForContact(String nameGroup) {
    click(By.name("to_group"));
    new Select(wd.findElement(By.name("to_group"))).selectByVisibleText(nameGroup);
  }


  public void selectGroupforRemove(GroupData selectGroup) {

    new Select(wd.findElement(By.name("group"))).selectByValue(String.valueOf(selectGroup.getId()));
  }


  public void addInGroup() {
    click(By.name("add"));
  }

  public void removeFromGroup() {
    click(By.name("remove"));
  }

  public void clickAllGroup(String name) {

    new Select(wd.findElement(By.name("group"))).selectByVisibleText(name);
  }

  public void submitAddContactToGroup() {
    wd.findElement(By.name("add")).click();
  }

  public void clickGroupToAdd(GroupData selectGroup) {
    new Select(wd.findElement(By.name("to_group"))).selectByValue(String.valueOf(selectGroup.getId()));
  }

  public void submitRemoveContactFromGroup() {
    wd.findElement(By.name("remove")).click();
  }

  public ContactData selectContactToAdd(GroupData selectGroup, Contacts contacts) {

    ContactData selectContact = null;
    if (selectGroup.getContacts().size() == 0) {
      selectContact = contacts.stream().iterator().next();
      selectContactbyId(selectContact.getId());
      clickGroupToAdd(selectGroup);
      submitAddContactToGroup();
    } else {
      selectContact = selectGroup.getContacts().stream().iterator().next();
    }
    return selectContact;

  }

  public void clickGroup(GroupData selectGroup) {
    new Select(wd.findElement(By.name("group"))).selectByValue(String.valueOf(selectGroup.getId()));

  }

}