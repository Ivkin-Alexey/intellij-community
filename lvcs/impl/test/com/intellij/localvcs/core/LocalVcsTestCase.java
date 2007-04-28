package com.intellij.localvcs.core;

import com.intellij.localvcs.core.changes.Change;
import com.intellij.localvcs.core.changes.ChangeSet;
import com.intellij.localvcs.core.storage.ByteContent;
import com.intellij.localvcs.core.storage.Content;
import com.intellij.localvcs.integration.Clock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public abstract class LocalVcsTestCase extends Assert {
  private Locale myDefaultLocale;

  @Before
  public void setUpLocale() {
    myDefaultLocale = Locale.getDefault();
    Locale.setDefault(new Locale("ru", "RU"));
  }

  @After
  public void restoreLocale() {
    Locale.setDefault(myDefaultLocale);
  }

  protected static Content c(String data) {
    return new ByteContent(b(data));
  }

  protected static byte[] b(String data) {
    return data == null ? null : data.getBytes();
  }

  protected static <T> T[] a(T... objects) {
    return objects;
  }

  protected static IdPath idp(int... parts) {
    return new IdPath(parts);
  }

  protected static ChangeSet cs(Change... changes) {
    return cs(null, changes);
  }

  protected static ChangeSet cs(String name, Change... changes) {
    return cs(0, name, changes);
  }

  protected static ChangeSet cs(long timestamp, Change... changes) {
    return cs(timestamp, null, changes);
  }

  protected static ChangeSet cs(long timestamp, String name, Change... changes) {
    return new ChangeSet(timestamp, name, Arrays.asList(changes));
  }

  protected void setCurrentTimestamp(long t) {
    Clock.setCurrentTimestamp(t);
  }

  protected static void assertEquals(Object[] expected, Collection actual) {
    List<Object> expectedList = Arrays.asList(expected);
    String message = notEqualsMessage(expectedList, actual);

    assertTrue(message, expectedList.size() == actual.size());
    assertTrue(message, actual.containsAll(expectedList));
  }

  protected static void assertEquals(byte[] expected, byte[] actual) {
    String message = notEqualsMessage(expected, actual);

    assertTrue(message, expected.length == actual.length);
    for (int i = 0; i < expected.length; i++) {
      assertTrue(message, expected[i] == actual[i]);
    }
  }

  protected void assertEquals(long expected, long actual) {
    assertEquals((Object)expected, (Object)actual);
  }

  private static String notEqualsMessage(Object expected, Object actual) {
    return "elements are not equal:\n" + "\texpected: " + expected + "\n" + "\tactual: " + actual;
  }
}
