package Core;

public class Pair<T>
{
	// ===================================================================
	
	private T	key,
				value;

	// ===================================================================
	
	public Pair(T key, T value)
	{
		this.key = key;
		this.value = value;
	}

	// ===================================================================

	public T getKey()
	{
		return this.key;
	}
	
	public void setKey(T key)
	{
		this.key = key;
	}

	// ===================================================================
	
	public T getValue()
	{
		return this.value;
	}
	
	public void setValue(T value)
	{
		this.value = value;
	}
	
	// ===================================================================
}
