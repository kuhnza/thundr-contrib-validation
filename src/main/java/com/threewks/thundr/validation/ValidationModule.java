package com.threewks.thundr.validation;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.threewks.thundr.injection.BaseModule;
import com.threewks.thundr.injection.UpdatableInjectionContext;

public class ValidationModule extends BaseModule {
	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		super.configure(injectionContext);

		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		injectionContext.inject(validatorFactory.getValidator()).as(Validator.class);
	}
}
