package {{ cookiecutter.package }}

/**
 * A placeholder object for testing Maven Central publishing.
 */
public object Placeholder {
    private const val EXPECTED = "Initial Version"

    /**
     * This is a placeholder function that returns a set string.
     */
    @JvmStatic
    public fun placeholder(): String {
        return "Placeholder: $EXPECTED"
    }
}
