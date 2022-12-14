package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactDeletionTest extends TestBase {
  @BeforeMethod
  public void ensurePreconditions() {
    app.contact().homePage();
    if (app.db().contact().size() == 0) {
      app.contact().homePage();
      app.contact().create(new ContactData().withUsername("username").withMiddle("middle").withLastname("lastname")
              .withComp("comp").withAddrr("addrr").withPhonenum1("03056789").withEmail1("julQjul.com"));
    }
  }
  @Test
  public void testContactDeletion() throws Exception {
    app.contact().homePage();
    Contacts before = app.db().contact();
    ContactData deleteContact = before.iterator().next();
    app.contact().delete(deleteContact);
    app.contact().homePage();

    Contacts after = app.db().contact();
   // assertEquals(after.size(), before.size()-1);
    assertThat(after, equalTo(before));
  }


}
