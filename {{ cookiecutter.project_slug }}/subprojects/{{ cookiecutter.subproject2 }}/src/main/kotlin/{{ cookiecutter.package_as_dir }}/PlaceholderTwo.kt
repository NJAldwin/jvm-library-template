package {{ cookiecutter.package }}

/**
 * A placeholder object for testing Maven Central publishing.
 */
public object PlaceholderTwo {
    private const val EXPECTED = "Initial Version (2)"

    /**
     * This is a placeholder function that returns a set string.
     */
    @JvmStatic
    public fun placeholder(): String {
        return "Placeholder: $EXPECTED"
    }

    /**
     * This is a placeholder function that returns the original placeholder string from [Placeholder].
     */
    @JvmStatic
    public fun originalPlaceholder(): String {
        return Placeholder.placeholder()
    }
}
