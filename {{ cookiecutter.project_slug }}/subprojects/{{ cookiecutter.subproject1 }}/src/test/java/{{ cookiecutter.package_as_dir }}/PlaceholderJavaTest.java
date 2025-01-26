package {{ cookiecutter.package }};

import org.junit.jupiter.api.Test;

public class PlaceholderJavaTest {

  @Test
  public void itReturnsTheExpectedInformation() {
    assert "Placeholder: Initial Version".equals(Placeholder.placeholder());
  }

  @SuppressWarnings("AccessStaticViaInstance")
  @Test
  public void itReturnsTheExpectedInformationUsingInstance() {
    assert "Placeholder: Initial Version".equals(Placeholder.INSTANCE.placeholder());
  }
}
