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