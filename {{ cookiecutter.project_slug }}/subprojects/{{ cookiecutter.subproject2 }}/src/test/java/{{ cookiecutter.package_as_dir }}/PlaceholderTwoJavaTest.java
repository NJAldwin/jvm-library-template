package {{ cookiecutter.package }};

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PlaceholderTwoJavaTest {
  @Test
  public void itReturnsTheExpectedInformation() {
    assertEquals("Placeholder: Initial Version (2)", PlaceholderTwo.placeholder());
  }

  @SuppressWarnings("AccessStaticViaInstance")
  @Test
  public void itReturnsTheExpectedInformationUsingInstance() {
    assertEquals("Placeholder: Initial Version (2)", PlaceholderTwo.INSTANCE.placeholder());
  }

  @Test
  public void itReturnsTheExpectedInformationFromTheOriginalPlaceholder() {
    assertEquals("Placeholder: Initial Version", PlaceholderTwo.originalPlaceholder());
  }

  @SuppressWarnings("AccessStaticViaInstance")
  @Test
  public void itReturnsTheExpectedInformationFromTheOriginalPlaceholderUsingInstance() {
    assertEquals("Placeholder: Initial Version", PlaceholderTwo.INSTANCE.originalPlaceholder());
  }
}
