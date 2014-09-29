package com.threewks.thundr.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;

public class ConstraintViolationSetToMapTransformerTest {

	private static final ConstraintViolationSetToMapTransformer<SampleModel> ToMap = new ConstraintViolationSetToMapTransformer<>();
	private Validator validator;

	@Before
	public void before() throws Exception {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@Test
	public void shouldTransformFlatModel() throws Exception {
		SampleModel model = new SampleModel();
		Set<ConstraintViolation<SampleModel>> validationErrors = validator.validate(model);
		Map<String, Object> errorMap = ToMap.from(validationErrors);

		assertThat(errorMap, is(notNullValue()));
		assertThat(errorMap, hasEntry("stringProperty", (Object) "may not be empty"));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void shouldTransformNestedModel() throws Exception {
		SampleModel model = new SampleModel().setParent(new SampleModel());
		Set<ConstraintViolation<SampleModel>> validationErrors = validator.validate(model);
		Map<String, Object> errorMap = ToMap.from(validationErrors);

		assertThat(errorMap, is(notNullValue()));
		assertThat(errorMap, hasEntry("stringProperty", (Object) "may not be empty"));
		assertThat(errorMap.get("parent"), is(notNullValue()));
		assertThat(errorMap.get("parent"), instanceOf(Map.class));
		assertThat((Map<String, Object>) errorMap.get("parent"), hasEntry("stringProperty", (Object) "may not be empty"));
	}
}