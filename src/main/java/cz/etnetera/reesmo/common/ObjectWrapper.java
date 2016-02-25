package cz.etnetera.reesmo.common;

public class ObjectWrapper<T> {

	private T value;

	public ObjectWrapper() {
	}

	public ObjectWrapper(T value) {
		this();
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

}
