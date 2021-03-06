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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.commons.lang3.StringUtils;

import com.atomicleopard.expressive.ETransformer;
import com.atomicleopard.expressive.Expressive;

/**
 * Takes a <code>Set<ConstraintViolation<T>></code> produced by {@link javax.validation.Validator#validate(Object, Class[])} and
 * transforms it into a serialization friendly graph of maps.
 *
 * @param <T> the bean type being validated
 */
public class ConstraintViolationSetToMapTransformer<T> implements ETransformer<Set<ConstraintViolation<T>>, Map<String, Object>> {

	private static final String PROPERTY_PATH_DELIMITER = ".";
	private static final String PROPERTY_PATH_DELIMITER_PATTERN = "\\.";

	/**
	 * Transform a set of constraint violations into a serialization friendly graph of maps. The map keys relate to fields being
	 * validated and the values are the corresponding error messages.
	 *
	 * @param constraintViolations a set of constraint violations
	 * @return a graph of <code>Map</code>s
	 */
	@Override
	public Map<String, Object> from(Set<ConstraintViolation<T>> constraintViolations) {
		Map<String, Object> errorMap = Expressive.map();
		for (ConstraintViolation<T> violation : constraintViolations) {
			putError(errorMap, violation.getPropertyPath().toString(), violation.getMessage());
		}
		return errorMap;
	}

	/**
	 * Add an error to the error map. Nested properties are put into sub maps to make for nice property style
	 * lookups in JavaScript, EL etc.
	 */
	@SuppressWarnings("unchecked")
	private void putError(Map<String, Object> errorMap, String propertyPath, String message) {
		if (propertyPath.contains(PROPERTY_PATH_DELIMITER)) {
			List<String> parts = Arrays.asList(propertyPath.split(PROPERTY_PATH_DELIMITER_PATTERN));
			String key = parts.get(0);

			Map<String, Object> subMap = (Map<String, Object>) errorMap.get(key);
			if (subMap == null) {
				subMap = Expressive.map();
				errorMap.put(key, subMap);
			}

			String subPath = StringUtils.join(parts.subList(1, parts.size()), PROPERTY_PATH_DELIMITER);
			putError(subMap, subPath, message);
			return;
		}

		errorMap.put(propertyPath, message);
	}
}
