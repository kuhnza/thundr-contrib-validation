package com.threewks.thundr.validation;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class SampleModel {

	@Valid
	private SampleModel parent;

	@NotBlank
	@Size(max = 10)
	private String stringProperty;

	public SampleModel getParent() {
		return parent;
	}

	public SampleModel setParent(SampleModel parent) {
		this.parent = parent;
		return this;
	}

	public String getStringProperty() {
		return stringProperty;
	}

	public SampleModel setStringProperty(String stringProperty) {
		this.stringProperty = stringProperty;
		return this;
	}
}
