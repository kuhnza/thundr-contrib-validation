/*
 * This file is a component of thundr, a software library from 3wks.
 * Read more: http://www.3wks.com.au/thundr
 * Copyright (C) 2013 3wks, <thundr@3wks.com.au>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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