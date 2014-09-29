package com.threewks.thundr.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

import com.threewks.thundr.injection.InjectionContextImpl;

public class ValidationModuleTest {

	private ValidationModule module;
	private InjectionContextImpl injectionContext;

	@Before
	public void before() throws Exception {
		module = new ValidationModule();
		injectionContext = new InjectionContextImpl();

		module.configure(injectionContext);
	}

	@Test
	public void shouldConfigureValidator() throws Exception {
		Validator validator = injectionContext.get(Validator.class);

		assertThat(validator, is(notNullValue()));
	}
}